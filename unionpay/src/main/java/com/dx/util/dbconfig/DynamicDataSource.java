package com.dx.util.dbconfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/** 
* @author: zhangsir
* @date：2018年12月12日 下午10:39:18
* @description: 动态数据源实现读写分离
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	 @Override  
     protected Object determineCurrentLookupKey() {  
         return DynamicDataSourceHolder.getDataSouce();  
     }  
}
