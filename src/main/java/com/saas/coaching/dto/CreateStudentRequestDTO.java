package com.saas.coaching.dto;

import com.saas.coaching.entity.StudentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "CreateStudentRequest", description = "Request payload to create a student for the authenticated institute")
public class CreateStudentRequestDTO {

    @Schema(description = "Student full name", example = "Rahul Sharma")
    @NotBlank
    @Size(max = 100)
    private String fullName;

    @Schema(description = "Student email address if available", example = "rahul@example.com", nullable = true)
    @Email
    @Size(max = 150)
    private String email;

    @Schema(description = "Student phone number", example = "9876543210")
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Schema(description = "Parent or guardian full name", example = "Suresh Sharma")
    @NotBlank
    @Size(max = 100)
    private String parentName;

    @Schema(description = "Parent or guardian phone number", example = "9876501234")
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Parent phone number must be 10 digits")
    private String parentPhoneNumber;

    @Schema(description = "Course or batch assigned to the student", example = "Class 10 Science")
    @NotBlank
    @Size(max = 100)
    private String courseName;

    @Schema(description = "Student lifecycle status", allowableValues = {"ACTIVE", "INACTIVE", "DISCONTINUED"}, example = "ACTIVE")
    @NotNull
    private StudentStatus status;
}
