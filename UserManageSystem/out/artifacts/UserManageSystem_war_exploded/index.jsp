<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>用户管理系统</title>
  <style>
    table {
      border-collapse: collapse;
      width: 100%;
    }
    th, td {
      border: 1px solid #ddd;
      padding: 8px;
      text-align: left;
    }
    th {
      background-color: #f2f2f2;
    }
  </style>
</head>
<body>
<h2>用户管理系统</h2>
<!-- 操作表单 -->
<form action="your_action_page.jsp" method="post">
  <!-- 下拉框选择操作类型 -->
  <select name="action">
    <option value="delete">删除</option>
    <option value="edit">编辑</option>
    <!-- 可以根据需求添加更多操作类型 -->
  </select>
  <input type="submit" value="执行操作">

  <!-- 用户信息表格 -->
  <table>
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
    <!-- 模拟用户数据，实际使用时从数据库获取 -->
    <tr>
      <td><input type="checkbox" name="userIds[]" value="1"></td>
      <td>张三</td>
      <td>男</td>
      <td>13800138000</td>
      <td>zhangsan@example.com</td>
    </tr>
    <tr>
      <td><input type="checkbox" name="userIds[]" value="2"></td>
      <td>李四</td>
      <td>女</td>
      <td>13900139000</td>
      <td>lisi@example.com</td>
    </tr>
    <!-- 可以根据实际情况添加更多用户行 -->
    </tbody>
  </table>
</form>

<script>
  // 全选/反选功能
  document.getElementById('selectAll').addEventListener('change', function() {
    var checkboxes = document.querySelectorAll('input[name="userIds[]"]');
    checkboxes.forEach(function(checkbox) {
      checkbox.checked = this.checked;
    }, this);
  });
</script>
</body>
</html>