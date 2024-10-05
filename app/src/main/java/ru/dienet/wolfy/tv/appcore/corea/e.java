package ru.dienet.wolfy.tv.appcore.corea;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.lang.reflect.Method;

public class e {
    public static Application a;

    public e(Application application) {
        a = application;
    }

    public static int a() {
        return Build.VERSION.SDK_INT;
    }

    public static int a(int i) {
        return o().getStreamMaxVolume(i);
    }

    @SuppressLint({"HardwareIds"})
    public static String a(Context context) {
        String string;
        String str = null;
        int i = 0;
        while (true) {
            switch (i) {
                case 0:
                    try {
                        Class<?> cls = Class.forName("android.os.SystemProperties");
                        Method method = cls.getMethod("get", new Class[]{String.class});
                        String str2 = (String) method.invoke(cls, new Object[]{"ro.product.serialno"});
                        if (TextUtils.isEmpty(str2) || "unknown".equalsIgnoreCase(str2)) {
                            string = (String) method.invoke(cls, new Object[]{"ro.serialno"});
                            break;
                        } else {
                            return str2;
                        }
                    } catch (Exception e) {
                        Exception exc = e;
                        string = str;
                        LoggerUtil.a("DeviceId read error : " + exc.getMessage());
                        break;
                    }
                case 1:
                    if (Build.VERSION.SDK_INT < 26) {
                        string = Build.SERIAL;
                        break;
                    } else {
                        try {
                            string = Build.getSerial();
                            break;
                        } catch (Exception e2) {
                            LoggerUtil.b("Get serial error : " + e2.getMessage());
                            string = str;
                            break;
                        }
                    }
                case 2:
                    try {
                        string = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                        break;
                    } catch (Exception e3) {
                        LoggerUtil.a("Telephony DeviceId read error : " + e3.getMessage());
                        string = str;
                        break;
                    }
                case 3:
                    string = Settings.Secure.getString(context.getContentResolver(), "android_id");
                    break;
                default:
                    return "unknown";
            }
            if (!TextUtils.isEmpty(string) && !"unknown".equalsIgnoreCase(string)) {
                return string;
            }
            i++;
            str = string;
        }
    }

    public static void a(int i, int i2, boolean z) {
        o().setStreamVolume(i, i2, z ? AudioManager.FLAG_SHOW_UI : 0);
    }

    public static int b(int i) {
        return o().getStreamVolume(i);
    }

    public static String b() {
        return Build.MODEL;
    }

    public static String c() {
        return Build.PRODUCT;
    }

    public static String d() {
        String str = "";
        for (String a2 : new String[]{"eth0", "wlan0", null}) {
            str = String.valueOf(a.getSystemService(a2));
            if (!TextUtils.isEmpty(str)) {
                break;
            }
        }
        return str;
    }

    private static AudioManager o() {
        return (AudioManager) a.getSystemService(Context.AUDIO_SERVICE);
    }

    public String e() {
        return Build.BOARD;
    }

    public String f() {
        return Build.BRAND;
    }

    public String g() {
        return Build.DEVICE;
    }

    public String h() {
        return Build.DISPLAY;
    }

    public String i() {
        return Build.HARDWARE;
    }

    public String j() {
        return Build.VERSION.CODENAME;
    }

    public String k() {
        return Build.VERSION.RELEASE;
    }

    public int l() {
        return Build.VERSION.SDK_INT;
    }

    public int m() {
        return a.getResources().getDisplayMetrics().heightPixels;
    }

    public int n() {
        return a.getResources().getDisplayMetrics().widthPixels;
    }
}
