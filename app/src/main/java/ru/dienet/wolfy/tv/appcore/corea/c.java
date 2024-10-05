package ru.dienet.wolfy.tv.appcore.corea;

import java.lang.Thread;

public class c implements Thread.UncaughtExceptionHandler {
    public c() {
        Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread thread, Throwable th) {
        LoggerUtil.a(th);
    }
}
