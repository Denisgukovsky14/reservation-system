package com.sarcofuckusLesson.designhub.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/home")
    public String home() {
        return "forward:/src/pages/home/home.html";
    }



}
