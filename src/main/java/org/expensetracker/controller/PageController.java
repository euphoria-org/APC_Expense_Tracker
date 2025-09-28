package org.expensetracker.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PageController {
//    @GetMapping("/dashboard")
//    public String dashboard() {
//        return "dashboard.html";
//    }
//
//    @GetMapping("/history")
//    public String history() {
//        return "/src/history.html";
//    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/src/admin.html";
        }
        return "redirect:/src/dashboard.html";
    }

    @GetMapping("/admin")
    public String admin() {
        return "redirect:/src/admin.html";
    }

}
