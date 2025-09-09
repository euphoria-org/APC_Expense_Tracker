package org.expensetracker.controller;

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



}
