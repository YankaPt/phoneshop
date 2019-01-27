package com.es.phoneshop.web.services;

import com.es.core.model.phone.Phone;
import com.es.core.services.phone.PhoneService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductListPageServiceTest {
    private static final String KEYWORD = "keyword";
    private static final String ORDER_BY = "brand";
    private static final boolean IS_ASCEND = true;
    private static final int LIMIT = 10;
    private static final int FIRST_PAGE_NUMBER = 1;

    private List<Phone> phones = new ArrayList<>();
    private PhoneService phoneService = mock(PhoneService.class);
    private ProductListPageService productListPageService = new ProductListPageService(phoneService);

    @Before
    public void setUp() {
        phones.clear();
        for (int i = 0; i < 50; i++) {
            phones.add(new Phone());
        }
    }

    @Test
    public void shouldReturnPhonesByKeyword() {
        when(phoneService.getPhonesByKeyword(KEYWORD)).thenReturn(phones);

        assertArrayEquals(phones.toArray(), productListPageService.findPhonesBySearch(KEYWORD).toArray());
    }

    @Test
    public void shouldFindLimitAmountOfPhonesForFirstPage() {
        when(phoneService.getTotalAmountOfPhonesWithPositiveStock()).thenReturn((long) phones.size());
        when(phoneService.getPhonesWithPositiveStockWithOrderBy(FIRST_PAGE_NUMBER - 1, LIMIT, ORDER_BY, IS_ASCEND)).thenReturn(phones.subList(FIRST_PAGE_NUMBER - 1, LIMIT));

        List<Phone> actualPhoneList = productListPageService.findPhonesForCurrentPage(FIRST_PAGE_NUMBER, LIMIT, ORDER_BY, IS_ASCEND);

        assertEquals(LIMIT, actualPhoneList.size());
        assertArrayEquals(phones.subList(FIRST_PAGE_NUMBER-1, LIMIT).toArray(), actualPhoneList.toArray());
    }

    @Test
    public void shouldResolveAsFirstPage() {
        assertEquals(1, (int) productListPageService.resolveParamsAndGetPage(1, true, null));
    }

    @Test
    public void shouldResolveAsSecondPage() {
        assertEquals(2, (int) productListPageService.resolveParamsAndGetPage(1, null, true));
    }
}