
package com.FieldServiceManagement.DTO;

import jakarta.validation.constraints.NotNull;

public class AssignTechnicianDTO {

    @NotNull
    private Long technicianId;

    public AssignTechnicianDTO() {
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }
}
