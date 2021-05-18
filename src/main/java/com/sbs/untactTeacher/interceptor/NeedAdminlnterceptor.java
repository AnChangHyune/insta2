package com.sbs.untactTeacher.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sbs.untactTeacher.dto.Rq;
import com.sbs.untactTeacher.util.Util;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NeedAdminlnterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        Rq rq = (Rq) req.getAttribute("rq");

        if (rq.isNotAdmin()) {
            String resultCode = "F-C";
            String resultMsg = "관리자만 이용할 수 있습니다.";

            if ( rq.isAjax() ) {
                resp.setContentType("application/json; charset=UTF-8");
                resp.getWriter().append("{\"resultCode\":\"" + resultCode + "\",\"msg\":\"" + resultMsg + "\"}");
            }
            else {
                resp.setContentType("text/html; charset=UTF-8");
                String afterLoginUri = rq.getEncodedCurrentUri();
                resp.getWriter().append(Util.msgAndReplace(resultMsg, "../member/login?afterLoginUri=" + afterLoginUri));
            }

            return false;
        }

        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }
}
