import com.it18zhang.umeng.util.DateUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestDateUtil {

    public static void main(String[] args) throws Exception{
        long ts = DateUtil.getDayBegin(new Date(), 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(sdf.format(ts));

    }
    @Test
    public void testDate() throws Exception{
        String date = "04/May/2018:16:57:36 +0800";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss z", Locale.US);
        Date date1 = sdf.parse(date);
        System.out.println(date1.getTime());
    }
}
