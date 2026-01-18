package com.ernesto.monolith.order.service;

import com.ernesto.monolith.common.dto.NotificationType;
import com.ernesto.monolith.course.model.Course;
import com.ernesto.monolith.course.repository.CourseRepository;
import com.ernesto.monolith.enrollment.service.EnrollmentService;
import com.ernesto.monolith.notification.service.NotificationService;
import com.ernesto.monolith.order.model.Purchase;
import com.ernesto.monolith.order.model.enums.PaymentMethod;
import com.ernesto.monolith.order.model.enums.PaymentStatus;
import com.ernesto.monolith.order.repository.PurchaseRepository;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private NotificationService notificationService;

    public Purchase createPurchase(Long courseId, PaymentMethod method, User student) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getPublished()) {
            throw new RuntimeException("Course not available");
        }

        boolean alreadyPurchased = purchaseRepository.existsByStudentIdAndCourseIdAndStatus(student.getId(), courseId, PaymentStatus.PAID);

        if (alreadyPurchased) {
            throw new RuntimeException("Course already purchased");
        }

        Purchase purchase = new Purchase();
        purchase.setStudentId(student.getId());
        purchase.setCourseId(courseId);
        purchase.setAmount(course.getPrice());
        purchase.setPaymentMethod(method);

        return purchaseRepository.save(purchase);
    }

    public void confirmPayment(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        if (purchase.getStatus().equals(PaymentStatus.PAID)) {
            return;
        }

        switch (purchase.getPaymentMethod()) {
            case VISA -> processVisaPayment(purchase);
            case PAYPAL -> processPaypalPayment(purchase);
        }

        purchase.setStatus(PaymentStatus.PAID);
        purchase.setPurchasedAt(LocalDateTime.now());
        purchaseRepository.save(purchase);

        notificationService.notify(purchase.getStudentId(),
                "Payment confirm",
                "The payment of course was confirm with success.",
                NotificationType.PAYMENT);
        enrollmentService.createEnrollment(purchase);
    }

    private void processVisaPayment(Purchase purchase) {

    }

    private void processPaypalPayment(Purchase purchase) {

    }
}
