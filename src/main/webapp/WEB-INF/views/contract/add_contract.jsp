<%@ page import="model.Customer" %>
<%@ page import="model.Employee" %>
<%@ page import="model.Service" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- Cần cho format số nếu dùng lại giá trị --%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Tạo Hợp Đồng Mới - Furama Resort</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <%-- CSS/JS cho Datepicker --%>
  <style>
    .error-messages { list-style-type: none; padding-left: 0; color: red; font-size: 0.9em; }
    label .text-danger { font-size: 0.8em; }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-4 mb-5">
  <h2 class="mb-3">Tạo Hợp Đồng Mới</h2>
  <hr>

  <c:if test="${not empty errors}">
    <div class="alert alert-danger" role="alert">
      <strong>Vui lòng sửa các lỗi sau:</strong>
      <ul class="error-messages mt-2 mb-0">
        <c:forEach var="error" items="${errors}">
          <li><c:out value="${error}"/></li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/contract?action=create" method="POST" class="needs-validation" novalidate>
    <%-- Lấy giá trị cũ từ request attribute bằng scriptlet --%>
    <%
      String startDateValue = (String) request.getAttribute("startDateValue");
      String endDateValue = (String) request.getAttribute("endDateValue");
      String depositValue = (String) request.getAttribute("depositValue");
      String totalMoneyValue = (String) request.getAttribute("totalMoneyValue");
      String employeeIdValueStr = (String) request.getAttribute("employeeIdValue");
      String customerIdValueStr = (String) request.getAttribute("customerIdValue");
      String serviceIdValueStr = (String) request.getAttribute("serviceIdValue");

      int employeeIdValue = -1, customerIdValue = -1, serviceIdValue = -1;
      try { if (employeeIdValueStr != null) employeeIdValue = Integer.parseInt(employeeIdValueStr); } catch (NumberFormatException ex) {}
      try { if (customerIdValueStr != null) customerIdValue = Integer.parseInt(customerIdValueStr); } catch (NumberFormatException ex) {}
      try { if (serviceIdValueStr != null) serviceIdValue = Integer.parseInt(serviceIdValueStr); } catch (NumberFormatException ex) {}

      List<Customer> customerList = (List<Customer>) request.getAttribute("customerList");
      List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
      List<Service> serviceList = (List<Service>) request.getAttribute("serviceList"); // List đã lọc Villa/House
    %>

    <div class="row g-3">
      <%-- Ngày Bắt Đầu --%>
      <div class="col-md-6">
        <label for="contractStartDate" class="form-label">Ngày Giờ Bắt Đầu <span class="text-danger">*</span></label>
        <input type="datetime-local" class="form-control" id="contractStartDate" name="contractStartDate" value="<%= startDateValue != null ? startDateValue : "" %>" required>
        <%-- Lưu ý: datetime-local gửi format yyyy-MM-ddTHH:mm --%>
      </div>

      <%-- Ngày Kết Thúc --%>
      <div class="col-md-6">
        <label for="contractEndDate" class="form-label">Ngày Giờ Kết Thúc <span class="text-danger">*</span></label>
        <input type="datetime-local" class="form-control" id="contractEndDate" name="contractEndDate" value="<%= endDateValue != null ? endDateValue : "" %>" required>
      </div>

      <%-- Tiền Đặt Cọc --%>
      <div class="col-md-6">
        <label for="contractDeposit" class="form-label">Tiền Đặt Cọc ($)</label>
        <input type="number" step="any" min="0" class="form-control" id="contractDeposit" name="contractDeposit" value="<%= depositValue != null ? depositValue : "0" %>">
      </div>

      <%-- Tổng Tiền (Có thể để readonly hoặc tính sau) --%>
      <div class="col-md-6">
        <label for="contractTotalMoney" class="form-label">Tổng Tiền ($)</label>
        <input type="number" step="any" min="0" class="form-control" id="contractTotalMoney" name="contractTotalMoney" value="<%= totalMoneyValue != null ? totalMoneyValue : "0" %>" >
      </div>

      <%-- Chọn Nhân Viên --%>
      <div class="col-md-4">
        <label for="employeeId" class="form-label">Nhân Viên Lập HĐ <span class="text-danger">*</span></label>
        <select class="form-select" id="employeeId" name="employeeId" required>
          <option value="">-- Chọn nhân viên --</option>
          <% if (employeeList != null) {
            for (Employee emp : employeeList) {
              String selected = (emp.getEmployeeId() == employeeIdValue) ? "selected" : "";
          %>
          <option value="<%= emp.getEmployeeId() %>" <%= selected %>><%= emp.getEmployeeName() %> (ID: <%= emp.getEmployeeId() %>)</option>
          <%
              }
            }
          %>
        </select>
      </div>

      <%-- Chọn Khách Hàng --%>
      <div class="col-md-4">
        <label for="customerId" class="form-label">Khách Hàng <span class="text-danger">*</span></label>
        <select class="form-select" id="customerId" name="customerId" required>
          <option value="">-- Chọn khách hàng --</option>
          <% if (customerList != null) {
            for (Customer cus : customerList) {
              String selected = (cus.getCustomerId() == customerIdValue) ? "selected" : "";
          %>
          <option value="<%= cus.getCustomerId() %>" <%= selected %>><%= cus.getCustomerName() %> (ID: <%= cus.getCustomerId() %>)</option>
          <%
              }
            }
          %>
        </select>
      </div>

      <%-- Chọn Dịch Vụ (Villa/House) --%>
      <div class="col-md-4">
        <label for="serviceId" class="form-label">Dịch Vụ (Villa/House) <span class="text-danger">*</span></label>
        <select class="form-select" id="serviceId" name="serviceId" required>
          <option value="">-- Chọn dịch vụ --</option>
          <% if (serviceList != null) { // serviceList đã được lọc ở Servlet
            for (Service ser : serviceList) {
              String selected = (ser.getServiceId() == serviceIdValue) ? "selected" : "";
          %>
          <option value="<%= ser.getServiceId() %>" <%= selected %>><%= ser.getServiceName() %> (ID: <%= ser.getServiceId() %>)</option>
          <%
              }
            }
          %>
        </select>
      </div>
    </div>

    <hr class="my-4">

    <button class="btn btn-primary btn-lg" type="submit">Tạo Hợp Đồng</button>
    <a href="${pageContext.request.contextPath}/contract" class="btn btn-secondary btn-lg">Hủy</a>
  </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%-- Script cho Datepicker nếu cần (HTML5 datetime-local có sẵn giao diện) --%>
</body>
</html>
