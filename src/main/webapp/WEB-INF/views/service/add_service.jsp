<%@ page import="model.ServiceType" %>
<%@ page import="model.RentType" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Dịch Vụ Mới - Furama Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        .error-messages { list-style-type: none; padding-left: 0; color: red; font-size: 0.9em; }
        label .text-danger { font-size: 0.8em; }
        /* Style để ẩn/hiện các trường đặc thù */
        .specific-fields { display: none; } /* Mặc định ẩn */
        /* Hiện ra khi có class active */
        .specific-fields.active { display: block; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-4 mb-5">
    <h2 class="mb-3">Thêm Dịch Vụ Mới</h2>
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

    <form action="${pageContext.request.contextPath}/service?action=create" method="POST" class="needs-validation" novalidate>
        <%-- Lấy giá trị cũ từ request attribute bằng scriptlet --%>
        <%
            String sName = (String) request.getAttribute("serviceNameValue");
            String sArea = (String) request.getAttribute("serviceAreaValue");
            String sCost = (String) request.getAttribute("serviceCostValue");
            String sMaxPeople = (String) request.getAttribute("serviceMaxPeopleValue");
            String sRentTypeIdStr = (String) request.getAttribute("rentTypeIdValue");
            String sServiceTypeIdStr = (String) request.getAttribute("serviceTypeIdValue");
            String sStandardRoom = (String) request.getAttribute("standardRoomValue");
            String sDescription = (String) request.getAttribute("descriptionValue");
            String sPoolArea = (String) request.getAttribute("poolAreaValue");
            String sNumberOfFloors = (String) request.getAttribute("numberOfFloorsValue");
            String sFreeService = (String) request.getAttribute("freeServiceValue");

            int sRentTypeId = -1, sServiceTypeId = -1;
            try { if (sRentTypeIdStr != null) sRentTypeId = Integer.parseInt(sRentTypeIdStr); } catch (NumberFormatException ex) {}
            try { if (sServiceTypeIdStr != null) sServiceTypeId = Integer.parseInt(sServiceTypeIdStr); } catch (NumberFormatException ex) {}

            List<ServiceType> serviceTypeList = (List<ServiceType>) request.getAttribute("serviceTypeList");
            List<RentType> rentTypeList = (List<RentType>) request.getAttribute("rentTypeList");
        %>

        <div class="row g-3">
            <%-- Tên Dịch Vụ --%>
            <div class="col-md-6">
                <label for="serviceName" class="form-label">Tên Dịch Vụ <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="serviceName" name="serviceName" value="<%= sName != null ? sName : "" %>" required>
            </div>

            <%-- Loại Dịch Vụ (Villa, House, Room) --%>
            <div class="col-md-6">
                <label for="serviceTypeId" class="form-label">Loại Dịch Vụ <span class="text-danger">*</span></label>
                <select class="form-select" id="serviceTypeId" name="serviceTypeId" required onchange="toggleSpecificFields()"> <%-- onchange gọi hàm JS --%>
                    <option value="">-- Chọn loại dịch vụ --</option>
                    <% if (serviceTypeList != null) {
                        for (ServiceType type : serviceTypeList) {
                            String selected = (type.getServiceTypeId() == sServiceTypeId) ? "selected" : "";
                    %>
                    <option value="<%= type.getServiceTypeId() %>" <%= selected %> data-type-name="<%= type.getServiceTypeName().toLowerCase() %>"><%= type.getServiceTypeName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <%-- Diện tích sử dụng --%>
            <div class="col-md-4">
                <label for="serviceArea" class="form-label">Diện tích sử dụng (m²) <span class="text-danger">*</span></label>
                <input type="number" min="1" class="form-control" id="serviceArea" name="serviceArea" value="<%= sArea != null ? sArea : "" %>" required>
            </div>

            <%-- Chi phí thuê --%>
            <div class="col-md-4">
                <label for="serviceCost" class="form-label">Chi phí thuê ($) <span class="text-danger">*</span></label>
                <input type="number" step="any" min="0.01" class="form-control" id="serviceCost" name="serviceCost" value="<%= sCost != null ? sCost : "" %>" required>
            </div>

            <%-- Số người tối đa --%>
            <div class="col-md-4">
                <label for="serviceMaxPeople" class="form-label">Số người tối đa <span class="text-danger">*</span></label>
                <input type="number" min="1" class="form-control" id="serviceMaxPeople" name="serviceMaxPeople" value="<%= sMaxPeople != null ? sMaxPeople : "" %>" required>
            </div>

            <%-- Kiểu thuê --%>
            <div class="col-md-6">
                <label for="rentTypeId" class="form-label">Kiểu thuê <span class="text-danger">*</span></label>
                <select class="form-select" id="rentTypeId" name="rentTypeId" required>
                    <option value="">-- Chọn kiểu thuê --</option>
                    <% if (rentTypeList != null) {
                        for (RentType type : rentTypeList) {
                            String selected = (type.getRentTypeId() == sRentTypeId) ? "selected" : "";
                    %>
                    <option value="<%= type.getRentTypeId() %>" <%= selected %>><%= type.getRentTypeName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <%-- === CÁC TRƯỜNG ĐẶC THÙ (ẨN/HIỆN BẰNG JS) === --%>

            <%-- Trường cho Villa và House --%>
            <div class="col-md-6 specific-fields villa-fields house-fields">
                <label for="standardRoom" class="form-label">Tiêu chuẩn phòng</label>
                <input type="text" class="form-control" id="standardRoom" name="standardRoom" value="<%= sStandardRoom != null ? sStandardRoom : "" %>">
            </div>
            <div class="col-md-12 specific-fields villa-fields house-fields">
                <label for="descriptionOtherConvenience" class="form-label">Mô tả tiện nghi khác</label>
                <textarea class="form-control" id="descriptionOtherConvenience" name="descriptionOtherConvenience" rows="2"><%= sDescription != null ? sDescription : "" %></textarea>
            </div>

            <%-- Trường riêng cho Villa --%>
            <div class="col-md-6 specific-fields villa-fields">
                <label for="poolArea" class="form-label">Diện tích hồ bơi (m²)</label>
                <input type="number" step="any" min="0.01" class="form-control" id="poolArea" name="poolArea" value="<%= sPoolArea != null ? sPoolArea : "" %>">
            </div>

            <%-- Trường cho Villa và House --%>
            <div class="col-md-6 specific-fields villa-fields house-fields">
                <label for="numberOfFloors" class="form-label">Số tầng</label>
                <input type="number" min="1" class="form-control" id="numberOfFloors" name="numberOfFloors" value="<%= sNumberOfFloors != null ? sNumberOfFloors : "" %>">
            </div>

            <%-- Trường riêng cho Room --%>
            <div class="col-md-12 specific-fields room-fields">
                <label for="freeServiceIncluded" class="form-label">Dịch vụ miễn phí đi kèm</label>
                <textarea class="form-control" id="freeServiceIncluded" name="freeServiceIncluded" rows="2"><%= sFreeService != null ? sFreeService : "" %></textarea>
            </div>
            <%-- ============================================== --%>

        </div>

        <hr class="my-4">

        <button class="btn btn-primary btn-lg" type="submit">Thêm Dịch Vụ</button>
        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary btn-lg">Hủy</a> <%-- Tạm về home --%>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%-- Script để ẩn/hiện các trường đặc thù (ĐÃ SỬA) --%>
<script>
    function toggleSpecificFields() {
        const serviceTypeSelect = document.getElementById('serviceTypeId');
        // Kiểm tra xem select element có tồn tại không
        if (!serviceTypeSelect) return;

        const selectedOption = serviceTypeSelect.options[serviceTypeSelect.selectedIndex];
        // Kiểm tra xem có option nào được chọn không và có data attribute không
        if (!selectedOption || !selectedOption.hasAttribute('data-type-name')) return;

        const selectedTypeName = selectedOption.getAttribute('data-type-name');

        // Lấy tất cả các nhóm trường đặc thù
        const villaFields = document.querySelectorAll('.villa-fields');
        const houseFields = document.querySelectorAll('.house-fields');
        const roomFields = document.querySelectorAll('.room-fields');
        const allSpecificFields = document.querySelectorAll('.specific-fields');

        // Ẩn tất cả trước bằng cách xóa class 'active'
        allSpecificFields.forEach(field => field.classList.remove('active'));

        // Hiện các trường tương ứng bằng cách thêm class 'active'
        if (selectedTypeName === 'villa') {
            villaFields.forEach(field => field.classList.add('active'));
            // Villa cũng có trường của House, thêm active cho chúng nếu chưa có
            houseFields.forEach(field => {
                if (!field.classList.contains('villa-fields')) {
                    field.classList.add('active');
                }
            });
        } else if (selectedTypeName === 'house') {
            houseFields.forEach(field => field.classList.add('active'));
        } else if (selectedTypeName === 'room') {
            roomFields.forEach(field => field.classList.add('active'));
        }
    }

    // Gọi hàm lần đầu khi trang tải xong
    document.addEventListener('DOMContentLoaded', toggleSpecificFields);
</script>
</body>
</html>
