/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.wco.entity.JobTask;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author C0160
 */
public class ResponseJobTask extends ResponseData<JobTask> {

    private List<String> LS = new ArrayList<>();

    public ResponseJobTask() {

    }

    public ResponseJobTask(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public void setData(List<JobTask> data) {
        super.setData(data);
        if (data != null && !data.isEmpty()) {
            for (JobTask jt : data) {
                if (!LS.contains(jt.getPlannedStartDate())) {
                    LS.add(jt.getPlannedStartDate());
                }
            }
        }
    }

    /**
     * @return the LS
     */
    public List<String> getLS() {
        return LS;
    }

    /**
     * @param LS the LS to set
     */
    public void setLS(List<String> LS) {
        this.LS = LS;
    }

}
