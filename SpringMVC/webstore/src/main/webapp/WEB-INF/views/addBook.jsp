<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Add Book</title>
</head>
<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h3>Add new book</h3>
			</div>
		</div>
	</section>
	<section class="container-fluid">
		<div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4" style="padding-bottom: 15px">
				<form:form class="form-horizontal" modelAttribute="newBook">
					<fieldset>
						<div class="form-group">
							<label class="control-label">Title</label><br>
							<form:input path="title" type="text" class="form:input-large" />
						</div>
						<div class="form-group">
							<label class="control-label">Author</label><br>
							<form:input path="authors" type="text" class="form:input-large" />
						</div>
						<div class="form-group">
							<label class="control-label">Status</label><br>
							<form:radiobutton path="status" value="FREE" />
							Free
							<form:radiobutton path="status" value="LOAN" />
							Loan
							<form:radiobutton path="status" value="MISSING" />
							Missing
						</div>
					</fieldset>
					<div class="col-sm-4">
						<button type="submit" class="btn btn-mini btn-success">Add</button>
					</div>
				</form:form>
			</div>
			<div class="col-sm-4">
				<a href="<c:url value="/j_spring_security_logout" />"
					class="btn  btn-mini btn-warning">Logout</a>
			</div>
		</div>
	</section>
</body>
</html>