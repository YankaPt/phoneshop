package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.services.ProductListPageService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ProductListPageControllerTest {
    private CartService cartService = mock(CartService.class);
    private PhoneService phoneService = mock(PhoneService.class);
    private TotalPriceService totalPriceService = mock(TotalPriceService.class);
    private ProductListPageService productListPageService = mock(ProductListPageService.class);
    private ProductListPageController controller = new ProductListPageController(phoneService, cartService, totalPriceService, productListPageService);

    @BeforeClass
    public static void init() {

    }
}