package com.creative.mart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creative.mart.model.Product;
import com.creative.mart.repository.ProductRepository;

import java.util.List;
@Service
public class ProductService {
	
    @Autowired
    private ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // 상품 목록 가져오기
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 특정 상품 조회
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
    }
    //상품 url
    public String getProductImageUrl(String imagePath) {
    	final String storage = 
    			"https://kjapvzdtnxlqisnkcxgy.supabase.co/storage/v1/object/public/";
    	final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1cmwiOiJzYW1wbGVpbWFnZS9idXJnZXIuanBnIiwiaWF0IjoxNzQzNTMyNjI3LCJleHAiOjE3NzUwNjg2Mjd9.aVws3lOzj8SGWLxUI2z6kIqmp7DUK26yYvUUAgDUroE";
        
    	if (imagePath == null || imagePath.isEmpty()) {
            return "/images/default.png";
        }
        return storage + token;
    }

}
