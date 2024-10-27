package com.viaas.certification.api.handler;

import com.alibaba.fastjson2.JSON;
import com.viaas.certification.api.entity.FormAuthenticationToken;
import com.viaas.certification.api.util.JwtUtil;
import com.viaas.commons.Constants;
import com.viaas.commons.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;

public class FormAuthenticationHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(ObjectUtils.isEmpty(authentication)){

        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("account",authentication.getName());
        map.put("principal",authentication.getPrincipal());

        String token = jwtUtil.generateToken(map);
//        Result result = Result.success(Constants.CODE_200,token);
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().println(JSON.toJSONString(result));
    }
}
