/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.ConfigProperties;
import cn.hanbell.wco.entity.Temporarymaterial;
import cn.hanbell.wco.entity.WeChatToken;
import com.lightshell.comm.BaseLib;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author C2082
 */
@Startup
@Singleton
public class Agent1000016Bean extends WeChatCorpBean {

    @EJB
    private ConfigPropertiesBean configPropertiesBean;
    @EJB
    private TemporarymaterialBean temporarymaterialBean;

    public Agent1000016Bean() {
        agentId = 100002;
    }

    @Override
    public void initConfiguration() {
        this.agentId = Integer.valueOf(configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.agentId").getConfigvalue());
        WeChatToken token = getWeChatToken(String.valueOf(getAgentId()));
        if (token != null && !isConfigured) {
            // 单例只需初始化一次
            initWeChatCrypt(token.getAppASEKey());
            this.appSecret = token.getAppSecret();
//            this.appToken = token.getAppToken();
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

    public String getBirthdatPicteureUrl(String deptno) {
        String sub = deptno.substring(0, 1);
        if ("1".equals(sub) || "3".equals(sub) || "6".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.hanbellBirthdayPictureUrl").getConfigvalue();
        } else if ("2".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.hansonBirthdayPictureUrl").getConfigvalue();
        } else if ("5".equals(sub) || "8".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.comerBirthdayPictureUrl").getConfigvalue();
        } else if ("7".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.hanyoungBirthdayPictureUrl").getConfigvalue();
        }
        return null;
    }

    
    public String getWorkingAgePicteureUrl(String deptno) {
        String sub = deptno.substring(0, 1);
        if ("1".equals(sub) || "3".equals(sub) || "6".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.hanbellWorkPictureUrl").getConfigvalue();
        } else if ("2".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.hansonWorkPictureUrl").getConfigvalue();
        } else if ("5".equals(sub) || "8".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.comerWorkPictureUrl").getConfigvalue();
        } else if ("7".equals(sub)) {
            return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent1000016Bean.hanyoungWorkPictureUrl").getConfigvalue();
        }
        return null;
    }
    
    

    @Override
    public String sendMsgToUser(String userid, String msgType, String data) {
        setAccessToken(this.getAppID(), this.getAppSecret());
        String access_token = getAccessToken(this.getAppID(), this.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + access_token;
            //构建消息
            StringBuilder jsonString = new StringBuilder();
            switch (msgType) {
                case "text":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'text','agentid':").append(this.agentId).append(",'text':{'content':'").append(data).append("'},'safe':0}");
                    break;
                case "image":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'image','image':{'media_id':'").append(data).append("'}}");
                    break;
                case "voice":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'voice','voice':{'media_id':'").append(data).append("'}}");
                    break;
                case "news":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'news','agentid':").append(this.agentId).append(",'news':{'articles':[").append(data).append("]}}");
                    break;
                case "mpnews":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'mpnews','agentid':").append(this.agentId).append(",'mpnews':{'articles':[").append(data).append("]}}");
                    break;
                case "wxcard":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'wxcard','wxcard':{'card_id':'").append(data).append("'}}");
                    break;
                case "taskcard":
                    //截取data中的数据查看taskid的前缀,判断发送的是哪个回执
                    String taskid = data.substring(data.indexOf("'task_id':'") + 11, data.indexOf("'task_id':'") + 15);
                    switch (taskid) {
                        //薪资回执
                        case "XZHZ":
                            //传过来的是userid在薪资回执中为部门ID，通过部门去发放
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
                        //人事异动单
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
                    }
                    break;
            }
            //log4j.info(jsonString.toString());
            JSONObject jop = new JSONObject(jsonString.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    log4j.info(jor.getString("errmsg"));
                    return jor.getString("errmsg")+"|"+jor.getString("msgid");
                } catch (IOException | ParseException | JSONException ex) {
                    log4j.error(ex);
                } finally {
                    try {
                        response.close();
                    } catch (IOException ex) {
                        log4j.error(ex);
                    }
                }
            } else {
                return "平台未响应";
            }
        }
        return "系统异常操作失败";
    }

    public String getMaterialId(String type, String url) throws IOException {
        Temporarymaterial material = temporarymaterialBean.findByUrl(url);
        //如果超时或者为空的情况下，需要请求materialID
        if (material == null) {
            JSONObject jsob = uploadTempMaterial(type, url);
            if (jsob.getInt("errcode") == 0) {
                material = new Temporarymaterial();
                material.setMediaId(jsob.getString("media_id"));
                material.setUrl(url);
                material.setStatus("V");
                material.setCreator("mis");
                material.setCredate(BaseLib.getDate());
                material.setCfmdate(BaseLib.getDate());
                temporarymaterialBean.persist(material);
                return material.getMediaId();
            }
        }

        if (material != null && BaseLib.getDate().getTime() - material.getCfmdate().getTime() > 1000 * 60 * 60 * 24 * 2.5) {
            JSONObject jsob = uploadTempMaterial(type, url);
            if (jsob.getInt("errcode") == 0) {
                material.setMediaId(jsob.getString("media_id"));
                material.setCfmuser("mis");
                material.setCfmdate(BaseLib.getDate());
                temporarymaterialBean.update(material);
                return material.getMediaId();
            }
        }
        return material.getMediaId();
    }
}
