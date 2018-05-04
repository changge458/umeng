import com.it18zhang.umeng.util.ParseLogUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestParseLog {

    public static void main(String[] args) {
        String s = "{\"appChannel\":\"anroid bus\",\"appErrorLogs\":[{\"createdAtMs\":1525414978564,\"errorBrief\":\"at cn.lift.appIn.control.CommandUtil.getInfo(CommandUtil.java:67)\",\"errorDetail\":\"at cn.lift.dfdfdf.control.CommandUtil.getInfo(CommandUtil.java:67) at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) at java.lang.reflect.Method.invoke(Method.java:606)\"}],\"appEventLogs\":[{\"createdAtMs\":1525414978571,\"eventId\":\"popmenu\",\"logType\":\"event\"}],\"appId\":\"gaodemap\",\"appPageLogs\":[{\"createdAtMs\":1525414978603,\"logType\":\"page\",\"nextPage\":\"list.html\",\"pageId\":\"main.html\",\"pageViewCntInSession\":0,\"stayDurationSecs\":0,\"visitIndex\":0}],\"appPlatform\":\"android\",\"appStartupLogs\":[{\"brand\":\"小米\",\"carrier\":\"中国联通\",\"country\":\"america\",\"createdAtMs\":1525414978584,\"logType\":\"startup\",\"network\":\"3g\",\"province\":\"guangdong\",\"screenSize\":\"1136 * 640\"}],\"appUsageLogs\":[{\"createdAtMs\":1525414978593,\"logType\":\"usage\"}],\"appVersion\":\"1.1.0\",\"deviceId\":\"d0583\",\"deviceStyle\":\"iphone 7 plus\",\"osType\":\"7.1.1\",\"tenantId\":\"tnt023\"}";
        String s2 = "{\\\"appChannel\\\":\\\"anroid bus\\\",\\\"appErrorLogs\\\":[{\\\"createdAtMs\\\":1525424269026,\\\"errorBrief\\\":\\\"at cn.lift.dfdf.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)\\\",\\\"errorDetail\\\":\\\"at cn.lift.dfdfdf.control.CommandUtil.getInfo(CommandUtil.java:67) at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) at java.lang.reflect.Method.invoke(Method.java:606)\\\"}],\\\"appEventLogs\\\":[{\\\"createdAtMs\\\":1525424269027,\\\"eventId\\\":\\\"bookstore\\\",\\\"logType\\\":\\\"event\\\"}],\\\"appId\\\":\\\"faobengplay\\\",\\\"appPageLogs\\\":[{\\\"createdAtMs\\\":1525424269031,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"list.html\\\",\\\"pageId\\\":\\\"main.html\\\",\\\"pageViewCntInSession\\\":0,\\\"stayDurationSecs\\\":0,\\\"visitIndex\\\":0}],\\\"appPlatform\\\":\\\"blackberry\\\",\\\"appStartupLogs\\\":[{\\\"brand\\\":\\\"华为\\\",\\\"carrier\\\":\\\"中国铁通\\\",\\\"country\\\":\\\"america\\\",\\\"createdAtMs\\\":1525424269028,\\\"logType\\\":\\\"startup\\\",\\\"network\\\":\\\"3g\\\",\\\"province\\\":\\\"jiazhou\\\",\\\"screenSize\\\":\\\"1136 * 640\\\"}],\\\"appUsageLogs\\\":[{\\\"createdAtMs\\\":1525424269029,\\\"logType\\\":\\\"usage\\\"}],\\\"appVersion\\\":\\\"1.0.0\\\",\\\"deviceId\\\":\\\"d0944\\\",\\\"deviceStyle\\\":\\\"vivo 3\\\",\\\"osType\\\":\\\"11.0\\\",\\\"tenantId\\\":\\\"tnt009\\\"}";
        if(s2.contains("\\")){
            String s3 = s2.replaceAll("\\\\","");
            System.out.println(ParseLogUtil.parseStartup(s2));
            System.out.println(ParseLogUtil.parseChannel(s2));
        }
    }

    @Test
    public void testArrayToStr(){
        List<String> list = new ArrayList<String>();
        list.add("tom");
        list.add("tomas");
        list.add("tomaon");
        System.out.println(list.toString());
    }

}
