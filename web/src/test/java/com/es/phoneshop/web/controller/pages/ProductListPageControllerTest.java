package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.services.ProductListPageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ProductListPageControllerTest {
    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String ORDER_BY = "brand";
    private static final boolean IS_ASCEND = true;
    private static final int PAGE_NUMBER = 1;
    private static final int LIMIT = 5;
    private static final boolean IS_PREVIOUS_PAGE = false;
    private static final boolean IS_NEXT_PAGE = false;
    private static final String SEARCH = null;

    private List<Phone> phones = new ArrayList<>();
    private Model model = spy(Model.class);
    private Authentication authentication = mock(Authentication.class);
    private CartService cartService = mock(CartService.class);
    private PhoneService phoneService = mock(PhoneService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private ProductListPageService productListPageService = mock(ProductListPageService.class);
    private ProductListPageController controller = new ProductListPageController(phoneService, cartService, totalPriceService, productListPageService);

    @Before
    public void setUp() {
        when(productListPageService.resolveParamsAndGetPage(PAGE_NUMBER, IS_PREVIOUS_PAGE, IS_NEXT_PAGE)).thenReturn(PAGE_NUMBER);
        when(productListPageService.findPhonesForCurrentPage(PAGE_NUMBER, LIMIT, ORDER_BY, IS_ASCEND)).thenReturn(phones);
    }

    @Test
    public void shouldFindProducts() {
        controller.showProductList(PAGE_NUMBER, IS_PREVIOUS_PAGE, IS_NEXT_PAGE, SEARCH, ORDER_BY, IS_ASCEND, model, authentication);

        verify(model).addAttribute("phones", phones);
        verify(model).addAttribute(PAGE_NUMBER_ATTRIBUTE, PAGE_NUMBER);
    }
}