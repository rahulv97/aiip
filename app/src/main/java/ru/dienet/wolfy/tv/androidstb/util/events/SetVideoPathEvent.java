package ru.dienet.wolfy.tv.androidstb.util.events;

public class SetVideoPathEvent {
    private String videoUri;

    public SetVideoPathEvent(String str) {
        this.videoUri = str;
    }

    public String getVideoUri() {
        return this.videoUri;
    }
}
