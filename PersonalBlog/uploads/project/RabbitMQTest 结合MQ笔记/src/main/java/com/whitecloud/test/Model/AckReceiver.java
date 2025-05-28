package com.whitecloud.test.Model;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = "TestDirectQueue", ackMode = "MANUAL")
public class AckReceiver {
    @RabbitHandler
    public void process(Map map, Channel channel,Message message) throws IOException {
        String data = (String) map.get("messageData");
        System.out.println("收到反序列化的Map消息: " + data);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if (data.contains("reject")) {
                throw new RuntimeException("拒绝该消息");
            }
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            System.out.println("消息被拒绝");
            channel.basicReject(deliveryTag, false);
        }
    }
}