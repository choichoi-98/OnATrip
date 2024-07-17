package com.naver.OnATrip.securingWeb;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // TODO Auto-generated method stub

        logger.error("Access Denied Handler");

        String url = "error.html";

        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        request.setAttribute("key", "접근 권한이 없는 사용자입니다.");
        dispatcher.forward(request, response);
    }
}
