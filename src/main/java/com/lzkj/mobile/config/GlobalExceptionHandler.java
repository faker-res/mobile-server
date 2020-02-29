package com.lzkj.mobile.config;

import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.exception.ServiceException;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.vo.GlobeResponse;
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

    @ExceptionHandler(value = GlobeException.class)
    public GlobeResponse<String> myExceptionHandler(GlobeException exception) {
        //log.error("",exception);
        //封装数据,返回给前端
        GlobeResponse<String> response = new GlobeResponse<>();
        response.setMsg(exception.getMsg());
        response.setCode(SystemConstants.FAIL_CODE);
        return response;
    }


    /**
     * 所有异常报错
     *
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public GlobeResponse<String> exceptionHandler(Exception exception) {
        log.info("异常", exception);
        //封装数据,返回给前端
        GlobeResponse<String> response = new GlobeResponse<>();
        response.setMsg("系统繁忙,请稍后再试");
        response.setCode(SystemConstants.EXCEPTION_CODE);
        response.setData("");
        return response;
    }

    //validate框架效验异常
    @ExceptionHandler(BindException.class)
    public Response bindExceptionHandler(BindException e) {
        log.info("validate框架效验异常BindException:{}", e.getMessage());
        return Response.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.info("illegalArgumentExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.info("missingServletRequestParameterExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Response serviceExceptionHandler(ServiceException e){
        log.info("serviceExceptionHandler:{}", e.getMessage());
        return Response.fail(e.getMessage());
    }

}
