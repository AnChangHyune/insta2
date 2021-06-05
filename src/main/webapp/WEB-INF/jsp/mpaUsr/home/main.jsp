<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle"
	value="<span><i class='fas fa-home'></i></span> <span>HOME</span>" />
<script>

</script>
<%@ include file="../common/head.jspf"%>
<div id="fullscroll-0" class="fullscroll-container">
	<section class="onepage section-0">
		<p>Instar blog</p>
	</section>	
	<section class="onepage section-1">
		<div class="newArticle">
		<div>
			<div class="mb-3 flex items-center">
				<div>
				 	<h1>최신글</h1>
				</div>
				<div>
					<a href="/mpaUsr/article/list?boardId=1" style="float: right;"><i class="fas fa-plus"></i></a>
				</div>
			</div>
			
			<div class="newArticle-list">
			<hr />
				<c:forEach items="${articles}" var="article">
					<div class="mt-8 mb-4" style="width: 100%;">
							<span class="ico_outer"><i class="ico">NEW</i></span>
							<a href="../article/detail?id=${article.id}" class="title-list">${article.title}</a>
							<a href="../article/detail?id=${article.id}" class="regDate-list">${article.regDate}</a>
					</div>
						
				</c:forEach>
			</div>
		</div>
			<div>
				<div class="mb-3 flex items-center ">
				<div>
					<h1>인기글</h1>
				</div>
				<div>
					<a href="/mpaUsr/article/list?boardId=1" style="float: right;"><i class="fas fa-plus"></i></a>
				</div>
			</div>
				<div class="newArticle-list">
					<hr />
					<c:forEach items="${articles}" var="article">
						<div class="mt-8 mb-4" style="width: 100%;">
								<span class="ico_outer"><i class="ico">Best</i></span>
								<a href="../article/detail?id=${article.id}" class="title-list">${article.title}</a>
								<a href="../article/detail?id=${article.id}" class="regDate-list">${article.regDate}</a>
						</div>
							
					</c:forEach>
				</div>
			</div>
		</div>
	</section>
	<section class="onepage section-2"></section>
	<section class="onepage section-3"></section>
	<footer class="section-footer">
		<div>
			instar
		</div>
		<div>
			<a href="https://twitter.com/minimalmonkey" class="icon-button twitter"><i class="icon-twitter"></i><span></span></a>
			<a href="https://facebook.com" class="icon-button facebook"><i class="icon-facebook"></i><span></span></a>
			<a href="https://plus.google.com" class="icon-button google-plus"><i class="icon-google-plus"></i><span></span></a>
		</div>
	</footer>
</div>

<%@ include file="../common/foot.jspf"%>