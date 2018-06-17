<%@include file="./PageTemplates/PageHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${isUpdate eq true }">
		<form class="EditorForm" name="EditorForm" action="/board/update"
		method="POST" enctype="multipart/form-data">
		<input type="hidden" name="id" value="${board.id }"/>
	</c:when>
	<c:otherwise>
		<form class="EditorForm" name="EditorForm" action="/board/editor"
		method="POST" enctype="multipart/form-data">
	</c:otherwise>
</c:choose>

	<input type="hidden" name="username" value="${username }"/>
	<div class="ErrorMessage">
		${ErrorMessage }
	</div>	

	<div class="Line">
		<div class="Description">
			글제목 
		</div>
		<div class="InputWrapper">
			<input type="text" name="title" class="Input" value="${board.title }" />
		</div>
	</div>
	<div class="Line">
		<div class="Description">
			글 내용 
		</div>
		<div class="InputWrapper">
			<textarea name="content" class="InputContent">${board.content }</textarea>
		</div>
	</div>
	<div class="Line">
		<div class="Description">
			파일 
		</div>
		<div class="InputWrapper">
			<input type="file" name="file"/>
		</div>
	</div>
	<div class="ButtonWrapper">
		<input type="submit" name="SubmitButton" class="SubmitButton" value="작성하기" />
	</div>
</form>
<%@include file="./PageTemplates/PageFooter.jsp"%>