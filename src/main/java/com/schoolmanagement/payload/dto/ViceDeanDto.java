package com.schoolmanagement.payload.dto;

import com.schoolmanagement.entity.concretes.ViceDean;
import com.schoolmanagement.payload.request.ViceDeanRequest;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class ViceDeanDto {

    public ViceDean dtoViceBean(ViceDeanRequest viceDeanRequest){

        return ViceDean.builder()
                .birthDay(viceDeanRequest.getBirthDay())
                .username(viceDeanRequest.getUsername())
                .name(viceDeanRequest.getName())
                .surname(viceDeanRequest.getSurname())
                .password(viceDeanRequest.getPassword())
                .ssn(viceDeanRequest.getSsn())
                .birthPlace(viceDeanRequest.getBirthPlace())
                .phoneNumber(viceDeanRequest.getPhoneNumber())
                .gender(viceDeanRequest.getGender())
                .build();

    }
}