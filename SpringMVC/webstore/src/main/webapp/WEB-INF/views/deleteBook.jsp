<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Delete Book</title>
</head>
<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h3>Delete a book</h3>
				<p>${info}</p>
			</div>
		</div>
	</section>
	<section class="container-fluid">
		<div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4" style="padding-bottom: 15px">
				<form action="/webstore/books/deleteID">
				Delete ID:<br> <input type="text" name="ID"><br>
				<div class="col-sm-4">
					<button type="submit" class="btn btn-mini btn-danger">Delete</button>
				</div>
			</form>
			</div>
			<div class="col-sm-4">
				<a href="<c:url value="/j_spring_security_logout" />"
					class="btn  btn-mini btn-warning">Logout</a>
			</div>
		</div>
	</section> 
</body>
</html>