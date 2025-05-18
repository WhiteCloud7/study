package com.CloudWhite.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("person")
public class Person{
    @Value("hello")
    private String name;
    @Value("男")
    private String sex;
    @Value("18")
    private int age;
    private Student stu1;
    private Student stu2;
    public Teacher getTeacher() {
        return teacher;
    }
    private Teacher teacher;
    public Person(){};
    @Autowired
    public Person(Student stu1, Student stu2, Teacher teacher) {
        this.stu1 = stu1;
        this.stu2 = stu2;
        this.teacher = teacher;
    }
    public Person(String name, String sex, int age) {
        this.name = name;
        this.age = age;

    }public Student getStu1() {
        return stu1;

    }public Student getStu2() {
        return stu2;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }
}