package com.FieldServiceManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FieldServiceManagement.DTO.AssignTechnicianDTO;
import com.FieldServiceManagement.DTO.CreateWorkOrderDTO;
import com.FieldServiceManagement.DTO.WorkOrderHistoryResponseDTO;
import com.FieldServiceManagement.DTO.WorkOrderResponseDTO;
import com.FieldServiceManagement.DTO.WorkOrderStatusUpdateDTO;
import com.FieldServiceManagement.Entity.Customer;
import com.FieldServiceManagement.Entity.Site;
import com.FieldServiceManagement.Entity.UserAuth;
import com.FieldServiceManagement.Entity.WorkOrder;
import com.FieldServiceManagement.Entity.WorkOrderStatusHistory;
import com.FieldServiceManagement.ENUM.Role;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;
import com.FieldServiceManagement.Repository.CustomerRepository;
import com.FieldServiceManagement.Repository.SiteRepository;
import com.FieldServiceManagement.Repository.UserAuthRepository;
import com.FieldServiceManagement.Repository.WorkOrderRepository;
import com.FieldServiceManagement.Repository.WorkOrderStatusHistoryRepository;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {
 
	private final WorkOrderRepository workOrderRepository;
    private final WorkOrderStatusHistoryRepository historyRepository;
    private final CustomerRepository customerRepository;
    private final SiteRepository siteRepository;
    private final UserAuthRepository userAuthRepository;
    
    
	public WorkOrderServiceImpl(WorkOrderRepository workOrderRepository,
			WorkOrderStatusHistoryRepository historyRepository, CustomerRepository customerRepository,
			SiteRepository siteRepository, UserAuthRepository userAuthRepository) {
		
		this.workOrderRepository = workOrderRepository;
		this.historyRepository = historyRepository;
		this.customerRepository = customerRepository;
		this.siteRepository = siteRepository;
		this.userAuthRepository = userAuthRepository;
	}
    
	 private UserAuth getUser(String email)
	 {
	        return userAuthRepository.findByUserEmail(email)
	                .orElseThrow(() ->
	                        new IllegalArgumentException(
	                                "Authenticated user not found"));
	  }

	    @Transactional
	    public WorkOrderResponseDTO create(
	            CreateWorkOrderDTO dto, String email) 
	    {

	        UserAuth creator = getUser(email);

	        if (creator.getRole() != Role.MANAGER
	                && creator.getRole() != Role.DISPATCHER) {
	            throw new IllegalArgumentException(
	                    "Only MANAGER or DISPATCHER can create work orders");
	        }

	        Customer customer = customerRepository
	                .findById(dto.getCustomerId())
	                .orElseThrow(() ->
	                        new IllegalArgumentException("Customer not found"));

	        Site site = siteRepository.findById(dto.getSiteId())
	                .orElseThrow(() ->
	                        new IllegalArgumentException("Site not found"));

	        if (site.getCustomer() == null
	                || !site.getCustomer().getId().equals(customer.getId())) {
	            throw new IllegalArgumentException(
	                    "Selected site does not belong to this customer");
	        }

	        WorkOrder wo = new WorkOrder();
	        wo.setTitle(dto.getTitle().trim());
	        wo.setDescription(dto.getDescription().trim());
	        wo.setPriority(dto.getPriority() == null
	                ? com.FieldServiceManagement.ENUM.WorkOrderPriority.MEDIUM
	                : dto.getPriority());
	        wo.setCustomer(customer);
	        wo.setSite(site);
	        wo.setCreatedBy(creator);
	        wo.setScheduledAt(dto.getScheduledAt());
	        wo.setStatus(WorkOrderStatus.OPEN);

	        WorkOrder saved = workOrderRepository.save(wo);

	        saveHistory(saved, null, WorkOrderStatus.OPEN,
	                creator, "Work order created");

	        return WorkOrderResponseDTO.from(saved);
	    }

	    
	    @Transactional(readOnly = true)
	    public List<WorkOrderResponseDTO> getAll() 
	    {
	        return workOrderRepository.findAll()
	                .stream()
	                .map(WorkOrderResponseDTO::from)
	                .toList();
	    }
	    

	    @Transactional(readOnly = true)
	    public WorkOrderResponseDTO getById(Long id) {
	        return WorkOrderResponseDTO.from(getWorkOrder(id));
	    }

	    
	    @Transactional
	    public WorkOrderResponseDTO assign(
	            Long workOrderId,
	            AssignTechnicianDTO dto,
	            String email) {

	        UserAuth actor = getUser(email);

	        if (actor.getRole() != Role.MANAGER
	                && actor.getRole() != Role.DISPATCHER) {
	            throw new IllegalArgumentException(
	                    "Only MANAGER or DISPATCHER can assign work orders");
	        }

	        WorkOrder wo = getWorkOrder(workOrderId);

	        if (wo.getStatus() == WorkOrderStatus.CLOSED
	                || wo.getStatus() == WorkOrderStatus.CANCELLED
	                || wo.getStatus() == WorkOrderStatus.COMPLETED) {
	            throw new IllegalArgumentException(
	                    "Cannot assign a completed, closed or cancelled work order");
	        }

	        UserAuth technician = userAuthRepository.findById(dto.getTechnicianId())
	                .orElseThrow(() ->
	                        new IllegalArgumentException("Technician not found"));

	        if (technician.getRole() != Role.TECHNICIAN) {
	            throw new IllegalArgumentException(
	                    "Selected user is not a TECHNICIAN");
	        }

	        WorkOrderStatus oldStatus = wo.getStatus();
	        wo.setAssignedTechnician(technician);
	        wo.setStatus(WorkOrderStatus.ASSIGNED);

	        WorkOrder saved = workOrderRepository.save(wo);

	        saveHistory(saved, oldStatus, WorkOrderStatus.ASSIGNED,
	                actor, "Assigned to " + technician.getUserName());

	        return WorkOrderResponseDTO.from(saved);
	    }
	    

	    @Transactional
	    public WorkOrderResponseDTO updateStatus(
	            Long workOrderId,
	            WorkOrderStatusUpdateDTO dto,
	            String email) {

	        UserAuth actor = getUser(email);
	        WorkOrder wo = getWorkOrder(workOrderId);

	        WorkOrderStatus oldStatus = wo.getStatus();
	        WorkOrderStatus newStatus = dto.getStatus();

	        if (oldStatus == WorkOrderStatus.CLOSED
	                || oldStatus == WorkOrderStatus.CANCELLED) {
	            throw new IllegalArgumentException(
	                    "Closed or cancelled work orders cannot be changed");
	        }

	        if (actor.getRole() == Role.TECHNICIAN) {
	            if (wo.getAssignedTechnician() == null
	                    || !wo.getAssignedTechnician().getId()
	                            .equals(actor.getId())) {
	                throw new IllegalArgumentException(
	                        "You can update only work orders assigned to you");
	            }

	            if (newStatus != WorkOrderStatus.IN_PROGRESS
	                    && newStatus != WorkOrderStatus.ON_HOLD
	                    && newStatus != WorkOrderStatus.COMPLETED) {
	                throw new IllegalArgumentException(
	                        "Technicians cannot set this status");
	            }
	        } else if (actor.getRole() != Role.MANAGER
	                && actor.getRole() != Role.DISPATCHER) {
	            throw new IllegalArgumentException(
	                    "You are not allowed to update work order status");
	        }

	        if (!isValidTransition(oldStatus, newStatus)) {
	            throw new IllegalArgumentException(
	                    "Invalid status transition: " + oldStatus
	                            + " -> " + newStatus);
	        }

	        wo.setStatus(newStatus);

	        if (newStatus == WorkOrderStatus.IN_PROGRESS
	                && wo.getStartedAt() == null) {
	            wo.setStartedAt(java.time.LocalDateTime.now());
	        }

	        if (newStatus == WorkOrderStatus.COMPLETED) {
	            wo.setCompletedAt(java.time.LocalDateTime.now());
	        }

	        WorkOrder saved = workOrderRepository.save(wo);

	        saveHistory(saved, oldStatus, newStatus, actor, dto.getRemarks());

	        return WorkOrderResponseDTO.from(saved);
	    }
	    


  @Transactional(readOnly = true)
  @Override
   public List<WorkOrderHistoryResponseDTO> getHistory(Long workOrderId) {
    getWorkOrder(workOrderId);

    return historyRepository
            .findByWorkOrderIdOrderByChangedAtDesc(workOrderId)
            .stream()
            .map(WorkOrderHistoryResponseDTO::from)
            .toList();
}


	    private WorkOrder getWorkOrder(Long id) {
	        return workOrderRepository.findById(id)
	                .orElseThrow(() ->
	                        new IllegalArgumentException("Work order not found"));
	    }
	    
	    private void saveHistory(
	            WorkOrder wo,
	            WorkOrderStatus oldStatus,
	            WorkOrderStatus newStatus,
	            UserAuth actor,
	            String remarks) {

	        WorkOrderStatusHistory history = new WorkOrderStatusHistory();
	        history.setWorkOrder(wo);
	        history.setPreviousStatus(oldStatus);
	        history.setNewStatus(newStatus);
	        history.setChangedBy(actor);
	        history.setRemarks(remarks);

	        historyRepository.save(history);
	    }

	    private boolean isValidTransition(
	            WorkOrderStatus from, WorkOrderStatus to) {

	        if (from == to) {
	            return false;
	        }

	        return switch (from) {
	            case OPEN ->
	                    to == WorkOrderStatus.ASSIGNED
	                    || to == WorkOrderStatus.CANCELLED;

	            case ASSIGNED ->
	                    to == WorkOrderStatus.IN_PROGRESS
	                    || to == WorkOrderStatus.CANCELLED;

	            case IN_PROGRESS ->
	                    to == WorkOrderStatus.ON_HOLD
	                    || to == WorkOrderStatus.COMPLETED;

	            case ON_HOLD ->
	                    to == WorkOrderStatus.IN_PROGRESS
	                    || to == WorkOrderStatus.CANCELLED;

	            case COMPLETED ->
	                    to == WorkOrderStatus.CLOSED;

	            case CLOSED, CANCELLED -> false;
	        };
	    }
    
}
