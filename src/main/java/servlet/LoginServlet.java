package servlet;

import model.User;
import service.IUserService;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private IUserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String username = "";
            String password = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }

            if (!username.isEmpty()) {
                request.setAttribute("cookieUsername", username);
                request.setAttribute("rememberMe", "checked"); // Đánh dấu checkbox
            }
        }


        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("rememberMe"); // Lấy giá trị checkbox "Remember Me"

        User user = null;
        try {
            user = userService.checkLogin(username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);
            if (remember != null && remember.equals("on")) {
                Cookie userCookie = new Cookie("username", user.getUsername());
                userCookie.setMaxAge(7 * 24 * 60 * 60); // Thời gian sống 7 ngày
                response.addCookie(userCookie);
                Cookie passCookie = new Cookie("password", user.getPassword());
                passCookie.setMaxAge(7 * 24 * 60 * 60); // Thời gian sống 7 ngày
                response.addCookie(passCookie);
            } else {
                Cookie userCookie = new Cookie("username", null);
                userCookie.setMaxAge(0); // Xóa cookie
                response.addCookie(userCookie);

                Cookie passCookie = new Cookie("password", null);
                passCookie.setMaxAge(0); // Xóa cookie
                response.addCookie(passCookie);
            }
            response.sendRedirect(request.getContextPath() + "/");

        } else {
            request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
            dispatcher.forward(request, response);
        }
    }
}