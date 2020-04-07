/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.eap.ejb.CompanyBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.ejb.JobTaskBean;
import cn.hanbell.wco.ejb.Prg9f247ab6d5e4Bean;
import cn.hanbell.wco.ejb.WeChatUserBean;
import cn.hanbell.wco.entity.JobTask;
import cn.hanbell.wco.entity.WeChatSession;
import cn.hanbell.wco.entity.WeChatUser;
import cn.hanbell.wco.comm.MiniProgramSession;
import com.lightshell.comm.BaseLib;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author kevindong
 */
@Stateless
@Path("prg9f247ab6d5e4")
public class Prg9f247ab6d5e4FacadeREST extends WeChatOpenFacade<WeChatUser> {

    @EJB
    private JobTaskBean jobTaskBean;
    @EJB
    private Prg9f247ab6d5e4Bean prg9f247ab6d5e4Bean;
    @EJB
    private WeChatUserBean wechatUserBean;
    @EJB
    private SystemUserBean systemUserBean;
    @EJB
    private CompanyBean companyBean;

    public Prg9f247ab6d5e4FacadeREST() {
        super(WeChatUser.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return wechatUserBean.getEntityManager();
    }

    @DELETE
    @Path("jobtask/{id}")
    @Produces({"application/json"})
    public ResponseMessage deleteJobTask(@PathParam("id") Integer id, @QueryParam("openid") String openid,
            @QueryParam("sessionkey") String sessionkey) {
        if (openid == null || sessionkey == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        log4j.info(String.format("deleteJobTask openid:%s sessionkey:%s", openid, sessionkey));
        try {
            JobTask jt = jobTaskBean.findById(id);
            if (jt != null) {
                jobTaskBean.delete(jt);
                return new ResponseMessage("200", "删除成功");
            } else {
                return new ResponseMessage("404", "删除异常");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseMessage("500", "系统异常");
        }
    }

    @GET
    @Path("checkcode")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseMessage getCheckCode(@QueryParam("openid") String openid, @QueryParam("sessionkey") String sessionkey,
            @QueryParam("mobile") String mobile) {
        if (openid == null || sessionkey == null || mobile == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            WeChatUser wcu = wechatUserBean.findByOpenId(openid);
            WeChatSession wcs = wechatSessionBean.findByOpenIdAndSessionId(openid, sessionkey);
            if (wcu != null && wcs != null) {
                String code = this.getCheckCode();
                wcs.setCheckCode(code);
                wcs.setExpiresIn(180);// 秒
                wcs.setCredateToNow();
                wcs.setCreatorToSystem();
                wechatSessionBean.update(wcs);
                String[] value = new String[]{code, "3"};
                BaseLib.sendShortMessage("8aaf07085adadc12015aeae7d82003a4", mobile, "460510", value);
                return new ResponseMessage("200", "已经发送");
            } else {
                return new ResponseMessage("404", "授权异常");
            }
        } catch (Exception ex) {
            return new ResponseMessage("500", "发送异常");
        }
    }

    @GET
    @Path("jobtask")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseJobTask getJobTask(@QueryParam("openid") String openid, @QueryParam("sessionkey") String sessionkey,
            @QueryParam("employeeid") String employeeid) {
        try {
            List<JobTask> list = jobTaskBean.findByLeaderIdAndStatus(employeeid, "N");
            ResponseJobTask res = new ResponseJobTask("200", "获取成功");
            res.setData(list);
            return res;
        } catch (Exception ex) {
            return new ResponseJobTask("500", "系统异常");
        }
    }

    @GET
    @Path("session")
    @Produces({MediaType.APPLICATION_JSON})
    public MiniProgramSession getSession(@QueryParam("code") String code) {
        JSONObject jo = prg9f247ab6d5e4Bean.getSessionInfo(prg9f247ab6d5e4Bean.getAppID(),
                prg9f247ab6d5e4Bean.getAppSecret(), code);
        if (jo != null) {
            log4j.info("微信小程序code2session:" + jo.toString());
            String uuid = getUUID();
            MiniProgramSession s = new MiniProgramSession();
            s.setOpenId(jo.getString("openid"));
            s.setSessionKey(uuid);// 3rd_session
            if (jo.has("unionid")) {
                s.setUnionId(jo.getString("unionid"));
            }
            wechatSessionBean.persistIfNotExist(jo.getString("openid"), jo.getString("session_key"), uuid);
            currentEntity = wechatUserBean.findByOpenId(jo.getString("openid"));
            if (currentEntity != null) {
                s.setAuthorized(currentEntity.getAuthorized());
                s.setEmployeeId(currentEntity.getEmployeeId());
                s.setEmployeeName(currentEntity.getEmployeeName());
            }
            return s;
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @POST
    @Path("jobtask")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage createJobTask(JobTask entity, @QueryParam("openid") String openid,
            @QueryParam("sessionkey") String sessionkey) {
        if (entity == null || openid == null || sessionkey == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            if (wechatSessionBean.has(openid, sessionkey)) {
                jobTaskBean.persist(entity);
                return new ResponseMessage("201", "提交成功");
            } else {
                return new ResponseMessage("401", "会话异常");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseMessage("500", "系统异常");
        }
    }

    @POST
    @Path("wechatuser")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage createWeChatUser(WeChatUser entity) {
        if (entity == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            if (!wechatUserBean.has(entity.getOpenId())) {
                entity.setAuthorized(false);
                entity.setStatusToNew();
                wechatUserBean.persist(entity);
                return new ResponseMessage("201", "授权成功");
            } else {
                return new ResponseMessage("200", "授权成功");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseMessage("500", "系统异常");
        }
    }

    @PUT
    @Path("jobtask/{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage updateJobTask(@PathParam("id") Integer id, JobTask entity, @QueryParam("openid") String openid,
            @QueryParam("sessionkey") String sessionkey) {
        if (id == null || entity == null || openid == null || sessionkey == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            if (wechatSessionBean.has(openid, sessionkey)) {
                JobTask jt = jobTaskBean.findById(id);
                if (jt != null) {
                    jt.setName(entity.getName());
                    if (entity.getDescription() != null) {
                        jt.setDescription(entity.getDescription());
                    }
                    jt.setLeaderId(entity.getLeaderId());
                    jt.setLeader(entity.getLeader());
                    jt.setPlannedStartDate(entity.getPlannedStartDate());
                    jt.setPlannedStartTime(entity.getPlannedStartTime());
                    jt.setPlannedFinishDate(entity.getPlannedFinishDate());
                    jt.setPlannedFinishTime(entity.getPlannedFinishTime());
                    if (entity.getActualStartDate() != null) {
                        jt.setActualStartDate(entity.getActualStartDate());
                    }
                    if (entity.getActualStartTime() != null) {
                        jt.setActualStartTime(entity.getActualStartTime());
                    }
                    if (entity.getActualFinishDate() != null) {
                        jt.setActualFinishDate(entity.getActualFinishDate());
                    }
                    if (entity.getActualFinishTime() != null) {
                        jt.setActualFinishTime(entity.getActualFinishTime());
                    }
                    if (entity.getLocation() != null) {
                        jt.setLocation(entity.getLocation());
                    }
                    jt.setStatus(entity.getStatus());
                    jobTaskBean.update(jt);
                    return new ResponseMessage("200", "更新成功");
                } else {
                    return new ResponseMessage("404", "更新异常");
                }
            } else {
                return new ResponseMessage("401", "会话异常");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseMessage("500", "系统异常");
        }
    }

    @PUT
    @Path("wechatuser/{openid}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseSession updateWeChatUser(@PathParam("openid") String openid, WeChatUser entity,
            @QueryParam("sessionkey") String sessionkey, @QueryParam("checkcode") String checkcode) {
        if (openid == null || entity == null || sessionkey == null || checkcode == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            WeChatSession wcs = wechatSessionBean.findByCheckCode(openid, sessionkey, checkcode);
            if (wcs != null) {
                WeChatUser wcu = wechatUserBean.findByOpenId(openid);
                if (wcu != null) {
                    wcu.setEmployeeId(entity.getEmployeeId());
                    wcu.setEmployeeName(entity.getEmployeeName());
                    wcu.setMobile(entity.getMobile());
                    // 需要加入工号+预留手机检查
                    SystemUser su = systemUserBean.findByUserId(entity.getEmployeeId());
                    if (null == su) {
                        return new ResponseSession("401", "授权工号不存在");
                    } else {
                        if (!su.getPhone().equals(entity.getMobile())) {
                            return new ResponseSession("401", "手机号与企业预留不一致");
                        }
                    }
                    wcu.setAuthorized(Boolean.TRUE);
                    wcu.setStatus("V");
                    wechatUserBean.update(wcu);
                    ResponseSession session = new ResponseSession("200", "更新成功");
                    session.setAuthorized(wcu.getAuthorized());
                    session.setEmployeeId(wcu.getEmployeeId());
                    session.setEmployeeName(wcu.getEmployeeName());
                    //session 加入部门和事业单位
                    session.setDeptno(su.getDeptno());
                    session.setDeptName(su.getDept().getDept());
                    session.setCompany(su.getDept().getCompany());
                    session.setCompanyName(companyBean.findByCompany(session.getCompany()).getName());
                    // 更新WeChatSession状态
                    wcs.setStatus("V");
                    wcs.setCfmdateToNow();
                    wechatSessionBean.update(wcs);

                    return session;
                } else {
                    return new ResponseSession("401", "授权异常");
                }
            } else {
                return new ResponseSession("404", "验证码错误或失效");
            }
        } catch (Exception ex) {
            log4j.error(ex);
            return new ResponseSession("500", "系统异常");
        }
    }

}
