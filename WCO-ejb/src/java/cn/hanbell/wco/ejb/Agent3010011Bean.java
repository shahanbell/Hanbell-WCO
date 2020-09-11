/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.ConfigProperties;
import cn.hanbell.wco.entity.WeChatToken;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 打卡应用
 *
 * @author C2082
 */
@Startup
@Singleton
public class Agent3010011Bean extends WeChatCorpBean {

    @EJB
    private ConfigPropertiesBean configPropertiesBean;

    public Agent3010011Bean() {
        agentId = 100002;
    }

    @Override
    public void initConfiguration() {
        this.agentId = Integer.valueOf(configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent3010011Bean.agentId").getConfigvalue());
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
        ConfigProperties cp = configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent3010011Bean.appld");
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

    /**
     * 获取打卡记录
     *
     * @param opencheckindatatype 打卡类型。1：上下班打卡；2：外出打卡；3：全部打卡
     * @param starttime 获取打卡记录的开始时间。Unix时间戳
     * @param endtime 获取打卡记录的结束时间。Unix时间戳
     * @param users 需要获取打卡记录的用户列表
     * @return
     */
    public JSONObject getPunchCardRecord(int opencheckindatatype, long starttime, long endtime, List<String> users) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("opencheckindatatype", opencheckindatatype);
        job.add("starttime", starttime);
        job.add("endtime", endtime);
        JsonArrayBuilder attrs = Json.createArrayBuilder();
        for (String u : users) {
            attrs.add(u);
        }
        job.add("useridlist", attrs);
        setAccessToken(this.getAppID(), this.getAppSecret());
        String access_token = getAccessToken(this.getAppID(), this.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=" + access_token;
            JsonObject jo = job.build();
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return jor;
                    } else {
                        return null;
                    }
                } catch (IOException | ParseException | JSONException ex) {
                    log4j.error(ex);
                    return null;
                } finally {
                    try {
                        response.close();
                    } catch (IOException ex) {
                        log4j.error(ex);
                    }
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * 同步企业微信打卡记录到本地数据库（EAP）中
     *
     * @param array 打卡记录
     * @return
     */
    public boolean syncPunchCardRecordToEap(JSONArray array) {
        JsonObjectBuilder records = Json.createObjectBuilder();
        JsonArrayBuilder attrs = Json.createArrayBuilder();
        JsonObjectBuilder record = null;
        for (int i = 0; i < array.length(); i++) {
            record = Json.createObjectBuilder();
            record.add("userid", array.getJSONObject(i).getString("userid"));
            record.add("groupname", array.getJSONObject(i).getString("groupname"));
            record.add("checkintype", array.getJSONObject(i).getString("checkin_type"));
            record.add("exceptiontype", array.getJSONObject(i).getString("exception_type"));
            record.add("checkintime", String.valueOf(array.getJSONObject(i).getLong("checkin_time")));
            record.add("locationtitle", array.getJSONObject(i).getString("location_title"));
            record.add("locationdetail", array.getJSONObject(i).getString("location_detail"));
            record.add("wifiname", array.getJSONObject(i).getString("wifiname"));
            record.add("notes", array.getJSONObject(i).getString("notes"));
            record.add("wifimac", array.getJSONObject(i).getString("wifimac"));
            if (array.getJSONObject(i).has("lat")) {
                record.add("lat", String.valueOf(array.getJSONObject(i).getLong("lat")));
            } else {
                record.add("lat", "0");
            }
            if (array.getJSONObject(i).has("lng")) {
                record.add("lng",String.valueOf(array.getJSONObject(i).getLong("lng")));
            } else {
                record.add("lng", "0");
            }
            JSONArray mediaids = array.getJSONObject(i).getJSONArray("mediaids");
           StringBuffer ms=new StringBuffer("[");
            for (int m = 0; m < mediaids.length(); m++) {
               ms.append("'").append(mediaids.getString(m)).append("',");
            }
            ms.append("]");
            record.add("mediaids", ms.toString());
            attrs.add(record);
        }
        records.add("records", attrs);
        if (records != null) {
            StringBuffer url = new StringBuffer(configPropertiesBean.findByKey("cn.hanbell.wco.ejb.Agent3010011Bean.getPunchCardRecord.url").getConfigvalue());
            JsonObject jo = records.build();
            CloseableHttpResponse response = post(url.toString(), initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    String code = jor.getString("code");
                    if ("200".equals(code)) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException | ParseException | JSONException ex) {
                    log4j.error(ex);
                    return false;
                } finally {
                    try {
                        response.close();
                    } catch (IOException ex) {
                        log4j.error(ex);
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
