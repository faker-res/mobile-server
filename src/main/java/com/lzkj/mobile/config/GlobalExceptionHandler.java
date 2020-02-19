package com.lzkj.mobile.config;

import com.lzkj.mobile.v2.common.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *    
 *  *  
 *  * @Project: shiyi 
 *  * @Package: com.rg.shiyi.db.config 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2019年12月06日16:18   
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //validate框架效验异常
    @ExceptionHandler(BindException.class)
    public Response bindExceptionHandler(BindException e){
        log.info("validate框架效验异常BindException:{}", e.getMessage());
        return Response.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response illegalArgumentExceptionHandler(IllegalArgumentException e){
        log.info("illegalArgumentExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e){
        log.info("missingServletRequestParameterExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }


}
