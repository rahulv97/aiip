package ru.dienet.wolfy.tv.androidstb;

import static android.os.Build.VERSION_CODES.R;

import static ru.dienet.wolfy.tv.appcore.corea.e.a;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.InputMismatchException;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.dienet.wolfy.tv.androidstb.stba.JavascriptInterfaceHandler;
import ru.dienet.wolfy.tv.androidstb.util.c;
import ru.dienet.wolfy.tv.androidstb.util.d;
import ru.dienet.wolfy.tv.androidstb.util.events.BringToFrontRequestEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.DispatchAppKeyEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.ExternalApplicationNotFoundEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.HdmiConnectedEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.HdmiDisconnectedEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.HideUiEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.RequestPortalRebootEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetBoundedVideoViewSizeRequestEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetVideoAspectEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetVideoPathEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetVideoViewSizeToFullscreenRequestEvent;
import ru.dienet.wolfy.tv.androidstb.util.events.SetWebViewAlphaLevelEvent;
import ru.dienet.wolfy.tv.androidstb.view.ImpulsWebView;
import ru.dienet.wolfy.tv.androidstb.view.b;

public class StbPlayer extends AppCompatActivity {
    public static WeakReference<Activity> b;
    public static MediaPlayer c = null;
    private static Handler o = new Handler();
    /* access modifiers changed from: private */
    public static int p = 0;
    private static short q = 0;
    protected View a;
    FrameLayout d;
    private Timer e;
    /* access modifiers changed from: private */
    public Timer f;
    private Timer g;
    private ru.dienet.wolfy.tv.appcore.video.a h;
    /* access modifiers changed from: private */
    public c i;
    private b j;
    private b k;
    private boolean l = false;
    /* access modifiers changed from: private */
    public boolean m = false;
    private KeyEvent n = null;
    private long r = -1;
    @Deprecated
    private long s = -1;
    private SurfaceView t;
    private ImpulsWebView u;
    /* access modifiers changed from: private */
    public FrameLayout v;
    private FrameLayout w;
    private LinearLayout x;
    private ru.dienet.wolfy.tv.androidstb.stbb.a<SurfaceView, FrameLayout> y;

    private class a implements Runnable {
        private int b;

        private a() {
        }

        private boolean b(int i) {
            if (4 != i) {
                return false;
            }
            StbPlayer.this.i.a("onAppKeyEvent(4);");
            return true;
        }

        public void a(int i) {
            this.b = i;
        }

        public void run() {
            b(this.b);
        }
    }

    private void a(int i2, int i3) {
        if (this.h != null) {
            this.h.a(i2, i3);
        }
    }

    private void a(long j2) {
        long random = j2 + ((long) (Math.random() * ((double) j2)));
        if (this.g != null) {
            this.g.cancel();
            this.g.purge();
        }
        this.g = new Timer(true);
        this.g.schedule(new TimerTask() {
            public void run() {
                if (d.a()) {
                    ru.dienet.wolfy.tv.androidstb.util.a.a(new HdmiConnectedEvent());
                } else {
                    ru.dienet.wolfy.tv.androidstb.util.a.a(new HdmiDisconnectedEvent());
                }
            }
        }, random, random);
    }

    /* access modifiers changed from: private */
    public void a(final SslErrorHandler sslErrorHandler) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) getString(ru.dienet.wolfy.tv.R.string.app_name));
        builder.setMessage((CharSequence) String.format(getString(R.string.sslErrorConfirmIgnoreErrorDialogMessage), new Object[]{getString(R.string.sslErrorDialogContinueOption)}));
        LinearLayout linearLayout = new LinearLayout(this);
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setText(R.string.sslErrorDialogCheckboxIgnoreMessage);
        checkBox.setSelected(false);
        linearLayout.addView(checkBox);
        linearLayout.setPadding(30, 10, 10, 10);
        builder.setView((View) linearLayout);
        builder.setPositiveButton((int) R.string.sslErrorDialogContinueOption, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (checkBox.isChecked()) {
                    SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                    edit.putBoolean("preferenceSslErrorIgnore", true);
                    edit.apply();
                }
                sslErrorHandler.proceed();
            }
        });
        builder.setNeutralButton((CharSequence) "Настроить время", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                d.a(StbPlayer.this, defaultSharedPreferences, new Intent("android.settings.DATE_SETTINGS"));
            }
        });
        builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                sslErrorHandler.cancel();
                StbPlayer.this.h();
            }
        });
        builder.create().show();
    }

    /* access modifiers changed from: private */
    @TargetApi(23)
    public void a(WebResourceError webResourceError) {
        String charSequence = webResourceError.getDescription().toString();
        if (!charSequence.contains("INSUFFICIENT_RESOURCES") && !charSequence.contains("ERR_FILE_NOT_FOUND")) {
            h();
        }
    }

    private void a(FrameLayout.LayoutParams layoutParams, FrameLayout.LayoutParams layoutParams2) {
        if (!(layoutParams == null || this.h == null)) {
            this.h.setLayoutParams(layoutParams);
        }
        if (layoutParams2 != null && this.d != null) {
            this.d.setLayoutParams(layoutParams2);
        }
    }

    private void a(String str) {
        if (this.w != null && this.t != null) {
            b(13000);
            this.w.setVisibility(0);
            this.t.setVisibility(0);
            this.t.setZOrderOnTop(false);
            SurfaceHolder holder = this.t.getHolder();
            holder.setFormat(-3);
            if (c == null) {
                c = new MediaPlayer();
                try {
                    c.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + getResources().getIdentifier(str, "raw", getPackageName())));
                    c.prepare();
                } catch (IOException | IllegalArgumentException | IllegalStateException e2) {
                    e2.printStackTrace();
                }
            }
            holder.addCallback(new SurfaceHolder.Callback() {
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                }

                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    StbPlayer.c.setDisplay(surfaceHolder);
                    StbPlayer.this.d();
                }

                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                }
            });
            c.setAudioStreamType(3);
            c.setScreenOnWhilePlaying(true);
            if (e.a() >= 17) {
                c.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                        if (i != 3) {
                            return false;
                        }
                        StbPlayer.this.b();
                        return false;
                    }
                });
            } else {
                c.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        StbPlayer.this.b();
                    }
                });
            }
            c.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            StbPlayer.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    StbPlayer.this.c();
                                    if (StbPlayer.c != null) {
                                        if (StbPlayer.c.isPlaying()) {
                                            StbPlayer.c.stop();
                                        }
                                        StbPlayer.c.reset();
                                        StbPlayer.c.release();
                                        StbPlayer.c = null;
                                    }
                                }
                            });
                        }
                    }, 3000);
                }
            });
        }
    }

    private void a(ru.dienet.wolfy.tv.appcore.video.a aVar) {
        aVar.setOnCompletionListener(new a.f() {
            public void a() {
                StbPlayer.this.l();
            }
        });
        aVar.setOnErrorListener(new a.c() {
            public boolean a(String str) {
                if (str == null) {
                    StbPlayer.this.m();
                    return false;
                }
                StbPlayer.this.b(str);
                return false;
            }
        });
        aVar.setOnPlayingStartListener(new a.d() {
            public void a(int i) {
                StbPlayer.this.d(i);
            }
        });
        aVar.setOnAudioTrackInfoListener(new a.C0026a() {
            public void a(String[] strArr, int[] iArr, int i) {
                StbPlayer.this.a(strArr, iArr, i);
            }
        });
        aVar.setOnSpuTrackInfoListener(new a.e() {
        });
        aVar.setOnBufferingEventListener(new a.b() {
            public void a() {
                StbPlayer.this.n();
                ru.dienet.wolfy.tv.appcore.a.d.b("Buffering: start");
            }

            public void b() {
                StbPlayer.this.o();
                ru.dienet.wolfy.tv.appcore.a.d.b("Buffering: stop");
            }
        });
    }

    /* access modifiers changed from: private */
    @TargetApi(19)
    public void a(String[] strArr, int[] iArr, int i2) {
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        if (!(strArr == null || iArr == null)) {
            int i3 = 0;
            while (i3 < strArr.length) {
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("language", strArr[i3]);
                    jSONObject2.put("trackId", iArr[i3]);
                    jSONArray.put(jSONObject2);
                    i3++;
                } catch (JSONException e2) {
                    ru.dienet.wolfy.tv.appcore.a.d.b("Audio track info error: " + e2.getMessage());
                    return;
                }
            }
            jSONObject.put("tracks", jSONArray);
        }
        jSONObject.put("selectedTrackId", i2);
        ru.dienet.wolfy.tv.appcore.a.d.b("audio: " + jSONObject.toString());
        this.i.a("onAudioTrackList( '" + jSONObject.toString() + "' );");
    }

    private boolean a(int i2, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            if (this.l) {
                j();
            } else if (this.m) {
                if (this.k == null) {
                    this.k = new b(this);
                }
                ru.dienet.wolfy.tv.appcore.a.d.a("KeyActionEvent" + String.valueOf(keyEvent.getAction()), d.a.FATAL);
                this.k.a((DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (StbPlayer.this.i != null) {
                            StbPlayer.this.h();
                        }
                    }
                });
            } else if ((i2 == 111 || i2 == 4) && e()) {
                this.u.goBack();
            }
            if (!a(keyEvent) || !c(i2)) {
                String str = null;
                switch (i2) {
                    case 4:
                        str = "onAppKeyEvent(4);";
                        break;
                    case 24:
                        str = "onAppKeyEvent(24);";
                        break;
                    case 25:
                        str = "onAppKeyEvent(25);";
                        break;
                    case 82:
                        str = "onAppKeyEvent(82);";
                        break;
                    case 85:
                    case 127:
                        str = "onAppKeyEvent(85);";
                        break;
                    case 86:
                        str = "onAppKeyEvent(86);";
                        break;
                    case 87:
                        str = "onAppKeyEvent(87);";
                        break;
                    case 88:
                        str = "onAppKeyEvent(88);";
                        break;
                    case 89:
                        str = "onAppKeyEvent(89);";
                        break;
                    case 90:
                        str = "onAppKeyEvent(90);";
                        break;
                    case 91:
                    case 164:
                        str = "onAppKeyEvent(164);";
                        break;
                    case 130:
                        str = "onAppKeyEvent(130);";
                        break;
                    case 140:
                    case 168:
                        str = "onAppKeyEvent(140);";
                        break;
                    case 165:
                        str = "onAppKeyEvent(196);";
                        break;
                }
                if (str != null) {
                    this.i.a(str);
                } else {
                    this.n = keyEvent;
                    b(i2, keyEvent);
                }
            }
        }
        return true;
    }

    private boolean a(int i2, boolean z) {
        return 23 == i2 || 66 == i2 || (z && 96 == i2);
    }

    private boolean a(KeyEvent keyEvent) {
        return (keyEvent.getSource() & 1025) == 1025;
    }

    /* access modifiers changed from: private */
    public void b() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                StbPlayer.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (StbPlayer.this.v != null) {
                            StbPlayer.this.v.setVisibility(8);
                        }
                    }
                });
            }
        }, 800);
    }

    private void b(int i2) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                StbPlayer.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (StbPlayer.c != null) {
                            try {
                                StbPlayer.c.stop();
                            } catch (Exception e) {
                            }
                        }
                        StbPlayer.this.c();
                    }
                });
            }
        }, (long) i2);
    }

    private void b(int i2, KeyEvent keyEvent) {
        if (this.u.dispatchKeyEvent(keyEvent)) {
            return;
        }
        if (i2 == 0 || i2 < 0) {
            c(i2, keyEvent);
            return;
        }
        String str = Build.MODEL;
        char c2 = 65535;
        switch (str.hashCode()) {
            case -1599154794:
                if (str.equals("Vermax HD100")) {
                    c2 = 2;
                    break;
                }
                break;
            case -976987217:
                if (str.equals("NV310WAC")) {
                    c2 = 3;
                    break;
                }
                break;
            case 1127256759:
                if (str.equals("MX Enjoy TV BOX")) {
                    c2 = 0;
                    break;
                }
                break;
            case 1208196739:
                if (str.equals("rk31sdk")) {
                    c2 = 1;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
            case 1:
            case 2:
            case 3:
                c(i2, keyEvent);
                return;
            default:
                if (i2 != 3) {
                    ru.dienet.wolfy.tv.appcore.a.d.a((Throwable) new InputMismatchException("Unhandled remote controller key input"), d.a.WARNING, "On device " + Build.MODEL + " not handled key " + keyEvent.toString());
                    return;
                }
                return;
        }
    }

    /* access modifiers changed from: private */
    public void b(long j2) {
        this.i.a(false);
        if (this.k == null) {
            this.k = new b(this);
        }
        this.k.a((DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                StbPlayer.this.i();
            }
        }, j2);
    }

    /* access modifiers changed from: private */
    public void b(String str) {
        if (this.i != null) {
            this.i.a("App.popupMessageScreen.setMessageText('" + str + "');");
            this.i.a(getString(R.string.javascriptFunctionShowPopupMessage));
        }
    }

    /* access modifiers changed from: private */
    public void c() {
        if (this.u != null) {
            if (!(this.t == null || this.w == null)) {
                this.t.setVisibility(8);
                this.w.setVisibility(8);
            }
            this.u.requestFocus();
        }
        System.gc();
    }

    private void c(int i2, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            String str = "onAppKeyEvent(" + String.valueOf(i2) + "," + String.valueOf(keyEvent.getScanCode()) + ");";
            if (this.i != null) {
                this.i.a(str);
            }
        }
    }

    private boolean c(int i2) {
        String str = null;
        switch (i2) {
            case 4:
            case 97:
            case 189:
                str = "onAppKeyEvent(4);";
                break;
            case 23:
            case 96:
            case 188:
                KeyEvent keyEvent = new KeyEvent(0, 66);
                new KeyEvent(1, 66);
                b(66, keyEvent);
                return true;
            case 82:
            case 109:
                str = "onAppKeyEvent(82);";
                break;
            case 99:
                a();
                return true;
            case 102:
                str = "onAppKeyEvent(165);";
                break;
            case 108:
                str = "onAppKeyEvent(85);";
                break;
        }
        if (str == null) {
            return false;
        }
        this.i.a(str);
        return true;
    }

    /* access modifiers changed from: private */
    public void d() {
        c.start();
    }

    /* access modifiers changed from: private */
    public void d(int i2) {
        if (this.i != null) {
            this.i.a(getString(R.string.javascriptFunctionOnVideoStart, new Object[]{Integer.valueOf(i2)}));
            if (p > 0) {
                this.f = new Timer();
                this.f.schedule(new TimerTask() {
                    public void run() {
                        int unused = StbPlayer.p = 0;
                        Timer unused2 = StbPlayer.this.f = null;
                    }
                }, 120000);
            }
        }
    }

    private boolean e() {
        String url = this.u.getUrl();
        if (url == null) {
            return true;
        }
        return !Uri.parse(url).getHost().contains(Uri.parse(k()).getHost()) && this.u.canGoBack();
    }

    private void f() {
        g();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2, 16);
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(layoutParams);
        this.x = new LinearLayout(this);
        this.x.addView(progressBar);
        this.x.setOrientation(0);
        this.x.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        ((FrameLayout) this.a).addView(this.x);
    }

    /* access modifiers changed from: private */
    public void g() {
        if (this.x != null) {
            ((FrameLayout) this.a).removeView(this.x);
            this.x = null;
        }
    }

    /* access modifiers changed from: private */
    public void h() {
        if (!this.l) {
            ru.dienet.wolfy.tv.appcore.a.d.b("ConnectionEvent: Portal loading error handled");
            this.l = true;
            this.m = false;
            g();
            this.i.b("errorpage.html");
            j();
        }
    }

    /* access modifiers changed from: private */
    public void i() {
        this.l = false;
        this.m = true;
        this.i.a(true);
        f();
        if (ru.dienet.wolfy.tv.appcore.a.a.b(getApplicationContext())) {
            this.i.a(k(), (WebViewClient) new WebViewClient() {
                @TargetApi(23)
                private String a(WebResourceError webResourceError) {
                    return "description:" + webResourceError.getDescription() + " errorCode:" + String.valueOf(webResourceError.getErrorCode());
                }

                public void onPageFinished(WebView webView, String str) {
                    boolean unused = StbPlayer.this.m = false;
                    StbPlayer.this.g();
                    super.onPageFinished(webView, str);
                }

                public void onReceivedError(WebView webView, int i, String str, String str2) {
                    ru.dienet.wolfy.tv.appcore.a.d.a((Throwable) new Exception("Description:" + str + " errCode:" + String.valueOf(i) + " fallingUrl:" + str2), d.a.INFO, StbPlayer.this.getString(R.string.errorReceivedByWebView));
                    StbPlayer.this.h();
                }

                @TargetApi(23)
                public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                    d.a aVar = d.a.INFO;
                    int errorCode = webResourceError.getErrorCode();
                    if (errorCode == -2) {
                        StbPlayer.this.a(webResourceError);
                    } else {
                        aVar = d.a.WARNING;
                    }
                    if (errorCode == -8) {
                        aVar = d.a.DEBUG;
                    }
                    ru.dienet.wolfy.tv.appcore.a.d.a((Throwable) new Exception("Received portal error. Method:" + webResourceRequest.getMethod() + " URL:" + webResourceRequest.getUrl().toString() + a(webResourceError) + webResourceError.toString() + " ErrorCode: " + errorCode), aVar, StbPlayer.this.getString(R.string.errorReceivedByWebView));
                }

                public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                    super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                }

                public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                    int i;
                    if (Build.VERSION.SDK_INT >= 14) {
                        ru.dienet.wolfy.tv.appcore.a.d.a("SSL Error on retrieve url " + sslError.getUrl(), d.a.ERROR);
                    }
                    int primaryError = sslError.getPrimaryError();
                    switch (primaryError) {
                        case 0:
                            i = R.string.notificationErrorSslNotYetValid;
                            break;
                        case 1:
                            i = R.string.notificationErrorSslExpired;
                            break;
                        case 2:
                            i = R.string.notificationErrorSslIdMismatch;
                            break;
                        case 3:
                            i = R.string.notificationErrorSslUntrusted;
                            break;
                        case 4:
                            i = R.string.notificationErrorSslDateInvalid;
                            break;
                        case 5:
                            i = R.string.notificationErrorSslInvalid;
                            break;
                        default:
                            i = R.string.notificationErrorSslCertInvalid;
                            break;
                    }
                    boolean z = PreferenceManager.getDefaultSharedPreferences(StbPlayer.this).getBoolean("preferenceSslErrorIgnore", false);
                    ru.dienet.wolfy.tv.appcore.a.d.a("SSL error: " + i + " " + sslError.toString());
                    if (!z) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StbPlayer.this);
                        builder.setTitle((CharSequence) StbPlayer.this.getString(R.string.sslErrorDialogTitle));
                        builder.setMessage((CharSequence) String.format(StbPlayer.this.getString(R.string.sslErrorDialogMessage), new Object[]{Integer.valueOf(primaryError), StbPlayer.this.getString(i)}));
                        builder.setPositiveButton((CharSequence) StbPlayer.this.getString(R.string.sslErrorDialogContinueOption), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                StbPlayer.this.a(sslErrorHandler);
                            }
                        });
                        builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sslErrorHandler.cancel();
                                StbPlayer.this.h();
                            }
                        });
                        builder.create().show();
                        return;
                    }
                    sslErrorHandler.proceed();
                }

                public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
                    super.onUnhandledKeyEvent(webView, keyEvent);
                    ru.dienet.wolfy.tv.appcore.a.d.a((Throwable) new Exception("UnhandledKeyEvent"), d.a.WARNING, keyEvent.toString());
                }
            });
            this.i.a(163);
            return;
        }
        ru.dienet.wolfy.tv.appcore.a.d.b("ConnectionEvent: Unable portal load^ no connection");
        b(10000);
    }

    private void j() {
        if (!ru.dienet.wolfy.tv.appcore.a.a.b(getApplicationContext()) || !this.l) {
            b(0);
            return;
        }
        if (this.k == null) {
            this.k = new b(this);
        }
        this.k.b((DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                StbPlayer.this.b(0);
            }
        });
    }

    private String k() {
        String a2 = new a().a();
        if (a2 == null || "".equals(a2)) {
            ru.dienet.wolfy.tv.appcore.a.d.a((Throwable) new IllegalArgumentException(getString(R.string.errorNotDefinedPortalUrl)), getString(R.string.errorMessagePortalParamNotDefined));
            return "";
        } else if (a2.startsWith("http://") || a2.startsWith("https://")) {
            return a2;
        } else {
            throw new IllegalArgumentException(getString(R.string.errorUrlPrefixIsIncorrect));
        }
    }

    /* access modifiers changed from: private */
    public void l() {
        if (this.i != null) {
            this.i.a(getString(R.string.javascriptFunctionOnVideoComplete));
        }
    }

    /* access modifiers changed from: private */
    public void m() {
        if (this.i != null) {
            if (this.f != null) {
                this.f.cancel();
                this.f = null;
            }
            if (p >= 3) {
                if (this.h != null) {
                    this.h.d();
                }
                b(getString(R.string.messageTextVideoUnavailable));
                this.h.destroyDrawingCache();
                p = 0;
                return;
            }
            p++;
            Snackbar.make(this.a, (CharSequence) String.format(getString(R.string.formattedMessageConnectError), new Object[]{Integer.valueOf(p)}), 0).show();
            this.i.a(true);
            this.i.a(getString(R.string.javascriptFunctionOnVideoError));
        }
    }

    /* access modifiers changed from: private */
    public void n() {
        this.e = new Timer();
        this.e.schedule(new TimerTask() {
            public void run() {
                StbPlayer.this.o();
            }
        }, 10000);
        if (this.i != null) {
            this.i.a(getString(R.string.javascriptFunctionOnVideoBufferingBegin));
        }
        ru.dienet.wolfy.tv.appcore.a.d.b(getString(R.string.debugMessageBeginBuffering));
    }

    /* access modifiers changed from: private */
    public void o() {
        if (this.e != null) {
            this.e.cancel();
            this.e = null;
        }
        if (this.i != null) {
            this.i.a(getString(R.string.javascriptFunctionOnVideoBufferingEnd));
        }
        ru.dienet.wolfy.tv.appcore.a.d.b(getString(R.string.debugMessageBufferingDone));
        p = 0;
    }

    private void p() {
        if (this.h != null && this.h.a()) {
            try {
                this.h.d();
                l();
            } catch (Exception e2) {
                ru.dienet.wolfy.tv.appcore.a.d.a((Throwable) e2, d.a.DEBUG);
            }
        }
    }

    private void q() {
        setRequestedOrientation(0);
        p();
        if (e.a() >= 16) {
            this.i.b(true);
        }
        this.i.a(new JavascriptInterfaceHandler(this, new ru.dienet.wolfy.tv.androidstb.stba.b(this, this.i, this.h)), getString(R.string.systemConst_javaScriptInterfaceName));
        if (ru.dienet.wolfy.tv.appcore.a.d.a()) {
            this.i.c(true);
        }
        this.i.a();
        i();
    }

    /* access modifiers changed from: package-private */
    public void a() {
        this.i.a("onLongKeyEnter();");
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle bundle) {
        int i2;
        Activity activity;
        Intent intent = getIntent();
        intent.setFlags(intent.getFlags() | 1073741824);
        setIntent(intent);
        super.onCreate(bundle);
        ru.dienet.wolfy.tv.appcore.a.d.b("Lifecycle: onCreate");
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("preferenceUseVlc", getResources().getBoolean(R.bool.useVLC))) {
            if ("plustv".contains("telset")) {
            }
            i2 = 1;
        } else {
            i2 = 0;
        }
        setContentView((int) R.layout.stb_layout);
        this.d = (FrameLayout) findViewById(R.id.playerSurfaceFrame);
        this.j = new b(this, this.d, i2);
        this.y = this.j.b();
        this.h = this.j.c();
        this.u = (ImpulsWebView) findViewById(R.id.portalWebView);
        if (getResources().getBoolean(R.bool.usedStalkerPortal)) {
            this.u.setFocusable(true);
            this.u.setFocusableInTouchMode(true);
        }
        this.u.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return motionEvent.getAction() == 2;
            }
        });
        this.u.setVerticalScrollBarEnabled(false);
        this.u.setHorizontalScrollBarEnabled(false);
        this.a = findViewById(R.id.fullscreenContent);
        if (!(b == null || (activity = (Activity) b.get()) == null || activity == this)) {
            ru.dienet.wolfy.tv.appcore.a.d.b(getString(R.string.debugMessageKillingPreviousState));
            activity.finish();
        }
        WebSettings settings = this.u.getSettings();
        settings.setCacheMode(2);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
        }
        if (!settings.getLoadWithOverviewMode() && !"plustv".equalsIgnoreCase("orbita")) {
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
        }
        settings.setBuiltInZoomControls(false);
        this.v = (FrameLayout) findViewById(R.id.logoHider);
        this.w = (FrameLayout) findViewById(R.id.coloredBg);
        this.t = (SurfaceView) findViewById(R.id.surfaceView);
        if (this.v != null) {
            this.v.setVisibility(0);
        }
        String string = getString(R.string.rawLogoFileNameNoExtensions);
        if ("".equals(string)) {
            b(3000);
        } else {
            a(string);
        }
        this.i = new c(this.u, (View.OnKeyListener) null);
        a(this.h);
        try {
            invalidateOptionsMenu();
        } catch (Exception e2) {
        }
        q();
        a(60000);
        ru.dienet.wolfy.tv.androidstb.util.d.a(this);
    }

    public void onDestroy() {
        this.i.b();
        ru.dienet.wolfy.tv.appcore.a.d.b("Lifecycle: onDestroy");
        if (this.g != null) {
            this.g.cancel();
        }
        super.onDestroy();
        if (this.k != null) {
            this.k.a();
        }
        b = null;
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        KeyEvent keyEvent = null;
        KeyEvent keyEvent2 = null;
        if (ru.dienet.wolfy.tv.androidstb.util.b.b(motionEvent)) {
            switch (new ru.dienet.wolfy.tv.androidstb.util.b().a(motionEvent)) {
                case 0:
                    keyEvent = new KeyEvent(0, 19);
                    keyEvent2 = new KeyEvent(1, 19);
                    break;
                case 1:
                    keyEvent = new KeyEvent(0, 21);
                    keyEvent2 = new KeyEvent(1, 21);
                    break;
                case 2:
                    keyEvent = new KeyEvent(0, 22);
                    keyEvent2 = new KeyEvent(1, 22);
                    break;
                case 3:
                    keyEvent = new KeyEvent(0, 20);
                    keyEvent2 = new KeyEvent(1, 20);
                    break;
                case 4:
                    keyEvent = new KeyEvent(0, 23);
                    keyEvent2 = new KeyEvent(1, 23);
                    break;
            }
            if (keyEvent != null) {
                a(keyEvent.getKeyCode(), keyEvent);
                a(keyEvent2.getKeyCode(), keyEvent2);
                return true;
            }
        }
        return super.onGenericMotionEvent(motionEvent);
    }

    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        ru.dienet.wolfy.tv.appcore.a.d.b("onKeyDown " + i2);
        long currentTimeMillis = System.currentTimeMillis();
        if (this.s == -1) {
            this.s = currentTimeMillis;
        }
        boolean a2 = a(keyEvent);
        this.s = currentTimeMillis;
        if (keyEvent.getAction() != 0 || !a(i2, a2)) {
            this.r = -1;
            if (i2 != 4 && ((!a2 || i2 != 97) && i2 != 189)) {
                a(i2, keyEvent);
            } else if (e()) {
                this.u.goBack();
            } else {
                a aVar = new a();
                aVar.a(4);
                o.post(aVar);
            }
        } else {
            this.n = keyEvent;
            if (this.r == -1) {
                this.r = System.currentTimeMillis();
            }
        }
        return true;
    }

    public boolean onKeyUp(int i2, KeyEvent keyEvent) {
        ru.dienet.wolfy.tv.appcore.a.d.b("onKeyUp " + i2);
        if (a(i2, a(keyEvent))) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.n != null && this.n.getKeyCode() == i2) {
                if (currentTimeMillis - this.r < 501) {
                    a(i2, this.n);
                } else {
                    a();
                }
                this.r = -1;
            }
        }
        return super.onKeyUp(i2, keyEvent);
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(BringToFrontRequestEvent bringToFrontRequestEvent) {
        switch (bringToFrontRequestEvent.getViewLayer()) {
            case WEB:
                this.u.bringToFront();
                break;
            case VIDEO:
                this.d.bringToFront();
                break;
            default:
                if (this.w != null) {
                    this.w.bringToFront();
                    break;
                }
                break;
        }
        this.a.invalidate();
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(DispatchAppKeyEvent dispatchAppKeyEvent) {
        if (this.n != null) {
            c(this.n.getKeyCode(), this.n);
            this.n = null;
        }
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(ExternalApplicationNotFoundEvent externalApplicationNotFoundEvent) {
        if (this.k == null) {
            this.k = new b(this);
        }
        this.k.a(externalApplicationNotFoundEvent.getAppIdString());
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(HdmiConnectedEvent hdmiConnectedEvent) {
        if (q != 1) {
            if (this.i != null) {
                ru.dienet.wolfy.tv.appcore.a.d.b("onHdmiConnectEvent");
                this.i.a("onHdmiConnectEvent(1);");
                a(60000);
            }
            q = 1;
        }
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(HdmiDisconnectedEvent hdmiDisconnectedEvent) {
        if (q != 0) {
            if (this.i != null) {
                ru.dienet.wolfy.tv.appcore.a.d.b("HdmiDisconnectedEvent");
                this.i.a("onHdmiConnectEvent(0);");
                a(5000);
            }
            q = 0;
        }
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(HideUiEvent hideUiEvent) {
        if (this.a != null) {
            new c(this).a(this.a, 100);
        }
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(RequestPortalRebootEvent requestPortalRebootEvent) {
        p();
        this.h.destroyDrawingCache();
        this.i.b();
        i();
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(SetBoundedVideoViewSizeRequestEvent setBoundedVideoViewSizeRequestEvent) {
        int leftCornerPosition = setBoundedVideoViewSizeRequestEvent.getLeftCornerPosition();
        int topCornerPosition = setBoundedVideoViewSizeRequestEvent.getTopCornerPosition();
        int width = setBoundedVideoViewSizeRequestEvent.getWidth();
        int height = setBoundedVideoViewSizeRequestEvent.getHeight();
        a(width, height);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(width, height);
        marginLayoutParams.setMargins(leftCornerPosition, topCornerPosition, 0, 0);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(marginLayoutParams);
        if (getResources().getBoolean(R.bool.useVLC)) {
            if (this.h != null) {
                this.h.setVideoViewAspect(3);
            }
            a(layoutParams2, layoutParams2);
            a(width, height);
            return;
        }
        a(layoutParams2, layoutParams);
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(SetVideoAspectEvent setVideoAspectEvent) {
        if (this.h != null) {
            this.h.setVideoViewAspect(setVideoAspectEvent.getVideoViewAspectId());
        }
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(SetVideoViewSizeToFullscreenRequestEvent setVideoViewSizeToFullscreenRequestEvent) {
        int width = getWindow().getDecorView().getWidth();
        int height = getWindow().getDecorView().getHeight();
        a(width, height);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        a(layoutParams, layoutParams);
        if (getResources().getBoolean(R.bool.useVLC)) {
            a(width, height);
        }
    }

    @j(a = ThreadMode.MAIN)
    public void onMessage(SetWebViewAlphaLevelEvent setWebViewAlphaLevelEvent) {
        if (this.u != null) {
            this.u.setAlpha(setWebViewAlphaLevelEvent.getAlphaLevel());
        }
    }

    public void onPause() {
        p();
        super.onPause();
        ru.dienet.wolfy.tv.appcore.a.d.b("Lifecycle: onPause");
        if (!isFinishing()) {
            b = new WeakReference<>(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onPostResume() {
        super.onPostResume();
        ru.dienet.wolfy.tv.appcore.a.d.b("Lifecycle: onPostResume");
        setRequestedOrientation(0);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        ru.dienet.wolfy.tv.appcore.a.d.b("Lifecycle: onRestoreInstanceState");
        this.i.a(bundle);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        ru.dienet.wolfy.tv.androidstb.util.d.a(this);
        if (this.k != null) {
            this.k.a(false);
        }
        ru.dienet.wolfy.tv.appcore.a.d.b("Lifecycle: onResume");
        new c(this).a(this.a, 100);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        ru.dienet.wolfy.tv.appcore.a.d.b("Lifecycle: onSaveInstanceState");
        this.i.b(bundle);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        ru.dienet.wolfy.tv.androidstb.util.a.b(this);
        if (this.y != null) {
            this.y.a();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        ru.dienet.wolfy.tv.androidstb.util.a.c(this);
        super.onStop();
        if (this.y != null) {
            this.y.b();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    @j(a = ThreadMode.MAIN)
    public void onVideoPathChanged(SetVideoPathEvent setVideoPathEvent) {
        this.j.a();
        this.y = this.j.b();
        this.h = this.j.c();
        a(this.h);
        ru.dienet.wolfy.tv.androidstb.a.b.a(this.h);
        this.h.setVideoPath(setVideoPathEvent.getVideoUri());
    }
}
