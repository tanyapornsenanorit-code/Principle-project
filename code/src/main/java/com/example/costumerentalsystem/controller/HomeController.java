package com.example.costumerentalsystem.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.repository.UserRepository;
import com.example.costumerentalsystem.service.CostumeService;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.domain.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final CostumeService costumeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HomeController(CostumeService costumeService, 
                          UserRepository userRepository, 
                          PasswordEncoder passwordEncoder) {
        this.costumeService = costumeService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. หน้าแรก: ค้นหา และแสดงชุดตามสิทธิ์ผู้ใช้งาน
    @GetMapping("/")
    public String home(@RequestParam(value = "search", required = false) String search, 
                       Model model, 
                       HttpSession session) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        boolean isAdmin = false;

        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser.getUsername());
            model.addAttribute("user", loggedInUser);
            model.addAttribute("role", loggedInUser.getRole() != null ? loggedInUser.getRole().name() : "USER");
            
            //  เช็คสิทธิ์ Admin อย่างรัดกุม (รองรับทั้ง Role และอีเมลที่มีคำว่า admin)
            if (loggedInUser.getRole() == Role.ADMIN) {
                isAdmin = true;
            } else if (loggedInUser.getUsername() != null && loggedInUser.getUsername().toLowerCase().contains("admin")) {
                isAdmin = true;
            }
        } else {
            model.addAttribute("loggedInUser", "Guest");
            model.addAttribute("role", "GUEST");
        }

        // ส่งตัวแปร isAdmin ไปเปิด/ปิดปุ่มในหน้า HTML
        model.addAttribute("isAdmin", isAdmin);

        // ดึงข้อมูลชุดทั้งหมด
        List<Costume> costumes;
        if (search != null && !search.trim().isEmpty()) {
            costumes = costumeService.searchCostumes(search);
        } else {
            costumes = costumeService.getAllCostumes();
        }

        //  กรองข้อมูลชุดเช่าสำหรับลูกค้าทั่วไป
        if (isAdmin) {
            // ถ้าเป็นแอดมิน -> เห็นครบทุกชุดทุกสถานะ
        } else {
            // ถ้าเป็นลูกค้า -> เห็นเฉพาะชุดที่ว่าง
            costumes = costumes.stream()
                    .filter(c -> c.getStatus() == CostumeStatus.AVAILABLE)
                    .collect(Collectors.toList());
        }

        model.addAttribute("costumes", costumes);
        model.addAttribute("searchKeyword", search);

        return "index";
    }

    // 2. เข้าสู่ระบบ
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {
        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("loggedInUser", user);
            
            // เช็คเด้งไป Dashboard สำหรับ Admin
            if (user.getRole() == Role.ADMIN) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }

        model.addAttribute("error", "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
        return "login";
    }

    // 3. สมัครสมาชิก
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, HttpSession session, Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 🔥 บังคับให้ผู้สมัครใหม่ทุกคนมีสิทธิ์เป็นแค่ USER เท่านั้น
        user.setRole(Role.USER);
        userRepository.save(user);

        // สมัครเสร็จทำการ Log-in ให้อัตโนมัติและไปที่หน้าแรก
        session.setAttribute("loggedInUser", user);
        return "redirect:/";
    }

    // 4. ออกจากระบบ
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }
}