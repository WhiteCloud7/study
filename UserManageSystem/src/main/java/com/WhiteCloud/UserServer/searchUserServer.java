package com.WhiteCloud.UserServer;

import com.WhiteCloud.UserEneity.userInfo;
import com.WhiteCloud.UserServerImpl.searchUserServerImpl;
import com.WhiteCloud.Utils.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.WhiteCloud.Utils.getConnection.*;

public class searchUserServer implements searchUserServerImpl {
    Connection conn = myConnection1();

    @Override
    public List<userInfo> showUser() {
        List<userInfo> userinfo = new ArrayList<>(); // 每次调用方法时初始化新的列表
        String sql = "SELECT * FROM userinfo;";

        if (conn != null) {
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    userInfo user = new userInfo(); // 每次循环创建一个新的 userInfo 对象
                    String username = rs.getString("username");
                    String sex = rs.getString("sex");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");

                    user.setUsername(username);
                    user.setSex(sex);
                    user.setPhone(phone);
                    user.setEmail(email);

                    userinfo.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return userinfo;
    }

    @Override
    public List<userInfo> selectByUsername(String name) {
        List<userInfo> userinfo = new ArrayList<>(); // 每次调用方法时初始化新的列表
        String sql = "SELECT * FROM userinfo WHERE username LIKE ?;";
        if(conn!=null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + name + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    userInfo user = new userInfo();
                    String username = rs.getString("username");
                    String sex = rs.getString("sex");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");

                    user.setUsername(username);
                    user.setSex(sex);
                    user.setPhone(phone);
                    user.setEmail(email);

                    userinfo.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
                throw new RuntimeException();
            }
        return userinfo;
    }

    @Override
    public List<userInfo> selectByOtherInfo(String sex0, String phone0, String email0) {
        List<userInfo> userinfo = new ArrayList<>(); // 每次调用方法时初始化新的列表
        StringBuffer sqlBuffer = new StringBuffer("SELECT * FROM userinfo WHERE 1=1 ");
        //注意不能同时为空，即搜索条件必填
        if (sex0 != null)
            sqlBuffer.append(" AND sex LIKE ?");
        if (phone0 != null)
            sqlBuffer.append(" AND phone LIKE ?");
        if (email0 != null)
            sqlBuffer.append(" AND email LIKE ?");
        String sql = sqlBuffer.toString();
        if(conn != null){
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int parameterIndex = 1;
                if (sex0 != null)
                    ps.setString(parameterIndex++, "%" + sex0 + "%");
                if (phone0 != null)
                    ps.setString(parameterIndex++, "%" + phone0 + "%");
                if (email0 != null)
                    ps.setString(parameterIndex,"%" + email0 + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    userInfo user = new userInfo(); // 每次循环创建一个新的 userInfo 对象
                    String username = rs.getString("username");
                    String sex = rs.getString("sex");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");

                    user.setUsername(username);
                    user.setSex(sex);
                    user.setPhone(phone);
                    user.setEmail(email);

                    userinfo.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new RuntimeException();
        }
        return userinfo;
    }
}