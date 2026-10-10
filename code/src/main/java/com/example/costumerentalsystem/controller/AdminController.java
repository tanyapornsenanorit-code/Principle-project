package com.example.costumerentalsystem.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.costumerentalsystem.domain.entity.Costume;
import com.example.costumerentalsystem.domain.entity.Rental;
import com.example.costumerentalsystem.repository.CostumeRepository;
import com.example.costumerentalsystem.repository.RentalRepository;
import com.example.costumerentalsystem.service.CostumeService;
import com.example.costumerentalsystem.domain.enums.CostumeStatus;
import com.example.costumerentalsystem.domain.enums.RentalStatus;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CostumeService costumeService;
    private final CostumeRepository costumeRepository;
    private final RentalRepository rentalRepository;

    public AdminController(CostumeService costumeService, 
                           CostumeRepository costumeRepository,
                           RentalRepository rentalRepository) {
        this.costumeService = costumeService;
        this.costumeRepository = costumeRepository;
        this.rentalRepository = rentalRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("costumes", costumeService.getAllCostumes());
        model.addAttribute("rentals", rentalRepository.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/costumes/new")
    public String showAddCostumeForm(Model model) {
        model.addAttribute("costume", new Costume());
        return "admin/costume-form";
    }

    @GetMapping("/costumes/edit/{id}")
    public String showAdminEditForm(@PathVariable Long id, Model model) {
        Costume costume = costumeService.getCostumeById(id);
        model.addAttribute("costume", costume);
        return "admin/costume-edit";
    }

    @PostMapping("/costumes/save")
    public String saveCostume(@ModelAttribute("costume") Costume costume,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        if (costume.getId() != null && (imageFile == null || imageFile.isEmpty())) {
            Costume existing = costumeRepository.findById(costume.getId()).orElse(null);
            if (existing != null) {
                costume.setImageUrl(existing.getImageUrl());
            }
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadDir = Paths.get("src/main/resources/static/uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Files.copy(imageFile.getInputStream(), uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                costume.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (costume.getStatus() == null) {
            costume.setStatus(CostumeStatus.AVAILABLE);
        }

        costumeRepository.save(costume);
        return "redirect:/admin/dashboard";
    }

    // 1. อัปเดตเลขพัสดุและขนส่ง
    @PostMapping("/rentals/update-tracking/{id}")
    public String updateTracking(@PathVariable Long id,
                                 @RequestParam("courier") String courier,
                                 @RequestParam("trackingNo") String trackingNo) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental != null) {
            rental.setCourier(courier);
            rental.setTrackingNo(trackingNo);
            // TODO(A5): เปลี่ยนเป็นเรียก RentalLifecycleService.ship() ผ่าน State Pattern
            rental.setStatus(RentalStatus.SHIPPED);
            rentalRepository.save(rental);
        }
        return "redirect:/admin/dashboard";
    }

    //  2. บันทึกรับคืนชุด -> เปลี่ยนสถานะชุดในร้านกลับเป็น "ว่าง"
    @PostMapping("/rentals/return/{id}")
    public String returnCostume(@PathVariable Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental != null) {
            // อัปเดตสถานะการเช่าเป็น "คืนเรียบร้อย" (เส้น Timeline จะสิ้นสุด)
            // TODO(A5): เปลี่ยนเป็นเรียก RentalLifecycleService.returnItem() ผ่าน State Pattern
            rental.setStatus(RentalStatus.RETURNED);
            rentalRepository.save(rental);

            // อัปเดตชุดเช่ากลับมาพร้อมให้คนอื่นเช่าต่อ
            if (rental.getCostume() != null) {
                Costume costume = rental.getCostume();
                costume.setStatus(CostumeStatus.AVAILABLE);
                costumeRepository.save(costume);
            }
        }
        return "redirect:/admin/dashboard";
    }
}