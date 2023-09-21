/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemGrantPrg;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.ejb.Agent1000016Bean;
import cn.hanbell.wco.ejb.BirthdayUserBean;
import cn.hanbell.wco.entity.BirthdayUser;
import cn.hanbell.wco.lazy.BirthdayUserModel;
import cn.hanbell.wco.web.SuperQueryBean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
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
@ManagedBean(name = "birthdayUserManagedBean")
@SessionScoped
public class BirthdayUserManagedBean extends SuperQueryBean<BirthdayUser> {

    private String facno;
    private String userid;
    private String username;
    private String jdcard;
    private int year;
    private String status;
    private int uploadYear;
    @EJB
    private BirthdayUserBean birthdayUserBean;

    @EJB
    private Agent1000016Bean agent1000016Bean;
    @EJB
    private SystemUserBean systemUserBean;

    public BirthdayUserManagedBean() {
        super(BirthdayUser.class);
    }

    @Override
    public void init() {
        this.superEJB = birthdayUserBean;
        this.model = new BirthdayUserModel(this.birthdayUserBean);
        String userid = userManagedBean.getCurrentUser().getUserid();
        //上海汉钟
        if (userid.startsWith("C")) {
            this.facno = "C";
        } else if (userid.startsWith("H")) {
            this.facno = "H";
        } else if (userid.startsWith("Y")) {
            this.facno = "Y";
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        year = c.get(Calendar.YEAR);
        uploadYear = c.get(Calendar.YEAR);
        setToolBar();
        super.init();

    }

    @Override
    public void query() {
        this.model = new BirthdayUserModel(this.birthdayUserBean);
        if (this.model != null && this.model.getFilterFields() != null) {
            this.model.getFilterFields().put("facno", facno);
            this.model.getFilterFields().put("year", year);
            if (userid != null && !"".equals(userid)) {
                this.model.getFilterFields().put("userid", userid);
            }
            if (username != null && !"".equals(username)) {
                this.model.getFilterFields().put("username", username);
            }
            if (jdcard != null && !"".equals(jdcard)) {
                this.model.getFilterFields().put("jdcard", jdcard);
            }
            if (status != null && !"All".equals(status)) {
                this.model.getFilterFields().put("status", status);
            }
        }
    }

    @Override
    public void reset() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        year = c.get(Calendar.YEAR);
        uploadYear = c.get(Calendar.YEAR);
        userid = "";
        username = "";
        jdcard = "";
        status = "All";
    }

    @Override
    public void print() throws Exception {
        InputStream is = null;
        try {
            String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("WEB-INF");
            String filePath = new String(finalFilePath.substring(1, index));
            String pathString = new String(filePath.concat("rpt/"));
            File file = new File(pathString, "生日人员明细模板.xls");
            is = new FileInputStream(file);
            this.fileName = "生日人员明细模板" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
            Workbook wb = WorkbookFactory.create(is);
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(this.reportOutputPath + this.fileName);
                wb.write(os);
                this.reportViewPath = this.reportViewContext + this.fileName;
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
                    var37.printStackTrace();
                }
            }
        } catch (FileNotFoundException var40) {
            var40.printStackTrace();
        } catch (Exception var42) {
            var42.printStackTrace();
        }
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        UploadedFile file1 = event.getFile();
        Integer a = 0;
        if (file1 != null) {
            try {
                InputStream inputStream = file1.getInputstream();
                Workbook excel = WorkbookFactory.create(inputStream);
                Sheet sheet = excel.getSheetAt(0);
                int lastrow = sheet.getLastRowNum();
                Cell cell;
                Calendar calendar=Calendar.getInstance();
                for (int i = 2; i <= lastrow; i++) {
                    Row row = sheet.getRow(i);
                    this.create();
                    newEntity.setFormdate(new Date());
                    String formid = this.superEJB.getFormId(newEntity.getFormdate(), this.currentPrgGrant.getSysprg().getNolead(), this.currentPrgGrant.getSysprg().getNoformat(), this.currentPrgGrant.getSysprg().getNoseqlen(), "formid");
                    newEntity.setFormid(formid);
                    newEntity.setFacno(facno);
                    newEntity.setYear(this.uploadYear);
                    newEntity.setUserid(cellToVlaue(row.getCell(0)));
                    newEntity.setUsername(cellToVlaue(row.getCell(1)));
                    newEntity.setDeptno(cellToVlaue(row.getCell(2)));
                    newEntity.setDeptname(cellToVlaue(row.getCell(3)));
                    newEntity.setJdcard(cellToVlaue(row.getCell(4)));
                    newEntity.setJdpassword(cellToVlaue(row.getCell(5)));
                    newEntity.setStatus("X");
                    newEntity.setCreator(getUserManagedBean().getCurrentUser().getUsername());
                    newEntity.setCredateToNow();
                    Map<String, Object> map = new HashMap();
                    map.put("facno", this.newEntity.getFacno());
                    map.put("year", this.newEntity.getYear());
                    map.put("userid", this.newEntity.getUserid());
                    List<BirthdayUser> list = this.superEJB.findByFilters(map);
                    if (list.size() == 1 && !"V".equals(list.get(0).getStatus())) {
                        this.superEJB.delete(list);
                    } else if (list.size() == 1 && "V".equals(list.get(0).getStatus())) {
                        this.doAfterPersist();
                        continue;
                    }
                    this.persist();
                }
                int cha = a - 1;
                FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传成功，共" + cha + "条数据！"));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传失败"));
                ex.printStackTrace();
                showErrorMsg("Error", "导入失败,找不到文件或格式错误--第" +(a+1)+ "行附近栏位发生错误" + ex.toString());
            }
        }
    }

    @Override
    protected void setToolBar() {
        if (currentEntity != null && getCurrentPrgGrant() != null && currentEntity.getStatus() != null) {
            switch (currentEntity.getStatus()) {
                case "V":
                    this.doEdit = getCurrentPrgGrant().getDoedit() && false;
                    this.doDel = getCurrentPrgGrant().getDodel() && false;
                    this.doCfm = false;
                    this.doUnCfm = getCurrentPrgGrant().getDouncfm() && true;
                    break;
                default:
                    this.doEdit = getCurrentPrgGrant().getDoedit() && true;
                    this.doDel = getCurrentPrgGrant().getDodel() && true;
                    this.doCfm = getCurrentPrgGrant().getDocfm() && true;
                    this.doUnCfm = false;
            }
        } else {
            this.doEdit = getCurrentPrgGrant().getDoedit();
            this.doDel = getCurrentPrgGrant().getDodel();
            this.doCfm = getCurrentPrgGrant().getDocfm();
            this.doUnCfm = getCurrentPrgGrant().getDouncfm();
            this.doAdd = getCurrentPrgGrant().getDoadd();
            this.doPrt = true;
        }
    }

    public String cellToVlaue(Cell cell) throws Exception {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        switch (type) {
            case 0:
                double d = cell.getNumericCellValue();
                //时间格式
                short format = cell.getCellStyle().getDataFormat();
                if (DateUtil.isCellDateFormatted(cell)) {
                    if (format == 31) {
                        Date date = DateUtil.getJavaDate(d);
                        return BaseLib.formatDate("MM/dd", date);
                    } else {
                        throw new Exception("时间格式异常，请使用XXXX年XX月XX日格式！");
                    }
                }
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

    public void test() {
        agent1000016Bean.initConfiguration();
        String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = finalFilePath.indexOf("WEB-INF");
        String filePath = new String(finalFilePath.substring(1, index));
        String pathString = new String(filePath.concat("rpt/"));
        String selectDate = BaseLib.formatDate("____-MM-dd%", new Date());
        List<SystemUser> list = systemUserBean.findByLikeBirthdayDateAndDeptno(selectDate);
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        if (list != null && !list.isEmpty()) {
            for (SystemUser s : list) {
                if ("V".equals(s.getSyncWeChatStatus())) {
                    try {
                        String materialId = agent1000016Bean.getMaterialId(agent1000016Bean.MEDIA_IMG, pathString.concat(agent1000016Bean.getBirthdatPicteureUrl(s.getDeptno())));
                        Calendar a = Calendar.getInstance();
                        BirthdayUser birthdayUser = this.birthdayUserBean.findByUseridAndYear(s.getUserid(), date.get(Calendar.YEAR));
                        //发送消息
                        if (birthdayUser == null) {
                            StringBuffer data = new StringBuffer("{");
                            data.append("'title':'").append(s.getUsername()).append(",生日快乐!").append("',");
                            data.append("'thumb_media_id':'").append(materialId).append("',");
                            data.append("'content':'").append("<img src=\"http://i2.hanbell.com.cn:8480/birthday.png\"></img>").append("',");
                            data.append("'safe':").append(2).append("}");
                            agent1000016Bean.sendMsgToUser(this.userManagedBean.getUserid(), "mpnews", data.toString());
                        } else if (birthdayUser != null && !birthdayUser.getStatus().equals("N")) {
                            sendMsg(birthdayUser, materialId);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }

    }

    public void send() {
        agent1000016Bean.initConfiguration();
        String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = finalFilePath.indexOf("WEB-INF");
        String filePath = new String(finalFilePath.substring(1, index));
        String pathString = new String(filePath.concat("rpt/"));
        for (BirthdayUser entity : this.entityList) {
            if ("N".equals(entity.getStatus())) {
                try {
                    String materialId = agent1000016Bean.getMaterialId(agent1000016Bean.MEDIA_IMG, pathString.concat(agent1000016Bean.getBirthdatPicteureUrl(entity.getDeptno())));
                    sendMsg(entity, materialId);
                } catch (Exception e) {

                }
            }
        }
    }

    public void sendMsg(BirthdayUser user, String materialId) throws Exception {
        //发送消息
        StringBuffer data = new StringBuffer("{");
        data.append("'title':'").append(user.getUsername()).append(",生日快乐!").append("',");
        data.append("'digest':'").append("在这特别的日子里。愿你的生日充满无尽的快乐，愿你的未来充满无限的幸福。\\n\\n 我们知道，你的工作能力和团队精神让你在团队中发挥着举足轻重的作用。我们非常感谢你为我们的公司所做出的贡献，期待你未来会有更多的精彩表现。\\n\\n在生日的这一天，我们还为你准备了一份特别的礼物，请点击卡片获取。").append("',");
        data.append("'thumb_media_id':'").append(materialId).append("',");
        data.append("'content':'").append("<img src=\"https://jrs.hanbell.com.cn/birthday.png?page=123\"></img>这是京东卡号和密码，你可以使用它来充值100京豆。</br></br><h3 style=\"color: royalblue;\">- 兑换码：");
        data.append(user.getJdcard()).append("</h3><h3 style=\"color: royalblue;\">- 密码：");
        data.append(user.getJdpassword());
        data.append("</h3></br></br>请尽快使用这个兑换码和密码，获取属于你的生日礼物。</br>我们希望这份礼物能给你带来更多的快乐和惊喜。</br>再次祝福你生日快乐！愿你的每一天都充满阳光和喜悦。");
        data.append("',");
        data.append("'safe':").append(2).append("}");
        String msg = agent1000016Bean.sendMsgToUser(this.userManagedBean.getUserid(), "mpnews", data.toString());
        if (msg.startsWith("ok")) {
            System.out.println("msg==" + msg);
            String[] split = msg.split("|");
            user.setStatus("V");
            user.setMsgid(msg.replace("ok|", ""));
            this.superEJB.update(user);
        }
    }

    @Override
    public void verify() {
        for (BirthdayUser entity : this.entityList) {
            if (!entity.getStatus().equals("V")) {
                entity.setStatus("N");
            }
        }
        this.superEJB.update(entityList);
    }

    @Override
    public void unverify() {
        for (BirthdayUser entity : this.entityList) {
            if (!entity.getStatus().equals("V")) {
                entity.setStatus("X");
            }
        }
        this.superEJB.update(entityList);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJdcard() {
        return jdcard;
    }

    public void setJdcard(String jdcard) {
        this.jdcard = jdcard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getUploadYear() {
        return uploadYear;
    }

    public void setUploadYear(int uploadYear) {
        this.uploadYear = uploadYear;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getAppDataPath() {
        return appDataPath;
    }

    public void setAppDataPath(String appDataPath) {
        this.appDataPath = appDataPath;
    }

    public SystemGrantPrg getCurrentPrgGrant() {
        return currentPrgGrant;
    }

    public void setCurrentPrgGrant(SystemGrantPrg currentPrgGrant) {
        this.currentPrgGrant = currentPrgGrant;
    }

}
