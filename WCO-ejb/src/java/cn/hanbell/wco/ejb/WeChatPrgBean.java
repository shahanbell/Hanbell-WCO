/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.WeChatUser;
import java.io.IOException;
import javax.ejb.EJB;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kevindong
 */
public abstract class WeChatPrgBean extends WeChatPubBean {

    @EJB
    protected WeChatUserBean wechatUserBean;

    public WeChatPrgBean() {

    }

    public JSONObject getSessionInfo(String appid, String secret, String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/jscode2session");
        sb.append("?appid=").append(appid);
        sb.append("&secret=").append(secret);
        sb.append("&js_code=").append(code);
        sb.append("&grant_type=authorization_code");

        CloseableHttpResponse response = get(sb.toString(), null, null);
        if (response != null) {
            try {
                HttpEntity entity = response.getEntity();
                JSONObject jo = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                 boolean b = "0".equals(jo.get("errcode").toString());
                //查看当前状态码是否为0，状态码除了0之外的请求都是存在问题，须记录
                if (b == false) {
                    log4j.error("获取会话异常。" + jo.get("errcode") + ":" + jo.get("errmsg"));
                }
                if (!jo.has("errcode")) {
                    return jo;
                } else {
                    log4j.error(jo.get("errmsg").toString());
                }
            } catch (IOException | ParseException | JSONException ex) {
                log4j.error(ex);
            } finally {
                try {
                    response.close();
                } catch (IOException ex) {
                    log4j.error(ex);
                }
            }
        }
        return null;
    }

    @Override
    public String sendTemplateMsg(String openid, String templateid, String data) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
            StringBuilder jsonString = new StringBuilder();
            jsonString.append("{'touser':").append(openid).append(",'template_id':'").append(templateid).append("',");
            jsonString.append(data);
            jsonString.append("}");
            log4j.info(jsonString.toString());
            JSONObject jop = new JSONObject(jsonString.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    log4j.info(jor.getString("errmsg"));
                    return jor.getString("errmsg");
                } catch (IOException | ParseException | JSONException ex) {
                    log4j.error(ex);
                } finally {
                    try {
                        response.close();
                    } catch (IOException ex) {
                        log4j.error(ex);
                    }
                }
            }
        }
        return "系统异常操作失败";
    }

    public String sendSalaryPayNotification(String employeeId) {
        WeChatUser wcu = wechatUserBean.findByEmployeeId(employeeId);
        if (wcu != null) {
            StringBuilder data = new StringBuilder();
            data.append("'form_id': 'PAYSALARYS',")
                    .append("'data':{")
                    .append("'keyword1':{'value':'Hanbell'},")
                    .append("'keyword2':{'value':'2019-07'},")
                    .append("'keyword3':{'value':3000.00},")
                    .append("'keyword4':{'value':3200.00},")
                    .append("'keyword5':{'value':400.00},")
                    .append("'keyword6':{'value':0.00},")
                    .append("'keyword7':{'value':6600.00},")
                    .append("'keyword8':{'value':6000.00},")
                    .append("'keyword9':{'value':'2019-08-06'}")
                    .append("}");
            return sendTemplateMsg(wcu.getOpenId(), "28xLD9Rf86F2PQZbaiV9IdG3hwZegapTsfOzY58Z7mc", data.toString());
        }
        return "";
    }

}
