<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Thêm Khách Hàng Mới - Furama Resort</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <%-- Nhớ thêm CSS cho Datepicker nếu dùng thư viện ngoài --%>
  <style>
    .error-messages { list-style-type: none; padding-left: 0; color: red; }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-4">
  <h2>Thêm Khách Hàng Mới</h2>
  <hr>

  <c:if test="${not empty errors}">
    <div class="alert alert-danger" role="alert">
      <strong>Vui lòng sửa các lỗi sau:</strong>
      <ul class="error-messages mt-2">
        <c:forEach var="error" items="${errors}">
          <li>${error}</li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/customer?action=create" method="POST">
    <div class="row g-3">
      <div class="col-md-6">
        <label for="customerName" class="form-label">Tên Khách Hàng <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="customerName" name="customerName" value="${nameValue}" required>
      </div>

      <div class="col-md-6">
        <label for="customerTypeId" class="form-label">Loại Khách Hàng <span class="text-danger">*</span></label>
        <select class="form-select" id="customerTypeId" name="customerTypeId" required>
          <option value="">-- Chọn loại khách --</option>
          <c:forEach var="type" items="${customerTypeList}">
            <option value="${type.customerTypeId}" ${type.customerTypeId == typeIdValue ? 'selected' : ''}>
                ${type.customerTypeName}
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="col-md-6">
        <label for="customerBirthday" class="form-label">Ngày Sinh <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="customerBirthday" name="customerBirthday" placeholder="DD/MM/YYYY" value="${birthdayValue}" required>
        <%-- Tích hợp Datepicker JS vào đây --%>
      </div>

      <div class="col-md-6">
        <label class="form-label">Giới Tính <span class="text-danger">*</span></label>
        <div>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="customerGender" id="genderMale" value="1" ${genderValue == '1' ? 'checked' : ''} required>
            <label class="form-check-label" for="genderMale">Nam</label>
          </div>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="customerGender" id="genderFemale" value="0" ${genderValue == '0' ? 'checked' : ''} required>
            <label class="form-check-label" for="genderFemale">Nữ</label>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <label for="customerIdCard" class="form-label">Số CMND/CCCD <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="customerIdCard" name="customerIdCard" value="${idCardValue}" required>
      </div>

      <div class="col-md-6">
        <label for="customerPhone" class="form-label">Số Điện Thoại <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="customerPhone" name="customerPhone" value="${phoneValue}" required>
      </div>

      <div class="col-md-6">
        <label for="customerEmail" class="form-label">Email <span class="text-danger">*</span></label>
        <input type="email" class="form-control" id="customerEmail" name="customerEmail" value="${emailValue}" required>
      </div>

      <div class="col-md-6">
        <label for="customerAddress" class="form-label">Địa Chỉ <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="customerAddress" name="customerAddress" value="${addressValue}" required>
      </div>
    </div>

    <hr class="my-4">

    <button class="btn btn-primary btn-lg" type="submit">Thêm Khách Hàng</button>
    <a href="${pageContext.request.contextPath}/customer" class="btn btn-secondary btn-lg">Hủy</a>
  </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%-- Thêm script cho Datepicker nếu dùng --%>
</body>
</html>