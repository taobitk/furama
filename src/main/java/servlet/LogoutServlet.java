package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Hủy session và xóa tất cả các attribute trong đó
            System.out.println("Session đã được hủy."); // Log (tùy chọn)
        } else {
            System.out.println("Không có session để hủy."); // Log (tùy chọn)
        }
        Cookie userCookie = new Cookie("username", null);
        userCookie.setMaxAge(0); // Đặt thời gian sống = 0 -> trình duyệt sẽ xóa cookie
        response.addCookie(userCookie);
        Cookie passCookie = new Cookie("password", null);
        passCookie.setMaxAge(0);
        response.addCookie(passCookie);
        System.out.println("Cookie Remember Me đã được yêu cầu xóa."); // Log (tùy chọn)
        // 3. Chuyển hướng về trang Login
        System.out.println("Chuyển hướng về trang Login..."); // Log (tùy chọn)
        response.sendRedirect(request.getContextPath() + "/login");
    }
}