package com.viaas.docker.execption;

import com.alibaba.fastjson.JSON;
import com.github.dockerjava.api.exception.ConflictException;
import com.viaas.docker.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 全局处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExecptionHandler {

    @ExceptionHandler(CustomException.class)
    public Result<String> customException(CustomException customException) {

        return Result.error(customException.getCode(), customException.getMessage());

    }

    /**
     * 容器存在 反复启动异常
     * @param conflictException
     * @return
     */
    @ExceptionHandler(ConflictException.class)
    public Result<String> conflictException(ConflictException conflictException){
        String message = conflictException.getMessage();
        System.out.println(message);
        Map map = JSON.parseObject(message, Map.class);
        map.forEach((k,v)->System.out.println(k+":"+v));
        int httpStatus = conflictException.getHttpStatus();
        return Result.error(Integer.valueOf(httpStatus),message);
    }
}
