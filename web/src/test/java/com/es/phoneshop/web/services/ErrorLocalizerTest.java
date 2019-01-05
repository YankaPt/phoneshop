package com.es.phoneshop.web.services;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorLocalizerTest {
    private static final long CART_ITEM_PHONE_ID = 1234L;
    private static final String ERROR_CODE = "quantity"+CART_ITEM_PHONE_ID;
    private static final String ERROR_MESSAGE = "someError";
    private static final String LOCALIZED_MESSAGE = "SomeMessage";
    private static final Locale TEST_LOCALE = Locale.ENGLISH;

    private Errors errors = mock(Errors.class);
    private ObjectError objectError = mock(ObjectError.class);
    private MessageSource messageSource = mock(MessageSource.class);
    private ErrorLocalizer errorLocalizer = new ErrorLocalizer(messageSource);

    @Before
    public void setUp() {
        List<ObjectError> objectErrors = new ArrayList<>();
        objectErrors.add(objectError);
        when(messageSource.getMessage(ERROR_MESSAGE, null, TEST_LOCALE)).thenReturn(LOCALIZED_MESSAGE);
        when(errors.getAllErrors()).thenReturn(objectErrors);
        when(objectError.getCode()).thenReturn(ERROR_CODE);
        when(objectError.getDefaultMessage()).thenReturn(ERROR_MESSAGE);
    }

    @Test
    public void shouldLocalizeError() {
        Map<Long, String> actualLocalizedErrors = errorLocalizer.localizeErrors(errors, TEST_LOCALE);

        assertTrue(actualLocalizedErrors.containsKey(CART_ITEM_PHONE_ID));
        assertEquals(1, actualLocalizedErrors.size());
        assertEquals(LOCALIZED_MESSAGE, actualLocalizedErrors.get(CART_ITEM_PHONE_ID));
    }
}