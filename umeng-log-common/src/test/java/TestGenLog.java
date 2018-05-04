import com.it18zhang.umeng.common.AppLogAggEntity;
import com.it18zhang.umeng.common.AppStartupLog;
import com.it18zhang.umeng.util.GenLogUtil;
import org.junit.Test;

import static com.it18zhang.umeng.util.DictionaryUtil.randomValue;

public class TestGenLog {

    @Test
    public void testGen(){
        System.out.println(randomValue("appid"));
    }

    @Test
    public void testGenLog(){

        AppLogAggEntity appLogAggEntity = GenLogUtil.genLogAgg();

        System.out.println("");

    }
}
