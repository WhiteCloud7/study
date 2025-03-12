package com.WhiteCloud.Controller;
import com.WhiteCloud.UserDao.UserManage;
import com.WhiteCloud.UserEntity.userInfo;
import com.WhiteCloud.Utils.MybatisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/userInfo")
public class showAllUserInfo extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        SqlSession ss = MybatisUtils.getSqlSession();
        UserManage userManage = ss.getMapper(UserManage.class);
        List<userInfo> a = userManage.selectAll();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(a);
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
        out.close();

        ss.close();
    }
}