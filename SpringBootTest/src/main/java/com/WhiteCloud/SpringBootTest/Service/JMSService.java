package com.WhiteCloud.SpringBootTest.Service;

import jakarta.jms.Destination;

public interface JMSService {
    public void sendMessage(Destination destination, String messageContent);

    public void receiveMessage(String msg);
}
