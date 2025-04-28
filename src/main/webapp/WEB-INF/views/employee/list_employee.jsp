<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Nhân Viên - Furama Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        .action-buttons a, .action-buttons button { margin-right: 5px; }
        /* Cho bảng hiển thị tốt hơn trên màn hình nhỏ */
        .table-responsive { overflow-x: auto; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-4">
    <h2>Danh Sách Nhân Viên</h2>
    <hr>

    <%-- Thông báo (ví dụ: thêm thành công) --%>
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/employee?action=create" class="btn btn-success mb-3">
        <i class="bi bi-person-plus-fill"></i> Thêm Nhân Viên Mới
    </a>

    <%-- TODO: Form Tìm kiếm --%>

    <div class="table-responsive">
        <table class="table table-bordered table-striped table-hover">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Họ Tên</th>
                <th>Ngày Sinh</th>
                <th>CMND</th>
                <th>Lương</th>
                <th>Điện Thoại</th>
                <th>Email</th>
                <th>Địa Chỉ</th>
                <th>Vị Trí ID</th> <%-- Sẽ thay bằng Tên Vị trí sau --%>
                <th>Trình Độ ID</th> <%-- Sẽ thay bằng Tên Trình độ sau --%>
                <th>Bộ Phận ID</th> <%-- Sẽ thay bằng Tên Bộ phận sau --%>
                <th>Username</th>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="emp" items="${employeeList}">
                <tr>
                    <td>${emp.employeeId}</td>
                    <td><c:out value="${emp.employeeName}"/></td>
                    <td>
                        <fmt:formatDate value="${emp.birthdaySqlDate}" pattern="dd/MM/yyyy" />
                    </td>
                    <td><c:out value="${emp.employeeIdCard}"/></td>
                    <td><fmt:formatNumber value="${emp.employeeSalary}" type="currency" currencySymbol="$" /></td> <%-- Định dạng tiền tệ ví dụ --%>
                    <td><c:out value="${emp.employeePhone}"/></td>
                    <td><c:out value="${emp.employeeEmail}"/></td>
                    <td><c:out value="${emp.employeeAddress}"/></td>
                    <td>${emp.positionId}</td>
                    <td>${emp.educationDegreeId}</td>
                    <td>${emp.divisionId}</td>
                    <td><c:out value="${emp.username}"/></td>
                    <td class="action-buttons">
                        <a href="${pageContext.request.contextPath}/employee?action=edit&id=${emp.employeeId}" class="btn btn-warning btn-sm" title="Sửa">
                            <i class="bi bi-pencil-square"></i>
                        </a>
                        <button class="btn btn-danger btn-sm" title="Xóa"
                                onclick="confirmDeleteEmployee('${emp.employeeId}', '${emp.employeeName}')">
                            <i class="bi bi-trash3-fill"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty employeeList}">
                <tr>
                    <td colspan="13" class="text-center">Không có dữ liệu nhân viên.</td>
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
    function confirmDeleteEmployee(id, name) {
        if (confirm("Bạn có chắc chắn muốn xóa nhân viên '" + name + "' (ID: " + id + ") không? \n(Lưu ý: Đây chỉ là xóa hiển thị trên trình duyệt theo yêu cầu 3)")) {
            let buttonElement = event.target.closest('button');
            if(buttonElement){
                let row = buttonElement.closest('tr');
                if (row) {
                    row.remove();
                    console.log("Đã xóa hiển thị dòng của nhân viên ID: " + id);
                }
            }
            // Trong thực tế cần gọi server để xóa thật:
            // Ví dụ: document.getElementById('deleteForm' + id).submit();
            // với một form ẩn cho mỗi dòng hoặc dùng AJAX
        }
    }
</script>
</body>
</html>