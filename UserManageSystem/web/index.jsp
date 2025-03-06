<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.WhiteCloud.UserServer.searchUserServer" %>
<%@ page import="com.WhiteCloud.UserEntity.userInfo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>用户管理系统</title>
  <!-- 引入Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  <style>
    body {
      padding: 20px;
    }
    table {
      width: 100%;
    }
    th, td {
      text-align: left;
    }
  </style>
</head>
<body>
<%
  // 创建searchUserServer实例
  searchUserServer sUs = new searchUserServer();
  // 调用showUser方法获取用户信息列表
  List<userInfo> userInfoList = sUs.showUser();
  // 将用户信息列表存储在请求属性中
  request.setAttribute("userInfo", userInfoList);
%>
<h2 class="mb-4">用户管理系统</h2>
<!-- 操作表单 -->
<form action="handleAction" method="post">
  <!-- 下拉框选择操作类型 -->
  <div class="mb-3">
    <select name="action" id="actionSelect" class="form-select" onchange="toggleSearchInput(this)">
      <option value="search">查找</option>
      <option value="delete">删除</option>
      <option value="edit">修改</option>
      <option value="add">添加</option>
      <!-- 可以根据需求添加更多操作类型 -->
    </select>
    <input type="text" id="searchInput" name="searchInput" disabled class="form-control mt-2">
    <input type="submit" value="执行操作" class="btn btn-primary mt-2">
  </div>

  <!-- 用户信息表格 -->
  <table class="table table-striped table-bordered">
    <thead>
    <tr>
      <th><input type="checkbox" id="selectAll"></th>
      <th>用户名</th>
      <th>性别</th>
      <th>电话</th>
      <th>邮箱</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${userInfo}" var="user">
      <tr>
        <td><input type="checkbox" name="userIds[]" value="${user.userId}"></td>
        <td>${user.username}</td>
        <td>${user.sex}</td>
        <td>${user.phone}</td>
        <td>${user.email}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</form>

<!-- 修改模态框 -->
<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="editModalLabel">修改用户信息</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form>
          <div class="mb-3">
            <label for="editUsername" class="form-label">用户名</label>
            <input type="text" class="form-control" id="editUsername">
          </div>
          <div class="mb-3">
            <label for="editSex" class="form-label">性别</label>
            <input type="text" class="form-control" id="editSex">
          </div>
          <div class="mb-3">
            <label for="editPhone" class="form-label">电话</label>
            <input type="text" class="form-control" id="editPhone">
          </div>
          <div class="mb-3">
            <label for="editEmail" class="form-label">邮箱</label>
            <input type="text" class="form-control" id="editEmail">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary">保存修改</button>
      </div>
    </div>
  </div>
</div>

<!-- 添加模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="addModalLabel">添加用户信息</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form>
          <div class="mb-3">
            <label for="addUsername" class="form-label">用户名</label>
            <input type="text" class="form-control" id="addUsername">
          </div>
          <div class="mb-3">
            <label for="addSex" class="form-label">性别</label>
            <input type="text" class="form-control" id="addSex">
          </div>
          <div class="mb-3">
            <label for="addPhone" class="form-label">电话</label>
            <input type="text" class="form-control" id="addPhone">
          </div>
          <div class="mb-3">
            <label for="addEmail" class="form-label">邮箱</label>
            <input type="text" class="form-control" id="addEmail">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary">添加用户</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
<script>
  function toggleSearchInput(select) {
    const searchInput = document.getElementById('searchInput');
    if (select.value ==='search') {
      searchInput.disabled = false;
    } else {
      searchInput.disabled = true;
    }
  }

  document.getElementById('actionSelect').addEventListener('change', function() {
    var action = this.value;
    if (action === 'edit') {
      const editModal = new bootstrap.Modal(document.getElementById('editModal'));
      editModal.show();
    } else if (action === 'add') {
      const addModal = new bootstrap.Modal(document.getElementById('addModal'));
      addModal.show();
    }
  });

  // 全选/反选功能
  document.getElementById('selectAll').addEventListener('change', function() {
    const checkboxes = document.querySelectorAll('input[name="userIds[]"]');
    checkboxes.forEach(function(checkbox) {
      checkbox.checked = this.checked;
    }, this);
  });
</script>
</body>
</html>