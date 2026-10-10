package com.example.costumerentalsystem.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.enums.Role;
import com.example.costumerentalsystem.service.RentalService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    private final RentalService rentalService;

    public OrderController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/orders")
    public String myOrders(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("loggedInUser", loggedInUser.getUsername());

        if (loggedInUser.getRole() == Role.ADMIN) {
            model.addAttribute(
                    "orders",
                    rentalService.findAll(null, Pageable.unpaged()).getContent());
        } else {
            model.addAttribute(
                    "orders",
                    rentalService.findByUser(
                            loggedInUser.getId(),
                            Pageable.unpaged()).getContent());
        }

        return "orders";
    }
}
