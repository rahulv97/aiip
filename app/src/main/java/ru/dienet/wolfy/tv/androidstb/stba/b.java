package ru.dienet.wolfy.tv.androidstb.stba;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import java.net.URI;
import java.net.URISyntaxException;

import ru.dienet.wolfy.tv.R;
import ru.dienet.wolfy.tv.androidstb.c;
import ru.dienet.wolfy.tv.androidstb.util.events.BringToFrontRequestEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.DispatchAppKeyEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.ExternalApplicationNotFoundEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.RequestPortalRebootEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetBoundedVideoViewSizeRequestEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetVideoAspectEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetVideoPathEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetVideoViewSizeToFullscreenRequestEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetWebViewAlphaLevelEvent;
import ru.dienet.wolfy.tv.appcore.corea.e;
import ru.dienet.wolfy.tv.appcore.video.a;

public class b {
    private static String a = "VideoViewAspect";
    private static FragmentActivity b;
    private static c c;
    /* access modifiers changed from: private */
    public static a d;
    private static SharedPreferences e;
    private static AlertDialog f;
    private static boolean g = false;
    private static boolean h = false;

    public b(FragmentActivity fragmentActivity, c cVar, a aVar) {
        b = fragmentActivity;
        c = cVar;
        d = aVar;
        e = PreferenceManager.getDefaultSharedPreferences(fragmentActivity);
        d();
    }

    public static void _deviceinfo(String str) {
        int m;
        int n;
        int i = 0;
        ru.dienet.wolfy.tv.androidstb.util.a.a(new SetVideoViewSizeToFullscreenRequestEvent());
        if (b != null) {
            int i2 = -1;
            try {
                i2 = b.getPackageManager().getPackageInfo(b.getPackageName(), 0).versionCode / 10000;
            } catch (Exception e2) {
            }
            e eVar = new e(b.getApplication());
            try {
                m = b.getWindow().getDecorView().getHeight();
                n = b.getWindow().getDecorView().getWidth();
            } catch (Exception e3) {
                m = eVar.m();
                n = eVar.n();
            }
            boolean z = b.getResources().getBoolean(R.bool.isLogoutEnabled);
            String a2 = e.a((Context) b);
            String a3 = ru.dienet.wolfy.tv.appcore.a.b.a(b, e);
            String d2 = e.d();
            Settings.Secure.getString(b.getContentResolver(), "android_id");
            if (a3.contains("12345")) {
                a3 = d2;
            }
            if ("plustv".contains("comnetTvcom") || "plustv".contains("connectLLC")) {
                a3 = d2;
            }
            String language = b.getResources().getConfiguration().locale.getLanguage();
            if (language != null && language.contains("en")) {
                language = "en";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("{\"Common\":{\"brand\":\"").append(eVar.f());
            sb.append("\",\"Product\":\"").append(e.c());
            sb.append("\",\"Release\":\"").append(eVar.k());
            sb.append("\",\"Codename\":\"").append(eVar.j());
            sb.append("\",\"SDK\":\"").append(eVar.l());
            StringBuilder append = sb.append("\",\"IsLogoutEnabled\":\"");
            if (z) {
                i = 1;
            }
            append.append(i);
            sb.append("\",\"AppVersionCode\":\"").append(i2);
            sb.append("\"},\"Device\":{\"DevName\":\"").append(eVar.g());
            sb.append("\",\"Board\":\"").append(eVar.e());
            sb.append("\",\"Hardware\":\"").append(eVar.i());
            sb.append("\",\"Display\":\"").append(eVar.h());
            sb.append("\",\"Serial\":\"").append(a2);
            sb.append("\",\"DeviceId\":\"").append(a3);
            sb.append("\",\"Language\":\"").append(language);
            sb.append("\"},\"Screen\":{\"Height\":\"").append(m);
            sb.append("\",\"Width\":\"").append(n);
            sb.append("\"},");
            sb.append("\"DevInfo\":{\"Brand\":\"").append(Build.BRAND);
            sb.append("\",\"Manufacturer\":\"").append(Build.MANUFACTURER);
            sb.append("\",\"Model\":\"").append(Build.MODEL);
            sb.append("\",\"MacAddress\":\"").append(d2);
            sb.append("\"}").append("}");
            String sb2 = sb.toString();
            if (c != null) {
                c.a("onAppCallback(" + str + ",'" + sb2 + "');");
            }
        }
    }

    public static void _dispatchappkeyevent() {
        ru.dienet.wolfy.tv.androidstb.util.a.a(new DispatchAppKeyEvent());
    }

    public static void _exitapp() {
        System.exit(0);
    }

    public static void _getaspectratio(String str) {
        String str2;
        switch (d.getVideoViewAspect()) {
            case 0:
                str2 = "BEST_FIT";
                break;
            case 1:
                str2 = "FIT_HORIZONTAL";
                break;
            case 2:
                str2 = "FIT_VERTICAL";
                break;
            case 3:
                str2 = "FILL";
                break;
            case 4:
                str2 = "16_9";
                break;
            case 5:
                str2 = "4_3";
                break;
            case 6:
                str2 = "CENTER";
                break;
            default:
                str2 = "FILL";
                break;
        }
        String str3 = "onAppCallback(" + str + ",'" + str2 + "');";
        if (c != null) {
            c.a(str3);
        }
    }

    public static void _getvolume(String str) {
        new e(b.getApplication());
        int a2 = e.a(3);
        String str2 = "onAppCallback(" + str + ",'" + String.valueOf(((double) (e.b(3) * 100)) / ((double) a2)) + "');";
        if (c != null) {
            c.a(str2);
        }
    }

    public static void _openappbyid(String str) {
        Intent launchIntentForPackage = b.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage == null) {
            ru.dienet.wolfy.tv.androidstb.util.a.a(new ExternalApplicationNotFoundEvent(str));
            return;
        }
        try {
            a(launchIntentForPackage);
        } catch (ActivityNotFoundException e2) {
            d.a((Throwable) e2, d.a.WARNING);
        }
    }

    public static void _openfilebrowser(String str) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setFlags(268435456);
        intent.setType("audio/*");
        Intent createChooser = Intent.createChooser(intent, "File Browser");
        createChooser.setFlags(268435456);
        try {
            a(createChooser);
        } catch (ActivityNotFoundException e2) {
            Toast.makeText(b, "No File Manager.", 0).show();
            try {
                a(intent);
            } catch (ActivityNotFoundException e3) {
                Toast.makeText(b, "Please install a File Manager.", 0).show();
            }
        } catch (Exception e4) {
            d.a((Throwable) e4, d.a.WARNING, "UnableToOpen FileManager");
        }
    }

    public static void _openwebbrowser(String str) {
        if (str == null) {
            str = "http://";
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "http://" + str;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.setData(Uri.parse(str));
        intent.setFlags(268435456);
        try {
            a(intent);
        } catch (ActivityNotFoundException e2) {
            Toast.makeText(b, "Unable to open web browser", 0).show();
            b(str);
        }
    }

    public static void _rebootportal() {
        ru.dienet.wolfy.tv.androidstb.util.a.a(new RequestPortalRebootEvent());
    }

    public static void _selectaudiotrack(String str) {
        if (d != null) {
            try {
                Integer decode = Integer.decode(str);
                if (e.a() >= 16) {
                    d.a(decode.intValue());
                } else if (c != null) {
                    c.a("alert('selectAudioTrack unsupported by current Android version');");
                }
            } catch (NumberFormatException e2) {
                if (c != null) {
                    c.a("alert('" + e2.getMessage() + "');");
                }
            } catch (IllegalStateException e3) {
            } catch (Exception e4) {
                d.a((Throwable) e4);
            }
        }
    }

    public static void _selectsubtitlestrack(String str) {
        if (d != null) {
            try {
                d.b(Integer.decode(str).intValue());
            } catch (NumberFormatException e2) {
                if (c != null) {
                    c.a("alert('" + e2.getMessage() + "');");
                }
            } catch (IllegalStateException e3) {
            } catch (Exception e4) {
                d.a((Throwable) e4);
            }
        }
    }

    public static void _setaspectratio(String str) {
        int i = 0;
        if (!g) {
            d.a("Aspect ratio changed to " + str);
            d.b(str);
            char c2 = 65535;
            switch (str.hashCode()) {
                case -1668695274:
                    if (str.equals("BEST_FIT")) {
                        c2 = 0;
                        break;
                    }
                    break;
                case -53867214:
                    if (str.equals("FIT_HORIZONTAL")) {
                        c2 = 10;
                        break;
                    }
                    break;
                case 52968:
                    if (str.equals("4_3")) {
                        c2 = 4;
                        break;
                    }
                    break;
                case 69617:
                    if (str.equals("FIT")) {
                        c2 = 6;
                        break;
                    }
                    break;
                case 78483:
                    if (str.equals("OPT")) {
                        c2 = 8;
                        break;
                    }
                    break;
                case 1514655:
                    if (str.equals("16_9")) {
                        c2 = 3;
                        break;
                    }
                    break;
                case 2074369:
                    if (str.equals("COMB")) {
                        c2 = 9;
                        break;
                    }
                    break;
                case 2157955:
                    if (str.equals("FILL")) {
                        c2 = 2;
                        break;
                    }
                    break;
                case 608000594:
                    if (str.equals("ZOOM_2X")) {
                        c2 = 7;
                        break;
                    }
                    break;
                case 1974130692:
                    if (str.equals("FIT_VERTICAL")) {
                        c2 = 1;
                        break;
                    }
                    break;
                case 1984282709:
                    if (str.equals("CENTER")) {
                        c2 = 5;
                        break;
                    }
                    break;
            }
            switch (c2) {
                case 0:
                    break;
                case 1:
                    i = 2;
                    break;
                case 2:
                    i = 3;
                    break;
                case 3:
                    i = 4;
                    break;
                case 4:
                    i = 5;
                    break;
                case 5:
                    i = 6;
                    break;
                case 6:
                    i = 8;
                    break;
                case 7:
                    i = 9;
                    break;
                case 8:
                    i = 10;
                    break;
                case 9:
                    i = 11;
                    break;
                case 10:
                    i = 12;
                    break;
                default:
                    i = 3;
                    break;
            }
            ru.dienet.wolfy.tv.androidstb.util.a.a(new SetVideoAspectEvent(i));
        }
    }

    public static void _setstandbymode(String str) {
        char c2 = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c2 = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c2 = 2;
                    break;
                }
                break;
            case 241338917:
                if (str.equals("STANDBY_MODE_OFF")) {
                    c2 = 1;
                    break;
                }
                break;
            case 977616457:
                if (str.equals("STANDBY_MODE_ON")) {
                    c2 = 3;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
            case 1:
                d.b("STANDBY_MODE_OFF");
                return;
            case 2:
            case 3:
                d.b("STANDBY_MODE_ON");
                b.runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
                return;
            default:
                return;
        }
    }

    public static void _settingsget(String str, String str2) {
        String str3 = "onAppCallback(" + str + ",'" + e.getString(str2, "") + "');";
        if (c != null) {
            c.a(str3);
        }
    }

    public static void _settingsset(String str, String str2) {
        SharedPreferences.Editor edit = e.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static void _settopwin(String str) {
        BringToFrontRequestEvent.Layer layer;
        char c2 = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c2 = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c2 = 1;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                layer = BringToFrontRequestEvent.Layer.WEB;
                break;
            case 1:
                layer = BringToFrontRequestEvent.Layer.VIDEO;
                break;
            default:
                layer = BringToFrontRequestEvent.Layer.LOGO;
                break;
        }
        ru.dienet.wolfy.tv.androidstb.util.a.a(new BringToFrontRequestEvent(layer));
    }

    public static void _setvideobounds(String str, String str2, String str3, String str4) {
        try {
            int round = Math.round(Float.valueOf(str2).floatValue());
            int round2 = Math.round(Float.valueOf(str).floatValue());
            int round3 = Math.round(Float.valueOf(str3).floatValue());
            int round4 = Math.round(Float.valueOf(str4).floatValue());
            new FrameLayout.LayoutParams(-1, -1);
            if (round >= 0 || round2 >= 0) {
                g = true;
                int videoViewAspect = d.getVideoViewAspect();
                SharedPreferences.Editor edit = e.edit();
                edit.putInt(a, videoViewAspect);
                edit.apply();
                ru.dienet.wolfy.tv.androidstb.util.a.a(new SetBoundedVideoViewSizeRequestEvent(round2, round, round3, round4));
                return;
            }
            int i = e.getInt(a, 3);
            ru.dienet.wolfy.tv.androidstb.util.a.a(new SetVideoViewSizeToFullscreenRequestEvent());
            ru.dienet.wolfy.tv.androidstb.util.a.a(new SetVideoAspectEvent(i));
            g = false;
        } catch (NumberFormatException e2) {
            if (c != null) {
                c.a("alert('NumberFormatException: " + e2.getMessage() + "');");
            }
            d.b("setVideoBounds: NumberFormatException" + e2.getMessage());
        } catch (Exception e3) {
            d.a((Throwable) e3, d.a.WARNING, "Video bounds param parse error");
        }
    }

    public static void _setvolume(String str) {
        if (str.equals("")) {
            str = String.valueOf(5);
        }
        e.a(3, (int) ((((double) e.a(3)) * Double.parseDouble(str)) / 100.0d), false);
    }

    public static void _setwinalphalevel(String str, String str2) {
        float f2 = 1.0f;
        try {
            float parseInt = (float) Integer.parseInt(str2);
            if (parseInt > 255.0f) {
                parseInt = 255.0f;
            } else if (parseInt < 0.0f) {
                parseInt = 50.0f;
            }
            f2 = parseInt / 255.0f;
        } catch (Exception e2) {
            d.a((Throwable) e2, "At alphalevel parsing");
        }
        ru.dienet.wolfy.tv.androidstb.util.a.a(new SetWebViewAlphaLevelEvent(f2));
    }

    public static void _showsystemsettings(String str) {
        d.b("settingsType: " + str);
        char c2 = 65535;
        switch (str.hashCode()) {
            case -409712851:
                if (str.equals("udpProxy")) {
                    c2 = 3;
                    break;
                }
                break;
            case 3649301:
                if (str.equals("wifi")) {
                    c2 = 0;
                    break;
                }
                break;
            case 1185113648:
                if (str.equals("appsList")) {
                    c2 = 2;
                    break;
                }
                break;
            case 1671764162:
                if (str.equals("display")) {
                    c2 = 1;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                a("android.settings.WIFI_SETTINGS");
                return;
            case 1:
                a("android.settings.DISPLAY_SETTINGS");
                return;
            case 2:
                if (b != null) {
                    new ru.dienet.wolfy.tv.androidstb.view.a().show(b.getSupportFragmentManager(), "apps_list");
                    return;
                }
                return;
            case 3:
                if (f != null) {
                    try {
                        f.dismiss();
                        f = null;
                    } catch (Exception e2) {
                    }
                }
                ru.dienet.wolfy.tv.androidstb.view.c cVar = new ru.dienet.wolfy.tv.androidstb.view.c(b);
                cVar.a((DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.d();
                    }
                });
                f = cVar.a();
                return;
            default:
                a("android.settings.SETTINGS");
                return;
        }
    }

    public static void _videoloadchannel(final String str) {
        d();
        d.b("UDP proxy enabled" + String.valueOf(h));
        if (str != null && !"".equals(str) && h) {
            str = a(str, e.getString("preference_udp_ip", ""), e.getString("preference_udp_port", ""));
            d.b("UDP proxy after: " + str);
        }
        if (d != null) {
            b.runOnUiThread(new Runnable() {
                public void run() {
                    if (b.d != null) {
                        try {
                            if (b.d.a()) {
                                b.d.d();
                                b.d.destroyDrawingCache();
                            }
                        } catch (IllegalStateException e) {
                            d.a((Throwable) e, d.a.DEBUG);
                        }
                        ru.dienet.wolfy.tv.androidstb.util.a.a(new SetVideoPathEvent(str));
                    }
                }
            });
        }
    }

    public static void _videopause() {
        if (d != null) {
            b.runOnUiThread(new Runnable() {
                public void run() {
                    if (b.d != null && b.d.a()) {
                        b.d.b();
                    }
                }
            });
        }
    }

    public static void _videoplay() {
        if (d != null) {
            b.runOnUiThread(new Runnable() {
                public void run() {
                    if (b.d != null && !b.d.a()) {
                        b.d.c();
                    }
                }
            });
        }
    }

    public static void _videoseek(String str) {
        if (d != null) {
            try {
                d.c((int) Double.parseDouble(str));
            } catch (Exception e2) {
            }
        }
    }

    public static void _videostop() {
        if (!ru.dienet.wolfy.tv.appcore.a.a.b(b.getApplicationContext())) {
            Toast.makeText(b, R.string.networkConnectionUnavailable, 1).show();
        } else if (d != null) {
            b.runOnUiThread(new Runnable() {
                public void run() {
                    if (b.d != null) {
                        try {
                            if (b.d.a()) {
                                b.d.d();
                            }
                        } catch (IllegalStateException e) {
                            d.a((Throwable) e, d.a.DEBUG);
                        }
                    }
                }
            });
        }
    }

    private static String a(String str, String str2, String str3) {
        if ("".equals(str2) || "".equals(str3)) {
            return str;
        }
        try {
            URI uri = new URI(str);
            if (!"udp".equals(uri.getScheme())) {
                return str;
            }
            return "http://" + str2 + ":" + str3 + "/udp/" + uri.getHost() + ":" + uri.getPort();
        } catch (URISyntaxException e2) {
            d.a((Throwable) e2);
            e2.printStackTrace();
            return str;
        }
    }

    private static void a(Intent intent) {
        if ("plustv".equalsIgnoreCase("atlanttelecom")) {
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setFlags(intent.getFlags() | 268435456 | 16777216);
            b.startActivity(intent);
            return;
        }
        ru.dienet.wolfy.tv.androidstb.util.d.a(b, e, intent);
    }

    private static void a(String str) {
        Intent intent = new Intent(str);
        intent.setFlags(268435456);
        try {
            a(intent);
        } catch (Exception e2) {
            d.a((Throwable) e2, d.a.FATAL);
        }
    }

    public static void a(a aVar) {
        d = aVar;
    }

    private static void b(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        intent.setFlags(268435456);
        try {
            a(intent);
        } catch (ActivityNotFoundException e2) {
            Toast.makeText(b, "Unable to open web browser", 0).show();
        }
    }

    /* access modifiers changed from: private */
    public static void d() {
        h = !"".equals(e.getString("preference_udp_ip", "")) && !"".equals(e.getString("preference_udp_port", ""));
    }

    public long a() {
        return (long) d.getCurrentPosition();
    }
}
