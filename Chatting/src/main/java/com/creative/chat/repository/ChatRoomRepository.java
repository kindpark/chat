package com.creative.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.creative.chat.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	@Query("SELECT c FROM ChatRoom c WHERE " +
		       "(c.user1Id = :user1 AND c.user2Id = :user2) OR " +
		       "(c.user1Id = :user2 AND c.user2Id = :user1)")
		Optional<ChatRoom> findByUserIds(@Param("user1") String user1, @Param("user2") String user2);

}
