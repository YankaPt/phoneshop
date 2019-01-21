package com.es.phoneshop.web.controller.pages;

import com.es.core.exceptions.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.services.cart.CartService;
import com.es.core.services.order.Customer;
import com.es.core.services.order.OrderService;
import com.es.phoneshop.web.validators.CustomerValidator;
import com.es.phoneshop.web.validators.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    private static final String REDIRECTING_ORDER_OVERVIEW_ADDRESS = "redirect:/orderOverview";
    private static final String REDIRECTING_GET_ADDRESS = "redirect:/order";
    private final OrderService orderService;
    private final CartService cartService;
    private final CustomerValidator customerValidator;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderPageController(OrderService orderService, CartService cartService, CustomerValidator customerValidator, OrderValidator orderValidator) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.customerValidator = customerValidator;
        this.orderValidator = orderValidator;
    }

    @GetMapping
    public String getOrder(Model model) {
        Order order;
        if (model.containsAttribute("order")) {
            order = (Order) model.asMap().get("order");
            model.addAttribute("errors", (BindingResult) model.asMap().get("errors"));
        } else {
            order = orderService.createOrder(cartService.getCart());
        }
        model.addAttribute("order", order);
        model.addAttribute("customer", new Customer());
        return "order";
    }

    @PostMapping
    public String placeOrder(Customer customer, RedirectAttributes redirectAttributes) {
        Order order = orderService.createOrder(cartService.getCart());
        DataBinder customerBinder = new DataBinder(customer);
        customerBinder.setValidator(customerValidator);
        if (hasErrors(order, customerBinder, redirectAttributes)) {
            return REDIRECTING_GET_ADDRESS;
        }
        DataBinder orderBinder = new DataBinder(order);
        orderBinder.setValidator(orderValidator);
        if (hasErrors(order, orderBinder, redirectAttributes)) {
            return REDIRECTING_GET_ADDRESS;
        }
        setCustomerInfoAndStatusToOrder(order, customer);
        try {
            orderService.placeOrder(order);
        } catch (OutOfStockException ex) {
            return REDIRECTING_GET_ADDRESS; //fix this stub with message and return order
        }
        cartService.getCart().getCartItems().clear();
        redirectAttributes.addFlashAttribute("orderId", order.getId());
        return REDIRECTING_ORDER_OVERVIEW_ADDRESS;
    }

    private void setCustomerInfoAndStatusToOrder(Order order, Customer customer) {
        order.setFirstName(customer.getFirstName());
        order.setLastName(customer.getLastName());
        order.setDeliveryAddress(customer.getAddress());
        order.setContactPhoneNo(customer.getContactNumber());
        order.setAdditionalInformation(customer.getAdditionalInformation());
        order.setStatus(OrderStatus.NEW);
    }

    private boolean hasErrors(Order order, DataBinder dataBinder, RedirectAttributes redirectAttributes) {
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", dataBinder.getBindingResult());
            redirectAttributes.addFlashAttribute("order", order);
            return true;
        }
        return false;
    }
}
