package com.whitecloud.provider.servce;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.whitecloud.provider.Dao.testDao;
import com.whitecloud.provider.Entity.Order;
import com.whitecloud.provider.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public class testService {
    @Autowired
    private testDao testDao;
    private Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    public Order getOrder1(){
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss"));
        Order order = new Order("test1",String.valueOf(snowflake.nextId()),"tttt",dateTime);
        return order;
    }

    public Order getOrder2(User user){
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss"));
        Order order = new Order("test1",String.valueOf(snowflake.nextId()),"tttt",dateTime);
        order.setUser(user);
        return order;
    }
}
