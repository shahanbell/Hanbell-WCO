/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.wco.ejb.Pub9780bcc30275Bean;
import cn.hanbell.wco.pub.OutputTextMessage;
import cn.hanbell.wco.pub.InputMessage;
import com.lightshell.comm.BaseLib;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author kevindong
 */
@Named(value = "wechatPubManagedBean")
@SessionScoped
public class WeChatPubManagedBean implements Serializable {

    protected String openid;
    protected String cardid;
    protected String mediaid;

    @EJB
    private Pub9780bcc30275Bean wechatBean;

    /**
     * Creates a new instance of WeChatManagedBean
     */
    public WeChatPubManagedBean() {
    }

    public void createMenu() {
        wechatBean.createMemu();
    }

    public void sendTemplateMsg() {
        String msg = "";
        if (openid != null) {
            msg = wechatBean.sendTemplateMsg(openid);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
        }
    }

    public void sendCardToOpenId() {
        String msg = "";
        if (openid != null) {
            msg = wechatBean.sendCardToOpenId(openid, cardid);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
        }
    }

    public void findMediaId(String type) {
        wechatBean.pullMedias(type, 0, 10);
    }

    public void findOpenUser() {
        wechatBean.saveUsers();
    }

    public void toXml() throws JAXBException {
        OutputTextMessage outputMsg = new OutputTextMessage();
        outputMsg.setToUserName("111");
        outputMsg.setFromUserName("222");
        outputMsg.setCreateTime((int) BaseLib.getDate().getTime());
        outputMsg.setMsgType("text");
        outputMsg.setContent("Hello,Sam");

        JAXBContext outputContext = JAXBContext.newInstance(OutputTextMessage.class);
        Marshaller marshaller = outputContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        OutputStream os = new ByteArrayOutputStream();
        marshaller.marshal(outputMsg, os);

        System.out.print(os.toString());
    }

    public void fromXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[FromUser]]></FromUserName>");
        sb.append("<CreateTime>123456789</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[CLICK]]></Event>");
        sb.append("<EventKey><![CDATA[EVENTKEY]]></EventKey></xml>");

        JAXBContext inputContext;
        try {
            StringReader sr = new StringReader(sb.toString());
            inputContext = JAXBContext.newInstance(InputMessage.class);
            Unmarshaller unmarshaller = inputContext.createUnmarshaller();
            //InputStream is
            InputMessage im = (InputMessage) unmarshaller.unmarshal(sr);
            //System.out.print(im.getEvent());
        } catch (JAXBException ex) {
            Logger.getLogger(WeChatPubManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid the openid to set
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return the cardid
     */
    public String getCardid() {
        return cardid;
    }

    /**
     * @param cardid the cardid to set
     */
    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    /**
     * @return the mediaid
     */
    public String getMediaid() {
        return mediaid;
    }

    /**
     * @param mediaid the mediaid to set
     */
    public void setMediaid(String mediaid) {
        this.mediaid = mediaid;
    }

}
