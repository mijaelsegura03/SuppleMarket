package com.mijaelsegura.eCommerceSpring.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseDetail> purchaseDetails;
    @ManyToOne
    @JoinColumn(name = "user_dni", referencedColumnName = "dni")
    private User user;


}
