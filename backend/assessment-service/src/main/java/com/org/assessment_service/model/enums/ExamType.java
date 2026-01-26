package com.org.assessment_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExamType {

    MODULE_TEST("MODULE_TEST"),
    FINAL_EXAM("FINAL_EXAM"),
    QUIZ("QUIZ");

    private final String type;
}
