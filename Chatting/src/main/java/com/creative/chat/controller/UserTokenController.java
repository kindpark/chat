package com.creative.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creative.chat.model.UserToken;
import com.creative.chat.repository.UserTokenRepository;

//UserTokenController.java
@RestController
@RequestMapping("/api/token")
public class UserTokenController {

 @Autowired
 private UserTokenRepository userTokenRepository;

 @PostMapping
 public ResponseEntity<Void> saveToken(@RequestParam("userId") String userId, @RequestParam("token") String token) {
     // 기존 토큰 존재 여부 확인
     UserToken userToken = userTokenRepository
         .findByUserId(userId)
         .orElse(new UserToken());

     userToken.setUserId(userId);
     userToken.setToken(token);
     userTokenRepository.save(userToken);
     return ResponseEntity.ok().build();
 }
}
