package com.mijaelsegura.eCommerceSpring.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
public class CartItem {
    @EmbeddedId
    private IdCartItem id;
    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @MapsId("cartId") // SI NO PONGO ESTE MAPSID TOMA COMO SI ESTUVIESE DUPLICADA LA COLUMNA CART_ID POR EL OBJETO CART Y EL ID DE IDCARDITEM
    //PONIENDOLO ASÍ ESTOY INDICANDO QUE ESTE ID ES EL MISMO DEL DE IDCARTITEM
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
    @ManyToOne
    @MapsId("supplementId") // LO MISMO PASA ACA
    @JoinColumn(name = "supplement_id", referencedColumnName = "id")
    private Supplement supplement;
}
