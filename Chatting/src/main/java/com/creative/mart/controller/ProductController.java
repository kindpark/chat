package com.creative.mart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.creative.mart.model.Product;
import com.creative.mart.repository.ProductRepository;
import com.creative.mart.service.CartService;
import com.creative.mart.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop/products")
public class ProductController {
    @Autowired
	private ProductService productService;
    /*
    @Autowired
    private ProductRepository productRepository;
    private static final String SUPABASE_STORAGE_URL 
    = "https://kjapvzdtnxlqisnkcxgy.supabase.co/storage/v1/object/sign/sampleimage/burger.jpg?token=";
    private static final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1cmwiOiJzYW1wbGVpbWFnZS9idXJnZXIuanBnIiwiaWF0IjoxNzQzNTMyNjI3LCJleHAiOjE3NzUwNjg2Mjd9.aVws3lOzj8SGWLxUI2z6kIqmp7DUK26yYvUUAgDUroE";
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts().stream()
                .map(product -> {
                    // Supabase 이미지 경로 변환
                    String imageUrl = (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) 
                        ? SUPABASE_STORAGE_URL + product.getImageUrl() 
                        : "/images/default.png"; // 기본 이미지
                    product.setImageUrl(imageUrl);
                    return product;
                })
                .collect(Collectors.toList());

            model.addAttribute("products", products);
            return "product"; // product.html 반환
    }
    */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}