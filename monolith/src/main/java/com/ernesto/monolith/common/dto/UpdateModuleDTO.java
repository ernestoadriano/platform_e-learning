package com.ernesto.monolith.common.dto;

import lombok.NonNull;

public record UpdateModuleDTO(
        @NonNull String title
) {
}
