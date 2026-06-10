package dev.elearning.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Slf4j
@Service
public class TemplateService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String renderTemplate(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                variables.forEach(context::setVariable);
            }
            context.setVariable("headerTitle", "E-Learning Platform");
            return templateEngine.process("email/" + templateName, context);
        } catch (Exception exception) {
            log.error("Error rendering template: {}", templateName, exception);
            return "<html><body><h1>Error rendering email</h1></body></html>";
        }
    }
}
