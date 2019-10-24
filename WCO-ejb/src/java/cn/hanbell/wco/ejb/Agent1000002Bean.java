/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.ejb;

import cn.hanbell.wco.entity.OperationStatus;
import cn.hanbell.wco.entity.OperationTask;
import cn.hanbell.wco.entity.WeChatToken;
import com.lightshell.comm.BaseLib;
import java.io.IOException;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Named;
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
@Startup
@Singleton
@Named
public class Agent1000002Bean extends WeChatCorpBean {

    @EJB
    protected OperationStatusBean operationStatusBean;
    @EJB
    protected OperationTaskBean operationTaskBean;

    public Agent1000002Bean() {

    }

    @Override
    public void initConfiguration() {
        agentId = 1000002;
        WeChatToken token = getWeChatToken(String.valueOf(agentId));
        if (token != null) {
            initWeChatCrypt(token.getAppASEKey());
            this.appSecret = token.getAppSecret();
            this.appToken = token.getAppToken();
        }
    }

    public String getOperationContainer(int agentId, String userId) {
        OperationStatus op = operationStatusBean.findByAgentIdAndUserId(agentId, userId);
        if (op == null) {
            return "没有设置工作状态";
        } else {
            return "Process:" + op.getProcess() + ",Unit:" + op.getUnit();
        }
    }

    public String getOperationContext(int agentId, String userId) {
        OperationStatus op = operationStatusBean.findByAgentIdAndUserId(agentId, userId);
        if (op == null) {
            return "请先设置工作状态";
        } else {
            if (op.getFormid() != null && !"".equals(op.getFormid())) {
                return "存在待确认工作状态,不能继续";
            }
            if (op.getCheckCode() != null && !"".equals(op.getCheckCode())) {
                return "存在待确认工作状态,不能继续";
            }
            return "";
        }
    }

    public OperationStatus getOperationStatus(String taskId) {
        return operationStatusBean.findByCheckCode(taskId);
    }

    public boolean updateOperationContainer(int agentId, String userId, String process, String unit) {
        OperationStatus op = operationStatusBean.findByAgentIdAndUserId(agentId, userId);
        try {
            if (op == null) {
                op = new OperationStatus();
                op.setAgentId(agentId);
                op.setUserId(userId);
                op.setProcess(process);
                op.setUnit(unit);
                op.setStatus("N");
                op.setCreatorToSystem();
                op.setCredateToNow();
                operationStatusBean.persist(op);
            } else {
                op.setProcess(process);
                op.setUnit(unit);
                op.setOptuserToSystem();
                op.setOptdateToNow();
                operationStatusBean.update(op);
            }
            return true;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return false;
    }

    public boolean updateOperationContext(int agentId, String userId, String formId, String sn, BigDecimal value, String check) {
        OperationStatus op = operationStatusBean.findByAgentIdAndUserId(agentId, userId);
        if (op == null) {
            return false;
        }
        try {
            op.setFormid(formId);
            op.setSn(sn);
            op.setQty(value);
            op.setCheckCode(check);
            operationStatusBean.update(op);
            return true;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return false;
    }

    public String resetOperationContext(int agentId, String userId) {
        OperationStatus p = operationStatusBean.findByAgentIdAndUserId(agentId, userId);
        if (p == null) {
            return "重置工作状态失败，未找到信息";
        } else {
            try {
                p.setFormid(null);
                p.setSn(null);
                p.setQty(BigDecimal.ZERO);
                p.setCheckCode(null);
                operationStatusBean.update(p);
                return "重置工作状态成功";
            } catch (Exception ex) {
                return "重置工作状态失败" + ex.getMessage();
            }
        }
    }

    public String resetOperationContext(String taskId) {
        OperationStatus p = getOperationStatus(taskId);
        if (p == null) {
            return "重置工作状态失败，未找到信息";
        } else {
            try {
                p.setFormid(null);
                p.setSn(null);
                p.setQty(BigDecimal.ZERO);
                p.setCheckCode(null);
                operationStatusBean.update(p);
                return "重置工作状态成功";
            } catch (Exception ex) {
                return "重置工作状态失败" + ex.getMessage();
            }
        }
    }

    public String sendOperationConfirmToUser(int agentid, String userid, String title, String description, String taskid) {
        setAccessToken();
        if (accessToken != null) {
            String urlString = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
            //构建消息
            StringBuilder jsonString = new StringBuilder();
            jsonString.append("{'touser':'").append(userid).append("','msgtype':'taskcard','agentid':").append(agentid);
            jsonString.append(",'taskcard':{'title':'").append(title).append("','description':'").append(description).append("'");
            jsonString.append(",'task_id':'").append(taskid).append("'");
            jsonString.append(",'btn':");
            jsonString.append("[{'key':'MN_SFC_OK','name':'OK'},{'key':'MN_SFC_NO','name':'NO'}]");
            jsonString.append("}}");
            //转JSON对象
            JSONObject jop = new JSONObject(jsonString.toString());
            CloseableHttpResponse response = post(urlString, initStringEntity(jop.toString()));
            if (response != null) {
                HttpEntity httpEntity = response.getEntity();
                try {
                    JSONObject jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    log4j.info("sendOperationConfirmToUser" + taskid + jor.getString("errmsg"));
                    return jor.getString("errmsg");
                } catch (IOException | ParseException | JSONException ex) {
                    log4j.error("sendOperationConfirmToUser" + taskid + ex);
                } finally {
                    try {
                        response.close();
                    } catch (IOException ex) {
                        log4j.error(ex);
                    }
                }
            } else {
                return "sendOperationConfirmToUser" + taskid + "平台未响应";
            }
        }
        return "sendOperationConfirmToUser获取Token失败";
    }

    public String updateOperationTask(String taskId) {
        OperationStatus p = getOperationStatus(taskId);
        if (p == null) {
            return "建立工作任务失败，未找到任务";
        } else {
            try {
                OperationTask t = new OperationTask();
                t.setAgentId(p.getAgentId());
                t.setUserId(p.getUserId());
                t.setTaskId(taskId);
                t.setCreateTime(BaseLib.getDate().getTime());
                t.setProcess(p.getProcess());
                t.setUnit(p.getUnit());
                t.setFormid(p.getFormid());
                t.setSn(p.getSn());
                t.setQty(p.getQty());
                t.setStatus("N");
                operationTaskBean.persist(t);
                p.setFormid(null);
                p.setQty(BigDecimal.ZERO);
                p.setCheckCode(null);
                operationStatusBean.update(p);
                return "建立工作任务成功<br>任务编号" + taskId;
            } catch (Exception ex) {
                return "建立工作任务失败" + ex.getMessage();
            }
        }
    }

    @Override
    public String getAppID() {
        return "ww7f3e1ce36d3bc75e";
    }

    @Override
    public String getAppSecret() {
        return this.appSecret;
    }

    @Override
    protected String getAppToken() {
        return this.appToken;
    }

}
