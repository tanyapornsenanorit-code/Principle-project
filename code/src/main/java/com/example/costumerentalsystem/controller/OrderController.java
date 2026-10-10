package com.example.costumerentalsystem.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.repository.RentalRepository;
import com.example.costumerentalsystem.domain.enums.Role;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private RentalRepository rentalRepository;

    @GetMapping({"/orders", "/user/rentals"})
    public String myOrders(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        String currentUsername = loggedInUser.getUsername();
        model.addAttribute("loggedInUser", currentUsername);

        List<Rental> allRentals = rentalRepository.findAll();
        List<Rental> orders;

        boolean isAdmin = (loggedInUser.getRole() == Role.ADMIN) ||
                          (currentUsername != null && currentUsername.toLowerCase().contains("admin"));

        if (isAdmin) {
            orders = allRentals;
        } else {
            orders = allRentals.stream().filter(r -> {
                try {
                    if (r.getUser() != null && r.getUser().getUsername() != null) {
                        return r.getUser().getUsername().equalsIgnoreCase(currentUsername);
                    }
                } catch (Exception e) {
                }
                return false;
            }).collect(Collectors.toList());
        }

        model.addAttribute("orders", orders);
        return "orders";
    }
}