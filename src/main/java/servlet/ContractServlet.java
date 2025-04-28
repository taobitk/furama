// File: src/main/java/servlet/ContractServlet.java
package servlet;

import common.ValidationUtil;
import model.Contract;
import model.Customer; // Cần để lấy ds khách hàng
import model.Employee; // Cần để lấy ds nhân viên
import model.Service;  // Cần để lấy ds dịch vụ
import repository.*; // Import các repository cần thiết

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Dùng để lọc service

@WebServlet(name = "ContractServlet", urlPatterns = "/contract")
public class ContractServlet extends HttpServlet {

    // Khởi tạo trực tiếp các Repository
    private ContractRepository contractRepository = new ContractRepository();
    private CustomerRepository customerRepository = new CustomerRepository(); // Cần để lấy ds KH
    private EmployeeRepositoryImpl employeeRepository = new EmployeeRepositoryImpl(); // Cần để lấy ds NV
    private ServiceRepository serviceRepository = new ServiceRepository(); // Cần để lấy ds DV

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
                    showNewContractForm(request, response);
                    break;
                case "list":
                    listContracts(request, response);
                    break;
                default:
                    listContracts(request, response); // Mặc định về list
                    break;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in ContractServlet doGet: " + e.getMessage());
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
            listContractsWithError(request, response, "Hành động không hợp lệ.");
            return;
        }

        try {
            switch (action) {
                case "create":
                    insertContract(request, response);
                    break;
                // case "edit": updateContract...
                // case "delete": deleteContract...
                default:
                    listContractsWithError(request, response, "Hành động không hợp lệ.");
                    break;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in ContractServlet doPost: " + e.getMessage());
            List<String> errors = new ArrayList<>();
            errors.add("Lỗi hệ thống CSDL, không thể thực hiện thao tác.");
            if ("create".equals(action)) {
                try {
                    forwardToFormWithError(request, response, errors, null);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                listContractsWithError(request, response, "Lỗi hệ thống CSDL.");
            }
        } catch (Exception e) {
            System.err.println("Unexpected Error in ContractServlet doPost: " + e.getMessage());
            throw new ServletException("Lỗi hệ thống không mong muốn.", e);
        }
    }

    // --- Các phương thức xử lý ---

    private void listContracts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Contract> contractList = contractRepository.findAll();
        request.setAttribute("contractList", contractList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/contract/list_contract.jsp");
        dispatcher.forward(request, response);
    }

    private void listContractsWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        try {
            List<Contract> contractList = contractRepository.findAll();
            request.setAttribute("contractList", contractList);
            request.setAttribute("errorMessage", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/contract/list_contract.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e){
            throw new ServletException("Không thể lấy danh sách hợp đồng", e);
        }
    }

    private void showNewContractForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Lấy danh sách Khách hàng, Nhân viên, Dịch vụ (chỉ Villa/House)
        List<Customer> customerList = customerRepository.findAll(); // Giả sử đã có findAll() trong CustomerRepository
        List<Employee> employeeList = employeeRepository.findAll(); // Giả sử đã có findAll() trong EmployeeRepository
        List<Service> allServiceList = serviceRepository.findAll(); // Lấy tất cả service

        // Lọc ra chỉ Villa (service_type_id = 3) và House (service_type_id = 2)
        List<Service> filteredServiceList = allServiceList.stream()
                .filter(s -> s.getServiceTypeId() == 2 || s.getServiceTypeId() == 3)
                .collect(Collectors.toList());

        request.setAttribute("customerList", customerList);
        request.setAttribute("employeeList", employeeList);
        request.setAttribute("serviceList", filteredServiceList); // Chỉ gửi list đã lọc

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/contract/add_contract.jsp");
        dispatcher.forward(request, response);
    }

    private void insertContract(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        // 1. Lấy dữ liệu từ form
        String startDateStr = request.getParameter("contractStartDate"); // Format dự kiến: YYYY-MM-DDTHH:mm
        String endDateStr = request.getParameter("contractEndDate");     // Format dự kiến: YYYY-MM-DDTHH:mm
        String depositStr = request.getParameter("contractDeposit");
        String totalMoneyStr = request.getParameter("contractTotalMoney"); // Có thể tính toán sau
        String employeeIdStr = request.getParameter("employeeId");
        String customerIdStr = request.getParameter("customerId");
        String serviceIdStr = request.getParameter("serviceId");

        // 2. Validate dữ liệu
        List<String> errors = new ArrayList<>();
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        double deposit = 0; // Mặc định là 0 nếu không nhập hoặc lỗi
        double totalMoney = 0; // Mặc định là 0
        int employeeId = -1;
        int customerId = -1;
        int serviceId = -1;

        // Validate và parse ngày giờ (HTML input datetime-local gửi dạng yyyy-MM-ddTHH:mm)
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Format chuẩn
        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDateTime.parse(startDateStr, formatter);
            } else {
                errors.add("Ngày bắt đầu không được để trống.");
            }
        } catch (DateTimeParseException e) {
            errors.add("Định dạng ngày bắt đầu không hợp lệ.");
        }
        try {
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDateTime.parse(endDateStr, formatter);
            } else {
                errors.add("Ngày kết thúc không được để trống.");
            }
        } catch (DateTimeParseException e) {
            errors.add("Định dạng ngày kết thúc không hợp lệ.");
        }

        // Validate ngày kết thúc >= ngày bắt đầu
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            errors.add("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
        }

        // Validate số dương
        if (depositStr != null && !depositStr.trim().isEmpty()) {
            if (!ValidationUtil.isPositiveDouble(depositStr)) errors.add("Tiền đặt cọc phải là số không âm."); else deposit = Double.parseDouble(depositStr);
        } // Cho phép cọc = 0
        if (totalMoneyStr != null && !totalMoneyStr.trim().isEmpty()) {
            if (!ValidationUtil.isPositiveDouble(totalMoneyStr)) errors.add("Tổng tiền phải là số không âm."); else totalMoney = Double.parseDouble(totalMoneyStr);
        } // Cho phép tổng tiền = 0 ban đầu

        // Validate các ID được chọn
        try { employeeId = Integer.parseInt(employeeIdStr); if(employeeId <= 0) errors.add("Vui lòng chọn nhân viên."); } catch (NumberFormatException | NullPointerException e) { errors.add("Nhân viên không hợp lệ."); }
        try { customerId = Integer.parseInt(customerIdStr); if(customerId <= 0) errors.add("Vui lòng chọn khách hàng."); } catch (NumberFormatException | NullPointerException e) { errors.add("Khách hàng không hợp lệ."); }
        try { serviceId = Integer.parseInt(serviceIdStr); if(serviceId <= 0) errors.add("Vui lòng chọn dịch vụ."); } catch (NumberFormatException | NullPointerException e) { errors.add("Dịch vụ không hợp lệ."); }

        // 3. Xử lý kết quả validate
        if (!errors.isEmpty()) {
            // Có lỗi -> Forward về form
            forwardToFormWithError(request, response, errors, null);
        } else {
            // Không lỗi -> Tạo Contract và lưu
            Contract newContract = new Contract();
            newContract.setContractStartDate(startDate);
            newContract.setContractEndDate(endDate);
            newContract.setContractDeposit(deposit);
            newContract.setContractTotalMoney(totalMoney); // Sẽ cần tính toán lại sau này
            newContract.setEmployeeId(employeeId);
            newContract.setCustomerId(customerId);
            newContract.setServiceId(serviceId);

            try {
                contractRepository.save(newContract);
                // Redirect về trang danh sách hợp đồng
                response.sendRedirect(request.getContextPath() + "/contract?action=list&message=add_success");
            } catch (SQLException e) {
                System.err.println("SQL Error during contract insert: " + e.getMessage());
                // Lỗi khóa ngoại có thể xảy ra nếu ID không tồn tại dù đã chọn
                errors.add("Lỗi hệ thống khi lưu hợp đồng. Vui lòng kiểm tra lại thông tin Khách hàng, Nhân viên, Dịch vụ.");
                forwardToFormWithError(request, response, errors, newContract); // Gửi lại object vừa tạo
            }
        }
    }

    /**
     * Phương thức trợ giúp để forward về form add_contract.jsp kèm lỗi và dữ liệu
     */
    private void forwardToFormWithError(HttpServletRequest request, HttpServletResponse response, List<String> errors, Contract contract)
            throws ServletException, IOException, SQLException {
        request.setAttribute("errors", errors);
        // Lấy lại danh sách cho dropdowns
        request.setAttribute("customerList", customerRepository.findAll());
        request.setAttribute("employeeList", employeeRepository.findAll());
        List<Service> allServiceList = serviceRepository.findAll();
        List<Service> filteredServiceList = allServiceList.stream()
                .filter(s -> s.getServiceTypeId() == 2 || s.getServiceTypeId() == 3)
                .collect(Collectors.toList());
        request.setAttribute("serviceList", filteredServiceList);


        // Đặt lại dữ liệu người dùng đã nhập vào request attribute
        if (contract != null) { // Lỗi xảy ra sau khi tạo object
            request.setAttribute("contractValue", contract); // Gửi cả object
        } else { // Lỗi xảy ra trước khi tạo object
            request.setAttribute("startDateValue", request.getParameter("contractStartDate"));
            request.setAttribute("endDateValue", request.getParameter("contractEndDate"));
            request.setAttribute("depositValue", request.getParameter("contractDeposit"));
            request.setAttribute("totalMoneyValue", request.getParameter("contractTotalMoney"));
            request.setAttribute("employeeIdValue", request.getParameter("employeeId"));
            request.setAttribute("customerIdValue", request.getParameter("customerId"));
            request.setAttribute("serviceIdValue", request.getParameter("serviceId"));
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/contract/add_contract.jsp");
        dispatcher.forward(request, response);
    }
}
