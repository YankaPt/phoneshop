package com.es.phoneshop.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class ErrorLocalizer {
    private final MessageSource messageSource;

    @Autowired
    public ErrorLocalizer(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Map<Long, String> localizeErrors(Errors errors, Locale locale) {
        Map<Long, String> localizeErrors = new HashMap<>();
        errors.getAllErrors().forEach((objectError -> localizeErrors.put(Long.parseLong(objectError.getCode().substring(8)),
                messageSource.getMessage(objectError.getDefaultMessage(), null, locale))));
        return localizeErrors;
    }
}
