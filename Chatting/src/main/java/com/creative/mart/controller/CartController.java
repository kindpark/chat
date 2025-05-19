package com.creative.mart.controller;
import java.util.List;
import java.util.Map;
/*
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creative.mart.model.Cart;
import com.creative.mart.model.CartItem;
import com.creative.mart.model.Product;
import com.creative.mart.repository.CartItemRepository;
import com.creative.mart.repository.CartRepository;
import com.creative.mart.repository.ProductRepository;
import com.creative.mart.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shop/cart")
public class CartController {
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository; // Product 엔티티에 대한 리포지토리

    public CartController(CartService cartService, CartRepository cartRepository, CartItemRepository cartItemRepository) {
    	this.cartService=cartService;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
    }
    
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId,
            @RequestParam(value = "custom", required = false) String custom,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
    		HttpSession session) {
    	//세션을 불러와서 세션에서 sessionID 챙겨오기
    	Object sessionIdObj= session.getAttribute("sessionId");
    	String sessionId = null;
        if (sessionIdObj instanceof String) {
            sessionId = (String) sessionIdObj;
        } else if (sessionIdObj instanceof UUID) {
            sessionId = sessionIdObj.toString(); 
            session.setAttribute("sessionId", sessionId);
        } else {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }
    	cartService.addProductToCart(sessionId, productId, quantity, custom);
        return "redirect:/shop/cart"; // 장바구니 페이지로 리디렉션
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cart", cartService.getCartItems());
        return "cart";
    }
    @PostMapping("/update")
    public String updateCartQuantity(@RequestParam("cartId") int cartId, @RequestParam("quantity") int quantity) {
        cartService.updateCartQuantity(cartId, quantity);
        return "redirect:/shop/cart"; // 장바구니 페이지로 리디렉션
    }
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") int productId) {
        cartService.removeProductFromCart(productId);
        return "redirect:/shop/cart";
    }
    
    //나중엔 클래스를 수정해야 할수 있으니까 그떄 되면 클래스로 수정
    @PostMapping("/edit")
    public String updateSweetness(@RequestParam int cartId, @RequestParam int newSweetness) {
        cartService.updateProductSweetness(cartId, newSweetness);
        return "redirect:/shop/cart";  // 수정 후 장바구니 페이지로 리다이렉트
    }
    @GetMapping("/editcartmenu/{cartId}")
    public String editCartItem(@PathVariable int cartId, Model model) {
        // 장바구니에 있는 상품을 찾음
        CartItem cartitem = cartItemRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 항목: " + cartId));
        //장바구니 상품 fetch
        Product product = cartitem.getProduct();  
        model.addAttribute("cart", cartitem); 
        model.addAttribute("product", product);

        return "editcartmenu";
    }
    //클래스로 수정 필요하면 할것.
    @PostMapping("/editcartmenu/{cartId}")
    public String updateCartItemSweetness(@PathVariable int cartId) {
        // 장바구니에서 해당 항목을 찾음
        CartItem cartitem = cartItemRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 항목: " + cartId));
        
        Product product = cartitem.getProduct();
        //product.setSweetness(newSweetness);  
        productRepository.save(product);  

        return "redirect:/shop/cart"; 
    }

}
*/
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.creative.mart.model.CartItem;
import com.creative.mart.model.Product;
import com.creative.mart.repository.CartItemRepository;
import com.creative.mart.repository.CartRepository;
import com.creative.mart.repository.ProductRepository;
import com.creative.mart.service.CartService;
import com.creative.user.model.User;
import com.creative.user.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
@CrossOrigin(origins = {"http://10.0.2.2:8080","http://localhost:8080"}, allowCredentials = "true")
@RestController
@RequestMapping("/shop/cart")
public class CartController {
    @Autowired
	private final CartService cartService;
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    public CartController(CartService cartService, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        int productId = (int) payload.get("productId");
        int quantity = (int) payload.get("quantity");
        String custom = (String) payload.get("custom");
        User user = userRepository.findById(userId).orElseThrow();
        cartService.addProductToCart(user, productId, quantity, custom);
        return ResponseEntity.ok().body("상품이 장바구니에 추가되었습니다.");
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
    	System.out.println("SessionId:" + user.getId());
        return ResponseEntity.ok(cartService.getCartItems(user));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCartQuantity(@RequestParam("cartId") int cartId, @RequestParam("quantity") int quantity) {
        cartService.updateCartQuantity(cartId, quantity);
        return ResponseEntity.ok().body("수량이 수정되었습니다.");
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam("productId") int productId) {
        cartService.removeProductFromCart(productId);
        return ResponseEntity.ok().body("상품이 삭제되었습니다.");
    }

    @PostMapping("/edit")
    public ResponseEntity<?> updateSweetness(@RequestParam int cartId, @RequestParam int newSweetness) {
        cartService.updateProductSweetness(cartId, newSweetness);
        return ResponseEntity.ok().body("당도가 수정되었습니다.");
    }

    @GetMapping("/editcartmenu/{cartId}")
    public ResponseEntity<?> editCartItem(@PathVariable int cartId) {
        CartItem cartItem = cartItemRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 항목: " + cartId));
        Product product = cartItem.getProduct();
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/editcartmenu/{cartId}")
    public ResponseEntity<?> updateCartItemSweetness(@PathVariable int cartId, @RequestParam int newSweetness) {
        CartItem cartItem = cartItemRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 항목: " + cartId));
        Product product = cartItem.getProduct();
        product.setSweetness(newSweetness);
        productRepository.save(product);
        return ResponseEntity.ok().body("상품 당도 정보가 수정되었습니다.");
    }
}
