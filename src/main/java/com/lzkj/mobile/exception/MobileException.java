package com.lzkj.mobile.exception;

import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.vo.GlobeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author robert
 *  异常补捉
 */
@RestControllerAdvice
@Slf4j
public class MobileException {
    /**
     * 所有异常报错
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value= GlobeException.class)
    public GlobeResponse<String> myExceptionHandler(GlobeException exception)
    {
        //log.error("",exception);
        //封装数据,返回给前端
        GlobeResponse<String> response = new GlobeResponse<>();
        response.setMsg(exception.getMsg());
        response.setCode(SystemConstants.FAIL_CODE);
        return response;
    }


    /**
     * 所有异常报错
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value= Exception.class)
    public GlobeResponse<String> exceptionHandler(Exception exception)
    {
        log.info("异常", exception);
        //封装数据,返回给前端
        GlobeResponse<String> response = new GlobeResponse<>();
        response.setMsg("系统繁忙,请稍后再试");
        response.setCode(SystemConstants.EXCEPTION_CODE);
        response.setData("");
        return response;
    }
}
