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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/updateUser")
@MultipartConfig
public class updateUser extends HttpServlet {
    int userId;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter pw = response.getWriter();

        SqlSession ss = MybatisUtils.getSqlSession();
        UserManage userManage = ss.getMapper(UserManage.class);

        Map<String,String> updateInfo = new HashMap<>();

        for(Part part : request.getParts()){
            String fieldName = part.getName();
            StringBuilder sb = new StringBuilder();
            String line;
            try(BufferedReader br = new BufferedReader(new InputStreamReader(part.getInputStream()))){
                while((line=br.readLine())!=null){
                    sb.append(line);
                }
                if(fieldName.equals("alterUserId")){
                    userId = Integer.parseInt(sb.toString());
                }else {
                    String fieldValue = sb.toString();
                    updateInfo.put(fieldName,fieldValue);
                }
            }
        }
        userInfo userInfo = new userInfo();
        userInfo.setUserId(userId);
        userInfo.setUsername(updateInfo.get("alterUsername"));
        userInfo.setSex(updateInfo.get("alterSex"));
        userInfo.setPhone(updateInfo.get("alterPhone"));
        userInfo.setEmail(updateInfo.get("alterEmail"));
        try{
            userManage.updateUserInfo(userInfo);
            ss.commit();
            pw.write("更改成功！");
        }catch (Exception e){
            ss.rollback();
            pw.write("更改失败！");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
