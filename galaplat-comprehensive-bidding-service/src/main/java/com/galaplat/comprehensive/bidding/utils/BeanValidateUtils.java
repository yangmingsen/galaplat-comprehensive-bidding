package com.galaplat.comprehensive.bidding.utils;


import com.galaplat.base.core.common.enums.CodeEnum;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.vos.ValidateResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 实体校验工具类
 * @Author: luffy
 * @CreateDate: 2018/9/25 10:14
 */
@Slf4j
public class BeanValidateUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidateResultVO validateEntity(T obj, Class... groupClasses) {
        ValidateResultVO result = new ValidateResultVO();
        Set<ConstraintViolation<T>> set = validator.validate(obj, groupClasses);
        if (set != null && !set.isEmpty()) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            for (ConstraintViolation<T> cv : set) {
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    public static <T> ValidateResultVO validateProperty(T obj, String propertyName, Class... groupClasses) {
        ValidateResultVO result = new ValidateResultVO();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName);
        if (set != null && !set.isEmpty()) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            for (ConstraintViolation<T> cv : set) {
                errorMsg.put(propertyName, cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    public static <T> void validateErrorThenThrowException(T validateBean, String logFlag, Class... groupClasses) throws BaseException {
        if (null == validateBean) {
            String error = logFlag + "-->校验的实体为空！";
            log.info(error);
            throw new BaseException(CodeEnum.VALIDATOR_INVALID, error);
        }
        ValidateResultVO resultVO = null;
        if ((validateBean instanceof List) && !(validateBean instanceof ValidableList)) {
            resultVO = validateEntity(new ValidableList<>((List) validateBean), groupClasses);
        } else {
            resultVO = validateEntity(validateBean, groupClasses);
        }
        if (resultVO.isHasErrors()) {
            String error = resultVO.getErrorMessageDetail();
            log.info("【{}-->校验】失败:{}", logFlag, error);
            throw new BaseException(CodeEnum.VALIDATOR_INVALID, error);
        }
    }

    public static <T> void validateErrorThenThrowException(T validateBean, Class... groupClasses) throws BaseException {
        validateErrorThenThrowException(validateBean, "", groupClasses);
    }
}

