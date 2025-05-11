package com.creative.chat.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatMessage {
    @Id @GeneratedValue 
    private Long id;
    private String sender;
    private String receiver;
    private String content;
    private boolean wRead;
    private LocalDateTime ldt;
}

