package ru.dienet.wolfy.tv.appcore.corea;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import java.lang.ref.WeakReference;

public class f {
    private final Handler a = new Handler();
    private WeakReference<Activity> b;
    /* access modifiers changed from: private */
    public View c;
    private final Runnable d = new Runnable() {
        @SuppressLint({"InlinedApi"})
        public void run() {
            f.this.c.setSystemUiVisibility(4871);
        }
    };
    private final Runnable e = new Runnable() {
        @SuppressLint({"InlinedApi"})
        public void run() {
            f.this.c.setSystemUiVisibility(0);
        }
    };
    private final Runnable f = new Runnable() {
        public void run() {
            f.this.d();
        }
    };
    private final Runnable g = new Runnable() {
        public void run() {
            f.this.c();
        }
    };

    protected f() {
    }

    /* access modifiers changed from: private */
    public void c() {
        a();
        this.a.postDelayed(this.d, 300);
    }

    /* access modifiers changed from: private */
    public void d() {
        b();
        this.a.postDelayed(this.e, 300);
    }

    /* access modifiers changed from: protected */
    public void a() {
        ActionBar actionBar;
        Activity activity = (Activity) this.b.get();
        if (activity != null && (actionBar = activity.getActionBar()) != null) {
            actionBar.hide();
        }
    }

    public void a(View view, int i) {
        this.c = view;
        this.a.removeCallbacks(this.g);
        this.a.postDelayed(this.g, (long) i);
    }

    /* access modifiers changed from: protected */
    public void b() {
        ActionBar actionBar;
        Activity activity = (Activity) this.b.get();
        if (activity != null && (actionBar = activity.getActionBar()) != null) {
            actionBar.show();
        }
    }
}
