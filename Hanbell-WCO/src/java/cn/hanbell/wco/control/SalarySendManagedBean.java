/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.ejb.SalarySendBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SalarySend;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.lazy.DepartmentModel;
import cn.hanbell.wco.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "salarySendManagedBean")
@SessionScoped
public class SalarySendManagedBean extends SuperSingleBean<Department> {

    @EJB
    private Agent1000002Bean agent1000002Bean;
    @EJB
    private SalarySendBean salarySendBean;
    @EJB
    private DepartmentBean departmentBean;
    @EJB
    private Agent1000002Bean wechatCorpBean;
    @EJB
    private SystemUserBean systemUserBean;
    private TreeNode rootNode;
    private TreeNode selectedNode;
    private List<Department> deptList;
    private List<SalarySend> salaryList;
    private Set<SystemUser> userSet;

    public SalarySendManagedBean() {
        super(Department.class);
    }

    @Override
    public void init() {
        wechatCorpBean.initConfiguration();
        salaryList = new ArrayList<>();
        userSet = new HashSet<>();
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
                        // 已停用部门无需载入
                        continue;
                    }
                    loadUser(e, inservice);
                }
            }
            long date = new Date().getTime() + 1000 * 60 * 60 * 24 * 31 + 1000 * 60 * 60 * 24 * 31;
            String dateString = BaseLib.formatDate("yyyyMM", new Date(date));
            if (inservice) {
                salaryList.addAll(salarySendBean.findByTaskidAndDeptno("XZHZ" + dateString, dept.getDeptno()));
                userSet.addAll(systemUserBean.findBySyncWeChatStatusAndDeptno(dept.getDeptno()));
            } else {
                salaryList.addAll(salarySendBean.findByTaskidAndDeptno("XZHZ" + dateString, dept.getDeptno()));
                userSet.addAll(systemUserBean.findBySyncWeChatStatusAndDeptno(dept.getDeptno()));
            }
        }
    }

    public void loadUserOnJob() {
        if (currentEntity != null) {
            salaryList.clear();
            userSet.clear();
            loadUser(currentEntity, true);
            if (salaryList.size() > 1) {
                salaryList.sort((SalarySend o1, SalarySend o2) -> {
                    if (o1.getEmployeeid().compareTo(o2.getEmployeeid()) < 0) {
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

    public void sendmsg() {
        if (userSet.isEmpty()) {
        }
        loadUserOnJob();
        StringBuffer data=new StringBuffer();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dateString = sdf.format(cal.getTime());
        String taskid = salarySendBean.getTaskId("XZHZ" + dateString);
        //发送人
        StringBuffer userid = new StringBuffer();
        for (SystemUser user : userSet) {
            SalarySend s = new SalarySend();
            s.setEmployeeid(user.getUserid());
            s.setTaskid(taskid);
            s.setTaskname(dateString + "月薪资发放");
            s.setEmployeename(user.getUsername());
            s.setDeptno(user.getDeptno());
            s.setDept(user.getDept().getDept());
            s.setSendtime(new Date());
            s.setStatus("N");
            //排除已经发过的员工和一些劳务等临时账号
            if (!user.getUserid().startsWith("CL") && !user.getUserid().startsWith("CM") && !user.getUserid().startsWith("CG")) {
                SalarySend salarySend = salarySendBean.findByTaskNameAndEmployeeid(s.getTaskname(), user.getUserid());
                if (salarySend == null) {
                    salarySendBean.persist(s);
                    userid.append(user.getUserid());
                    userid.append("|");
                }
            }
        }
        //清除最后一个|
        String users = userid.substring(0, userid.length() - 1);
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
        agent1000002Bean.sendMsgToUser(users, "taskcard", data.toString());
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

    public List<SalarySend> getSalaryList() {
        return salaryList;
    }

    public void setSalaryList(List<SalarySend> salaryList) {
        this.salaryList = salaryList;
    }

}
