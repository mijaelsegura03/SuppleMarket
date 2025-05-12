package com.mijaelsegura.eCommerceSpring.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class IdPurchaseDetail implements Serializable {
    @Column(name = "purchase_id")
    private long purchaseId;
    @Column(name = "supplement_id")
    private long supplementId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdPurchaseDetail that = (IdPurchaseDetail) o;
        return purchaseId == that.purchaseId && supplementId == that.supplementId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, supplementId);
    }

}
