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
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.JsonObject;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "departmentManagedBean")
@SessionScoped
public class DepartmentManagedBean extends SuperSingleBean<Department> {

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

    public DepartmentManagedBean() {
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

    public void syncEmployee() {
        if (userList != null && !userList.isEmpty()) {

        }
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
