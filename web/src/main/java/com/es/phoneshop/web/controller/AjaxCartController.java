package com.es.phoneshop.web.controller;

import com.es.core.exceptions.OutOfStockException;
import com.es.core.services.cart.CartService;
import com.es.core.model.cart.CartItem;
import com.es.core.services.cart.TotalPriceService;
import com.es.phoneshop.web.validators.CartItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    private static final String SUCCESS_MESSAGE = "success";

    private CartService cartService;
    private TotalPriceService totalPriceService;
    private CartItemValidator cartItemValidator;
    private MessageSource messageSource;

    @Autowired
    public AjaxCartController(CartService cartService, TotalPriceService totalPriceService, CartItemValidator cartItemValidator, MessageSource messageSource) {
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
        this.cartItemValidator = cartItemValidator;
        this.messageSource = messageSource;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(cartItemValidator);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> addPhone(@RequestBody @Validated CartItem cartItem, Errors errors) {
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> response = new HashMap<>();
        if (errors.hasErrors()) {
            response.put("message", messageSource.getMessage(errors.getAllErrors().get(0).getCode(), null, locale));
            return response;
        }
        try {
            cartService.addPhone(cartItem.getPhoneId(), cartItem.getQuantity());
            response.put("cartItemsAmount", cartService.getQuantityOfProducts());
            response.put("cartItemsPrice", totalPriceService.getTotalPriceOfProducts());
            response.put("message", SUCCESS_MESSAGE);
            return response;
        } catch (OutOfStockException exception) {
            response.put("message", messageSource.getMessage("outOfStockMessage", null, locale));
            return response;
        }
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Map<String, Object> handle() {
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> response = new HashMap<>();
        response.put("message", messageSource.getMessage("quantity.NAN", null, locale));
        return response;
    }
}
