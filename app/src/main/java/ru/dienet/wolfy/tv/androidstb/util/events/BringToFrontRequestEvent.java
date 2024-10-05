package ru.dienet.wolfy.tv.androidstb.util.events;

public class BringToFrontRequestEvent {
    private Layer viewLayer;

    public enum Layer {
        WEB,
        VIDEO,
        LOGO
    }

    public BringToFrontRequestEvent(Layer layer) {
        this.viewLayer = layer;
    }

    public Layer getViewLayer() {
        return this.viewLayer;
    }
}
