package com.schoolmanagement.utils;

import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.exception.BadRequestException;

import java.util.HashSet;
import java.util.Set;

public class CheckSameLessonProgram {

    public static void  checkLessonPrograms(Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest){

        if(existLessonProgram.isEmpty() && lessonProgramRequest.size()>1) {
            checkDuplicateLessonPrograms(lessonProgramRequest);
        } else {
            checkDuplicateLessonPrograms(lessonProgramRequest);
            checkDuplicateLessonPrograms(existLessonProgram,lessonProgramRequest);
        }

    }

    private static void checkDuplicateLessonPrograms(Set<LessonProgram> lessonPrograms) {

        Set<String> uniqueLessonProgramKeys = new HashSet<>();

        for (LessonProgram lessonProgram : lessonPrograms ) {
            String lessonProgramKey = lessonProgram.getDay().name() + lessonProgram.getStartTime();
            if(uniqueLessonProgramKeys.contains(lessonProgramKey)){
                throw  new BadRequestException(Messages.LESSON_PROGRAM_EXIST_MESSAGE);
            }
            uniqueLessonProgramKeys.add(lessonProgramKey);
        }
    }

    public static void checkDuplicateLessonPrograms(Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest ){

        for (LessonProgram requestLessonProgram : lessonProgramRequest) {

            if(existLessonProgram.stream().anyMatch(lessonProgram ->
                    lessonProgram.getStartTime().equals(requestLessonProgram.getStartTime()) &&
                            lessonProgram.getDay().name().equals(requestLessonProgram.getDay().name()))) {
                throw  new BadRequestException(Messages.LESSON_PROGRAM_EXIST_MESSAGE);
            }

        }

    }

    // TODO : startTime baska bir lessonProgramin startTime ve endTime arasindami kontrolu eklebnecek
}