package dev.elearing.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OptionDTO {

    private Long id;
    private String text;
    private Integer optionOrder;
}
