package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.entity.concretes.Meet;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.request.MeetRequestWithoutId;
import com.schoolmanagement.payload.response.MeetResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.MeetRepository;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.utils.Messages;
import com.schoolmanagement.utils.TimeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final AdvisorTeacherService advisorTeacherService;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    // Not :  Save() *************************************************************************
    public ResponseMessage<MeetResponse> save(String username, MeetRequestWithoutId meetRequest) {

        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherByUsername(username).orElseThrow(()->
                new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE_WITH_USERNAME, username)));

        // !!! toplanti saat kontrolu
        if(TimeControl.check(meetRequest.getStartTime(), meetRequest.getStopTime()))
            throw new BadRequestException(Messages.TIME_NOT_VALID_MESSAGE);

        // !!! toplantiya katilacak ogrenciler icin yeni meeting saatlerinde cakisma var mi kontrolu
        for (Long studentId : meetRequest.getStudentIds()) {
            boolean check =    studentRepository.existsById(studentId);
            if(!check) throw  new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, studentId));

            checkMeetConflict(studentId, meetRequest.getDate(),meetRequest.getStartTime(), meetRequest.getStopTime());
        }

        // !!! Meet e katilacak olan Student lar getiriliyor
        List<Student> students = studentService.getStudentByIds(meetRequest.getStudentIds());
        // !!! Meet nesnesi olusturup ilgili fieldlar setleniyor
        Meet meet = new Meet();
        meet.setDate(meetRequest.getDate());
        meet.setStartTime(meetRequest.getStartTime());
        meet.setStopTime(meetRequest.getStopTime());
        meet.setStudentList(students);
        meet.setDescription(meetRequest.getDescription());
        meet.setAdvisorTeacher(advisorTeacher);

        //!!! save islemi
        Meet savedMeet = meetRepository.save(meet);

        //!!! Response nesnesi olusturuluyor

        return ResponseMessage.<MeetResponse>builder()
                .message("Meet Saved Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(createMeetResponse(savedMeet))
                .build();
    }

    private void checkMeetConflict(Long studentId, LocalDate date, LocalTime startTime, LocalTime stopTime){

        List<Meet> meets = meetRepository.findByStudentList_IdEquals(studentId);
        // TODO : meet size kontrol edilecek
        for(Meet meet : meets){

            LocalTime existingStartTime =  meet.getStartTime();
            LocalTime existingStopTime =  meet.getStopTime();

            if(meet.getDate().equals(date) &&
                    ((startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) || // yeni gelen meetingin startTime bilgisi mevcut mettinglerden herhangi birinin startTim,e ve stopTime arasinda mi ???
                            (stopTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime)) || //  yeni gelen meetingin stopTime bilgisi mevcut mettinglerden herhangi birinin startTim,e ve stopTime arasinda mi ???
                            (startTime.isBefore(existingStartTime) && stopTime.isAfter(existingStopTime)) ||
                            (startTime.equals(existingStartTime) && stopTime.equals(existingStopTime)))){
                throw new ConflictException(Messages.MEET_EXIST_MESSAGE);
            }
        }

    }
    private MeetResponse createMeetResponse(Meet meet) {
        return MeetResponse.builder()
                .id(meet.getId())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .stopTime(meet.getStopTime())
                .description((meet.getDescription()))
                .advisorTeacherId(meet.getAdvisorTeacher().getId())
                .teacherSsn(meet.getAdvisorTeacher().getTeacher().getSsn())
                .teacherName(meet.getAdvisorTeacher().getTeacher().getName())
                .students(meet.getStudentList())
                .build();
    }

    // Not : getAll() *************************************************************************
    public List<MeetResponse> getAll() {

        return meetRepository.findAll()
                .stream()
                .map(this::createMeetResponse)
                .collect(Collectors.toList());
    }

    // Not :  getMeetById() ********************************************************************
    public ResponseMessage<MeetResponse> getMeetById(Long meetId) {

        Meet meet = meetRepository.findById(meetId).orElseThrow(()->
                new ResourceNotFoundException(String.format(Messages.MEET_NOT_FOUND_MESSAGE,meetId)));

        return ResponseMessage.<MeetResponse>builder()
                .message("Meet Successfully found")
                .httpStatus(HttpStatus.OK)
                .object(createMeetResponse(meet))
                .build();
    }

    // Not : getAllMeetByAdvisorAsPage() **************************************************
    public Page<MeetResponse> getAllMeetByAdvisorTeacherAsPage(String username, Pageable pageable) {
        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherByUsername(username).orElseThrow(()->
                new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE_WITH_USERNAME,username)));

        return meetRepository.findByAdvisorTeacher_IdEquals(advisorTeacher.getId(), pageable) // advisorTeacher.getMeet()
                .map(this::createMeetResponse);
    }
}