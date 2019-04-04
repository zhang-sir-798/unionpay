package com.dx.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dx.dao.OrderDetailDao;
import com.dx.model.OrderDetail;
import com.dx.util.dbconfig.DataSource;


@Service
@Transactional
public class testService {
	
	private static final Log log = LogFactory.getLog(testService.class);
	@Autowired
	private OrderDetailDao orderDetailDao;
	
	//@DataSource("King")
	@DataSource("Queen")
	@Transactional(rollbackFor=Exception.class)
	public void doFixedCodePay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("请求进入 ");
		
			//DBHelper.setDbType(DBHelper.DB_WRITE);
			//DBHelper.setDbType(DBHelper.DB_READ);
			/*OrderDetail orderDetail = orderDetailDao.queryDetailByOrderNo("aaaatest123456789");
			if (orderDetail == null) {
				log.info("订单为空！");
				
			}
			log.info("订单："+orderDetail);*/
			
			OrderDetail orderDetail =new OrderDetail();
			orderDetail.setNotify_url("test001");
			orderDetail.setId(Long.parseLong("25050"));
			
			
			int x =orderDetailDao.updateByPrimaryKey(orderDetail);
			//int a =1/0;
			
			log.info("更新结果："+x);
		
		
		
	}
}
