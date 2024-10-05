package ru.dienet.wolfy.tv.androidstb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ru.dienet.wolfy.tv.androidstb.util.a;
import ru.dienet.wolfy.tv.androidstb.util.events.HdmiConnectedEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.HdmiDisconnectedEvent;

public class HdmiListener extends BroadcastReceiver {
    private static String a = "android.intent.action.HDMI_PLUGGED";

    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(a)) {
            return;
        }
        if (intent.getBooleanExtra("state", false)) {
            Log.e("HDMIListner", "[BroadcastReceiver] BroadcastReceiver.onReceive() : Connected HDMI-TV");
            a.a(new HdmiConnectedEvent());
            return;
        }
        Log.e("HDMIListner", "[BroadcastReceiver] HDMI >>: Disconnected HDMI-TV");
        a.a(new HdmiDisconnectedEvent());
    }
}
