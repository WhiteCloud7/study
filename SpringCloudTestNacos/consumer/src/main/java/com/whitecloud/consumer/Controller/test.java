package com.whitecloud.consumer.Controller;

import com.whitecloud.consumer.Entity.Order;
import com.whitecloud.consumer.Entity.User;
import com.whitecloud.consumer.servce.testService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class test {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    testService2 testService2;
    @Value("${spring.datasource.url}")
    private String source;
    @GetMapping("test")
    public String test(){
        return source;
    }
    @GetMapping("getOrder1")
    public Object getOrder1(){
        return testService2.getOrder1();
    }

    @PostMapping("getOrder2")
    public Object getOrder2(User user){
        return testService2.getOrder1();
    }
}
