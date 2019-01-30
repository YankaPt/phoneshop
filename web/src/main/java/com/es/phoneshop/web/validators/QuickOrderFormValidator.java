package com.es.phoneshop.web.validators;

import com.es.core.services.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;

@Service
public class QuickOrderFormValidator implements Validator {
    private final PhoneService phoneService;

    @Autowired
    public QuickOrderFormValidator(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Map<String, String> map = (Map<String, String>)target;
        map.forEach((paramName, values) -> {
            if (paramName.startsWith("quantity") && values.matches("\\d*")) {
                if ("".equals(values)) {
                    errors.reject("quantity"+paramName, "field.required");
                } else if (values.matches("[-0]+.*")) {
                    errors.reject("quantity"+paramName, "quantity.negativeOrZero");
                } else if (values.matches(".*\\D+.*")) {
                    errors.reject("quantity" + paramName, "quantity.NAN");
                }
            } else if (paramName.startsWith("code") && values.matches("\\d*")) {
                if ("".equals(values)) {
                    errors.reject("code" + paramName, "field.required");
                } else if (paramName.startsWith("code") && !isPhoneExist(paramName)) {
                    errors.reject("code"+paramName, "incorrectCode");
                }
            }
        });
    }
    private boolean isPhoneExist(String paramName) {
        Long phoneId = Long.parseLong(paramName.substring("code".length()));
        return phoneService.get(phoneId).isPresent();
    }
}
