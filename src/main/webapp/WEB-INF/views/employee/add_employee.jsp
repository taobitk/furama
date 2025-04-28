<%@ page import="model.Position" %>
<%@ page import="java.util.List" %>
<%@ page import="model.EducationDegree" %>
<%@ page import="model.Division" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Thêm Nhân Viên Mới - Furama Resort</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <%-- CSS cho Datepicker --%>
  <style>
    .error-messages { list-style-type: none; padding-left: 0; color: red; font-size: 0.9em; }
    label .text-danger { font-size: 0.8em; }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-4 mb-5">
  <h2 class="mb-3">Thêm Nhân Viên Mới</h2>
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

  <form action="${pageContext.request.contextPath}/employee?action=create" method="POST" class="needs-validation" novalidate>
    <%-- Lấy giá trị cũ từ request attribute bằng scriptlet --%>
    <%
      String eName = (String) request.getAttribute("nameValue");
      String eBirthday = (String) request.getAttribute("birthdayValue");
      String eIdCard = (String) request.getAttribute("idCardValue");
      String eSalary = (String) request.getAttribute("salaryValue");
      String ePhone = (String) request.getAttribute("phoneValue");
      String eEmail = (String) request.getAttribute("emailValue");
      String eAddress = (String) request.getAttribute("addressValue");
      String ePositionIdStr = (String) request.getAttribute("positionIdValue");
      String eEducationDegreeIdStr = (String) request.getAttribute("educationDegreeIdValue");
      String eDivisionIdStr = (String) request.getAttribute("divisionIdValue");
      String eUsername = (String) request.getAttribute("usernameValue");
      String eGenderValue = (String) request.getAttribute("genderValue"); // Giữ lại "0" hoặc "1"
      int ePositionId = -1, eEducationDegreeId = -1, eDivisionId = -1;
      try { if (ePositionIdStr != null) ePositionId = Integer.parseInt(ePositionIdStr); } catch (NumberFormatException ex) {}
      try { if (eEducationDegreeIdStr != null) eEducationDegreeId = Integer.parseInt(eEducationDegreeIdStr); } catch (NumberFormatException ex) {}
      try { if (eDivisionIdStr != null) eDivisionId = Integer.parseInt(eDivisionIdStr); } catch (NumberFormatException ex) {}

      // Lấy danh sách cho dropdown
      List<Position> positionList = (List<Position>) request.getAttribute("positionList");
      List<EducationDegree> educationDegreeList = (List<EducationDegree>) request.getAttribute("educationDegreeList");
      List<Division> divisionList = (List<Division>) request.getAttribute("divisionList");
    %>

    <div class="row g-3">
      <div class="col-md-6">
        <label for="employeeName" class="form-label">Tên Nhân Viên <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="employeeName" name="employeeName" value="<%= eName != null ? eName : "" %>" required>
      </div>

      <div class="col-md-6">
        <label for="employeeBirthday" class="form-label">Ngày Sinh <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="employeeBirthday" name="employeeBirthday" placeholder="DD/MM/YYYY" value="<%= eBirthday != null ? eBirthday : "" %>" required>
        <%-- Tích hợp Datepicker JS vào đây --%>
      </div>

      <div class="col-md-6">
        <label for="employeeIdCard" class="form-label">Số CMND/CCCD <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="employeeIdCard" name="employeeIdCard" value="<%= eIdCard != null ? eIdCard : "" %>" required>
      </div>

      <div class="col-md-6">
        <label for="employeeSalary" class="form-label">Lương <span class="text-danger">*</span></label>
        <input type="number" step="any" min="0.01" class="form-control" id="employeeSalary" name="employeeSalary" value="<%= eSalary != null ? eSalary : "" %>" required>
      </div>

      <div class="col-md-6">
        <label for="employeePhone" class="form-label">Số Điện Thoại <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="employeePhone" name="employeePhone" value="<%= ePhone != null ? ePhone : "" %>" required>
      </div>

      <div class="col-md-6">
        <label for="employeeEmail" class="form-label">Email <span class="text-danger">*</span></label>
        <input type="email" class="form-control" id="employeeEmail" name="employeeEmail" value="<%= eEmail != null ? eEmail : "" %>" required>
      </div>

      <div class="col-md-12">
        <label for="employeeAddress" class="form-label">Địa Chỉ <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="employeeAddress" name="employeeAddress" value="<%= eAddress != null ? eAddress : "" %>" required>
      </div>

      <%-- Vị Trí (Dùng Scriptlet) --%>
      <div class="col-md-4">
        <label for="positionId" class="form-label">Vị Trí <span class="text-danger">*</span></label>
        <select class="form-select" id="positionId" name="positionId" required>
          <option value="">-- Chọn vị trí --</option>
          <% if (positionList != null) {
            for (Position pos : positionList) {
              String selected = (pos.getPositionId() == ePositionId) ? "selected" : "";
          %>
          <option value="<%= pos.getPositionId() %>" <%= selected %>><%= pos.getPositionName() %></option>
          <%
              }
            }
          %>
        </select>
      </div>

      <%-- Trình Độ (Dùng Scriptlet) --%>
      <div class="col-md-4">
        <label for="educationDegreeId" class="form-label">Trình Độ <span class="text-danger">*</span></label>
        <select class="form-select" id="educationDegreeId" name="educationDegreeId" required>
          <option value="">-- Chọn trình độ --</option>
          <% if (educationDegreeList != null) {
            for (EducationDegree degree : educationDegreeList) {
              String selected = (degree.getEducationDegreeId() == eEducationDegreeId) ? "selected" : "";
          %>
          <option value="<%= degree.getEducationDegreeId() %>" <%= selected %>><%= degree.getEducationDegreeName() %></option>
          <%
              }
            }
          %>
        </select>
      </div>

      <%-- Bộ Phận (Dùng Scriptlet) --%>
      <div class="col-md-4">
        <label for="divisionId" class="form-label">Bộ Phận <span class="text-danger">*</span></label>
        <select class="form-select" id="divisionId" name="divisionId" required>
          <option value="">-- Chọn bộ phận --</option>
          <% if (divisionList != null) {
            for (Division div : divisionList) {
              String selected = (div.getDivisionId() == eDivisionId) ? "selected" : "";
          %>
          <option value="<%= div.getDivisionId() %>" <%= selected %>><%= div.getDivisionName() %></option>
          <%
              }
            }
          %>
        </select>
      </div>

      <%-- Username --%>
      <div class="col-md-6">
        <label for="username" class="form-label">Username Đăng Nhập <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="username" name="username" value="<%= eUsername != null ? eUsername : "" %>" required>
      </div>

      <%-- === THÊM Ô MẬT KHẨU === --%>
      <div class="col-md-6">
        <label for="password" class="form-label">Mật Khẩu <span class="text-danger">*</span></label>
        <input type="password" class="form-control" id="password" name="password" required>
        <div id="passwordHelpBlock" class="form-text">
          Mật khẩu cho tài khoản đăng nhập của nhân viên mới.
        </div>
      </div>
      <%-- ======================== --%>

      <%-- Giới Tính (Dùng Scriptlet) --%>
      <div class="col-md-6">
        <label class="form-label d-block">Giới Tính <span class="text-danger">*</span></label>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="employeeGender" id="genderMale" value="1" <%= "1".equals(eGenderValue) ? "checked" : "" %> required>
          <label class="form-check-label" for="genderMale">Nam</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="employeeGender" id="genderFemale" value="0" <%= "0".equals(eGenderValue) ? "checked" : "" %> required>
          <label class="form-check-label" for="genderFemale">Nữ</label>
        </div>
      </div>
    </div>

    <hr class="my-4">

    <button class="btn btn-primary btn-lg" type="submit">Thêm Nhân Viên</button>
    <a href="${pageContext.request.contextPath}/employee" class="btn btn-secondary btn-lg">Hủy</a>
  </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%-- Script cho Datepicker --%>
</body>
</html>
