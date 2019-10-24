/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.Material;
import cn.hanbell.wco.entity.WeChatUser;
import com.lightshell.wx.WeChatUtil;
import java.io.IOException;
import java.util.Iterator;
import javax.ejb.EJB;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kevindong
 */
public abstract class WeChatPubBean extends WeChatUtil {

    @EJB
    protected MaterialBean materialBean;
    @EJB
    protected WeChatUserBean openUserBean;

    public WeChatPubBean() {

    }

    @Override
    protected String getAppToken() {
        return "shgxxx";
    }

    public String createMemu() {
        setAccessToken();
        if (accessToken != null) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=");
            urlBuilder.append(accessToken);

            StringBuilder jsonString = new StringBuilder();
            jsonString.append("{'button':[{'name':'公司简介','sub_button':[");
            jsonString.append("{'type':'view','name':'关于我们','url':'https://mp.weixin.qq.com/mp/homepage?__biz=MzI5NzI3MDY5Mw==&hid=1&sn=9c086bbfe7da65be77c5205c5341233f'}]},");
            jsonString.append("{'name':'扫码','sub_button':[");
            jsonString.append("{'type':'scancode_waitmsg','name': '扫码等待','key':'M_SCW_00'},");
            jsonString.append("{'type':'scancode_push','name': '扫码事件','key':'M_SCP_00'}]},");
            jsonString.append("{'name':'会员中心','sub_button':[");
            jsonString.append("{'type':'click','name':'关注我们','key':'M_GZMJ'},");
            jsonString.append("{'type':'click','name':'领取会员卡','key':'M_VIPCARD'}]}");
            jsonString.append("]}");

            JSONObject jsonObject = new JSONObject(jsonString.toString());
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");

            CloseableHttpResponse response = post(urlBuilder.toString(), stringEntity);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jo = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    response.close();
                    return jo.getString("errmsg");
                } catch (IOException | ParseException | JSONException ex) {
                    log4j.error(ex);
                }
            }
        }
        return "系统异常,操作失败";
    }

    @Override
    public void initConfiguration() {

    }

    public JSONObject pullCards(int offset, int count, String status) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/card/batchget?access_token=" + accessToken;

            StringBuilder jsonString = new StringBuilder();
            jsonString.append("{'offset':").append(offset).append(",'count':").append(count).append("}");

            JSONObject jop = new JSONObject(jsonString.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
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
        }
        return null;

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

    public JSONObject pullMedia(String media_id) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=" + accessToken;
            String jsonString = "{'media_id':" + media_id + "}";

            JSONObject jsonObject = new JSONObject(jsonString);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");

            CloseableHttpResponse response = post(urlString, stringEntity);
            try {
                HttpEntity httpEntity = response.getEntity();
                return new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
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

    public String sendCardToOpenId(String openid, String cardid) {
        return sendMsgToOpenId(openid, "wxcard", cardid);
    }

    public String sendMsgToOpenId(String openid, String msgType, String data) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;

            StringBuilder jsonString = new StringBuilder();
            switch (msgType) {
                case "text":
                    jsonString.append("{'touser':'").append(openid).append("','msgtype':'text','text':{'content':'").append(data).append("'}}");
                    break;
                case "image":
                    jsonString.append("{'touser':'").append(openid).append("','msgtype':'image','image':{'media_id':'").append(data).append("'}}");
                    break;
                case "voice":
                    jsonString.append("{'touser':'").append(openid).append("','msgtype':'voice','voice':{'media_id':'").append(data).append("'}}");
                    break;
                case "mpnews":
                    jsonString.append("{'touser':'").append(openid).append("','msgtype':'mpnews','mpnews':{'media_id':'").append(data).append("'}}");
                    break;
                case "wxcard":
                    jsonString.append("{'touser':'").append(openid).append("','msgtype':'wxcard','wxcard':{'card_id':'").append(data).append("'}}");
                    break;
            }
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

}
