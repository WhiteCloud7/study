package com.CloudWhite.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("teacher")
public class Teacher {
    @Value("nima")
    private String name;

    public String getName() {
        return name;
    }
}
