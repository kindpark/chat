package com.creative.mart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creative.mart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
