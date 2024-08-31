package com.example.demo.security;

import io.jsonwebtoken.Jwts;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.SpringApplicationContext;
import com.example.demo.bean.RequestScopedBean;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import javax.crypto.spec.SecretKeySpec;

public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

    public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        RequestScopedBean requestScopedBean = (RequestScopedBean) SpringApplicationContext.getBean("requestScopedBean");

        if (request.getCookies() != null) {

            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("rememberMeCookie")) {
                    SecretKeySpec secretKeySpec = new SecretKeySpec(SecurityConstants.getTokenSecret().getBytes(),
                            "HmacSHA512");

                    try {
                        String username = Jwts.parser().verifyWith(secretKeySpec).build()
                                .parseSignedClaims(cookie.getValue())
                                .getPayload().getSubject();

                        UserService userService = (UserService) SpringApplicationContext.getBean("userService");

                        User user = userService.getUserByEmail(username);
                        requestScopedBean.setUser(user);
                    } catch (io.jsonwebtoken.ExpiredJwtException e) {
                        Cookie logoutCookie = new Cookie("rememberMeCookie", "");
                        logoutCookie.setMaxAge(0);
                        response.addCookie(logoutCookie);
                    }
                }
            }

        }

        chain.doFilter(request, response);
    }

}
