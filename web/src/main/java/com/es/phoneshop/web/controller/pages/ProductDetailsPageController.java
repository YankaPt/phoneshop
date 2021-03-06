package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.core.services.phone.PhoneService;
import com.es.phoneshop.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    private final PhoneService phoneService;
    private final CartService cartService;
    private final TotalPriceService totalPriceService;

    @Autowired
    public ProductDetailsPageController(PhoneService phoneService, CartService cartService, TotalPriceService totalPriceService) {
        this.phoneService = phoneService;
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
    }

    @GetMapping()
    public String showProduct(Long phoneId, Model model, Authentication authentication) {
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts(cartService.getCart()));
        if (phoneId != null) {
            Optional<Phone> phone = phoneService.get(phoneId);
            phone.ifPresent(p -> model.addAttribute("phone", p));
        } else {
            return Constants.REDIRECTING_TO_404_ADDRESS;
        }
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("userName", authentication.getName());
        } else {
            model.addAttribute("userName", null);
        }
        return "productDetails";
    }
}
