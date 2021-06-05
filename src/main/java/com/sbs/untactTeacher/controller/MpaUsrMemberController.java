package com.sbs.untactTeacher.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.sbs.untactTeacher.dto.Member;
import com.sbs.untactTeacher.dto.ResultData;
import com.sbs.untactTeacher.dto.Rq;
import com.sbs.untactTeacher.service.GenFileService;
import com.sbs.untactTeacher.service.MemberService;
import com.sbs.untactTeacher.util.Util;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MpaUsrMemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private GenFileService genFileService;

	@RequestMapping("/mpaUsr/member/mypage")
	public String showMypage(HttpServletRequest req) {
		return "mpaUsr/member/mypage";
	}
	
	@RequestMapping("/mpaUsr/member/getLoginIdDup")
	@ResponseBody
	public ResultData getLoginIdDup(HttpServletRequest req, String loginId) {
		Member member = memberService.getMemberByLoginId(loginId);
		
		if(member != null) {
			return new ResultData("F-1", "해당 로그인아이디는 이미 사용 중 입니다.", "loginId",loginId);
		}
		
		return new ResultData("S-1", "사용가능한 로그인 아이디 입니다.", "loginId",loginId);
	}
	// 회원정보 수정 페이지 이동
	@RequestMapping("/mpaUsr/member/modify")
	public String showModify(HttpServletRequest req, String checkPasswordAuthCode) {

		Member loginedMember = ((Rq) req.getAttribute("rq")).getLoginedMember();
		ResultData checkValidCheckPasswordAuthCodeResultData = memberService
				.checkValidCheckPasswordAuthCode(loginedMember.getId(), checkPasswordAuthCode);

		if (checkValidCheckPasswordAuthCodeResultData.isFail()) {
			return Util.msgAndBack(req, checkValidCheckPasswordAuthCodeResultData.getMsg());
		}

		return "mpaUsr/member/modify";
	}
	// 회원정보 수정
	@RequestMapping("/mpaUsr/member/doModify")
	public String doModify(HttpServletRequest req, String loginPw, String name, String nickname, String cellphoneNo,
			String email, String checkPasswordAuthCode, MultipartRequest multipartRequest) {

		Member loginedMember = ((Rq) req.getAttribute("rq")).getLoginedMember();
		ResultData checkValidCheckPasswordAuthCodeResultData = memberService
				.checkValidCheckPasswordAuthCode(loginedMember.getId(), checkPasswordAuthCode);

		if (checkValidCheckPasswordAuthCodeResultData.isFail()) {
			return Util.msgAndBack(req, checkValidCheckPasswordAuthCodeResultData.getMsg());
		}

		if (loginPw != null && loginPw.trim().length() == 0) {
			loginPw = null;
		}

		int id = ((Rq) req.getAttribute("rq")).getLoginedMemberId();
		ResultData modifyRd = memberService.modify(id, loginPw, name, nickname, cellphoneNo, email);

		if (modifyRd.isFail()) {
			return Util.msgAndBack(req, modifyRd.getMsg());
		}
		
		if ( req.getParameter("deleteFile__member__0__extra__profileImg__1") != null ) {
            genFileService.deleteGenFile("member", loginedMember.getId(), "extra", "profileImg", 1);
        }

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, loginedMember.getId());
			}
		}

		return Util.msgAndReplace(req, modifyRd.getMsg(), "/");
	}

	@RequestMapping("/mpaUsr/member/checkPassword")
	public String showCheckPassword(HttpServletRequest req) {
		return "mpaUsr/member/checkPassword";
	}

	@RequestMapping("/mpaUsr/member/doCheckPassword")
	public String doCheckPassword(HttpServletRequest req, String loginPw, String redirectUri) {
		Member loginedMember = ((Rq) req.getAttribute("rq")).getLoginedMember();

		if (loginedMember.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack(req, "비밀번호가 일치하지 않습니다.");
		}

		String authCode = memberService.genCheckPasswordAuthCode(loginedMember.getId());

		redirectUri = Util.getNewUri(redirectUri, "checkPasswordAuthCode", authCode);

		return Util.msgAndReplace(req, "", redirectUri);
	}
	// 비밀번호 찾기 페이지로 이동
	@RequestMapping("/mpaUsr/member/findLoginPw")
	public String showFindLoginPw(HttpServletRequest req) {
		return "/mpaUsr/member/findLoginPw";
	}
	// 비밀번호 첮기
	@RequestMapping("/mpaUsr/member/doFindLoginPw")
	public String doFindLogindPw(HttpServletRequest req, String loginId, String email, String name,
			String redirectUri) {

		if (Util.isEmpty(redirectUri)) {
			redirectUri = "/";
		}

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
		}

		if (member.getName().equals(name) == false) {
			return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
		}

		if (member.getEmail().equals(email) == false) {
			return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
		}

		ResultData notifyTempLoginPwByEmailRs = memberService.notifyTempLoginPwByEmail(member);

		return Util.msgAndReplace(req, notifyTempLoginPwByEmailRs.getMsg(), redirectUri);
	}
	//아이디 찾기 페이지로 이동
	@RequestMapping("/mpaUsr/member/findLoginId")
    public String showFindLoginId(HttpServletRequest req) {
        return "mpaUsr/member/findLoginId";
    }
	// 아이디 찾기
    @RequestMapping("/mpaUsr/member/doFindLoginId")
    public String doFindLoginId(HttpServletRequest req, String name, String email, String redirectUri) {
        if (Util.isEmpty(redirectUri)) {
            redirectUri = "/";
        }

        Member member = memberService.getMemberByNameAndEmail(name, email);

        if (member == null) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }
        if (member.getName().equals(name) == false) {
			return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
		}

		if (member.getEmail().equals(email) == false) {
			return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
		}

        return Util.msgAndBack(req, String.format("회원님의 아이디는 `%s` 입니다.", member.getLoginId()));
    }

	@RequestMapping("/mpaUsr/member/login")
	public String showLogin(HttpServletRequest req) {
		return "mpaUsr/member/login";
	}

	@RequestMapping("/mpaUsr/member/doLogout")
	public String doLogout(HttpServletRequest req, HttpSession session) {
		session.removeAttribute("loginedMemberId");

		String msg = "로그아웃 되었습니다.";
		return Util.msgAndReplace(req, msg, "/");
	}

	@RequestMapping("/mpaUsr/member/doLogin")
	public String doLogin(HttpServletRequest req, HttpSession session, String loginId, String loginPw,
			String redirectUri) {

		if (Util.isEmpty(redirectUri)) {
			redirectUri = "/";
		}

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return Util.msgAndBack(req, loginId + "(은)는 사용중 이거나 등록된 회원이 아닙니다.");
		}

		if (member.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack(req, "비밀번호가 일치하지 않습니다.");
		}

		session.setAttribute("loginedMemberId", member.getId());

		String msg = member.getNickname() + "님 환영합니다.";

		boolean needToChangePassword = memberService.needToChangePassword(member.getId());

		if (needToChangePassword) {
			msg = "현재 비밀번호를 사용한지" + memberService.getNeedToChangePasswordFreeDays() + "일이 지났습니다. 비밀번호를 변경해주세요.";
			redirectUri = "/mpaUsr/member/mypage";
		}

		boolean isUsingTempPassword = memberService.usingTempPassword(member.getId());

		if (isUsingTempPassword) {
			msg = "임시 비밀번호를 변경해주세요.";
			redirectUri = "/mpaUsr/member/mypage";
		}

		return Util.msgAndReplace(req, msg, redirectUri);
	}
	
	// 회원가입 페이지 이동
	@RequestMapping("/mpaUsr/member/join")
	public String showJoin(HttpServletRequest req) {
		return "mpaUsr/member/join";
	}
	// 회원가입 처리
	@RequestMapping("/mpaUsr/member/doJoin")
	public String doJoin(HttpServletRequest req, String loginId, String loginPw, String name, String nickname,
			String cellphoneNo, String email, MultipartRequest multipartRequest) {
		// 아이디 중복 체크
		Member oldMember = memberService.getMemberByLoginId(loginId);

		if (oldMember != null) {
			return Util.msgAndBack(req, loginId + "(은)는 이미 사용중인 아이디 입니다.");
		}
		// 회원가입 체크(이름,이메일)
		oldMember = memberService.getMemberByNameAndEmail(name, email);

        if (oldMember != null) {
            return Util.msgAndBack(req, String.format("%s님은 이미 %s 메일주소로 %s 에 가입하셨습니다.", name, email, oldMember.getRegDate()));
        }
		ResultData joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);
		
		if (joinRd.isFail()) {
			return Util.msgAndBack(req, joinRd.getMsg());
		}
		int newMemberId = (int) joinRd.getBody().get("id");
		// 프로필 이미지 설정
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, newMemberId);
			}
		}
		Rq rq = (Rq) req.getAttribute("rq");

		return Util.msgAndReplace(req, joinRd.getMsg(), rq.getLoginPageUri());
	}

}
