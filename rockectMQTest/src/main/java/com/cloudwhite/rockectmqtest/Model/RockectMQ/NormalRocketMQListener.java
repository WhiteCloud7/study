package com.cloudwhite.rockectmqtest.Model.RockectMQ;

import io.netty.util.CharsetUtil;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component  // rocketmq 消息监听，@RocketMQMessageListener中的selectorExpression为tag，默认为*
@RocketMQMessageListener(topic = "queue_test_topic",selectorExpression="*",consumerGroup = "queue_group_test")
public class NormalRocketMQListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String msg = new String(body, CharsetUtil.UTF_8);
        System.out.println("接收到消息："+msg);
    }
}
