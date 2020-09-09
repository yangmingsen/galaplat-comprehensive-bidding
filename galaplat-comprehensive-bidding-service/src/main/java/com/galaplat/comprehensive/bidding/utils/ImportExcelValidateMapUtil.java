package com.galaplat.comprehensive.bidding.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 导入excel，校验Map，并得到对象
 * @Author: weiyuxuan
 * @CreateDate: 2020/8/25 17:31
 */
public class ImportExcelValidateMapUtil {

    private static final Logger log = LoggerFactory.getLogger(ImportExcelValidateMapUtil.class);

    /**
     * 校验导入数据,返回有异常的行
     * @param referenceParamClazz
     * @param excelParam
     * @param excelDataMap
     * @return
     */
    public static  <K> Tuple<String,K> validateField_1(Class referenceParamClazz,K excelParam, Map<String, Object> excelDataMap) {
        Field[] fileds = referenceParamClazz.getDeclaredFields();
        StringBuffer errorMsg = new StringBuffer("");

        for (Field field : fileds) {
            AlisaField alisaAnnotation = field.getAnnotation(AlisaField.class);
            NotNull notNullAnnotation = field.getAnnotation(NotNull.class);
            DateFieldFormate  dateFieldFormateAnnotation = field.getAnnotation(DateFieldFormate.class);

            String  fieldName  = field.getName();
            String  excelTitle;
            StringBuffer  temErrorMsg = new StringBuffer("") ;

            if (alisaAnnotation != null) {
                excelTitle = alisaAnnotation.value();
                Class fieldClass = field.getType();
                Class superclass = field.getType().getSuperclass();
                Class clazz = field.getType();
                String  fieldValue  = (String) excelDataMap.get(fieldName);

                if (notNullAnnotation != null && StringUtils.isEmpty(fieldValue)) {
                    temErrorMsg.append(notNullAnnotation.message());
                    continue;
                }

                if (superclass  == Number.class && (clazz == int.class
                        || clazz == short.class || clazz == long.class
                        || clazz == Integer.class || clazz == Short.class
                        || clazz == Long.class)) {

                    String firstNum = fieldValue.substring(0,1);
                    fieldValue = StringUtils.equals(firstNum,"-") ? fieldValue.substring(0,fieldValue.length()) : fieldValue;
                    String tempFieldValue = StringUtils.replaceFirst(fieldValue,"\\.","");
                    if (!StringUtils.isNumeric(tempFieldValue)) {
                        temErrorMsg.append(excelTitle).append("必须为数字！");
                    } else {
                        if (fieldValue.contains(".")) { temErrorMsg.append(excelTitle).append("必须为整数！");}
                    }
                }

                if (superclass  == Number.class && (clazz == double.class || clazz == float.class
                        ||  clazz == Double.class || clazz == Float.class || clazz == BigDecimal.class)) {
                    fieldValue = StringUtils.replaceFirst(fieldValue,"\\.","");
                    if (!StringUtils.isNumeric(fieldValue)) {
                        temErrorMsg.append(excelTitle).append("必须为数字！");
                    }
                }

                try {
                    String methodName = "set"+ upperFirstWordString(fieldName);
                    Method method = excelParam.getClass().getDeclaredMethod(methodName,String.class);
                    Object fieldVal = excelDataMap.get(fieldName);
                    method.invoke(excelParam, fieldVal);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("导入校验异常{}{}",e.getMessage(),e );
                }
                errorMsg.append(temErrorMsg);
            }// if
        }

        if (StringUtils.isNotEmpty(errorMsg.toString())) {
            try {
                Method method = excelParam.getClass().getDeclaredMethod("setErrorMsg",String.class);
                method.invoke(excelParam,errorMsg.toString());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("There is a validatting Exception,【{}】,【{}】", e.getMessage(),e);
            }
        }
        return new Tuple<>(errorMsg.toString(), excelParam);
    }

    /**
     * 校验导入数据,返回有异常的行
     * @param param
     * @param excelDataMap
     * @return
     */
    public static  <T> Tuple<String,T> validateField(T param, Map<String, Object> excelDataMap) {
        Field[] fileds = param.getClass().getDeclaredFields();
        StringBuffer errorMsg = new StringBuffer("");

        for (Field field : fileds) {
            AlisaField alisaAnnotation = field.getAnnotation(AlisaField.class);
            NotNull notNullAnnotation = field.getAnnotation(NotNull.class);
            DateFieldFormate  dateFieldFormateAnnotation = field.getAnnotation(DateFieldFormate.class);

            String  fieldName  = field.getName();
            String  excelTitle;
            StringBuffer  temErrorMsg = new StringBuffer("") ;

            if (alisaAnnotation != null) {
                excelTitle = alisaAnnotation.value();
                Class fieldClass = field.getType();
                Class superclass = field.getType().getSuperclass();
                Class clazz = field.getType();
                String  fieldValue  = (String) excelDataMap.get(fieldName);

                if (notNullAnnotation != null && StringUtils.isEmpty(fieldValue)) {
                    temErrorMsg.append(notNullAnnotation.message());
                    continue;
                }

                if (superclass  == Number.class && (clazz == int.class
                        || clazz == short.class || clazz == long.class
                        || clazz == Integer.class || clazz == Short.class
                        || clazz == Long.class)) {

                    String firstNum = fieldValue.substring(0,1);
                    fieldValue = StringUtils.equals(firstNum,"-") ? fieldValue.substring(0,fieldValue.length()) : fieldValue;
                    String tempFieldValue = StringUtils.replaceFirst(fieldValue,"\\.","");
                    if (!StringUtils.isNumeric(tempFieldValue)) {
                        temErrorMsg.append(excelTitle).append("必须为数字！");
                    } else {
                        if (fieldValue.contains(".")) { temErrorMsg.append(excelTitle).append("必须为整数！");}
                    }
                }

                if (superclass  == Number.class && (clazz == double.class || clazz == float.class
                        ||  clazz == Double.class || clazz == Float.class || clazz == BigDecimal.class)) {
                    fieldValue = StringUtils.replaceFirst(fieldValue,"\\.","");
                    if (!StringUtils.isNumeric(fieldValue)) {
                        temErrorMsg.append(excelTitle).append("必须为数字！");
                    }
                }

                try {
                    String methodName = "set"+ upperFirstWordString(fieldName);
                    Method method = param.getClass().getDeclaredMethod(methodName,clazz);
                    Object fieldVal = excelDataMap.get(fieldName);

                    if (null != method && StringUtils.isEmpty(temErrorMsg.toString())) {
                        if (superclass == Number.class) {
                            method.invoke(param, getNumberValue((String) fieldVal, clazz));
                        } else if (fieldClass.equals(java.util.Date.class)) {
                             boolean assignmentDate = dateFieldFormateAnnotation.assignmentDate();
                            Date date;
                            if (assignmentDate) {
                                date = new Date((String) fieldVal);
                            } else {
                                String formate = dateFieldFormateAnnotation.dateFormate();
                                date = DateUtils.parseDate((String) fieldVal,formate);
                            }
                            method.invoke(param, date);
                        } else {
                            method.invoke(param, fieldVal);
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ParseException e) {
                    log.error("导入校验异常{}{}",e.getMessage(),e );
                }
                errorMsg.append(temErrorMsg);
            }// if
        }

        if (StringUtils.isNotEmpty(errorMsg.toString())) {
            try {
                Method method = param.getClass().getDeclaredMethod("setErrorMsg",String.class);
                method.invoke(param,errorMsg.toString());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("There is a validatting Exception,【{}】,【{}】", e.getMessage(),e);
            }
        }
        return new Tuple<>(errorMsg.toString(), param);
    }

    /**
     * 字符串首字母大写
     * @return
     */
    private static  String upperFirstWordString(String fieldName) {
        return  fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 实例化数字字段
     * @param value
     * @param clazz
     * @return
     */
    private static  Object getNumberValue(String value, Class clazz) {
        Object result = null;
        try {
            Constructor constructor = clazz.getConstructor(new Class[]{String.class});
            result = constructor.newInstance(new Object []{value});
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("实例化数字字段异常！There is a number field newInstance exception. 【{}】,【{}】",e.getMessage(),e );
        }
        return result;
    }


    /**
     * 起别名注解
     */
    @Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AlisaField {
        String value() ;
    }

    /**
     * 格式化时间字段注解
     * assignmentDate - 是否可以通过 new Date(String) 获取到一个日期，取决于时间字段的格式
     * dateFormate - 字符串的公式,默认 yyyy-MM-dd
     */
    @Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DateFieldFormate {
        boolean assignmentDate() default false;
        String dateFormate() default "yyyy-MM-dd";
    }

    public interface InsertParam {
    }

}
