package ru.dienet.wolfy.tv.androidstb.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

public class b {
    /* access modifiers changed from: private */
    public final Activity a;
    private AlertDialog b;
    private BroadcastReceiver c = null;
    /* access modifiers changed from: private */
    public Handler d;

    public interface a {
        void a();
    }

    public b(Activity activity) {
        this.a = activity;
    }

    /* access modifiers changed from: private */
    public void a(DialogInterface.OnClickListener onClickListener, a aVar) {
        if (!this.a.isFinishing()) {
            if (ru.dienet.wolfy.tv.appcore.a.a.b(this.a.getApplicationContext()) && aVar != null) {
                aVar.a();
            }
            a(false);
            this.b = new ProgressDialog(this.a);
            this.b.setMessage(this.a.getString(R.string.waitForConnection) + "");
            this.b.setCancelable(false);
            if (onClickListener != null) {
                this.b.setButton(-1, this.a.getString(R.string.openSystemSettings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            b.this.a.startActivity(new Intent("android.settings.SETTINGS"));
                        } catch (Exception e) {
                            Toast.makeText(b.this.a, R.string.notFoundSystemActivityForSystemSettings, 1).show();
                        }
                    }
                });
                this.b.setButton(-3, this.a.getString(R.string.wifiSettingsString), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            b.this.a.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                        } catch (Exception e) {
                            Toast.makeText(b.this.a, R.string.notFoundSystemActivityForWifiSettings, 1).show();
                        }
                    }
                });
                this.b.setButton(-2, this.a.getString(R.string.retryNow), onClickListener);
            }
            this.b.show();
        }
    }

    private boolean a(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return ((String) packageManager.getPackageInfo("com.android.vending", 1).applicationInfo.loadLabel(packageManager)) != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void a() {
        a(true);
    }

    public void a(DialogInterface.OnClickListener onClickListener) {
        a();
        this.b = new AlertDialog.Builder(this.a).setMessage(R.string.abortConnection).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039370, onClickListener).show();
    }

    public void a(final DialogInterface.OnClickListener onClickListener, long j) {
        a(onClickListener, (a) new a() {
            public void a() {
                onClickListener.onClick((DialogInterface) null, -1);
            }
        }, j);
    }

    public void a(final DialogInterface.OnClickListener onClickListener, final a aVar, long j) {
        if (this.c == null) {
            this.c = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (ru.dienet.wolfy.tv.appcore.a.a.b(context.getApplicationContext())) {
                        d.b("ConnectionEvent: Recieved net message");
                        if (b.this.d != null) {
                            d.b("ConnectionEvent: Removed delay dialog shower");
                            b.this.d.removeCallbacksAndMessages((Object) null);
                        }
                        b.this.a();
                        if (aVar != null) {
                            aVar.a();
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.a.registerReceiver(this.c, intentFilter);
        }
        this.d = new Handler();
        this.d.postDelayed(new Runnable() {
            public void run() {
                d.b("ConnectionEvent: Dialog showed");
                b.this.a(onClickListener, aVar);
            }
        }, j);
    }

    public void a(final String str) {
        a();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.a);
        builder.setTitle("Приложение не найдено");
        if (a((Context) this.a)) {
            builder.setMessage("Перейти в PlayMarket?").setNegativeButton(17039369, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    b.this.a(false);
                }
            }).setPositiveButton(17039379, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + str));
                    try {
                        b.this.a.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                    }
                }
            });
        } else {
            builder.setMessage("Установите приложение и повторите попытку").setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    b.this.a(false);
                }
            });
        }
        this.b = builder.show();
    }

    public void a(boolean z) {
        boolean z2 = true;
        if (this.b != null) {
            try {
                this.b.dismiss();
                this.b = null;
            } catch (Exception e) {
            }
        }
        d.b("try unregister broadcastReceiver");
        d.b("dismissBroadcastReciever " + z);
        d.b("activity " + (this.a != null));
        StringBuilder append = new StringBuilder().append("broadcastReceiver ");
        if (this.c == null) {
            z2 = false;
        }
        d.b(append.append(z2).toString());
        if (z && this.a != null && this.c != null) {
            try {
                this.a.unregisterReceiver(this.c);
                this.c = null;
                d.b("broadcastReceiver unregistered");
            } catch (Exception e2) {
                d.a((Throwable) e2, "Unable unregister receiver");
            }
        }
    }

    public void b(DialogInterface.OnClickListener onClickListener) {
        a();
        this.b = new AlertDialog.Builder(this.a).setTitle(R.string.connectionError).setNegativeButton(R.string.repeatConnection, onClickListener).setNeutralButton(R.string.openSystemSettings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (b.this.a != null) {
                    b.this.a.startActivity(new Intent("android.settings.SETTINGS"));
                }
            }
        }).setPositiveButton(17039360, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}
