package yoon.community.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(Authentication authentication) {
        return authentication.getName();

    }

    @GetMapping("/all")
    public String all() {
        return "all";
    }

}
