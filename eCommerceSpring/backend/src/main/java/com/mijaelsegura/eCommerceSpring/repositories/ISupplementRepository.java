package com.mijaelsegura.eCommerceSpring.repositories;

import com.mijaelsegura.eCommerceSpring.models.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupplementRepository extends JpaRepository<Supplement, Long> {
}
