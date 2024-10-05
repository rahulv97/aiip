package ru.dienet.wolfy.tv.appcore.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class ImpulsBackground extends View {
    private int viewWidth;
    private int viewHeight;
    private Random random;

    public ImpulsBackground(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        random = new Random();
    }

    public ImpulsBackground(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        random = new Random();
    }

    private void drawRandomCircles(Paint paint, Canvas canvas) {
        for (int i = 0; i < 10; i++) {
            float x = random.nextInt(viewWidth);
            float y = random.nextInt(viewHeight);
            float radius = 20 + random.nextFloat() * 100;  // Ensuring a positive radius between 20 and 120
            canvas.drawCircle(x, y, radius, paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#0674CB"));
        paint.setAlpha(200);  // Set alpha to a reasonable value (0-255, 255 is fully opaque)
        drawRandomCircles(paint, canvas);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.viewWidth = width;
        this.viewHeight = height;
    }
}
