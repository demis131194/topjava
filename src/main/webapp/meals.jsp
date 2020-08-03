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
        <th>Id</th>
        <th>Description</th>
        <th>Date</th>
        <th>Calories</th>
        <th>Exceeded</th>
        <th>Action</th>
    </tr>

    </thead>
    <c:forEach var="meal" items="${requestScope.meals}">
        <tbody style="${meal.excess ? "background-color: red" : "background-color: green"}">
        <tr>
            <th>${meal.id}</th>
            <th>${meal.description}</th>
            <th><javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm"/></th>
            <th>${meal.calories}</th>
            <th>${meal.excess}</th>
            <th><a href="${pageContext.request.contextPath}/meals?action=delete&mealId=${meal.id}">Delete</a></th>
        </tr>
        </tbody>
    </c:forEach>
</table>
<hr/>
<form action="${pageContext.request.contextPath}/meals" method="post">
    <div>
        <label for="mealId">Id(Nothing if create):</label> <br/>
        <input id="mealId" type="text" name="mealId" placeholder="Meal id">
    </div>
    <div>
        <label for="description">Description:</label> <br/>
        <input id="description" type="text" name="description">
    </div>
    <div>
        <label for="meal-date-time">Date:</label>
        <div id="meal-date-time">
            <input id="meal-date" type="date" name="mealDate">
            <input id="meal-time" type="time" name="mealTime">
        </div>
    </div>
    <div>
        <label for="calories">Calories:</label> <br/>
        <input id="calories" type="number" name="calories" min="0">
    </div>
    <button type="submit">Save</button>

</form>
</body>
</html>
