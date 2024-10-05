package ru.dienet.wolfy.tv.androidstb.util.events;

public class SetWebViewAlphaLevelEvent {
    private float alpha;

    public SetWebViewAlphaLevelEvent(float f) {
        this.alpha = f;
    }

    public float getAlphaLevel() {
        return this.alpha;
    }
}
