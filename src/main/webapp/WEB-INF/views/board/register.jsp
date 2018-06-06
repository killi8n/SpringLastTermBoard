<%@include file="./PageTemplates/PageHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form name="LoginForm" action="/board/register" method="POST" class="LoginForm">
	<div class="Contents">
		<div class="Logo">
			회원가입 
		</div>
		<div class="ErrorMessage">
			${ErrorMessage }
		</div>
		<div class="Line">
			<div class="Label">
				아이디 
			</div>
			<div class="InputWrapper">
				<input type="text" name="username" class="Input" value="${username }" />
			</div>
		</div>
		<div class="Line">
			<div class="Label">
				패스워드 
			</div>
			<div class="InputWrapper">
				<input type="password" name="password" class="Input" value="${password }" />
			</div>
		</div>
		<div class="Line">
			<div class="Label">
				패스워드 체크  
			</div>
			<div class="InputWrapper">
				<input type="password" name="passwordCheck" class="Input" value="${passwordCheck }" />
			</div>
		</div>
		<div class="Line">
			<div class="Label">
				이메일 
			</div>
			<div class="InputWrapper">
				<input type="email" name="email" class="Input" value="${email }" />
			</div>
		</div>
		<div class="ButtonWrapper">
			<input type="submit" class="Button" name="SubmitButton" value="회원가입"/>
		</div>
		<div class="Description">
			이미 회원이신가요? <a href="/board/login">로그인하러 가기</a>
		</div>
	</div>
</form>

<%@include file="./PageTemplates/PageFooter.jsp"%>