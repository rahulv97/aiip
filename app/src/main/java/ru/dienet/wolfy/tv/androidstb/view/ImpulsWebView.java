package ru.dienet.wolfy.tv.androidstb.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ImpulsWebView extends WebView {
    public ImpulsWebView(Context context) {
        super(context);
    }

    public ImpulsWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ImpulsWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public ImpulsWebView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void computeScroll() {
    }

    public boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        return false;
    }

    public void scrollTo(int i, int i2) {
    }
}
