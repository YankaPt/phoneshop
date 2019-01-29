package com.es.phoneshop.web.validators;

import com.es.core.services.order.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerValidatorTest {
    private CustomerValidator customerValidator = new CustomerValidator();

    @Test
    public void shouldSupportCustomer() {
        Customer customer = new Customer();

        boolean isSupport = customerValidator.supports(customer.getClass());

        assertTrue(isSupport);
    }

    @Test
    public void shouldNotSupportCommonObject() {
        Object object = new Object();

        boolean isSupport = customerValidator.supports(object.getClass());

        assertFalse(isSupport);
    }
}