package com.WhiteCloud.Controller;
import com.WhiteCloud.UserDao.UserManage;
import com.WhiteCloud.UserEntity.userInfo;
import com.WhiteCloud.Utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/addUser")
@MultipartConfig
public class addUser extends HttpServlet {
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        SqlSession ss = MybatisUtils.getSqlSession();

        Map<String, String> newUserInfoMap = new HashMap<>();

        for (Part part : request.getParts()) {
            String fieldName = part.getName();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8));){
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String fieldValue = sb.toString();
                newUserInfoMap.put(fieldName, fieldValue);
                System.out.println("参数名: " + fieldName + ", 参数值: " + fieldValue);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        userInfo newUserInfo = new userInfo();
        newUserInfo.setUsername(newUserInfoMap.get("addUsername"));
        newUserInfo.setSex(newUserInfoMap.get("addSex"));
        newUserInfo.setPhone(newUserInfoMap.get("addPhone"));
        newUserInfo.setEmail(newUserInfoMap.get("addEmail"));

        UserManage userManage = ss.getMapper(UserManage.class);
        try {
            userManage.addUser(newUserInfo);
            response.setStatus(HttpServletResponse.SC_OK);
            ss.commit();
            out.write("添加成功！");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("添加失败！");
            ss.rollback();
            e.printStackTrace();
        }
        out.close();
        ss.close();
    }
}
