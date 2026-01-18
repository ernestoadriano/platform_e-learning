package com.ernesto.monolith.common.dto;

public record OptionDTO(
        Long questionId,
        String text,
        boolean correct
) {
}
