
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">
      <i class="bi bi-building me-2"></i> <%-- Sử dụng Bootstrap Icon --%>
      Furama Resort
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/employee">Employee</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/customer">Customer</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/service">Service</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/contract">Contract</a>
        </li>
      </ul>
      <form class="d-flex me-3" role="search">
        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-success" type="submit">Search</button>
      </form>
      <span class="navbar-text me-3">
                Xin chào,
                <c:choose>
                  <c:when test="${not empty sessionScope.loggedInUser}">
                    <c:out value="${sessionScope.loggedInUser.username}"/>
                  </c:when>
                  <c:otherwise>
                    Guest
                  </c:otherwise>
                </c:choose>
            </span>
      <form action="${pageContext.request.contextPath}/logout" method="post" class="d-flex">
        <button type="submit" class="btn btn-outline-light btn-sm">Logout</button>
      </form>
    </div>
  </div>
</nav>