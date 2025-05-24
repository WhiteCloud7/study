package com.cloudwhite.rockectmqtest.Controller;

import com.cloudwhite.rockectmqtest.Model.RockectMQ.NormalRocketMQListener;
import com.cloudwhite.rockectmqtest.rocketMQ.normalMessageProducer;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @Autowired
    normalMessageProducer normalMessageProducer;
    @GetMapping("testNormalMessage")
    public String testNormalMessage(){
        normalMessageProducer.sendMsg("你好");
        return "ok";
    }
}
