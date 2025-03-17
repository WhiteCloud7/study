package com.CloudWhite.SpringMvcTest.Controller;

import com.CloudWhite.SpringMvcTest.Model.userInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @GetMapping(value = "/test")
    public userInfo test(@RequestBody userInfo userInfo){

        return userInfo;
    }
}
