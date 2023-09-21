/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.RewardsPunishmentBean;
import cn.hanbell.eap.entity.RewardsPunishment;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.lazy.RewardsPunishmentModel;
import cn.hanbell.wco.web.SuperQueryBean;
import com.lightshell.comm.BaseLib;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "rewardspunishmentManagedBean")
@SessionScoped
public class RewardsPunishmentManagedBean extends SuperQueryBean<RewardsPunishment> {

    @EJB
    private Agent1000002Bean agent1000002Bean;
    private String employeeId;
    private String employeeName;
    private Date startDate;
    private Date endDate;
    private String status;
    private List<RewardsPunishment> selectData;
    @EJB
    private RewardsPunishmentBean rewardspunishmentBean;

    public RewardsPunishmentManagedBean() {
        super(RewardsPunishment.class);
    }

    @Override
    public void reset() {
        employeeId = "";
        employeeName = "";
        startDate = null;
        endDate = null;
        super.reset();
    }

    @Override
    public void query() {
        if (this.model != null && this.model.getFilterFields() != null) {
            this.model.getFilterFields().clear();
            if (employeeId != null && !"".equals(employeeId)) {
                this.model.getFilterFields().put("employeeid", employeeId);
            }
            if (employeeName != null && !"".equals(employeeName)) {
                this.model.getFilterFields().put("employeename", employeeName);
            }
            if (startDate != null) {
                this.model.getFilterFields().put("sendtimeBegin", startDate);
            }
            if (endDate != null) {
                this.model.getFilterFields().put("sendtimeEnd", endDate);
            }
            if (status != null && !"All".equals(status)) {
                this.model.getFilterFields().put("status", status);
            }
        }
    }

    public void press() {
        agent1000002Bean.initConfiguration();
        for (RewardsPunishment p : selectData) {
            if ("X".equals(p.getStatus())) {
                StringBuffer msg = new StringBuffer("【上海汉钟】");
                msg.append(p.getTaskname()).append("已于").append(BaseLib.formatDate("yyyyMMdd", p.getSendtime()));
                msg.append("发出，您还未确认，请进入企业微信 系统消息 及时签收！谢谢！");
                String errmsg = agent1000002Bean.sendMsgToUser(p.getEmployeeid(), "text", msg.toString());
                if (errmsg.startsWith("ok")) {
                    FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "发送成功"));
                }
            }
        }
    }

    @Override
    public void init() {
        this.setSuperEJB(this.rewardspunishmentBean);
        this.model = new RewardsPunishmentModel(this.rewardspunishmentBean);
        super.init();
    }

    @Override
    public void openDialog(String view) {
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        PrimeFaces.current().dialog().openDynamic(view, options, null);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RewardsPunishment> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<RewardsPunishment> selectData) {
        this.selectData = selectData;
    }

    public RewardsPunishmentBean getRewardspunishmentBean() {
        return rewardspunishmentBean;
    }

    public void setRewardspunishmentBean(RewardsPunishmentBean rewardspunishmentBean) {
        this.rewardspunishmentBean = rewardspunishmentBean;
    }
    
}
