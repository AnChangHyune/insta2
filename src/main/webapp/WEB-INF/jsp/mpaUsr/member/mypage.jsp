<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.sbs.untactTeacher.util.Util" %>

<%@ include file="../common/head.jspf"%>

<div class="section section-mypage px-2">
	<div class="container mx-auto">
		<div>
			<div style="font-size: 50px; text-align: center; color: gray; letter-spacing: 10px; font-family: 'Caveat', cursive; margin-bottom: 50px;">MYPAGE</div>
			<div>
				<a href="#">
                  <img style="margin: 0 auto;" class="w-40 h-40 object-cover rounded-full" onerror="${rq.loginedMember.profileFallbackImgOnErrorHtmlAttr}" src="${rq.loginedMember.profileImgUri}" alt="">
                </a>
			</div>
			<div style="margin: 0 auto; width: 100%; text-align: center;">
				<div class="mt-5">				
                    <a href="../member/checkPassword?afterUri=${Util.getUriEncoded('../member/modify')}" class="text-blue-500 hover:underline">
                        <span>
                            <i class="fas fa-edit"></i>
                            <span>수정</span>
                        </span>
                    </a>
                    <a onclick="if ( !confirm('삭제하시겠습니까?') ) return false;" href="#" class="text-blue-500 hover:underline">
                        <span>
                            <i class="fas fa-trash"></i>
                            <span>탈퇴</span>
                        </span>
                    </a>
				</div>
            </div>
			<div class="mypage-list px-4 py-8">
				<ul>
					 <li>
	                    <a href="#">
	                        <strong class="badge badge-accent">회원타입</strong>
	                        <span>${rq.loginedMember.authLevelName}</span>
	                    </a>
	                 </li>
	                 <li>	                 
	                      <a href="#">
	                        <strong class="badge">아이디</strong>
	                        <span class="text-gray-600">${rq.loginedMember.loginId}</span>
	                    </a>
	                 </li>   
	                 <li>
	                 	<a href="#">
	                        <strong class="badge">이름</strong>
	                        <span class="text-gray-600">${rq.loginedMember.name}</span>
	                    </a>
	                 </li>
	                 <li>
	                      <a href="#">
	                        <strong class="badge">별명</strong>
	                        <span class="text-gray-600">${rq.loginedMember.nickname}</span>
	                    </a>
	                 </li>
	                  <li>
	                     <a href="#">
	                        <strong class="badge">등록날짜</strong>
	                        <span class="text-gray-600 text-light">${rq.loginedMember.regDate}</span>
	                    </a>
	                  </li>          
	                  <li>
	                    <a href="#">
	                        <strong class="badge">수정날짜</strong>
	                        <span class="text-gray-600 text-light">${rq.loginedMember.updateDate}</span>
	                    </a>
	                 </li>
				</ul>
            </div>
		</div>
	</div>
</div>


<%@ include file="../common/foot.jspf"%>