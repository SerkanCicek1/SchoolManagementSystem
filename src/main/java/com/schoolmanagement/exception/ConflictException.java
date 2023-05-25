package com.schoolmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // status kodunu setliyoruz
public class ConflictException extends RuntimeException{

    public ConflictException(String message) {
        super(message);
    }
}