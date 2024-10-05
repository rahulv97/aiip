package ru.dienet.wolfy.tv.androidstb.view;

import static android.app.Notification.FLAG_HIGH_PRIORITY;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ru.dienet.wolfy.tv.R;

public class LogoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Paint a = new Paint();
    private Bitmap b;
    private boolean c = true;

    public LogoSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
        getHolder().addCallback(this);
    }

    public LogoSurfaceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setWillNotDraw(false);
        getHolder().addCallback(this);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.a.setColor(-16776961);
        this.a.setStyle(Paint.Style.FILL);
        super.onDraw(canvas);
        if (this.c) {
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        float height = ((float) decodeResource.getHeight()) / ((float) getHeight());
        Math.round(((float) decodeResource.getWidth()) / height);
        Math.round(((float) decodeResource.getHeight()) / height);
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        createBitmap.eraseColor(Color.argb(FLAG_HIGH_PRIORITY, 0, 255, 0));
        this.b = createBitmap;
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
