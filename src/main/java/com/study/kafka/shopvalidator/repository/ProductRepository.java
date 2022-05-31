package com.study.kafka.shopvalidator.repository;

import com.study.kafka.shopvalidator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByIdentifier(String identifier);
}
