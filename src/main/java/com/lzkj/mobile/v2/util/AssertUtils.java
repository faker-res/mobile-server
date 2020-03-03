package com.lzkj.mobile.v2.util;

import com.lzkj.mobile.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

/**
 *  * @Description: TODO   
 *  * @CreateDate:  2020/2/29 12:20  
 */
@Slf4j
public class AssertUtils {

    public static <T> void notAllNull(T t){
        notAllNull(t, null);
    }

    public static <T> void notAllNull(T t, String msg) {
        Assert.notNull(t, "入参对象不能为空");
        try {
            Class<?> clz = t.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                if(testAllUpperCase(field.getName())){
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(t);
                if (value != null) {
                    return;
                }
            }
            if (msg == null) {
                throw new ServiceException("入参对象" + clz.getName() + "属性不能全部为空");
            } else {
                throw new ServiceException(msg);
            }
        } catch (IllegalAccessException e) {
            log.info("效验入参参数出现错误:{}", e.getMessage());
        }
    }

    /**
     * 效验字符串是否是常量属性名(全大写)
     * @param str
     * @return
     */
    public static boolean testAllUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 97 && c <= 122) {
                return false;
            }
        }
        return true;
    }

}
