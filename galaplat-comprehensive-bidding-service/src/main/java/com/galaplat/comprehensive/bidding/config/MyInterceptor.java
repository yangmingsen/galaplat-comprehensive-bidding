package com.galaplat.comprehensive.bidding.config;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyInterceptor implements HandlerInterceptor {
    Logger LOGGER = LoggerFactory.getLogger(MyInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        LOGGER.info("from session: "+httpServletRequest.getSession().getId());
        JbxtUserDO userExist = (JbxtUserDO)httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);
        LOGGER.info("user: "+userExist);

        LOGGER.info("requestPath="+httpServletRequest.getServletPath());

        if (userExist == null) {
//            httpServletResponse.setHeader(SessionConstant.ACCESS_CONTROL_EXPOSE_HEADERS, SessionConstant.SESSION_STATE);
            httpServletResponse.setHeader(SessionConstant.SESSION_STATE,SessionConstant.SESSION_TIME_OUT);
            return false;
        }

        LOGGER.info("requestUser="+userExist.getUsername()+" Code="+userExist.getCode());


        JbxtUserDO user = (JbxtUserDO)userExist;
        String reqPath = httpServletRequest.getServletPath();
        if (reqPath.startsWith("/admin")) { //拦截非管理员的请求
            String isAdmin = user.getAdmin();
            if (isAdmin.equals("0")) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
