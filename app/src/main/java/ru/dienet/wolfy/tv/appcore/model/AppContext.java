package ru.dienet.wolfy.tv.appcore.model;

import android.app.Application;
import ru.dienet.wolfy.tv.appcore.corea.c;

public abstract class AppContext extends Application {
    public abstract void a();

    public void onCreate() {
        a();
        Thread.setDefaultUncaughtExceptionHandler(new c());
        super.onCreate();
    }
}
