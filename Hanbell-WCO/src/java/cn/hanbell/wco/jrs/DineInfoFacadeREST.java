/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import cn.hanbell.wco.ejb.DineInfobean;
import cn.hanbell.wco.entity.DineInfo;
import cn.hanbell.wco.web.SuperRESTForWCO;
import com.lightshell.comm.SuperEJB;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author C1879
 */
@Stateless
@Path("wco/dineinfo")
public class DineInfoFacadeREST extends SuperRESTForWCO<DineInfo> {

    @EJB
    private DineInfobean dineInfobean;

    public DineInfoFacadeREST() {
        super(DineInfo.class);
    }

    @Override
    protected SuperEJB getSuperEJB() {
        return dineInfobean;
    }
    
    @POST
    @Path("create")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public ResponseMessage create(DineInfo entity, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                entity.setStatus("N");
                entity.setCreator(entity.getUserid()+ "-" + entity.getUsername());
                entity.setCredateToNow();
                getSuperEJB().persist(entity);
                return new ResponseMessage("200", "更新成功");
            } catch (Exception ex) {
                log4j.error(ex);
                return new ResponseMessage("500", "系统错误更新失败");
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
    
    @POST
    @Path("verify")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseMessage verify(DineInfo entity, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                boolean a=dineInfobean.notExist(entity.getUserid(), entity.getDinedate());
                if(!a){
                    return new ResponseMessage("405", "验证失败");
                }
                return new ResponseMessage("200", "验证成功");
            } catch (Exception ex) {
                log4j.error(ex);
                return new ResponseMessage("500", "系统错误验证失败");
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
    

    @POST
    @Path("removeForStatus")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseMessage removeForStatus(@QueryParam("id") String id, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                dineInfobean.removeForStatus(Integer.parseInt(id));
                return new ResponseMessage("200", "更新成功");
            } catch (Exception ex) {
                log4j.error(ex);
                return new ResponseMessage("500", "系统错误更新失败");
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("findDineInfoList")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public List<DineInfo> findDineInfoList(@QueryParam("userid") String userid, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            try {
                return dineInfobean.getListByUseridAndDinedate(userid);
            } catch (Exception ex) {
                log4j.error(ex);
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

}
