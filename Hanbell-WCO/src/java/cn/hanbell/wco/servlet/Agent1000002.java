/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.servlet;

import cn.hanbell.eap.ejb.PersonnelChangeBean;
import cn.hanbell.eap.ejb.RewardsPunishmentBean;
import cn.hanbell.eap.ejb.SalarySendBean;
import cn.hanbell.eap.ejb.ServiceContractBean;
import cn.hanbell.eap.entity.PersonnelChange;
import cn.hanbell.eap.entity.RewardsPunishment;
import cn.hanbell.eap.entity.SalarySend;
import cn.hanbell.eap.entity.ServiceContract;
import cn.hanbell.wco.corp.ReqEncryptMessage;
import cn.hanbell.wco.corp.ReqMessage;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.pub.OutputTextMessage;
import com.lightshell.comm.BaseLib;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author kevindong
 */
public class Agent1000002 extends HttpServlet {

    @EJB
    private Agent1000002Bean agent1000002Bean;
    @EJB
    private SalarySendBean salarySendBean;
    @EJB
    private PersonnelChangeBean personnelChangeBean;
    @EJB
    private RewardsPunishmentBean rewardspunishmentBean;
        @EJB
    private ServiceContractBean serviceContractBean;

    private final Logger log4j = LogManager.getLogger("cn.hanbell.wco");

    private final String CHARSET = "UTF-8";

    private final String JRS_APPID = "1505912014724";
    private final String JRS_TOKEN = "0ec858293fccfad55575e26b0ce31177";

    protected String corpID = "";
    protected String corpSecret = "";
    protected String token = "shgxxx";
    protected String encodingAESKey = null;

    protected String dataPath;
    protected String resPath;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(CHARSET);
        response.setCharacterEncoding(CHARSET);
        // 微信加密签名
        String signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        JSONObject jsonObject;
        InputStream is;
        try {
            is = request.getInputStream();
            ReqEncryptMessage reqEncryptMsg = BaseLib.convertXMLToObject(ReqEncryptMessage.class, is);
            if (reqEncryptMsg != null) {
                String resp = "";
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                String toUser = reqEncryptMsg.getToUserName();
                int agentID = reqEncryptMsg.getAgentID();
                String encrypt = reqEncryptMsg.getEncrypt();
                // 签名验证
                String checking = agent1000002Bean.getSignature(token, timestamp, nonce, encrypt);
                if (!signature.equals(checking)) {
                    throw new Exception("ValidateSignatureError");
                }
                // 得到明文消息
                String content = agent1000002Bean.decrypt(encrypt);
                log4j.info(content);
                // 明文XML转成对象
                is = new ByteArrayInputStream(content.getBytes(CHARSET));
                ReqMessage inputMsg = BaseLib.convertXMLToObject(ReqMessage.class, is);
                if (inputMsg != null) {
                    String msgType = inputMsg.getMsgType();
                    OutputTextMessage textMsg;
                    switch (msgType) {
                        case "text":
                            textMsg = new OutputTextMessage();
                            textMsg.setToUserName(inputMsg.getFromUserName());
                            textMsg.setFromUserName(inputMsg.getToUserName());
                            textMsg.setCreateTime(BaseLib.getDate().getTime());
                            textMsg.setMsgType("text");
                            textMsg.setContent("感谢你发消息过来");
                            BaseLib.convertObjectToXML(OutputTextMessage.class, textMsg, os);
                            resp = agent1000002Bean.encrypt(os.toString(CHARSET), toUser, timestamp, nonce);
                            break;
                        case "event":
                            String fromUser = inputMsg.getFromUserName();
                            String eventType = inputMsg.getEvent();
                            String eventKey = inputMsg.getEventKey();
                            switch (eventType) {
                                case "click":
                                    switch (eventKey) {
                                        default:
                                            textMsg = new OutputTextMessage();
                                            textMsg.setToUserName(fromUser);
                                            textMsg.setFromUserName(inputMsg.getToUserName());
                                            textMsg.setCreateTime(BaseLib.getDate().getTime());
                                            textMsg.setMsgType("text");
                                            textMsg.setContent("点击事件：" + eventKey);
                                            BaseLib.convertObjectToXML(OutputTextMessage.class, textMsg, os);
                                            resp = agent1000002Bean.encrypt(os.toString(CHARSET), toUser, timestamp, nonce);
                                            break;
                                    }
                                    break;
                                case "scancode_waitmsg":
                                    switch (eventKey) {

                                    }
                                    break;
                                case "taskcard_click":
                                    String taskId = inputMsg.getTaskId();
                                    String userid = inputMsg.getFromUserName();
                                    if (taskId.startsWith("XZHZ")) {
                                        String taskMsg;
                                        switch (eventKey) {
                                            // 确认
                                            case "confirm":
                                                SalarySend confirm = salarySendBean.findByTaskidAndEmployeeid(taskId, userid);
                                                confirm.setStatus("V");
                                                confirm.setConfirmtime(new Date());
                                                salarySendBean.update(confirm);
                                                break;
                                        }
                                    } else if (taskId.startsWith("RSYD")) {
                                        //人事异动的确认回调
                                        String taskMsg;
                                        switch (eventKey) {
                                            // 确认
                                            case "confirm":
                                                PersonnelChange p = personnelChangeBean.findByEmployeeIdAndTaskId(userid, taskId);
                                                p.setStatus("V");
                                                p.setConfirmtime(new Date());
                                                personnelChangeBean.update(p);
                                                break;
                                        }
                                    } else if (taskId.startsWith("GRJC")) {
                                        //个人奖惩的确认
                                        String taskMsg;
                                        switch (eventKey) {
                                            // 确认
                                            case "confirm":
                                                RewardsPunishment rp = rewardspunishmentBean.findByEmployeeIdAndTaskId(taskId, userid);
                                                rp.setStatus("V");
                                                rp.setConfirmtime(new Date());
                                                rewardspunishmentBean.update(rp);
                                                break;
                                        }
                                    }else if (taskId.startsWith("SC")) {
                                        //电子合同确认
                                        String taskMsg;
                                        switch (eventKey) {
                                            // 确认
                                            case "confirm":
                                                ServiceContract sc = serviceContractBean.findByEmployeeIdAndTaskId(taskId, userid);
                                                sc.setStatus("V");
                                                sc.setConfirmtime(new Date());
                                                serviceContractBean.update(sc);
                                                break;
                                        }
                                    } else if (taskId.startsWith("PKG_HS_PB015")) {
                                        String[] arr = taskId.split("@");
                                        if (eventKey.equals("approve") && arr.length == 2) {
                                            String url = agent1000002Bean.getConfiguration("Hanbell-JRS");
                                            url = url + "/efgp/hspb015/approve/" + arr[1] + "/" + userid + "?appid=" + JRS_APPID + "&token=" + JRS_TOKEN;
                                            agent1000002Bean.post(url, null);
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;// event
                        case "image":
                            String picurl = inputMsg.getPicUrl();
                            String mediaId = inputMsg.getMediaId();
                            CloseableHttpResponse c = agent1000002Bean.get(picurl, null, null);
                            if (c != null) {
                                HttpEntity httpEntity = c.getEntity();
                                if (httpEntity != null) {
                                    InputStream inputStream = httpEntity.getContent();
                                    FileUtils.copyInputStreamToFile(inputStream, new File(resPath + mediaId + ".jpeg"));
                                    log4j.info("写入图片" + mediaId);
                                }
                            }
                            resp = "";
                            break;
                        default:
                            resp = "";
                    }
                }
                log4j.info(os.toString(CHARSET));
                response.getWriter().write(resp);
            }
            response.getWriter().write("");
        } catch (JAXBException ex) {
            log4j.error(ex);
        } catch (Exception ex) {
            log4j.error(ex);
        }

    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 消息接入验证
        String signature = request.getParameter("msg_signature"); // 微信加密签名
        String timestamp = request.getParameter("timestamp"); // 时间戳
        String nonce = request.getParameter("nonce"); // 随机数
        String echostr = request.getParameter("echostr"); // 随机字符串
        // 确认此次GET请求来自微信服务器，原样返回echostr参数中的msg，则接入生效
        String key = agent1000002Bean.getSignature(token, timestamp, nonce, echostr);
        if (signature.equals(key)) {
            try {
                String ret = agent1000002Bean.decrypt(echostr);
                response.getWriter().write(ret);
            } catch (Exception ex) {
                log4j.error(ex);
                response.getWriter().write("error");
            }
        } else {
            response.getWriter().write("error");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "WeiXin Interaction";
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.dataPath = config.getServletContext().getRealPath("/") + config.getInitParameter("DataPath");
        this.resPath = config.getServletContext().getRealPath("/") + config.getInitParameter("ResPath");
        agent1000002Bean.setDataPath(dataPath);
        agent1000002Bean.setResPath(resPath);
        agent1000002Bean.initConfiguration();
    }

}
