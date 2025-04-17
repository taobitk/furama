<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Khách Hàng - Furama Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        .action-buttons a, .action-buttons button {
            margin-right: 5px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="container mt-4">
    <h2>Danh Sách Khách Hàng</h2>
    <hr>

    <a href="${pageContext.request.contextPath}/customer?action=create" class="btn btn-success mb-3">
        <i class="bi bi-plus-lg"></i> Thêm Khách Hàng Mới
    </a>

    <%-- TODO: Thêm Form Tìm kiếm ở đây --%>

    <table class="table table-bordered table-striped table-hover">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Họ Tên</th>
            <th>Ngày Sinh</th>
            <th>Giới Tính</th>
            <th>CMND</th>
            <th>Điện Thoại</th>
            <th>Email</th>
            <th>Địa Chỉ</th>
            <th>Loại Khách</th>
            <th>Hành Động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="customer" items="${customerList}">
            <tr>
                <td>${customer.customerId}</td>
                <td><c:out value="${customer.customerName}"/></td>
                <td>
                        <%-- === SỬA DÒNG NÀY === --%>
                    <fmt:formatDate value="${customer.birthdaySqlDate}" pattern="dd/MM/yyyy"/>
                        <%-- ================== --%>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${customer.customerGender}">Nam</c:when>
                        <c:otherwise>Nữ</c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${customer.customerIdCard}"/></td>
                <td><c:out value="${customer.customerPhone}"/></td>
                <td><c:out value="${customer.customerEmail}"/></td>
                <td><c:out value="${customer.customerAddress}"/></td>
                <td>${customer.customerTypeId}</td>
                    <%-- Tạm hiển thị ID --%>
                <td class="action-buttons">
                    <a href="${pageContext.request.contextPath}/customer?action=edit&id=${customer.customerId}"
                       class="btn btn-warning btn-sm" title="Sửa">
                        <i class="bi bi-pencil-square"></i>
                    </a>
                    <button class="btn btn-danger btn-sm" title="Xóa"
                            onclick="confirmDelete('${customer.customerId}', '${customer.customerName}')">
                        <i class="bi bi-trash3-fill"></i>
                    </button>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty customerList}">
            <tr>
                <td colspan="10" class="text-center">Không có dữ liệu khách hàng.</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <%-- TODO: Thêm điều hướng Phân trang ở đây --%>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function confirmDelete(id, name) {
        if (confirm("Bạn có chắc chắn muốn xóa khách hàng '" + name + "' (ID: " + id + ") không? \n(Lưu ý: Đây chỉ là xóa hiển thị trên trình duyệt theo yêu cầu 3)")) {
            let buttonElement = event.target.closest('button');
            if (buttonElement) {
                let row = buttonElement.closest('tr');
                if (row) {
                    row.remove();
                    console.log("Đã xóa hiển thị dòng của khách hàng ID: " + id);
                }
            }
        }
    }
</script>
</body>
</html>