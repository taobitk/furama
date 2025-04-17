
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
import java.time.LocalDate;
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
        // Phân tích action từ parameter
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "create":
                showNewCustomerForm(request, response);
                break;
            case "edit":
                // showEditCustomerForm(request, response); // Sẽ làm sau
                break;
            default:
                listCustomers(request, response);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Phân tích action từ parameter
        String action = request.getParameter("action");
        if (action == null) {
            listCustomers(request, response);
            return;
        }

        switch (action) {
            case "create":
                insertCustomer(request, response);
                break;
            case "edit":
                // updateCustomer(request, response); // Sẽ làm sau
                break;
            case "delete":
                // deleteCustomer(request, response); // Sẽ làm sau
                break;
            default:
                listCustomers(request, response);
                break;
        }
    }



    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customerList = customerService.findAll();
        request.setAttribute("customerList", customerList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewCustomerForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CustomerType> customerTypeList = customerTypeService.findAll();
        request.setAttribute("customerTypeList", customerTypeList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/add.jsp");
        dispatcher.forward(request, response);
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("customerName");
        String birthdayStr = request.getParameter("customerBirthday");
        String genderStr = request.getParameter("customerGender");
        String idCard = request.getParameter("customerIdCard");
        String phone = request.getParameter("customerPhone");
        String email = request.getParameter("customerEmail");
        String address = request.getParameter("customerAddress");
        String customerTypeIdStr = request.getParameter("customerTypeId");

        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            errors.add("Tên khách hàng không được để trống.");
        }
        int customerTypeId = -1;
        if(customerTypeIdStr == null || customerTypeIdStr.isEmpty()){
            errors.add("Vui lòng chọn loại khách hàng.");
        } else {
            try {
                customerTypeId = Integer.parseInt(customerTypeIdStr);
                if (customerTypeId <= 0) errors.add("Loại khách hàng không hợp lệ.");
            } catch (NumberFormatException e){
                errors.add("Loại khách hàng không hợp lệ.");
            }
        }
        LocalDate birthday = null;
        if (!ValidationUtil.isValidDate(birthdayStr)) {
            errors.add("Ngày sinh không hợp lệ hoặc sai định dạng DD/MM/YYYY.");
        } else {
            try {
                birthday = LocalDate.parse(birthdayStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu"));
            } catch (DateTimeParseException e){
                errors.add("Lỗi xử lý ngày sinh.");
            }
        }
        boolean gender = false;
        if ("true".equalsIgnoreCase(genderStr) || "1".equals(genderStr)) {
            gender = true;
        } else if ("false".equalsIgnoreCase(genderStr) || "0".equals(genderStr)) {
            gender = false;
        } else if (genderStr != null && !genderStr.isEmpty()) {
            errors.add("Giới tính không hợp lệ.");
        }

        if (!ValidationUtil.isValidIdCard(idCard)) {
            errors.add("Số CMND phải có 9 hoặc 12 chữ số.");
        }
        if (!ValidationUtil.isValidPhoneNumber(phone)) {
            errors.add("Số điện thoại không đúng định dạng.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            errors.add("Email không đúng định dạng.");
        }
        if (address == null || address.trim().isEmpty()) {
            errors.add("Địa chỉ không được để trống.");
        }

        // --- 3. Xử lý kết quả validate ---
        if (!errors.isEmpty()) {
            // Nếu có lỗi -> Gửi lỗi về lại form add.jsp
            request.setAttribute("errors", errors); // Gửi danh sách lỗi
            request.setAttribute("customerTypeList", customerTypeService.findAll()); // Gửi lại list type

            // Gửi lại dữ liệu người dùng đã nhập để họ không phải nhập lại từ đầu
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
            // Nếu không có lỗi -> Tạo đối tượng Customer và lưu vào DB
            Customer newCustomer = new Customer();
            newCustomer.setCustomerTypeId(customerTypeId);
            newCustomer.setCustomerName(name);
            newCustomer.setCustomerBirthday(birthday);
            newCustomer.setCustomerGender(gender);
            newCustomer.setCustomerIdCard(idCard);
            newCustomer.setCustomerPhone(phone);
            newCustomer.setCustomerEmail(email);
            newCustomer.setCustomerAddress(address);

            customerService.addCustomer(newCustomer);

            response.sendRedirect(request.getContextPath() + "/customer?action=list&message=add_success"); // Thêm message để thông báo (tùy chọn)
        }
    }

}