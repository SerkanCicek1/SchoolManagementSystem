package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.ViceDeanRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.ViceDeanResponse;
import com.schoolmanagement.service.ViceDeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("vicedean")
@RequiredArgsConstructor
public class ViceDeanController {

    private final ViceDeanService viceDeanService;

    // Not :  Save() *************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PostMapping("/save") // http://localhost:8080/vicedean/save
    public ResponseMessage<ViceDeanResponse> save(@RequestBody @Valid ViceDeanRequest viceDeanRequest) {

        return viceDeanService.save(viceDeanRequest);

    }

    // Not :  UpdateById() ********************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PutMapping("/update/{userId}") // http://localhost:8080/vicedean/update/1
    public ResponseMessage<ViceDeanResponse> update(@RequestBody @Valid ViceDeanRequest viceDeanRequest
            ,@PathVariable Long userId){
        return viceDeanService.update(viceDeanRequest, userId);
    }

    // Not :  Delete() *************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @DeleteMapping("/delete/{userId}") // http://localhost:8080/vicedean/delete/1
    public ResponseMessage<?> delete(@PathVariable Long userId){

        return viceDeanService.deleteViceDean(userId);

    }

    // Not :  getById() ************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/getViceDeanById/{userId}")  // http://localhost:8080/vicedean/getViceDeanById/1
    public ResponseMessage<ViceDeanResponse> getViceDeanById(@PathVariable Long userId) {

        return viceDeanService.getViceDeanById(userId);

    }

    // Not :  getAll() *************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/getAll")  // http://localhost:8080/vicedean/getAll
    public List<ViceDeanResponse> getAll(){
        return viceDeanService.getAllViceDean();
    }

    // Not :  getAllWithPage() ********************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/search")  // http://localhost:8080/vicedean/search?page=0&size=3&sort=ssn&type=desc
    public Page<ViceDeanResponse> getAllWithPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ) {

        return viceDeanService.getAllWithPage(page,size,sort,type);

    }





}