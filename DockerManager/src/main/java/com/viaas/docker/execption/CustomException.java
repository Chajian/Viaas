package com.viaas.docker.execption;

import com.viaas.docker.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException implements Serializable {
    private Integer code;  //状态编码

    private String message; //异常消息

    public CustomException(Constants result){
        this.code = result.getCode();
        this.message = result.getMsg();
    }
}
