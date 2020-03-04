package com.lzkj.mobile.v2.util;

import com.lzkj.mobile.exception.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.misc.Regexp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 *  * @Description: TODO   效验post json数据入参请求参数效验通用方法
 *  * @CreateDate:  2020/2/28 14:22  
 */
@Slf4j
@Component
public class ValidateParamUtil {

    public <T> void validList(List<T> list){
        validList(list, null);
    }

    public <T> void validList(List<T> list, Class<? extends Default> group){
        for (T t : list) {
            valid(t, group);
        }
    }

    public <T> void valid(T t){
        valid(t, null);
    }

    public <T> void valid(T t, Class<? extends Default> group){
        try {
            Class<?> clz = t.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(t);
                validateField(field, value, group);
            }
        } catch (IllegalAccessException e) {
            log.info("效验入参参数出现错误:{}", e.getMessage());
        }
    }

    private void validateField(Field field, Object value, Class<? extends Default> group) {
        Annotation[] annotations = field.getAnnotations();
        //结合swagger注解，可以产生默认的错误提示
        ModelProperty modelProperty = getApiModelProperty(annotations);
        if(modelProperty.getValue() == null){
            modelProperty.setValue(field.getName());
        }
        for (Annotation annotation : annotations) {
            if(annotation instanceof NotNull){
                hanldleNotNull(annotation, value, group, modelProperty);
            } else if(annotation instanceof NotBlank){
                hanldleNotBlank(annotation, value, group, modelProperty);
            } else if(annotation instanceof Pattern){
                hanldlePattern(annotation, value, group, modelProperty);
            } else if(annotation instanceof Email){
                hanldleEmail(annotation, value, group, modelProperty);
            } else if(annotation instanceof Length){
                hanldleLength(annotation, value, group, modelProperty);
            }
        }
    }

    private ModelProperty getApiModelProperty(Annotation[] annotations) {
        ModelProperty modelProperty = new ModelProperty();
        for (Annotation annotation : annotations) {
            if(annotation instanceof ApiModelProperty){
                ApiModelProperty an = (ApiModelProperty) annotation;
                if(StringUtils.hasText(an.name())){
                    modelProperty.setName(an.name());
                }
                if(StringUtils.hasText(an.value())){
                    modelProperty.setValue(an.value());
                }
                if(StringUtils.hasText(an.allowableValues())){
                    modelProperty.setAllowableValues(Arrays.asList(an.allowableValues().split(",")));
                }
            }
        }
        return modelProperty;
    }

    private static void hanldleLength(Annotation annotation, Object value, Class<? extends Default> group, ModelProperty property) {
        if(value == null || "".equals(value.toString().trim())){
            return;
        }
        Length t = (Length) annotation;
        String msg;
        if(!isValidate(group, t.groups())){
            return;
        }
        if(value instanceof Integer || value instanceof BigDecimal){
            if(value instanceof Integer){
                if((Integer)value >= t.min() && (Integer)value  <= t.max()){
                    return;
                }
            }
            if(value instanceof BigDecimal){
                if(((BigDecimal) value).compareTo(new BigDecimal(t.min())) >= 0 && ((BigDecimal) value).compareTo(new BigDecimal(t.max())) <= 0){
                    return;
                }
            }
            if("{org.hibernate.validator.constraints.Length.message}".equals(t.message())){
                if(t.min() == t.max()){
                    msg = property.getValue() + "大小只能为" + t.min();
                } else {
                    msg = property.getValue() + "大小只能在" + t.min() + " ~ " + t.max() + "之间";
                }
            } else {
                msg = t.message();
            }
        } else {
            if(value.toString().length() >= t.min() && value.toString().length() <= t.max()){
                return;
            }
            if("{org.hibernate.validator.constraints.Length.message}".equals(t.message())){
                if(t.min() == t.max()){
                    msg = property.getValue() + "长度只能为" + t.min();
                } else {
                    msg = property.getValue() + "长度只能在" + t.min() + " ~ " + t.max() + "之间";
                }
            } else {
                msg = t.message();
            }
        }
        throw new ServiceException(msg);
    }

    private static void hanldleEmail(Annotation annotation, Object value, Class<? extends Default> group, ModelProperty property) {
        if(value == null || "".equals(value.toString().trim())){
            return;
        }
        Email t = (Email) annotation;
        if(!isValidate(group, t.groups())){
            return;
        }
        if(value.toString().matches(Regexp.EMAIL)){
            validateAllowValues(value, property);
        } else {
            if("{javax.validation.constraints.Email.message}".equals(t.message())){
                throw new ServiceException(Message.EMAIL);
            }
            throw new ServiceException(t.message());
        }
    }

    private static void hanldlePattern(Annotation annotation, Object value, Class<? extends Default> group, ModelProperty property) {
        if(value == null || "".equals(value.toString().trim())){
            return;
        }
        Pattern t = (Pattern) annotation;
        if(!isValidate(group, t.groups())){
            return;
        }
        if(value.toString().matches(t.regexp())){
            validateAllowValues(value, property);
        } else {
            if(Regexp.PHONE.equals(t.regexp())){
                throw new ServiceException(Message.PHONE);
            }
            if("{javax.validation.constraints.Pattern.message}".equals(t.message())){
                throw new ServiceException(property.getValue() + "格式不正确");
            }
            throw new ServiceException(t.message());
        }
    }

    private static void hanldleNotBlank(Annotation annotation, Object value, Class<? extends Default> group, ModelProperty property) {
        NotBlank t = (NotBlank) annotation;
        if(!isValidate(group, t.groups())){
            return;
        }
        if(value == null || "".equals(value.toString().trim())){
            if("{javax.validation.constraints.NotBlank.message}".equals(t.message())){
                throw new ServiceException(property.getValue() + "不能为空");
            }
            throw new ServiceException(t.message());
        } else {
            validateAllowValues(value, property);
        }
    }

    private static void hanldleNotNull(Annotation annotation, Object value, Class<? extends Default> group, ModelProperty property) {
        NotNull t = (NotNull) annotation;
        if(!isValidate(group, t.groups())){
            return;
        }
        if(value == null){
            if("{javax.validation.constraints.NotNull.message}".equals(t.message())){
                throw new ServiceException(property.getValue() + "不能为空");
            }
            throw new ServiceException(t.message());
        } else {
            validateAllowValues(value, property);
        }
    }

    /**
     * 效验swagger注解@ApiModelProperty对应的AllowableValues
     * @param value
     * @param property
     */
    private static void validateAllowValues(Object value, ModelProperty property) {
        if(property.getAllowableValues() == null){
            return;
        }
        if(property.getAllowableValues().contains(value.toString())){
            return;
        }
        throw new ServiceException(property.getValue() + "只能为" + property.getAllowableValues());
    }

    /**
     * 分组效验
     * @param group
     * @param groups
     * @return
     */
    private static boolean isValidate(Class<? extends Default> group, Class<?>[] groups) {
        boolean flag = false;
        if(group == null || groups == null || groups.length == 0){
            flag = true;
        } else {
            if (Arrays.asList(groups).contains(group)) {
                flag = true;
            }
        }
        return flag;
    }

    public static class Regexp{

        public final static String PHONE = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
        public final static String EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        public final static String NUMBER = "^[1-9][0-9]*$";//大于0的正整数，效验主键ID
    }

    public static class Message{

        public final static String PHONE = "手机号格式不正确";
        public final static String EMAIL = "邮箱格式不正确";

    }
    
    public interface Insert extends Default{}
    
    public interface update extends Default{}

    /**
     * swagger注解@ApiModelProperty对应属性
     */
    @Data
    public class ModelProperty{

        private String name;

        private String value;

        private List<String> allowableValues;

    }

    /**
     * 关联效验List对象，如银行卡4要素只要有一个数据不为空则需要所有的必填
     * @param list
     * @param <T>
     */
    public <T> void validRelationList(List<T> list, String... excludeField){
        for (T t : list) {
            validRelation(t, excludeField);
        }
    }

    /**
     * 关联效验对象，如银行卡4要素只要有一个数据不为空则需要所有的必填
     * @param t
     * @param <T>
     */
    public <T> boolean validRelation(T t, String... excludeField){
        boolean hasNotNull = false;
        try {
            Class<?> clz = t.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                //排除自定义常量对象和序列号
                if(AssertUtils.testAllUpperCase(field.getName()) || "serialVersionUID".equals(field.getName())){
                    continue;
                }
                if(excludeField != null && Arrays.asList(excludeField).contains(field.getName())){
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(t);
                if(value != null && !"".equals(value.toString().trim())){
                    hasNotNull = true;
                    break;
                }
            }
            if(hasNotNull){
                valid(t);
            }
        } catch (IllegalAccessException e) {
            log.info("关联效验入参参数出现错误:{}", e.getMessage());
        }
        return hasNotNull;
    }

}
