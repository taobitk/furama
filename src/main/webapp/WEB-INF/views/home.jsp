<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard - Furama Resort</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <style>
    body { display: flex; flex-direction: column; min-height: 100vh; }
    main { flex: 1; }
    .sidebar { background-color: #f8f9fa; padding: 15px; min-height: calc(100vh - 56px - 60px); }
  </style>
</head>
<body>

<jsp:include page="common/header.jsp" />

<div class="container-fluid">
  <div class="row">
    <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse bg-light" id="sidebarMenu">
      <div class="position-sticky pt-3">
        <ul class="nav flex-column">
          <li class="nav-item">
            <a class="nav-link active" href="${pageContext.request.contextPath}/">
              <i class="bi bi-house-door me-2"></i>
              Bảng điều khiển
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              <i class="bi bi-file-earmark-text me-2"></i>
              Báo cáo
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              <i class="bi bi-gear me-2"></i>
              Cài đặt
            </a>
          </li>
        </ul>
      </div>
    </nav>

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 pt-3">
      <h1>Bảng điều khiển</h1>
      <hr>

      <div class="row">
        <div class="col-md-4">
          <div class="card text-white bg-primary mb-3">
            <div class="card-header">Khách đang thuê</div>
            <div class="card-body">
              <h5 class="card-title">
                ${numberOfActiveContracts != null ? numberOfActiveContracts : 'N/A'} Khách
              </h5>
              <p class="card-text">Số lượng hợp đồng đang hoạt động.</p>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card text-white bg-success mb-3">
            <div class="card-header">Dịch vụ sẵn có</div>
            <div class="card-body">
              <h5 class="card-title">${totalServices != null ? totalServices : 'N/A'}</h5>
              <p class="card-text">Tổng số Villa/House/Room.</p>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card text-dark bg-warning mb-3">
            <div class="card-header">Nhân viên</div>
            <div class="card-body">
              <h5 class="card-title">${totalEmployees != null ? totalEmployees : 'N/A'}</h5>
              <p class="card-text">Tổng số nhân viên đang làm việc.</p>
            </div>
          </div>
        </div>
      </div>

      <h2>Truy cập nhanh</h2>
      <a href="${pageContext.request.contextPath}/customer?action=create" class="btn btn-info me-2 mb-2">
        <i class="bi bi-person-plus-fill me-1"></i> Thêm Khách Hàng
      </a>
      <a href="${pageContext.request.contextPath}/contract?action=create" class="btn btn-secondary me-2 mb-2">
        <i class="bi bi-file-earmark-plus-fill me-1"></i> Tạo Hợp Đồng
      </a>
      <a href="${pageContext.request.contextPath}/employee?action=create" class="btn btn-warning me-2 mb-2">
        <i class="bi bi-person-badge me-1"></i> Thêm Nhân Viên
      </a>

    </main>
  </div>
</div>

<jsp:include page="common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="${pageContext.request.contextPath}/static/js/script.js"></script>
</body>
</html>