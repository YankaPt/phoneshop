package com.es.phoneshop.web.controller.pages;

import com.es.core.exceptions.OutOfStockException;
import com.es.core.services.cart.CartService;
import com.es.core.services.cart.TotalPriceService;
import com.es.phoneshop.web.services.ErrorsWrapper;
import com.es.phoneshop.web.services.QuickOrderResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping(value = "/quickOrderEntry")
public class QuickOrderEntryPageController {
    private static final Integer INITIAL_NUMBER_OF_ROWS = 2;
    private static final String QUICK_ORDER_ENTRY_ADDRESS = "quickOrderEntry";
    private static final String GET_REDIRECT_ADDRESS = "redirect:/quickOrderEntry";
    private static final String NUMBER_OF_ROWS_ATTRIBUTE = "numberOfRows";
    private static final String FORM_DATA_ATTRIBUTE = "formData";

    private final QuickOrderResolver quickOrderResolver;
    private final CartService cartService;
    private final TotalPriceService totalPriceService;

    @Autowired
    public QuickOrderEntryPageController(QuickOrderResolver quickOrderResolver, CartService cartService, TotalPriceService totalPriceService) {
        this.quickOrderResolver = quickOrderResolver;
        this.cartService = cartService;
        this.totalPriceService = totalPriceService;
    }

    @GetMapping
    public String getQuickOrderEntry(Model model) {
        Integer numberOfRows = model.containsAttribute(NUMBER_OF_ROWS_ATTRIBUTE) ? (Integer) model.asMap().get(NUMBER_OF_ROWS_ATTRIBUTE) : INITIAL_NUMBER_OF_ROWS;
        Map<Long, Integer> formData = new HashMap<>();
        initializeFormData(formData, numberOfRows);
        model.addAttribute("cartItemsAmount", cartService.getQuantityOfProducts());
        model.addAttribute("cartItemsPrice", totalPriceService.getTotalPriceOfProducts(cartService.getCart()));
        model.addAttribute(FORM_DATA_ATTRIBUTE, formData);
        return QUICK_ORDER_ENTRY_ADDRESS;
    }

    private void initializeFormData(Map<Long, Integer> formData, int size) {
        for (int i = 0; i < size; i++) {
            formData.put((long) i, null);
        }
    }

    @PostMapping
    public String addToCart(@RequestParam Map<String, String> formData, RedirectAttributes redirectAttributes, Locale locale) {
        System.out.println("Post is working, size of formData is " + formData.size());
        ErrorsWrapper errorsWrapper = new ErrorsWrapper();
        Map<Long, Integer> resolvedData = quickOrderResolver.resolveFormData(formData, errorsWrapper);
        resolvedData.forEach((key, value) -> System.out.println(key+" "+value));
        addResolvedToCart(resolvedData, errorsWrapper);
        redirectAttributes.addFlashAttribute("oldCartItems", formData);
        redirectAttributes.addFlashAttribute("errors", errorsWrapper.getErrors());
        return GET_REDIRECT_ADDRESS;
    }

    private void addResolvedToCart(Map<Long, Integer> resolvedData, ErrorsWrapper errorsWrapper) {
        resolvedData.forEach((key, value) -> {
            try {
                cartService.addPhone(key, value);
            } catch (OutOfStockException exception) {
                errorsWrapper.getErrors().getAllErrors().add(new ObjectError(key.toString(), "outOfStock"));
            }
        });
    }
}
