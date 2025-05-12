package com.mijaelsegura.eCommerceSpring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MapsId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Data
public class IdCartItem implements Serializable {

    @Column(name = "cart_id")
    private long cartId;
    @Column(name = "supplement_id")
    private long supplementId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdCartItem that = (IdCartItem) o;
        return cartId == that.cartId && supplementId == that.supplementId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, supplementId);
    }
}
