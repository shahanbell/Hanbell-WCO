/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.lazy.DepartmentModel;
import cn.hanbell.wco.web.SuperSingleBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.JsonObject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "organizationManagedBean")
@SessionScoped
public class OrganizationManagedBean extends SuperSingleBean<Department> {

    @EJB
    private cn.hanbell.hrm.ejb.DepartmentBean hrmDepartmentBean;
    @EJB
    private cn.hanbell.hrm.ejb.EmployeeBean hrmEmployeeBean;

    @EJB
    private DepartmentBean departmentBean;
    @EJB
    private SystemUserBean systemUserBean;

    @EJB
    private Agent1000002Bean wechatCorpBean;

    private TreeNode rootNode;
    private TreeNode selectedNode;
    private List<Department> deptList;

    private SystemUser currentUser;
    private List<SystemUser> userList;

    public OrganizationManagedBean() {
        super(Department.class);
    }

    @Override
    public void init() {
        wechatCorpBean.initConfiguration();
        userList = new ArrayList<>();
        superEJB = departmentBean;
        model = new DepartmentModel(departmentBean);
        super.init();
        initTree();
    }

    private void initTree() {
        setRootNode(new DefaultTreeNode(new Department("Root", "Root"), null));
        getRootNode().setExpanded(true);
        Department root = departmentBean.findByDeptno("00000");
        if (root != null) {
            deptList = departmentBean.findByPId(root.getId());
        }
        if (deptList != null && !deptList.isEmpty()) {
            for (Department p : deptList) {
                TreeNode n = new DefaultTreeNode(p, getRootNode());
                //n.setExpanded(true);
                initTree(p, n);
            }
        }
    }

    private void initTree(Department position, TreeNode node) {
        List<Department> departments = departmentBean.findByPId(position.getId());
        if (departments != null && !departments.isEmpty()) {
            for (Department p : departments) {
                TreeNode n = new DefaultTreeNode(p, node);
                //n.setExpanded(true);
                initTree(p, n);
            }
        }
    }

    public void loadUser(Department dept, boolean inservice) {
        if (dept != null) {
            entityList = departmentBean.findByPId(dept.getId());
            if (entityList != null && !entityList.isEmpty()) {
                for (Department e : entityList) {
                    if (e.getStatus().equals("X")) {
                        continue;
                    }
                    loadUser(e, inservice);
                }
            }
            if (inservice) {
                userList.addAll(systemUserBean.findByDeptnoAndOnJob(dept.getDeptno()));
            } else {
                userList.addAll(systemUserBean.findByDeptno(dept.getDeptno()));
            }
        }
    }

    public void loadUserAll() {
        if (currentEntity != null) {
            userList.clear();
            loadUser(currentEntity, false);
            if (userList.size() > 1) {
                userList.sort((SystemUser o1, SystemUser o2) -> {
                    if (o1.getUserid().compareTo(o2.getUserid()) < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
            }
        } else {
            showInfoMsg("Info", "请先选择部门");
        }
    }

    public void loadUserOnJob() {
        if (currentEntity != null) {
            userList.clear();
            loadUser(currentEntity, true);
            if (userList.size() > 1) {
                userList.sort((SystemUser o1, SystemUser o2) -> {
                    if (o1.getUserid().compareTo(o2.getUserid()) < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
            }
        } else {
            showInfoMsg("Info", "请先选择部门");
        }
    }

    public void syncDept() {
        if (currentEntity != null) {
            if (syncDept(currentEntity)) {
                showInfoMsg("Info", "同步成功");
            }
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
                dept.setSyncWeChatDate(this.getDate());
                dept.setSyncWeChatStatus("V");
                dept.setOptdate(dept.getSyncWeChatDate());
                departmentBean.update(dept);
                entityList = departmentBean.findByPId(dept.getId());
                if (entityList != null && !entityList.isEmpty()) {
                    for (Department e : entityList) {
                        ret = ret && syncDept(e);
                    }
                }
            } else {
                ret = false;
                showErrorMsg("Error", msg);
            }
            return ret;
        } else {
            if (dept.getStatus().equals("X")) {
                //先删除子阶
                entityList = departmentBean.findByPId(dept.getId());
                if (entityList != null && !entityList.isEmpty()) {
                    for (Department e : entityList) {
                        ret = ret && syncDept(e);
                    }
                }
                if (ret) {
                    msg = wechatCorpBean.deleteDepartment(dept.getId());
                    if (msg.equals("success")) {
                        dept.setSyncWeChatDate(this.getDate());
                        dept.setSyncWeChatStatus("X");
                        dept.setOptdate(dept.getSyncWeChatDate());
                        departmentBean.update(dept);
                    } else {
                        ret = false;
                        showErrorMsg("Error", msg);
                    }
                }
                return ret;
            } else {
                entityList = departmentBean.findByPId(dept.getId());
                if (entityList != null && !entityList.isEmpty()) {
                    for (Department e : entityList) {
                        ret = ret && syncDept(e);
                    }
                }
                if (("N".equals(dept.getSyncWeChatStatus()) || dept.getSyncWeChatDate().before(dept.getOptdate())) && ret) {
                    msg = wechatCorpBean.updateDepartment(jo);
                    if (msg.equals("success")) {
                        dept.setSyncWeChatDate(this.getDate());
                        dept.setSyncWeChatStatus("V");
                        dept.setOptdate(dept.getSyncWeChatDate());
                        departmentBean.update(dept);
                    } else {
                        ret = false;
                        showErrorMsg("Error", msg);
                    }
                }
                return ret;
            }
        }
    }

    public void syncDeptByHRM() {
        try {
            // 同步部门
            List<cn.hanbell.hrm.entity.Department> departmentList = hrmDepartmentBean.findAll();
            if (departmentList != null && !departmentList.isEmpty()) {
                departmentList.forEach((hd) -> {
                    // EAP
                    cn.hanbell.eap.entity.Department ep = null;
                    cn.hanbell.hrm.entity.Department hp = hrmDepartmentBean.findByDepartmentId(hd.getParentId());
                    if (hp != null) {
                        ep = departmentBean.findByDeptno(hp.getCode());
                    }
                    cn.hanbell.eap.entity.Department ed = departmentBean.findByDeptno(hd.getCode());
                    if (ed == null) {
                        ed = new cn.hanbell.eap.entity.Department();
                        ed.setDeptno(hd.getCode());
                        ed.setDept(hd.getName());
                        if (ep != null) {
                            ed.setParentDept(ep);
                        }
                        if (hd.getFlag()) {
                            ed.setStatus("N");
                        } else {
                            ed.setStatus("X");
                        }
                        ed.setCreatorToSystem();
                        ed.setCredateToNow();
                        departmentBean.persist(ed);
                    } else {
                        if (!ed.getDept().equals(hd.getName()) || !Objects.equals(ed.getParentDept(), ep)) {
                            ed.setDept(hd.getName());
                            if (ep != null) {
                                ed.setParentDept(ep);
                            }
                            if (!hd.getFlag()) {
                                ed.setStatus("X");
                            }
                            ed.setOptdate(hd.getLastModifiedDate());
                            departmentBean.update(ed);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            showErrorMsg("Error", ex.toString());
        }
    }

    public void syncEmployee() {
        if (userList != null && !userList.isEmpty()) {
            String msg;
            boolean ret = true;
            for (SystemUser user : userList) {
                if (user.getSyncWeChatStatus() != null && "X".equals(user.getSyncWeChatStatus())) {
                    continue;
                }
                if (user.getPhone() == null || "".equals(user.getPhone()) || user.getDeptno() == null || "".equals(user.getDeptno())) {
                    continue;
                }
                JsonObject jo = systemUserBean.createJsonObjectBuilder(user).build();
                if (user.getSyncWeChatStatus() == null || user.getSyncWeChatDate() == null) {
                    msg = wechatCorpBean.createEmployee(jo);
                    if (msg.equals("success")) {
                        user.setSyncWeChatDate(this.getDate());
                        user.setSyncWeChatStatus("V");
                        user.setOptdate(user.getSyncWeChatDate());
                        systemUserBean.update(user);
                    } else {
                        ret = false;
                        showErrorMsg("Error", msg);
                    }
                } else {
                    if (user.getStatus().equals("X")) {
                        msg = wechatCorpBean.deleteEmployee(user.getUserid());
                        if (msg.equals("success")) {
                            user.setSyncWeChatDate(this.getDate());
                            user.setSyncWeChatStatus("X");
                            user.setOptdate(user.getSyncWeChatDate());
                            systemUserBean.update(user);
                        } else {
                            ret = false;
                            showErrorMsg("Error", msg);
                        }
                    } else {
                        if (("N".equals(user.getSyncWeChatStatus()) || user.getSyncWeChatDate().before(user.getOptdate()))) {
                            msg = wechatCorpBean.updateEmployee(jo);
                            if (msg.equals("success")) {
                                user.setSyncWeChatDate(this.getDate());
                                user.setSyncWeChatStatus("V");
                                user.setOptdate(user.getSyncWeChatDate());
                                systemUserBean.update(user);
                            } else {
                                ret = false;
                                showErrorMsg("Error", msg);
                            }
                        }
                    }
                }
            }
            if (ret) {
                showInfoMsg("Info", "同步成功");
            }
        } else {
            showInfoMsg("Info", "没有需要同步的资料");
        }
    }

    public void syncEmployeeByHRM() {
        if (currentEntity != null) {
            if (syncEmployeeByHRM(currentEntity)) {
                loadUserOnJob();
                showInfoMsg("Info", "同步成功");
            }
        }
    }

    private boolean syncEmployeeByHRM(Department dept) {
        // 同步人员
        if (dept != null) {
            try {
                List<cn.hanbell.hrm.entity.Employee> employeeList = hrmEmployeeBean.findByDepartmentCode(dept.getDeptno());
                if (employeeList != null && !employeeList.isEmpty()) {
                    employeeList.forEach((e) -> {
                        boolean flag = false;
                        // EAP
                        cn.hanbell.eap.entity.SystemUser eu = systemUserBean.findByUserId(e.getCode());
                        if (eu == null) {
                            eu = new cn.hanbell.eap.entity.SystemUser();
                            eu.setUserid(e.getCode());
                            eu.setUsername(e.getCnName());
                            eu.setDeptno(e.getDepartment().getCode());
                            eu.setPhone(e.getMobilePhone());
                            eu.setEmail(e.getEmail());
                            eu.setCreatorToSystem();
                            eu.setCredateToNow();
                            eu.setOptdate(eu.getCredate());
                            systemUserBean.persist(eu);
                        } else {
                            if (eu.getOptdate() != null && eu.getOptdate().before(e.getLastModifiedDate())) {
                                eu.setUsername(e.getCnName());
                                eu.setDeptno(e.getDepartment().getCode());
                                eu.setPhone(e.getMobilePhone());
                                eu.setEmail(e.getEmail());
                                eu.setOptuserToSystem();
                                eu.setOptdate(e.getLastModifiedDate());
                                flag = true;
                            }
                            if (!eu.getStatus().equals("X") && e.getLastModifiedDate().compareTo(e.getLastWorkDate()) != -1) {
                                eu.setStatus("X");
                                eu.setOptuserToSystem();
                                eu.setOptdate(e.getLastModifiedDate());
                            }
                            if (flag) {
                                systemUserBean.update(eu);
                            }
                        }
                    });
                }
                entityList = departmentBean.findByPId(dept.getId());
                if (entityList != null && !entityList.isEmpty()) {
                    for (Department e : entityList) {
                        if (e.getStatus().equals("X")) {
                            continue;
                        }
                        syncEmployeeByHRM(e);
                    }
                }
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
                return false;
            }
        }
        return true;
    }

    /**
     * @return the rootNode
     */
    public TreeNode getRootNode() {
        return rootNode;
    }

    /**
     * @param rootNode the rootNode to set
     */
    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * @return the selectedNode
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @param selectedNode the selectedNode to set
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
        if (selectedNode != null) {
            currentEntity = (Department) selectedNode.getData();
            loadUserOnJob();
        }
    }

    /**
     * @return the currentUser
     */
    public SystemUser getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(SystemUser currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return the userList
     */
    public List<SystemUser> getUserList() {
        return userList;
    }

    /**
     * @param userList the userList to set
     */
    public void setUserList(List<SystemUser> userList) {
        this.userList = userList;
    }

}
