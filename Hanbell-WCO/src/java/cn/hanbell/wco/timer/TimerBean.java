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
import com.lightshell.comm.BaseLib;
import java.util.ArrayList;
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
    private Agent1000002Bean wechatCorpBean;

    private List<Department> deptList;
    private List<Department> childDepts;
    private List<SystemUser> userList;

    private final Logger log4j = LogManager.getLogger("cn.hanbell.wco");

    public TimerBean() {

    }

    //@Schedule(minute = "45", hour = "7,16,23", persistent = false)
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
            String msg;
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
                    msg = wechatCorpBean.createEmployee(jo);
                    if (msg.equals("success")) {
                        user.setSyncWeChatDate(BaseLib.getDate());
                        user.setSyncWeChatStatus("V");
                        user.setOptdate(user.getSyncWeChatDate());
                        systemUserBean.update(user);
                    } else {
                        log4j.error(msg);
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
                                log4j.error(msg);
                            }
                        }
                    }
                }
            }
        }
    }

}
