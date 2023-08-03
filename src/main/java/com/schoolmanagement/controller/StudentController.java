package com.schoolmanagement.controller;

import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.payload.request.ChooseLessonProgramWithId;
import com.schoolmanagement.payload.request.StudentRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // Not: Save() **********************************************************
    /*
    *{
    "username" : "student2",
    "password" : "12345678"
}
    * */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/save")
    public ResponseMessage<StudentResponse> save(@RequestBody @Valid StudentRequest studentRequest) {
        return studentService.save(studentRequest);
    }

    // Not: changeActiveStatus() *********************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/changeStatus")
    public ResponseMessage<?> changeStatus(@RequestParam Long id, @RequestParam boolean status){
        return studentService.changeStatus(id,status);
    }

    // Not: getAllStudent() *******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAll")
    public List<StudentResponse> getAllStudent(){
        return studentService.getAllStudent();
    }

    // Not: updateStudent() ******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PutMapping("/update/{userId}")
    public ResponseMessage<StudentResponse> updateStudent(@PathVariable Long userId,
                                                          @RequestBody @Valid StudentRequest studentRequest){
        return studentService.updateStudent(userId, studentRequest);

    }

    // Not: deleteStudent() ******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete/{studentId}")
    public ResponseMessage<?> deleteStudent(@PathVariable Long studentId){

        return studentService.deleteStudent(studentId);
    }


    // Not: getStudentByName() ***************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getStudentByName")
    public List<StudentResponse> getStudentByName(@RequestParam(name = "name") String studentName){

        return studentService.getStudentByName(studentName);
    }



    // Not: getStudentById() ******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getStudentById")
    // TODO donen deger POJO olmamali DTO olarak donmemiz gerekiyor ResponseMessage<StudentResponse>
    public Student getStudentById(@RequestParam(name = "id") Long id) {
        return studentService.getStudentByIdForResponse(id);
    }



    // Not: getAllStudentWithPage() ***********************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/search")
    public Page<StudentResponse> search(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ){
        return studentService.search(page,size,sort,type);
    }


    // Not: chooseLessonProgramById() *********************************************
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/chooseLesson")
    public ResponseMessage<StudentResponse> chooseLesson(HttpServletRequest request,
                                                         @RequestBody @Valid ChooseLessonProgramWithId chooseLessonProgramRequest){
        // bu kisimn servicede yazilirsa daha iyi olur
        String username = (String) request.getAttribute("username");
        return studentService.chooseLesson(username,chooseLessonProgramRequest);
    }


    // Not : getAllStudentByAdvisorUsername() ********************************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllByAdvisorId")
    public List<StudentResponse>  getAllByAdvisorId(HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        return studentService.getAllStudentByTeacher_Username(username);
    }




}