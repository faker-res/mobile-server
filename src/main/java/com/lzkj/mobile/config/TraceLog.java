package com.lzkj.mobile.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.lzkj.mobile.v2.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Aspect
@Component
public class TraceLog {

    //需要脱敏的字段
    private static final Set<String> SPECIAL = new HashSet();

    {
        SPECIAL.add("password");
        SPECIAL.add("oldPassword");
        SPECIAL.add("ackPassword");
    }

    @Pointcut("execution(* com.lzkj.mobile.v2.controller.*.*(..))")
    public void traceLogAspect() {
    }

    @Before("traceLogAspect()")
    public void deBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> maps = getParamMap(request);
        String classMethodName =
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        maps.put("URL", request.getRequestURL().toString());
        maps.put("HTTP_METHOD", request.getMethod());
        maps.put("IP", IPUtils.getIp(request));
        log.info("\n 执行方法：[{}] \n 请求参数：{}", classMethodName, JSON.toJSONString(maps));
    }

    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> maps = new LinkedHashMap<>();
        try {
            Map parameterMap = getRequestPostStr(request);
            for (Object key : parameterMap.keySet()) {
                String paraName = String.valueOf(key);
                String parameter = String.valueOf(parameterMap.get(key));
                if (SPECIAL.contains(paraName)) {
                    parameter = parameter.substring(0, 1) + "***" + parameter.substring(parameter.length() - 1);
                }
                maps.put(paraName, parameter);
            }
        } catch (IOException e) {
            log.info("deBefore获取入参参数失败：{}", e.getMessage());
        }
        return maps;
    }

    @AfterReturning(returning = "returnValue", pointcut = "traceLogAspect()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        log.info("\n 执行方法：[{}] \n 返回结果：{}",
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                JSON.toJSONString(returnValue));
    }

    @AfterThrowing(value = "traceLogAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.info("\n 执行方法：[{}] \n 异常信息返回：{}",
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                e.getMessage());
    }

    private static Map getRequestPostStr(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return Maps.newHashMap();
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        String s = new String(buffer, charEncoding);
        return JSON.parseObject(s, Map.class);
    }


}
