package ru.dienet.wolfy.tv.androidstb.util.events;

public class ExternalApplicationNotFoundEvent {
    private String appIdString;

    public ExternalApplicationNotFoundEvent(String str) {
        this.appIdString = str;
    }

    public String getAppIdString() {
        return this.appIdString;
    }
}
