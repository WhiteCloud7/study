[参考](https://blog.csdn.net/weixin_44207403/article/details/106736102?ops_request_misc=%257B%2522request%255Fid%2522%253A%252232d38a872bc9357d21ce37e50a5b0faf%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=32d38a872bc9357d21ce37e50a5b0faf&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-106736102-null-null.142^v102^pc_search_result_base8&utm_term=spring&spm=1018.2226.3001.4187)
[这个有步骤图](https://blog.csdn.net/weixin_68522070/article/details/141360851?ops_request_misc=%257B%2522request%255Fid%2522%253A%252232d38a872bc9357d21ce37e50a5b0faf%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=32d38a872bc9357d21ce37e50a5b0faf&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-2-141360851-null-null.142^v102^pc_search_result_base8&utm_term=spring&spm=1018.2226.3001.4187)
# Spring Core
## IOC(控制反转)：
1. 基本概念
   - ***控制反转***IoC(Inversion of Control)，是一种设计思想，DI(依赖注入)是实现IoC的一种方法，也有人认为DI只是IoC的另一种说法。没有IoC的程序中 , 我们使用面向对象编程 , 对象的创建与对象间的依赖关系完全硬编码在程序中，对象的创建由程序自己控制，控制反转后将对象的创建转移给第三方。就如我们写service时，不需要实现Dao层，第三方帮我们是实现了，我们只需要new一个即可。
   - 控制反转是一种通过描述（XML或注解）并通过第三方去生产或获取特定对象的方式。在Spring中实现控制反转的是IoC容器，其实现方法是***依赖注入***（Dependency Injection,DI）。
2. 接下来通过例子理解控制反转和依赖注入，先是控制反转：
- 首先写个bean.xml(resources下):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- id是bean的id是唯一的 name是bean名字可重复，未设置id可用name来
    找bean class是实体类位置前面两个都没设置可以用实体类名.class来找 属性里是实体类的名字和值 -->
    <bean id="person" name="person" class="com.CloudWhite.Entity.Person">
        <property name="name" value="张三"></property>
        <property name="age" value="18"></property>
    </bean>
</beans>
``` 
- 然后写个测试类：
```java
//解析beam文件
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
Person person = (Person) context.getBean("person");//getBean参数是bean的id
person.show();
```
可以发现new。而是用bean的id来new的，这就是控制反转。由此
Person对象是谁创建的 ? Person对象是由Spring创建的
Person对象的属性是怎么设置的 ? Person对象的属性是由Spring容器设置的
这个过程就叫控制反转
控制:谁来控制对象的创建,传统应用程序的对象是由程序本身控制创建的,使用Spring后,对象是由Spring来创建的
反转:程序本身不创建对象, 变成被动的接收对象
- 补充一下构造函数，无参构造函数xml无需改动，而有参则要加入如下：
```xml
<!-- 第一种根据index参数下标设置 -->
<bean id="person" class="com.CloudWhite.Entity.Person">
   <!-- index指构造方法 , 下标从0开始 -->
   <constructor-arg index="0" value="kuangshen2"/>
</bean>
<!-- 第二种根据参数名字设置 -->
<bean id="person" class="com.CloudWhite.Entity.Person">
   <!-- name指参数名 -->
   <constructor-arg name="name" value="kuangshen2"/>
</bean>
<!-- 第三种根据参数类型设置 -->
<bean id="person" class="com.CloudWhite.Entity.Person">
   <constructor-arg type="java.lang.String" value="kuangshen2"/>
</bean>
```
## 依赖注入：
我们分set注入和构造注入两种，构造器注入就是我前面解释控制反转的例子，不说了
**set注入**（重点）： 
- 要求被注入的属性,必须有set方法,set方法的方法名由set+属性首字母大写,如果属性是boolean类型,没有set方法，则方法名由is + 属性首字母大写
- 注入方法同上xml，这里再说一下一些特殊形式的注入：
  - collection注入，比如数组：
  ```xml
  <property name="books">
         <array>
             <value>西游记</value>
             <value>红楼梦</value>
             <value>水浒传</value>
         </array>
     </property>
  ``` 
  - Map注入：
  ```xml
  <map>
        <entry key="中国邮政" value="456456456465456"/>
        <entry key="建设" value="1456682255511"/>
    </map>
  ```  
  - null注入:`<property name="wife"><null/></property>`
  - props注入：
  ```xml
  <props>
         <prop key="学号">20190604</prop>
         <prop key="性别">男</prop>
     </props>
  ```
  - p命名空间注入：先导入`xmlns:p="http://www.springframework.org/schema/p"`，然后`<bean id="user" class="..." p:name="狂神" p:age="18"/>`
  - c命名空间注入，先导入`xmlns:c="http://www.springframework.org/schema/c"`,然后`<bean id="user" class="..." c:name="狂神" c:age="18"/>`
## **关于其他xml配置**：
  - `<!--设置别名：在获取Bean的时候可以使用别名获取--><alias name="person" alias="me"/>`
  - `import resource="beans.xml" 导入其他xml文件`
  - `<!--设置作用域：singleton(单例)、prototype(多例)、session、request --><bean id="user" class="..." scope="singleton"/>`
    - singleton：当一个bean的作用域为Singleton，那么Spring IoC容器中只会存在一个共享的bean实例，并且所有对bean的请求，只要id与该bean定义相匹配，则只会返回bean的同一实例。Singleton是单例类型，就是在创建起容器时就同时自动创建了一个bean的对象，不管你是否使用，他都存在了，每次获取到的对象都是同一个对象。
    - prototype：当一个bean的作用域为Prototype，表示一个bean定义对应多个对象实例。Prototype作用域的bean会导致在每次对该bean请求（将其注入到另一个bean中，或者以程序的方式调用容器的getBean()方法）时都会创建一个新的bean实例。Prototype是原型类型，它在我们创建容器的时候并没有实例化，而是当我们获取bean的时候才会去创建一个对象，而且我们每次获取到的对象都不是同一个对象。
    - 根据经验，对有状态的bean应该使用prototype作用域，而对无状态的bean则应该使用singleton作用域。
    - request：当一个bean的作用域为request，表示该bean会为每一次HTTP请求创建一个新实例。即每次HTTP请求将会有各自的bean实例，它们依据某个bean定义创建而成。该作用域仅在基于Web的Spring ApplicationContext情形下有效。
    - session：当一个bean的作用域为session，表示该bean为每一次HTTP Session创建一个新实例。即执行过程中同个session内的bean是同一个实例。仅在基于Web的Spring ApplicationContext情形下有效。
  - `<!--设置自动装配：byName(根据名称)、byType(根据类型)--><bean id="user" class="..." autowire="byName"/>`
  - `<!--设置延迟加载：true(延迟加载)、false(非延迟加载)--><bean id="user" class="..." lazy-init="true"/>`
  - `<!--设置初始化方法和销毁方法--><bean id="user" class="..."init-method="init" destroy-method="destroy"/>`
  - `<!--设置工厂方法--><bean id="user" class="..." factory-method="getInstance"/>`
  - `<!--设置工厂Bean--><bean id="user" class="..." factory-bean="factory"/>`
## ***自动注入***： 
***这里只自动注入其他bean，即需要注入的实体类有其他有其他的类，不自动注入基本数据类型***
1. 原理：
   - 组件扫描(component scanning)：spring会自动发现应用上下文中所创建的bean；
   - 自动装配(autowiring)：spring自动满足bean之间的依赖，也就是我们说的IoC/DI；
2. **这里推荐使用注解而不是使用xml**，但我们还是先介绍一下用xml：
   1. byName：bean标签里加`autowire="byName"`，这里要保证该bean里同类型的对象只有一个，否则报错
   2. byType：bean标签里加`autowire="byType"`，这里无需保证同类型的对象只有一个
3. **注解**：
   1. 现在xml头部改成如下：
   ```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"

       xmlns:context="http://www.springframework.org/schema/context"

       xsi:schemaLocation="
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd

       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
   ```
   2. ***注解自动注入***： 先再xml加上`<context:annotation-config/>`
      - @Autowired：按类型自动转配的，不支持id匹配，*使用时删除原有的set方法*，相当于bytype
      - @Qualifier：按id匹配的，*使用时删除原有的set方法*，相当于byname
      - @Resource(可选的默认name)：这里如果有指定name就按指定name，没有就按byname，还没有就bytype，*使用时删除原有的set方法*，*以上对于删除set方法，也不可以删，在set方法上注解即可，下面的也同理。*
   3. ***其他配置，以下就不止是只注入其他bean，具体自行看下面理解***
      - `<context:component-scan base-package="com.CloudWhite.Entity"/>`: 配置要扫描的包. 然后在指定包里的类加上注解@Component("可写的bean的id")，就相当于xml里的bean标签
      - @value("可选的默认赋值")：给属性赋值，这里就可以去掉基本数据类型的set方法
      - @Component有三个衍生注解，即@Controller、@Service、@Repository，分别对应控制层、业务层、持久层，使用了就将其交给spring管理。
## 基于Java类进行配置：
以下一个例子了解注解作用：
```java
//先写一个实体类
@Component  //将这个类自动注入为bean，上面有说
public class Dog {
   public String name = "dog";
}
//然后来看配置类，注意两个注解
@Configuration  //代表这是一个配置类
public class MyConfig {

   @Bean //通过方法注册一个bean，这里的返回值就Bean的类型，方法名就是bean的id！
   public Dog dog(){
       return new Dog();
  }
}
@Import(MyConfig2.class)//想导入其他配置类，用这个注解也写在类外部
```
# Spring AOP
## 代理模式：
# Spring MVC
# Spring Boot
# Spring Data
# Spring Security
# Spring Cloud
# Spring webflux