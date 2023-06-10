package com.schoolmanagement.config;

import com.schoolmanagement.payload.dto.DeanDto;
import com.schoolmanagement.payload.dto.LessonProgramDto;
import com.schoolmanagement.payload.dto.TeacherRequestDto;
import com.schoolmanagement.payload.dto.ViceDeanDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateObjectBean {

    @Bean
    public DeanDto deanDTO() {
        return new DeanDto();
    }

    @Bean
    public ViceDeanDto viceDeanDto(){
        return new ViceDeanDto();
    }

    @Bean
    public LessonProgramDto lessonProgramRequestDto() {
        return new LessonProgramDto();
    }

    @Bean
    public TeacherRequestDto teacherRequestDto() {
        return new TeacherRequestDto();
    }
}