package com.creative.chat.repository;

import com.creative.chat.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query("SELECT u.token FROM UserToken u WHERE u.userId = :userId")
    Optional<String> findTokenByUserId(@Param("userId")String userId);
    
    Optional<UserToken> findByUserId(String userId);

}
