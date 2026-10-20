package com.FieldServiceManagement.DTO;

import jakarta.validation.constraints.*;

public class CreateTimeLogDTO {

    @NotNull
    @Min(1)
    @Max(1440)
    private Integer minutes;

    private String note;

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer v) { minutes = v; }
    public String getNote() { return note; }
    public void setNote(String v) { note = v; }
}