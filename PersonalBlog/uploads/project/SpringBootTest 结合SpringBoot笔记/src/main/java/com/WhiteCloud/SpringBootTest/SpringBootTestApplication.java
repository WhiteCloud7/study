package com.WhiteCloud.SpringBootTest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

//@SpringBootApplication
//@MapperScan("com/WhiteCloud/SpringBootTest/Dao")
@EnableCaching
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })//这样测试时可以关闭安全默认配置
public class SpringBootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTestApplication.class, args);
	}

}
