<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Dịch Vụ - Furama Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        .action-buttons a, .action-buttons button { margin-right: 5px; }
        .table-responsive { overflow-x: auto; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-4">
    <h2>Danh Sách Dịch Vụ</h2>
    <hr>

    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <c:out value="${message}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <c:out value="${errorMessage}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/service?action=create" class="btn btn-success mb-3">
        <i class="bi bi-plus-lg"></i> Thêm Dịch Vụ Mới
    </a>

    <%-- TODO: Form Tìm kiếm --%>

    <div class="table-responsive">
        <table class="table table-bordered table-striped table-hover">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Tên Dịch Vụ</th>
                <th>Diện tích (m²)</th>
                <th>Chi phí ($)</th>
                <th>Số người tối đa</th>
                <th>Kiểu thuê ID</th> <%-- Sẽ thay bằng tên --%>
                <th>Loại DV ID</th> <%-- Sẽ thay bằng tên --%>
                <th>Tiêu chuẩn</th>
                <%-- Có thể thêm các cột khác nếu cần (Hồ bơi, số tầng...) --%>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="service" items="${serviceList}">
                <tr>
                    <td>${service.serviceId}</td>
                    <td><c:out value="${service.serviceName}"/></td>
                    <td><c:out value="${service.serviceArea}"/></td>
                    <td><fmt:formatNumber value="${service.serviceCost}" type="currency" currencySymbol="$" /></td>
                    <td><c:out value="${service.serviceMaxPeople}"/></td>
                    <td>${service.rentTypeId}</td>
                    <td>${service.serviceTypeId}</td>
                    <td><c:out value="${service.standardRoom}"/></td>
                    <td class="action-buttons">
                        <a href="${pageContext.request.contextPath}/service?action=edit&id=${service.serviceId}" class="btn btn-warning btn-sm" title="Sửa">
                            <i class="bi bi-pencil-square"></i>
                        </a>
                        <button class="btn btn-danger btn-sm" title="Xóa"
                                onclick="confirmDeleteService('${service.serviceId}', '${service.serviceName}')">
                            <i class="bi bi-trash3-fill"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty serviceList}">
                <tr>
                    <td colspan="9" class="text-center">Không có dữ liệu dịch vụ.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div> <%-- End table-responsive --%>

    <%-- TODO: Phân trang --%>

</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function confirmDeleteService(id, name) {
        const escapedName = name.replace(/'/g, "\\'").replace(/"/g, '\\"');
        if (confirm("Bạn có chắc chắn muốn xóa dịch vụ '" + escapedName + "' (ID: " + id + ") không? \n(Lưu ý: Đây chỉ là xóa hiển thị trên trình duyệt theo yêu cầu 3)")) {
            let buttonElement = event.target.closest('button');
            if(buttonElement){
                let row = buttonElement.closest('tr');
                if (row) {
                    row.remove();
                    console.log("Đã xóa hiển thị dòng của dịch vụ ID: " + id);
                }
            }
        }
    }
</script>
</body>
</html>
