package com.parishi.utility;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoggerFactory {
    private LoggerFactory(){}
    static
    {
        System.setProperty("logFileName", "Main");
    }
    public static Logger getLogger(){
        return LogManager.getLogger("LoggerFactory");
    }
}
