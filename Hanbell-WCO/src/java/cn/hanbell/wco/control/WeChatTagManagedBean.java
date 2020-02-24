/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.control;

import cn.hanbell.eap.ejb.WeChatTagBean;
import cn.hanbell.eap.ejb.WeChatTagUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.eap.entity.WeChatTag;
import cn.hanbell.eap.entity.WeChatTagUser;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.lazy.WeChatTagModel;
import cn.hanbell.wco.web.SuperSingleBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.JsonObject;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "wechatTagManagedBean")
@SessionScoped
public class WeChatTagManagedBean extends SuperSingleBean<WeChatTag> {

    @EJB
    private cn.hanbell.hrm.ejb.EmployeeBean hrmEmployeeBean;
    @EJB
    private cn.hanbell.hrm.ejb.CodeInfoBean codeInfoBean;

    @EJB
    private WeChatTagBean wechatTagBean;
    @EJB
    private WeChatTagUserBean wechatTagUserBean;

    @EJB
    private Agent1000002Bean wechatCorpBean;

    private List<SystemUser> userList;
    private List<WeChatTag> tagList;
    private List<WeChatTagUser> tagUserList;
    private List<WeChatTagUser> selectTagUserList;

    public WeChatTagManagedBean() {
        super(WeChatTag.class);
    }

    @Override
    public void init() {
        wechatCorpBean.initConfiguration();
        userList = new ArrayList<>();
        tagList = new ArrayList<>();
        tagUserList = new ArrayList<>();
        selectTagUserList = new ArrayList<>();
        superEJB = wechatTagBean;
        model = new WeChatTagModel(wechatTagBean);
        model.getFilterFields().put("weChatStatus =", "V");
        super.init();
    }

    @Override
    public void query() {
        tagUserList.clear();
        model.getFilterFields().clear();
        if (queryName != null && !"".equals(queryName)) {
            model.getFilterFields().put("tagname", queryName);
        }
    }

    public void query(String weChatStatus) {
        query();
        model.getFilterFields().put("weChatStatus =", weChatStatus);
    }

    @Override
    public void reset() {
        tagUserList.clear();
        super.reset();
    }

    public void loadWeChatTagUser() {
        tagUserList = new ArrayList<>();
        if (currentEntity == null || currentEntity.getId() == null) {
            showErrorMsg("Error", "请选中标签");
            return;
        }
        tagUserList.addAll(wechatTagUserBean.findByTagid(currentEntity.getId()));
        if (tagUserList.size() > 1) {
            tagUserList.sort((WeChatTagUser o1, WeChatTagUser o2) -> {
                if (o1.getUserid().compareTo(o2.getUserid()) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            });
        }
    }

    public void loadWeChatTagUserAll() {
        //查询全部把选中的Tag清掉，删除做管控
        tagUserList.clear();
        tagUserList.addAll(wechatTagUserBean.findAll());
        if (tagUserList.size() > 1) {
            tagUserList.sort((WeChatTagUser o1, WeChatTagUser o2) -> {
                if (o1.getUserid().compareTo(o2.getUserid()) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            });
        }
    }

    public void syncTag() {
        //微信同步标签，对当前model中的数据进行更新
        tagUserList.clear();
        Boolean ret = true;
        String msg;
        tagList.clear();
        tagList.addAll(model.getDataList());
        if (tagList != null && !tagList.isEmpty()) {
            for (WeChatTag tg : tagList) {
                ret = true;
                //"V"代表已同步,"X"代表无效不执行同步
                if ("V".equals(tg.getWeChatStatus()) || "X".equals(tg.getWeChatStatus())) {
                    continue;
                }
                //新增
                if ("N".equals(tg.getWeChatStatus()) && !"X".equals(tg.getStatus())) {
                    JsonObject jo = wechatTagBean.createJsonObjectBuilder(tg).build();
                    msg = wechatCorpBean.createWeChatTag(jo);
                    if (msg.equals("success")) {
                        tg.setWeChatStatus("V");
                        tg.setOptdate(this.getDate());
                        wechatTagBean.update(tg);
                    } else {
                        ret = false;
                        showErrorMsg("Error", msg + "新增失败" + tg.getTagname());
                    }
                }
                //更新
                if ("U".equals(tg.getWeChatStatus()) && !"X".equals(tg.getStatus())) {
                    JsonObject jo = wechatTagBean.createJsonObjectBuilder(tg).build();
                    msg = wechatCorpBean.updateWeChatTag(jo);
                    if (msg.equals("success")) {
                        tg.setWeChatStatus("V");
                        tg.setOptdate(this.getDate());
                        wechatTagBean.update(tg);
                    } else {
                        ret = false;
                        showErrorMsg("Error", msg + "更新失败" + tg.getTagname());
                    }
                }
                //删除
                if ("U".equals(tg.getWeChatStatus()) && "X".equals(tg.getStatus())) {
                    //对于失效的标签删除、先删除组员
                    WeChatTagManagedBean.this.deleteWeChatTagUser(wechatTagUserBean.findByTagid(tg.getId()), tg.getId());
                    msg = wechatCorpBean.deleteWeChatTag(tg.getId());
                    if (msg.equals("success")) {
                        tg.setWeChatStatus("X");
                        tg.setOptdate(this.getDate());
                        wechatTagBean.update(tg);
                    } else {
                        ret = false;
                        showErrorMsg("Error", msg + "删除失败" + tg.getTagname());
                    }
                }
            }
            if (ret) {
                showInfoMsg("Info", "同步成功");
            }
        }
    }

    public boolean syncTagUser(List<WeChatTagUser> tagUserList) {
        //微信同步标签组员
        Boolean ret = true;
        String msg;
        if (tagUserList != null && !tagUserList.isEmpty()) {
            JsonObject jo = wechatTagUserBean.createJsonObjectBuilder(tagUserList, currentEntity.getId()).build();
            msg = wechatCorpBean.createWeChatTagUser(jo);
            if (!msg.equals("success")) {
                ret = false;
                showErrorMsg("Error", msg);
            }
        }
        return ret;
    }

    public void syncTagByHRM() {
        try {
            //标签更新数据源HR CodeInfo表
            List<cn.hanbell.hrm.entity.CodeInfo> infolist = codeInfoBean.findByDecisionLevel();
            if (infolist != null && !infolist.isEmpty()) {
                infolist.forEach((ld) -> {
                    WeChatTag ed = wechatTagBean.findByTagcode(ld.getCodeInfoId());
                    WeChatTag wt = wechatTagBean.findByTagname(ld.getScName());
                    if (ed == null) {
                        //标签名相同不可新增
                        if (wt == null) {
                            ed = new WeChatTag();
                            ed.setTagcode(ld.getCodeInfoId());
                            ed.setTagname(ld.getScName());
                            if (ld.getFlag()) {
                                ed.setStatus("N");
                            } else {
                                ed.setStatus("X");
                            }
                            ed.setWeChatStatus("N");
                            ed.setCreator(userManagedBean.getUserid());
                            ed.setCredateToNow();
                            ed.setOptdate(covertTime(ld.getLastModifiedDate()));
                            wechatTagBean.persist(ed);
                        }
                    } else {
                        if (!ed.getTagname().equals(ld.getScName()) || ed.getOptdate().before(covertTime(ld.getLastModifiedDate()))) {
                            ed.setTagname(ld.getScName());
                            ed.setStatus(ld.getFlag() ? "N" : "X");
                            //如已同步微信的 则变更状态U
                            if ("V".equals(ed.getWeChatStatus())) {
                                ed.setWeChatStatus("U");
                            }
                            //如已失效的又重新生效 并且之前已同步过微信并删除的 重新更新微信状态未同步
                            if ("X".equals(ed.getWeChatStatus()) && ld.getFlag()) {
                                ed.setWeChatStatus("N");
                            }
                            ed.setCfmdate(getDate());
                            ed.setCfmuser(userManagedBean.getUserid());
                            ed.setOptdate(ld.getLastModifiedDate());
                            wechatTagBean.update(ed);
                        }
                    }
                });

            }
            //标签更新数据源HR员工表levelid
            List levelidList = null;//hrmEmployeeBean.findByLevelIdForWeChatTag();
            if (levelidList != null && !levelidList.isEmpty()) {
                for (Object ob : levelidList) {
                    if (ob != null) {
                        WeChatTag wct = wechatTagBean.findByTagcode(ob.toString());
                        if (wct == null) {
                            WeChatTag tag = new WeChatTag();
                            tag.setTagcode(ob.toString());
                            tag.setTagname(ob.toString());
                            tag.setStatus("N");
                            tag.setWeChatStatus("N");
                            tag.setCreator(userManagedBean.getUserid());
                            tag.setCredateToNow();
                            wechatTagBean.persist(tag);
                        }
                    }
                }
            }
            showInfoMsg("Info", "同步成功");
        } catch (Exception ex) {
            showErrorMsg("Error", ex.toString());
        }
    }

    @Override
    public void openDialog(String view) {
        if (currentEntity == null || currentEntity.getId() == null) {
            showErrorMsg("Error", "请选中标签");
            return;
        }
        if (!"V".equals(currentEntity.getWeChatStatus()) && !"U".equals(currentEntity.getWeChatStatus())) {
            showErrorMsg("Error", "未同步企业微信标签不能新增组员");
            return;
        }
        super.openDialog(view);
    }

    public void handleDialogReturnWhenDetailAllNew(SelectEvent event) {
        //同步企业微信、EAP新增资料
        if (currentEntity == null || currentEntity.getId() == null) {
            showErrorMsg("Error", "请选中标签");
            return;
        }
        try {
            if (event.getObject() != null) {
                List<SystemUser> userlist = (List<SystemUser>) event.getObject();
                List<WeChatTagUser> taguserlist = new ArrayList<>();
                userlist.forEach((user) -> {
                    //未已同步人员筛选
                    if (wechatTagUserBean.findByTagidAndUserid(currentEntity.getId(), user.getUserid()) == null) {
                        WeChatTagUser taguser = new WeChatTagUser();
                        taguser.setTagid(currentEntity.getId());
                        taguser.setUserid(user.getUserid());
                        taguser.setCredateToNow();
                        taguser.setCreator(userManagedBean.getUserid());
                        taguserlist.add(taguser);
                    }
                });
                //进行企业微信同步,成功后新增记录数据
                if (!taguserlist.isEmpty() && taguserlist.size() > 0) {
                    if (syncTagUser(taguserlist)) {
                        taguserlist.forEach((taguser) -> {
                            taguser.setWeChatStatus("V");
                            wechatTagUserBean.persist(taguser);
                        });
                        showInfoMsg("Info", "同步成功");
                    }
                }
            }
            loadWeChatTagUser();
        } catch (Exception e) {
            showErrorMsg("Error", e.toString());
        }
    }

    /**
     * 删除企业微信标签成员、并删除WeCharTagUser记录数据 只能一组组删除
     */
    public void deleteWeChatTagUser() {
        if (currentEntity == null || currentEntity.getId() == null) {
            showErrorMsg("Error", "请选中标签且只能按标签组删除");
            return;
        }
        if (selectTagUserList != null && !selectTagUserList.isEmpty()) {
            Boolean ret = true;
            String msg;
            JsonObject jo = wechatTagUserBean.createJsonObjectBuilder(selectTagUserList, currentEntity.getId()).build();
            msg = wechatCorpBean.deleteWeChatTagUser(jo);
            if (msg.equals("success")) {
                wechatTagUserBean.delete(selectTagUserList);
                loadWeChatTagUser();
            } else {
                showErrorMsg("Error", msg);
            }
        } else {
            showErrorMsg("Error", "请选择需要删除项");
        }
    }

    public void deleteWeChatTagUser(List<WeChatTagUser> list, int tagid) {
        if (list != null && !list.isEmpty()) {
            Boolean ret = true;
            String msg;
            JsonObject jo = wechatTagUserBean.createJsonObjectBuilder(list, tagid).build();
            msg = wechatCorpBean.deleteWeChatTagUser(jo);
            if (msg.equals("success")) {
                wechatTagUserBean.delete(list);
            } else {
                showErrorMsg("Error", msg);
            }
        }
    }

    public String syncWeChatStatus(String value) {
        if (value != null) {
            switch (value) {
                case "N":
                    return "未同步";
                case "U":
                    return "待更新";
                case "V":
                    return "已同步";
                default:
                    return "无效";
            }
        }
        return "";
    }

    private Date covertTime(Date date) {
        //最后修改时间去掉秒、毫秒
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = df.format(date.getTime());
        Date newDate = null;
        try {
            newDate = df.parse(str);
        } catch (Exception e) {
        }
        return newDate;
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

    @Override
    public void setCurrentEntity(WeChatTag currentEntity) {
        super.setCurrentEntity(currentEntity);
        if (currentEntity != null) {
            loadWeChatTagUser();
        }
    }

    /**
     * @return the tagList
     */
    public List<WeChatTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<WeChatTag> tagList) {
        this.tagList = tagList;
    }

    public List<WeChatTagUser> getTagUserList() {
        return tagUserList;
    }

    public void setTagUserList(List<WeChatTagUser> tagUserList) {
        this.tagUserList = tagUserList;
    }

    public List<WeChatTagUser> getSelectTagUserList() {
        return selectTagUserList;
    }

    public void setSelectTagUserList(List<WeChatTagUser> selectTagUserList) {
        this.selectTagUserList = selectTagUserList;
    }

}
