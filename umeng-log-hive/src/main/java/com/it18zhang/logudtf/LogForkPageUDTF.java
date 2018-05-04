package com.it18zhang.logudtf;

import com.it18zhang.umeng.common.AppLogAggEntity;
import com.it18zhang.umeng.common.AppPageLog;

import java.util.List;

/**
 * 子类化日志分叉类。
 */
public class LogForkPageUDTF extends LogForkBaseUDTF<AppPageLog>{

	public List<AppPageLog> getAppLogList(AppLogAggEntity aggLog) {
		return aggLog.getAppPageLogs();
	}
}
