package com.saas.coaching.dto;

import com.saas.coaching.entity.StudentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "StudentResponse", description = "Student details returned by the API")
public class StudentResponseDTO {

    @Schema(description = "Unique student identifier", example = "1")
    private Long id;
    @Schema(description = "Student full name", example = "Rahul Sharma")
    private String fullName;
    @Schema(description = "Student email address", example = "rahul@example.com", nullable = true)
    private String email;
    @Schema(description = "Student phone number", example = "9876543210")
    private String phoneNumber;
    @Schema(description = "Parent or guardian full name", example = "Suresh Sharma")
    private String parentName;
    @Schema(description = "Parent or guardian phone number", example = "9876501234")
    private String parentPhoneNumber;
    @Schema(description = "Course or batch assigned to the student", example = "Class 10 Science")
    private String courseName;
    @Schema(description = "Student lifecycle status", allowableValues = {"ACTIVE", "INACTIVE", "DISCONTINUED"}, example = "ACTIVE")
    private StudentStatus status;
    @Schema(description = "Institute id owning this student record", example = "1")
    private Long instituteId;
    @Schema(description = "Student record creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "Student record last update timestamp")
    private LocalDateTime updatedAt;
}
