package com.mijaelsegura.eCommerceSpring.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_dni", referencedColumnName = "dni")
    @JsonBackReference
    private User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) //el orphanRemoval es para que si saco un item de esta lista se borre también su registro en la BD
    private List<CartItem> cartItems;
}
