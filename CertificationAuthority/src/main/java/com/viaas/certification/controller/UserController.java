package com.viaas.certification.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mysql.cj.protocol.a.authentication.Sha256PasswordPlugin;
import com.viaas.certification.entity.MyAuthentication;
import com.viaas.certification.entity.UserDTO;
import com.viaas.certification.entity.UserDetail;
import com.viaas.certification.service.JwtUtil;
import com.viaas.certification.service.UserService;
import com.viaas.certification.service.impl.AccountUserDetailService;
import com.viaas.commons.Constants;
import com.viaas.commons.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/ibs/api/verify")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AccountUserDetailService userDetailService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/login")
    public Result<Authentication> login(@RequestBody UserDTO info){
        //check userinfo from database
        UserDTO userDTO = userService.getUserByAccount(info.getAccount());
        if(ObjectUtils.isEmpty(userDTO)){

        }
        if(!info.getPwd().equals(userDTO.getPwd())){

        }
        UserDetails userDetails = userDetailService.loadUserByUsername(info.getAccount());
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(userDetails.getUsername(), userDetails.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        if(ObjectUtils.isEmpty(userDetails)){

        }
        String token = jwtUtil.generateToken(userDTO);

        OAuth2Token oAuth2Token = new OAuth2Token() {
            @Override
            public String getTokenValue() {
                return token;
            }
        };
        MyAuthentication myAuthentication = new MyAuthentication(oAuth2Token);
        myAuthentication.setDetails(userDetails);
        return Result.success(Constants.CODE_200,myAuthentication);
    }
}
