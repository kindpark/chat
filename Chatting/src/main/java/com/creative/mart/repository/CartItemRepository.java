package com.creative.mart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creative.mart.model.Cart;
import com.creative.mart.model.CartItem;
import com.creative.mart.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
	
	List<CartItem> findByCartId(int cartId);
}
