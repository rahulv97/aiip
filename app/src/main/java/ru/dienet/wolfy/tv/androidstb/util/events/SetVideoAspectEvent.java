package ru.dienet.wolfy.tv.androidstb.util.events;

public class SetVideoAspectEvent {
    private int videoViewAspectId;

    public SetVideoAspectEvent(int i) {
        this.videoViewAspectId = i;
    }

    public int getVideoViewAspectId() {
        return this.videoViewAspectId;
    }
}
