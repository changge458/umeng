package com.it18zhang.logudtf;

import com.it18zhang.umeng.common.AppErrorLog;
import com.it18zhang.umeng.common.AppLogAggEntity;

import java.util.List;

/**
 * 子类化日志分叉类。
 */
public class LogForkErrorUDTF extends LogForkBaseUDTF<AppErrorLog>{


	public List<AppErrorLog> getAppLogList(AppLogAggEntity aggLog) {
		return aggLog.getAppErrorLogs();
	}
}
