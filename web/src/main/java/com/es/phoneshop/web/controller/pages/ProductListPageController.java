package com.es.phoneshop.web.controller.pages;

import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.services.ProductListPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {
    private static final String REDIRECTING_ADDRESS = "redirect:/productList?";
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
    public String showProductList(Integer pageNumber, Boolean previousPage, Boolean nextPage, String search, String orderBy, boolean isAscend, Model model, Authentication authentication) {
        if (search != null) {
            model.addAttribute("phones", productListPageService.findPhonesBySearch(search));
        } else {
            pageNumber = productListPageService.resolveParamsAndGetPage(pageNumber, previousPage, nextPage);
            model.addAttribute("phones", productListPageService.findPhonesForCurrentPage(pageNumber, AMOUNT_OF_SHOWED_PRODUCTS, orderBy, isAscend));
        }
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("userName", authentication.getName());
        } else {
            model.addAttribute("userName", null);
        }
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts(cartService.getCart()));
        model.addAttribute("maxPageNumber", phoneService.getTotalAmountOfPhonesWithPositiveStock() / AMOUNT_OF_SHOWED_PRODUCTS);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("isAscend", isAscend);
        return "productList";
    }

    @PostMapping()
    public String doPost(@RequestParam(value = "pageNumber", required = false) Integer pageNumber, String search, String orderBy, boolean isAscend) {
        return search == null ? orderBy == null ? REDIRECTING_ADDRESS + "pageNumber=" + pageNumber : REDIRECTING_ADDRESS + "pageNumber=" + pageNumber + "&orderBy=" + orderBy + "&isAscend=" + isAscend : REDIRECTING_ADDRESS + "search=" + search;
    }
}