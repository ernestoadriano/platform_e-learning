package dev.elearning.notification.service;

import dev.elearning.notification.dto.request.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateService templateService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendEmail(EmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo());
            helper.setFrom(fromEmail);
            helper.setSubject(request.getSubject());

            String htmlContent = templateService.renderTemplate(request.getTemplate(), request.getVariables());
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email sent to: {}", request.getTo());
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", request.getTo(), e);
            throw new RuntimeException(e);
        }
    }
}
