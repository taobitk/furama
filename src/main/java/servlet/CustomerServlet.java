package servlet;

import common.ValidationUtil;
import model.Customer;
import model.CustomerType;
import service.CustomerService;
import service.CustomerTypeService;
import service.ICustomerService;
import service.ICustomerTypeService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    private ICustomerService customerService = new CustomerService();
    private ICustomerTypeService customerTypeService = new CustomerTypeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "create":
                    showNewCustomerForm(request, response);
                    break;
                case "edit":
                    request.setAttribute("message", "Chức năng sửa chưa được triển khai");
                    listCustomers(request, response);
                    break;
                default:
                    listCustomers(request, response);
                    break;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong CustomerServlet doGet: " + e.getMessage());
            throw new ServletException("Lỗi truy cập cơ sở dữ liệu", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            try {
                listCustomers(request, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try {
            switch (action) {
                case "create":
                    insertCustomer(request, response);
                    break;
                case "edit":
                    request.setAttribute("message", "Chức năng sửa chưa được triển khai");
                    listCustomers(request, response);
                    break;
                case "delete":
                    request.setAttribute("message", "Chức năng xóa chưa được triển khai");
                    listCustomers(request, response);
                    break;
                default:
                    listCustomers(request, response);
                    break;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong CustomerServlet doPost: " + e.getMessage());
            throw new ServletException("Lỗi xử lý cơ sở dữ liệu", e);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Customer> customerList = customerService.findAll();
        request.setAttribute("customerList", customerList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewCustomerForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<CustomerType> customerTypeList = customerTypeService.findAll();
        request.setAttribute("customerTypeList", customerTypeList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp");
        dispatcher.forward(request, response);
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String name = request.getParameter("customerName");
        String birthdayStr = request.getParameter("customerBirthday");
        String genderStr = request.getParameter("customerGender");
        String idCard = request.getParameter("customerIdCard");
        String phone = request.getParameter("customerPhone");
        String email = request.getParameter("customerEmail");
        String address = request.getParameter("customerAddress");
        String customerTypeIdStr = request.getParameter("customerTypeId");

        List<String> errors = new ArrayList<>();
        LocalDate birthday = null;
        int customerTypeId = -1;
        boolean gender = false;

        if (name == null || name.trim().isEmpty()) { errors.add("Tên khách hàng không được để trống."); }
        if(customerTypeIdStr == null || customerTypeIdStr.isEmpty()){ errors.add("Vui lòng chọn loại khách hàng."); } else {
            try {
                customerTypeId = Integer.parseInt(customerTypeIdStr);
                if (customerTypeId <= 0) errors.add("Loại khách hàng không hợp lệ.");
            } catch (NumberFormatException e){ errors.add("Loại khách hàng không hợp lệ."); }
        }
        if (!ValidationUtil.isValidDate(birthdayStr)) { errors.add("Ngày sinh không hợp lệ hoặc sai định dạng DD/MM/YYYY."); } else {
            try { birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("dd/MM/uuuu")); } catch (DateTimeParseException e){ errors.add("Lỗi xử lý ngày sinh."); }
        }
        if ("1".equals(genderStr)) { gender = true; } else if ("0".equals(genderStr)) { gender = false; } else { errors.add("Vui lòng chọn giới tính."); }
        if (!ValidationUtil.isValidIdCard(idCard)) { errors.add("Số CMND phải có 9 hoặc 12 chữ số."); }
        if (!ValidationUtil.isValidPhoneNumber(phone)) { errors.add("Số điện thoại không đúng định dạng (Bắt đầu bằng 0, 10-11 số)."); }
        if (!ValidationUtil.isValidEmail(email)) { errors.add("Email không đúng định dạng."); }
        if (address == null || address.trim().isEmpty()) { errors.add("Địa chỉ không được để trống."); }

        boolean dataValid = errors.isEmpty();

        if (dataValid) {
            try {
                if (email != null && !email.trim().isEmpty() && ValidationUtil.isValidEmail(email)) {
                    if (customerService.isEmailTaken(email)) {
                        errors.add("Địa chỉ email này đã được sử dụng.");
                        dataValid = false;
                    }
                }
                if (phone != null && !phone.trim().isEmpty() && ValidationUtil.isValidPhoneNumber(phone)) {
                    if (customerService.isPhoneTaken(phone)) {
                        errors.add("Số điện thoại này đã được sử dụng.");
                        dataValid = false;
                    }
                }
                if (idCard != null && !idCard.trim().isEmpty() && ValidationUtil.isValidIdCard(idCard)) {
                    if (customerService.isIdCardTaken(idCard)) {
                        errors.add("Số CMND/CCCD này đã được sử dụng.");
                        dataValid = false;
                    }
                }
            } catch (SQLException e) {
                errors.add("Lỗi hệ thống khi kiểm tra dữ liệu. Vui lòng thử lại.");
                System.err.println("Lỗi SQL khi kiểm tra tồn tại trong Servlet: " + e.getMessage());
                dataValid = false;
            }
        }

        if (!dataValid) {
            request.setAttribute("errors", errors);
            request.setAttribute("customerTypeList", customerTypeService.findAll());
            request.setAttribute("nameValue", name);
            request.setAttribute("birthdayValue", birthdayStr);
            request.setAttribute("genderValue", genderStr);
            request.setAttribute("idCardValue", idCard);
            request.setAttribute("phoneValue", phone);
            request.setAttribute("emailValue", email);
            request.setAttribute("addressValue", address);
            request.setAttribute("typeIdValue", customerTypeIdStr);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp");
            dispatcher.forward(request, response);
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setCustomerTypeId(customerTypeId);
            newCustomer.setCustomerName(name);
            newCustomer.setCustomerBirthday(birthday);
            newCustomer.setCustomerGender(gender);
            newCustomer.setCustomerIdCard(idCard);
            newCustomer.setCustomerPhone(phone);
            newCustomer.setCustomerEmail(email);
            newCustomer.setCustomerAddress(address);

            try {
                customerService.addCustomer(newCustomer);
                response.sendRedirect(request.getContextPath() + "/customer?action=list&message=add_success");
            } catch (SQLException e) {
                System.err.println("Lỗi SQL không mong muốn khi INSERT khách hàng: " + e.getMessage());
                errors.add("Lỗi hệ thống khi lưu dữ liệu. Vui lòng thử lại.");
                request.setAttribute("errors", errors);
                request.setAttribute("customerTypeList", customerTypeService.findAll());
                request.setAttribute("nameValue", name);
                request.setAttribute("birthdayValue", birthdayStr);
                request.setAttribute("genderValue", genderStr);
                request.setAttribute("idCardValue", idCard);
                request.setAttribute("phoneValue", phone);
                request.setAttribute("emailValue", email);
                request.setAttribute("addressValue", address);
                request.setAttribute("typeIdValue", customerTypeIdStr);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}