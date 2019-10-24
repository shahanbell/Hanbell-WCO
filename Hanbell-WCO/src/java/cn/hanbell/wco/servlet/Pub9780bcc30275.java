/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.servlet;

import cn.hanbell.wco.pub.InputMessage;
import cn.hanbell.wco.pub.OutputArticleContent;
import cn.hanbell.wco.pub.OutputArticleMessage;
import cn.hanbell.wco.pub.OutputImageMessage;
import cn.hanbell.wco.pub.OutputTextMessage;
import cn.hanbell.wco.ejb.MaterialBean;
import cn.hanbell.wco.ejb.Pub9780bcc30275Bean;
import cn.hanbell.wco.ejb.WeChatUserCardBean;
import cn.hanbell.wco.entity.Material;
import cn.hanbell.wco.entity.WeChatUserCard;
import com.lightshell.comm.BaseLib;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author kevindong
 */
public class Pub9780bcc30275 extends HttpServlet {

    @EJB
    private MaterialBean materialBean;
    @EJB
    private WeChatUserCardBean wechatUserCardBean;
    @EJB
    private Pub9780bcc30275Bean wechatPubBean;

    private final Logger log4j = LogManager.getLogger("cn.gxxx.wco");
    private String token = "shgxxx";
    private String appID;
    private String appSecret;

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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            OutputArticleMessage articleMsg;
            OutputImageMessage imgMsg;
            OutputTextMessage textMsg;
            JSONObject jsonObject;
            Material material;
            List<OutputArticleContent> articleItems;
            InputStream is = request.getInputStream();
            JAXBContext jaxb = JAXBContext.newInstance(InputMessage.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            InputMessage inputMsg = (InputMessage) unmarshaller.unmarshal(is);

            if (inputMsg != null) {
                String msgType = inputMsg.getMsgType();
                String event = inputMsg.getEvent();
                String eventKey = inputMsg.getEventKey();
                log4j.info(msgType);
                log4j.info(event);
                log4j.info(eventKey);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //判断消息类型
                switch (msgType) {
                    case "text":
                        jsonObject = wechatPubBean.pullMedia("cRP5YvaWssf1nAoVT6Azyss1m4aqDd3GDbQ2xYJCLbE");
                        articleMsg = new OutputArticleMessage();
                        articleMsg.setToUserName(inputMsg.getFromUserName());
                        articleMsg.setFromUserName(inputMsg.getToUserName());
                        articleMsg.setCreateTime(BaseLib.getDate().getTime());
                        articleMsg.setMsgType("news");
                        articleItems = new ArrayList<>();
                        for (Iterator<Object> it = jsonObject.getJSONArray("news_item").iterator(); it.hasNext();) {
                            JSONObject news = (JSONObject) it.next();
                            material = materialBean.findByMediaId(news.getString("thumb_media_id"));

                            OutputArticleContent item = new OutputArticleContent();
                            item.setTitle(news.getString("title"));
                            item.setDescription(news.getString("digest"));
                            if (material != null) {
                                item.setPicUrl(material.getUrl());
                            }
                            item.setUrl(news.getString("content_source_url"));
                            articleItems.add(item);
                        }
                        articleMsg.setItems(articleItems);
                        articleMsg.setArticleCount(articleItems.size());
                        //转换
                        BaseLib.convertObjectToXML(OutputArticleMessage.class, articleMsg, os);
                        break;
                    case "event":
                        switch (event) {
                            case "CLICK":
                                switch (eventKey) {
                                    case "M_VIPCARD":
                                        //领取会员卡
                                        jsonObject = wechatPubBean.pullMedia("cRP5YvaWssf1nAoVT6AzyvZFv6TaV9SK1ZCfyUkLRhA");
                                        articleMsg = new OutputArticleMessage();
                                        articleMsg.setToUserName(inputMsg.getFromUserName());
                                        articleMsg.setFromUserName(inputMsg.getToUserName());
                                        articleMsg.setCreateTime(BaseLib.getDate().getTime());
                                        articleMsg.setMsgType("news");
                                        articleItems = new ArrayList<>();
                                        for (Iterator<Object> it = jsonObject.getJSONArray("news_item").iterator(); it.hasNext();) {
                                            JSONObject news = (JSONObject) it.next();
                                            material = materialBean.findByMediaId(news.getString("thumb_media_id"));

                                            OutputArticleContent item = new OutputArticleContent();
                                            item.setTitle(news.getString("title"));
                                            item.setDescription(news.getString("digest"));
                                            if (material != null) {
                                                item.setPicUrl(material.getUrl());
                                            }
                                            item.setUrl(news.getString("url"));
                                            item.setContent(news.getString("content"));
                                            articleItems.add(item);
                                        }
                                        articleMsg.setItems(articleItems);
                                        articleMsg.setArticleCount(articleItems.size());
                                        //转换
                                        BaseLib.convertObjectToXML(OutputArticleMessage.class, articleMsg, os);
                                        break;
                                    default:
                                        textMsg = new OutputTextMessage();
                                        textMsg.setToUserName(inputMsg.getFromUserName());
                                        textMsg.setFromUserName(inputMsg.getToUserName());
                                        textMsg.setCreateTime(BaseLib.getDate().getTime());
                                        textMsg.setMsgType("text");
                                        textMsg.setContent("收到点击菜单事件" + inputMsg.getEventKey());
                                        BaseLib.convertObjectToXML(OutputTextMessage.class, textMsg, os);
                                }
                                break;
                            case "user_get_card":
                                textMsg = new OutputTextMessage();
                                textMsg.setToUserName(inputMsg.getFromUserName());
                                textMsg.setFromUserName(inputMsg.getToUserName());
                                textMsg.setCreateTime(BaseLib.getDate().getTime());
                                textMsg.setMsgType("text");
                                int i = wechatUserCardBean.getCountByOpenId(inputMsg.getFromUserName());
                                if (i == 0) {
                                    WeChatUserCard card = new WeChatUserCard();
                                    card.setOpenid(inputMsg.getFromUserName());
                                    card.setCardid(inputMsg.getCardId());
                                    card.setCardcode(inputMsg.getUserCardCode());
                                    card.setStatusToNew();
                                    card.setCreatorToSystem();
                                    card.setCredateToNow();
                                    try {
                                        wechatUserCardBean.persist(card);
                                        textMsg.setContent("您的卡号是：" + inputMsg.getUserCardCode() + ",请妥善保管");
                                    } catch (Exception ex) {
                                        log4j.error(ex);
                                        textMsg.setContent("系统出错，保存卡片资料失败");
                                    }
                                } else {
                                    textMsg.setContent("支持一人一卡,一人多券");
                                }
                                BaseLib.convertObjectToXML(OutputTextMessage.class, textMsg, os);
                                break;
                            case "scancode_waitmsg":
                                textMsg = new OutputTextMessage();
                                textMsg.setToUserName(inputMsg.getFromUserName());
                                textMsg.setFromUserName(inputMsg.getToUserName());
                                textMsg.setCreateTime(BaseLib.getDate().getTime());
                                textMsg.setMsgType("text");
                                textMsg.setContent("扫码" + inputMsg.getScanCodeInfo().getScanResult());
                                BaseLib.convertObjectToXML(OutputTextMessage.class, textMsg, os);
                                break;
                            default:
                                response.getWriter().write("success");
                        }
                        break;
                    default:
                        response.getWriter().write("success");
                }
                log4j.info(os.toString("UTF-8"));
                response.getWriter().write(os.toString("UTF-8"));
            } else {
                response.getWriter().write("");
            }
        } catch (JAXBException ex) {
            log4j.error(ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getServletPath();
        //成为开发者验证
        String signature = request.getParameter("signature");   //微信加密签名
        String timestamp = request.getParameter("timestamp");   //时间戳
        String nonce = request.getParameter("nonce");           //随机数
        String echostr = request.getParameter("echostr");       //随机字符串
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效
        String[] paramStr = {token, timestamp, nonce};
        Arrays.sort(paramStr);//字典排序

        String param = String.join("", paramStr);
        String key = BaseLib.sha1(param);

        if (signature.equals(key)) {
            response.getWriter().write(echostr);
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
        this.appID = config.getInitParameter("AppID");
        this.appSecret = config.getInitParameter("AppSecret");
        log4j.info("WeChatPub Init Param AppID:" + appID);
        log4j.info("WeChatPub Init Param AppSecret:" + appSecret);
    }

}
