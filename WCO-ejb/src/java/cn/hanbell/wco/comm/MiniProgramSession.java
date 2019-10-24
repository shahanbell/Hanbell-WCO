/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.comm;

/**
 *
 * @author kevindong
 */
public class MiniProgramSession {

    private String openId;
    private String sessionKey;
    private String unionId;
    private boolean authorized;
    private String employeeId;
    private String employeeName;
    private String defaultDeptId;
    private String defaultDeptName;

    public MiniProgramSession() {

    }

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return the sessionKey
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * @param sessionKey the sessionKey to set
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * @return the unionId
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * @param unionId the unionId to set
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * @return the authorized
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * @param authorized the authorized to set
     */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the defaultDeptId
     */
    public String getDefaultDeptId() {
        return defaultDeptId;
    }

    /**
     * @param defaultDeptId the defaultDeptId to set
     */
    public void setDefaultDeptId(String defaultDeptId) {
        this.defaultDeptId = defaultDeptId;
    }

    /**
     * @return the defaultDeptName
     */
    public String getDefaultDeptName() {
        return defaultDeptName;
    }

    /**
     * @param defaultDeptName the defaultDeptName to set
     */
    public void setDefaultDeptName(String defaultDeptName) {
        this.defaultDeptName = defaultDeptName;
    }

}
