package com.example.costumerentalsystem.controller;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.dto.request.RentalCreateRequest;
import com.example.costumerentalsystem.service.CostumeService;
import com.example.costumerentalsystem.service.RentalService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rentals")
public class RentalController {
    public RentalController(RentalService rentalService, CostumeService costumeService) {
        this.rentalService = rentalService;
        this.costumeService = costumeService;
    }


    private final RentalService rentalService;

    private final CostumeService costumeService;

    @GetMapping("/new/{costumeId}")
    public String showRentalForm(
            @PathVariable Long costumeId,
            Model model,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("rental", new RentalCreateRequest(
                costumeId,
                null,
                null
        ));

        model.addAttribute(
                "costume",
                costumeService.getCostumeById(costumeId)
        );

        return "user/rental-form";
    }

    @PostMapping("/save")
    public String createRental(
            @ModelAttribute RentalCreateRequest request,
            @RequestParam Long costumeId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        RentalCreateRequest rentalRequest = new RentalCreateRequest(
                costumeId,
                request.startDate(),
                request.endDate()
        );

        rentalService.create(loggedInUser.getId(), rentalRequest);

        return "redirect:/orders";
    }
}
