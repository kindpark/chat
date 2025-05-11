package com.creative.chat.handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.creative.chat.model.ChatMessage;
import com.creative.chat.repository.ChatMessageRepository;
import com.creative.chat.repository.UserTokenRepository;
import com.creative.chat.service.FirebaseService;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

@Component
public class ChatHandler implements WebSocketHandler {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private FirebaseService firebaseService;
    
    @Autowired
    private UserTokenRepository userTokenRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserId(session);
        userSessions.put(userId, session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage textMessage) {
            String payload = textMessage.getPayload();
            Map<String, String> data = mapper.readValue(payload, new TypeReference<>() {});
            String sender = data.get("sender");
            String receiver = data.get("receiver");
            String content = data.get("content");

            saveMessage(sender, receiver, content);
            sendPush(receiver, content);
            if (userSessions.containsKey(receiver)) {
                userSessions.get(receiver).sendMessage(new TextMessage(payload));
            }
        } else {
            // BinaryMessage나 PongMessage는 무시하거나 에러 처리
            throw new IllegalArgumentException("Unsupported message type: " + message.getClass().getSimpleName());
        }
    }
    private String getUserId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null || uri.getQuery() == null) {
            throw new IllegalArgumentException("WebSocket URI에 쿼리 파라미터가 없습니다.");
        }

        String[] params = uri.getQuery().split("&");
        for (String param : params) {
            if (param.startsWith("userId=")) {
                return param.substring("userId=".length());
            }
        }

        throw new IllegalArgumentException("userId 파라미터가 존재하지 않습니다.");
    }

    private void saveMessage(String sender, String receiver, String content) {
        // 메시지 DB 저장
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setWRead(false);  // 기본적으로 읽지 않은 메시지로 설정
        message.setLdt(LocalDateTime.now());
        chatMessageRepository.save(message);
    }

    private void sendPush(String toUserId, String content) {
        Optional<String> tokenOpt = userTokenRepository.findTokenByUserId(toUserId);
        tokenOpt.ifPresent(token -> {
            try {
                firebaseService.sendMessage(token, "새 메시지", content);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        });
    }

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("❗ WebSocket 오류 발생: " + exception.getMessage());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub
	    String userId = getUserId(session);
	    userSessions.remove(userId);
	    System.out.println("🔌 연결 종료됨: " + userId);
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}
}
