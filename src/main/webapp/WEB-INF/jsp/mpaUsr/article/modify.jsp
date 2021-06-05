<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ include file="../common/head.jspf"%>

<script>
ArticleModify__fileInputMaxCount = parseInt("${fileInputMaxCount}");
</script>

<script>
ArticleModify__submited = false;
function ArticleModify__checkAndSubmit(form) {
	if ( ArticleModify__submited ) {
		alert('처리중입니다.');
		return;
	}
	
	form.title.value = form.title.value.trim();
	if ( form.title.value.length == 0 ) {
		alert('제목을 입력해주세요.');
		form.title.focus();
		return false;
	}
	form.body.value = form.body.value.trim();
	if ( form.body.value.length == 0 ) {
		alert('내용을 입력해주세요.');
		form.body.focus();
		return false;
	}
	var maxSizeMb = 50;
	var maxSize = maxSizeMb * 1024 * 1024;
	for ( let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
		const input = form["file__article__0__common__attachment__" + inputNo];
		
		if (input.value) {
			if (input.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				input.focus();
				
				return;
			}
		}
	}
	const startSubmitForm = function(data) {
		if (data && data.body && data.body.genFileIdsStr) {
			form.genFileIdsStr.value = data.body.genFileIdsStr;
		}
		
		for ( let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
			const input = form["file__article__0__common__attachment__" + inputNo];
			input.value = '';
		}
		
		form.submit();
	};
	const startUploadFiles = function(onSuccess) {
		var needToUpload = false;
		for ( let inputNo = 1; inputNo <= ArticleModify__fileInputMaxCount; inputNo++ ) {
			const input = form["file__article__0__common__attachment__" + inputNo];
			if ( input.value.length > 0 ) {
				needToUpload = true;
				break;
			}
		}
		
		if (needToUpload == false) {
			onSuccess();
			return;
		}
		
		var fileUploadFormData = new FormData(form);
		
		$.ajax({
			url : '/common/genFile/doUpload',
			data : fileUploadFormData,
			processData : false,
			contentType : false,
			dataType : "json",
			type : 'POST',
			success : onSuccess
		});
	}
	ArticleModify__submited = true;
	startUploadFiles(startSubmitForm);
}
</script>

<div class="section-article-list">
	<div class="container mx-auto">
		<div class="card bordered shadow-lg item-bt-1-not-last-child">
			<div class="card-title">
				<a href="javascript:history.back();" class="cursor-pointer"> <i
					class="fas fa-chevron-left"></i>
				</a> <span>게시물 수정</span>
			</div>
			<div class="section section-article-write ml-2 mb-5 mr-2">
				<div class="container mx-auto">
					<form method="POST" action="doModify"
						onsubmit="ArticleModify__submitForm(this); return false;">
						<input type="hidden" name="genFileIdsStr" value="" />
						<input type="hidden" name="id" value="${article.id}" />
						<input type="hidden" name="redirectUri" value="${param.redirectUri}" />

						<div class="form-control mt-5">
							<label class="label"> 작성자 </label>
							<div class="text-light text-sm text-gray-400 ml-1">${article.extra__writerName}</div>
						</div>

						<div class="form-control">
							<label class="label"> 게시글 제목 </label>
							<input value="${article.title}" type="text" name="title" autofocus="autofocus"
						class="form-row-input w-full input input-bordered" placeholder="제목을 입력해주세요." />
						</div>

						<div class="form-control">
							<label class="label"> 내용 </label>
							<textarea class="textarea textarea-bordered w-full h-24"
								placeholder="내용을 입력해주세요." name="body" maxlength="2000">${article.body}</textarea>
						</div>

						<div class="mt-4 btn-wrap gap-1">
							<button type="submit" href="#"
								class="btn btn-primary btn-sm mb-1">
								<span><i class="far fa-edit"></i></span> &nbsp; <span>수정</span>
							</button>

							<a href="#" onclick="history.back();" class="btn btn-sm mb-1"
								title="자세히 보기"> <span><i class="fas fa-list"></i></span>
								&nbsp; <span>리스트</span>
							</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>

<%@ include file="../common/foot.jspf"%>