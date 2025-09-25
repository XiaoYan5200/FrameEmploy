package com.framecommon.framecommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 乔乾彭
 * @desc
 * @date 2025/5/26
 */
public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    private static final String SECRET = "y6plziidr4a5prcw6zupgyihrd6o0id2as1238dfda1dafmbm";
    private static final String TOKEN_PREFIX = "Bearer ";

    private TokenUtil() {}

    /**
     * 检测 token 的合法性
     *
     * @param req HTTP 请求
     * @return 如果 token 非法返回 true，否则返回 false
     */
    public boolean isFailed(HttpServletRequest req) {
        try {
            String token = getTokenFromRequest(req);
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return false;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 检测 token 的合法性
     *
     * @param token JWT Token
     * @return 如果 token 非法返回 true，否则返回 false
     */
    public static boolean isFailed(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return false;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 创建 token
     *
     * @param id   用户 ID
     * @param name 用户名称
     * @return 生成的 JWT Token
     * @throws NoSuchAlgorithmException 如果指定的算法不可用
     */
    public static String createToken(String id, String name) throws NoSuchAlgorithmException {
        Map<String, Object> header = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        // 添加头
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        // 添加负载
        params.put("id", id);
        params.put("name", name);

        // 生成 token 内容，设定两周内有效
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(params)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        return token;
    }

    /**
     * 从 token 中获取用户 ID
     * @return 用户 ID
     */
    public static String getId(HttpServletRequest req) {
        String token =  req.getHeader("Authorization").substring("Bearer ".length());
        Claims body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return(String) body.get("id");
    }

    /**
     * 从 token 中获取用户 ID
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public static String getId(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return (String) body.get("id");
        } catch (Exception e) {
            logger.error("Failed to get user ID from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从 token 中获取用户名称
     *
     * @param req HTTP 请求
     * @return 用户名称
     */
    public static String getName(HttpServletRequest req) {
        try {
            String token = getTokenFromRequest(req);
            Claims body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return (String) body.get("name");
        } catch (Exception e) {
            logger.error("Failed to get user name from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从 token 中获取用户名称
     *
     * @param token JWT Token
     * @return 用户名称
     */
    public static String getName(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return (String) body.get("name");
        } catch (Exception e) {
            logger.error("Failed to get user name from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从请求中提取 Token
     *
     * @param req HTTP 请求
     * @return 提取的 Token
     */
    private static String getTokenFromRequest(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            logger.info("Bearer token found in request.");
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
