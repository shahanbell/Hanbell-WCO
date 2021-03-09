/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import com.lightshell.wx.WeChatUtil;
import cn.hanbell.wco.entity.WeChatToken;
import javax.ejb.EJB;
import javax.json.JsonObject;
import java.io.IOException;
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=" + access_token + "&id=" + id;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=" + access_token + "&userid=" + userid;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/tag/delete?access_token=" + access_token + "&tagid=" + tagid;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
                    jsonString.append("{'touser':'").append(userid).append("','msgtype':'mpnews','mpnews':{'media_id':'").append(data).append("'}}");
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
     * @param department_id 部门ID
     * @param fetch_child   是否递归部门人员，0：否   1：是
     * @return 
     */
    public JSONObject getWeChatUser(String department_id,String fetch_child){
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
            CloseableHttpResponse response = get(url.toString(), null,null);
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
                    return  null;
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
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/agent/set_workbench_template?access_token=" + access_token;
            //构建消息
            StringBuilder jsonString = new StringBuilder("{'agentid':1000003,'type':'keydata','keydata':{ 'items':[{ 'key':'公司制度','data':'2','jump_url':'','pagepath':'pages/index'},{ 'key':'疫情规定','data':'4','jump_url':'http://www.qq.com','pagepath':'pages/index'},{'key':'祝福消息','data':'45','jump_url':'http://www.qq.com','pagepath':'pages/index'},{'key':'培训消息','data':'98','jump_url':'http://www.qq.com','pagepath':'pages/index'}]},'replace_user_data':true}");

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
    
     public String getUserIdByCode(String code) {
        setAccessToken(this.getAppID(), this.getAppSecret());
        String access_token = getAccessToken(this.getAppID(), this.getAppSecret());
        if (access_token != null && !"".equals(access_token)) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + access_token + "&code=" + code;
            CloseableHttpResponse response = get(urlString, null, null);
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    //log4j.info(jor.getString("errmsg"));
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
