package com.whitecloud.provider.Controller;

import com.whitecloud.provider.Entity.Order;
import com.whitecloud.provider.Entity.User;
import com.whitecloud.provider.servce.testService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    testService testService = new testService();
    @GetMapping("/getOrder1")
    public Order getOrder1(){
        return testService.getOrder1();
    }

    @PostMapping("/getOrder2")
    public Order getOrder2(@RequestBody  User user){
        return testService.getOrder2(user);
    }
}
