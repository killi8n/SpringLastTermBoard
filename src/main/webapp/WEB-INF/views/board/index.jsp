<%@include file="./PageTemplates/PageHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div class="BoardForm">
	<div class="Contents">
		<div class="Line">
			<div class="Header">
				<div class="Head">글 번호</div>
				<div class="Head">글 제목</div>
				<div class="Head">글쓴이</div>
				<div class="Head">글쓴 날짜</div>
			</div>
		</div>
		<div class="Line">
			<div class="Body">
				<c:forEach items="${boardList }" var="board">
					<div class="InnerLine">
						<div class="Content">
							<a href="/board/${board.id }"> ${board.id } </a>
						</div>
						<div class="Content">
							<a href="/board/${board.id }"> ${board.title } </a>
						</div>
						<div class="Content">
							<a href="/board/${board.id }"> ${board.username } </a>
						</div>
						<div class="Content">
							<a href="/board/${board.id }"> <f:formatDate
									value="${board.createdAt }" pattern="yyyy-MM-dd HH:mm" />
							</a>
						</div>
					</div>
				</c:forEach>

			</div>
		</div>
	</div>
</div>
<div class="Pagination">
	<a href="/board/index?page=${Page - 1 }" class="Button"> 이전 페이지 </a>
	<div class="Page">Page ${Page }</div>
	<a href="/board/index?page=${Page + 1 }" class="Button"> 다음 페이지 </a>
</div>

<%@include file="./PageTemplates/PageFooter.jsp"%>