package com.example.costumerentalsystem.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
import com.example.costumerentalsystem.repository.CostumeRepository;
import com.example.costumerentalsystem.service.CostumeService;

@Controller
@RequestMapping("/costumes")
public class CostumeController {
    public CostumeController(CostumeService costumeService, CostumeRepository costumeRepository) {
        this.costumeService = costumeService;
        this.costumeRepository = costumeRepository;
    }


    private final CostumeService costumeService;

    private final CostumeRepository costumeRepository;

    @GetMapping("/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        Costume costume = costumeService.getCostumeById(id);
        model.addAttribute("costume", costume);
        return "costume-detail";
    }

    // 🟢 เมธอดสำหรับบันทึกข้อมูลชุดใหม่ (รองรับฟอร์ม Add New Costume ที่ส่งมาทาง /admin/costumes/save)
    @PostMapping("/admin/costumes/save")
    public String saveNewCostume(@ModelAttribute Costume costume,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "src/main/resources/static/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // สร้างโฟลเดอร์อัตโนมัติถ้ายังไม่มี
                }

                // ตั้งชื่อไฟล์ใหม่ด้วย UUID ป้องกันชื่อซ้ำกัน
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, imageFile.getBytes());

                // บันทึก Path สำหรับดึงรูปแสดงผลบนเว็บ
                costume.setImageUrl("/uploads/" + fileName);
            }

            costumeService.saveCostume(costume);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    // 🟢 ชี้ไปยัง templates/admin/costume-edit.html
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Costume costume = costumeService.getCostumeById(id);
        model.addAttribute("costume", costume);
        return "admin/costume-edit";
    }

    // 🟢 เมธอดรองรับการอัปโหลดไฟล์และอัปเดตข้อมูลชุดเดิม
    @PostMapping("/update/{id}")
    public String updateCostume(@PathVariable Long id,
                                @ModelAttribute Costume costumeDetails,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Costume existingCostume = costumeService.getCostumeById(id);
            if (existingCostume != null) {
                existingCostume.setName(costumeDetails.getName());
                existingCostume.setCategory(costumeDetails.getCategory());
                existingCostume.setPrice(costumeDetails.getPrice());
                existingCostume.setStatus(costumeDetails.getStatus());

                // รองรับทั้งชื่อตัวแปร file หรือ imageFile
                MultipartFile uploadFile = (file != null && !file.isEmpty()) ? file : imageFile;

                if (uploadFile != null && !uploadFile.isEmpty()) {
                    String uploadDir = "src/main/resources/static/uploads/";
                    File dir = new File(uploadDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    String fileName = UUID.randomUUID().toString() + "_" + uploadFile.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + fileName);
                    Files.write(filePath, uploadFile.getBytes());

                    existingCostume.setImageUrl("/uploads/" + fileName);
                }

                costumeRepository.save(existingCostume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    // ลบชุดเช่า
    @GetMapping("/delete/{id}")
    public String deleteCostume(@PathVariable Long id) {
        costumeRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}