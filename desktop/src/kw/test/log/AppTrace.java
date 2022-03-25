package kw.test.log;

public class AppTrace {
    public static void backtrace() {
        StackTraceElement[] lst = Thread.currentThread().getStackTrace();
        for (int idx = 2, n = lst.length; idx < n; ++idx) {
            NLog.d("bt: %s", lst[idx]);
        }
    }
}
