/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.ejb.RewardsPunishmentBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.RewardsPunishment;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import com.lightshell.comm.BaseLib;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "addPersonnelChangeAddManagedBean")
@SessionScoped
public class RewardsPunishmentAddManagedBean implements Serializable {

    @EJB
    private RewardsPunishmentBean rewardspunishmentBean;
    @EJB
    private DepartmentBean departmentBean;
    @EJB
    private Agent1000002Bean agent1000002Bean;
    private Date contentTime;
    private String serialNumber;
    private String companyName;
    private String acceptPerson;
    private String acceptPersonDept;
    public void reset() {
        acceptPerson = "";
        acceptPersonDept = "";
        contentTime = new Date();
        serialNumber = "";
    }

    public void save() {
        if (acceptPerson != null && !"".equals(acceptPerson)) {
            String[] person = acceptPerson.split(";");
            agent1000002Bean.initConfiguration();
            for (int i = 0; i < person.length; i++) {
                String taskid = rewardspunishmentBean.getTaskId("GRJC" + BaseLib.formatDate("yyyyMM", contentTime));
                String[] user = person[i].split("-");
                RewardsPunishment p = new RewardsPunishment();
                p.setTaskid(taskid);
                String taskname = BaseLib.formatDate("yyyyMM", new Date()) + "个人奖/惩单";
                p.setTaskname(taskname);
                p.setSendtime(new Date());
                p.setEmployeeid(user[0]);
                p.setEmployeename(user[1]);
                String[] dept = acceptPersonDept.split(";")[i].split("-");
                p.setDept(dept[0]);
                p.setDept(dept[1]);
                p.setStatus("X");
                StringBuffer data = new StringBuffer();
                data.append("'taskcard':{");
                data.append("'title':'").append(companyName).append("'");
                data.append(",'description':'").append("本人同意").append(BaseLib.formatDate("yyyy年", contentTime)).append(serialNumber);
                data.append("号文之奖/惩决定。").append("<br>并予以确认。'");
                data.append(",'url':'").append("'");
                data.append(",'task_id':'").append(taskid).append("'");
                data.append(",'btn':[{");
                data.append("'key':'").append("confirm'");
                data.append(",'name':'").append("确认'");
                data.append(",'replace_name':'").append("已确认'");
                data.append(",'color':'").append("red'");
                data.append(",'is_bold':").append(true).append("}");
                data.append("]},");
                p.setContent(data.toString());
                if("ok".equals(agent1000002Bean.sendMsgToUser(p.getEmployeeid(), "taskcard", data.toString()))){
                    rewardspunishmentBean.hasExistPersist(p);
                }
            }

        }
        reset();
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    public void handleDialogReturnWhenDetailAllNew(SelectEvent event) {
        StringBuffer person;
        StringBuffer dept;
        if (this.acceptPerson == null) {
            person = new StringBuffer();
        } else {
            person = new StringBuffer(this.acceptPerson);
        }
        if (this.acceptPersonDept == null) {
            dept = new StringBuffer();
        } else {
            dept = new StringBuffer(this.acceptPersonDept);
        }
        try {
            if (event.getObject() != null) {
                List<SystemUser> userlist = (List<SystemUser>) event.getObject();
                userlist.forEach((user) -> {
                    person.append(user.getUserid()).append("-").append(user.getUsername()).append(";");
                    dept.append(user.getDeptno()).append("-").append(user.getDept().getDept()).append(";");
                });
                this.setAcceptPerson(person.toString());
                this.setAcceptPersonDept(dept.toString());
            }
        } catch (Exception e) {
        }
    }

    public void handleDialogReturnSysuserByDeptno(SelectEvent event) throws IOException {
        Department department = (Department) event.getObject();
        JSONObject jsono = agent1000002Bean.getWeChatUser(String.valueOf(department.getId()), "1");
        jsono.getInt("errcode");
        JSONArray list = jsono.getJSONArray("userlist");
        StringBuffer person;
        StringBuffer dept;
        if (this.acceptPerson == null) {
            person = new StringBuffer();
        } else {
            person = new StringBuffer(this.acceptPerson);
        }
        if (this.acceptPersonDept == null) {
            dept = new StringBuffer();
        } else {
            dept = new StringBuffer(this.acceptPersonDept);
        }
        for (int i = 0; i < list.length(); i++) {
            person.append(list.getJSONObject(i).get("userid")).append("-").append(list.getJSONObject(i).get("name")).append(";");
            int m = list.getJSONObject(i).getJSONArray("department").length() - 1;
            int deptno = list.getJSONObject(i).getJSONArray("department").getInt(m);
            Department d = departmentBean.findById(deptno);
            dept.append(d.getDeptno()).append("-").append(d.getDept()).append(";");
        }
        this.setAcceptPerson(person.toString());
        this.setAcceptPersonDept(dept.toString());
    }

    public void openDialog(String view) {
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        PrimeFaces.current().dialog().openDynamic(view, options, null);
    }

    public String getAcceptPerson() {
        return acceptPerson;
    }

    public void setAcceptPerson(String acceptPerson) {
        this.acceptPerson = acceptPerson;
    }

    public String getAcceptPersonDept() {
        return acceptPersonDept;
    }

    public void setAcceptPersonDept(String acceptPersonDept) {
        this.acceptPersonDept = acceptPersonDept;
    }

    public Date getContentTime() {
        return contentTime;
    }

    public void setContentTime(Date contentTime) {
        this.contentTime = contentTime;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
