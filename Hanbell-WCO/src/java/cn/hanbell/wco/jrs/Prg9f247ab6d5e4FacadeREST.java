/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.eap.ejb.CompanyBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.wco.ejb.Prg9f247ab6d5e4Bean;
import cn.hanbell.wco.ejb.WeChatUserBean;
import cn.hanbell.wco.entity.WeChatSession;
import cn.hanbell.wco.entity.WeChatUser;
import cn.hanbell.wco.comm.MiniProgramSession;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import cn.hanbell.wco.ejb.WechatroleWechatauthorityBean;
import cn.hanbell.wco.ejb.WechatroleWechatuserBean;
import cn.hanbell.wco.entity.Wechatauthority;
import cn.hanbell.wco.entity.Wechatrole;
import cn.hanbell.wco.entity.WechatroleWechatuser;
import com.lightshell.comm.BaseLib;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
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
    private Prg9f247ab6d5e4Bean prg9f247ab6d5e4Bean;
    @EJB
    private WeChatUserBean wechatUserBean;

    @EJB
    private SystemUserBean systemUserBean;
    @EJB
    private CompanyBean companyBean;
    @EJB
    private WechatroleWechatuserBean wechatroleWechatuserBean;

    @EJB
    private WechatroleWechatauthorityBean wechatauthorityBean;

    @EJB
    private Agent1000002Bean wechatCorpBean;

    public Prg9f247ab6d5e4FacadeREST() {
        super(WeChatUser.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return wechatUserBean.getEntityManager();
    }

    @GET
    @Path("loginfaild")
    @Produces({MediaType.APPLICATION_JSON})
    public void loginFaild(@QueryParam("faild") String faild) {
        //前端wx.login()请求失败的回调请求，记录请求失败原因
        if (faild == null) {
            log4j.info("小程序登录失败：" + faild);
        } else {
            log4j.info("小程序登录失败：" + faild.toString());
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
                Map<String, Object> filter = new HashMap<>();
                filter.put("phone", mobile);
                List<SystemUser> user = systemUserBean.findByFilters(filter);
                if (user.size() == 1) {
                    wechatCorpBean.initConfiguration();
                    wechatCorpBean.sendMsgToUser(user.get(0).getUserid(), "text", String.format("[敬业汉钟]验证码:%s,请勿泄露给他人", code));
                }
                return new ResponseMessage("200", "已经发送");
            } else {
                return new ResponseMessage("404", "授权异常");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseMessage("500", "发送异常");
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
                s.setProfile(currentEntity.getProfile());
            }
            return s;
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @PUT
    @Path("wechatuser/{code}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage createWeChatUser(@PathParam("code") String code, @QueryParam("sessionkey") String sessionkey, WeChatUser entity) {
        if (entity.getOpenId() == null || sessionkey == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            WeChatUser wechatuser = wechatUserBean.findByOpenId(entity.getOpenId());
            if (wechatuser != null) {
                this.wechatUserBean.delete(wechatuser);
            }
            JSONObject jo = prg9f247ab6d5e4Bean.getWxAccessToken();
            JSONObject phone = prg9f247ab6d5e4Bean.getWxPhone(jo.getString("access_token"), code);
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("status", "N");
            filter.put("phone", phone.getJSONObject("phone_info").getString("phoneNumber"));
            List<SystemUser> users = this.systemUserBean.findByFilters(filter);
            if (users.size() == 1) {
                entity.setEmployeeId(users.get(0).getUserid());
                entity.setMobile(phone.getJSONObject("phone_info").getString("phoneNumber"));
                entity.setEmployeeName(users.get(0).getUsername());
                entity.setAuthorized(Boolean.TRUE);
                entity.setStatus("V");
                entity.setCfmdateToNow();
                this.wechatUserBean.persist(entity);
                ResponseSession session = new ResponseSession("200", "更新成功");
                session.setAuthorized(entity.getAuthorized());
                session.setEmployeeId(entity.getEmployeeId());
                session.setEmployeeName(entity.getEmployeeName());
                //session 加入部门和事业单位
                session.setDeptno(users.get(0).getDeptno());
                session.setDeptName(users.get(0).getDept().getDept());
                session.setCompany(users.get(0).getDept().getCompany());
                session.setCompanyName(companyBean.findByCompany(session.getCompany()).getName());
                session.setProfile(entity.getProfile());
                return session;
            }
            return new ResponseMessage("500", "HR数据异常，请联系管理员！");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseMessage("500", "系统异常");
        }
    }

    
    @PUT
    @Path("wechatuser/logout/{openId}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage logoutWeChatUser(@PathParam("openId") String openId) {
        if (openId == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            WeChatUser wechatuser = wechatUserBean.findByOpenId(openId);
            wechatuser.setAuthorized(Boolean.FALSE);
            wechatuser.setStatus("X");
            wechatuser.setCfmdateToNow();
            wechatuser.setCfmuser("wechat");
            return new ResponseMessage("200", "退出登录！");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseMessage("500", "系统异常");
        }
    }
    
//    @PUT
//    @Path("wechatuser/{openid}")
//    @Consumes({"application/json"})
//    @Produces({"application/json"})
//    public ResponseSession updateWeChatUser(@PathParam("openid") String openid, WeChatUser entity,
//            @QueryParam("sessionkey") String sessionkey, @QueryParam("checkcode") String checkcode) {
//        if (openid == null || entity == null || sessionkey == null || checkcode == null) {
//            throw new WebApplicationException(Response.Status.BAD_REQUEST);
//        }
//        try {
//            WeChatSession wcs = wechatSessionBean.findByCheckCode(openid, sessionkey, checkcode);
//            if (wcs != null) {
//                WeChatUser wcu = wechatUserBean.findByOpenId(openid);
//                if (wcu != null) {
//                    wcu.setEmployeeId(entity.getEmployeeId());
//                    wcu.setEmployeeName(entity.getEmployeeName());
//                    wcu.setMobile(entity.getMobile());
//                    // 需要加入工号+预留手机检查
//                    SystemUser su = systemUserBean.findByUserId(entity.getEmployeeId());
//                    if (null == su) {
//                        return new ResponseSession("401", "授权工号不存在");
//                    } else {
//                        if (!su.getPhone().equals(entity.getMobile())) {
//                            return new ResponseSession("401", "手机号与企业预留不一致");
//                        }
//                    }
//                    wcu.setAuthorized(Boolean.TRUE);
//                    wcu.setStatus("V");
//                    wechatUserBean.update(wcu);
//                    ResponseSession session = new ResponseSession("200", "更新成功");
//                    session.setAuthorized(wcu.getAuthorized());
//                    session.setEmployeeId(wcu.getEmployeeId());
//                    session.setEmployeeName(wcu.getEmployeeName());
//                    //session 加入部门和事业单位
//                    session.setDeptno(su.getDeptno());
//                    session.setDeptName(su.getDept().getDept());
//                    session.setCompany(su.getDept().getCompany());
//                    session.setCompanyName(companyBean.findByCompany(session.getCompany()).getName());
//                    // 更新WeChatSession状态
//                    wcs.setStatus("V");
//                    wcs.setCfmdateToNow();
//                    wechatSessionBean.update(wcs);
//
//                    return session;
//                } else {
//                    return new ResponseSession("401", "授权异常");
//                }
//            } else {
//                return new ResponseSession("404", "验证码错误或失效");
//            }
//        } catch (Exception ex) {
//            log4j.error(ex);
//            return new ResponseSession("500", "系统异常");
//        }
//    }
    @GET
    @Path("AuthValidation")
    @Produces({MediaType.APPLICATION_JSON})
    public Set<Wechatauthority> findAuthoityById(@QueryParam("employeeid") String employeeid) {
        List<WechatroleWechatuser> wechatroleWechatuser = wechatroleWechatuserBean.findAllByUserid(employeeid);
        Set<Wechatauthority> we = new HashSet<Wechatauthority>();
        if (wechatroleWechatuser != null) {
            for (int i = 0; i < wechatroleWechatuser.size(); i++) {
                Wechatrole w = wechatroleWechatuser.get(i).getWechatrole();
                if ("Y".equals(w.getStatus())) {
                    List<Wechatauthority> wa = wechatauthorityBean.findAllAuth(w.getId());
                    we.addAll(wa);
                }
            }
            return we;
        }
        return null;
    }
}
