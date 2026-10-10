package com.designhub;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThreeDController {

    // Когда пользователь переходит на /3Dtable
    @GetMapping("/3Dtable")
    public String forwardToRoot() {
        return "redirect:/";
    }

    // Когда пользователь обновляет страницу внутри /3Dtable
    // (например, /3Dtable/some-route) — отдаём index.html
    @GetMapping("/3Dtable/{path:^(?!assets|models|uploads|api|webjars|static).*}")
    public String forward() {
        return "forward:/index.html";
    }
}