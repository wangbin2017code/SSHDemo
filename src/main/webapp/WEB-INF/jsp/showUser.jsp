<%--
  Created by IntelliJ IDEA.
  User: Wangbin
  Date: 2017/09/23
  Time: 12:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>用户信息列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%--<script type="text/javascript" src="http://libs.baidu.com/jquery/1.9.1/jquery.js"></script>--%>
    <c:set value="${pageContext.request.contextPath}" var="path" scope="page"></c:set>
    <script type="text/javascript" src="${path}/static/js/extends/jquery-3.2.1.js"></script>
</head>
<body>
    <button onclick="output()">导出</button><br/>
    <c:if test="${!empty userList}">
        <c:forEach var="user" items="${userList}">
            姓名：${user.userName} &nbsp;&nbsp;手机号：${user.userPhone} &nbsp;&nbsp;邮箱：${user.userEmail} &nbsp;&nbsp;<br>
        </c:forEach>
    </c:if>
</body>
</html>
<script type="text/javascript">
    $(document).ready(function(){
        alert("欢迎您!");
    });
    function output(){
        $.ajax({
            type:"GET",
            url:"/user/poiExportExcel",
        });
    }
</script>

