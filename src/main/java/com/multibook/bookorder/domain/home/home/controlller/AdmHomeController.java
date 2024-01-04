package com.multibook.bookorder.domain.home.home.controlller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
@RequiredArgsConstructor
public class AdmHomeController {
    @GetMapping("")
    public String showMain() {
        return "domain/home/home/adm/main";
    }
}
