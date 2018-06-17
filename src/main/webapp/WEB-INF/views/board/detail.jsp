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
			<c:if test="${board.savename ne null }">
				<div class="ImageWrapper">
					<img class="Image" src="/board/image?${board.savename }" />
				</div>
			</c:if>
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
		<div class="ReplyZone">
			<div class="ReplyDescription">
				댓글 작성 
			</div>
			<div class="ReplyTextArea">
				<textarea name="replyText" id="ReplyText" class="TextArea"></textarea>
			</div>
			<div class="ReplyButton" id="ReplyButton">
				<div class="Text">
					댓글 작성
				</div>
			</div>
			<div class="ReplyLists" id="ReplyLists">
				<c:forEach items="${replyList }" var="reply" begin="0" end="${replyList.size() }">
					<div class="Line">
						<div class="Left">
							<div class="UserName">
								${reply.username }
							</div>
						</div>
						<div class="Right">
							<div class="ReplyText">
								<div class="Text" id="ReplyContent${reply.id }">
									${reply.replytext }
								</div>
								<input type="text" value=${reply.replytext } id="ReplyTextUpdate${reply.id }" style="display: none;"/>
							</div>
							<div class="ReplyDate">
							<f:formatDate pattern = "yyyy-MM-dd HH:mm" value = "${reply.regdate }" />
							<c:if test="${reply.username eq username }">
								<div class="UpdateOrDeleteZone">
									<div class="Update" id="${reply.id }" onclick="updateReplyRequest(event);">
										수정하기 
									</div>
									<div class="Delete" id="${reply.id }" onclick="deleteReplyRequest(event);">
										삭제하기 
									</div>
									<div class="Update" style="display: none;" id="updateReplyButton/${reply.id }" onclick="updateReplyRequestSend(event);">
										수정하기 
									</div>
									<div class="Update" style="display: none;" id="cancelReplyButton/${reply.id }" onclick="cancelReplyRequest(event);">
										취소하기  
									</div>
								</div>
							</c:if>
							</div>
						</div>
					</div>
				</c:forEach>
				

			</div>
		</div>
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
	<input type="hidden" id="UserName" value="${username }"/>
	<input type="hidden" id="BoardId" value="${board.id }"/>
</div>
<script>
	function removeBoard() {
		document.getElementById("RemoveForm").submit();
	}
	
	function cancelReplyRequest(event) {
		var id = event.target.id.split("/")[1];
		event.target.style.display = "none";
		document.getElementById('ReplyTextUpdate' + id).style.display = "none";
		document.getElementById('ReplyContent' + id).style.display = "block";
		document.getElementById('updateReplyButton/' + id).style.display = "none";
		document.getElementById(id).style.display = "block";
	}
	
	function updateReplyRequestSend(event) {
		var id = event.target.id.split("/")[1];
		console.log(id);
		
		var updateContent = document.getElementById('ReplyTextUpdate' + id).value;
		console.log(updateContent);
		
		var data = {};
		data["replyText"] = updateContent;
		data["id"] = id;
		
		$.ajax({
			contentType: "application/json",
			url: '/reply/',
			type: 'PATCH',
			data: JSON.stringify(data),
			success: function(data) {
				if(data === "success") {
					
					document.getElementById('ReplyContent' + id).innerHTML = updateContent;
					document.getElementById('ReplyContent' + id).style.display = "block";
					
					document.getElementById('ReplyTextUpdate' + id).style.display = "none";
					document.getElementById('cancelReplyButton/' + id).style.display = "none";
					document.getElementById('updateReplyButton/' + id).style.display = "none";
					document.getElementById(id).style.display = "block";
					
					
				}
				console.log("success");
			},
			error: function(err) {
				console.log("error");
			}
		}); 
	}
	
	function updateReplyRequest(event) {
		
		
		
		console.log(event.target.id);
		
		var id = event.target.id;
		document.getElementById('ReplyTextUpdate' + id).style.display = "block";
		document.getElementById('ReplyContent' + id).style.display = "none";
		document.getElementById('cancelReplyButton/' + id).style.display = "block";
		document.getElementById('updateReplyButton/' + id).style.display = "block";
		event.target.style.display = "none";
		
		

		
		
	}
	
	function deleteReplyRequest(event) {
		console.log(event.target.id);
		
		var targetThing = event.target;
		var $targetTarget = targetThing.parentNode.parentNode.parentNode.parentNode;
		
		
		
		
		$.ajax({
			contentType: "application/json",
			url: '/reply/' + event.target.id,
			type: 'DELETE',
			success: function(data) {
				if(data === "success") {
					$targetTarget.remove();
				}
			},
			error: function(err) {
				console.log("error");
			}
		});
		
	}
	
	(function() {
		$(document).ready(function() {
			$('#ReplyButton').on('click', function() {
				var $replyText = $('#ReplyText').val();
				var $username = $('#UserName').val();
				var $boardId = $('#BoardId').val();
				
				var data = {};
				data["replyText"] = $replyText;
				data["username"] = $username;
				data["boardId"] = $boardId;
				
				if($replyText) {
					$.ajax({
						contentType: "application/json",
						url: '/reply/',
						type: 'POST',
						data: JSON.stringify(data),
						success: function(data) {
							/* var responseText = data.responseText; */
							console.log(data);
							
							var replyDate = new Date(data.regdate);
							var year = replyDate.getFullYear();
							var month = replyDate.getMonth() + 1;
							month = month.length > 1 ? month : '0' + month;
							var day = replyDate.getDate();
							var hour = replyDate.getHours();
							var minute = replyDate.getMinutes();
							
							var replyText = data.replytext;
							var username = data.username;
							var id = data.id;
							if(data) {
								$('#ReplyLists').append(
										'<div class="Line">' + 
										'<div class="Left">' + 
											'<div class="UserName">' + 
												username + 
											'</div>' + 
										'</div>' + 
										'<div class="Right">' + 
											'<div class="ReplyText">' + 
												'<div class="Text" id="ReplyContent' + id + '">' + 
													replyText + 
												'</div>' +
												'<input type="text" value="' + replyText +  '" id="ReplyTextUpdate' + id + '" style="display: none;"/>' + 
											'</div>' + 
											'<div class="ReplyDate">' + 
												year + '-' + month + '-' + day + ' ' + hour + ':' + minute +
												'<div class="UpdateOrDeleteZone">' + 
												'<div class="Update" id="' + id + '" onclick="updateReplyRequest(event);">' + 
													'수정하기' + 
												'</div>' + 
												'<div class="Delete" id="' + id + '" onclick="deleteReplyRequest(event);">' + 
													'삭제하기' + 
												'</div>' + 
												'<div class="Update" style="display: none;" id="updateReplyButton/' + id + '" onclick="updateReplyRequestSend(event);">' + 
												'수정하기' + 
												'</div>' + 
												'<div class="Update" style="display: none;" id="cancelReplyButton/' + id + '" onclick="cancelReplyRequest(event);">' + 
												'취소하기' + 
												'</div>' + 
											'</div>' + 
											'</div>' + 
										'</div>' + 
									'</div>'
								);
								$('#ReplyText').val('');
								
							}
							
						},
						error: function(err) {
							alert("알수없는 오류가 났습니다. 새로고침 후 다시 시도해주세요 ");
						}
					});
				} else {
					alert("댓글 내용을 입력해주세요.. ");
				}
				return false;
			});
		});
	})();
</script>
<%@include file="./PageTemplates/PageFooter.jsp"%>