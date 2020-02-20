package com.example.photo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String redirectFromRootURLToOrdersList() {
        return "redirect:/photos";
    }
}