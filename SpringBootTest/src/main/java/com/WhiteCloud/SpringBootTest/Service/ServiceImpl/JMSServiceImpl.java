//package com.WhiteCloud.SpringBootTest.Service.ServiceImpl;
//
//import com.WhiteCloud.SpringBootTest.Service.JMSService;
//import com.WhiteCloud.SpringBootTest.Utils.Config.JMSConfig;
//import jakarta.jms.Destination;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.jms.core.JmsMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class JMSServiceImpl implements JMSService {
//    private JmsMessagingTemplate jmsMessagingTemplate;
//    @Autowired
//    public JMSServiceImpl(JmsMessagingTemplate jmsMessagingTemplate) {
//        this.jmsMessagingTemplate = jmsMessagingTemplate;
//    }
//
//    @Override
//    public void sendMessage(Destination destination, String messageContent){
//        jmsMessagingTemplate.convertAndSend(destination,messageContent);
//    }
//
//    @Override
//    @JmsListener(destination = JMSConfig.queueName)
//    public void receiveMessage(String msg){
//        System.out.println("收到的消息"+msg);
//    }
//}
