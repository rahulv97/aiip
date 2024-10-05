package ru.dienet.wolfy.tv.androidstb.util.events;

public class SetBoundedVideoViewSizeRequestEvent {
    private int height;
    private final int leftCornerPosition;
    private final int topCornerPosition;
    private int width;

    public SetBoundedVideoViewSizeRequestEvent(int i, int i2, int i3, int i4) {
        this.leftCornerPosition = i;
        this.topCornerPosition = i2;
        this.width = i3;
        this.height = i4;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLeftCornerPosition() {
        return this.leftCornerPosition;
    }

    public int getTopCornerPosition() {
        return this.topCornerPosition;
    }

    public int getWidth() {
        return this.width;
    }
}
