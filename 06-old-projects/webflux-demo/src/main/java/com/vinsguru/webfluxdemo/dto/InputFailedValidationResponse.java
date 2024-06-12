package com.vinsguru.webfluxdemo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputFailedValidationResponse {

    private int errorCode;
    private int input;
    private String message;

}
