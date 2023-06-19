/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import com.lightshell.wx.WeChatUtil;
import cn.hanbell.wco.entity.WeChatToken;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.ejb.EJB;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
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

    protected boolean isConfigured;

    final public String MEDIA_IMG = "image";
    final public String MEDIA_VOICE = "voice";
    final public String MEDIA_VIDEO = "video";
    final public String MEDIA_FILE = "file";

    public WeChatCorpBean() {

    }

    public String createDepartment(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor;
                    jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
                    }
                } catch (ParseException | JSONException | IOException ex) {
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

    public String createEmployee(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String createWeChatTag(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/tag/create?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String createWeChatTagUser(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/tag/addtagusers?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String deleteDepartment(int id) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=" + access_token
                    + "&id=" + id;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String deleteEmployee(String userid) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=" + access_token
                    + "&userid=" + userid;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String deleteWeChatTag(int tagid) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/tag/delete?access_token=" + access_token + "&tagid="
                    + tagid;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String deleteWeChatTagUser(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/tag/deltagusers?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    protected WeChatToken getWeChatToken(String app) {
        if (tokenList == null || tokenList.isEmpty()) {
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
                JSONObject resultJsonObject = new JSONObject(EntityUtils.toString(entity, WeChatUtil.CHARSET));
                accessToken = resultJsonObject.getString("access_token");
                expiresIn = Integer.parseInt(resultJsonObject.get("expires_in").toString());
                expiresDate.add(Calendar.SECOND, expiresIn);
                response.close();
                addAccessToken(corpid, corpsecret, accessToken, expiresIn, expiresDate);
            } catch (IOException | ParseException | JSONException ex) {
                log4j.error(ex);
            }
        }
    }

    @Deprecated
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
                    default :
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
        return sendMsgToUser(jsonString.toString(), true);
    }

    /**
     * 发送应用消息
     *
     * @param touser
     * @param toparty
     * @param totag
     * @param msgtype
     * @param data 按不同消息类型构建的消息主体
     * @return
     */
    public String sendMsgToUser(String touser, String toparty, String totag, String msgtype, String data) {
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("{");
        jsonString.append("'touser':'").append(touser).append("',");
        if (toparty != null && !"".equals(toparty)) {
            jsonString.append("'toparty':'").append(toparty).append("',");
        }
        if (totag != null && !"".equals(totag)) {
            jsonString.append("'totag':'").append(totag).append("',");
        }
        jsonString.append("'msgtype':'").append(msgtype).append("',");
        jsonString.append("'agentid':").append(this.agentId).append(",");
        jsonString.append("'").append(msgtype).append("':").append(data).append(",'enable_duplicate_check':0");
        jsonString.append("}");

        return sendMsgToUser(jsonString.toString(), true);
    }

    /**
     * 发送应用消息
     *
     * @param msg 消息内容
     * @param convertJSON 是否把msg转为JSONObject
     * @return
     */
    public String sendMsgToUser(String msg, boolean convertJSON) {
        setAccessToken(this.getAppID(), this.getAppSecret());
        String access_token = getAccessToken(this.getAppID(), this.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + access_token;
            CloseableHttpResponse response;
            if (convertJSON) {
                JSONObject reqObject = new JSONObject(msg);
                response = post(urlString, initStringEntity(reqObject.toString()));
            } else {
                response = post(urlString, initStringEntity(msg));
            }
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject resObject = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    return resObject.getString("errmsg");
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

    public String updateDepartment(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String updateEmployee(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    public String updateWeChatTag(JsonObject jo) {
        currentToken = this.getWeChatToken("org");
        if (currentToken == null) {
            return "Token参数异常";
        }
        this.setAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        String access_token = getAccessToken(currentToken.getAppId(), currentToken.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/tag/update?access_token=" + access_token;
            CloseableHttpResponse response = post(urlString, initStringEntity(jo.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return "success";
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    /**
     * 获取部门下的人员（非详细数据）
     *
     * @param department_id 部门ID
     * @param fetch_child 是否递归部门人员，0：否 1：是
     * @return
     */
    public JSONObject getWeChatUser(String department_id, String fetch_child) {
        WeChatToken t = this.getWeChatToken("org");
        if (t == null) {
            return null;
        }
        this.setAccessToken(t.getAppId(), t.getAppSecret());
        String access_token = getAccessToken(t.getAppId(), t.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            StringBuffer url = new StringBuffer("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=");
            url.append(access_token).append("&department_id=");
            url.append(department_id).append("&fetch_child=").append(fetch_child);
            CloseableHttpResponse response = get(url.toString(), null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    // log4j.info(resObject.getString("errmsg"));
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
     * 
     * @param userid
     * @return 
     */
      public JSONObject getQyWeChatUser(String userid) {
        WeChatToken t = this.getWeChatToken("org");
        if (t == null) {
            return null;
        }
        this.setAccessToken(t.getAppId(), t.getAppSecret());
        String access_token = getAccessToken(t.getAppId(), t.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            StringBuffer url = new StringBuffer("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=");
            url.append(access_token).append("&userid=");
            url.append(userid);
            CloseableHttpResponse response = get(url.toString(), null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
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

    public String updateApplication() {
        setAccessToken(this.getAppID(), this.getAppSecret());
        String access_token = getAccessToken(this.getAppID(), this.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/agent/set_workbench_template?access_token="
                    + access_token;
            // 构建消息
            StringBuilder jsonString = new StringBuilder(
                    "{'agentid':1000003,'type':'keydata','keydata':{ 'items':[{ 'key':'公司制度','data':'2','jump_url':'','pagepath':'pages/index'},{ 'key':'疫情规定','data':'4','jump_url':'http://www.qq.com','pagepath':'pages/index'},{'key':'祝福消息','data':'45','jump_url':'http://www.qq.com','pagepath':'pages/index'},{'key':'培训消息','data':'98','jump_url':'http://www.qq.com','pagepath':'pages/index'}]},'replace_user_data':true}");

            JSONObject jop = new JSONObject(jsonString.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
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

    public String getUserIdByCode(String code) {
        setAccessToken(this.getAppID(), this.getAppSecret());
        String access_token = getAccessToken(this.getAppID(), this.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + access_token
                    + "&code=" + code;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, WeChatUtil.CHARSET));
                    int errcode = jor.getInt("errcode");
                    if (errcode == 0) {
                        return jor.getString("UserId");
                    } else {
                        return jor.getString("errmsg");
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
        return "系统异常操作失败";
    }

    /**
     *
     * @param type 上传临时素材的类型
     * @param fileUrl 本地文件地址
     * @return JSONObject
     */
    public JSONObject uploadTempMaterial(String type, String fileUrl) throws IOException {

        // 1.创建本地文件
        File file = new File(fileUrl);
        //2.拼接请求url
        setAccessToken(this.getAppID(), this.getAppSecret());
        String access_token = getAccessToken(this.getAppID(), this.getAppSecret());
        StringBuffer uploadTempMaterial_url = new StringBuffer("https://qyapi.weixin.qq.com/cgi-bin/media/upload");
        uploadTempMaterial_url.append("?access_token=").append(access_token);
        uploadTempMaterial_url.append("&type=").append(type);

        //3.调用接口，发送请求，上传文件到微信服务器
        String result = httpRequest(uploadTempMaterial_url.toString(), file);

        //4.json字符串转对象：解析返回值，json反序列化
        result = result.replaceAll("[\\\\]", "");
        System.out.println("result:" + result);
//        JSONObject resultJSON ;
        JSONObject resultJSON = new JSONObject(result);
//        JSONObject resultJSON = null;
        //5.返回参数判断
        if (resultJSON != null) {
            if (resultJSON.get("media_id") != null) {
                System.out.println("上传" + type + "永久素材成功");
                return resultJSON;
            } else {
                System.out.println("上传" + type + "永久素材失败");
            }
        }
        return null;
    }

    public String httpRequest(String requestUrl, File file) {
        StringBuffer buffer = new StringBuffer();

        try {
            //1.建立连接
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  //打开链接

            //1.1输入输出设置
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setUseCaches(false); // post方式不能使用缓存
            //1.2设置请求头信息
            httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("Charset", "UTF-8");
            //1.3设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            // 请求正文信息
            // 第一部分：
            //2.将文件头输出到微信服务器
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length()
                    + "\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());
            // 将表头写入输出流中：输出表头
            outputStream.write(head);

            //3.将文件正文部分输出到微信服务器
            // 把文件以流文件的方式 写入到微信服务器中
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            in.close();
            //4.将结尾部分输出到微信服务器
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            outputStream.write(foot);
            outputStream.flush();
            outputStream.close();

            //5.将微信服务器返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源  
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        return buffer.toString();
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

    /**
     * @return the agentId
     */
    public int getAgentId() {
        return agentId;
    }
}
