/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.corp;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 *
 * @author kevindong
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReqMessage implements Serializable {

    @XmlElement(name = "ScanCodeInfo")
    private ScanCodeInfo scanCodeInfo;

    @XmlCDATA
    private String ToUserName;
    @XmlCDATA
    private String FromUserName;
    @XmlCDATA
    private int CreateTime;
    @XmlCDATA
    private String MsgType;
    @XmlCDATA
    private String Content;
    @XmlCDATA
    private String PicUrl;
    @XmlCDATA
    private String MediaId;
    @XmlCDATA
    private String Format;
    @XmlCDATA
    private String ThumbMediaId;
    @XmlCDATA
    private float Location_X;
    @XmlCDATA
    private float Location_Y;
    @XmlCDATA
    private int Scale;
    @XmlCDATA
    private String Label;
    @XmlCDATA
    private String Title;
    @XmlCDATA
    private String Description;
    @XmlCDATA
    private Integer MsgId;
    @XmlCDATA
    private int AgentId;
    @XmlCDATA
    private String Event;
    @XmlCDATA
    private String EventKey;
    @XmlCDATA
    private String TaskId;
    @XmlCDATA
    private float Latitude;
    @XmlCDATA
    private float Longitude;
    @XmlCDATA
    private float Precision;

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
    public int getCreateTime() {
        return CreateTime;
    }

    /**
     * @param CreateTime the CreateTime to set
     */
    public void setCreateTime(int CreateTime) {
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

    /**
     * @return the PicUrl
     */
    public String getPicUrl() {
        return PicUrl;
    }

    /**
     * @param PicUrl the PicUrl to set
     */
    public void setPicUrl(String PicUrl) {
        this.PicUrl = PicUrl;
    }

    /**
     * @return the MediaId
     */
    public String getMediaId() {
        return MediaId;
    }

    /**
     * @param MediaId the MediaId to set
     */
    public void setMediaId(String MediaId) {
        this.MediaId = MediaId;
    }

    /**
     * @return the Format
     */
    public String getFormat() {
        return Format;
    }

    /**
     * @param Format the Format to set
     */
    public void setFormat(String Format) {
        this.Format = Format;
    }

    /**
     * @return the ThumbMediaId
     */
    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    /**
     * @param ThumbMediaId the ThumbMediaId to set
     */
    public void setThumbMediaId(String ThumbMediaId) {
        this.ThumbMediaId = ThumbMediaId;
    }

    /**
     * @return the Location_X
     */
    public float getLocation_X() {
        return Location_X;
    }

    /**
     * @param Location_X the Location_X to set
     */
    public void setLocation_X(float Location_X) {
        this.Location_X = Location_X;
    }

    /**
     * @return the Location_Y
     */
    public float getLocation_Y() {
        return Location_Y;
    }

    /**
     * @param Location_Y the Location_Y to set
     */
    public void setLocation_Y(float Location_Y) {
        this.Location_Y = Location_Y;
    }

    /**
     * @return the Scale
     */
    public int getScale() {
        return Scale;
    }

    /**
     * @param Scale the Scale to set
     */
    public void setScale(int Scale) {
        this.Scale = Scale;
    }

    /**
     * @return the Label
     */
    public String getLabel() {
        return Label;
    }

    /**
     * @param Label the Label to set
     */
    public void setLabel(String Label) {
        this.Label = Label;
    }

    /**
     * @return the Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * @param Title the Title to set
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return the MsgId
     */
    public Integer getMsgId() {
        return MsgId;
    }

    /**
     * @param MsgId the MsgId to set
     */
    public void setMsgId(Integer MsgId) {
        this.MsgId = MsgId;
    }

    /**
     * @return the AgentId
     */
    public int getAgentId() {
        return AgentId;
    }

    /**
     * @param AgentId the AgentId to set
     */
    public void setAgentId(int AgentId) {
        this.AgentId = AgentId;
    }

    /**
     * @return the Event
     */
    public String getEvent() {
        return Event;
    }

    /**
     * @param Event the Event to set
     */
    public void setEvent(String Event) {
        this.Event = Event;
    }

    /**
     * @return the EventKey
     */
    public String getEventKey() {
        return EventKey;
    }

    /**
     * @param EventKey the EventKey to set
     */
    public void setEventKey(String EventKey) {
        this.EventKey = EventKey;
    }

    /**
     * @return the TaskId
     */
    public String getTaskId() {
        return TaskId;
    }

    /**
     * @param TaskId the TaskId to set
     */
    public void setTaskId(String TaskId) {
        this.TaskId = TaskId;
    }

    /**
     * @return the Latitude
     */
    public float getLatitude() {
        return Latitude;
    }

    /**
     * @param Latitude the Latitude to set
     */
    public void setLatitude(float Latitude) {
        this.Latitude = Latitude;
    }

    /**
     * @return the Longitude
     */
    public float getLongitude() {
        return Longitude;
    }

    /**
     * @param Longitude the Longitude to set
     */
    public void setLongitude(float Longitude) {
        this.Longitude = Longitude;
    }

    /**
     * @return the Precision
     */
    public float getPrecision() {
        return Precision;
    }

    /**
     * @param Precision the Precision to set
     */
    public void setPrecision(float Precision) {
        this.Precision = Precision;
    }

    /**
     * @return the scanCodeInfo
     */
    public ScanCodeInfo getScanCodeInfo() {
        return scanCodeInfo;
    }

    /**
     * @param scanCodeInfo the scanCodeInfo to set
     */
    public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
        this.scanCodeInfo = scanCodeInfo;
    }

}
