package com.ernesto.monolith.notification.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    //Simulation
    public void notifyCertificate(Long userId, Long courseId) {
        System.out.println("Certificate issued to student " + userId);
    }
}
