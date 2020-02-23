/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.query;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.lazy.SystemUserModel;
import cn.hanbell.wco.web.SuperQueryBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "systemUserQueryBean")
@ViewScoped
public class SystemUserQueryBean extends SuperQueryBean<SystemUser> {

    @EJB
    private SystemUserBean systemUserBean;

    private String queryPosition;
    private String queryDeptna;

    private List<SystemUser> selectSystemUser;

    public SystemUserQueryBean() {
        super(SystemUser.class);
        selectSystemUser = new ArrayList<>();
    }

    @Override
    public void init() {
        this.superEJB = systemUserBean;
        setModel(new SystemUserModel(systemUserBean));
        this.model.getSortFields().put("userid", "ASC");
        //只有已同步企业微信人员才能显示
        model.getFilterFields().put("syncWeChatStatus =", "V");
        super.init();
    }

    @Override
    public void reset() {
        super.reset();
        this.model.getSortFields().put("userid", "ASC");
        model.getFilterFields().put("syncWeChatStatus =", "V");
    }

    @Override
    public void query() {
        if (this.model != null) {
            this.model.getFilterFields().clear();
            if (this.queryFormId != null && !"".equals(this.queryFormId)) {
                this.model.getFilterFields().put("userid", this.queryFormId);
            }
            if (this.queryName != null && !"".equals(this.queryName)) {
                this.model.getFilterFields().put("username", this.queryName);
            }
            if (this.queryPosition != null && !"".equals(this.queryPosition)) {
                this.model.getFilterFields().put("position", this.queryPosition);
            }
            if (this.queryDeptna != null && !"".equals(this.queryDeptna)) {
                this.model.getFilterFields().put("dept.dept", this.queryDeptna);
            }
            this.model.getSortFields().put("userid", "ASC");
            this.model.getFilterFields().put("syncWeChatStatus =", "V");
        }
    }

    public void closeMultiSelect() {
        if (entityList != null && !entityList.isEmpty()) {
            PrimeFaces.current().dialog().closeDynamic(entityList);
        } else {
            showErrorMsg("Error", "没有选择数据");
        }
    }

    /**
     * @return the queryDeptna
     */
    public String getQueryDeptna() {
        return queryDeptna;
    }

    /**
     * @param queryDeptna the queryDeptna to set
     */
    public void setQueryDeptna(String queryDeptna) {
        this.queryDeptna = queryDeptna;
    }

    /**
     * @return the selectSystemUser
     */
    public List<SystemUser> getSelectSystemUser() {
        return selectSystemUser;
    }

    /**
     * @param selectSystemUser the selectSystemUser to set
     */
    public void setSelectSystemUser(List<SystemUser> selectSystemUser) {
        this.selectSystemUser = selectSystemUser;
    }

    /**
     * @return the queryPosition
     */
    public String getQueryPosition() {
        return queryPosition;
    }

    /**
     * @param queryPosition the queryPosition to set
     */
    public void setQueryPosition(String queryPosition) {
        this.queryPosition = queryPosition;
    }

}
