package com.zhangjun_study.build.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/build")
@RestController
public class BuildController {

    @GetMapping(value = "/test")
    public String testBlock(){
        return "hello,build...";
    }
}
