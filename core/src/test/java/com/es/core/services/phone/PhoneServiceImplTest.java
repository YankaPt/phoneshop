package com.es.core.services.phone;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PhoneServiceImplTest {
    private static final Integer OFFSET = 0;
    private static final Integer LIMIT = 3;
    private static final Long PHONE_ID = 1L;
    private static final String PHONE_BRAND = "SomeBrand";
    private static final String PHONE_MODEL = "SomeModel";
    private static final String KEYWORD = "Brand";
    private PhoneDao phoneDao = mock(PhoneDao.class);
    private Phone phone = new Phone();
    private List<Phone> phones = new ArrayList<>();
    private List<Phone> phonesByKeyword = new ArrayList<>();
    private PhoneService phoneService = new PhoneServiceImpl(phoneDao);

    @Before
    public void setUp() {
        phone = new Phone();
        phone.setId(PHONE_ID);
        phone.setModel(PHONE_MODEL);
        phone.setBrand(PHONE_BRAND);
        phones.clear();
        for (int i = 0; i < LIMIT; i++) {
            phones.add(new Phone());
        }
        phonesByKeyword.clear();
        phonesByKeyword.add(phone);
        when(phoneDao.findAllAvailable(OFFSET, LIMIT)).thenReturn(phones);
        when(phoneDao.get(PHONE_ID)).thenReturn(Optional.of(phone));
        when(phoneDao.findAllByKeyword(KEYWORD)).thenReturn(phonesByKeyword);
        when(phoneDao.getTotalAmountOfAvailablePhones()).thenReturn((long) phones.size());
    }

    @Test
    public void shouldReturnPhonesWithPositiveStock() {
        List<Phone> actualListOfPhones = phoneService.getPhonesWithPositiveStock(OFFSET, LIMIT);

        assertEquals(phones, actualListOfPhones);
    }

    @Test
    public void shouldReturnPhoneById() {
        Phone actualPhone = phoneService.get(PHONE_ID).get();

        assertEquals(phone, actualPhone);
    }

    @Test
    public void shouldReturnPhonesByKeyword() {
        List<Phone> actualListOfPhones = phoneService.getPhonesByKeyword(KEYWORD);

        assertEquals(1, actualListOfPhones.size());
        assertEquals(phone, actualListOfPhones.get(0));
    }

    @Test
    public void shouldReturnTotalAmountOfPhones() {
        long actualAmount = phoneService.getTotalAmountOfPhonesWithPositiveStock();

        assertEquals(phones.size(), actualAmount);
    }
}