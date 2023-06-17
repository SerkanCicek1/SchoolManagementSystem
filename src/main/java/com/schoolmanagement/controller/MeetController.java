package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.MeetRequestWithoutId;
import com.schoolmanagement.payload.response.MeetResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("meet")
@RequiredArgsConstructor
public class MeetController {

    private final MeetService meetService;

    // Not :  Save() *************************************************************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save")
    public ResponseMessage<MeetResponse> save(HttpServletRequest httpServletRequest ,
                                              @RequestBody @Valid MeetRequestWithoutId meetRequest){
        String username = (String) httpServletRequest.getAttribute("username");
        return meetService.save(username, meetRequest);
    }

    // Not : getAll() *************************************************************************
    @PreAuthorize("hasAnyAuthority( 'ADMIN')")
    @GetMapping("/getAll")
    public List<MeetResponse> getAll(){
        return meetService.getAll();
    }

    // Not :  getMeetById() ********************************************************************
    @PreAuthorize("hasAnyAuthority( 'ADMIN')")
    @GetMapping("/getMeetById/{meetId}")
    public ResponseMessage<MeetResponse> getMeetById(@PathVariable Long meetId){

        return meetService.getMeetById(meetId);
    }

    // Not : getAllMeetByAdvisorAsPage() **************************************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllMeetByAdvisorAsPage")
    public ResponseEntity<Page<MeetResponse>> getAllMeetByAdvisorAsPage(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        String username = (String) httpServletRequest.getAttribute("username");
        Pageable pageable = PageRequest.of(page,size, Sort.by("date").descending());
        Page<MeetResponse> meet = meetService.getAllMeetByAdvisorTeacherAsPage(username,pageable);
        return ResponseEntity.ok(meet);
    }






}