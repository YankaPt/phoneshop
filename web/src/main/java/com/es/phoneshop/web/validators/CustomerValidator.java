package com.es.phoneshop.web.validators;

import com.es.core.services.order.Customer;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class CustomerValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Customer.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required", "Field can't be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "field.required", "Field can't be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "field.required", "Field can't be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNumber", "field.required", "Field can't be empty");
    }
}
