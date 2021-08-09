package com.assignment.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String getLogin() {
        return "redirect:/home/notes/1?sortField=id&sortDir=asc";
    }
}
