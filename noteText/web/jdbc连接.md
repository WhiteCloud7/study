# 普通的JDBC连接
1. 建立连接
```java
// 数据库连接信息
private static final String URL = "jdbc:mysql://localhost:3306/your_database_name?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";

public static Connection establishConnection() {
    Connection connection = null;
    try {
        // 加载 MySQL 数据库的驱动类,如果是maven项目,则不需要这一步
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 建立与数据库的连接
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("数据库连接成功！");
    } catch (ClassNotFoundException e) {
        System.err.println("未找到数据库驱动类，请检查依赖。");
        e.printStackTrace();
    } catch (SQLException e) {
        System.err.println("建立数据库连接时出现错误。");
        e.printStackTrace();
    }
    return connection;
}
```
2. 执行sql语句
```java
public static void executeQuery(Connection connection, String sql) {
    String sql = "SELECT * FROM users";
        // 检查连接是否有效
        if (connection == null) {
            System.err.println("数据库连接未建立，无法执行查询。");
            return;
        }
        // 执行 SQL 查询,这里Statement需要try-with-resource来关闭
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            // 处理查询结果
            while (resultSet.next()) {
                // 这里可以根据表的列名和数据类型获取具体的数据
                // 例如，假设表中有一个名为 "column_name" 的列，数据类型为字符串
                // String columnValue = resultSet.getString("column_name");
                // 可以添加更多的处理逻辑，将获取到的数据进行处理或展示
            }
        } catch (SQLException e) {
            System.err.println("执行 SQL 查询时出现错误。");
            e.printStackTrace();
        }
    }
```
- **这里得到结果一般应该将其装配到一个列表中**,也可以是用set、map等其他接口,用这些接口来装配数据,方便后续如list能灵活变成arraylist等等。
- 装配到对应数据结构中后，上述代码的相应返回类型应该相应改变。
- ***但对于需要占位符即需要输入的sql语句，应该使用preparedStatement而不是statement***。  
具体使用方法如下：
```java
try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, inputUsername);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("登录成功");
                } else {
                    System.out.println("登录失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
```
    
1. 关闭连接
```java
public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("数据库连接已关闭。");
            } catch (SQLException e) {
                System.err.println("关闭数据库连接时出现错误。");
                e.printStackTrace();
            }
        }
    }
```

# HikariCP连接池
- 如下
```java
public class getConnection {
    private static final HikariDataSource dataSource;

    static {
        // 配置 HikariCP
        HikariConfig config = new HikariConfig();
        // 设置数据库连接 URL
        config.setJdbcUrl("jdbc:mysql://localhost:3306/usermanagesystem?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        // 设置数据库用户名
        config.setUsername("root");
        // 设置数据库密码
        config.setPassword("123456");
        // 设置连接池的最小空闲连接数
        config.setMinimumIdle(5);
        // 设置连接池的最大连接数
        config.setMaximumPoolSize(15);
        // 设置连接的最大空闲时间
        config.setIdleTimeout(30000);
        // 设置连接的最大生命周期
        config.setMaxLifetime(1800000);
        // 设置获取连接的最大等待时间
        config.setConnectionTimeout(30000);

        // 创建 HikariDataSource 实例
        dataSource = new HikariDataSource(config);
    }

    public static Connection myConnection() {
        try {
            // 从连接池中获取连接
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
```