package com.it18zhang.umeng.common;

import java.util.Map;

/**
 * 应用上报的事件相关信息
 */
public class AppEventLog extends AppBaseLog {

    private static final long serialVersionUID = 1L;

    private String eventId;                                //事件唯一标识
    private String productId;        //购买商品

    public AppEventLog() {
        setLogType(LOGTYPE_EVENT);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
