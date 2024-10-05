package ru.dienet.wolfy.tv.androidstb.util.events;

public class SetVideoAspectWithBoundsEvent {
    private final int surfaceHeight;
    private final int surfaceWidth;
    private int videoViewAspectId;

    public SetVideoAspectWithBoundsEvent(int i, int i2, int i3) {
        this.surfaceWidth = i2;
        this.surfaceHeight = i3;
        this.videoViewAspectId = i;
    }

    public int getSurfaceHeight() {
        return this.surfaceHeight;
    }

    public int getSurfaceWidth() {
        return this.surfaceWidth;
    }

    public int getVideoViewAspectId() {
        return this.videoViewAspectId;
    }
}
