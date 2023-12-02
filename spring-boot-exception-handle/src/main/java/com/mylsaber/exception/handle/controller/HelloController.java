package com.mylsaber.exception.handle.controller;

import com.mylsaber.exception.handle.domain.ResponseResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author mylsaber
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public ResponseResult<String> sayHello(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        return ResponseResult.success("hello " + name);
    }

    @RequestMapping("testDate")
    public Date processApi(@RequestParam("data") Date date) {
        return date;
    }

    @RequestMapping("currentUser")
    public ResponseResult<String> saveSomeObj(@ModelAttribute("currentUser") String user) {
        return ResponseResult.success("currentUser：" + user);
    }
}
