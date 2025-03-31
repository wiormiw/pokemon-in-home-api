package com.wiormiw.pokemon_in_home.security.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 14_400_000; // 4 hour
    public static final long REFRESH_EXPIRATION_TIME = 86_400_000; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ISSUER = "SilverMaiden";
    public static final String AUDIENCE = "Pokémon Player";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid authentication token";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "**" };
}
