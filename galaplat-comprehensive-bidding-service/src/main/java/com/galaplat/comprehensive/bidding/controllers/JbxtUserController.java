package com.galaplat.comprehensive.bidding.controllers;

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

    Logger LOGGER = LoggerFactory.getLogger(JbxtUserController.class);

    @PostMapping("/login")
    @RestfulResult
    public Object login(String username, String password) {
        LOGGER.info("JbxtUserController(login): username=" + username + " password=" + password);

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
            }
        }
        return new MyResult(false, "非法参数", null);
    }

}