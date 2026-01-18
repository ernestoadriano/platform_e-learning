package com.ernesto.monolith.notification.service;

import com.ernesto.monolith.common.dto.NotificationType;
import com.ernesto.monolith.notification.model.Notification;
import com.ernesto.monolith.notification.repository.NotificationRepository;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    public void notify(Long userId, String title, String message, NotificationType type) {
        Notification notification = new Notification();
        
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);

        notificationRepository.save(notification);
    }

    public void markAsRead(Long notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public List<Notification> myNotifications(User user) {
        return notificationRepository.findAllByUserIdOrderBySentAtDesc(user.getId());
    }
}
