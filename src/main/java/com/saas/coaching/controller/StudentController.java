package com.saas.coaching.controller;

import com.saas.coaching.dto.CreateStudentRequestDTO;
import com.saas.coaching.dto.StudentResponseDTO;
import com.saas.coaching.dto.UpdateStudentRequestDTO;
import com.saas.coaching.security.CustomUserPrincipal;
import com.saas.coaching.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Students", description = "Institute-scoped student management APIs")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create student", description = "Creates a new student under the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "409", description = "Duplicate student detected for the institute")
    })
    public StudentResponseDTO createStudent(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody CreateStudentRequestDTO request) {
        return studentService.createStudent(principal.getInstituteId(), request);
    }

    @GetMapping
    @Operation(summary = "List students", description = "Returns all students for the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Students fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired")
    })
    public List<StudentResponseDTO> getAllStudents(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return studentService.getAllStudents(principal.getInstituteId());
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "Get student by id", description = "Returns a single student for the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Student not found for the current institute")
    })
    public StudentResponseDTO getStudentById(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long studentId) {
        return studentService.getStudentById(principal.getInstituteId(), studentId);
    }

    @PutMapping("/{studentId}")
    @Operation(summary = "Update student", description = "Updates a student for the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Student not found for the current institute"),
            @ApiResponse(responseCode = "409", description = "Duplicate student detected for the institute")
    })
    public StudentResponseDTO updateStudent(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long studentId,
            @Valid @RequestBody UpdateStudentRequestDTO request) {
        return studentService.updateStudent(principal.getInstituteId(), studentId, request);
    }

    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete student", description = "Deletes a student for the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Student not found for the current institute")
    })
    public void deleteStudent(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long studentId) {
        studentService.deleteStudent(principal.getInstituteId(), studentId);
    }
}
