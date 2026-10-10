package com.example.costumerentalsystem.controller;

import java.util.List;
import java.util.stream.Collectors;

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
    public OrderController(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }


    private final RentalRepository rentalRepository;

    @GetMapping("/orders")
    public String myOrders(Model model, HttpSession session) {
        // 1. ดึง User จาก Session ที่เราใช้ล็อกอินจริงในระบบ
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // ถ้ายังไม่ได้ล็อกอิน ค่อยพาไปหน้า Login
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        String currentUsername = loggedInUser.getUsername();
        model.addAttribute("loggedInUser", currentUsername);

        List<Rental> allRentals = rentalRepository.findAll();
        List<Rental> orders;

        // 2. เช็คว่าเป็น Admin หรือไม่
        boolean isAdmin = (loggedInUser.getRole() == Role.ADMIN) ||
                          (currentUsername != null && currentUsername.toLowerCase().contains("admin"));

        if (isAdmin) {
            // 👑 แอดมิน: เห็นรายการเช่าทั้งหมด
            orders = allRentals;
        } else {
            // 🟢 User ธรรมดา: กรองดูเฉพาะออเดอร์ของตัวเอง ("ของใครของมัน")
            orders = allRentals.stream().filter(r -> {
                try {
                    if (r.getUser() != null && r.getUser().getUsername() != null) {
                        return r.getUser().getUsername().equalsIgnoreCase(currentUsername);
                    }
                } catch (Exception e) {
                    // ป้องกัน Error กรณีข้อมูลไม่มี User ผูกไว้
                }
                return false;
            }).collect(Collectors.toList());
        }

        model.addAttribute("orders", orders);
        return "orders";
    }
}