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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/selectOnePara")
public class selectOnePara extends HttpServlet {
    public void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        SqlSession ss = null;
        PrintWriter pw;
        try {
            pw = response.getWriter();
            ss = MybatisUtils.getSqlSession();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserManage userManage = ss.getMapper(UserManage.class);

        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String line;
        while((line=br.readLine())!=null) sb.append(line);
        String para = sb.toString();
        List<userInfo> data = userManage.selectOnePara(para);

        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(data);

        pw.write(json);
        pw.flush();
        pw.close();
        ss.close();
    }
}
