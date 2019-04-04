package com.dx.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dx.dao.ChannelDao;
import com.dx.dao.ChannelMerchantDao;
import com.dx.dao.RoutemappingDao;
import com.dx.model.Channel;
import com.dx.model.OrderDetail;
import com.dx.model.Routemapping;
import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;


/**
 * 支持按商户路由和按代理商路由
 * 
 * 1)支持按笔均，通道商户使用次数，随机，时间段组合路由
 * 2)限额路由(order by方式) 限额200以下系统自动移除
 * 3) transAmt 在绑卡时需要在web应用中上送
 */
@Service
public class RouteMappingService  {

    private static final Log _log = LogFactory.getLog(RouteMappingService.class);
   
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private RoutemappingDao routeMappingDao;
    @Autowired
    ChannelMerchantDao channelMerchantMapper;
    
    
    @DataSource("King")
    //@DataSource("Queen")//生产走从库
    public Map<String,String> selectRoute4AT(String params) throws Exception {
    	JSONObject jsonStr = JSONObject.parseObject(params);
    	Map<String, String> bizResult = new HashMap<String, String>();
    	
    	OrderDetail orderDetail = jsonStr.getObject("orderDetail", OrderDetail.class);
    	String trxType = jsonStr.getString("trx_type");//下游送交易类型
    	String merNo = orderDetail.getMer_no();
    	String serial_no = orderDetail.getSerial_no();
        Routemapping routeMapping = routeMappingDao.selectByMerNoXType(merNo,trxType);//根据商编和交易类型查询路由
        
        if(routeMapping == null){
        	String errorMessage = "请联系商家。";
			_log.info("VVVV serial_no=[" + serial_no + "] , mer_no=["+merNo+"] ret errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_R_NOFOUND, errorMessage); 	
        }
        Channel channel= channelDao.selectByChannelId(routeMapping.getChnl_id());
        if(channel == null){
        	String errorMessage = "请联系商家。";
			_log.info("VVVV 注意通道商户为空 serial_no=[" + serial_no + "] , mer_no=["+merNo+"] ret errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_MER_INVALID, errorMessage); 	
        }
          
        jsonStr.put("channel",channel); 
        jsonStr.put("routeMapping",routeMapping); 
        jsonStr.put("sub_mer_id",routeMapping.getSub_mer_id()); 
   
        bizResult =  Retutil.createSuccessBiz(jsonStr);	
        _log.info("VVVV serial_no=["+serial_no+"], routeMapping return value =="+bizResult);
        merNo=null;
		jsonStr=null;	
		trxType=null;	
		serial_no=null;
		orderDetail=null;	
		routeMapping=null;		
        return bizResult;
    }
    
}
