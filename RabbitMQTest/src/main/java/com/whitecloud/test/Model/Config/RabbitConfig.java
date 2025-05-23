package com.whitecloud.test.Model.Config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();  //使用json序列化机制，进行消息转换
    }
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setMessageConverter(messageConverter()); //注意合并前面写的json序列化，因为只能有一个RabbitTemplate的Bean多了会注入错误
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数,不如可能出错导致不回调
        rabbitTemplate.setMandatory(true);
        //当你找到或没找到交换机都会触发，这里手动写了没找到才触发
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if(!ack){
                    System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
                    System.out.println("ConfirmCallback:     "+"确认情况："+ack);
                    System.out.println("ConfirmCallback:     "+"原因："+cause);
                }
            }
        });
        //当你没找到路由键时触发
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("ReturnCallback:     "+"消息："+returnedMessage.getMessage());
                System.out.println("ReturnCallback:     "+"回应码："+returnedMessage.getReplyCode());
                System.out.println("ReturnCallback:     "+"回应信息："+returnedMessage.getReplyText());
                System.out.println("ReturnCallback:     "+"交换机："+returnedMessage.getExchange());
                System.out.println("ReturnCallback:     "+"路由键："+returnedMessage.getRoutingKey());
            }
        });
        return rabbitTemplate;
    }
}
