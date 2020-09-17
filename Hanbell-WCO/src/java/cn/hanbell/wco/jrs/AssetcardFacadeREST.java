/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.wco.jrs;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import cn.hanbell.eam.entity.AssetCard;
import cn.hanbell.eam.ejb.AssetCardBean;
import cn.hanbell.eam.ejb.EquipmentRepairBean;
import cn.hanbell.eam.entity.AssetItem;
import cn.hanbell.eam.entity.EquipmentRepair;
import cn.hanbell.wco.web.SuperRESTForWCO;
import com.lightshell.comm.SuperEJB;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

/**
 *
 * @author C2090
 */
@Stateless
@Path("shbeam/assetcardtest")
public class AssetcardFacadeREST extends SuperRESTForWCO<AssetCard> {
    @EJB
    private AssetCardBean assetCardBean;
    @EJB
    private EquipmentRepairBean equipmentrepairBean;
    
    protected SuperEJB superEJB;
    
    @Override
    protected SuperEJB getSuperEJB() {
        return assetCardBean;
    }
    

    public AssetcardFacadeREST() {
        super(AssetCard.class);
    }
    
    @GET
    @Path("getAssetCardModel/{filters}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public AssetCard findByAssetno(@PathParam("filters") PathSegment filters, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            List<AssetCard> assetCardListRes = new ArrayList<>();
            AssetCard assetCardRes = new AssetCard();
            try {
                MultivaluedMap<String, String> filtersMM = filters.getMatrixParameters();
                Map<String, Object> filterFields = new HashMap<>();
                Map<String, String> sortFields = new LinkedHashMap<>();
                String key, value;
                if (filtersMM != null) {
                    for (Map.Entry<String, List<String>> entrySet : filtersMM.entrySet()) {
                        key = entrySet.getKey();
                        value = entrySet.getValue().get(0);
                        filterFields.put(key, value);
                    }
                }
                assetCardRes = assetCardBean.findByAssetno(filterFields.get("formid").toString());
            } catch (Exception ex) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
//            return assetCardListRes;
                return assetCardRes;
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
    
    
    @GET
    @Path("getAssetCardList/{filters}/{sorts}/{offset}/{pageSize}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public List<AssetCard> findByNewFilters(@PathParam("filters") PathSegment filters, @PathParam("sorts") PathSegment sorts, @PathParam("offset") Integer offset, @PathParam("pageSize") Integer pageSize, @QueryParam("appid") String appid, @QueryParam("token") String token) {
        if (isAuthorized(appid, token)) {
            this.superEJB = assetCardBean;
            List<AssetCard> assetCardListRes = new ArrayList<>();
            AssetCard assetCardRes = new AssetCard();
            try {
                MultivaluedMap<String, String> filtersMM = filters.getMatrixParameters();
                MultivaluedMap<String, String> sortsMM = sorts.getMatrixParameters();
                Map<String, Object> filterFields = new HashMap<>();
                Map<String, String> sortFields = new LinkedHashMap<>();
                String key, value="";
                if (filtersMM != null) {
                    for (Map.Entry<String, List<String>> entrySet : filtersMM.entrySet()) {
                        key = entrySet.getKey();
                        value = entrySet.getValue().get(0);
                    }
                }
                if (sortsMM != null) {
                    for (Map.Entry<String, List<String>> entrySet : sortsMM.entrySet()) {
                        key = entrySet.getKey();
                        value = entrySet.getValue().get(0);
                        sortFields.put(key, value);
                    }
                }
                assetCardListRes = assetCardBean.getAssetCardList("C", value);
            } catch (Exception ex) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
                return assetCardListRes;
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
    
    @POST
    @Path("createEqpRepairHad")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ResponseMessage createEqpRepairHad(EquipmentRepair entity, @QueryParam("appid") String appid, @QueryParam("token") String token, @QueryParam("itemno") String itemno) {
        if (isAuthorized(appid, token)) {
            if (entity == null) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            try {
                EquipmentRepair equipInvenTemp = new EquipmentRepair();
                AssetCard assetCardTemp = new AssetCard();
                AssetItem assetItemTemp = new AssetItem();
                assetItemTemp.setItemno(itemno);
                assetCardTemp.setAssetItem(assetItemTemp);
                
                
                String formid = equipmentrepairBean.getFormId(new Date(), "AP", "YYYYMMdd", 4);
                
                equipInvenTemp.setCompany(entity.getCompany());
                equipInvenTemp.setAssetno(entity.getAssetno());
                equipInvenTemp.setRepairuser(entity.getRepairuser());
                equipInvenTemp.setFormid(formid);
                equipInvenTemp.setCredate(new Date()); 
                equipmentrepairBean.persist(equipInvenTemp);
                return new ResponseMessage("200", "Code=200");
            } catch (Exception ex) {
                return new ResponseMessage("500", "系统错误Insert失败");
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

}


class AssetcardResponseResult {

    private Integer size;
    private List<AssetCard> result;

    public AssetcardResponseResult() {

    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * @return the result
     */
    public List<AssetCard> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<AssetCard> result) {
        this.result = result;
    }

}