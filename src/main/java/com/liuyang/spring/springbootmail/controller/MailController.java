package com.liuyang.spring.springbootmail.controller;

import com.liuyang.spring.springbootmail.entity.ResponseResult;
import com.liuyang.spring.springbootmail.service.MailServices;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {

    private static final Logger logger =  LoggerFactory.getLogger(MailController.class);

    @Resource
    MailServices mailServices;

    @GetMapping("/test")
    public ResponseResult roomTemperature(){
        logger.info("test...");
        mailServices.test();
        return ResponseResult.success();
    }
}
