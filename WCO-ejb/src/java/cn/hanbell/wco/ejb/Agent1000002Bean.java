/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.ConfigProperties;
import cn.hanbell.wco.entity.WeChatToken;
import cn.hanbell.wco.entity.WechatMessage;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author kevindong
 */
@Startup
@Singleton
public class Agent1000002Bean extends WeChatCorpBean {

    @EJB
    private ConfigPropertiesBean configPropertiesBean;
    @EJB
    private WechatMessageBean wechatmessageBean;

    private String currentUserid;

    public Agent1000002Bean() {
        agentId = 100002;
    }

    @Override
    public void initConfiguration() {
        this.agentId = Integer.valueOf(configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000002Bean.agentId").getConfigvalue());
        WeChatToken token = getWeChatToken(String.valueOf(getAgentId()));
        if (token != null && !isConfigured) {
            // 单例只需初始化一次
            initWeChatCrypt(token.getAppASEKey());
            this.appSecret = token.getAppSecret();
            this.appToken = token.getAppToken();
            this.isConfigured = true;
        }
    }

    @Override
    public String getAppID() {
        ConfigProperties cp = configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000002Bean.appld");
        return cp.getConfigvalue();
    }

    @Override
    public String getAppSecret() {
        return this.appSecret;
    }

    @Override
    protected String getAppToken() {
        return this.appToken;
    }

    public String getConfiguration(String name) {
        try {
            return configPropertiesBean.findByKey(name).getConfigvalue();
        } catch (Exception ex) {
            return "";
        }
    }

    @Override
    public String sendMsgToUser(String userid, String msgType, String data) {
        StringBuilder jsonString = new StringBuilder();
        switch (msgType) {
            case "text":
                jsonString.append("{'touser':'").append(userid).append("','msgtype':'text','agentid':")
                        .append(this.agentId).append(",'text':{'content':'").append(data).append("'},'safe':0}");
                break;
            case "image":
                jsonString.append("{'touser':'").append(userid).append("','msgtype':'image','image':{'media_id':'")
                        .append(data).append("'}}");
                break;
            case "voice":
                jsonString.append("{'touser':'").append(userid).append("','msgtype':'voice','voice':{'media_id':'")
                        .append(data).append("'}}");
                break;
            case "news":
                jsonString.append("{'touser':'").append(userid).append("','msgtype':'news','agentid':")
                        .append(this.agentId).append(",'news':{'articles':[").append(data).append("]}}");
                break;
            case "mpnews":
                jsonString.append("{'touser':'").append(userid).append("','msgtype':'mpnews','mpnews':{'media_id':'")
                        .append(data).append("'}}");
                break;
            case "wxcard":
                jsonString.append("{'touser':'").append(userid).append("','msgtype':'wxcard','wxcard':{'card_id':'")
                        .append(data).append("'}}");
                break;
            case "markdown":
                jsonString.append("{'touser':'").append(userid).append("','msgtype':'markdown','agentid':")
                        .append(this.agentId).append(",'markdown':{'content':'").append(data).append("'},'safe':0}");
                break;
            case "taskcard":
                // 截取data中的数据查看taskid的前缀,判断发送的是哪个回执
                String taskid = data.substring(data.indexOf("'task_id':'") + 11, data.indexOf("'task_id':'") + 15);
                switch (taskid) {
                    // 薪资回执
                    case "XZHZ":
                        // 传过来的是userid在薪资回执中为部门ID，通过部门去发放
                        jsonString.append("{'touser':'").append(userid);
                        jsonString.append("','toparty':'");
                        jsonString.append("','totag':'").append("");
                        jsonString.append("','msgtype':'").append("taskcard");
                        jsonString.append("','agentid':'").append(this.agentId).append("',");
                        jsonString.append(data);
                        jsonString.append("'enable_id_trans':").append(0);
                        jsonString.append(",'enable_duplicate_check':").append(0);
                        jsonString.append(",'duplicate_check_interval':").append(1800);
                        jsonString.append("}");
                        break;
                    // 人事异动单
                    case "RSYD":
                        jsonString.append("{'touser':'").append(userid);
                        jsonString.append("','toparty':'");
                        jsonString.append("','totag':'").append("");
                        jsonString.append("','msgtype':'").append("taskcard");
                        jsonString.append("','agentid':'").append(this.agentId).append("',");
                        jsonString.append(data);
                        jsonString.append("'enable_id_trans':").append(0);
                        jsonString.append(",'enable_duplicate_check':").append(0);
                        jsonString.append(",'duplicate_check_interval':").append(1800);
                        jsonString.append("}");
                        break;
                    case "GRJC":
                        jsonString.append("{'touser':'").append(userid);
                        jsonString.append("','toparty':'");
                        jsonString.append("','totag':'").append("");
                        jsonString.append("','msgtype':'").append("taskcard");
                        jsonString.append("','agentid':'").append(this.agentId).append("',");
                        jsonString.append(data);
                        jsonString.append("'enable_id_trans':").append(0);
                        jsonString.append(",'enable_duplicate_check':").append(0);
                        jsonString.append(",'duplicate_check_interval':").append(1800);
                        jsonString.append("}");
                        break;
                    default:
                        jsonString.append("{'touser':'").append(userid);
                        jsonString.append("','toparty':'");
                        jsonString.append("','totag':'").append("");
                        jsonString.append("','msgtype':'").append("taskcard");
                        jsonString.append("','agentid':'").append(this.agentId).append("',");
                        jsonString.append(data);
                        jsonString.append("'enable_id_trans':").append(0);
                        jsonString.append(",'enable_duplicate_check':").append(0);
                        jsonString.append(",'duplicate_check_interval':").append(1800);
                        jsonString.append("}");
                        break;
                }
                break;
        }
        String msg = sendMsgToUser(jsonString.toString(), true);
        WechatMessage msgentity = new WechatMessage(userid, jsonString.toString(), this.currentUserid==null ? "mis" :this.currentUserid);
        msgentity.setResponecontent(msg);
        this.wechatmessageBean.persist(msgentity);
        return msg;
    }

    public void setCurrentUserid(String currentUserid) {
        this.currentUserid = currentUserid;
    }

}
