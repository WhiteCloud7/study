package com.WhiteCloud.SpringBootTest.Utils.Config;

import jakarta.jms.Destination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JMSConfig {
    //发布/订阅队列
    public static final String topicName = "MyTopic";
    //点对点队列
    public static final String queueName = "MyQueue";

    @Bean
    public Destination topic(){
        return new ActiveMQTopic(topicName);
    }

    @Bean
    public Destination queue(){
        return new ActiveMQQueue(queueName);
    }
}
