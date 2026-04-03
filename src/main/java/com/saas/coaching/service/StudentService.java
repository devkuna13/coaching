package com.saas.coaching.service;

import com.saas.coaching.dto.CreateStudentRequestDTO;
import com.saas.coaching.dto.StudentResponseDTO;
import com.saas.coaching.dto.UpdateStudentRequestDTO;
import com.saas.coaching.entity.Institute;
import com.saas.coaching.entity.Student;
import com.saas.coaching.exception.ResourceNotFoundException;
import com.saas.coaching.exception.StudentAlreadyExistsException;
import com.saas.coaching.repository.InstituteRepository;
import com.saas.coaching.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final InstituteRepository instituteRepository;

    @Transactional
    public StudentResponseDTO createStudent(Long instituteId, CreateStudentRequestDTO request) {
        Institute institute = findInstituteById(instituteId);
        validateDuplicateStudent(instituteId, request.getFullName(), request.getParentPhoneNumber(), null);

        Student student = Student.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .parentName(request.getParentName())
                .parentPhoneNumber(request.getParentPhoneNumber())
                .courseName(request.getCourseName())
                .status(request.getStatus())
                .institute(institute)
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents(Long instituteId) {
        findInstituteById(instituteId);
        return studentRepository.findAllByInstituteIdOrderByCreatedAtDesc(instituteId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long instituteId, Long studentId) {
        Student student = findStudentByInstitute(studentId, instituteId);
        return mapToResponse(student);
    }

    @Transactional
    public StudentResponseDTO updateStudent(Long instituteId, Long studentId, UpdateStudentRequestDTO request) {
        Student student = findStudentByInstitute(studentId, instituteId);
        validateDuplicateStudent(instituteId, request.getFullName(), request.getParentPhoneNumber(), studentId);

        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setParentName(request.getParentName());
        student.setParentPhoneNumber(request.getParentPhoneNumber());
        student.setCourseName(request.getCourseName());
        student.setStatus(request.getStatus());

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long instituteId, Long studentId) {
        Student student = findStudentByInstitute(studentId, instituteId);
        studentRepository.delete(student);
    }

    private Institute findInstituteById(Long instituteId) {
        return instituteRepository.findById(instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Institute not found"));
    }

    private Student findStudentByInstitute(Long studentId, Long instituteId) {
        return studentRepository.findByIdAndInstituteId(studentId, instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for the current institute"));
    }

    private void validateDuplicateStudent(
            Long instituteId,
            String fullName,
            String parentPhoneNumber,
            Long currentStudentId) {
        boolean exists = currentStudentId == null
                ? studentRepository.existsByInstituteIdAndFullNameIgnoreCaseAndParentPhoneNumber(
                        instituteId, fullName, parentPhoneNumber)
                : studentRepository.existsByInstituteIdAndFullNameIgnoreCaseAndParentPhoneNumberAndIdNot(
                        instituteId, fullName, parentPhoneNumber, currentStudentId);

        if (exists) {
            throw new StudentAlreadyExistsException(
                    "A student with the same name and parent phone number already exists in this institute");
        }
    }

    private StudentResponseDTO mapToResponse(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .parentName(student.getParentName())
                .parentPhoneNumber(student.getParentPhoneNumber())
                .courseName(student.getCourseName())
                .status(student.getStatus())
                .instituteId(student.getInstitute().getId())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
