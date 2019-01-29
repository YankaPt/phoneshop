package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.Order;
import com.es.core.services.order.Customer;
import com.es.core.services.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    private final OrderService orderService;

    @Autowired
    public OrderOverviewPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrderOverview(Model model) {
        if (!model.containsAttribute("orderId")) {
            return "redirect:/404page";
        }
        Order order = orderService.getOrder((long) model.asMap().get("orderId"));
        Customer customer = new Customer();
        customer.setFirstName(order.getFirstName());
        customer.setLastName(order.getLastName());
        customer.setAddress(order.getDeliveryAddress());
        customer.setContactNumber(order.getContactPhoneNo());
        customer.setAdditionalInformation(order.getAdditionalInformation());
        model.addAttribute("customer", customer);
        model.addAttribute("order", order);
        return "orderOverview";
    }
}
