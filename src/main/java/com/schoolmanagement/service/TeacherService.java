package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.payload.dto.TeacherRequestDto;
import com.schoolmanagement.payload.request.TeacherRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.TeacherResponse;
import com.schoolmanagement.repository.TeacherRepository;
import com.schoolmanagement.utils.FieldControl;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final LessonProgramService lessonProgramService;
    private final FieldControl fieldControl;
    private final PasswordEncoder passwordEncoder;
    private final TeacherRequestDto teacherRequestDto;
    private final UserRoleService userRoleService;


    // Not: Save() **********************************************************
    public ResponseMessage<TeacherResponse> save(TeacherRequest teacherRequest) {

        Set<LessonProgram> lessons = lessonProgramService.getLessonProgramById(teacherRequest.getLessonIdList());

        if(lessons.size()==0){
            throw  new BadRequestException(Messages.LESSON_PROGRAM_NOT_FOUND_MESSAGE);
        } else {

            // !!! Dublicate kontrolu
            fieldControl.checkDuplicate(teacherRequest.getUsername(),
                    teacherRequest.getSsn(),
                    teacherRequest.getPhoneNumber(),
                    teacherRequest.getEmail());

            // !!! dto -> POJO donusumu
            Teacher teacher = teacherRequestToDto(teacherRequest);
            // !!! Rol bilgisi setleniyor
            teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
            // !!! dersProgrami ekleniyor
            teacher.setLessonsProgramList(lessons);
            // !!! sifre encode ediliyor
            teacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
            // !!! Db ye kayit islemi
            Teacher savedTeacher = teacherRepository.save(teacher);
            //TODO AdvisorTeacher yazilinca ekleme yapilacak

            return ResponseMessage.<TeacherResponse>builder()
                    .message("Teacher saved successfully")
                    .httpStatus(HttpStatus.CREATED)
                    .object(createTeacherResponse(savedTeacher))
                    .build();
        }
    }

    private Teacher teacherRequestToDto(TeacherRequest teacherRequest){
        return teacherRequestDto.dtoTeacher(teacherRequest);
    }

    private TeacherResponse createTeacherResponse(Teacher teacher){
        return TeacherResponse.builder()
                .userId(teacher.getId())
                .username(teacher.getUsername())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .birthDay(teacher.getBirthDay())
                .birthPlace(teacher.getBirthPlace())
                .ssn(teacher.getSsn())
                .phoneNumber(teacher.getPhoneNumber())
                .gender(teacher.getGender())
                .email(teacher.getEmail())
                .build();
    }
}