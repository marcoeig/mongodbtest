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

<form:form action="/persons/${person.id}" commandName="person" method="${not empty person.id ? 'POST' : 'PUT'}">

	<form:hidden path="id" />
	<form:input path="firstName" />
	<form:input path="lastName" />
	<br/>
	Straße:<form:input path="address.street" />
	Stadt:<form:input path="address.city" />
	Plz:<form:input path="address.zipcode" />
	Land:<form:input path="address.country" />
	
	<input type="submit" value="Speichern" />

</form:form>

</body>
</html>