<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ include file="../common/head.jspf"%>



<form action="" class="grid gap-2">
	<input type="hidden" name="boardId" value="${board.id}" />
	<header class="main_h">

		<div class="row">
			<a class="logo2" href="#">INSTA</a>

			<nav class="search-bar">
				<div class="sample ten">
					<input value="${param.searchKeyword}" class="searh-input" name="searchKeyword"
						type="text" placeholder="검색어를 입력해주세요." maxlength="10">
					<button type="submit" class="search-btn">
						<i class="fa fa-search"></i>
					</button>
					<button type="reset" class="btn-reset fa fa-times"></button>
				</div>
			</nav>
		</div>
	</header>
	<!-- / row -->
</form>
<div class="writer-ico">
	<div>
		<div class="mb-3">
			<a href="write?boardId=${board.id}"
				class="btn btn-sm border border-gray-200"> <span><i
					class="fas fa-edit"></i></span>
			</a>
		</div>
		<div>
			<a href="/" class="btn btn-sm border border-gray-300"> <span><i
					class="fas fa-home"></i></span>
			</a>
		</div>
	</div>
</div>
<div class="article-list">
	<div class="item-bt-1-not-last-child">
		<c:if test="${articles == null || articles.size() == 0}">
                        검색결과가 존재하지 않습니다.
                </c:if>
		<c:forEach items="${articles}" var="article">
			<!-- 게시물 아이템, first -->
			<div class="px-4 py-8">
				<div
					style="width: 100%; height: 100%; display: flex; align-items: center;">
					<div style="width: 50px; height: 50px;" class="mt-2">
						<a href="detail?id=${article.id}"> <img
							class="w-10 h-10  object-cover rounded-full"
							onerror="${article.writerProfileFallbackImgOnErrorHtmlAttr}"
							src="${article.writerProfileImgUri}" alt="">
						</a>
					</div>
					<div class="ml-2">
						<a href="detail?id=${article.id}"
							style="display: block; font-weight: bolder;">${article.extra__writerName}</a>
						<a href="detail?id=${article.id}"
							style="display: block; font-size: 12px;">${article.regDate}</a>
					</div>
				</div>
				<div class="mt-8 mb-4">
					<a href="detail?id=${article.id}">${article.title}</a>
				</div>
				<hr />
				<div
					class="mt-3 grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-3">
					<a href="detail?id=${article.id}" class="row-span-7"> <img
						class="rounded"
						onerror="${article.writerfileFallbackImgOnErrorHtmlAttr}"
						src="${article.writerfileImgUri}" alt="">
					</a>
				</div>
				<div class="line-clamp-3 mb-12">${article.body}</div>
				<div class="plain-link-wrap gap-3 mt-4 mb-4">
					<a href="detail?id=${board.id}" class="plain-link" title="자세히 보기">
						<span><i class="fas fa-info"></i></span> <span>자세히 보기</span>
					</a> <a href="detail?id=${article.id}" class="plain-link" title="댓글">
						<span><i class="fas fa-comment-dots"></i></span> <span>댓글
							달기</span>
					</a>
					<c:choose>
						<c:when
							test="${article.extra__writerName != rq.loginedMember.nickname}">
						</c:when>
						<c:otherwise>
							<a href="#" class="plain-link"> <span><i
									class="fas fa-edit"></i></span> <span>수정</span>
							</a>
							<a onclick="if ( !confirm('삭제하시겠습니까?') ) return false;"
								href="../article/doDelete?id=${board.id}" class="plain-link">
								<span> <i class="fas fa-trash"></i> <span>삭제</span>
							</span>
							</a>
						</c:otherwise>
					</c:choose>
				</div>
				<div style="width: 100%; height: 5px; background: #5555;"
					class="mt-5"></div>
			</div>
		</c:forEach>
	</div>
</div>
<div class="pages mt-4 mb-4 text-center">
	<c:set var="pageMenuArmSize" value="4" />
	<c:set var="startPage"
		value="${page - pageMenuArmSize >= 1  ? page - pageMenuArmSize : 1}" />
	<c:set var="endPage"
		value="${page + pageMenuArmSize <= totalPage ? page + pageMenuArmSize : totalPage}" />

	<c:set var="uriBase" value="?boardId=${board.id}" />
	<c:set var="uriBase"
		value="${uriBase}&searchKeywordType=${param.searchKeywordType}" />
	<c:set var="uriBase"
		value="${uriBase}&searchKeyword=${param.searchKeyword}" />

	<c:set var="aClassStr"
		value="px-2 inline-block border border-gray-200 rounded text-lg hover:bg-gray-200 mb-10" />

	<c:if test="${startPage > 1}">
		<a class="${aClassStr}" href="${uriBase}&page=1">◀◀</a>
		<a class="${aClassStr}" href="${uriBase}&page=${startPage - 1}">◀</a>
	</c:if>

	<c:forEach var="i" begin="${startPage}" end="${endPage}">
		<a class="${aClassStr} ${page == i ? 'text-red-500' : ''}"
			href="${uriBase}&page=${i}">${i}</a>
	</c:forEach>

	<c:if test="${endPage < totalPage}">
		<a class="${aClassStr}" href="${uriBase}&page=${endPage + 1}">▶</a>

		<a class="${aClassStr}" href="${uriBase}&page=${totalPage}">▶▶</a>
	</c:if>
</div>
<div style="width: 100%; height: 50px;"></div>
<div class="card-buttm">
	<a href="javascript:history.back();" class="cursor-pointer"> 
		<i class="fas fa-chevron-left"></i> 
		<i class="fas fa-chevron-right"></i>
	</a>
	<div>
		<a href="write?boardId=${board.id}"
			class="btn btn-sm border border" style="background: #fff; color: #000;"> <span><i
				class="fas fa-edit"></i></span>
		</a> <a href="/" class="btn btn-sm border border" style="background: #fff; color: #000;"> <span><i
				class="fas fa-home"></i></span>
		</a>
	</div>
</div>
<%@ include file="../common/foot.jspf"%>