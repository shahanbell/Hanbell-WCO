/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 通过扫描二维码跳转微信权限验证，验证成功后微信回调此Servlet
 *
 * @author C2082
 */
public class AuthCallBack extends HttpServlet {
 protected final Logger log4j = LogManager.getLogger("cn.hanbell.wco");
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        //微信code凭证
        //state键位微信固定。根据state的值判断执行某个业务逻辑
        String ceode = req.getParameter("code");
        String state = req.getParameter("state");
        log4j.info("--------code="+ceode+"---------"+"state="+state);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
