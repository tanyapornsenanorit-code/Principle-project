package com.example.costumerentalsystem.controller;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    public UserController(UserService userService) {
        this.userService = userService;
    }


    private final UserService userService;

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        User user = userService.findByEmail(userEmail);

        if (user == null) {
            user = new User();
            user.setUsername(userEmail);
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUserData,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String currentEmail = principal.getName();
            userService.updateUserProfile(currentEmail, updatedUserData);

            // 🟢 2. ดึงข้อมูล Authentication ปัจจุบัน
            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

            // 🟢 3. กำหนดชื่อผู้ใช้งานใหม่ (รับค่าจากอีเมลหรือ username ที่อัปเดตแล้ว)
            String newUsername = (updatedUserData.getEmail() != null && !updatedUserData.getEmail().trim().isEmpty())
                    ? updatedUserData.getEmail()
                    : (updatedUserData.getUsername() != null ? updatedUserData.getUsername() : currentEmail);

            // 🟢 4. สร้าง Authentication token ชุดใหม่และอัปเดตลงใน SecurityContextHolder
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                newUsername,
                currentAuth.getCredentials(),
                currentAuth.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            redirectAttributes.addFlashAttribute("successMessage", "บันทึกข้อมูลส่วนตัวเรียบร้อยแล้ว!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาด: " + e.getMessage());
        }
        return "redirect:/user/profile";
    }
@GetMapping("/rentals")
    public String showUserRentalsPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        User user = userService.findByEmail(userEmail);
        model.addAttribute("user", user);

        return "user/rentals"; // หรือเปลี่ยนเป็น "redirect:/rentals" หากต้องการส่งไปหน้า /rentals
    }
}
