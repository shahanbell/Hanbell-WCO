/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.query;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.wco.lazy.DepartmentModel;
import cn.hanbell.wco.web.SuperQueryBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "departmentQueryBean")
@ViewScoped
public class DepartmentQueryBean extends SuperQueryBean<Department>{
    
    
    
    @EJB
    private DepartmentBean departmentBean;


    private Department selectDepartment;

    public DepartmentQueryBean() {
        super(Department.class);
         selectDepartment = new Department();
    }
    @Override
    public void init() {
        this.superEJB = departmentBean;
        setModel(new DepartmentModel(departmentBean));
        this.model.getSortFields().put("id", "ASC");
        //只有已同步企业微信人员才能显示
        model.getFilterFields().put("syncWeChatStatus =", "V");
        super.init();
    }

    @Override
    public void reset() {
        super.reset();
        this.model.getSortFields().put("id", "ASC");
        model.getFilterFields().put("syncWeChatStatus =", "V");
    }

    @Override
    public void query() {
        if (this.model != null) {
            this.model.getFilterFields().clear();
            if (this.queryFormId != null && !"".equals(this.queryFormId)) {
                this.model.getFilterFields().put("deptno", this.queryFormId);
            }
            if (this.queryName != null && !"".equals(this.queryName)) {
                this.model.getFilterFields().put("dept", this.queryName);
            }
      
            this.model.getSortFields().put("id", "ASC");
            this.model.getFilterFields().put("syncWeChatStatus =", "V");
        }
    }

    public void closeMultiSelect() {
        System.out.println("selectDepartment=="+selectDepartment);
        if (selectDepartment != null && !"".equals(selectDepartment)) {
            PrimeFaces.current().dialog().closeDynamic(selectDepartment);
        } else {
            showErrorMsg("Error", "没有选择数据");
        }
    }

    public Department getSelectDepartment() {
        return selectDepartment;
    }

    public void setSelectDepartment(Department selectDepartment) {
        this.selectDepartment = selectDepartment;
    }
    
}
