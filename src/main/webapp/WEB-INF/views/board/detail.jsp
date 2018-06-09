<%@include file="./PageTemplates/PageHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="BoardDetailForm">
	<form id="RemoveForm" action="/board/remove/${board.id }" method="POST">
		<input type="hidden" value="${board.id }"/>
	</form>
	<form id="GoodCheckForm" action="/board/good" method="POST">
		<input type="hidden" name="boardId" value="${board.id }"/>
	</form>
	<script type="text/javascript">
		function clickGood() {
			var willSubmitForm = document.getElementById("GoodCheckForm");
			willSubmitForm.submit();
		}
	</script>
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
			<div class="Description">조회수</div>
			<div class="Content">${board.count }</div>
		</div>
		<div class="Line">
			<div class="Description">좋아요</div>
			<div class="Content">${board.good }</div>
			<c:choose>
				<c:when test="${alreadyChecked eq false or alreadyChecked eq null }">
					<i class="far fa-thumbs-up" onclick="clickGood();"></i>
				</c:when>
				<c:otherwise>
					<i class="fas fa-thumbs-up" onclick="clickGood();"></i>
				</c:otherwise>
			</c:choose>
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
		<div class="NextPrevWrapper">
			<c:if test="${isFirst eq true }">
				<a class="Right" href="/board/${nextBoardItem.id }">
					<i class="fas fa-arrow-right"></i>
				</a>
			</c:if>
			<c:if test="${isLast eq true }">
				<a class="Left" href="/board/${prevBoardItem.id }">
					<i class="fas fa-arrow-left"></i>
				</a>
			</c:if>
			<c:if test="${isFirst eq false && isLast eq false }">
				<a class="Left" href="/board/${prevBoardItem.id }">
				<i class="fas fa-arrow-left"></i>
				</a>
				<a class="Right" href="/board/${nextBoardItem.id }">
					<i class="fas fa-arrow-right"></i>
				</a>
			</c:if>
			
		</div>
	</div>
</div>
<script>
	function removeBoard() {
		document.getElementById("RemoveForm").submit();
	}
</script>
<%@include file="./PageTemplates/PageFooter.jsp"%>