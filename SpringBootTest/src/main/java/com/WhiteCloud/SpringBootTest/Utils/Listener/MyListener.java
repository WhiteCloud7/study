package com.WhiteCloud.SpringBootTest.Utils.Listener;

import com.WhiteCloud.SpringBootTest.Model.MyException;
import com.WhiteCloud.SpringBootTest.Model.MyListenerEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyListener implements ApplicationListener<MyListenerEvent> {
    @Override
    public void onApplicationEvent(MyListenerEvent myListenerEvent){
        String event = myListenerEvent.getEvent();
        System.out.println("监听到了事件："+event);
    }
}
