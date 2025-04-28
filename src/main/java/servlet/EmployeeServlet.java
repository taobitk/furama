// File: src/main/java/servlet/EmployeeServlet.java
package servlet;

import common.ValidationUtil;
import model.Division;
import model.EducationDegree;
import model.Employee;
import model.Position;
import model.User; // Import User model
import service.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/employee")
public class EmployeeServlet extends HttpServlet {

    // Khởi tạo các Service cần thiết
    private IEmployeeService employeeService = new EmployeeService();
    private IPositionService positionService = new PositionService();
    private IEducationDegreeService educationDegreeService = new EducationDegreeService();
    private IDivisionService divisionService = new DivisionService();
    private IUserService userService = new UserService(); // Đã có

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
                    showNewEmployeeForm(request, response);
                    break;
                case "edit":
                    request.setAttribute("message", "Chức năng sửa nhân viên chưa triển khai");
                    listEmployees(request, response);
                    break;
                default:
                    listEmployees(request, response);
                    break;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in EmployeeServlet doGet: " + e.getMessage());
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
            listEmployeesWithError(request, response, "Hành động không hợp lệ.");
            return;
        }

        try {
            switch (action) {
                case "create":
                    insertEmployee(request, response);
                    break;
                case "edit":
                    request.setAttribute("message", "Chức năng sửa nhân viên chưa triển khai");
                    listEmployees(request, response);
                    break;
                case "delete":
                    request.setAttribute("message", "Chức năng xóa nhân viên chưa triển khai");
                    listEmployees(request, response);
                    break;
                default:
                    listEmployees(request, response);
                    break;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in EmployeeServlet doPost: " + e.getMessage());
            // Khi có lỗi SQL ở tầng Service/Repo, trả về form với lỗi chung
            // Cần lấy lại dữ liệu cho dropdowns và dữ liệu người dùng đã nhập
            List<String> errors = new ArrayList<>();
            errors.add("Lỗi hệ thống CSDL, vui lòng thử lại.");
            forwardToFormWithError(request, response, errors, null); // Gửi null vì chưa chắc đã tạo được employee
        } catch (Exception e) { // Bắt các lỗi khác nếu có
            System.err.println("Unexpected Error in EmployeeServlet doPost: " + e.getMessage());
            throw new ServletException("Lỗi hệ thống không mong muốn.", e);
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Employee> employeeList = employeeService.findAll();
        request.setAttribute("employeeList", employeeList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/employee/list_employee.jsp");
        dispatcher.forward(request, response);
    }

    private void listEmployeesWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        try {
            List<Employee> employeeList = employeeService.findAll();
            request.setAttribute("employeeList", employeeList);
            request.setAttribute("errorMessage", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/employee/list_employee.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e){
            throw new ServletException("Không thể lấy danh sách nhân viên", e);
        }
    }


    private void showNewEmployeeForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Position> positionList = positionService.findAll();
        List<EducationDegree> educationDegreeList = educationDegreeService.findAll();
        List<Division> divisionList = divisionService.findAll();

        request.setAttribute("positionList", positionList);
        request.setAttribute("educationDegreeList", educationDegreeList);
        request.setAttribute("divisionList", divisionList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/employee/add_employee.jsp");
        dispatcher.forward(request, response);
    }

    private void insertEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        // 1. Lấy dữ liệu từ form
        String name = request.getParameter("employeeName");
        String birthdayStr = request.getParameter("employeeBirthday");
        String idCard = request.getParameter("employeeIdCard");
        String salaryStr = request.getParameter("employeeSalary");
        String phone = request.getParameter("employeePhone");
        String email = request.getParameter("employeeEmail");
        String address = request.getParameter("employeeAddress");
        String positionIdStr = request.getParameter("positionId");
        String educationDegreeIdStr = request.getParameter("educationDegreeId");
        String divisionIdStr = request.getParameter("divisionId");
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // Lấy mật khẩu mới
        // String genderStr = request.getParameter("employeeGender"); // Lấy giới tính nếu có

        // 2. Validate dữ liệu cơ bản
        List<String> errors = new ArrayList<>();
        LocalDate birthday = null;
        double salary = -1;
        int positionId = -1;
        int educationDegreeId = -1;
        int divisionId = -1;
        // boolean gender = false; // Xử lý gender nếu có

        if (name == null || name.trim().isEmpty()) errors.add("Tên không được để trống.");
        if (!ValidationUtil.isValidDate(birthdayStr)) errors.add("Ngày sinh không hợp lệ (DD/MM/YYYY)."); else {
            try { birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("dd/MM/uuuu")); } catch (DateTimeParseException e) { errors.add("Lỗi parse ngày sinh.");}
        }
        if (!ValidationUtil.isValidIdCard(idCard)) errors.add("CMND phải có 9 hoặc 12 số.");
        if (!ValidationUtil.isPositiveDouble(salaryStr)) errors.add("Lương phải là số dương."); else {
            try { salary = Double.parseDouble(salaryStr); } catch(NumberFormatException e) { errors.add("Lương không hợp lệ."); }
        }
        if (!ValidationUtil.isValidPhoneNumber(phone)) errors.add("Số điện thoại không đúng (0xxxxxxxxx, 10-11 số).");
        if (!ValidationUtil.isValidEmail(email)) errors.add("Email không đúng định dạng.");
        if (address == null || address.trim().isEmpty()) errors.add("Địa chỉ không được để trống.");
        try { positionId = Integer.parseInt(positionIdStr); if(positionId <= 0) errors.add("Vui lòng chọn vị trí."); } catch (NumberFormatException e) { errors.add("Vị trí không hợp lệ."); }
        try { educationDegreeId = Integer.parseInt(educationDegreeIdStr); if(educationDegreeId <= 0) errors.add("Vui lòng chọn trình độ."); } catch (NumberFormatException e) { errors.add("Trình độ không hợp lệ."); }
        try { divisionId = Integer.parseInt(divisionIdStr); if(divisionId <= 0) errors.add("Vui lòng chọn bộ phận."); } catch (NumberFormatException e) { errors.add("Bộ phận không hợp lệ."); }
        if (username == null || username.trim().isEmpty()) errors.add("Username không được để trống.");
        if (password == null || password.trim().isEmpty()) errors.add("Mật khẩu không được để trống."); // Thêm validate password
        // Thêm validate cho gender nếu có

        // 3. Kiểm tra trùng lặp (chỉ nếu validate cơ bản OK)
        boolean dataValid = errors.isEmpty();
        boolean userExistsInUserTable = false; // Biến cờ để biết user đã có trong bảng user chưa

        if (dataValid) {
            try {
                // --- KIỂM TRA USERNAME TRONG BẢNG 'user' TRƯỚC ---
                User existingUser = userService.findUserByUsername(username);
                if (existingUser != null) {
                    userExistsInUserTable = true; // Đánh dấu là user đã tồn tại
                    // Nếu user đã tồn tại, kiểm tra xem nó đã được gán cho employee khác chưa
                    if (employeeService.isUsernameTaken(username)) {
                        errors.add("Username này đã được sử dụng bởi nhân viên khác.");
                        dataValid = false;
                    }
                }
                // -------------------------------------------------

                // --- KIỂM TRA TRÙNG LẶP KHÁC TRONG BẢNG 'employee' ---
                if (dataValid) { // Chỉ kiểm tra tiếp nếu chưa có lỗi
                    if (phone != null && !phone.trim().isEmpty() && employeeService.isPhoneTaken(phone)) { errors.add("Số điện thoại đã tồn tại."); dataValid = false; }
                    if (email != null && !email.trim().isEmpty() && employeeService.isEmailTaken(email)) { errors.add("Email đã tồn tại."); dataValid = false; }
                    if (idCard != null && !idCard.trim().isEmpty() && employeeService.isIdCardTaken(idCard)) { errors.add("Số CMND đã tồn tại."); dataValid = false; }
                }
                // ----------------------------------------------------

            } catch (SQLException e) {
                errors.add("Lỗi hệ thống khi kiểm tra dữ liệu trùng lặp.");
                System.err.println("SQL Error during uniqueness check: " + e.getMessage());
                dataValid = false;
            }
        }

        // 4. Xử lý kết quả
        if (!dataValid) {
            // Có lỗi -> Forward về form kèm lỗi và dữ liệu đã nhập
            forwardToFormWithError(request, response, errors, null);
        } else {
            // Dữ liệu hợp lệ và không trùng -> Tiến hành tạo User (nếu cần) và Employee
            try {
                // --- TẠO USER MỚI NẾU CHƯA TỒN TẠI ---
                if (!userExistsInUserTable) {
                    // !!! QUAN TRỌNG: HASH PASSWORD TRƯỚC KHI LƯU !!!
                    // String hashedPassword = hashFunction(password); // Thay hashFunction bằng thư viện hash thực tế (vd: BCrypt)
                    // Tạm thời dùng plain text cho bài tập
                    User newUser = new User(username, password);
                    try {
                        userService.addUser(newUser);
                        System.out.println("Đã thêm user mới: " + username);
                    } catch (SQLException e) {
                        // Xử lý trường hợp không thêm được user (ví dụ: lỗi race condition username trùng)
                        System.err.println("Lỗi SQL khi thêm user mới: " + e.getMessage());
                        errors.add("Không thể tạo tài khoản người dùng. Username có thể đã tồn tại.");
                        forwardToFormWithError(request, response, errors, null);
                        return; // Dừng xử lý nếu không thêm được user
                    }
                }
                // ------------------------------------

                // --- TẠO VÀ LƯU EMPLOYEE ---
                Employee newEmployee = new Employee();
                newEmployee.setEmployeeName(name);
                newEmployee.setEmployeeBirthday(birthday);
                newEmployee.setEmployeeIdCard(idCard);
                newEmployee.setEmployeeSalary(salary);
                newEmployee.setEmployeePhone(phone);
                newEmployee.setEmployeeEmail(email);
                newEmployee.setEmployeeAddress(address);
                newEmployee.setPositionId(positionId);
                newEmployee.setEducationDegreeId(educationDegreeId);
                newEmployee.setDivisionId(divisionId);
                newEmployee.setUsername(username); // Username này giờ đã chắc chắn tồn tại trong bảng user
                // Set gender nếu có

                employeeService.addEmployee(newEmployee);
                // Redirect về trang danh sách sau khi thêm thành công
                response.sendRedirect(request.getContextPath() + "/employee?action=list&message=add_success");
                // ---------------------------

            } catch (SQLException e) {
                // Xử lý lỗi SQL không mong muốn khác nếu có khi INSERT Employee
                System.err.println("Lỗi SQL không mong muốn khi INSERT employee: " + e.getMessage());
                errors.add("Lỗi hệ thống khi lưu thông tin nhân viên.");
                // Tạo object tạm để gửi lại form (dù chưa có ID)
                Employee tempEmployee = new Employee();
                tempEmployee.setEmployeeName(name); // Set các giá trị đã lấy được
                // ... set các giá trị khác ...
                tempEmployee.setUsername(username);
                forwardToFormWithError(request, response, errors, tempEmployee);
            }
        }
    }

    private void forwardToFormWithError(HttpServletRequest request, HttpServletResponse response, List<String> errors, Employee employee)
            throws ServletException, IOException {
        request.setAttribute("errors", errors);
        try {
            request.setAttribute("positionList", positionService.findAll());
            request.setAttribute("educationDegreeList", educationDegreeService.findAll());
            request.setAttribute("divisionList", divisionService.findAll());
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy lại danh sách lookup: " + e.getMessage());
            // Có thể thêm lỗi vào errors hoặc xử lý khác
        }

        if(employee != null) {
            request.setAttribute("employeeValue", employee); // Gửi object nếu có lỗi sau khi tạo
            // Set các giá trị ...Value từ object employee để nhất quán
            request.setAttribute("nameValue", employee.getEmployeeName());
            request.setAttribute("birthdayValue", employee.getEmployeeBirthday() != null ? employee.getEmployeeBirthday().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
            request.setAttribute("idCardValue", employee.getEmployeeIdCard());
            request.setAttribute("salaryValue", String.valueOf(employee.getEmployeeSalary())); // Chuyển double về String
            request.setAttribute("phoneValue", employee.getEmployeePhone());
            request.setAttribute("emailValue", employee.getEmployeeEmail());
            request.setAttribute("addressValue", employee.getEmployeeAddress());
            request.setAttribute("positionIdValue", String.valueOf(employee.getPositionId()));
            request.setAttribute("educationDegreeIdValue", String.valueOf(employee.getEducationDegreeId()));
            request.setAttribute("divisionIdValue", String.valueOf(employee.getDivisionId()));
            request.setAttribute("usernameValue", employee.getUsername());
            // request.setAttribute("genderValue", employee.isEmployeeGender() ? "1" : "0"); // Nếu có gender
        } else {
            // Giữ lại giá trị từ request nếu lỗi xảy ra trước khi tạo object
            request.setAttribute("nameValue", request.getParameter("employeeName"));
            request.setAttribute("birthdayValue", request.getParameter("employeeBirthday"));
            request.setAttribute("idCardValue", request.getParameter("employeeIdCard"));
            request.setAttribute("salaryValue", request.getParameter("employeeSalary"));
            request.setAttribute("phoneValue", request.getParameter("employeePhone"));
            request.setAttribute("emailValue", request.getParameter("employeeEmail"));
            request.setAttribute("addressValue", request.getParameter("employeeAddress"));
            request.setAttribute("positionIdValue", request.getParameter("positionId"));
            request.setAttribute("educationDegreeIdValue", request.getParameter("educationDegreeId"));
            request.setAttribute("divisionIdValue", request.getParameter("divisionId"));
            request.setAttribute("usernameValue", request.getParameter("username"));
            request.setAttribute("genderValue", request.getParameter("employeeGender")); // Nếu có gender
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/employee/add_employee.jsp");
        dispatcher.forward(request, response);
    }
}
