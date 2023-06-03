package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("dean")
public class DeanController {

    private final DeanService deanService;

    // Not: Save() *************************************************
    @PostMapping("/save") // http://localhost:8080/dean/save
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessage<DeanResponse> save(@RequestBody @Valid DeanRequest deanRequest) {

        return deanService.save(deanRequest);

    }

    // Not :  UpdateById() **********************************************
    @PutMapping("/update/{userId}") // http://localhost:8080/dean/update/1
    @PreAuthorize("hasAuthority('ADMIN')") // TODO : Dean eklenmeli
    public ResponseMessage<DeanResponse> update(@RequestBody @Valid DeanRequest deanRequest,
                                                @PathVariable Long userId) {
        return deanService.update(deanRequest, userId);
    }

    // Not :  Delete() ****************************************************
    @DeleteMapping("/delete/{userId}") // http://localhost:8080/dean/delete/1
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessage<?> delete(@PathVariable Long userId){

        return deanService.deleteDean(userId);
    }

    // Not :  getById() ************************************************************************
    @GetMapping("/getManagerById/{userId}") // http://localhost:8080/dean/getManagerById/1
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessage<DeanResponse> getDeanById(@PathVariable Long userId){

        return deanService.getDeanById(userId);

    }

    // Not :  getAll() *************************************************************************

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")  // http://localhost:8080/dean/getAll
    public List<DeanResponse> getAll() {
        return deanService.getAllDean();
    }

    // Not :  Search() *************************************************************************
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search") // hht://localhost:8080/dean/search
    public Page<DeanResponse> search( // TODO getALLWithPAge
                                      @RequestParam(value = "page") int page,
                                      @RequestParam(value = "size") int size,
                                      @RequestParam(value = "sort") String sort,
                                      @RequestParam(value = "type") String type
    ) {

        return deanService.search(page,size,sort,type);
    }

}