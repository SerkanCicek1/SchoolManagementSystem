package com.schoolmanagement.payload.dto;

import com.schoolmanagement.entity.concretes.Dean;
import com.schoolmanagement.payload.request.DeanRequest;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
//@Component
public class DeanDto {

    // Dto - POJO
    public Dean dtoDean(DeanRequest deanRequest) {
        return Dean.builder()
                .username(deanRequest.getUsername())
                .name(deanRequest.getName())
                .surname(deanRequest.getSurname())
                .password(deanRequest.getPassword())
                .ssn(deanRequest.getSsn())
                .birthDay(deanRequest.getBirthDay())
                .birthPlace(deanRequest.getBirthPlace())
                .phoneNumber(deanRequest.getPhoneNumber())
                .gender(deanRequest.getGender())
                .build();
    }
}