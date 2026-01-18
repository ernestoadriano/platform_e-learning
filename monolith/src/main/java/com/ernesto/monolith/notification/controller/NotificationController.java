package com.ernesto.monolith.notification.controller;

import com.ernesto.monolith.notification.model.Notification;
import com.ernesto.monolith.notification.service.NotificationService;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping
    public List<Notification> myNotifications(@AuthenticationPrincipal User user) {
        return service.myNotifications(user);
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        service.markAsRead(id, user);
    }
}
