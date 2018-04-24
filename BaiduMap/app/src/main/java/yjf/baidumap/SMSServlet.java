package yjf.baidumap;

/**
 * Created by Yin on 2016/10/26.
 */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SMSServlet")
public class SMSServlet extends HttpServlet {

    private String body;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String sender = request.getParameter("sender");
        body = request.getParameter("body");
        String time = request.getParameter("time");
        System.out.println("发送方："+sender);
        System.out.println("发送内容："+body);
        System.out.println("发送时间："+time);
    }
}