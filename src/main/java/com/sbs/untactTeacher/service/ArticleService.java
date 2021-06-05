package com.sbs.untactTeacher.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untactTeacher.dao.ArticleDao;
import com.sbs.untactTeacher.dao.ReplyDao;
import com.sbs.untactTeacher.dto.Article;
import com.sbs.untactTeacher.dto.Board;
import com.sbs.untactTeacher.dto.Reply;
import com.sbs.untactTeacher.dto.ResultData;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private ReplyDao replyDao;
	// 게시물 수정
	public ResultData modifyArticle(int id, String title, String body) {
		Article article = getArticleById(id);

		if (isEmpty(article)) {
			return new ResultData("F-1", "존재하지 않는 게시물 번호입니다.", "id", id);
		}

		articleDao.modifyArticle(id, title, body);

		return new ResultData("S-1", "게시물이 수정되었습니다.", "id", id);
	}

	private boolean isEmpty(Article article) {
		if (article == null) {
			return true;
		} else if (article.isDelStatus()) {
			return true;
		}

		return false;
	}

	public ResultData deleteArticleById(int id) {
		Article article = getArticleById(id);

		if (isEmpty(article)) {
			return new ResultData("F-1", "게시물이 존재하지 않습니다.", "id", id);
		}

		articleDao.deleteArticleById(id);

		return new ResultData("S-1", id + "번 게시물이 삭제되었습니다.", "id", id, "boardId", article.getBoardId());
	}
	// 게시물 작성
	public ResultData writeArticle(int boardId, int memberId, String title, String body) {
		
		articleDao.writeArticle(boardId, memberId, title, body);
		int id = articleDao.getLastInsertId();

		return new ResultData("S-1", "게시물이 작성되었습니다.", "id", id);
	}

	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}
	// 게시판 번호
	public Board getBoardById(int id) {
		return articleDao.getBoardById(id);
	}
	// 게시물 총 페이지(검색/페이지)
	public int getArticlesTotalCount(int boardId, String searchKeywordTypeCode, String searchKeyword) {
		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		return articleDao.getArticlesTotalCount(boardId, searchKeywordTypeCode, searchKeyword);
	}
	// 게시물 리스트(검색/페이지)
	public List<Article> getForPrintArticles(int boardId, String searchKeywordTypeCode, String searchKeyword,
			int itemsCountInAPage, int page) {
		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		int limitFrom = (page - 1) * itemsCountInAPage;
		int limitTake = itemsCountInAPage;

		return articleDao.getForPrintArticles(boardId, searchKeywordTypeCode, searchKeyword, limitFrom, limitTake);
	}
	// 게시물 번호
	public Article getForPrintArticleById(int id) {
		return articleDao.getForPrintArticleById(id);
	}

}
