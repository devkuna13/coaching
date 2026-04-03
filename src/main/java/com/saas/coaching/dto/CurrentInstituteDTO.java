package com.saas.coaching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CurrentInstitute", description = "Authenticated institute account details")
public class CurrentInstituteDTO {

    @Schema(description = "Unique institute identifier", example = "1")
    private Long instituteId;

    @Schema(description = "Institute name", example = "Gyanjyoti Coaching")
    private String instituteName;

    @Schema(description = "Institute account email", example = "owner@gyanjyoti.com")
    private String email;
}
