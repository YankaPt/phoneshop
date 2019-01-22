package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {
    private static final String REDIRECTING_ADDRESS = "redirect:/productList?pageNumber=";
    private static final Integer AMOUNT_OF_SHOWED_PRODUCTS = 10;
    private final PhoneService phoneService;
    private final CartService cartService;
    private final TotalPriceService totalPriceService;

    @Autowired
    public ProductListPageController(PhoneService phoneService, CartService cartService, TotalPriceService totalPriceService) {
        this.phoneService = phoneService;
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
    }

    @GetMapping()
    public String showProductList(Integer pageNumber, Boolean previousPage, Boolean nextPage, String search, Model model) {
        if (search != null) {
            model.addAttribute("phones", findPhonesBySearch(search));
        } else {
            pageNumber = resolveParamsAndGetPage(pageNumber, previousPage, nextPage);
            model.addAttribute("phones", findPhonesForCurrentPage(pageNumber));
        }
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts(cartService.getCart()));
        model.addAttribute("maxPageNumber", phoneService.getTotalAmountOfPhonesWithPositiveStock() / AMOUNT_OF_SHOWED_PRODUCTS);
        model.addAttribute("pageNumber", pageNumber);
        return "productList";
    }

    @PostMapping()
    public String doPost(@RequestParam(value = "pageNumber", required = false) Integer pageNumber, String search) {
        return search == null ? REDIRECTING_ADDRESS + pageNumber : REDIRECTING_ADDRESS + search;
    }

    Integer resolveParamsAndGetPage(Integer pageNumber, Boolean previousPage, Boolean nextPage) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (previousPage != null) {
            pageNumber = pageNumber > 1 ? pageNumber - 1 : 1;
        } else if (nextPage != null) {
            pageNumber++;
        }
        return pageNumber;
    }

    List<Phone> findPhonesForCurrentPage(Integer pageNumber) {
        Long totalAmountOfProductsOnStock = phoneService.getTotalAmountOfPhonesWithPositiveStock();
        if (AMOUNT_OF_SHOWED_PRODUCTS * (pageNumber - 1) > totalAmountOfProductsOnStock) {
            pageNumber = ((Long) (totalAmountOfProductsOnStock / AMOUNT_OF_SHOWED_PRODUCTS)).intValue();
        }
        return phoneService.getPhonesWithPositiveStock(AMOUNT_OF_SHOWED_PRODUCTS * (pageNumber - 1), AMOUNT_OF_SHOWED_PRODUCTS);
    }

    List<Phone> findPhonesBySearch(String search) {
        return phoneService.getPhonesByKeyword(search);
    }

}