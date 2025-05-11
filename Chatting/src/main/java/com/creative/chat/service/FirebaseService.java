package com.creative.chat.service;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public void sendMessage(String token, String title, String body) throws FirebaseMessagingException {
        Message msg = Message.builder()
            .setToken(token)

            // ① Notification 페이로드: 시스템 알림 트레이에 자동 표시
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())

            // ② (선택) 필요하다면 data 페이로드 추가
            .putData("sender", title)        // 예시: 보낸 사람 정보
            .putData("chatRoomId", "2")      // 예시: 채팅방 ID

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

