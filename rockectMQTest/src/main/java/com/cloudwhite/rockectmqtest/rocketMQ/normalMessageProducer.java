package com.cloudwhite.rockectmqtest.rocketMQ;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class normalMessageProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Value("${rocketmq.producer.send-message-timeout}")
    private Integer messageTimeOut;
    // 发送普通消息
    public SendResult sendMsg(String msgBody){
        SendResult result = rocketMQTemplate.syncSend("queue_test_topic", MessageBuilder.withPayload(msgBody).build());
        return result;
    }

    //发送异步消息 在SendCallback中可处理相关成功失败时的逻辑
    public void sendAsyncMsg(String msgBody) {
        rocketMQTemplate.asyncSend("queue_test_topic", MessageBuilder.withPayload(msgBody).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功:" + Arrays.toString(sendResult.getRawRespBody()));// 处理消息发送成功逻辑
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送失败！");// 处理消息发送异常逻辑
            }
        });
    }
    //发送延时消息,延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
    public void sendDelayMsg(String msgBody, Integer delayLevel){
        rocketMQTemplate.syncSend("queue_test_topic",MessageBuilder.withPayload(msgBody).build(),messageTimeOut,delayLevel);
    }
    // 发送带tag的消息,直接在topic后面加上":tag"
    public void sendTagMsg(String msgBody){
        rocketMQTemplate.syncSend("queue_test_topic:tag1",MessageBuilder.withPayload(msgBody).build());
    }
}