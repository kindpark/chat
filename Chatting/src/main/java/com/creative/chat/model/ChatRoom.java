package com.creative.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="chat_room")
@Getter
@Setter
public class ChatRoom {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="user1_id", nullable=false)
    private String user1Id;
    @Column(name="user2_id", nullable=false)
    private String user2Id;
}
