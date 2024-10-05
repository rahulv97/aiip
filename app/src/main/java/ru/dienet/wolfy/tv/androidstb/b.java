package ru.dienet.wolfy.tv.androidstb;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import ru.dienet.wolfy.tv.appcore.video.ImpulsSurfaceView;
import ru.dienet.wolfy.tv.appcore.video.a;

public class b {
    private Activity a;
    private FrameLayout b;
    private a c;
    private ru.dienet.wolfy.tv.androidstb.stbb.a<SurfaceView, FrameLayout> d;
    private int e;

    public b(Activity activity, FrameLayout frameLayout, int i) {
        this.e = i;
        new SparseArray().append(0, "Default");
        this.a = activity;
        this.b = frameLayout;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
        a(i);
    }

    private void a(int i) {
        if (this.b != null) {
            this.b.removeAllViews();
        }
        switch (i) {
            case 0:
                d();
                return;
            default:
                d();
                return;
        }
    }

    private void d() {
        this.d = null;
        ImpulsSurfaceView impulsSurfaceView = new ImpulsSurfaceView((Context) this.a, this.b);
        impulsSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.b.addView(impulsSurfaceView);
        impulsSurfaceView.setFocusable(false);
        impulsSurfaceView.setFocusableInTouchMode(false);
        this.c = impulsSurfaceView;
    }

    public void a() {
        if (this.e == 0) {
            boolean z = this.c != null ? this.b.indexOfChild((SurfaceView) this.c) == this.b.getChildCount() + -1 : false;
            a(this.e);
            if (z) {
                ((SurfaceView) this.c).bringToFront();
            }
        }
    }

    public ru.dienet.wolfy.tv.androidstb.stbb.a<SurfaceView, FrameLayout> b() {
        return this.d;
    }

    public a c() {
        return this.c;
    }
}
