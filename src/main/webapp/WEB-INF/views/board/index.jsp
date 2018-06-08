<%@include file="./PageTemplates/PageHeader.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div class="BoardForm">
<c:choose>
	<c:when test="${isSearched eq true }">
		<h1>검색된 리스트 </h1>
		<div class="Contents">
		<div class="Line">
			<div class="Header">
				<div class="Head">글 번호</div>
				<div class="Head">글 제목</div>
				<div class="Head">글쓴이</div>
				<div class="Head">글쓴 날짜</div>
			</div>
		</div>
		<c:choose>
			<c:when test="${emptyList eq true }">
				<div class="EmptyList">
					<div class="Text">
						검색된 게시물이 없습니다.
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="Line">
					<div class="Body">
						<c:forEach items="${searchedList }" var="board">
							<a href="/board/${board.id }" class="InnerLine">
								<div class="Content">
									${board.id }
								</div>
								<div class="Content">
									 ${board.title }
								</div>
								<div class="Content">
									${board.username }
								</div>
								<div class="Content">
									<f:formatDate
											value="${board.createdAt }" pattern="yyyy-MM-dd HH:mm" />
									
								</div>
							
							</a>
						</c:forEach>

					</div>
				</div>
			</c:otherwise>
		</c:choose>
		

	</div>
	</c:when>
	<c:otherwise>
		<h1>게시판 리스트</h1>
		<div class="Contents">
		<div class="Line">
			<div class="Header">
				<div class="Head">글 번호</div>
				<div class="Head">글 제목</div>
				<div class="Head">글쓴이</div>
				<div class="Head">글쓴 날짜</div>
			</div>
		</div>
		<c:choose>
			<c:when test="${emptyList eq true }">
				<div class="EmptyList">
					<div class="Text">
						현재 게시글이 없습니다!
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="Line">
					<div class="Body">
						<c:forEach items="${boardList }" var="board">
							<a href="/board/${board.id }" class="InnerLine">
								<div class="Content">
									${board.id }
								</div>
								<div class="Content">
									 ${board.title }
								</div>
								<div class="Content">
									${board.username }
								</div>
								<div class="Content">
									<f:formatDate
											value="${board.createdAt }" pattern="yyyy-MM-dd HH:mm" />
									
								</div>
							
							</a>
						</c:forEach>

					</div>
				</div>
			</c:otherwise>
		</c:choose>

	</div>
	</c:otherwise>
</c:choose>
	
</div>
<c:if test="${emptyList eq false }">
<div class="SearchZone">
	<div class="Selector">
		<select id="searchSelector" onchange="onChangeSelector();">
			<option value="title">제목</option>
			<option value="username">글쓴이</option>
			<option value="content">내용</option>
		</select>
	</div>
	<script type="text/javascript">
		function onChangeSelector() {
			var selector = document.getElementById("searchSelector");
			var category = selector.options[selector.selectedIndex].value;
			var hiddenCategory = document.getElementById("searchCategory");
			hiddenCategory.value = category;
		}	
	</script>
	<form name="SearchForm" action="/board/search" method="POST">
		<input type="text" name="searchWord" />
		<input id="searchCategory" type="hidden" name="searchCategory" value="title"/>
			<input type="hidden" name="page" value="1" />

	</form>
	<form id="SearchByPagingNextForm" action="/board/search" method="POST">
		<input type="hidden" name="searchWord" value="${searchWord }"/>
		<input type="hidden" name="searchCategory" value="${category }"/>
		<input type="hidden" name="page" value="${SearchedPage + 1 }" />
	</form>
	
	<form id="SearchByPagingPrevForm" action="/board/search" method="POST">
		<input type="hidden" name="searchWord" value="${searchWord }"/>
		<input type="hidden" name="searchCategory" value="${category }"/>
		<input type="hidden" name="page" value="${SearchedPage - 1 }" />
	</form>
</div>
<c:choose>
	<c:when test="${isSearched eq true }">
	<div class="Pagination">
	<c:choose>
		<c:when test="${isFirstPage eq true }">
			<div class="DisabledButton"> 이전 페이지 </div>
		</c:when>
		<c:otherwise>
			<div class="Button" onclick="prevSearchedPage();"> 이전 페이지 </div>
		</c:otherwise>
	</c:choose>
	<script type="text/javascript">
			function prevSearchedPage() {
				document.getElementById("SearchByPagingPrevForm").submit();
			}
		</script>

	<div class="Page">
		<c:if test="${lessThanDpp eq true }">
			<c:forEach begin="1" end="${LastPage}" var="i">
				<c:choose>
					<c:when test="${i eq Page }">
						<div class="PageButtonCurrent" onclick="SearchedPageByClick(event);">
							${i }
						</div>
					</c:when>
					<c:otherwise>
						<div class="PageButton" onclick="SearchedPageByClick(event);">
							${i }
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:if>

		<c:if test="${lessThanDpp eq false and lastPageZone eq true }">
			<c:forEach begin="${startPage }" end="${LastPage }" var="i">
				<c:choose>
					<c:when test="${i eq Page }">
						<div class="PageButtonCurrent" onclick="SearchedPageByClick(event);">
							${i }
						</div>
					</c:when>
					<c:otherwise>
						<div class="PageButton" onclick="SearchedPageByClick(event);">
							${i }
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:if>

		<c:if test="${lessThanDpp eq false and lastPageZone eq false }">
			<c:forEach begin="${startPage }" end="${startPage + 9 }" var="i">
				<c:choose>
					<c:when test="${i eq Page }">
						<div class="PageButtonCurrent" onclick="SearchedPageByClick(event);">
							${i }
						</div>
					</c:when>
					<c:otherwise>
						<div class="PageButton" onclick="SearchedPageByClick(event);">
							${i }
						</div>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</c:if>
		
		<form id="SearchedClickForm" action="/board/search" method="POST">
			<input type="hidden" name="searchWord" value="${searchWord }"/>
			<input type="hidden" name="searchCategory" value="${category }"/>
			<input type="hidden" id="ClickFormPage" name="page"/>
		</form>

		<script type="text/javascript">
			function SearchedPageByClick(event) {
				document.getElementById("ClickFormPage").value = parseInt(event.target.innerHTML.trim(), 10);
				document.getElementById("SearchedClickForm").submit();
			}
		</script>

	</div>
	
	<c:choose>
		<c:when test="${isLastPage eq true }">
			<div class="DisabledButton"> 다음 페이지 </div>
		</c:when>
		<c:otherwise>
			<div class="Button" onclick="nextSearchedPage();"> 다음 페이지 </div>
			
		</c:otherwise>
		
	</c:choose>
	<script type="text/javascript">
			function nextSearchedPage() {
				document.getElementById("SearchByPagingNextForm").submit();
			}
		</script>
</div>
	</c:when>
	<c:otherwise>
		<div class="Pagination">
	<c:choose>
		<c:when test="${isFirstPage eq true }">
			<a href="#" class="DisabledButton"> 이전 페이지 </a>
		</c:when>
		<c:otherwise>
			<a href="/board/index?page=${Page - 1 }" class="Button"> 이전 페이지 </a>
		</c:otherwise>
	</c:choose>

	<div class="Page">
		<c:if test="${lessThanDpp eq true }">
			<c:forEach begin="1" end="${LastPage}" var="i">
				<c:choose>
					<c:when test="${i eq Page }">
						<div class="PageButtonCurrent">
							<a href="/board/index?page=${i }">${i }</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="PageButton">
							<a href="/board/index?page=${i }">${i }</a>
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:if>

		<c:if test="${lessThanDpp eq false and lastPageZone eq true }">
			<c:forEach begin="${startPage }" end="${LastPage }" var="i">
				<c:choose>
					<c:when test="${i eq Page }">
						<div class="PageButtonCurrent">
							<a href="/board/index?page=${i }">${i }</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="PageButton">
							<a href="/board/index?page=${i }">${i }</a>
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:if>

		<c:if test="${lessThanDpp eq false and lastPageZone eq false }">
			<c:forEach begin="${startPage }" end="${startPage + 9 }" var="i">
				<c:choose>
					<c:when test="${i eq Page }">
						<div class="PageButtonCurrent">
							<a href="/board/index?page=${i }">${i }</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="PageButton">
							<a href="/board/index?page=${i }">${i }</a>
						</div>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</c:if>


	</div>
	
	<c:choose>
		<c:when test="${isLastPage eq true }">
			<a href="#" class="DisabledButton"> 다음 페이지 </a>
		</c:when>
		<c:otherwise>
			<a href="/board/index?page=${Page + 1 }" class="Button"> 다음 페이지 </a>
		</c:otherwise>
	</c:choose>
</div>
	</c:otherwise>
</c:choose>
</c:if>

<%@include file="./PageTemplates/PageFooter.jsp"%>