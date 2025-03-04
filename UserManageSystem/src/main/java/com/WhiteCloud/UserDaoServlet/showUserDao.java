package com.WhiteCloud.UserDaoServlet;

import com.WhiteCloud.UserEneity.userInfo;
import com.WhiteCloud.UserServer.searchUserServer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import static com.WhiteCloud.Utils.getConnection.myConnection1;

@WebServlet("/show")
public class showUserDao extends HttpServlet {
    Connection conn ;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        conn = myConnection1();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        searchUserServer sUs = new searchUserServer();
        List<userInfo> userInfo = sUs.showUser();
        System.out.println(userInfo);
        request.setAttribute("userInfo",userInfo);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
