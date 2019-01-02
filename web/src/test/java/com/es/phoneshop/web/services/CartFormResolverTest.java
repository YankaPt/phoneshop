package com.es.phoneshop.web.services;

import com.es.core.model.cart.CartItem;
import com.es.phoneshop.web.validators.CartValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CartFormResolverTest {
    private static final long FIRST_ITEM_PHONE_ID = 1L;
    private static final long SECOND_ITEM_PHONE_ID = 2L;
    private static final long THIRD_ITEM_PHONE_ID = 3L;
    private static final int FIRST_ITEM_QUANTITY = 1;
    private static final int SECOND_ITEM_QUANTITY = 2;
    private static final String NEGATIVE_OR_ZERO_MESSAGE = "quantity.negativeOrZero";
    private static final String NAN_MESSAGE = "quantity.NAN";
    private static final String EMPTY_MESSAGE = "field.required";
    private Map<String, String> formData = new HashMap<>();
    private CartItem firstItem = new CartItem(FIRST_ITEM_PHONE_ID, FIRST_ITEM_QUANTITY);
    private CartItem secondItem = new CartItem(SECOND_ITEM_PHONE_ID, SECOND_ITEM_QUANTITY);
    private CartValidator validator = new CartValidator();
    private CartFormResolver cartFormResolver = new CartFormResolver(validator);

    @Before
    public void setUp() {
        formData.clear();
        formData.put("_method", "PUT");
        formData.put(firstItem.getPhoneId().toString(), firstItem.getQuantity().toString());
        formData.put(secondItem.getPhoneId().toString(), secondItem.getQuantity().toString());
    }

    @Test
    public void shouldNotResolveNotIntegerParams() {
        ErrorsWrapper errorsWrapper = new ErrorsWrapper();
        formData.put(Long.toString(THIRD_ITEM_PHONE_ID), "f");

        cartFormResolver.resolveFormData(formData, errorsWrapper);

        assertEquals(NAN_MESSAGE, errorsWrapper.getErrors().getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldNotResolveEmptyParams() {
        ErrorsWrapper errorsWrapper = new ErrorsWrapper();
        formData.put(Long.toString(THIRD_ITEM_PHONE_ID), "");

        cartFormResolver.resolveFormData(formData, errorsWrapper);

        assertTrue(errorsWrapper.getErrors().hasErrors());
        assertEquals(EMPTY_MESSAGE, errorsWrapper.getErrors().getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldNotResolveNegativeParams() {
        ErrorsWrapper errorsWrapper = new ErrorsWrapper();
        formData.put(Long.toString(THIRD_ITEM_PHONE_ID), "-1");

        cartFormResolver.resolveFormData(formData, errorsWrapper);

        assertTrue(errorsWrapper.getErrors().hasErrors());
        assertEquals("quantity" + THIRD_ITEM_PHONE_ID, errorsWrapper.getErrors().getAllErrors().get(0).getCode());
        assertEquals(NEGATIVE_OR_ZERO_MESSAGE, errorsWrapper.getErrors().getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldNotResolveZeroParams() {
        ErrorsWrapper errorsWrapper = new ErrorsWrapper();
        formData.put(Long.toString(THIRD_ITEM_PHONE_ID), "0");

        cartFormResolver.resolveFormData(formData, errorsWrapper);

        assertTrue(errorsWrapper.getErrors().hasErrors());
        assertEquals(NEGATIVE_OR_ZERO_MESSAGE, errorsWrapper.getErrors().getAllErrors().get(0).getDefaultMessage());
    }

}