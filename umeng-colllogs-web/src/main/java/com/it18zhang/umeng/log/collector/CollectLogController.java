package com.it18zhang.umeng.log.collector;


import com.it18zhang.umeng.common.AppLogAggEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

@Controller
@RequestMapping("/collector")
public class CollectLogController {

    /**
     * 收集日志
     * @param aggLog
     * @return
     */
    @RequestMapping(value = "/agg" , method = RequestMethod.POST)
    @ResponseBody
    public AppLogAggEntity collect(@RequestBody AppLogAggEntity aggLog){

        mergeBaseInfoToLogs(aggLog);

        System.out.println(aggLog.getAppChannel());
        return aggLog;
    }

    private void mergeBaseInfoToLogs(AppLogAggEntity aggLog) {

    }

    /**
     * 复制源对象属性到dest对象，类型一致即可
     * @param src
     * @param dest
     */
    private void copyPropos(Object src , Object dest){
        try {
            Class srcClass = src.getClass();
            Class destClass = dest.getClass();
            BeanInfo srcInfo = Introspector.getBeanInfo(srcClass);
            BeanInfo destInfo = Introspector.getBeanInfo(destClass);
            PropertyDescriptor[] pd =  srcInfo.getPropertyDescriptors();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
