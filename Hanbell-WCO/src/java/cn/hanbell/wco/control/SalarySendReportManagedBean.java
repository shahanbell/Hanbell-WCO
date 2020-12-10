/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.SalarySendBean;
import cn.hanbell.eap.entity.SalarySend;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.ejb.ConfigPropertiesBean;
import cn.hanbell.wco.lazy.SalarySendModel;
import cn.hanbell.wco.web.SuperQueryBean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "salarySendReportManagedBean")
@SessionScoped
public class SalarySendReportManagedBean extends SuperQueryBean<SalarySend> {

    @EJB
    private Agent1000002Bean agent1000002Bean;
    private String employeeName;
    private String status;
    private String employeeId;
    private Date date;
    private List<SalarySend> selectData;
    private Date changeFormlTime;
    private String acceptPerson;
    protected final Logger log4j = LogManager.getLogger("cn.hanbell.wco");
    @EJB
    private SalarySendBean salarySendBean;

    @EJB
    private ConfigPropertiesBean configPropertiesBean;

    public SalarySendReportManagedBean() {
        super(SalarySend.class);
    }

    @Override
    public void init() {
        employeeName = "";
        status = "All";
        employeeId = "";
        date = null;
        this.setSuperEJB(this.salarySendBean);
        this.model = new SalarySendModel(this.salarySendBean);
        super.init();
    }

    @Override
    public void query() {
        this.model = new SalarySendModel(this.salarySendBean);
        if (this.model != null && this.model.getFilterFields() != null) {
            if (employeeName != null && !"".equals(employeeName)) {
                this.model.getFilterFields().put("employeename", employeeName);
            }
            if (employeeId != null) {
                this.model.getFilterFields().put("employeeid", employeeId);
            }
            if (date != null) {
                this.model.getFilterFields().put("taskname", BaseLib.formatDate("YYYYMM", date));
            }
            if (!"All".equals(status)) {
                this.model.getFilterFields().put("status", status);
            }
        }
    }

    @Override
    public void reset() {
        this.setSuperEJB(this.salarySendBean);
        this.model = new SalarySendModel(this.salarySendBean);
        employeeName = "";
        employeeId = "";
        status = "All";
        date = null;
        super.reset();
    }

    //发送消息
    public void upload() {

        if (date == null) {
            FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "发送时间前必须筛选月份"));
            return;
        }
        if (this.selectData == null || this.selectData.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "请选择"));
            return;
        }
        StringBuffer toUsers = new StringBuffer("");
        for (SalarySend s : this.selectData) {
            if ("N".equals(s.getStatus())) {
                toUsers.append(s.getEmployeeid()).append("|");
            }
        }

        System.out.println("toUser=" + toUsers);
        String data = this.selectData.get(0).getTaskname().substring(0, 6);
        Date sendDate = this.selectData.get(0).getSendtime();
        StringBuffer msg = new StringBuffer("【上海汉钟】");
        msg.append(data).append("期薪资发放回执已与").append(BaseLib.formatDate("yyyyMMdd", sendDate));
        msg.append("发出，您还未确认，请进入企业微信 系统消息 及时签收！谢谢！");
        agent1000002Bean.initConfiguration();
        String errmsg = agent1000002Bean.sendMsgToUser(toUsers.substring(0, toUsers.length() - 1), "text", msg.toString());
    }

    public void sendmsg() {
        StringBuffer toUsers = new StringBuffer("");
        String dateString = BaseLib.formatDate("yyyyMM", changeFormlTime);
        String taskid = salarySendBean.getTaskId("XZHZ" + dateString);
        StringBuffer data = new StringBuffer();
        for (SalarySend s : this.selectData) {
                toUsers.append(s.getEmployeeid()).append("|");
                SalarySend salary = salarySendBean.findByTaskNameAndEmployeeid(dateString + "月薪资发放", s.getEmployeeid());
                salary.setSendtime(new Date());
                salary.setConfirmtime(null);
                salary.setStatus("N");
                salary.setTaskid(taskid);
                salarySendBean.update(salary);
        }
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
        agent1000002Bean.sendMsgToUser(toUsers.substring(0,toUsers.length()-1), "taskcard", data.toString());
    }

    @Override
    public void openDialog(String view) {
        StringBuffer p = new StringBuffer();
        for (SalarySend s : this.selectData) {
                p.append(s.getEmployeename()).append("|");
        }
        this.acceptPerson = p.toString();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        PrimeFaces.current().dialog().openDynamic(view, options, null);
    }

    @Override
    public void print() {
        query();
        entityList = salarySendBean.findByFilters(model.getFilterFields(), model.getSortFields());
        InputStream is = null;
        try {
            try {
                String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                int index = finalFilePath.indexOf("WEB-INF");
                String filePath = new String(finalFilePath.substring(1, index));
                String pathString = new String(filePath.concat("rpt/"));
                File file = new File(pathString, "薪资回执明细.xlsx");
                is = new FileInputStream(file);
                this.fileName = "薪资回执明细" + com.lightshell.comm.BaseLib.formatDate("yyyyMMddHHmmss", com.lightshell.comm.BaseLib.getDate()) + ".xlsx";
                String fullname = this.ec.getRealPath("/") + this.fileName;
                Workbook wb = WorkbookFactory.create(is);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                Sheet sheet = wb.getSheetAt(0);
                wb.setSheetName(0, sdf.format(new Date()) + "服务打卡明细");
                Row row = null;
                int i = 1;
                for (Iterator var12 = this.entityList.iterator(); var12.hasNext(); ++i) {
                    SalarySend e = (SalarySend) var12.next();
                    row = sheet.createRow(i);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(i);
                    row.createCell(1).setCellValue(e.getTaskid() != null ? e.getTaskid() : "");
                    row.createCell(2).setCellValue(e.getTaskname() != null ? e.getTaskname() : "");
                    row.createCell(3).setCellValue(e.getSendtime() != null ? BaseLib.formatDate("yyyy/MM/dd", e.getSendtime()) : "");
                    row.createCell(4).setCellValue(e.getEmployeeid() != null ? e.getEmployeeid() : "");
                    row.createCell(5).setCellValue(e.getEmployeename() != null ? e.getEmployeename() : "");
                    row.createCell(6).setCellValue(e.getDeptno() != null ? e.getDeptno() : "");
                    row.createCell(7).setCellValue(e.getDept() != null ? e.getDept() : "");
                    row.createCell(8).setCellValue(e.getStatus() != null ? e.getStatus() : "");
                    row.createCell(9).setCellValue(e.getConfirmtime() != null ? BaseLib.formatDate("yyyy/MM/dd", e.getConfirmtime()) : "");
                }
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(fullname);
                    wb.write(os);
                    this.reportViewPath = "/Hanbell-WCO/" + this.fileName;
                    this.preview();
                } catch (Exception var38) {
                    var38.printStackTrace();
                } finally {
                    try {
                        if (null != os) {
                            os.flush();
                            os.close();
                        }
                    } catch (IOException var37) {
                        this.log4j.error(var37.getMessage());
                    }
                }
            } catch (FileNotFoundException var40) {
                this.log4j.error(var40.getMessage());
            } catch (IOException var41) {
                this.log4j.error(var41.getMessage());
            } catch (Exception var42) {
                this.log4j.error(var42.getMessage());
            }

        } finally {

        }
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<SalarySend> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SalarySend> selectData) {
        this.selectData = selectData;
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println("event=" + event);;
    }

    public void onRowUnselect(UnselectEvent event) {
        System.out.println("event=" + event);
    }

    public Date getChangeFormlTime() {
        return changeFormlTime;
    }

    public void setChangeFormlTime(Date changeFormlTime) {
        this.changeFormlTime = changeFormlTime;
    }

    public String getAcceptPerson() {
        return acceptPerson;
    }

    public void setAcceptPerson(String acceptPerson) {
        this.acceptPerson = acceptPerson;
    }

}
