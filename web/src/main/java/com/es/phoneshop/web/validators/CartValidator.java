package com.es.phoneshop.web.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;

@Service
public class CartValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Map<String, String> map = (Map<String, String>)target;
        map.forEach((paramName, values) -> {
            if (paramName.matches("\\d*") && values!=null) {
                if ("".equals(values)) {
                    errors.reject("quantity"+paramName, "field.required");
                } else if (values.matches("[-0]+.*")) {
                    errors.reject("quantity"+paramName, "quantity.negativeOrZero");
                } else if (values.matches(".*\\D+.*")) {
                    errors.reject("quantity" + paramName, "quantity.NAN");
                }
            }
        });
    }
}
