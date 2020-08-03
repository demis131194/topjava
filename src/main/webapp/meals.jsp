<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<br>
<table border="2">
    <thead>
    <tr>
        <th>Description</th>
        <th>Date</th>
        <th>Calories</th>
        <th>Exceeded</th>
    </tr>

    </thead>
    <c:forEach var="meal" items="${requestScope.meals}">
        <tbody style="${meal.excess ? "background-color: red" : "background-color: green"}">
        <tr>
            <th>${meal.description}</th>
            <th><javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm"/></th>
            <th>${meal.calories}</th>
            <th>${meal.excess}</th>
        </tr>
        </tbody>
    </c:forEach>
</table>
</body>
</html>
