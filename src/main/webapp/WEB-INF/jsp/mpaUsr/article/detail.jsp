<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jspf"%>


<div class="section section-article-detail">
	<div class="container mx-auto">
		<div>
			<div>
				<div class="px-4">
					<div style="width: 100%; height: 100%; display: flex; align-items: center;">
						<div style="width: 40px; height: 40px;">
							<a href="detail?id=${article.id}"> <img class="w-10 h-10 object-cover rounded-full" onerror="${article.writerProfileFallbackImgOnErrorHtmlAttr}" src="${article.writerProfileImgUri}" alt="">
							</a>
						</div>
						<div class="ml-4">
							<a href="detail?id=${article.id}"
								style="display: block; font-weight: bolder;">${article.extra__writerName}</a>
							<a href="detail?id=${article.id}"
								style="display: block; font-size: 12px;">${article.regDate}</a>
						</div>
						<div class="flex-grow"></div>
						
					</div>

					<div class="mt-4">
						<div>${article.title}</div>
						<div class="flex mt-8 mb-2">
							<span> <i class="fas fa-heart"></i> <span
							class="text-gray-400 text-light">120k</span>
							</span>
							<div class="flex-grow"></div>
							<span>Number : </span> <span class="text-gray-400 text-light">${article.id}</span>
							<span class="ml-3"> </span> <span class="ml-3"> <span>Views:</span>
								<span class="text-gray-400 text-light">60k</span>
							</span>
						</div>
					</div>
					<hr />

					<div class="mt-6">

						<div class="mt-3">
							<img class="rounded" onerror="${article.writerfileFallbackImgOnErrorHtmlAttr}" src="${article.writerfileImgUri}" alt="">
						</div>
						<div class="mt-3">${article.bodyForPrint}</div>
					</div>
				</div>
			</div>
			   <!-- 댓글 수정 모달 시작 -->
            <style>
            .section-reply-modify {
                position:fixed;
                top:0;
                left:0;
                width:100%;
                height:100%;
                background-color:rgba(0,0,0,0.5);
                z-index:10;
                display:none;
                align-items:center;
                justify-content:center;
            }
            .section-reply-modify > div {
                background-color:white;
                padding:20px 30px;
                border-radius:30px;
            }
            </style>

            <script>
            function ReplyModify__showModal(el) {
                const $div = $(el).closest('[data-id]');
                const replyId = $div.attr('data-id');
                const replyBody = $div.find('.reply-body').html();
                $('.section-reply-modify [name="id"]').val(replyId);
                $('.section-reply-modify [name="body"]').val(replyBody);
                $('.section-reply-modify').css('display', 'flex');
            }
            function ReplyModify__hideModal() {
                $('.section-reply-modify').hide();
            }
            let ReplyModify__submitFormDone = false;
            function ReplyModify__submitForm(form) {
                if ( ReplyModify__submitFormDone ) {
                    return;
                }
                form.body.value = form.body.value.trim();
                if ( form.body.value.length == 0 ) {
                    alert('내용을 입력해주세요.');
                    form.body.focus();
                    return;
                }
                form.submit();
                ReplyModify__submitFormDone = true;
            }
            </script>

            <div class="section section-reply-modify hidden">
                <div>
                    <div class="container mx-auto">
                        <form method="POST" enctype="multipart/form-data" action="../reply/doModify" onsubmit="ReplyModify__submitForm(this); return false;">
                            <input type="hidden" name="id" value="" />
                            <input type="hidden" name="redirectUri" value="${rq.currentUri}" />

                            <div class="form-control">
                                <label class="label">
                                    내용
                                </label>
                                <textarea class="textarea textarea-bordered w-full h-24" placeholder="내용을 입력해주세요." name="body" maxlength="2000"></textarea>
                            </div>

                            <div class="mt-4 btn-wrap gap-1">
                                <button type="submit" href="#" class="btn btn-primary btn-sm mb-1">
                                    <span><i class="far fa-edit"></i></span>
                                    &nbsp;
                                    <span>수정</span>
                                </button>

                                <button type="button" onclick="history.back();" class="btn btn-sm mb-1" title="닫기">
                                    <span><i class="fas fa-list"></i></span>
                                    &nbsp;
                                    <span>닫기</span>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <!-- 댓글 수정 모달 끝 -->


			

			<div>
				<c:if test="${rq.notLogined}">
                    <div class="text-center py-4">
                        글 작성은 <a class="plain-link" href="${rq.loginPageUri}">로그인</a> 후 이용할 수 있습니다.
                    </div>
                </c:if>
				<c:if test="${rq.logined}">
					<div class="px-4 py-8">
						<!-- 댓글 입력 시작 -->
						<form enctype="multipart/form-data" method="POST" action="../reply/doWrite"
							class="relative flex py-4 text-gray-600 focus-within:text-gray-400">
							<input type="hidden" name="relTypeCode" value="article" /> <input
								type="hidden" name="relId" value="${article.id}" /> <input
								type="hidden" name="redirectUri" value="${rq.currentUri}" /> 
							<img class="w-10 h-10 object-cover rounded-full shadow mr-2 cursor-pointer" onerror="${rq.loginedMember.profileFallbackImgOnErrorHtmlAttr}" src="${rq.loginedMember.profileImgUri}" alt="">
							<span class="absolute inset-y-0 right-0 flex items-center pr-6">
								<button type="submit"
									class="p-1 focus:outline-none focus:shadow-none hover:text-blue-500">
									<svg
										class="w-6 h-6 transition ease-out duration-300 hover:text-blue-500 text-gray-400"
										xmlns="http://www.w3.org/2000/svg" fill="none"
										viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round"
											stroke-linejoin="round" stroke-width="2"
											d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                </svg>
								</button>
							</span> <input type="text" name="body"
								class="w-full py-2 pl-4 pr-10 text-sm bg-gray-100 border border-transparent appearance-none rounded-tg placeholder-gray-400 focus:bg-white focus:outline-none focus:border-blue-500 focus:text-gray-900 focus:shadow-outline-blue"
								style="border-radius: 25px" placeholder="댓글을 입력해주세요."
								autocomplete="off">
						</form>
						<!-- 댓글 입력 끝 -->
					</div>
				</c:if>
				
				<!-- 댓글 리스트 -->
                <style>
                .reply-list [data-id] {
                  transition: background-color 1s;
                }
                .reply-list [data-id].focus {
                  background-color:#efefef;
                  transition: background-color 0s;
                }
                </style>

                <script>
                function ReplyList__goToReply(id) {
                    setTimeout(function() {
                        const $target = $('.reply-list [data-id="' + id + '"]');
                        const targetOffset = $target.offset();
                        $(window).scrollTop(targetOffset.top - 50);
                        $target.addClass('focus');
                        setTimeout(function() {
                            $target.removeClass('focus');
                        }, 1000);
                    }, 1000);
                }
                function ReplyList__deleteReply(btn) {
                    const $clicked = $(btn);
                    const $target = $clicked.closest('[data-id]');
                    const id = $target.attr('data-id');
                    $clicked.text('삭제중...');
                    $.post(
                        '../reply/doDeleteAjax',
                        {
                            id: id
                        },
                        function(data) {
                            if ( data.success ) {
                                $target.remove();
                            }
                            else {
                                if ( data.msg ) {
                                    alert(data.msg);
                                }
                                $clicked.text('삭제실패!!');
                            }
                        },
                        'json'
                    );
                }
                if ( param.focusReplyId ) {
                    ReplyList__goToReply(param.focusReplyId);
                }
                </script>

                <div class="reply-list ml-6">
                    <c:forEach items="${replies}" var="reply">
                        <div data-id="${reply.id}" class="py-5 px-4">
                        	<script type="text/x-template" class="reply-body hidden">${reply.body}</script>
                            <div class="flex">
                                <!-- 아바타 이미지 -->
                                <div class="flex-shrink-0">
									<img class="w-10 h-10 object-cover rounded-full shadow mr-2 cursor-pointer" onerror="${reply.writerProfileFallbackImgOnErrorHtmlAttr}" src="${reply.writerProfileImgUri}" alt="">
                                </div>


                                <div class="flex-grow px-1">
                                    <div class="flex text-gray-400 text-light text-sm">
                                        <spqn>${reply.extra__writerName}</spqn>
                                        <span class="mx-1">·</span>
                                        <spqn>${reply.updateDate}</spqn>
                                    </div>
                                    <div class="break-all">
                                        ${reply.bodyForPrint}
                                    </div>
                                    <div class="mt-1">
                                        <span class="text-gray-400 cursor-pointer">
                                            <span><i class="fas fa-thumbs-up"></i></span>
                                            <span>5,600</span>
                                        </span>
                                        <span class="ml-1 text-gray-400 cursor-pointer">
                                            <span><i class="fas fa-thumbs-down"></i></span>
                                            <span>5,600</span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="plain-link-wrap gap-3 mt-3 pl-14">
                                <c:if test="${reply.memberId == rq.loginedMemberId}">
                                    <a onclick="if ( confirm('정말 삭제하시겠습니까?') ) { ReplyList__deleteReply(this); } return false;" class="plain-link cursor-pointer">
                                        <span><i class="fas fa-trash-alt"></i></span>
                                        <span>삭제</span>
                                    </a>
                                </c:if>
                                 <c:if test="${reply.memberId == rq.loginedMemberId}">
                                    <button onclick="ReplyModify__showModal(this);" class="plain-link">
                                        <span><i class="far fa-edit"></i></span>
                                        <span>수정</span>
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
			</div>
		</div>
	</div>
</div>
<div class="card-buttm">
	<a href="javascript:history.back();" class="cursor-pointer"> 
		<i class="fas fa-chevron-left"></i>
		<i class="fas fa-chevron-right"></i>
	</a>
</div>

<%@ include file="../common/foot.jspf"%>