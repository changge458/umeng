package com.it18zhang.logudtf;

import com.it18zhang.umeng.common.AppEventLog;
import com.it18zhang.umeng.common.AppLogAggEntity;

import java.util.List;

/**
 * 子类化日志分叉类。
 */
public class LogForkEventUDTF extends LogForkBaseUDTF<AppEventLog>{

	public List<AppEventLog> getAppLogList(AppLogAggEntity aggLog) {
		return aggLog.getAppEventLogs() ;
	}
}
