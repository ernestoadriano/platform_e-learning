package dev.elearing.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProgressResponse {

    private Long courseId;

    private int completedLessons;

    private int totalLessons;

    private int percentage;

    private Map<Long, Boolean> lessonStatus = new HashMap<>();

    private Map<Long, Integer> lessonsScores = new HashMap<>();
}
