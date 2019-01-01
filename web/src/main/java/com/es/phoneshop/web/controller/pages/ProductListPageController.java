package com.es.phoneshop.web.controller.pages;

import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.services.ProductListPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {
    private static final String REDIRECTING_ADDRESS = "redirect:/productList?pageNumber=";
    private static final Integer AMOUNT_OF_SHOWED_PRODUCTS = 10;
    private final PhoneService phoneService;
    private final CartService cartService;
    private final TotalPriceService totalPriceService;
    private final ProductListPageService productListPageService;

    @Autowired
    public ProductListPageController(PhoneService phoneService, CartService cartService, TotalPriceService totalPriceService, ProductListPageService productListPageService) {
        this.phoneService = phoneService;
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
        this.productListPageService = productListPageService;
    }

    @GetMapping()
    public String showProductList(Integer pageNumber, Boolean previousPage, Boolean nextPage, String search, Model model) {
        if (search != null) {
            model.addAttribute("phones", productListPageService.findPhonesBySearch(search));
        } else {
            pageNumber = productListPageService.resolveParamsAndGetPage(pageNumber, previousPage, nextPage);
            model.addAttribute("phones", productListPageService.findPhonesForCurrentPage(pageNumber, AMOUNT_OF_SHOWED_PRODUCTS));
        }
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts());
        model.addAttribute("maxPageNumber", phoneService.getTotalAmountOfPhonesWithPositiveStock() / AMOUNT_OF_SHOWED_PRODUCTS);
        model.addAttribute("pageNumber", pageNumber);
        return "productList";
    }

    @PostMapping()
    public String doPost(@RequestParam(value = "pageNumber", required = false) Integer pageNumber, String search) {
        return search == null ? REDIRECTING_ADDRESS + pageNumber : REDIRECTING_ADDRESS + search;
    }
}