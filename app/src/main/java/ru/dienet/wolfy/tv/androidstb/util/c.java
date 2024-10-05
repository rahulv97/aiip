package ru.dienet.wolfy.tv.androidstb.util;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import ru.dienet.wolfy.tv.appcore.corea.f;

public class c extends f {
    private WeakReference<AppCompatActivity> a;

    public c(AppCompatActivity appCompatActivity) {
        this.a = new WeakReference<>(appCompatActivity);
    }

    /* access modifiers changed from: protected */
    public void a() {
        ActionBar supportActionBar;
        AppCompatActivity appCompatActivity = (AppCompatActivity) this.a.get();
        if (appCompatActivity != null && (supportActionBar = appCompatActivity.getSupportActionBar()) != null) {
            supportActionBar.hide();
        }
    }

    /* access modifiers changed from: protected */
    public void b() {
        ActionBar supportActionBar;
        AppCompatActivity appCompatActivity = (AppCompatActivity) this.a.get();
        if (appCompatActivity != null && (supportActionBar = appCompatActivity.getSupportActionBar()) != null) {
            supportActionBar.show();
        }
    }
}
