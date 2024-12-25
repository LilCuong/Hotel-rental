package com.n3fpoly.hotel_rental.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.n3fpoly.hotel_rental.common.utils.Session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    Session session = new Session();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        session.setHttpSession(request.getSession());
        String uri = request.getRequestURI();

        if (uri.startsWith("/dashboard/") && !uri.matches(".*\\.(css|js|png|jpg|jpeg|gif|woff|woff2|ttf|eot)$")) {
            if (!session.isDirector()) {
                response.sendRedirect(request.getContextPath() + "/auth/login/");
                return false; // Ngăn không cho yêu cầu tiếp tục đến controller
            }
        } else if (uri.startsWith("/client/") && !uri.matches(".*\\.(css|js|png|jpg|jpeg|gif|woff|woff2|ttf|eot)$")) {
            if (!session.isLoggedIn()) {
                response.sendRedirect(request.getContextPath() + "/auth/login/");
                return false; // Ngăn không cho yêu cầu tiếp tục đến controller
            }
        } else if (uri.startsWith("/auth/login/") || uri.startsWith("/auth/register/")) {
            if (session.isLoggedIn()) {
                response.sendRedirect(request.getContextPath() + "/client/");
                return false;
            }
        }
        return true; // Cho phép yêu cầu tiếp tục nếu không cần kiểm tra hoặc đã đăng nhập
    }
}
