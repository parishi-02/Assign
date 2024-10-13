package com.parishi.controller;

import com.parishi.dto.TraineeDTO;
import com.parishi.utility.ExcelFileReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class HomeController {
    @RequestMapping("/login")
    public String getDashboard() {
        return "login-page";
    }

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

}