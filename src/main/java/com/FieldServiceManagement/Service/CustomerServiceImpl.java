package com.FieldServiceManagement.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FieldServiceManagement.Entity.Customer;
import com.FieldServiceManagement.Repository.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class CustomerServiceImpl implements CustomerService {
 
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Override
	public Customer createCustomer(Customer customer) {
		if(customerRepo.existsByEmail(customer.getEmail())) {
			throw new RuntimeException("Customer already exixts");
		}
		customer.setActive(true);
		customer.setCreatedAt(LocalDateTime.now());
		
		return customerRepo.save(customer);
	}
	
	@Override
	public Customer updateCustomer(String email,Customer customer) {
		Customer existingCustomer = customerRepo.findByEmail(email)
				.orElseThrow(()-> new RuntimeException("Customer not found"));
				
				existingCustomer.setCompanyName(customer.getCompanyName());
				existingCustomer.setContactPerson(customer.getContactPerson());
				existingCustomer.setPhone(customer.getPhone());
				existingCustomer.setAddress(customer.getAddress());
				existingCustomer.setActive(customer.isActive());
				
				return customerRepo.save(existingCustomer);
	}
	@Override
	public Customer getCustomer(Long id) {
		return customerRepo.findById(id)
				.orElseThrow(()-> new RuntimeException("Customer not found"));
	}
	@Override
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User not Found"));
	}
	@Override
	public List<Customer>getAllCustomer(){
		return customerRepo.findAll();
	}
	@Override
	public void deleteCustomer(Long id) {
		Customer customer = customerRepo.findById(id)
				.orElseThrow(()-> new RuntimeException("Customer not found"));
		    customerRepo.delete(customer);
	}
}



