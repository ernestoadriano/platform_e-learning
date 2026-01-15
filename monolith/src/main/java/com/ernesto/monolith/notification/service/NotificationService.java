package com.ernesto.monolith.notification.service;

import com.ernesto.monolith.common.dto.NotificationResponse;
import com.ernesto.monolith.notification.model.Notification;
import com.ernesto.monolith.notification.repository.NotificationRepository;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    //Simulation
    public void notifyCertificate(User user, Long courseId) {
        System.out.println("Certificate issued to student " + user.getName());
    }

    public List<NotificationResponse> getMyNotifications(User user) {
        List<Notification> notifications = notificationRepository.findAllByUserId(user.getId());
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationResponses.add(toDTO(notification));
        }
        return notificationResponses;
    }

    public NotificationResponse getNotification(Long id, User user) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!Objects.equals(notification.getUserId(), user.getId())) {
            throw new RuntimeException("Forbidden");
        }

       return toDTO(notification);
    }

    private NotificationResponse toDTO(Notification notification) {
        return new NotificationResponse(notification.getTitle(), notification.getMessage(), notification.getSentAt());
    }
}
