package com.lzkj.mobile.util.bean;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.util.bean 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/1/23 10:46  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@FunctionalInterface
public interface BeanCopyCallBack<S, T> {

    void callback(S source, T target);


}
