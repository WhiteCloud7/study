package com.whitecloud.consumer.servce;

import com.whitecloud.consumer.Entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@FeignClient("provider-service")
public interface testService2 {
    @GetMapping("getOrder1")
    public Order getOrder1();
    @GetMapping("getOrder2")
    public Order getOrder2();
}
