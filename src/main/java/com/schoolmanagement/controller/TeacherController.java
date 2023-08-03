package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.ChooseLessonTeacherRequest;
import com.schoolmanagement.payload.request.TeacherRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.TeacherResponse;
import com.schoolmanagement.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForZonedDateTime;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Not: Save() **********************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/save")  // http://localhost:8080/teachers/save
    public ResponseMessage<TeacherResponse> save(@RequestBody @Valid TeacherRequest teacher) {

        return teacherService.save(teacher);
    }

    // Not: getAll() **********************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAll") // http://localhost:8080/teachers/getAll
    public List<TeacherResponse> getAllTeacher(){
        return teacherService.getAllTeacher();
    }

    // Not: updateTeacherById() ************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PutMapping("/update/{userId}")  // http://localhost:8080/teachers/update/1
    public ResponseMessage<TeacherResponse> updateTeacher(@RequestBody @Valid TeacherRequest teacher,
                                                          @PathVariable Long userId){

        return teacherService.updateTeacher(teacher, userId);
    }


    // Not: getTeacherByName() **************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getTeacherByName")
    public List<TeacherResponse> getTeacherByName(@RequestParam(name = "name") String teacherName){
        return teacherService.getTeacherByName(teacherName);

    }


    // Not: deleteTeacher() *****************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage<?> deleteTeacher(@PathVariable Long id) {
        return  teacherService.deleteTeacher(id);
    }



    // Not: getTeacherById() ****************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getSavedTeacherById/{id}")
    public ResponseMessage<TeacherResponse> getSavedTeacherById(@PathVariable Long id){
        return teacherService.getSavedTeacherById(id);
    }



    // Not: getAllWithPage() ****************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/search")
    public Page<TeacherResponse> search(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return teacherService.search(page, size,sort,type);
    }


    // Not: addLessonProgramToTeachersLessonsProgram() **********************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/chooseLesson")
    public ResponseMessage<TeacherResponse> chooseLesson(@RequestBody @Valid ChooseLessonTeacherRequest chooseLessonRequest){
        return teacherService.chooseLesson(chooseLessonRequest);
    }


}