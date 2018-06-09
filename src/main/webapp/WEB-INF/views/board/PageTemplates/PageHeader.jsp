<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Board</title>
<link rel="stylesheet" href="/resources/Compiled_Styles/Base.css" />
<link rel="stylesheet"
	href="/resources/Compiled_Styles/PageTemplate.css" />
<link rel="stylesheet" href="/resources/Compiled_Styles/PageHeader.css" />
<link rel="stylesheet" href="/resources/Compiled_Styles/PageFooter.css" />
<link rel="stylesheet" href="/resources/Compiled_Styles/LoginForm.css" />
<link rel="stylesheet" href="/resources/Compiled_Styles/BoardForm.css" />
<link rel="stylesheet" href="/resources/Compiled_Styles/EditorForm.css" />
<link rel="stylesheet" href="/resources/Compiled_Styles/Pagination.css" />
<link rel="stylesheet" href="/resources/Compiled_Styles/BoardDetailForm.css" />
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
</head>

<body>
	<div class="PageTemplate">
		<div class="PageHeader">
			<div class="Contents">
				<a href="/board/index" class="Logo">Board</a>
				<div class="RightMenu">
					<c:choose>
						<c:when test="${logged eq true }">
							<div class="Button" onclick="logoutAction()"> 로그아웃 </div>
							<a href="/board/editor" class="Button"> 글쓰기 </a>
						</c:when>
						<c:otherwise>
							<a href="/board/login" class="Button"> 로그인 / 회원가입 </a>
						</c:otherwise>
					</c:choose>
					
				</div>
			</div>
		</div>
		<main>
		<form id="LogoutForm" name="LogoutForm" action="/board/logout" method="POST">
			
		</form>
		
		<script>
			function logoutAction() {
				document.getElementById("LogoutForm").submit();
			}
		</script>