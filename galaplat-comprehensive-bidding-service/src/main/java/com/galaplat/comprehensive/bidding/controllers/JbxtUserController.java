package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.vos.pojo.MyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.querys.JbxtUserQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户表Controller
 *
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/jbxt/user")
public class JbxtUserController {


    @Autowired
    IJbxtUserService jbxtuserService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    Logger LOGGER = LoggerFactory.getLogger(JbxtUserController.class);

    @Deprecated
    public Object login(String username, String password) {
        //判断username和password非空非""
        if (username != null && (!username.equals(""))) {
            if (password != null && (!password.equals(""))) {
                //数据库查询
                boolean res = jbxtuserService.handlerLogin(username, password);
                if (res) {
                    return new MyResult(true, "登录成功", null);
                } else {
                    return new MyResult(false, "账号或者密码不正确", null);
                }
            } else {
                return new MyResult(false, "非法参数: 密码不能为空哦(*￣︶￣)", null);
            }
        } else {
            return new MyResult(false, "非法参数: 账号不能为空哦(*￣︶￣)", null);
        }
    }

    @PostMapping("/login")
    @RestfulResult
    public Object login2(String username, String password, String activityCode) {
        if (username == null || (username.equals(""))) {
            return new MyResult(false, "非法参数: 账号不能为空哦(*￣︶￣)", null);
        }
        if (password == null || (password.equals(""))) {
            return new MyResult(false, "非法参数: 密码不能为空哦(*￣︶￣)", null);
        }
        if (activityCode == null || (activityCode.equals(""))) {
            return new MyResult(false, "非法参数: activityCode不能为空哦(*￣︶￣)", null);
        }
        JbxtUserDO jbxtUserDO = jbxtuserService.selectByUsernameAndActivityCode(username, activityCode);
        if (jbxtUserDO == null) {
            return new MyResult(false, "提示: 目标用户不存在(*￣︶￣)", null);
        }

        if(jbxtUserDO.getPassword().equals(password)) {
            //存入session中
            httpServletRequest.getSession().setAttribute(SessionConstant.SESSION_USER,jbxtUserDO);
            LOGGER.info("jbxtUserController(login): "+jbxtUserDO.getUsername()+" Login");
             Map<String, String> map = new HashMap<>();
             map.put("userCode", jbxtUserDO.getCode());

             return new MyResult(true, "登录成功", map);
         } else {
             return new MyResult(false, "账号或者密码不正确", null);
         }
    }

}