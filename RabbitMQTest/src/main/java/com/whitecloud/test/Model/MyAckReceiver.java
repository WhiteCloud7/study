package com.whitecloud.test.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAckReceiver implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body = message.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> msgMap = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});

            String messageId = (String) msgMap.get("messageId");
            String messageData = (String) msgMap.get("messageData");
            String createTime = (String) msgMap.get("createTime");

            System.out.println(message.getMessageProperties().getMessageCount());
            System.out.println(message.getMessageProperties().getContentType());
            System.out.println(message.getMessageProperties().getReceivedUserId());
            System.out.println("  MyAckReceiver  messageId:" + messageId + "  messageData:" + messageData + "  createTime:" + createTime);
            System.out.println("消费的主题消息来自：" + message.getMessageProperties().getConsumerQueue());

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }
}
