package dev.elearning.notification.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class EmailRequest {

    private String to;
    private String subject;
    private String template;
    private Map<String, Object> variables;
    private Boolean isHtml = true;
}
