package com.es.phoneshop.web.services;

import org.springframework.validation.Errors;

public class ErrorsWrapper {
    private Errors errors;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
