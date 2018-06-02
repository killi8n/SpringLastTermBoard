<%@include file="./PageTemplates/PageHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form name="LoginForm" action="/board/login" method="POST"
	class="LoginForm">
	<div class="Contents">
		<div class="Logo">로그인</div>
		<div class="ErrorMessage">${ErrorMessage }</div>
		<div class="Line">
			<div class="Label">아이디</div>

			<div class="InputWrapper">
				<input type="text" name="username" class="Input" />
			</div>
		</div>
		<div class="Line">
			<div class="Label">패스워드</div>
			<div class="InputWrapper">
				<input type="password" name="password" class="Input" />
			</div>
		</div>

		<div class="ButtonWrapper">
			<input type="submit" class="Button" name="SubmitButton" value="로그인" />
		</div>
		<div class="Description">
			아직 가입하지 않으셨나요? <a href="/board/register">가입하러 가기</a>
		</div>
	</div>
</form>

<%@include file="./PageTemplates/PageFooter.jsp"%>