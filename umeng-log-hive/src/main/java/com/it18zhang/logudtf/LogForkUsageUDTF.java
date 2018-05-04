package com.it18zhang.logudtf;

import com.it18zhang.umeng.common.AppLogAggEntity;
import com.it18zhang.umeng.common.AppUsageLog;

import java.util.List;

/**
 * 子类化日志分叉类。
 */
public class LogForkUsageUDTF extends LogForkBaseUDTF<AppUsageLog>{

	public List<AppUsageLog> getAppLogList(AppLogAggEntity aggLog) {
		return aggLog.getAppUsageLogs();
	}
}
