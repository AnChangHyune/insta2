package com.sbs.untactTeacher.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
	private int id;
	private String regDate;
	private String updateDate;
	private int boardId;
	private int memberId;
	private String title;
	private String body;
	private boolean blindStatus;
    private String blindDate;
    private boolean delStatus;
    private String delDate;
    private int hitCount;
    private int repliesCount;
    private int likeCount;
    private int dislikeCount;
    
    private Map<String, Object> extra;
    
    private String extra__writerName;
    
    public String getBodyForPrint() {
    	String bodyForPrint = body.replaceAll("\r\n", "<br>");
    	bodyForPrint = bodyForPrint.replaceAll("\r", "<br>");
    	bodyForPrint = bodyForPrint.replaceAll("\n", "<br>");
    	
    	return bodyForPrint;
    }

	public Map<String, Object> getExtraNotNull() {
		if ( extra == null ) {
			extra = new HashMap<String, Object>();
		}
		
		return extra;
	}
	
    public String getWriterProfileImgUri() {
        return "/common/genFile/file/member/" + memberId + "/extra/profileImg/1";
    }

    public String getWriterProfileFallbackImgUri() {
        return "https://blog.kakaocdn.net/dn/cyOIpg/btqx7JTDRTq/1fs7MnKMK7nSbrM9QTIbE1/img.jpg";
    }

    public String getWriterProfileFallbackImgOnErrorHtmlAttr() {
        return "this.src = '" + getWriterProfileFallbackImgUri() + "'";
    }
     
    //=================================================
    
    public String getWriterfileImgUri() {
        return "/common/genFile/file/article/" + id + "/extra/profileImg/1";
    }

    public String getWriterfileFallbackImgUri() {
        return "";
    }

    public String getWriterfileFallbackImgOnErrorHtmlAttr() {
        return "this.src = '" + getWriterfileFallbackImgUri() + "'";
    }
}