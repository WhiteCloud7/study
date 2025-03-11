# mybatis
参考博客：  
[简单介绍](https://blog.csdn.net/vcj1009784814/article/details/106391982)
[这个有具体的结构](https://blog.csdn.net/weixin_52937170/article/details/142468894)
## mybatis.xml配置文件（(resoures下)）：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver"
                          value="com.mysql.cj.jdbc.Driver"/>
                <property name="url"
                          value="jdbc:mysql://localhost:3306/csx_demo?serverTimezone=Asia/Shanghai&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mapper/UserMapper.xml"></mapper>
    </mappers>
</configuration>
```
- 常用标签
  - configuration：根元素，用于配置 MyBatis 的全局设置。
    - environments：定义多个环境，每个环境对应一个数据源和事务管理器。
      - transactionManager：定义事务管理器，用于管理数据库事务。
        - dataSource：定义数据源，用于连接数据库。
  - **mappers**：定义映射器，用于指定 MyBatis 的映射文件。   
    mapper的四中路径配置方式： 
    - resource：指定映射文件的路径。相当于在resources目录下的路径。
    - url：指定映射文件的 URL。
    - class：指定映射文件的类路径。相当于在src/main/java下的路径。
    - package：指定映射文件所在的包路径。相当于在src/main/java下的路径，但这个是直接指定包名，而不是具体的路径，扫描包下的所有映射文件。
- 常用配置(配置顺序必须按照这个顺序，不用的可以不写)
    - **properties**：一般将数据源的信息单独放在一个properties文件中,然后用这个标签引入,在下面environment标签中，就可以用${}占位符快速获取数据源的信息
    - settings：用来开启或关闭mybatis的一些特性，比如可以用`<setting name="lazyLoadingEnabled" value="true"/>`来开启延迟加载，可以用`<settings name="cacheEnabled" value="true"/>`来开启二级缓存
    - **typeAliases**：在mapper.xml中需要使用parameterType和resultType属性来配置SQL语句的输入参数类型和输出参数类型，类必须要写上全限定名，比如一个SQL的返回值映射为Student类，则resultType属性要写com.yogurt.po.Student，这太长了，所以可以用别名来简化书写，比如`<typeAlias type="com.yogurt.po.Student" alias="student"/>`
    之后就可以在resultType上直接写student，mybatis会根据别名配置自动找到对应的类。
    当然，如果想要一次性给某个包下的所有类设置别名，可以用如下的方式`<package name="com.yogurt.po"/>`
    如此，指定包下的所有类，都会以简单类名的小写形式，作为它的别名
    另外，对于基本的Java类型 -> 8大基本类型以及包装类，以及String类型，mybatis提供了默认的别名，别名为其简单类名的小写，比如原本需要写java.lang.String，其实可以简写为string
    - typeHandlers:用于处理Java类型和Jdbc类型之间的转换，mybatis有许多内置的TypeHandler，比如StringTypeHandler，会处理Java类型String和Jdbc类型CHAR和VARCHAR。这个标签用的不多
    - objectFactory:mybatis会根据resultType或resultMap的属性来将查询得到的结果封装成对应的Java类，它有一个默认的DefaultObjectFactory，用于创建对象实例。这个标签用的也不多
    - **plugins**:可以用来配置mybatis的插件，比如在开发中经常需要对查询结果进行分页，就需要用到pageHelper分页插件，这些插件就是通过这个标签进行配置的。在mybatis底层，运用了责任链模式+动态代理去实现插件的功能
    ```
    <plugin interceptor="com.github.pagehelper.PageInterceptor">
     <property name="helperDialect" value="mysql"/>
    </plugin>
    ```
## mapper.xml(resoures下):
- 简单示例：
```xml
<!-- StudentMapper.xml -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="test">
    <select id="findAll" resultType="com.yogurt.po.Student">
        SELECT * FROM student;
    </select>

    <insert id="insert" parameterType="com.yogurt.po.Student">
        INSERT INTO student (name,score,age,gender) VALUES (#{name},#{score},#{age},#{gender});
    </insert>
    
    <delete id="delete" parameterType="int">
        DELETE FROM student WHERE id = #{id};
    </delete>
</mapper>
```
- resultType和parameterType
  - resultType：指定查询结果的映射类型，即查询结果将被映射为指定的Java类型。
  - parameterType：指定输入参数的类型，即指定SQL语句中的参数类型。
- 动态 SQL 标签:
  - if:用于根据条件动态地包含SQL片段。
  - choose、when、otherwise:用于根据条件选择不同的SQL片段。
  ```xml
  <select id="selectUsersByCondition" resultType="com.example.User">
    SELECT * FROM users
    <where>
        <if test="name != null and name != ''">
            name = #{name}
        </if>
        <if test="age != null">
            AND age = #{age}
        </if>
    </where>
  </select>
  ``` 
  - where:与 `<if>` 标签配合使用，自动处理 WHERE 关键字以及多余的 AND 或 OR 运算符。
  - set:与 `<if>`标签配合使用，用于动态更新语句，自动处理 SET 关键字以及多余的逗号。
  - trim:可以实现更灵活的 SQL 语句拼接，自定义前缀、后缀以及需要去除的前缀和后缀内容。
  ```xml
  <select id="selectUsersByConditionWithTrim" resultType="com.example.User">
    SELECT * FROM users
    <trim prefix="WHERE" prefixOverrides="AND |OR ">
        <if test="name != null and name != ''">
            AND name = #{name}
        </if>
        <if test="age != null">
            AND age = #{age}
        </if>
    </trim>
  </select>
  ```
    prefix 指定拼接内容的前缀，prefixOverrides 指定需要去除的前缀内容。
  - foreach:用于遍历集合或数组，生成重复的SQL片段。  
  ```xml
  <delete id="deleteUsersByIds" parameterType="java.util.List">
    DELETE FROM users WHERE id IN
    <foreach item="id" index="index" collection="list"
             open="(" separator="," close=")">
        #{id}
    </foreach>
  </delete>
  collection 指定要遍历的集合，item 指定集合中每个元素的别名，open、separator 和 close 分别指定遍历结果的开始符号、分隔符和结束符号。   
  ```
  - choose、when、otherwise:类似于 Java 中的 switch - case 语句，用于多条件判断，只会选择一个满足条件的分支。
  ```xml
  <select id="selectUsersByConditionChoose" resultType="com.example.User">
    SELECT * FROM users
    <where>
        <choose>
            <when test="name != null and name != ''">
                name = #{name}
            </when>
            <when test="age != null">
                age = #{age}
            </when>
            <otherwise>
                1 = 1
            </otherwise>
        </choose>
    </where>
  </select>
  ```
  - concat:用于拼接多个字符串，生成一个完整的SQL语句。
  ```xml
  <select id="selectUsersByConditionConcat" resultType="com.example.User">
    SELECT * FROM users
    <where>
        <if test="name!= null and name!= ''">
            <concat>
                name = #{name}
            </concat>
        </if>
    <!-- 还可以不用标签，可能更常用 -->
    <select id="selectByUsername" resultType="userInfo">
        SELECT * FROM userinfo WHERE username LIKE CONCAT('%',#{username},'%')
    </select>
  ```
- ***当有多个参数时，需要使用Param等方式来指定参数名，否则会报错。***
  如：
  ```xml
  <select id="selectByOtherInfo" resultType="userInfo">
        SELECT * FROM userinfo WHERE 1=1
        <if test="sex!=null">
            AND sex LIKE CONCAT('%',#{sex},'%')
        </if>
        <if test="phone!=null">
            AND phone LIKE CONCAT('%',#{phone},'%')
        </if>
        <if test="email!=null">
            AND email LIKE CONCAT('%',#{email},'%')
        </if>
    </select>
  ```
  这时需要有三种方式解决：
  1. 使用@Param注解：
  ```java
  List<userInfo> selectByOtherInfo(@Param("sex") String sex, @Param("phone") String phone, @Param("email") String email);
  ```
  2. 使用ParmX:
  ```xml
  <select id="selectByOtherInfo" resultType="UserInfo">
    SELECT * FROM userinfo WHERE 1=1
    <if test="param1!=null">
        AND sex LIKE CONCAT('%',#{param1},'%')
    </if>
    <if test="param2!=null">
        AND phone LIKE CONCAT('%',#{param2},'%')
    </if>
    <if test="param3!=null">
        AND email LIKE CONCAT('%',#{param3},'%')
    </if>
  </select>
  ```
  3. 用数据结构封装参数，如map、list、javaBean等
  
  
  依次判断when标签的条件，选择第一个满足条件的分支执行，如果都不满足，则执行otherwise标签中的内容。
## 注解形式：
注解不需要写mapper.xml文件，只需要在mapper接口中写注解即可。如：
```java
@Select("SELECT * FROM student WHERE name like '%${name}%' AND major like '%${major}%'")
	List<Student> find(@Param("name") String name, @Param("major") String major);
``` 

## 最终使用：
- 以上东西都有了后，就可以在service层中使用了。
```java
public class MyBatisUtil {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}

public class Main {
    public static void main(String[] args) {
        // 获取 SqlSession
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        try {
            // 获取对应接口的代理对象，这里假设接口为 UserManage
            UserManage userManage = sqlSession.getMapper(UserManage.class);

            // 举例调用方法，这里假设方法名为 selectAll
            List<userInfo> allUsers = userManage.selectAll();
            // 提交事务,如果需要
            sqlSession.commit();
        } catch (Exception e) {
            // 回滚事务，如果需要
            sqlSession.rollback();
            e.printStackTrace();
        } finally {
            // 关闭 SqlSession
            sqlSession.close();
        }
    }
}
```