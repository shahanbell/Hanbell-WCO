/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.AttendanceBean;
import cn.hanbell.eap.entity.Attendance;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.ejb.ConfigPropertiesBean;
import cn.hanbell.wco.lazy.AttendanceModel;
import cn.hanbell.wco.web.SuperQueryBean;
import com.lightshell.comm.BaseLib;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "attendanceManagedBean")
@SessionScoped
public class AttendanceManagedBean extends SuperQueryBean<Attendance> {

    @EJB
    private Agent1000002Bean agent1000002Bean;
    private String facno;
    private String employeeName;
    private String employeeId;
    private Date date;
    private String status;
    @EJB
    private AttendanceBean attendanceBean;

    @EJB
    private ConfigPropertiesBean configPropertiesBean;

    private String importCompany;
    private Date importDate;
    private String isOverride;
    private UploadedFile file;

    public AttendanceManagedBean() {
        super(Attendance.class);
    }

    @Override
    public void init() {
        this.setSuperEJB(this.attendanceBean);
        this.model = new AttendanceModel(this.attendanceBean);
        //根据工号判断当前的登录人员
        String userid = userManagedBean.getCurrentUser().getUserid();
        //上海汉钟
        if (userid.startsWith("C")) {
            this.facno = "C";
            this.importCompany = "C";
        } else if (userid.startsWith("H")) {
            this.facno = "H";
            this.importCompany = "H";
        } else if (userid.startsWith("Y")) {
            this.facno = "Y";
            this.importCompany = "Y";
        }
        this.importDate = new Date();
        this.isOverride = "N";
        super.init();
    }

    @Override
    public void query() {
        this.model = new AttendanceModel(this.attendanceBean);
        if (this.model != null && this.model.getFilterFields() != null) {
            if (facno != null && !"".equals(facno)) {
                this.model.getFilterFields().put("facno", facno);
            }
            if (employeeId != null && !"".equals(employeeId)) {
                this.model.getFilterFields().put("employeeId", employeeId);
            }
            if (date != null) {
                this.model.getFilterFields().put("attendanceDate", BaseLib.formatDate("YYYYMM", date));
            }
            if (status != null && !"All".equals(status)) {
                this.model.getFilterFields().put("status", status);
            }
        }
    }

    public void upload() {
        try {
            Integer a = 0;
            if (file != null) {
                try {
                    InputStream inputStream = file.getInputstream();
                    Workbook excel = WorkbookFactory.create(inputStream);
                    Sheet sheet = excel.getSheetAt(0);
                    for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                        a = i;
                        Row row = sheet.getRow(i);
                        Attendance attendance = new Attendance();
                        attendance.setEmployeeId(cellToVlaue(row.getCell(0)));
                        attendance.setEmployeeName(cellToVlaue(row.getCell(1)));
                        if ("".equals(attendance.getEmployeeId())) {
                            continue;
                        }
                        attendance.setFacno(this.importCompany);
                        attendance.setAttendanceDate(BaseLib.formatDate("yyyyMM", this.importDate));
                        attendance.setDept(cellToVlaue(row.getCell(2)));
                        attendance.setPacificOvertime(cellToVlaue(row.getCell(6)));
                        attendance.setRestOvertime(cellToVlaue(row.getCell(7)));
                        attendance.setHolidayOvertime(cellToVlaue(row.getCell(8)));
                        attendance.setSickLeave(cellToVlaue(row.getCell(9)));
                        attendance.setAffairLeave(cellToVlaue(row.getCell(10)));
                        attendance.setSpecialRest(cellToVlaue(row.getCell(11)));
                        attendance.setMarriageLeave(cellToVlaue(row.getCell(12)));
                        attendance.setDieLeave(cellToVlaue(row.getCell(13)));
                        attendance.setHurtLeave(cellToVlaue(row.getCell(14)));
                        attendance.setPaternityLeave(cellToVlaue(row.getCell(15)));
                        attendance.setMaternityLeave(cellToVlaue(row.getCell(16)));
                        attendance.setNoSalaryLeave(cellToVlaue(row.getCell(17)));
                        attendance.setAntenatalLeave(cellToVlaue(row.getCell(18)));
                        attendance.setPublicLeave(cellToVlaue(row.getCell(19)));
                        attendance.setBreastfeedingLeave(cellToVlaue(row.getCell(20)));
                        attendance.setHomeLeave(cellToVlaue(row.getCell(21)));
                        attendance.setChild(cellToVlaue(row.getCell(22)));
                        attendance.setForgetClock(cellToVlaue(row.getCell(23)));
                        attendance.setLate(cellToVlaue(row.getCell(24)));
                        attendance.setLeaveEarly(cellToVlaue(row.getCell(25)));
                        attendance.setAbsent(cellToVlaue(row.getCell(26)));
                        attendance.setMeal(cellToVlaue(row.getCell(27)));
                        attendance.setBreakfast(cellToVlaue(row.getCell(28)));
                        attendance.setLunch(cellToVlaue(row.getCell(29)));
                        attendance.setDinner(cellToVlaue(row.getCell(30)));
                        attendance.setOweClass(cellToVlaue(row.getCell(31)));
                        attendance.setStatus("X");
                        attendance.setCheckcode(getCheckCode());
                        attendance.setCreator(this.userManagedBean.getUserid());
                        attendance.setCredateToNow();
                        List<Attendance> list = attendanceBean.findByFacnoAndEmployeeidAndAttendanceDate(this.importCompany, attendance.getEmployeeId(), attendance.getAttendanceDate());
                        if (list != null && !list.isEmpty() && "V".equals(list.get(0).getStatus()) && "N".equals(this.isOverride)) {
                            continue; // 已经推送了消息，前端页面不覆盖的话跳过更新数据库。
                        }
                        if (list != null) {
                            attendanceBean.delete(list);
                            attendanceBean.persist(attendance);
                        } else {
                            attendanceBean.persist(attendance);
                        }
                    }
                } catch (Exception ex) {
                    FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传失败"));
                    ex.printStackTrace();
                    showErrorMsg("Error", "导入失败,找不到文件或格式错误--第" + a + "行附近栏位发生错误" + ex.toString());
                }
                int cha = a - 1;
                FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传成功，共" + cha + "条数据！"));
            }
        } catch (Exception ex) {
            Logger.getLogger(AttendanceManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void reset() {
        init();
        this.setSuperEJB(this.attendanceBean);
        this.model = new AttendanceModel(this.attendanceBean);
        employeeId = "";
        date = null;
        super.reset();
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        this.file = event.getFile();
        this.showInfoMsg("Info", "上传成功,请保存！");
    }

    public void fileChanged() { 
        if(this.file!=null){
            this.showInfoMsg("Info", "上传成功,请保存！");
        }
    } 
    public String cellToVlaue(Cell cell) {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        switch (type) {
            case 0:
                double d = cell.getNumericCellValue();
                //整数去掉小数点
                if (d == (int) d) {
                    return String.valueOf((int) d);
                }
                return String.valueOf(cell.getNumericCellValue());
            case 1:
                return cell.getStringCellValue();
            case 2:
                return cell.getCellFormula();
            case 3:
                return "0";
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            case 5:
                return String.valueOf(cell.getErrorCellValue());
        }
        return "";
    }

    //发送消息
    public void send() {
        try {
            Thread.sleep(2000);
            entityList = attendanceBean.findByFilters(this.model.getFilterFields(), this.model.getSortFields());
            for (Attendance a : entityList) {
                if ("X".equals(a.getStatus())) {
                    agent1000002Bean.initConfiguration();
                    StringBuffer msg = new StringBuffer("【上海汉钟】");
                    msg.append("您的").append(a.getAttendanceDate()).append("考勤记录已生成，<br>");
                    msg.append("<a href=\"");
                    StringBuffer url = new StringBuffer(configPropertiesBean.findByKey("cn.hanbell.wco.control.AttendanceManagedBean.attendanceUrl").getConfigvalue());

                    url.append(a.getEmployeeId()).append("&attendanceDate=").append(a.getAttendanceDate()).append("&checkcode=").append(a.getCheckcode());
                    msg.append(url).append("\">请点击此处").append("</a>   ");
                    msg.append("查看");
                    // String errmsg = agent1000002Bean.sendMsgToUser(a.getEmployeeId(), "text", msg.toString());
                    String errmsg = "ok";
                    if (errmsg.startsWith("ok")) {
                        a.setStatus("V");
                        a.setOptuser(this.userManagedBean.getUserid());
                        a.setOptdateToNow();
                        attendanceBean.update(a);
                    }
                }
            }
            this.showInfoMsg("Info", "发送成功");
        } catch (InterruptedException ex) {
            Logger.getLogger(AttendanceManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCheckCode() {
        String base = "0123456789qwertyuiopasdfghjklzxcvbnm";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getImportCompany() {
        return importCompany;
    }

    public void setImportCompany(String importCompany) {
        this.importCompany = importCompany;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getIsOverride() {
        return isOverride;
    }

    public void setIsOverride(String isOverride) {
        this.isOverride = isOverride;
    }

}
