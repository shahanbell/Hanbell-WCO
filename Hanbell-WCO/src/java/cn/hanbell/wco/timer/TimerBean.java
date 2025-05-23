/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.timer;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.ejb.WeChatTagUserBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.eap.entity.WeChatTagUser;
import cn.hanbell.oa.ejb.ProcessInstanceBean;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.ejb.Agent1000016Bean;
import cn.hanbell.wco.ejb.Agent1000022Bean;
import cn.hanbell.wco.ejb.Agent3010011Bean;
import com.lightshell.comm.BaseLib;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimerService;
import javax.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import tw.hanbell.exch.ejb.BPMEmployeeBean;
import tw.hanbell.exch.entity.BPMEmployee;

/**
 *
 * @author C0160
 */
@Singleton
@Startup
public class TimerBean {

    @Resource
    TimerService timerService;

    @EJB
    private DepartmentBean departmentBean;
    @EJB
    private SystemUserBean systemUserBean;
    @EJB
    private WeChatTagUserBean wechatTagUserBean;
    @EJB
    private Agent3010011Bean agent3010011Bean;
    @EJB
    private Agent1000002Bean wechatCorpBean;
    @EJB
    private Agent1000016Bean agent1000016Bean;
    @EJB
    private Agent1000022Bean agent1000022Bean;
    @EJB
    private ProcessInstanceBean processInstanceBean;
    @EJB
    private BPMEmployeeBean bPMEmployeeBean;
    private List<Department> deptList;
    private List<Department> childDepts;
    private List<SystemUser> userList;

    private final String errMsgUser = "C2082";
    private final Logger log4j = LogManager.getLogger("cn.hanbell.wco");

    public TimerBean() {

    }

    @Schedule(minute = "45", hour = "7,16,23", persistent = false)
    public void syncWXWorkOrganizationByEAP() {
        wechatCorpBean.initConfiguration();
        wechatCorpBean.sendMsgToUser(errMsgUser, "text", "企业微信更新开始");
        // 先由EAP执行排程，从HRM同步到EAP
        log4j.info("syncWXWorkOrganizationByEAP开始");
        Department dept = departmentBean.findByDeptno("00000");
        if (dept != null) {
            userList = new ArrayList<>();
            deptList = departmentBean.findByPId(dept.getId());
            if (deptList != null && !deptList.isEmpty()) {
                log4j.info("syncWXWorkDepartmentByEAP开始");
                deptList.forEach((d) -> {
                    syncDept(d);
                });
                log4j.info("syncWXWorkDepartmentByEAP结束");
                log4j.info("syncWXWorkEmployeeByEAP开始");
                deptList.forEach((d) -> {
                    userList.clear();
                    loadUser(d);
                    if (userList.size() > 1) {
                        userList.sort((SystemUser o1, SystemUser o2) -> {
                            if (o1.getUserid().compareTo(o2.getUserid()) < 0) {
                                return -1;
                            } else {
                                return 1;
                            }
                        });
                    }
                    syncEmployee();
                });
                log4j.info("syncWXWorkEmployeeByEAP结束");
            }
        }
        wechatCorpBean.sendMsgToUser(errMsgUser, "text", "企业微信更新结束");
        log4j.info("syncWXWorkOrganizationByEAP结束");
    }

    @Schedule(minute = "35", hour = "7,12,16", persistent = false)
    public void syncWXWorkCheckInData() {
        try {
            syncWeChatOAToEAP();
        } catch (Exception ex) {
            log4j.error(ex);
        }
    }

    private void loadUser(Department dept) {
        if (dept != null) {
            childDepts = departmentBean.findByPId(dept.getId());
            if (childDepts != null && !childDepts.isEmpty()) {
                for (Department e : childDepts) {
                    if (e.getStatus().equals("X")) {
                        // 已停用部门无需载入
                        continue;
                    }
                    loadUser(e);
                }
            }
            userList.addAll(systemUserBean.findByDeptno(dept.getDeptno()));
        }
    }

    private boolean syncDept(Department dept) {
        String msg;
        boolean ret = true;
        if (dept.getSyncWeChatStatus() != null && "X".equals(dept.getSyncWeChatStatus())) {
            return true;
        }
        JsonObject jo = departmentBean.createJsonObjectBuilder(dept).build();
        if (dept.getSyncWeChatStatus() == null || dept.getSyncWeChatDate() == null) {
            msg = wechatCorpBean.createDepartment(jo);
            if (msg.equals("success")) {
                dept.setSyncWeChatDate(BaseLib.getDate());
                dept.setSyncWeChatStatus("V");
                dept.setOptdate(dept.getSyncWeChatDate());
                departmentBean.update(dept);
                childDepts = departmentBean.findByPId(dept.getId());
                if (childDepts != null && !childDepts.isEmpty()) {
                    for (Department e : childDepts) {
                        ret = ret && syncDept(e);
                    }
                }
            } else {
                wechatCorpBean.sendMsgToUser(errMsgUser, "text", dept.getDeptno() + "更新失败");
                ret = false;
                log4j.error(msg);
            }
            return ret;
        } else {
            if (dept.getStatus().equals("X")) {
                // 先删除子阶
                childDepts = departmentBean.findByPId(dept.getId());
                if (childDepts != null && !childDepts.isEmpty()) {
                    for (Department e : childDepts) {
                        ret = ret && syncDept(e);
                    }
                }
                if (ret) {
                    msg = wechatCorpBean.deleteDepartment(dept.getId());
                    if (msg.equals("success")) {
                        dept.setSyncWeChatDate(BaseLib.getDate());
                        dept.setSyncWeChatStatus("X");
                        dept.setOptdate(dept.getSyncWeChatDate());
                        departmentBean.update(dept);
                    } else {
                        wechatCorpBean.sendMsgToUser(errMsgUser, "text", dept.getDeptno() + "更新失败");
                        ret = false;
                        log4j.error(msg);
                    }
                }
                return ret;
            } else {
                childDepts = departmentBean.findByPId(dept.getId());
                if (childDepts != null && !childDepts.isEmpty()) {
                    for (Department e : childDepts) {
                        ret = ret && syncDept(e);
                    }
                }
                if (("N".equals(dept.getSyncWeChatStatus()) || dept.getSyncWeChatDate().before(dept.getOptdate()))
                        && ret) {
                    msg = wechatCorpBean.updateDepartment(jo);
                    if (msg.equals("success")) {
                        dept.setSyncWeChatDate(BaseLib.getDate());
                        dept.setSyncWeChatStatus("V");
                        dept.setOptdate(dept.getSyncWeChatDate());
                        departmentBean.update(dept);
                    } else {
                        wechatCorpBean.sendMsgToUser(errMsgUser, "text", dept.getDeptno() + "更新失败");
                        ret = false;
                        log4j.error(msg);
                    }
                }
                return ret;
            }
        }
    }

    private void syncEmployee() {
        if (userList != null && !userList.isEmpty()) {
            List<BPMEmployee> entities = bPMEmployeeBean.findAll();
            for (SystemUser user : userList) {
                Date d = user.getSyncWeChatDate();
                String msg = "";
                try {
                    if (user.getSyncWeChatStatus() != null && "X".equals(user.getSyncWeChatStatus())) {
                        continue;
                    }
                    if (user.getSyncWeChatStatus() == null && "X".equals(user.getStatus())) {
                        user.setSyncWeChatStatus(user.getStatus());
                        systemUserBean.update(user);
                        continue;
                    }
                    if (((user.getPhone() == null || "".equals(user.getPhone()))
                            && (user.getEmail() == null || "".equals(user.getEmail()))) || user.getDeptno() == null
                            || "".equals(user.getDeptno())) {
                        // 离职人员没有手机号码也要更新微信同步状态
                        if ("X".equals(user.getStatus())) {
                            user.setSyncWeChatStatus(user.getStatus());
                            systemUserBean.update(user);
                        }
                        continue;
                    }
                    JsonObject jo;
                    if (user.getSyncWeChatStatus() == null || user.getSyncWeChatDate() == null) {
                        jo = systemUserBean.createJsonObjectBuilder(user).build();
                        if (user.getPhone() != null && !"".equals(user.getPhone())) {
                            msg = wechatCorpBean.createEmployee(jo);
                        } else {
                            user.setSyncWeChatDate(BaseLib.getDate());
                            user.setSyncWeChatStatus("X");
                            user.setOptdate(user.getSyncWeChatDate());
                            systemUserBean.update(user);
                        }
                        if (msg.equals("success")) {
                            user.setSyncWeChatDate(BaseLib.getDate());
                            user.setSyncWeChatStatus("V");
                            user.setOptdate(user.getSyncWeChatDate());
                            systemUserBean.update(user);
                        } else {
                            wechatCorpBean.sendMsgToUser(errMsgUser, "text", user.getUserid() + "更新失败");
                            log4j.error(user.getUserid() + "同步失败：" + msg);
                        }
                    } else {
                        if (user.getStatus().equals("X")) {
                            // 企业微信人员删除后删除标签组人员
                            List<WeChatTagUser> list = wechatTagUserBean.findByUserid(user.getUserid());
                            if (list != null && !list.isEmpty()) {
                                List<WeChatTagUser> tagUserList = new ArrayList<>();
                                for (WeChatTagUser tagUser : list) {
                                    // 单一用户多个标签处理逻辑
                                    tagUserList.clear();
                                    tagUserList.add(tagUser);
                                    jo = wechatTagUserBean.createJsonObjectBuilder(tagUserList, tagUser.getTagid()).build();
                                    msg = wechatCorpBean.deleteWeChatTagUser(jo);
                                    if (msg.equals("success")) {
                                        wechatTagUserBean.delete(tagUser);
                                    } else {
                                        log4j.error(msg);
                                    }
                                }
                                tagUserList.clear();
                                tagUserList = null;
                            }
                            msg = wechatCorpBean.deleteEmployee(user.getUserid());
                            if (msg.equals("success")) {
                                user.setSyncWeChatDate(BaseLib.getDate());
                                user.setSyncWeChatStatus("X");
                                user.setBirthdayDate(null);
                                user.setWorkingAgeBeginDate(null);
                                user.setOptdate(user.getSyncWeChatDate());
                                systemUserBean.update(user);
                            } else {
                                wechatCorpBean.sendMsgToUser(errMsgUser, "text", user.getUserid() + "更新失败");
                                log4j.error(msg);
                            }
                        } else {
                            if (("N".equals(user.getSyncWeChatStatus())
                                    || user.getSyncWeChatDate().before(user.getOptdate()))) {
                                jo = systemUserBean.createJsonObjectBuilder(user).build();
                                msg = wechatCorpBean.updateEmployee(jo);
                                if (msg.equals("success")) {
                                    user.setSyncWeChatDate(BaseLib.getDate());
                                    user.setSyncWeChatStatus("V");
                                    user.setOptdate(user.getSyncWeChatDate());
                                    systemUserBean.update(user);
                                } else {
                                    wechatCorpBean.sendMsgToUser(errMsgUser, "text", user.getUserid() + "更新失败");
                                    log4j.error(user.getUserid() + "同步失败：" + msg);
                                }
                            }
                        }
                    }
                    List<BPMEmployee> filerEneyties = entities.stream()
                            .filter(n -> n.getUserid().equals(user.getUserid()) && d.before(n.getLastModifiedDate()))
                            .collect(Collectors.toList());
                    if (filerEneyties != null && filerEneyties.size() == 1) {
                        JsonObject obj = bPMEmployeeBean.createThbJsonObjectBuilder(user.getDept().getId(), filerEneyties.get(0)).build();
                        msg = wechatCorpBean.updateEmployee(obj);
                        if (msg.equals("success")) {
                            user.setSyncWeChatDate(new Date());
                            systemUserBean.update(user);
                            filerEneyties.get(0).setLastModifiedDate(new Date());
                            bPMEmployeeBean.update(filerEneyties.get(0));
                        } else {
                            wechatCorpBean.sendMsgToUser(errMsgUser, "text", user.getUserid() + "更新失败");
                            log4j.error(user.getUserid() + "同步失败：" + msg);
                        }
                    }
                } catch (Exception e) {
                    log4j.error(user.getUserid() + "同步异常：" + e);
                }
            }
        }
    }

    public void syncWeChatOAToEAP() throws ParseException {
        log4j.info("========开始企业微信OA数据抛转到本地数据库中========");
        agent3010011Bean.initConfiguration();
        //获取最大的部门人员，并递归获取每个部门下的人员
        JSONObject js = agent3010011Bean.getWeChatUser("1", "1");
        if (js != null) {
            JSONArray users = js.getJSONArray("userlist");
            JSONArray recordArray = new JSONArray();
            JSONArray records = gerRecords(users, 1, 100, recordArray);
            for (int i = 0; i < records.length(); i++) {
                JSONArray ja = records.getJSONArray(i);
                agent3010011Bean.syncPunchCardRecordToEap(ja);
            }
        }
        log4j.info("========企业微信OA数据跑转到本地数据库中结束========");
    }

    /**
     *
     * @param array 所有的人员集合
     * @param m 前置索引下标
     * @param n 后置索引小标
     */
    public JSONArray gerRecords(JSONArray user, int m, int n, JSONArray records) throws ParseException {
        Long end1 = new Date().getTime();
        long start1 = end1 - 1000 * 60 * 60 * 24 * 3;
        long end = end1 / 1000;
        long start = start1 / 1000;
        if (user.length() < 100) {
            List<String> users = new ArrayList<>();
            for (int i = 0; i < user.length(); i++) {
                users.add(user.optJSONObject(i).getString("userid"));
            }
            return agent3010011Bean.getPunchCardRecord(3, start, end, users).getJSONArray("checkindata");
        } else {
            int floor = (int) (user.length() / 100);
            //这是最后一次了
            if (floor * 100 + 1 == m) {
                List<String> users = new ArrayList<>();
                for (int i = m - 1; i < user.length(); i++) {
                    users.add(user.optJSONObject(i).getString("userid"));
                }
                JSONArray a = agent3010011Bean.getPunchCardRecord(3, start, end, users).getJSONArray("checkindata");
                records.put(a);
            } else {
                List<String> users = new ArrayList<>();
                for (int i = m - 1; i <= n - 1; i++) {
                    users.add(user.optJSONObject(i).getString("userid"));
                }
                JSONArray a = agent3010011Bean.getPunchCardRecord(3, start, end, users).getJSONArray("checkindata");
                records.put(a);
                gerRecords(user, m + 100, n + 100, records);

            }
        }
        return records;
    }

    /**
     * 发送生日祝福
     */
    @Schedule(minute = "30", hour = "9", persistent = false)
    public void sendBirthdayBless() {
        agent1000016Bean.initConfiguration();
        String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = finalFilePath.indexOf("WEB-INF");
        String filePath = new String(finalFilePath.substring(1, index));
        String pathString = new String(filePath.concat("rpt/"));

        String selectDate = BaseLib.formatDate("____-MM-dd%", new Date());
        List<SystemUser> list = systemUserBean.findByLikeBirthdayDateAndDeptno(selectDate);
        if (list != null && !list.isEmpty()) {
            log4j.info("----- 发送生日祝福开始----------");
            for (SystemUser s : list) {
                if ("V".equals(s.getSyncWeChatStatus())) {
                    try {
                        String materialId = agent1000016Bean.getMaterialId(agent1000016Bean.MEDIA_IMG, pathString.concat(agent1000016Bean.getBirthdatPicteureUrl(s.getDeptno())));
                        //发送消息
                        StringBuffer data = new StringBuffer("{");
                        data.append("'title':'").append(s.getUsername()).append(",生日快乐!").append("',");
                        data.append("'thumb_media_id':'").append(materialId).append("',");
                        data.append("'content':'").append("<img src=\"https://jrs.hanbell.com.cn/Hanbell-WCO/birthday.jpg?page=1\"></img>").append("',");
                        data.append("'safe':").append(2).append("}");
                        agent1000016Bean.sendMsgToUser(s.getUserid(), "mpnews", data.toString());
                        log4j.info(data.toString());
                    } catch (Exception e) {

                        log4j.info(s.getUserid() + "发送失败：" + e.toString());
                    }
                }
            }
            log4j.info("----- 发送生日祝福结束----------");
        }
    }

    /**
     * 发送年资祝福
     */
    @Schedule(minute = "31", hour = "9", persistent = false)
    public void sendWorkAgeBless() {
        agent1000016Bean.initConfiguration();
        String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = finalFilePath.indexOf("WEB-INF");
        String filePath = new String(finalFilePath.substring(1, index));
        String pathString = new String(filePath.concat("rpt/"));
        String selectDate = BaseLib.formatDate("____-MM-dd%", new Date());
        List<SystemUser> list = systemUserBean.findByLikeWorkingAgeBeginDateAndDeptno(selectDate);
        if (list != null && !list.isEmpty()) {
            log4j.info("----- 发送年资祝福开始----------");
            for (SystemUser s : list) {
                if ("V".equals(s.getSyncWeChatStatus())) {
                    try {
                        String materialId = agent1000016Bean.getMaterialId(agent1000016Bean.MEDIA_IMG, pathString.concat("work.jpg"));
                        //计算时间
                        Integer now = Integer.valueOf(BaseLib.formatDate("yyyy", new Date()));
                        Integer workYear = Integer.valueOf(BaseLib.formatDate("yyyy", s.getWorkingAgeBeginDate()));
                        //发送消息
                        StringBuffer data = new StringBuffer("{");
                        data.append("'title':'").append(s.getUsername()).append(",感谢您").append(now - workYear).append("年来在汉钟坚守初心，筑梦前行！").append("',");
                        data.append("'thumb_media_id':'").append(materialId).append("',");
                        data.append("'content':'").append("<img src=\"https://jrs.hanbell.com.cn/Hanbell-WCO/work.jpg?page=1\"></img>").append("',");
                        data.append("'safe':").append(2).append("}");
                        agent1000016Bean.sendMsgToUser(s.getUserid(), "mpnews", data.toString());
                        log4j.info(data.toString());
                    } catch (Exception e) {
                        log4j.info(s.getUserid() + "发送失败：" + e.toString());
                    }
                }
            }
            log4j.info("----- 发送年资祝福结束----------");
        }
    }

    @Schedule(minute = "00,30", hour = "8-18", persistent = false)
    public void sendOAUnsignedFormCount() throws ParseException {
        try {
            Date d = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, -30);
            String dateBegin = BaseLib.formatDate("yyyy-MM-dd HH:mm:00.000", cal.getTime());
            String dateEnd = BaseLib.formatDate("yyyy-MM-dd HH:mm:00.000", d);
            if ("08:00:00.000".equals(dateEnd.split(" ")[1]) || "18:30:00.000".equals(dateEnd.split(" ")[1])) {
                return;
            }
            agent1000022Bean.initConfiguration();
            List<Object[]> res = processInstanceBean.getWorkAssignmentGroupByUserid(dateBegin, dateEnd);
            for (Object[] obj : res) {
                StringBuffer text = new StringBuffer(agent1000022Bean.getWeChatTitle((String) obj[0]));
                text.append((String) obj[1]).append("：<br>您有").append(obj[2]).append("件新待办事项,请尽速签核<br>http://oa.hanbell.com.cn:8086/NaNaWeb/");
                agent1000022Bean.sendMsgToUser((String) obj[0], "text", text.toString());
            }
        } catch (Exception e) {
            log4j.error(e);
        }
    }

    @Schedule(minute = "00", hour = "20,22", persistent = false)
    public void sendOAUnsignedFormCount1() {
        try {
            Date d = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.HOUR, -2);
            String dateBegin = BaseLib.formatDate("yyyy-MM-dd hh:mm:00.000", cal.getTime());
            String dateEnd = BaseLib.formatDate("yyyy-MM-dd hh:mm:00.000", d);
            agent1000022Bean.initConfiguration();
            List<Object[]> res = processInstanceBean.getWorkAssignmentGroupByUserid(dateBegin, dateEnd);
            for (Object[] obj : res) {
                StringBuffer text = new StringBuffer(agent1000022Bean.getWeChatTitle((String) obj[0]));
                text.append((String) obj[1]).append("：<br>您有").append(obj[2]).append("件新待办事项,请尽速签核<br>http://oa.hanbell.com.cn:8086/NaNaWeb/");
                agent1000022Bean.sendMsgToUser((String) obj[0], "text", text.toString());
            }
        } catch (Exception e) {
            log4j.error(e);
        }
    }

    @Schedule(minute = "45", hour = "8,15", persistent = false)
    public void dailyMessagePrompt() {
        agent1000016Bean.initConfiguration();
        agent1000016Bean.sendMsgToUser(errMsgUser, "text", "企业微信排成成功执行");
    }
}
