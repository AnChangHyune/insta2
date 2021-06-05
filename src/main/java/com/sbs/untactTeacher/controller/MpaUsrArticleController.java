package com.sbs.untactTeacher.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.sbs.untactTeacher.dto.Article;
import com.sbs.untactTeacher.dto.Board;
import com.sbs.untactTeacher.dto.GenFile;
import com.sbs.untactTeacher.dto.Reply;
import com.sbs.untactTeacher.dto.ResultData;
import com.sbs.untactTeacher.dto.Rq;
import com.sbs.untactTeacher.service.ArticleService;
import com.sbs.untactTeacher.service.GenFileService;
import com.sbs.untactTeacher.service.ReplyService;
import com.sbs.untactTeacher.util.Util;

@Controller
public class MpaUsrArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private GenFileService genFileService;
	// 게시물 상세보기
	@RequestMapping("/mpaUsr/article/detail")
	public String showDetail(HttpServletRequest req, int id, String body) {
		Article article = articleService.getForPrintArticleById(id);

		List<Reply> replies = replyService.getForPrintRepliesByRelTypeCodeAndRelId("article", id);
		if (article == null) {
			return Util.msgAndBack(req, id + "번 게시물은 존재하지 않습니다.");
		}

		Board board = articleService.getBoardById(article.getBoardId());

		req.setAttribute("replies", replies);
		req.setAttribute("board", board);
		req.setAttribute("article", article);

		return "mpaUsr/article/detail";
	}
	// JSP연결 게시물 작성 페이지로 이동
	@RequestMapping("/mpaUsr/article/write")
	public String showWrite(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId) {
		Board board = articleService.getBoardById(boardId);

		if (board == null) {
			return Util.msgAndBack(req, boardId + "번 게시물은 존재하지 않습니다.");
		}

		req.setAttribute("board", board);

		return "mpaUsr/article/write";
	}
	// 게시물 작성
	@RequestMapping("/mpaUsr/article/doWrite")
	public String doWrite(HttpServletRequest req, int boardId, String title, String body,MultipartRequest multipartRequest) {

		if (Util.isEmpty(title)) {
			return Util.msgAndBack(req, "제목을 입력해주세요.");
		}

		if (Util.isEmpty(body)) {
			return Util.msgAndBack(req, "내용을 입력해주세요.");
		}

		Rq rq = (Rq) req.getAttribute("rq");

		int memberId = rq.getLoginedMemberId();

		ResultData WriteRd = articleService.writeArticle(boardId, memberId, title, body);

		if (WriteRd.isFail()) {
			return Util.msgAndBack(req, WriteRd.getMsg());
		}

		int newArticleId = (int) WriteRd.getBody().get("id");

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, newArticleId);
			}
		}

		String replaceUri = "detail?id=" + WriteRd.getBody().get("id");

		return Util.msgAndReplace(req, WriteRd.getMsg(), replaceUri);
	}
	// JSP 연결 게시물 수정 페이지로 이동
	@RequestMapping("/mpaUsr/article/modify")
	public String showModify(Integer id, HttpServletRequest req) {
		if (id == null) {
			return Util.msgAndBack(req, "id를 입력해주세요.");
		}

		Article article = articleService.getForPrintArticleById(id);

		List<GenFile> files = genFileService.getGenFiles("article", article.getId(), "common", "attachment");

		Map<String, GenFile> filesMap = new HashMap<>();

		for (GenFile file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}

		article.getExtraNotNull().put("file__common__attachment", filesMap);
		
		req.setAttribute("article", article);

		return "mpaUsr/article/modify";
	}
	// 게시물 수정
	@RequestMapping("/mpaUsr/article/doModify")
	public String doModify(Integer id, String title, String body, HttpServletRequest req) {
		
		if (Util.isEmpty(id)) {
			return Util.msgAndBack(req, "번호를 입력해주세요.");
		}

		if (Util.isEmpty(title)) {
			return Util.msgAndBack(req, "제목을 입력해주세요.");
		}

		if (Util.isEmpty(body)) {
			return Util.msgAndBack(req, "내용을 입력해주세요.");
		}

		Article article = articleService.getArticleById(id);

		if (article == null) {
			return Util.msgAndBack(req, "존재하지 않는 게시물 번호입니다.");
		}
		
		ResultData modifyRs = articleService.modifyArticle(id, title, body);
		
		if(modifyRs.isFail()) {
			return Util.msgAndBack(req, modifyRs.getMsg());
		}
		
		String redirectUri = "../article/detail?id=" + modifyRs.getBody().get("id");
		

		return Util.msgAndReplace(req, modifyRs.getMsg(), redirectUri);
	}

    @RequestMapping("/mpaUsr/article/doDelete")
    public String doDelete(HttpServletRequest req, int id, String redirectUri) {
    	Article article = articleService.getArticleById(id);

        if ( article == null ) {
            return Util.msgAndBack(req, "존재하지 않는 댓글입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");

        if ( article.getMemberId() != rq.getLoginedMemberId() ) {
            return Util.msgAndBack(req, "권한이 없습니다.");
        }

        ResultData deleteResultData = articleService.deleteArticleById(id);
        
        if(deleteResultData.isFail()) {
        	return Util.msgAndBack(req, deleteResultData.getMsg());
        }
        
        redirectUri = "../article/list?boardId="+deleteResultData.getBody().get("boardId");

        return Util.msgAndReplace(req, deleteResultData.getMsg(), redirectUri);
    }
	// 게시물 리스트
	@RequestMapping("/mpaUsr/article/list")
	public String showList(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId,
			String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		Board board = articleService.getBoardById(boardId);

		if (Util.isEmpty(searchKeywordType)) {
			searchKeywordType = "titleAndBody";
		}

		if (board == null) {
			return Util.msgAndBack(req, boardId + "번 게시판이 존재하지 않습니다.");
		}

		req.setAttribute("board", board);

		int totalItemsCount = articleService.getArticlesTotalCount(boardId, searchKeywordType, searchKeyword);

		if (searchKeyword == null || searchKeyword.trim().length() == 0) {

		}

		req.setAttribute("totalItemsCount", totalItemsCount);

		// 한 페이지에 보여줄 수 있는 게시물 최대 개수
		int itemsCountInAPage = 20;
		// 총 페이지 수
		int totalPage = (int) Math.ceil(totalItemsCount / (double) itemsCountInAPage);

		// 현재 페이지(임시)
		req.setAttribute("page", page);
		req.setAttribute("totalPage", totalPage);

		List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword,
				itemsCountInAPage, page);

		req.setAttribute("articles", articles);

		return "mpaUsr/article/list";
	}

	@RequestMapping("/mpaUsr/article/getArticle")
	@ResponseBody
	public ResultData getArticle(Integer id) {
		if (Util.isEmpty(id)) {
			return new ResultData("F-1", "번호를 입력해주세요.");
		}

		Article article = articleService.getArticleById(id);

		if (article == null) {
			return new ResultData("F-1", id + "번 글은 존재하지 않습니다.", "id", id);
		}

		return new ResultData("S-1", article.getId() + "번 글 입니다.", "article", article);
	}
}
