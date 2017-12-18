package com.it18zhang.log;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestLog {

    @Test
    public void test1(){
        Logger logger = Logger.getLogger(TestLog.class);
        logger.info("info");
        logger.warn("warn|");
        logger.error("error");
        logger.debug("debug");
    }
}
