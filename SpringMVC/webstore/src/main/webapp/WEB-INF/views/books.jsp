<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Hello</title>
</head>

<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>Books</h1>
				<p>${info}</p>
			</div>
		</div>
	</section>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>Id</th>
				<th>Authors</th>
				<th>Title</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${bookList}" var="book">
				<tr>
					<th scope="row">${book.id}</th>
					<td><c:out value="${book.authors}" /></td>
					<td><c:out value="${book.title}" /></td>
					<td><button id="${book.id}" value="${book.status}"
							onclick="checkAvailability(this)">Check Availability</button> <script>
								function checkAvailability(element) {
									document.getElementById(element.id).innerHTML = element.value;
									if (element.value == "FREE")
										document.getElementById(element.id).style.background = '#00ff00';
									if (element.value == "LOAN")
										document.getElementById(element.id).style.background = '#ffff00';
									if (element.value == "MISSING")
										document.getElementById(element.id).style.background = '#ff0000';
								}
							</script></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="col-sm-4">
			<div class="thumbnail">
				<div class="caption">
					<h3>Delete book</h3>
					<p>
						<a href="/webstore/books/delete" class="btn btn-default"> <span
							class="glyphicon-info-sign glyphicon" /></span> Delete book
						</a>
					</p>
				</div>
			</div>
		</div>
</body>
</html>
