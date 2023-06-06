package com.schoolmanagement.payload.response;

import com.schoolmanagement.entity.enums.Term;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EducationTermResponse {


    private Long id ;
    private Term term;
    private LocalDate startDate;
    private  LocalDate endDate;
    private LocalDate lastRegistrationDate;

}