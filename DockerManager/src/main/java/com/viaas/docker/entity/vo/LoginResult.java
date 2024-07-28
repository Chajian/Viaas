package com.viaas.docker.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录接口返回信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    String avatar;
    String token;
    String userName;
    double balance;
}
