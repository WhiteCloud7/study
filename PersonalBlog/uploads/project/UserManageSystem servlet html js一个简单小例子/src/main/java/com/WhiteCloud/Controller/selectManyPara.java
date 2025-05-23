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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/selectManyPara")
public class selectManyPara extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        SqlSession ss = null;
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            ss = MybatisUtils.getSqlSession();
            UserManage userManage = ss.getMapper(UserManage.class);

            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String allPara = sb.toString();
            String[] ParaStringArray = allPara.split("\\s+");
            List<String> ParaList = new ArrayList<>();
            for (String para : ParaStringArray) {
                ParaList.add(para);
            }
            List<userInfo> data = userManage.selectByManyPara(ParaList);
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(data);

            pw.write(json);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if (pw != null) {
                pw.write("{\"error\": \"" + e.getMessage() + "\"}");
                pw.flush();
            }
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}