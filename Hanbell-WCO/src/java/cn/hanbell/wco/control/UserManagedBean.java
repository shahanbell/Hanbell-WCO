/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemGrantPrg;
import cn.hanbell.eap.entity.SystemUser;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "userManagedBean")
@SessionScoped
public class UserManagedBean implements Serializable {

    @EJB
    private SystemUserBean systemUserBean;

    private SystemUser currentUser;
    private String userid;
    private String mobile;
    private String email;
    private String pwd;
    private String newpwd;
    private String secpwd;
    private boolean status;

    private List<SystemGrantPrg> systemGrantPrgList;

    public UserManagedBean() {
        status = false;
    }

    public boolean checkUser() {
        return true;
    }

    public SystemUser findById(int id) {
        return systemUserBean.findById(id);
    }

    public String login() {
        if (getUserid().length() == 0 || getPwd().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "请输入用户名和密码"));
            return "";
        }
        secpwd = BaseLib.securityMD5(getPwd());
        try {
            SystemUser u = null;
            if ("Admin".equals(userid) || "admin".equals(userid)) {
                u = systemUserBean.findByUserIdAndPwd(userid, secpwd);
                if (u == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "用户名或密码错误"));
                    status = false;
                    return "";
                }
            } else if (cn.hanbell.util.BaseLib.ADAuth("172.16.10.190:389", userid + "@hanbell.com.cn", pwd)) {
                u = systemUserBean.findByUserId(getUserid());
            }
            if (u != null) {
                currentUser = u;
                status = true;
                mobile = u.getUserid();
                updateLoginTime();
                return "home";
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "用户名或密码不正确！"));
            status = false;
            return "login";
        }
        return "";
    }

    public String logout() {
        if (status) {
            currentUser = null;
            status = false;
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return "login";
        } else {
            return "home";
        }
    }

    public void update() {
        if (currentUser != null) {
            if (mobile != null && !mobile.equals("") && !mobile.equals(currentUser.getUserid())) {
                currentUser.setUserid(mobile);
            }
            if (email != null && !email.equals("") && !email.equals(currentUser.getEmail())) {
                currentUser.setEmail(email);
            }
            try {
                systemUserBean.update(currentUser);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新成功"));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "更新失败！"));
            }
        }
    }

    public void updateLoginTime() {
        if (currentUser != null) {
            currentUser.setLastlogin(currentUser.getNewestlogin());
            currentUser.setNewestlogin(BaseLib.getDate());
            update();
        }
    }

    public void updatePwd() {
        secpwd = BaseLib.securityMD5(getPwd());
        SystemUser u = systemUserBean.findByUserIdAndPwd(getUserid(), getSecpwd());
        if (u != null) {
            secpwd = BaseLib.securityMD5(newpwd);
            currentUser.setPassword(secpwd);
            update();
            pwd = "";
            newpwd = "";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新密码成功"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "原密码错误"));
        }
    }

    /**
     * @return the currentUser
     */
    public SystemUser getCurrentUser() {
        return currentUser;
    }

    public boolean getStatus() {
        return status;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * @return the newpwd
     */
    public String getNewpwd() {
        return newpwd;
    }

    /**
     * @param newpwd the newpwd to set
     */
    public void setNewpwd(String newpwd) {
        this.newpwd = newpwd;
    }

    /**
     * @return the secpwd
     */
    public String getSecpwd() {
        return secpwd;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the systemGrantPrgList
     */
    public List<SystemGrantPrg> getSystemGrantPrgList() {
        return systemGrantPrgList;
    }

    /**
     * @param systemGrantPrgList the systemGrantPrgList to set
     */
    public void setSystemGrantPrgList(List<SystemGrantPrg> systemGrantPrgList) {
        this.systemGrantPrgList = systemGrantPrgList;
    }

}
