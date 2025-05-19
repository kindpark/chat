package com.creative.mart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CartItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cart_item_id")
	private int cartItemId;
	@ManyToOne
	@JoinColumn(name="cart_id")
	private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable=false)
    private Product product;
    
    private int quantity;
    private String custom;
}
