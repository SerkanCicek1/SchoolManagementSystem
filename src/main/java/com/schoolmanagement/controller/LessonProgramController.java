package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.LessonProgramRequest;
import com.schoolmanagement.payload.response.LessonProgramResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessonPrograms")
@RequiredArgsConstructor
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;

    // Not :  Save() *************************************************************************
    @PostMapping("/save")  // http://localhost:8080/lessonPrograms/save
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    public ResponseMessage<LessonProgramResponse> save(@RequestBody @Valid LessonProgramRequest lessonProgramRequest) {
        return lessonProgramService.save(lessonProgramRequest);
    }
    // Not :  getAll() *************************************************************************
    @GetMapping("/getAll")  // http://localhost:8080/lessonPrograms/getAll
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAll() {
        return lessonProgramService.getAllLessonProgram();
    }

    // Not :  getById() ************************************************************************

    @GetMapping("/getById/{id}") //http://localhost:8080/lessonPrograms/getById/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    public LessonProgramResponse getById(@PathVariable Long id) {
        return lessonProgramService.getByLessonProgramId(id);
    }

    // Not :  getAllLessonProgramUnassigned() **************************************************
    @GetMapping("/getAllUnassigned") //http://localhost:8080/lessonPrograms/getAllUnassigned
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllUnassigned() {
        return lessonProgramService.getAllLessonProgramUnassigned();
    }

    // Not :  getAllLessonProgramAssigned() **************************************************
    @GetMapping("/getAllAssigned") //http://localhost:8080/lessonPrograms/getAllAssigned
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllAssigned() {
        return lessonProgramService.getAllLessonProgramAssigned();
    }

    // Not :  Delete() *************************************************************************
    @DeleteMapping("/delete/{id}") //http://localhost:8080/lessonPrograms/delete/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    public ResponseMessage delete (@PathVariable Long id) {
        return lessonProgramService.deleteLessonProgram(id);
    }
    // Not :  getLessonProgramByTeacher() ******************************************************
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/getAllLessonProgramByTeacher")  //http://localhost:8080/lessonPrograms/getAllLessonProgramByTeacher
    public Set<LessonProgramResponse> getAllLessonProgramByTeacherId(HttpServletRequest httpServletRequest) {

        String username = (String) httpServletRequest.getAttribute("username");
        return lessonProgramService.getLessonProgramByTeacher(username);

    }

    // Not :  getLessonProgramByStudent() ******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER','TEACHER')")
    @GetMapping("/getAllLessonProgramByStudent") //http://localhost:8080/lessonPrograms/getAllLessonProgramByStudent
    public Set<LessonProgramResponse> getAllLessonProgramByStudent(HttpServletRequest httpServletRequest) {

        String username = (String) httpServletRequest.getAttribute("username");
        return lessonProgramService.getLessonProgramByStudent(username);
    }

    // Not :  getAllWithPage() ******************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER','TEACHER','STUDENT')")
    @GetMapping("/search")
    public Page<LessonProgramResponse> search(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return lessonProgramService.search(page,size,sort,type);
    }

}