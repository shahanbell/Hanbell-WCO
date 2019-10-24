/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import com.lightshell.wx.WeChatUtil;
import cn.hanbell.wco.entity.Material;
import cn.hanbell.wco.entity.WeChatToken;
import cn.hanbell.wco.entity.WeChatUser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kevindong
 */
public abstract class WeChatCorpBean extends WeChatUtil {

    @EJB
    protected MaterialBean materialBean;
    @EJB
    protected WeChatTokenBean wechatTokenBean;
    @EJB
    protected WeChatUserBean openUserBean;

    protected String dataPath;
    protected String resPath;

    protected int agentId;
    protected List<WeChatToken> tokenList;
    protected WeChatToken currentToken;

    public WeChatCorpBean() {

    }

    public String createDepartment() {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + access_token;
            StringBuilder jsonData = new StringBuilder();
            jsonData.append("{");
            jsonData.append("'name':'策划部',");
            jsonData.append("'parentid':1");
            jsonData.append("}");

            JSONObject jop = new JSONObject(jsonData.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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

    public String updateDepartment() {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=" + access_token;
            StringBuilder jsonData = new StringBuilder();
            jsonData.append("{");
            jsonData.append("'id':6,");
            jsonData.append("'name':'客服部一部',");
            jsonData.append("'parentid':5");
            jsonData.append("}");

            JSONObject jop = new JSONObject(jsonData.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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

    protected WeChatToken getWeChatToken(String app) {
        if (tokenList == null) {
            tokenList = wechatTokenBean.findByAppId(getAppID());
        }
        for (WeChatToken e : tokenList) {
            if (e.getApp().equals(app)) {
                return e;
            }
        }
        return null;
    }

    @Override
    protected void initAccessToken(String corpid, String corpsecret) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://qyapi.weixin.qq.com/cgi-bin/gettoken?");
        sb.append("corpid=").append(corpid);
        sb.append("&");
        sb.append("corpsecret=").append(corpsecret);
        accessToken = null;
        expiresDate = null;
        CloseableHttpResponse response = get(sb.toString(), null, null);
        if (response != null) {
            expiresDate = Calendar.getInstance();
            HttpEntity entity = response.getEntity();
            try {
                JSONObject resultJsonObject = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                accessToken = resultJsonObject.getString("access_token");
                expiresIn = Integer.parseInt(resultJsonObject.get("expires_in").toString());
                expiresDate.add(Calendar.SECOND, expiresIn);
                response.close();
                addAccessToken(corpid, corpsecret, accessToken, expiresIn, expiresDate);
                //log4j.info("CorpAccessToken:" + accessToken);
            } catch (IOException | ParseException | JSONException ex) {
                log4j.error(ex);
            }
        }
    }

    public JSONObject pullMedias(String type, int offset, int count) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + accessToken;

            StringBuilder jsonString = new StringBuilder();
            jsonString.append("{'type':").append(type).append(",'offset':").append(offset).append(",'count':").append(count).append("}");

            JSONObject jop = new JSONObject(jsonString.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
            try {
                HttpEntity httpEntity = response.getEntity();
                JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                int n = Integer.parseInt(jor.get("item_count").toString());
                if (n > 0) {
                    Material m;
                    boolean isNew;
                    for (Iterator<Object> it = jor.getJSONArray("item").iterator(); it.hasNext();) {
                        JSONObject jsonItem = (JSONObject) it.next();
                        isNew = false;
                        m = materialBean.findByMediaId(jsonItem.getString("media_id"));
                        if (m == null) {
                            m = new Material();
                            m.setStatusToNew();
                            m.setCreatorToSystem();
                            m.setCredateToNow();
                            isNew = true;
                        }
                        m.setMediaId(jsonItem.getString("media_id"));
                        if ("news".equals(type)) {
                            m.setUpdateTime(jsonItem.getInt("update_time"));
                            JSONObject jsonNewsItem = jsonItem.getJSONObject("content").getJSONArray("news_item").getJSONObject(0);
                            m.setTitle(jsonNewsItem.getString("title"));
                            m.setAuthor(jsonNewsItem.getString("author"));
                            m.setContent(jsonNewsItem.getString("content"));
                            m.setUrl(jsonNewsItem.getString("url"));
                        } else {
                            m.setTitle(jsonItem.getString("name"));
                            m.setUrl(jsonItem.getString("url"));
                            m.setUpdateTime(jsonItem.getInt("update_time"));
                        }
                        if (isNew) {
                            materialBean.persist(m);
                        } else {
                            materialBean.update(m);
                        }
                    }
                    return jor;
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

    public void saveMedia(String media_id) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id=" + media_id;
            CloseableHttpResponse response = get(urlString, null, null);
            try {
                if (response != null) {
                    Header httpHeader = response.getFirstHeader("Content-disposition");
                    if (httpHeader != null) {
                        log4j.info(httpHeader.getValue());
                    }
                    HttpEntity httpEntity = response.getEntity();
                    if (httpEntity != null) {
                        InputStream inputStream = httpEntity.getContent();
                        FileUtils.copyInputStreamToFile(inputStream, new File(this.resPath + "Media" + media_id + ".jpeg"));
                    }
                    log4j.info("getMedia");
                }
            } catch (Exception ex) {
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

    public JSONObject pullUsers() {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken;

            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                try {
                    HttpEntity entity = response.getEntity();
                    JSONObject jo = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
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
        }
        return null;
    }

    public JSONObject pullUser(String openid) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid;
            //发起请求
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                try {
                    HttpEntity entity = response.getEntity();
                    JSONObject jor = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                    if (!jor.has("errcode")) {
                        return jor;
                    } else {
                        log4j.error(jor.get("errmsg").toString());
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
        }
        return null;
    }

    public boolean saveUsers() {
        JSONObject jo = pullUsers();
        if (jo != null) {
            try {
                JSONArray ja = jo.getJSONObject("data").getJSONArray("openid");
                for (int i = 0; i < ja.length(); i++) {
                    if (!openUserBean.has(ja.get(i).toString())) {
                        WeChatUser ou = new WeChatUser();
                        ou.setOpenId(ja.get(i).toString());
                        ou.setStatusToNew();
                        ou.setCreatorToSystem();
                        ou.setCredateToNow();
                        openUserBean.persist(ou);
                    }
                }
                for (int i = 0; i < ja.length(); i++) {
                    saveUser(ja.get(i).toString());
                }
                return true;
            } catch (Exception ex) {
                log4j.error(ex);
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean saveUser(String openid) {
        JSONObject jo = pullUser(openid);
        if (jo != null) {
            WeChatUser ou = openUserBean.findByOpenId(openid);
            if (ou != null) {
                ou.setSubscribe((jo.getInt("subscribe") == 1));
                ou.setNickname(jo.getString("nickname"));
                ou.setSex(jo.getInt("sex") == 1 ? "M" : "F");
                ou.setLanguage(jo.getString("language"));
                ou.setCity(jo.getString("city"));
                ou.setProvince(jo.getString("province"));
                ou.setCountry(jo.getString("country"));
                ou.setSubscribeTime(jo.getInt("subscribe_time"));
                ou.setUnionId(jo.getString("unionid"));
                ou.setRemark(jo.getString("remark"));
                try {
                    openUserBean.update(ou);
                    return true;
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
        }
        return false;
    }

    public String sendMsgToUser(int agentid, String userid, String msgType, String data) {
        setAccessToken(this.getAppID(), "VamHpPp_hvN6gehXFitmLGMjxqY8fgVv2xjcYiv8T1k");
        String access_token = getAccessToken(this.getAppID(), "VamHpPp_hvN6gehXFitmLGMjxqY8fgVv2xjcYiv8T1k");
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + access_token;
            //构建消息
            StringBuilder jsonString = new StringBuilder();
            switch (msgType) {
                case "text":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'text','agentid':").append(agentid).append(",'text':{'content':'").append(data).append("'},'safe':0}");
                    break;
                case "image":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'image','image':{'media_id':'").append(data).append("'}}");
                    break;
                case "voice":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'voice','voice':{'media_id':'").append(data).append("'}}");
                    break;
                case "mpnews":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'mpnews','mpnews':{'media_id':'").append(data).append("'}}");
                    break;
                case "wxcard":
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'wxcard','wxcard':{'card_id':'").append(data).append("'}}");
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
            } else {
                return "平台未响应";
            }
        }
        return "系统异常操作失败";
    }

    public String sendTemplateMsg(String openid) {
        StringBuilder jsonData = new StringBuilder();
        jsonData.append("'data':{");
        jsonData.append("'first':");
        jsonData.append("{'value':'亲，您好！我们已收到您的订单，并准备生产！'},");
        jsonData.append("'keyword1':");
        jsonData.append("{'value':'WX201805301400'},");
        jsonData.append("'keyword2':");
        jsonData.append("{'value':'古越龙山金5年'},");
        jsonData.append("'keyword3':");
        jsonData.append("{'value':'准备配送'},");
        jsonData.append("'keyword4':");
        jsonData.append("{'value':'2018年5月30日'},");
        jsonData.append("'remark':");
        jsonData.append("{'value':'需要一个URL链接查询配送进度'}");
        jsonData.append("}");
        return sendTemplateMsg(openid, "gihyOvlzCuLFZRO0VSEbiNtW9AN4_utNRS2lVq43R1w", jsonData.toString());
    }

    public String sendTemplateMsg(String openid, String templateid, String data) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;

            StringBuilder jsonString = new StringBuilder();
            jsonString.append("{'touser':").append(openid).append(",'template_id':'").append(templateid).append("',");
            jsonString.append(data);
            jsonString.append("}");
            log4j.info("sendTemplateMsg:" + jsonString.toString());
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

    /**
     * @return the dataPath
     */
    public String getDataPath() {
        return dataPath;
    }

    /**
     * @param dataPath the dataPath to set
     */
    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    /**
     * @return the resPath
     */
    public String getResPath() {
        return resPath;
    }

    /**
     * @param resPath the resPath to set
     */
    public void setResPath(String resPath) {
        this.resPath = resPath;
    }

}
