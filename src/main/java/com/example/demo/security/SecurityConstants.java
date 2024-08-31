package com.example.demo.security;

import com.example.demo.SpringApplicationContext;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 360; // 360 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getTokenSecret();
    }

}
