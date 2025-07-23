package com.mijaelsegura.eCommerceSpring.repositories;

import com.mijaelsegura.eCommerceSpring.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("from Purchase p where p.user.DNI = :dni")
    List<Purchase> findPurchasesByDni(long dni);
}
