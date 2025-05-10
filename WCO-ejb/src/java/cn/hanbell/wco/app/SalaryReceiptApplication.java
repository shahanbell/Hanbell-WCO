/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.app;

import java.util.Date;
import java.util.List;

/**
 *
 * @author C2082
 */

public class SalaryReceiptApplication {
    
    private String userId;
    private String salaryDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(String salaryDate) {
        this.salaryDate = salaryDate;
    }

    @Override
    public String toString() {
        return "SalaryReceiptApplication{" + "userId=" + userId + ", salaryDate=" + salaryDate + '}';
    }


    
}
