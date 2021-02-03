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
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.ejb.Agent1000016Bean;
import cn.hanbell.wco.ejb.Agent3010011Bean;
import com.lightshell.comm.BaseLib;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private List<Department> deptList;
    private List<Department> childDepts;
    private List<SystemUser> userList;

    private final Logger log4j = LogManager.getLogger("cn.hanbell.wco");

    public TimerBean() {

    }

    @Schedule(minute = "45", hour = "7,16,23", persistent = false)
    public void syncWXWorkOrganizationByEAP() {
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
            String msg = "";
            for (SystemUser user : userList) {
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
                        log4j.error(user.getUserid()+"同步失败："+msg);
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
                                log4j.error(user.getUserid()+"同步失败："+msg);
                            }
                        }
                    }
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
        String selectDate = BaseLib.formatDate("____-MM-dd%", new Date());
        List<SystemUser> list = systemUserBean.findByLikeBirthdayDateAndDeptno(selectDate);
        if (list != null && !list.isEmpty()) {
             log4j.info("----- 发送生日祝福开始----------");
            for (SystemUser s : list) {
                if ("V".equals(s.getSyncWeChatStatus())) {
                    //发送消息
                    StringBuffer data = new StringBuffer("{");
                    data.append("'title':'").append(s.getUsername() + ",祝您生日快乐").append("',");
                    data.append("'description':'").append("愿您平安健康，事事舒心。也希望未来的日子里，您能实现心中所想，自由地追逐梦想！").append("',");
                    data.append("'url':'").append("").append("',");
                    data.append("'picurl':'").append(agent1000016Bean.getBirthdatPicteureUrl(s.getDeptno())).append("'}");
                    agent1000016Bean.sendMsgToUser("C2082", "news", data.toString());
                     log4j.info(data.toString());
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
        String selectDate = BaseLib.formatDate("____-MM-dd%", new Date());
        List<SystemUser> list = systemUserBean.findByLikeWorkingAgeBeginDateAndDeptno(selectDate);
        if (list != null && !list.isEmpty()) {
             log4j.info("----- 发送年资祝福结束----------");
            for (SystemUser s : list) {
                //计算时间
                Integer now = Integer.valueOf(BaseLib.formatDate("yyyy", new Date()));
                Integer workYear = Integer.valueOf(BaseLib.formatDate("yyyy", s.getWorkingAgeBeginDate()));
                if ("V".equals(s.getSyncWeChatStatus()) && (now - workYear) >= 1) {
                      //发送消息
                    StringBuffer data = new StringBuffer("{");
                    data.append("'title':'").append(s.getUsername()).append(",感谢您").append(now - workYear).append( "年来在汉钟坚守初心，筑梦前行！").append("',");
                    data.append("'description':'").append("',");
                    data.append("'url':'").append("").append("',");
                    data.append("'picurl':'").append(agent1000016Bean.getWorkingAgePicteureUrl(s.getDeptno())).append("'}");
                    agent1000016Bean.sendMsgToUser("C2082", "news", data.toString());
                    log4j.info(data.toString());
                }
            }
             log4j.info("----- 发送年资祝福结束----------");
        }
    }
}
