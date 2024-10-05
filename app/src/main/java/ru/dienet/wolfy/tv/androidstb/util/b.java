package ru.dienet.wolfy.tv.androidstb.util;

import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class b {
    int a = -1;

    public static boolean b(InputEvent inputEvent) {
        return (inputEvent.getSource() & 513) != 513;
    }

    public int a(InputEvent inputEvent) {
        if (!b(inputEvent)) {
            return -1;
        }
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            float axisValue = motionEvent.getAxisValue(15);
            float axisValue2 = motionEvent.getAxisValue(16);
            if (Float.compare(axisValue, -1.0f) == 0) {
                this.a = 1;
            } else if (Float.compare(axisValue, 1.0f) == 0) {
                this.a = 2;
            } else if (Float.compare(axisValue2, -1.0f) == 0) {
                this.a = 0;
            } else if (Float.compare(axisValue2, 1.0f) == 0) {
                this.a = 3;
            }
        } else if (inputEvent instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) inputEvent;
            if (keyEvent.getKeyCode() == 21) {
                this.a = 1;
            } else if (keyEvent.getKeyCode() == 22) {
                this.a = 2;
            } else if (keyEvent.getKeyCode() == 19) {
                this.a = 0;
            } else if (keyEvent.getKeyCode() == 20) {
                this.a = 3;
            } else if (keyEvent.getKeyCode() == 23) {
                this.a = 4;
            }
        }
        return this.a;
    }
}
