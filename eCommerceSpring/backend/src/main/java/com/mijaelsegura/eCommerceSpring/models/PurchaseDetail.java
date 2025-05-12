package com.mijaelsegura.eCommerceSpring.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "purchase_detail")
public class PurchaseDetail implements Serializable {
    @EmbeddedId
    private IdPurchaseDetail id;
    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("purchaseId")
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("supplementId")
    @JoinColumn(name = "supplement_id", referencedColumnName = "id")
    private Supplement supplement;

}
