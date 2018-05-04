package com.it18zhang.logudtf;

import com.it18zhang.umeng.common.AppLogAggEntity;
import com.it18zhang.umeng.common.AppStartupLog;

import java.util.List;

/**
 * 子类化日志分叉类。
 */
public class LogForkStartupUDTF extends LogForkBaseUDTF<AppStartupLog>{

	public List<AppStartupLog> getAppLogList(AppLogAggEntity aggLog) {
		return aggLog.getAppStartupLogs();
	}

	public void processIpForList(List<AppStartupLog> logs, String country, String province) {
		for(AppStartupLog log : logs){
			log.setCountry(country);
			log.setProvince(province);
		}
	}
}
