package com.example.korner.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accessDenied")
public class AccessDeniedController {

    @GetMapping
    public String showAccessDeniedPage() {
        return "redirect:/korner/home" ;
    }
}
