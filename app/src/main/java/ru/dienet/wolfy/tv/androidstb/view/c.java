package ru.dienet.wolfy.tv.androidstb.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Pattern;

public class c {
    private Activity a;
    /* access modifiers changed from: private */
    public Context b;
    private SharedPreferences c = PreferenceManager.getDefaultSharedPreferences(this.b);
    private DialogInterface.OnClickListener d;

    public c(Activity activity) {
        this.a = activity;
        this.b = activity.getApplicationContext();
    }

    /* access modifiers changed from: private */
    public void a(String str, String str2) {
        SharedPreferences.Editor edit = this.c.edit();
        edit.putString("preference_udp_ip", str);
        edit.putString("preference_udp_port", str2);
        edit.apply();
    }

    /* access modifiers changed from: private */
    public void a(String str, String str2, DialogInterface dialogInterface, int i) {
        if ("".equals(str) || "".equals(str2)) {
            Toast.makeText(this.b, R.string.toastTextFieldEmpty, 0).show();
            return;
        }
        int parseInt = Integer.parseInt(str2);
        if (parseInt > 65535 || parseInt < 1) {
            Toast.makeText(this.b, R.string.toastIncorrectPortValue, 1).show();
            return;
        }
        a(str, str2);
        if (this.d != null) {
            this.d.onClick(dialogInterface, i);
        }
    }

    private InputFilter[] b() {
        final Pattern compile = Pattern.compile("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?");
        final Pattern compile2 = Pattern.compile("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)(\\.)?)+([A-Za-z]{2,6})?$");
        return new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                if (i2 <= i) {
                    return null;
                }
                String obj = spanned.toString();
                String str = obj.substring(0, i3) + charSequence.subSequence(i, i2) + obj.substring(i4);
                if (compile.matcher(str).matches()) {
                    for (String valueOf : str.split("\\.")) {
                        if (Integer.valueOf(valueOf).intValue() > 255) {
                            return "";
                        }
                    }
                    return null;
                } else if (!compile2.matcher(str).matches()) {
                    return "";
                } else {
                    return null;
                }
            }
        }};
    }

    public AlertDialog a() {
        View inflate = LayoutInflater.from(this.b).inflate(R.layout.layout_dialog_udp_proxy, (ViewGroup) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.a);
        builder.setView(inflate);
        final EditText editText = (EditText) inflate.findViewById(R.id.editTextIP);
        final EditText editText2 = (EditText) inflate.findViewById(R.id.editTextPort);
        String string = this.c.getString("preference_udp_ip", "");
        String string2 = this.c.getString("preference_udp_port", "");
        editText.setText(string);
        editText2.setText(string2);
        editText.setFilters(b());
        AlertDialog.Builder negativeButton = builder.setCancelable(true).setTitle((int) R.string.udpProxySettingsTitle).setPositiveButton(17039370, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                c.this.a(editText.getText().toString(), editText2.getText().toString(), dialogInterface, i);
            }
        }).setNeutralButton((CharSequence) "Clear", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                editText.setText("");
                editText2.setText("");
                c.this.a("", "");
                Toast.makeText(c.this.b, R.string.toastUdpSettingsCleared, 0).show();
            }
        }).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        if (Build.VERSION.SDK_INT >= 17) {
            negativeButton.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                }
            });
        }
        editText.requestFocus();
        AlertDialog create = builder.create();
        create.show();
        Window window = create.getWindow();
        if (window != null) {
            window.clearFlags(131080);
        }
        return create;
    }

    public void a(DialogInterface.OnClickListener onClickListener) {
        this.d = onClickListener;
    }
}
