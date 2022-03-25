package kw.test.log;

import com.badlogic.gdx.Application;

import org.junit.Test;

public class App {
    @Test
    public void testDefault() throws InterruptedException {
        NLog.DefaultPrinter defaultPrinter = new NLog.DefaultPrinter();
        Thread.sleep(1000);
        defaultPrinter.print(Application.LOG_DEBUG,"key","v");
    }

    @Test
    public void testNLog(){
        NLog.i("log %s","xx");
    }

    @Test
    public void testBackTrace(){
        AppTrace trace = new AppTrace();
        trace.backtrace();
    }

    @Test
    public void testLogFile(){
        LogFilePrinter printer = new LogFilePrinter("log------------------",1024);
        NLog.addPrinter(printer);
        for (int i = 0; i < 100; i++) {
            NLog.i("xxx %s","xxxxxxxx");
            NLog.e("xx %s","cccccc");
        }
    }
}
