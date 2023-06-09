package com.schoolmanagement.payload.dto;

import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.payload.request.TeacherRequest;
import lombok.Data;

@Data
public class TeacherRequestDto {

    public Teacher dtoTeacher(TeacherRequest teacherRequest) {

        return Teacher.builder()
                .name(teacherRequest.getName())
                .surname(teacherRequest.getSurname())
                .ssn(teacherRequest.getSsn())
                .username(teacherRequest.getUsername())
                .birthDay(teacherRequest.getBirthDay())
                .birthPlace(teacherRequest.getBirthPlace())
                .password(teacherRequest.getPassword())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .email(teacherRequest.getEmail())
                .isAdvisor(teacherRequest.getIsAdvisorTeacher())
                .gender(teacherRequest.getGender())
                .build();
    }
}