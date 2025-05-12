package com.mijaelsegura.eCommerceSpring.repositories;

import com.mijaelsegura.eCommerceSpring.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPurchaseRepository extends JpaRepository<Purchase, Long> {

}
