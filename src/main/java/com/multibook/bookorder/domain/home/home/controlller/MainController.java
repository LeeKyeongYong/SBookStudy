package com.multibook.bookorder.domain.home.home.controlller;

import com.multibook.bookorder.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final Rq rq;

    @GetMapping("/")
    public String showHome(){
        return "domain/home/home/main";
    }
}
