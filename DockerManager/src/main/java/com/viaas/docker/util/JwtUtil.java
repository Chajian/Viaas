package com.viaas.docker.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

/**
 * Jwt工具
 * @author Chajian
 *
 * 主要作用：生成令牌
 *
 */
public class JwtUtil {

    /**
     * token 验证码存在时间
     * 10分钟
     * */
    private static final long EXPIRE_TIME = 100*600*10000;
    /**密钥*/
    private static final String SECRET = "SHIRO+JWT+DOCKER+CS";

    /**
     * 通过account来生成token字符串
     * @param account 账号
     * @return
     */
    public static String sign(String account,int id){
        Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);

        Algorithm algorithm = Algorithm.HMAC256(account+SECRET);
        HashMap<String,Object> header = new HashMap<>(2);
        header.put("typ","JWT");
        header.put("alg","HS256");
        return JWT.create()
                .withHeader(header)
                .withClaim("account",account)
                .withClaim("id",id)
                .withExpiresAt(date).sign(algorithm);
    }

    /**
     * 通过token和用户账号来验证请求
     * @param token token值
     * @return 验证成功返回true，否则返回false
     */
    public static boolean verity(String token){
        String account = JwtUtil.getUserAccount(token);
        int id = JwtUtil.getUserId(token);
        try {
            Algorithm algorithm = Algorithm.HMAC256(account+SECRET);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .withClaim("account",account)
                    .withClaim("id",id)
                    .build();
            verifier.verify(token);
            return true;
        }
        catch (IllegalArgumentException e){
            return false;
        }
        catch (JWTVerificationException e){
            return false;
        }
    }

    /**
     * 获得token中的用户名
     * @param token token
     * @return
     */
    public static String getUserAccount(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        }
        catch (JWTDecodeException e){
            return "";
        }
    }

    /**
     * 获得token中的用户名
     * @param token token
     * @return
     */
    public static int getUserId(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        }
        catch (JWTDecodeException e){
            return -1;
        }
    }


}
