package com.viaas.docker.entity.dto;

import lombok.Data;
import lombok.ToString;

/**
*@descript 用户传输对象
*@author chen
*@date 2023/3/2 15:50
*@version 1.0
*/
@Data
@ToString
public class UserDto {
    private String account;
    private String pwd;
}
