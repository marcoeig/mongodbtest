<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>

<table>
	<thead>
		<tr>
			<th>Id</th>
			<th>Vorname</th>
			<th>Nachname</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="person" items="${persons}">
			<tr>
				<td><c:out value="${person.id}"/></td>
				<td><c:out value="${person.firstName}"/></td>
				<td><c:out value="${person.lastName}"/></td>
				<td>
					<form:form action="/persons/${person.id}" method="DELETE">
						<input type="submit" value="löschen" />
					</form:form>
					<a href="/persons/${person.id}">editieren</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<a href="/persons/add">anlegen</a>
</body>
</html>