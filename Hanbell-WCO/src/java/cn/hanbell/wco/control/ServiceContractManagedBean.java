/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.ServiceContractBean;
import cn.hanbell.eap.entity.PersonnelChange;
import cn.hanbell.eap.entity.ServiceContract;
import cn.hanbell.eap.entity.SystemGrantPrg;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.lazy.ServiceContractModel;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "serviceContractManagedBean")
@SessionScoped
public class ServiceContractManagedBean extends SuperQueryBean<ServiceContract> {

    @EJB
    private Agent1000002Bean agent1000002Bean;
    private String employeeId;
    private String employeeName;
    private Date startDate;
    private Date endDate;
    private String status;
    private List<ServiceContract> selectData;
    private String sendUsers;
    @EJB
    private ServiceContractBean serviceContractBean;

    public ServiceContractManagedBean() {
        super(ServiceContract.class);
    }

    @Override
    public void init() {
        this.setSuperEJB(this.serviceContractBean);
        this.model = new ServiceContractModel(this.serviceContractBean);
        super.init();
    }

    @Override
    public void query() {
        this.model = new ServiceContractModel(this.serviceContractBean);
        if (this.model != null && this.model.getFilterFields() != null) {
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

    @Override
    public void reset() {
        this.setSuperEJB(this.serviceContractBean);
        this.model = new ServiceContractModel(this.serviceContractBean);
        employeeId = "";
        employeeName = "";
        sendUsers="";
        startDate = null;
        endDate = null;
        super.reset();
    }

    public void press() {
        agent1000002Bean.initConfiguration();
        for (ServiceContract p : selectData) {
            if ("X".equals(p.getStatus())) {
                StringBuffer msg = new StringBuffer("【上海汉钟】");
                //msg.append(p.getTaskname()).append("已与").append(BaseLib.formatDate("yyyyMMdd", p.getSendtime()));
                msg.append("电子劳动合同签收回执，您还未确认，请进入企业微信 系统消息 及时签收！谢谢！");
                String errmsg = agent1000002Bean.sendMsgToUser(p.getEmployeeid(), "text", msg.toString());
                if (errmsg.startsWith("ok")) {
                    FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "发送成功"));
                }
            }
        }
    }

    public void save() {
        if (sendUsers != null && !"".equals(sendUsers)) {
            String[] person = sendUsers.split(";");
            agent1000002Bean.initConfiguration();
            for (int i = 0; i < person.length; i++) {
                String formid = this.superEJB.getFormId(new Date(), this.currentPrgGrant.getSysprg().getNolead(), this.currentPrgGrant.getSysprg().getNoformat(), this.currentPrgGrant.getSysprg().getNoseqlen());
                StringBuffer data = new StringBuffer();
                data.append("'taskcard':{");
                data.append("'title':'电子劳动合同签收回执'");
                data.append(",'description':'电子劳动合同已收到请点下方确认，谢谢!'");
                data.append(",'url':'").append("'");
                data.append(",'task_id':'").append(formid).append("'");
                data.append(",'btn':[{");
                data.append("'key':'").append("confirm'");
                data.append(",'name':'").append("确认'");
                data.append(",'replace_name':'").append("已确认'");
                data.append(",'color':'").append("red'");
                data.append(",'is_bold':").append(true).append("}");
                data.append("]},");
                String[] user = person[i].split("-");
                this.newEntity.setFormid(formid);
                this.newEntity.setFormname(BaseLib.formatDate("yyyyMMdd劳动合同", new Date()));
                this.newEntity.setContent(data.toString());
                this.newEntity.setSendtime(new Date());
                this.newEntity.setEmployeeid(user[0]);
                this.newEntity.setEmployeename(user[1]);
                this.newEntity.setStatus("X");
                String msg = agent1000002Bean.sendMsgToUser(this.newEntity.getEmployeeid(), "taskcard", data.toString());
                if (msg.startsWith("ok")) {
                    this.persist();
                }
            }
        }
        reset();
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    @Override
    public void persist() {
        if (this.getNewEntity() != null) {
            try {
                if (this.doBeforePersist()) {
                    this.getSuperEJB().persist(this.getNewEntity());
                    this.doAfterPersist();
                    this.showInfoMsg("Info", "更新成功!");
                } else {
                    this.showWarnMsg("Warn", "更新前检查失败!");
                }
            } catch (Exception var2) {
                this.showErrorMsg("Error", var2.getMessage());
            }
        } else {
            this.showWarnMsg("Warn", "没有可更新数据!");
        }
    }

    public void handleDialogReturnWhenDetailAllNew(SelectEvent event) {
        StringBuffer person;
        if (this.sendUsers == null) {
            person = new StringBuffer();
        } else {
            person = new StringBuffer(this.sendUsers);
        }
        try {
            if (event.getObject() != null) {
                List<SystemUser> userlist = (List<SystemUser>) event.getObject();
                userlist.forEach((user) -> {
                    person.append(user.getUserid()).append("-").append(user.getUsername()).append(";");

                });
                this.setSendUsers(person.toString());
            }
        } catch (Exception e) {
        }
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

    public ServiceContractBean getServiceContractBean() {
        return serviceContractBean;
    }

    public void setServiceContractBean(ServiceContractBean serviceContractBean) {
        this.serviceContractBean = serviceContractBean;
    }

    public void setSelectData(List<ServiceContract> selectData) {
        this.selectData = selectData;
    }

    public List<ServiceContract> getSelectData() {
        return selectData;
    }

    public String getSendUsers() {
        return sendUsers;
    }

    public void setSendUsers(String sendUsers) {
        this.sendUsers = sendUsers;
    }

}
