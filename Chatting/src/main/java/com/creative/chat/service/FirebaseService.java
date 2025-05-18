package com.creative.chat.service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import jakarta.annotation.PostConstruct;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
            	InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("chat1.json");
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String token, String title, String body) throws FirebaseMessagingException {
    	Message msg = Message.builder()
            .setToken(token)
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())

            // ② (선택) 필요하다면 data 페이로드 추가
            .putData("sender", title)        //예시: 보낸 사람 정보
            .putData("chatRoomId", "1")      //예시: 채팅방 ID

            // ③ Android 전용 설정: 알림 채널, 클릭 액션
            .setAndroidConfig(AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                    .setChannelId("chat_channel")           // Flutter와 동일한 채널명
                    .setClickAction("FLUTTER_NOTIFICATION_CLICK")
                    .build())
                .build())
            .build();

        // 실제 전송
        String response = FirebaseMessaging.getInstance().send(msg);
        System.out.println("Sent push response: " + response);
    }
}

