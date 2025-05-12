package com.mijaelsegura.eCommerceSpring.repositories;

import com.mijaelsegura.eCommerceSpring.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.DNI = :dni")
    Optional<Cart> findByUserDni(long dni); // puede hacerse con navegación aclarando UserDni en lugar de Dni en la firma, o por JPQL con @Query
}
