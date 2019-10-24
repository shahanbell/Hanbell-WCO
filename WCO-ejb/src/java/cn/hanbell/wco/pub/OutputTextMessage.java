/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.pub;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 *
 * @author kevindong
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutputTextMessage implements Serializable {

    @XmlCDATA
    private String ToUserName;
    @XmlCDATA
    private String FromUserName;
    @XmlCDATA
    private long CreateTime;
    @XmlCDATA
    private String MsgType;
    @XmlCDATA
    private String Content;

    /**
     * @return the ToUserName
     */
    public String getToUserName() {
        return ToUserName;
    }

    /**
     * @param ToUserName the ToUserName to set
     */
    public void setToUserName(String ToUserName) {
        this.ToUserName = ToUserName;
    }

    /**
     * @return the FromUserName
     */
    public String getFromUserName() {
        return FromUserName;
    }

    /**
     * @param FromUserName the FromUserName to set
     */
    public void setFromUserName(String FromUserName) {
        this.FromUserName = FromUserName;
    }

    /**
     * @return the CreateTime
     */
    public long getCreateTime() {
        return CreateTime;
    }

    /**
     * @param CreateTime the CreateTime to set
     */
    public void setCreateTime(long CreateTime) {
        this.CreateTime = CreateTime;
    }

    /**
     * @return the MsgType
     */
    public String getMsgType() {
        return MsgType;
    }

    /**
     * @param MsgType the MsgType to set
     */
    public void setMsgType(String MsgType) {
        this.MsgType = MsgType;
    }

    /**
     * @return the Content
     */
    public String getContent() {
        return Content;
    }

    /**
     * @param Content the Content to set
     */
    public void setContent(String Content) {
        this.Content = Content;
    }

}
