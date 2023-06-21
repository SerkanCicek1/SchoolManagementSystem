package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.request.ChooseLessonProgramWithId;
import com.schoolmanagement.payload.request.StudentRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.utils.CheckParameterUpdateMethod;
import com.schoolmanagement.utils.CheckSameLessonProgram;
import com.schoolmanagement.utils.FieldControl;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final AdvisorTeacherService advisorTeacherService;
    private final FieldControl fieldControl;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final LessonProgramService lessonProgramService;

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

    public StudentResponse createStudentResponse(Student student) {
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
        if(!CheckParameterUpdateMethod.checkParameter(student, studentRequest)) {
            fieldControl.checkDuplicate(studentRequest.getUsername(),studentRequest.getSsn(),
                    studentRequest.getPhoneNumber(),studentRequest.getEmail());
        }


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

    // Not: deleteStudent() ******************************************************
    public ResponseMessage<?> deleteStudent(Long studentId) {
        // !!! id var mi kontrolu
        Student student = studentRepository.findById(studentId).orElseThrow(()->
                new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));

        studentRepository.deleteById(studentId); // TODO silme yapmiyor
        //studentRepository.delete(student);
        //studentRepository.flush();

        return ResponseMessage.builder()
                .message("Student Deleted Successfully ")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: getStudentByName() ***************************************************
    public List<StudentResponse> getStudentByName(String studentName) {
        return studentRepository.getStudentByNameContaining(studentName)
                .stream()
                .map(this::createStudentResponse)
                .collect(Collectors.toList());
    }

    // Not: getStudentById() ******************************************************
    public Student getStudentByIdForResponse(Long id) {

        return studentRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));
    }

    // Not: getAllStudentWithPage() ***********************************************
    public Page<StudentResponse> search(int page, int size, String sort, String type) {

        // Pageable pageable = PageRequest.of(page, size, Sort.by(type,sort));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return studentRepository.findAll(pageable).map(this::createStudentResponse);
    }

    // Not: chooseLessonProgramById() *********************************************
    public ResponseMessage<StudentResponse> chooseLesson(String username,
                                                         ChooseLessonProgramWithId chooseLessonProgramRequest) {

        // !!! Student ve LessonProgram kontrolu
        Student student = studentRepository.findByUsername(username).orElseThrow(()->
                new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));
        //!!! talep edilen lessonProgram getiriliyor
        Set<LessonProgram> lessonPrograms = lessonProgramService.getLessonProgramById(chooseLessonProgramRequest.getLessonProgramId());

        if(lessonPrograms.size()==0){
            throw  new ResourceNotFoundException(Messages.LESSON_PROGRAM_NOT_FOUND_MESSAGE);
        }
        // !!! Ogrencinin mevcut lessonProgramini getiriyoruz
        Set<LessonProgram> studentLessonProgram = student.getLessonsProgramList();
        //!!! lesson icin dublicate kontrolu
        CheckSameLessonProgram.checkLessonPrograms(studentLessonProgram,lessonPrograms);

        studentLessonProgram.addAll(lessonPrograms);
        student.setLessonsProgramList(studentLessonProgram);
        Student savedStudent =  studentRepository.save(student);

        return ResponseMessage.<StudentResponse>builder()
                .message("Lessons added to Student")
                .object(createStudentResponse(savedStudent))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // Not : getAllStudentByAdvisorUsername() ********************************************
    public List<StudentResponse> getAllStudentByTeacher_Username(String username) {

        return studentRepository.getStudentByAdvisorTeacher_Username(username)
                .stream()
                .map(this::createStudentResponse)
                .collect(Collectors.toList());
    }

    public boolean existByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean existById(Long studentId) {
        return studentRepository.existsById(studentId);
    }

    public List<Student> getStudentByIds(Long[] studentIds) {
        return studentRepository.findByIdsEquals(studentIds);
    }

    public Optional<Student> getStudentByUsernameForOptional(String username) {

        return studentRepository.findByUsernameEqualsForOptional(username);
    }
}