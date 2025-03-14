package com.WhiteCloud.Controller;

import com.WhiteCloud.UserDao.UserManage;
import com.WhiteCloud.Utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/deleteOneUser")
public class deleteOneUser extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter pw =  response.getWriter();

        SqlSession ss = MybatisUtils.getSqlSession();
        UserManage userManage = ss.getMapper(UserManage.class);

        String userIdStr;
        try(BufferedReader br = request.getReader();){
            userIdStr=br.readLine();
            userManage.deleteOneUser(Integer.parseInt(userIdStr));
            ss.commit();
            userManage.resetMaxAutoKey();
            ss.commit();
            pw.write("删除成功！");
        }catch (Exception e){
            ss.rollback();
            pw.write("删除失败！");
            e.printStackTrace();
            throw new RuntimeException();
        }
        ss.close();
        pw.close();
    }
}
