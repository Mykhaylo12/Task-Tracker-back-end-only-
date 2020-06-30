package com.example.demo.rest;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getusers")
    public List<UserDto> getAllUsers(@RequestParam(value = "limit",
            required = false, defaultValue = "0") Integer limit) {
        return userService.getAll(limit);
    }
}
