package com.es.phoneshop.web.services;

import com.es.phoneshop.web.validators.CartValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartFormResolver {
    private final CartValidator cartValidator;

    @Autowired
    public CartFormResolver(CartValidator cartValidator) {
        this.cartValidator = cartValidator;
    }

    public Map<Long, Integer> resolveFormData(Map<String, String> formData, ErrorsWrapper errorsWrapper) {
        DataBinder dataBinder = new DataBinder(formData);
        dataBinder.setValidator(cartValidator);
        dataBinder.validate();
        errorsWrapper.setErrors(dataBinder.getBindingResult());
        Map<Long, Integer> result = new HashMap<>();
        formData.forEach((paramName, values) -> {
            if (paramName.matches("\\d*") && values!=null) {
                if (errorsWrapper.getErrors().getAllErrors().stream().noneMatch(objectError -> objectError.getCode().matches("quantity"+paramName)))
                    result.put(Long.parseLong(paramName), Integer.parseInt(values));
            }
        });
        return result;
    }
}
