package com.mijaelsegura.eCommerceSpring.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "supplements")
public class Supplement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "unitary_price")
    private BigDecimal unitaryPrice;
    @Column(name = "unitary_cost")
    private BigDecimal unitaryCost;
    @Column(name = "image_name")
    private String imageName;
    @Column(name = "image_type")
    private String imageType;
    @Column(name = "image_data")
    @Lob
    private byte[] imageData;

    @OneToMany(mappedBy = "supplement", cascade = CascadeType.REMOVE)
    private List<PurchaseDetail> purchaseDetails;

    public Supplement(String name, String description, BigDecimal unitaryPrice, BigDecimal unitaryCost) {
        this.name = name;
        this.description = description;
        this.unitaryPrice = unitaryPrice;
        this.unitaryCost = unitaryCost;
    }

}
