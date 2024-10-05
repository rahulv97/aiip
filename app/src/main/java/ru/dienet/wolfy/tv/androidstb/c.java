package ru.dienet.wolfy.tv.androidstb;

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ru.dienet.wolfy.tv.androidstb.stba.JavascriptInterfaceHandler;
import ru.dienet.wolfy.tv.appcore.corea.e;

public class c {
    /* access modifiers changed from: private */
    public WebView a;

    public c(WebView webView, View.OnKeyListener onKeyListener) {
        this.a = webView;
        if (onKeyListener != null) {
            this.a.setOnKeyListener(onKeyListener);
        }
        if (e.a() <= 15) {
            this.a.setLayerType(1, (Paint) null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.a.setLayerType(2, (Paint) null);
        }
    }

    private void c() {
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.a, true);
            CookieManager.setAcceptFileSchemeCookies(true);
            return;
        }
        CookieManager.getInstance().setAcceptCookie(true);
    }

    public void a() {
        this.a.setBackgroundColor(0);
    }

    public void a(int i) {
        this.a.requestFocus(i);
    }

    public void a(Bundle bundle) {
        if (this.a != null && bundle != null) {
            this.a.restoreState(bundle);
        }
    }

    public void a(final String str) {
        this.a.post(new Runnable() {
            public void run() {
                c.this.a.loadUrl("javascript:" + str);
            }
        });
    }

    public void a(String str, WebViewClient webViewClient) {
        this.a.setWebViewClient(webViewClient);
        if (str != null) {
            this.a.loadUrl(str);
            c();
        }
    }

    public void a(JavascriptInterfaceHandler javascriptInterfaceHandlerVar, String str) {
        this.a.addJavascriptInterface(javascriptInterfaceHandlerVar, str);
    }

    public void a(boolean z) {
        this.a.clearCache(z);
    }

    public void b() {
        if (this.a == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 18) {
            this.a.clearView();
        } else {
            this.a.loadUrl("about:blank");
        }
    }

    public void b(Bundle bundle) {
        if (this.a != null) {
            this.a.saveState(bundle);
        }
    }

    public void b(String str) {
        this.a.loadUrl("file:///android_asset/" + str);
    }

    @TargetApi(16)
    public void b(boolean z) {
        WebSettings settings = this.a.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(z);
    }

    public void c(boolean z) {
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(z);
        }
    }
}
