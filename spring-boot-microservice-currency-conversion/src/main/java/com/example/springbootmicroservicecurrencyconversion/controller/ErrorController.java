package com.example.springbootmicroservicecurrencyconversion.controller;

import com.example.springbootmicroservicecurrencyconversion.exception.ServiceException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(ServiceException.class)
    public String error(final ServiceException e, final Model model){
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
