package com.FieldServiceManagement.DTO;


import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class CreatePartDTO {

    @NotBlank
    private String partNumber;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal unitCost;

    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String v) { partNumber = v; }
    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description = v; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer v) { stockQuantity = v; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal v) { unitCost = v; }
}