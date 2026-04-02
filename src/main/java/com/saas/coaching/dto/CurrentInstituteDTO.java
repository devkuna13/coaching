package com.saas.coaching.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentInstituteDTO {

    private Long instituteId;
    private String instituteName;
    private String email;
}
