package ru.dienet.wolfy.tv.androidstb.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import java.io.File;
import java.util.Scanner;
import ru.dienet.wolfy.tv.androidstb.a;
import ru.dienet.wolfy.tv.androidstb.view.GreetingsDialog;
import ru.dienet.wolfy.tv.androidstb.view.SplashActivity;
import ru.dienet.wolfy.tv.appcore.corea.e;

public class d {
    public static void a(Activity activity, SharedPreferences sharedPreferences, Intent intent) {
        if (System.currentTimeMillis() - sharedPreferences.getLong("dateSettingsOpeningError", 0) > 12000) {
            Intent intent2 = new Intent(activity, SplashActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            try {
                activity.startActivity(intent2);
            } catch (ActivityNotFoundException e) {
            }
        }
        activity.startActivity(intent);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong("dateSettingsOpeningError", System.currentTimeMillis());
        edit.apply();
    }

    public static boolean a() {
        File file = new File("/sys/devices/virtual/switch/hdmi/state");
        if (!file.exists()) {
            file = new File("/sys/class/switch/hdmi/state");
        }
        try {
            Scanner scanner = new Scanner(file);
            int nextInt = scanner.nextInt();
            scanner.close();
            return nextInt > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean a(Activity activity) {
        String[] b = a.b();
        if (b == null) {
            return true;
        }
        if (b.length > 0) {
            for (String str : b) {
                if (e.b().contains(str) || str.contains(e.b())) {
                    return true;
                }
            }
        }
        activity.finish();
        Intent intent = new Intent(activity, GreetingsDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
        return false;
    }
}
