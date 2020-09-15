/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import java.io.IOException;
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
 * 服务部公众号的请求类
 *
 * @author C2082
 */
@Startup
@Singleton
public class ServicePubBean extends WeChatPubBean {

    @EJB
    private ConfigPropertiesBean configPropertiesBean;
    private String openId;
    @Override
    protected String getAppID() {
        return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.ServicePubBean.appld").getConfigvalue();
    }

    @Override
    protected String getAppSecret() {
         return configPropertiesBean.findByKey("cn.hanbell.wco.ejb.ServicePubBean.appSecret").getConfigvalue();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void initAccessTokenAndOpenId(String code) {
            getAppToken(code);    
    }

    public JSONObject getUserInfo() {
        StringBuffer url = new StringBuffer("https://api.weixin.qq.com/sns/userinfo?");
        url.append("access_token=").append(this.accessToken);
        url.append("&openid=").append(this.openId);
        url.append("&lang=").append("zh_CN");
        CloseableHttpResponse response = get(url.toString(), null, null);
        try {
            HttpEntity httpEntity = response.getEntity();
            JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
            return jor;

        } catch (IOException | ParseException | JSONException ex) {
            log4j.error(ex);
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                log4j.error(ex);
            }
        }
        return null;
    }

    /**
     * 通过code网页授权获取的access_token和openid 。与普通的acesstoken有区别
     *
     * @param code
     * @return
     */
    protected JSONObject getAppToken(String code) {
        StringBuffer url = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
        url.append("appid=").append(this.getAppID());
        url.append("&secret=").append(this.getAppSecret());
        url.append("&code=").append(code);
        url.append("&grant_type=").append("authorization_code");
        CloseableHttpResponse response = get(url.toString(), null, null);
        try {
            HttpEntity httpEntity = response.getEntity();
            JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
            setAccessToken(jor.getString("access_token"));
            setOpenId(jor.getString("openid"));
            return jor;

        } catch (IOException | ParseException | JSONException ex) {
            log4j.error(ex);
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                log4j.error(ex);
            }
        }
        return null;
    }

}
