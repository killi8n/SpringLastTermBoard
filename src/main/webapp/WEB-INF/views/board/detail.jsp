<%@include file="./PageTemplates/PageHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="BoardDetailForm">
	<form id="RemoveForm" action="/board/remove/${board.id }" method="POST">
		<input type="hidden" value="${board.id }"/>
	</form>
	<div class="Contents">
		<div class="Line">
			<div class="Description">제목</div>
			<div class="Content">${board.title }</div>
		</div>
		<div class="Line">
			<div class="Description">글쓴이</div>
			<div class="Content">${board.username }</div>
		</div>
		<div class="Line">
			<div class="Description">날짜</div>
			<div class="Content">
				<f:formatDate value="${board.createdAt }" pattern="yyyy-MM-dd HH:mm" />
			</div>
		</div>
		<div class="Line">
			<div class="Description">내용</div>
			<div class="Content">${board.content }</div>
		</div>

		<c:if test="${owner eq true }">
			<div class="ButtonWrapper">
				<a href="/board/editor?id=${board.id }" class="UpdateButton">
					수정하기
				</a>
				<div onclick="removeBoard()" class="RemoveButton">
					삭제하기
				</div>
			</div>
		</c:if>

	</div>
</div>
<script>
	function removeBoard() {
		document.getElementById("RemoveForm").submit();
	}
</script>
<%@include file="./PageTemplates/PageFooter.jsp"%>