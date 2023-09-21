/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.PersonnelChangeBean;
import cn.hanbell.eap.entity.PersonnelChange;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.lazy.PersonnelChangeModel;
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
@ManagedBean(name = "personnelChangeManagedBean")
@SessionScoped
public class PersonnelChangeManagedBean extends SuperQueryBean<PersonnelChange> {

    @EJB
    private Agent1000002Bean agent1000002Bean;
    private String employeeId;
    private String employeeName;
    private Date startDate;
    private Date endDate;
    private String status;
    private List<PersonnelChange> selectData;
    @EJB
    private PersonnelChangeBean personnelChangeBean;

    public PersonnelChangeManagedBean() {
        super(PersonnelChange.class);
    }

    @Override
    public void init() {
        this.setSuperEJB(this.personnelChangeBean);
        this.model = new PersonnelChangeModel(this.personnelChangeBean);
        super.init();
    }

    @Override
    public void query() {
        this.model = new PersonnelChangeModel(this.personnelChangeBean);
        if (this.model != null && this.model.getFilterFields() != null) {
            if (employeeId != null && !"".equals(employeeId)) {
                this.model.getFilterFields().put("employeeid", employeeId);
            }
            if (employeeName != null && !"".equals(employeeName)) {
                this.model.getFilterFields().put("employeename", employeeName);
            }
            if (startDate != null ) {
                this.model.getFilterFields().put("sendtimeBegin", startDate);
            }
             if (endDate != null ) {
                this.model.getFilterFields().put("sendtimeEnd", endDate);
            }
            if (status != null && !"All".equals(status)) {
                this.model.getFilterFields().put("status", status);
            }
        }
    }

    @Override
    public void reset() {
        this.setSuperEJB(this.personnelChangeBean);
        this.model = new PersonnelChangeModel(this.personnelChangeBean);
        employeeId="";
        employeeName = "";
        startDate=null;
        endDate=null;
        super.reset();

    }
  public void press() {
        agent1000002Bean.initConfiguration();
        for(PersonnelChange p:selectData){
            if("X".equals(p.getStatus())){
                StringBuffer msg = new StringBuffer("【上海汉钟】");
                msg.append(p.getTaskname()).append("已与").append(BaseLib.formatDate("yyyyMMdd", p.getSendtime()));
                msg.append("发出，您还未确认，请进入企业微信 系统消息 及时签收！谢谢！");
                String errmsg=agent1000002Bean.sendMsgToUser(p.getEmployeeid(), "text", msg.toString());
                if(errmsg.startsWith("ok")){
                 FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "发送成功"));
                }
            }
        }
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

    public PersonnelChangeBean getPersonnelChangeBean() {
        return personnelChangeBean;
    }

    public void setPersonnelChangeBean(PersonnelChangeBean personnelChangeBean) {
        this.personnelChangeBean = personnelChangeBean;
    }
    public List<PersonnelChange> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<PersonnelChange> selectData) {
        this.selectData = selectData;
    }


}
