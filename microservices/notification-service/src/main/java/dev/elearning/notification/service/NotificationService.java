package dev.elearning.notification.service;

import dev.elearning.notification.client.UserClient;
import dev.elearning.notification.dto.request.EmailRequest;
import dev.elearning.notification.dto.request.NotificationRequest;
import dev.elearning.notification.dto.response.NotificationResponse;
import dev.elearning.notification.dto.response.UserResponse;
import dev.elearning.notification.model.Notification;
import dev.elearning.notification.model.enums.NotificationStatus;
import dev.elearning.notification.model.enums.NotificationType;
import dev.elearning.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserClient userClient;

    @Value("url.courses")
    private String url;

    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setActionUrl(request.getActionUrl());
        notification.setImageUrl(request.getImageUrl());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCourseTitle(request.getCourseTitle());
        notification.setQuizScore(request.getQuizScore());
        notification.setCertificateCode(request.getCertificateCode());
        notification.setModuleName(request.getModuleName());

        notification = notificationRepository.save(notification);

        return toResponse(notification);
    }

    @Transactional
    public void sendWelcomeNotification(Long userId) {
        UserResponse user = userClient.getUserById(userId);
        if (user == null) return;

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(userId);
        notificationRequest.setType(NotificationType.WELCOME);
        notificationRequest.setTitle("Welcome to E-Learning!");
        notificationRequest.setContent("We're excited to have you on board. Start learning journey today!");
        notificationRequest.setActionUrl("/dashboard");
        createNotification(notificationRequest);

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("headerTitle", "Welcome to E-Learning");

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("Welcome to E-Learning Platform!");
        emailRequest.setVariables(variables);

        emailService.sendEmail(emailRequest);

        markAsSentByUserIdAndType(userId, NotificationType.WELCOME);
    }

    @Transactional
    public void sendCoursePurchasedNotification(Long userId, String courseTitle, String courseDescription, Long courseId) {
        UserResponse user = userClient.getUserById(userId);

        if (user == null) return;

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(userId);
        notificationRequest.setType(NotificationType.COURSE_PURCHASED);
        notificationRequest.setTitle("Course Purchased!");
        notificationRequest.setContent("You have successfully purchased: " + courseTitle);
        notificationRequest.setActionUrl("/courses/" + courseId);
        notificationRequest.setCourseTitle(courseTitle);
        createNotification(notificationRequest);

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("courseTitle", courseTitle);
        variables.put("courseDescription", courseDescription);
        variables.put("courseUrl", url + courseId);
        variables.put("headerTitle", "Course Purchase Confirmation");

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("Course Purchased: " + courseTitle);
        emailRequest.setTemplate("course-purchased");
        emailRequest.setVariables(variables);

        emailService.sendEmail(emailRequest);

        markAsSentByUserIdAndType(userId, NotificationType.COURSE_PURCHASED);
    }

    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> getUserNotificationsPaginated(Long userId, Pageable pageable) {
        return notificationRepository.findAllByUserId(userId, pageable)
                .map(this::toResponse)
                .toList();
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        notificationRepository.markAsRead(userId, notificationId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    private void markAsSentByUserIdAndType(Long userId, NotificationType type) {
        List<Notification> notifications = notificationRepository.findAllByUserIdAndType(userId, type);

        for (Notification notification : notifications) {
            if (notification.getStatus() == NotificationStatus.PENDING) {
                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
                notificationRepository.save(notification);
            }
        }
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setType(notification.getType());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setIsRead(notification.getIsRead());
        response.setReadAt(notification.getReadAt());
        response.setSentAt(notification.getSentAt());
        response.setCreatedAt(notification.getCreatedAt());
        response.setActionUrl(notification.getActionUrl());
        response.setImageUrl(notification.getImageUrl());
        response.setStatus(notification.getStatus());
        response.setCourseTitle(notification.getCourseTitle());
        response.setQuizScore(notification.getQuizScore());
        response.setCertificateCode(notification.getCertificateCode());
        response.setModuleName(notification.getModuleName());
        return response;
    }
}
