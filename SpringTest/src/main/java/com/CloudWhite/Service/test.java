package com.CloudWhite.Servce;
import com.CloudWhite.Entity.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test{
    public static void main(String[] args) {
        //解析beam文件
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Person person = (Person) context.getBean("person");//getBean参数是bean的id
        person.show();
    }
}
