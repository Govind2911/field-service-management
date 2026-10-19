
package com.FieldServiceManagement.DTO;

import com.FieldServiceManagement.ENUM.WorkOrderStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkOrderStatusUpdateDTO {

    @NotNull
    private WorkOrderStatus status;

    @Size(max = 500)
    private String remarks;

    public WorkOrderStatusUpdateDTO() {
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
