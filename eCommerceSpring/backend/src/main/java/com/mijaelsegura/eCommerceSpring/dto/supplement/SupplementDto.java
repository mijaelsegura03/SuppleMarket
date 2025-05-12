package com.mijaelsegura.eCommerceSpring.dto.supplement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplementDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal unitaryPrice;
    private BigDecimal unitaryCost;

    public SupplementDto(String name, String description, BigDecimal unitaryPrice, BigDecimal unitaryCost) {
        this.name = name;
        this.description = description;
        this.unitaryPrice = unitaryPrice;
        this.unitaryCost = unitaryCost;
    }

    public String validateAllProperties() {
        if (name == null || name.isBlank()) {
            return "Supplement name is required.";
        } else if (description == null || description.isBlank()) {
            return "Supplement description is required.";
        } else if (unitaryPrice == null || unitaryPrice.equals(BigDecimal.ZERO)) {
            return "Supplement unitary price is required.";
        } else if (unitaryCost == null || unitaryCost.equals(BigDecimal.ZERO)) {
            return "Supplement unitary cost is required.";
        }
        return "";
    }
}
