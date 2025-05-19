package com.creative.mart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creative.mart.model.Cart;
import com.creative.mart.model.CartItem;
import com.creative.mart.model.Product;
import com.creative.mart.repository.CartItemRepository;
import com.creative.mart.repository.CartRepository;
import com.creative.mart.repository.ProductRepository;
import com.creative.user.model.User;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    public Cart getOrCreateCart(User user) {
    	return cartRepository.findByUserId(user.getId())
		.orElseGet(()->{
			Cart inCart = new Cart();
			inCart.setUser(user);
			return cartRepository.save(inCart);
		});
    }
    @Transactional
    public void addProductToCart(User user, int productId, int quantity, String custom) {
        /*
    	Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        //개수 겹치는거 방지
        Optional<Cart> existingCart = cartRepository.findBySessionidAndProduct(sessionId, product);
        if (existingCart.isPresent()) {
            
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + 1);
            cartRepository.save(cart);
        } else {
            Cart cart = new Cart();
            cart.setSessionid(sessionId);
            cart.setProduct(product);
            cart.setQuantity(1);
            cartRepository.save(cart);
        }
        */
    	Cart cart = getOrCreateCart(user);
    	Product product = productRepository.findById(productId)
    			.orElseThrow(() -> new IllegalArgumentException("상품 없음"));
    	
    	Optional<CartItem> existItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existItem.isPresent()) {
            CartItem item = existItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setCustom(custom);
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setCustom(custom);
            cartItemRepository.save(item);
        }
    }	

    // 장바구니 상품 제거
    public void removeProductFromCart(int cartId) {
        cartRepository.deleteById(cartId);
    }
    //html
    /*
    // 장바구니 목록 가져오기
    public List<CartItem> getCartItems() {
        return cartItemRepository.findAll();
    }
    */
    //json
    // 특정 sessionId 기준 장바구니 목록 조회
    public List<CartItem> getCartItems(User user) {
        Cart cart = getOrCreateCart(user);
                
        if (cart == null) return List.of();
        return cartItemRepository.findByCartId(cart.getId());
    }
    public void updateCartQuantity(int cartId, int quantity) {
        Optional<CartItem> cartitem = cartItemRepository.findById(cartId);
        if (cartitem.isPresent()) {
            CartItem existingCart = cartitem.get();
            existingCart.setQuantity(quantity);
            cartItemRepository.save(existingCart);
        }
    }
    public void updateProductSweetness(int cartId, int newSweetness) {
        CartItem cartitem = cartItemRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Invalid cart id"));
        Product product = cartitem.getProduct();
        product.setSweetness(newSweetness);
        productRepository.save(product);  // 수정된 상품 저장
    }
}

