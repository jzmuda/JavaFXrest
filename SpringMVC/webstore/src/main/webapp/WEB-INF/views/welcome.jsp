<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
				<h1>${greeting}</h1>
				<p>${info}</p>
			</div>
		</div>
	</section>
	<section class="container-fluid">
		<div class="col-sm-4" style="padding-bottom: 15px">
			<div class="thumbnail">
				<div class="caption">
					<h3>Books</h3>
					<p>Display all books</p>
					<p>
						<a href="/webstore/books/all" class="btn btn-default"> <span
							class="glyphicon-info-sign glyphicon" /></span> Show all books
						</a>
					</p>
				</div>
			</div>
		</div>
		<div class="col-sm-4" style="padding-bottom: 15px">
		<div class="thumbnail">
				<div class="caption">
		<h3>Search Books</h3>
			<form action="/webstore/books/search">
				Author:<br> <input type="text" name="authorPrefix"><br>
				Title:<br> <input type="text" name="titlePrefix"><br>
				<div class="col-sm-4">
					<button type="submit" class="btn btn-mini btn-success">Search</button>
				</div>
			</form>
		</div>
		</div>
		</div>
		<div class="col-sm-4">
			<div class="thumbnail">
				<div class="caption">
					<h3>Add book</h3>
					<p>Create new book</p>
					<p>
						<a href="/webstore/books/add" class="btn btn-default"> <span
							class="glyphicon-info-sign glyphicon" /></span> Add book
						</a>
					</p>
				</div>
			</div>
		</div>
	</section>
</body>
</html>
