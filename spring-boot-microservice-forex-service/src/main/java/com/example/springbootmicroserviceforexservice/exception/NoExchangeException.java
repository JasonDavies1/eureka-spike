package com.example.springbootmicroserviceforexservice.exception;

import lombok.Data;

@Data
public class NoExchangeException extends Exception {

    public NoExchangeException(final String errorMessage){
        super(errorMessage);
    }

}
