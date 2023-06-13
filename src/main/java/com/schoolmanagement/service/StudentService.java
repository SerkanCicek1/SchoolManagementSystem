package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.request.StudentRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.utils.FieldControl;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final AdvisorTeacherService advisorTeacherService;
    private final FieldControl fieldControl;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    // Not: Save() **********************************************************
    public ResponseMessage<StudentResponse> save(StudentRequest studentRequest) {

        // !!! AdvisorTeacher kontrolu
        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherById(studentRequest.getAdvisorTeacherId()).orElseThrow(()->
                new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE,
                        studentRequest.getAdvisorTeacherId())));

        // !!! Dublicate kontrolu
        fieldControl.checkDuplicate(studentRequest.getUsername(), studentRequest.getSsn()
                ,studentRequest.getPhoneNumber(), studentRequest.getEmail());

        //!!! Student DTO -> POJO
        Student student = studentRequestToDto(studentRequest);
        // !!! student nesnesindeki eksik datalari setliyoruz
        student.setStudentNumber(lastNumber());
        student.setAdvisorTeacher(advisorTeacher);
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setActive(true);
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));

        // !!! Response Nesnesi olusturuluyor
        return ResponseMessage.<StudentResponse>builder()
                .object(createStudentResponse(studentRepository.save(student)))
                .message("Student saved Successfully")
                .build();
    }

    private Student studentRequestToDto(StudentRequest studentRequest){
        return Student.builder()
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .birthDay(studentRequest.getBirthDay())
                .birthPlace(studentRequest.getBirthPlace())
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .password(studentRequest.getPassword())
                .username(studentRequest.getUsername())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .phoneNumber(studentRequest.getPhoneNumber())
                .gender(studentRequest.getGender())
                .build();

    }

    public int lastNumber() {
        if(!studentRepository.findStudent()) {
            return 1000;
        }

        return studentRepository.getMaxStudentNumber() +1;
    }

    private StudentResponse createStudentResponse(Student student) {
        return StudentResponse.builder()
                .userId(student.getId())
                .username(student.getUsername())
                .name(student.getName())
                .surname(student.getSurname())
                .birthDay(student.getBirthDay())
                .birthPlace(student.getBirthPlace())
                .phoneNumber(student.getPhoneNumber())
                .gender(student.getGender())
                .email(student.getEmail())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .studentNumber(student.getStudentNumber())
                .isActive(student.isActive())
                .build();
    }
    // Not: changeActiveStatus() *********************************************
    public ResponseMessage<?> changeStatus(Long id, boolean status) {

        // !!! id kontrolu
        Student student = studentRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));

        student.setActive(status);

        studentRepository.save(student);

        return ResponseMessage.builder()
                .message("Student is " + (status ? "active" : "passive"))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: getAllStudent() *******************************************************
    public List<StudentResponse> getAllStudent() {
        return studentRepository.findAll()
                .stream()
                .map(this::createStudentResponse)
                .collect(Collectors.toList());
    }

    // Not: updateStudent() ******************************************************
    public ResponseMessage<StudentResponse> updateStudent(Long userId, StudentRequest studentRequest) {

        // !!! Student var mi kontrolu
        Student student = studentRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));
        // !!! AdvTeacher kontrolu
        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherById(studentRequest.getAdvisorTeacherId())
                .orElseThrow(()->
                        new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE, studentRequest.getAdvisorTeacherId())));
        // Dublicate Kontrolu
        fieldControl.checkDuplicate(studentRequest.getUsername(),studentRequest.getSsn(),studentRequest.getPhoneNumber(),studentRequest.getEmail());

        // !!! DTO -> POJO
        Student updatedStudent = createUpdatedStudent(studentRequest, userId);
        updatedStudent.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        updatedStudent.setAdvisorTeacher(advisorTeacher);
        updatedStudent.setStudentNumber(student.getStudentNumber());
        updatedStudent.setActive(true);


        studentRepository.save(updatedStudent);

        return ResponseMessage.<StudentResponse>builder()
                .object(createStudentResponse(updatedStudent))
                .message("Student updated Successfully")
                .httpStatus(HttpStatus.OK)
                .build();


    }

    private Student createUpdatedStudent(StudentRequest studentRequest, Long userId){
        return Student.builder()
                .id(userId)
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .birthDay(studentRequest.getBirthDay())
                .birthPlace(studentRequest.getBirthPlace())
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .password(studentRequest.getPassword())
                .username(studentRequest.getUsername())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .phoneNumber(studentRequest.getPhoneNumber())
                .gender(studentRequest.getGender())
                .userRole(userRoleService.getUserRole(RoleType.STUDENT))
                .build();

    }
}