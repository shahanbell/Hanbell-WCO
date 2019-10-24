/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.pub;

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
public class InputMessage implements Serializable {

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
    private Integer MsgId;
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
    private String Url;
    @XmlCDATA
    private String Event;
    @XmlCDATA
    private String EventKey;
    @XmlCDATA
    private String CardId;
    @XmlCDATA
    private int IsGiveByFriend;
    @XmlCDATA
    private String FriendUserName;
    @XmlCDATA
    private String UserCardCode;
    @XmlCDATA
    private String OldUserCardCode;
    @XmlCDATA
    private int OuterId;
    @XmlCDATA
    private String OuterStr;
    @XmlCDATA
    private int IsRestoreMemberCard;
    @XmlCDATA
    private int IsRecommendByFriend;

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
     * @return the Url
     */
    public String getUrl() {
        return Url;
    }

    /**
     * @param Url the Url to set
     */
    public void setUrl(String Url) {
        this.Url = Url;
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

    /**
     * @return the CardId
     */
    public String getCardId() {
        return CardId;
    }

    /**
     * @param CardId the CardId to set
     */
    public void setCardId(String CardId) {
        this.CardId = CardId;
    }

    /**
     * @return the IsGiveByFriend
     */
    public int getIsGiveByFriend() {
        return IsGiveByFriend;
    }

    /**
     * @param IsGiveByFriend the IsGiveByFriend to set
     */
    public void setIsGiveByFriend(int IsGiveByFriend) {
        this.IsGiveByFriend = IsGiveByFriend;
    }

    /**
     * @return the FriendUserName
     */
    public String getFriendUserName() {
        return FriendUserName;
    }

    /**
     * @param FriendUserName the FriendUserName to set
     */
    public void setFriendUserName(String FriendUserName) {
        this.FriendUserName = FriendUserName;
    }

    /**
     * @return the UserCardCode
     */
    public String getUserCardCode() {
        return UserCardCode;
    }

    /**
     * @param UserCardCode the UserCardCode to set
     */
    public void setUserCardCode(String UserCardCode) {
        this.UserCardCode = UserCardCode;
    }

    /**
     * @return the OldUserCardCode
     */
    public String getOldUserCardCode() {
        return OldUserCardCode;
    }

    /**
     * @param OldUserCardCode the OldUserCardCode to set
     */
    public void setOldUserCardCode(String OldUserCardCode) {
        this.OldUserCardCode = OldUserCardCode;
    }

    /**
     * @return the OuterId
     */
    public int getOuterId() {
        return OuterId;
    }

    /**
     * @param OuterId the OuterId to set
     */
    public void setOuterId(int OuterId) {
        this.OuterId = OuterId;
    }

    /**
     * @return the OuterStr
     */
    public String getOuterStr() {
        return OuterStr;
    }

    /**
     * @param OuterStr the OuterStr to set
     */
    public void setOuterStr(String OuterStr) {
        this.OuterStr = OuterStr;
    }

    /**
     * @return the IsRestoreMemberCard
     */
    public int getIsRestoreMemberCard() {
        return IsRestoreMemberCard;
    }

    /**
     * @param IsRestoreMemberCard the IsRestoreMemberCard to set
     */
    public void setIsRestoreMemberCard(int IsRestoreMemberCard) {
        this.IsRestoreMemberCard = IsRestoreMemberCard;
    }

    /**
     * @return the IsRecommendByFriend
     */
    public int getIsRecommendByFriend() {
        return IsRecommendByFriend;
    }

    /**
     * @param IsRecommendByFriend the IsRecommendByFriend to set
     */
    public void setIsRecommendByFriend(int IsRecommendByFriend) {
        this.IsRecommendByFriend = IsRecommendByFriend;
    }

}
