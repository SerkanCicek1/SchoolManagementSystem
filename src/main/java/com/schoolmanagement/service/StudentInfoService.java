package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.*;
import com.schoolmanagement.entity.enums.Note;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.request.StudentInfoRequestWithoutTeacherId;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentInfoResponse;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.repository.StudentInfoRepository;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

    private final StudentInfoRepository studentInfoRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;

    @Value("${midterm.exam.impact.percentage}")
    private Double midtermExamPercentage;
    @Value("${final.exam.impact.percentage}")
    private Double finalExamPercentage;

    // Not: save()****************************************************************
    public ResponseMessage<StudentInfoResponse> save(String username, StudentInfoRequestWithoutTeacherId studentInfoRequest) {

        // !!! DTO ve request den gelen Student,Teacher,Lesson ve EducationTerm getiriliyor
        Student student = studentService.getStudentByIdForResponse(studentInfoRequest.getStudentId());
        Teacher teacher = teacherService.getTeacherByUsername(username);
        Lesson lesson = lessonService.getLessonById(studentInfoRequest.getLessonId());
        EducationTerm educationTerm = educationTermService.getById(studentInfoRequest.getEducationTermId());

        // !!! lesson cakisma varmi kontrolu
        if(checkSameLesson(studentInfoRequest.getStudentId(), lesson.getLessonName())){
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_LESSON_MESSAGE, lesson.getLessonName()));
        }

        // !!! Ders notu ortalamasi aliniyor

        Double noteAverage = calculateExamAverage(studentInfoRequest.getMidtermExam(),
                studentInfoRequest.getFinalExam());
        // !!! Ders notu Alfabetik olarak hesaplaniyor
        Note note =  checkLetterGrade(noteAverage);

        // !!! DTO -> POJO
        StudentInfo studentInfo =  createDto(studentInfoRequest, note, noteAverage) ;
        // !!! DTO da olmayan fieldlar setleniyor
        studentInfo.setStudent(student);
        studentInfo.setEducationTerm(educationTerm);
        studentInfo.setTeacher(teacher);
        studentInfo.setLesson(lesson);
        // !!! DB ye kayit islemi
        StudentInfo savedStudentInfo = studentInfoRepository.save(studentInfo);
        //!!! Response objesi olusturuluyor
        return ResponseMessage.<StudentInfoResponse>builder()
                .message("Student Info Saved Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(createResponse(savedStudentInfo))
                .build();


    }

    private boolean checkSameLesson(Long studentId, String lessonName){

        return studentInfoRepository.getAllByStudentId_Id(studentId)
                .stream()
                .anyMatch((e)->e.getLesson().getLessonName().equalsIgnoreCase(lessonName));
    }

    private Double calculateExamAverage(Double midtermExam, Double finalExam){

        return ((midtermExam* midtermExamPercentage) + (finalExam*finalExamPercentage));
    }

    private Note checkLetterGrade(Double average){

        if(average<50.0) {
            return Note.FF;
        } else if (average>=50.0 && average<55) {
            return Note.DD;
        } else if (average>=55.0 && average<60) {
            return Note.DC;
        } else if (average>=60.0 && average<65) {
            return Note.CC;
        } else if (average>=65.0 && average<70) {
            return Note.CB;
        } else if (average>=70.0 && average<75) {
            return Note.BB;
        } else if (average>=75.0 && average<80) {
            return Note.BA;
        } else {
            return Note.AA;
        }
    }

    private StudentInfo createDto(StudentInfoRequestWithoutTeacherId studentInfoRequest,
                                  Note note, Double average){
        return StudentInfo.builder()
                .infoNote(studentInfoRequest.getInfoNote())
                .absentee(studentInfoRequest.getAbsentee())
                .midtermExam(studentInfoRequest.getMidtermExam())
                .finalExam(studentInfoRequest.getFinalExam())
                .examAverage(average)
                .letterGrade(note)
                .build();
    }

    private StudentInfoResponse createResponse(StudentInfo studentInfo){
        return StudentInfoResponse.builder()
                .lessonName(studentInfo.getLesson().getLessonName())
                .creditScore(studentInfo.getLesson().getCreditScore())
                .isCompulsory(studentInfo.getLesson().getIsCompulsory())
                .educationTerm(studentInfo.getEducationTerm().getTerm())
                .id(studentInfo.getId())
                .absentee(studentInfo.getAbsentee())
                .midtermExam(studentInfo.getMidtermExam())
                .finalExam(studentInfo.getFinalExam())
                .infoNote(studentInfo.getInfoNote())
                .Note(studentInfo.getLetterGrade())
                .average(studentInfo.getExamAverage())
                .studentResponse(createStudentResponse(studentInfo.getStudent()))
                .build();

    }

    public StudentResponse createStudentResponse(Student student) {

        return StudentResponse.builder()
                .userId(student.getId())
                .username(student.getUsername())
                .surname(student.getSurname())
                .name(student.getName())
                .birthDay(student.getBirthDay())
                .birthPlace(student.getBirthPlace())
                .phoneNumber(student.getPhoneNumber())
                .gender(student.getGender())
                .email(student.getEmail())
                .motherName(student.getMotherName())
                .fatherName(student.getFatherName())
                .studentNumber(student.getStudentNumber())
                .isActive(student.isActive())
                .build();
    }

    // Not: delete()****************************************************************
    public ResponseMessage<?> deleteStudentInfo(Long studentInfoId) {

        if(!studentInfoRepository.existsByIdEquals(studentInfoId)) {
            throw new ResourceNotFoundException(String.format(Messages.STUDENT_INFO_NOT_FOUND, studentInfoId));
        }

        studentInfoRepository.deleteById(studentInfoId);

        return ResponseMessage.builder()
                .message("Student Info deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}