/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.eap.ejb.SalarySendBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SalarySend;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.app.MSGApplication;
import cn.hanbell.wco.app.SalaryReceiptApplication;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.ejb.Agent1000016Bean;
import cn.hanbell.wco.ejb.ConfigPropertiesBean;
import cn.hanbell.wco.ejb.Prg9f247ab6d5e4Bean;
import cn.hanbell.wco.entity.WeChatUser;
import com.lightshell.comm.BaseLib;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author C2082
 */
@Path("sendmsg/")
@javax.enterprise.context.RequestScoped
public class SendMsgFacadeREST extends WeChatOpenFacade<WeChatUser> {

    @EJB
    private Agent1000002Bean agent1000002Bean;

    @EJB
    private SalarySendBean salarySendBean;

    @EJB
    private SystemUserBean systemUserBean;

    public SendMsgFacadeREST() {
        super(WeChatUser.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @POST
    @Path("send")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage sendMessage(MSGApplication entity) {
        if (entity.getUserId() == null || entity.getMsg() == null || entity.getOpenid() == null || entity.getSessionkey() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            if (wechatSessionBean.has(entity.getOpenid(), entity.getSessionkey())) {
                agent1000002Bean.initConfiguration();
                String errmsg = agent1000002Bean.sendMsgToUser(entity.getUserId(), "text", entity.getMsg());
                return new ResponseMessage("200", errmsg);
            } else {
                return new ResponseMessage("401", "会话异常");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseMessage("500", "系统异常");
        }
    }

    @POST
    @Path("salaryreceipt")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage sendSalaryReceiptMessage(SalaryReceiptApplication entity) {
        try {
            if (entity == null) {
                return new ResponseMessage("501", "无效参数");
            }
            if (entity.getUserId() == null || "".equals(entity.getUserId()) || entity.getSalaryDate() == null) {
                return new ResponseMessage("501", "无效参数");
            }
            String user = entity.getUserId();
            String[] agrs = user.split("\\|");
            Date date = BaseLib.getDate("yyyy/MM/dd", entity.getSalaryDate());
            String taskid = salarySendBean.getEdwTaskId("XZHZEDW" + BaseLib.formatDate("yyyyMMdd", date));
            String dateString = BaseLib.formatDate("yyyyMM", date);
            StringBuffer userBuffer = new StringBuffer();

            for (int i = 0; i < agrs.length; i++) {
                JSONObject js = agent1000002Bean.getQyWeChatUser(agrs[i]);
                //临时工号,用户是否存在监控
                if (js == null || agrs[i].startsWith("CL") || agrs[i].startsWith("CM") || agrs[i].startsWith("CM")) {
                    return new ResponseMessage("502", "收件人不合法或存在临时工号");
                }
                //已经发送过的人员监控
                SalarySend salarySend = salarySendBean.findByTaskNameAndEmployeeid(dateString + "月薪资发放", agrs[i]);
                if (salarySend != null) {
                    return new ResponseMessage("503", agrs[i] + ",已发送。不能重复发送");
                }
                //人员重复监控
                if (user.replaceFirst(agrs[i], ";;").contains(agrs[i])) {
                    return new ResponseMessage("504", agrs[i] + ",工号重复");
                }
            }
            StringBuffer data = new StringBuffer();
            data.append("'taskcard':{");
            data.append("'title':'").append(dateString).append("期薪资发放回执'");
            data.append(",'description':'").append("感谢您一个月的辛勤耕耘。").append(dateString).append("期工资单已发出，请查收！<br>工资单已收到请点下方确认！谢谢！'");
            data.append(",'url':'").append("'");
            data.append(",'task_id':'").append(taskid).append("'");
            data.append(",'btn':[{");
            data.append("'key':'").append("confirm'");
            data.append(",'name':'").append("确认'");
            data.append(",'replace_name':'").append("已确认'");
            data.append(",'color':'").append("red'");
            data.append(",'is_bold':").append(true).append("}");
            data.append("]},");
            agent1000002Bean.initConfiguration();
            String msg = agent1000002Bean.sendMsgToUser(entity.getUserId(), "taskcard", data.toString());
            if ("ok".equals(msg)) {
                for (int i = 0; i < agrs.length; i++) {
                    SalarySend sa = new SalarySend();
                    SystemUser systemuser = systemUserBean.findByUserId(agrs[i]);
                    sa.setTaskid(taskid);
                    sa.setTaskname(dateString + "月薪资发放");
                    sa.setSendtime(BaseLib.getDate());
                    sa.setEmployeeid(systemuser.getUserid());
                    sa.setEmployeename(systemuser.getUsername());
                    sa.setDeptno(systemuser.getDept().getDeptno());
                    sa.setDept(systemuser.getDept().getDept());
                    sa.setStatus("N");
                    salarySendBean.persist(sa);
                }
            } else {
                return new ResponseMessage("505", msg);
            }
            return new ResponseMessage("200", "success");
        } catch (Exception e) {
            return new ResponseMessage("500", "系统错误");
        }
    }
}
