package com.whitecloud.consumer.Controller;

import com.whitecloud.consumer.Entity.Order;
import com.whitecloud.consumer.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class test {
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("getOrder1")
    public Object getOrder1(){
        Order order = new Order();
        String url = "http://provider/getOrder1";
        order = restTemplate.getForObject(url,Order.class);
        return order;
    }

    @PostMapping("getOrder2")
    public Object getOrder2(User user){
        Order order = new Order();
        String url = "http://provider/getOrder2";
        order = restTemplate.postForObject(url,user,Order.class);
        return order;
    }
}
