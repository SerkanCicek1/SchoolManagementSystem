package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.ContactMessageRequest;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    // Not: save() **********************************************

    /*
        {
            "name" : "Mirac",
            "email" : "xxx@yyy.com",
            "subject" : "RestFull API",
            "message" : "RestFull API"
        }
     */ // Ornek JSON
    @PostMapping("/save") // http://localhost:8080/contactMessages/save
    public ResponseMessage<ContactMessageResponse> save(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {

        return contactMessageService.save(contactMessageRequest);

    }



    // Not: getAll() ********************************************
    @GetMapping("/getAll")  // http://localhost:8080/contactMessages/getAll
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    public Page<ContactMessageResponse> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "date") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.getAll(page,size,sort,type);

    }


    // Not: searchByEmail() *************************************
    @GetMapping("/searchByEmail") // http://localhost:8080/contactMessages/searchByEmail?email=xxx@yyy.com&page=0&size=1&sort=date&type=desc
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "date") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return contactMessageService.searchByEmail(email,page,size,sort,type);
    }


    // Not: searchBySubject()************************************
    @GetMapping("/searchBySubject") // http://localhost:8080/contactMessages/searchBySubject?subject=RestFull API&page=0&size=1&sort=date&type=desc
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "date") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return contactMessageService.searchBySubject(subject,page,size,sort,type);
    }

}

// ODEV : POSTMAN de END-POINTLER test edilecek