package com.CloudWhite.Eneity;

public class Person {
    private String name;
    private String age;
    Person(){
        super();
    }

    Person(String name,String age,String iq){
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void show(){
        System.out.println(name);
        System.out.println(age);
    }

}
