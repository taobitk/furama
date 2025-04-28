// File: src/main/java/servlet/ServiceServlet.java
package servlet;

import common.ValidationUtil;
import model.RentType;
import model.Service;
import model.ServiceType;
import repository.RentTypeRepository;
import repository.ServiceRepository;
import repository.ServiceTypeRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ServiceServlet", urlPatterns = "/service")
public class ServiceServlet extends HttpServlet {

    private ServiceRepository serviceRepository = new ServiceRepository();
    private ServiceTypeRepository serviceTypeRepository = new ServiceTypeRepository();
    private RentTypeRepository rentTypeRepository = new RentTypeRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Mặc định là list
        }

        try {
            switch (action) {
                case "create":
                    showNewServiceForm(request, response);
                    break;
                case "list": // === XỬ LÝ ACTION LIST ===
                    listServices(request, response);
                    break;
                default:
                    listServices(request, response); // Mặc định về list nếu action lạ
                    break;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in ServiceServlet doGet: " + e.getMessage());
            throw new ServletException("Database error occurred.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            listServicesWithError(request, response, "Hành động không hợp lệ.");
            return;
        }

        try {
            switch (action) {
                case "create":
                    insertService(request, response);
                    break;
                // case "edit": updateService...
                // case "delete": deleteService...
                default:
                    listServicesWithError(request, response, "Hành động không hợp lệ.");
                    break;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in ServiceServlet doPost: " + e.getMessage());
            List<String> errors = new ArrayList<>();
            errors.add("Lỗi hệ thống CSDL, không thể thực hiện thao tác.");
            if ("create".equals(action)) {
                try {
                    forwardToFormWithError(request, response, errors, null);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                listServicesWithError(request, response, "Lỗi hệ thống CSDL.");
            }
        } catch (Exception e) {
            System.err.println("Unexpected Error in ServiceServlet doPost: " + e.getMessage());
            throw new ServletException("Lỗi hệ thống không mong muốn.", e);
        }
    }

    // --- PHƯƠNG THỨC HIỂN THỊ DANH SÁCH ---
    private void listServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Service> serviceList = serviceRepository.findAll();
        request.setAttribute("serviceList", serviceList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/service/list_service.jsp");
        dispatcher.forward(request, response);
    }
    // -------------------------------------

    private void listServicesWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        try {
            List<Service> serviceList = serviceRepository.findAll();
            request.setAttribute("serviceList", serviceList);
            request.setAttribute("errorMessage", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/service/list_service.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e){
            throw new ServletException("Không thể lấy danh sách dịch vụ", e);
        }
    }

    private void showNewServiceForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        List<RentType> rentTypeList = rentTypeRepository.findAll();
        request.setAttribute("serviceTypeList", serviceTypeList);
        request.setAttribute("rentTypeList", rentTypeList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/service/add_service.jsp");
        dispatcher.forward(request, response);
    }

    private void insertService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // ... (code insertService giữ nguyên) ...
        // Sửa dòng redirect cuối cùng để về trang list service
        response.sendRedirect(request.getContextPath() + "/service?action=list&message=add_success");
    }

    private void forwardToFormWithError(HttpServletRequest request, HttpServletResponse response, List<String> errors, Service service)
            throws ServletException, IOException, SQLException {
        // ... (code forwardToFormWithError giữ nguyên) ...
    }
}
