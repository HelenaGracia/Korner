package com.example.korner.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/korner/home")
public class HomeController {
    @GetMapping("")
    public String showHome(){
        return "home";
    }

}
