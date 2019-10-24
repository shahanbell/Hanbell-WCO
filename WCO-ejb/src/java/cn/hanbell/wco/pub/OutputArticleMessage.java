/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.pub;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 *
 * @author kevindong
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutputArticleMessage {

    @XmlCDATA
    private String ToUserName;
    @XmlCDATA
    private String FromUserName;
    @XmlCDATA
    private long CreateTime;
    @XmlCDATA
    private String MsgType;
    @XmlCDATA
    private int ArticleCount;

    @XmlElementWrapper(name = "Articles")
    @XmlElement(name = "item")
    private List<OutputArticleContent> items;

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
     * @return the ArticleCount
     */
    public int getArticleCount() {
        return ArticleCount;
    }

    /**
     * @param ArticleCount the ArticleCount to set
     */
    public void setArticleCount(int ArticleCount) {
        this.ArticleCount = ArticleCount;
    }

    /**
     * @return the items
     */
    public List<OutputArticleContent> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<OutputArticleContent> items) {
        this.items = items;
    }

}
