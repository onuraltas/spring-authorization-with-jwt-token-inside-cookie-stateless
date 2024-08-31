package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.SpringApplicationContext;
import com.example.demo.bean.RequestScopedBean;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {

        try {
            LoginRequest credential = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.email(),
                            credential.password(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String username = userDetails.getUsername();

        RequestScopedBean requestScopedBean = (RequestScopedBean) SpringApplicationContext.getBean("requestScopedBean");

        UserService userService = (UserService) SpringApplicationContext.getBean("userService");

        User user = userService.getUserByEmail(username);
        requestScopedBean.setUser(user);

        if (userDetails.isRememberMe()) {

            SecretKeySpec secretKeySpec = new SecretKeySpec(SecurityConstants.getTokenSecret().getBytes(),
                    "HmacSHA512");

            String token = Jwts.builder().subject(user.email())
                    .expiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(secretKeySpec).compact();

            // create a cookie
            Cookie cookie = new Cookie("rememberMeCookie", token);
            cookie.setMaxAge(30 * 24 * 60 * 60); // expires in 30 days
            // cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/"); // global cookie accessible every where

            // add cookie to response
            res.addCookie(cookie);

        }

    }

}
