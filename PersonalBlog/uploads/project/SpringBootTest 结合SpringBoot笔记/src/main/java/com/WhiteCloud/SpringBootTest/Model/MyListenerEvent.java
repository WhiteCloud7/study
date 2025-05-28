package com.WhiteCloud.SpringBootTest.Model;

import org.springframework.context.ApplicationEvent;

public class MyListenerEvent extends ApplicationEvent {
    private String event;

    public MyListenerEvent(Object object){
        super(object);
    }

    public MyListenerEvent(Object object, String event){
        super(object);
        this.event=event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
