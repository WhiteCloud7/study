package com.example.SpringCloudTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

public class test {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping()
    public void test(){
        restTemplate.getForObject();
    }
}
