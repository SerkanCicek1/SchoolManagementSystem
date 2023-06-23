package com.schoolmanagement.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolmanagement.entity.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonProgramRequestForUpdate {

    @NotNull(message="Please enter day")
    private Day day;  //

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    @NotNull(message="Please enter start time")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    @NotNull(message="Please enter stop time")
    private LocalTime stopTime;

    @NotNull(message="Please select lesson")
    @Size(min=1, message ="Lesson must not be empty")
    private List<Long> lessonIdList;

    @NotNull(message="Please enter education term")
    private Long educationTermId;

    @NotNull(message = "Please select student ")
    @Size(min = 1, message = "student must not empty")
    private List<Long> studentIdList;

    @NotNull(message = "Please select teacher ")
    @Size(min = 1, message = "teacher must not empty")
    private List<Long> teacherIdList;
}