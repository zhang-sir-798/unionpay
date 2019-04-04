package com.dx.util.dbconfig;

import org.apache.commons.lang3.StringUtils;

public class DBHelper {

	private static ThreadLocal<String> dbContext = new ThreadLocal<String>();
	
	// 写数据源标识
	public final static String DB_WRITE = "dataSourceWrite";
	// 读数据源标识
	public final static String DB_READ = "dataSourceRead";

	/**
	 * 获取数据源类型，即是写数据源，还是读数据源
	 * 
	 * @return
	 */
	public static String getDbType() {
		String db_type = dbContext.get();
		if (StringUtils.isEmpty(db_type)) {
			// 默认是写数据源
			db_type = DB_WRITE;
		}
		return db_type;
	}
	
	/**
	 * 设置该线程的数据源类型
	 * 
	 * @param str
	 */
	public static void setDbType(String str) {
		dbContext.set(str);
	}
}