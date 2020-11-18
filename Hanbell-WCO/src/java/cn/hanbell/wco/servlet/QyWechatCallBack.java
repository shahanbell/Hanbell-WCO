package cn.hanbell.wco.servlet;

import cn.hanbell.wco.ejb.Agent1000002Bean;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C2082
 */
public class QyWechatCallBack extends HttpServlet {

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.wco");

    @EJB
    private Agent1000002Bean agent1000002Bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("content-type", "text/html;charset=UTF-8");
        String code = req.getParameter("code");
        String state = req.getParameter("state");
        log4j.info("--------code=" + code + "---------" + "state=" + state);
        agent1000002Bean.initConfiguration();
        String userid = agent1000002Bean.getUserIdByCode(code);
        log4j.info("--------userid=" + userid);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
