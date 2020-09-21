/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.servlet;

import cn.hanbell.wco.ejb.ConfigPropertiesBean;
import cn.hanbell.wco.ejb.ServicePubBean;
import cn.hanbell.wco.ejb.WeChatSessionBean;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * 该Servlet为统一微信扫码的回调Servlet,参数必须符合规范state 网页授权code
 * state回调时需要的数据.格式：首个单词表示你需要处理的业务，接下来以_区分。每条数据之间使用_隔开。例如
 * maintainDescribe_CQFW_2020060015_C2082_沈鑫_安庆卡尔特
 *
 * @author C2082
 */
public class AuthCallBack extends HttpServlet {

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.wco");
    @EJB
    private ServicePubBean servicePubBean;
    @EJB
    private WeChatSessionBean wechatSessionBean;

    @EJB
    private ConfigPropertiesBean configPropertiesBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("content-type", "text/html;charset=UTF-8");
        //微信code凭证
        //state键位微信固定。根据state的值判断执行某个业务逻辑
        String code = req.getParameter("code");
        String state = req.getParameter("state");
        servicePubBean.initAccessTokenAndOpenId(code);
        JSONObject jo = servicePubBean.getUserInfo();
        if (jo != null) {
            String nickname = jo.getString("nickname");
            String openID = jo.getString("openid");
            String sessionkey = getSessionKey();
            String sessionid= UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
            wechatSessionBean.persistIfNotExist(openID, sessionkey,sessionid);
            //维修描述
            if (state.startsWith("maintainDescribe")) {
                //二维码传递过来的维修单号和维修单别
                String[] infos = state.split("_");
                StringBuffer url = new StringBuffer();
                url.append(configPropertiesBean.findByKey("cn.hanbell.wco.servlet.AuthCallBack.maintainDescribe.redirectUrl").getConfigvalue());
                url.append("?maintainType=");
                url.append(infos[1]).append("&maintainNumber=");
                url.append(infos[2]).append("&maintainerId=");
                url.append(infos[3]).append("&maintainer=");
                url.append(URLEncoder.encode(infos[4].toString(), "utf-8")).append("&customer=");
                url.append(URLEncoder.encode(infos[5].toString(), "utf-8")).append("&openID=");
                url.append(openID).append("&nickname=");
                url.append(URLEncoder.encode(nickname.toString(), "utf-8")).append("&sessionkey=");
                url.append(sessionid);
                resp.sendRedirect(url.toString());
            }
        }
        log4j.info("--------code=" + code + "---------" + "state=" + state);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public String getSessionKey() {
        return RandomStringUtils.randomAlphanumeric(22).concat("==");
    }
}
