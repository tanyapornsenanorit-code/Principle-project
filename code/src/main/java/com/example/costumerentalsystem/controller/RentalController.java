package com.example.costumerentalsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.service.CostumeService;
import com.example.costumerentalsystem.service.RentalService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CostumeService costumeService;

    @GetMapping("/new/{costumeId}")
    public String showRentalForm(@PathVariable Long costumeId, Model model, HttpSession session) {
        // เช็คว่าล็อกอินหรือยัง ถ้ายังให้ดีดไปหน้า login
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Rental rental = new Rental();
        model.addAttribute("rental", rental);
        model.addAttribute("costume", costumeService.getCostumeById(costumeId));
        return "user/rental-form";
    }

    @PostMapping("/save")
    public String createRental(@ModelAttribute Rental rental, 
                             @RequestParam Long costumeId, 
                             HttpSession session) {
        
        // 1. ดึง User จาก Session ที่กำลังล็อกอินอยู่
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 2. 🟢 ผูก User เข้ากับ Rental ทันทีก่อนบันทึก เพื่อให้แยกออเดอร์ "ของใครของมัน"
        rental.setUser(loggedInUser);

        // 3. บันทึกข้อมูลการเช่า
        rentalService.createRental(rental, costumeId);

        // 4. บันทึกเสร็จเด้งไปหน้าติดตามสถานะออเดอร์ของตัวเองทันที
        return "redirect:/orders";
    }
}