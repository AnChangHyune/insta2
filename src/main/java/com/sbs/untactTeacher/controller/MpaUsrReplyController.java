package com.sbs.untactTeacher.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untactTeacher.dto.Article;
import com.sbs.untactTeacher.dto.Reply;
import com.sbs.untactTeacher.dto.ResultData;
import com.sbs.untactTeacher.dto.Rq;
import com.sbs.untactTeacher.service.ArticleService;
import com.sbs.untactTeacher.service.ReplyService;
import com.sbs.untactTeacher.util.Util;

@Controller
public class MpaUsrReplyController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ReplyService replyService;
	
	
	  @RequestMapping("/mpaUsr/reply/doDeleteAjax")
	    @ResponseBody
	    public ResultData doDeleteAjax(HttpServletRequest req, int id, String redirectUri) {
	        Reply reply = replyService.getReplyById(id);

	        if ( reply == null ) {
	            return new ResultData("F-1", "존재하지 않는 댓글입니다.");
	        }

	        Rq rq = (Rq)req.getAttribute("rq");

	        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
	            return new ResultData("F-1", "권한이 없습니다.");
	        }

	        ResultData deleteResultData = replyService.delete(id);

	        return new ResultData("S-1", String.format("%d번 댓글이 삭제되었습니다.", id));
	    }

	    @RequestMapping("/mpaUsr/reply/doDelete")
	    public String doDelete(HttpServletRequest req, int id, String redirectUri) {
	        Reply reply = replyService.getReplyById(id);

	        if ( reply == null ) {
	            return Util.msgAndBack(req, "존재하지 않는 댓글입니다.");
	        }

	        Rq rq = (Rq)req.getAttribute("rq");

	        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
	            return Util.msgAndBack(req, "권한이 없습니다.");
	        }

	        ResultData deleteResultData = replyService.delete(id);

	        return Util.msgAndReplace(req, deleteResultData.getMsg(), redirectUri);
	    }

	    @RequestMapping("/mpaUsr/reply/doWrite")
	    public String doWrite(HttpServletRequest req, String relTypeCode, int relId, String body, String redirectUri) {
	        switch ( relTypeCode ) {
	            case "article":
	                Article article = articleService.getArticleById(relId);
	                if ( article == null ) {
	                    return Util.msgAndBack(req, "해당 게시물이 존재하지 않습니다.");
	                }
	                break;
	            default:
	                return Util.msgAndBack(req, "올바르지 않은 relTypeCode 입니다.");
	        }

	        Rq rq = (Rq)req.getAttribute("rq");

	        int memberId = rq.getLoginedMemberId();

	        ResultData writeResultData = replyService.write(relTypeCode, relId, memberId, body);

	        int newReplyId = (int)writeResultData.getBody().get("id");

	        redirectUri = Util.getNewUri(redirectUri, "focusReplyId", newReplyId + "");

	        return Util.msgAndReplace(req, writeResultData.getMsg(), redirectUri);
	    }
}