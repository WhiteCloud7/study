package com.whitecloud.consumer.servce;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.whitecloud.consumer.Dao.testDao;
import com.whitecloud.consumer.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class testService {
    @Autowired
    private testDao testDao;
    private Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    public User getOrder(){
        return null;
    }
}
