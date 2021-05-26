package com.sexton.example.controller;

import com.sexton.example.model.response.NameResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping
    public NameResponse name(HttpServletRequest request) {
        return new NameResponse(request.getHeader("X-Name"));
    }
}
