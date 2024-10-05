package ru.dienet.wolfy.tv.androidstb.util.events;

import android.widget.FrameLayout;

public class SetSurfaceViewParamsEvent {
    private final FrameLayout.LayoutParams containerLayoutParams;
    private final FrameLayout.LayoutParams surfaceLayoutParams;

    public SetSurfaceViewParamsEvent(FrameLayout.LayoutParams layoutParams, FrameLayout.LayoutParams layoutParams2) {
        this.surfaceLayoutParams = layoutParams;
        this.containerLayoutParams = layoutParams2;
    }

    public FrameLayout.LayoutParams getContainerLayoutParams() {
        return this.containerLayoutParams;
    }

    public FrameLayout.LayoutParams getSurfaceLayoutParams() {
        return this.surfaceLayoutParams;
    }
}
