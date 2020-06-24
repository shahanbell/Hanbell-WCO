/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.pub.OutputTextMessage;
import com.lightshell.comm.BaseLib;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kevindong
 */
@Named(value = "wechatCorpManagedBean")
@SessionScoped
public class WeChatCorpManagedBean implements Serializable {

    protected int agentId;
    protected String userId;
    protected String openId;
    protected String cardId;
    protected String mediaId;
    private String textMsg;
    private final Logger log4j = LogManager.getLogger();

    @EJB
    private Agent1000002Bean wechatCorpBean;

    /**
     * Creates a new instance of WeChatManagedBean
     */
    public WeChatCorpManagedBean() {
    }

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        wechatCorpBean.setDataPath(fc.getExternalContext().getRealPath("/") + fc.getExternalContext().getInitParameter("DataPath"));
        wechatCorpBean.setResPath(fc.getExternalContext().getRealPath("/") + fc.getExternalContext().getInitParameter("ResPath"));
        wechatCorpBean.initConfiguration();
    }

    public void createMenu() {
    }

    public void sendTemplateMsg() {
        String msg = "";
        if (openId != null) {
            msg = wechatCorpBean.sendTemplateMsg(openId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
        }
    }

    public void sendTextMsg() {
        String msg = "Hello";
        if (openId != null) {
            msg = wechatCorpBean.sendMsgToUser(openId, "text", msg);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
        }
    }

    public void toXml() throws JAXBException {
        OutputTextMessage outputMsg = new OutputTextMessage();
        outputMsg.setToUserName(this.userId == null ? "UserId" : this.userId);
        outputMsg.setFromUserName(wechatCorpBean.getAppID());
        outputMsg.setCreateTime((int) BaseLib.getDate().getTime());
        outputMsg.setMsgType("text");
        outputMsg.setContent("Hello,Sam");

        JAXBContext outputContext = JAXBContext.newInstance(OutputTextMessage.class);
        Marshaller marshaller = outputContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        OutputStream os = new ByteArrayOutputStream();
        marshaller.marshal(outputMsg, os);

        log4j.info(os.toString());
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return the cardId
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * @param cardId the cardId to set
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * @return the agentId
     */
    public int getAgentId() {
        return agentId;
    }

    /**
     * @param agentId the agentId to set
     */
    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    /**
     * @return the mediaId
     */
    public String getMediaId() {
        return mediaId;
    }

    /**
     * @param mediaId the mediaId to set
     */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * @return the textMsg
     */
    public String getTextMsg() {
        return textMsg;
    }

    /**
     * @param textMsg the textMsg to set
     */
    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

}
