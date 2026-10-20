package com.FieldServiceManagement.DTO;


import jakarta.validation.constraints.*;

public class UsePartDTO {

    @NotNull
    @Min(1)
    private Long partId;

    @NotNull
    @Min(1)
    private Integer quantity;

    private String note;

    public Long getPartId() { return partId; }
    public void setPartId(Long v) { partId = v; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer v) { quantity = v; }
    public String getNote() { return note; }
    public void setNote(String v) { note = v; }
}