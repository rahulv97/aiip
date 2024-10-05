package ru.dienet.wolfy.tv.androidstb.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.p;
import android.support.v4.app.z;
import android.support.v4.content.k;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import ch.arnab.simplelauncher.b;
import ch.arnab.simplelauncher.c;
import im.micro.dimm.tv.stb.plustv.R;
import java.util.ArrayList;
import ru.dienet.wolfy.tv.androidstb.util.events.HideUiEvent;

public class a extends p implements z.a<ArrayList<b>> {
    ch.arnab.simplelauncher.a a;
    GridView b;
    LinearLayout c;
    private boolean d = true;

    private void a(boolean z, boolean z2) {
        if (this.c == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        } else if (this.d != z) {
            this.d = z;
            if (z) {
                if (z2) {
                    this.c.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
                    this.b.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
                } else {
                    this.c.clearAnimation();
                    this.b.clearAnimation();
                }
                this.c.setVisibility(8);
                this.b.setVisibility(0);
            } else {
                if (z2) {
                    this.c.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
                    this.b.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
                } else {
                    this.c.clearAnimation();
                    this.b.clearAnimation();
                }
                this.c.setVisibility(0);
                this.b.setVisibility(8);
            }
            this.b.requestFocus();
        }
    }

    public k<ArrayList<b>> a(int i, Bundle bundle) {
        return new c(getActivity());
    }

    public void a(k<ArrayList<b>> kVar) {
        this.a.a((ArrayList<b>) null);
    }

    public void a(k<ArrayList<b>> kVar, ArrayList<b> arrayList) {
        this.a.a(arrayList);
        if (isResumed()) {
            a(true);
        } else {
            b(true);
        }
    }

    public void a(boolean z) {
        a(z, true);
    }

    public void b(boolean z) {
        a(z, false);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(2, R.style.DialogTheme);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        FrameLayout frameLayout = new FrameLayout(activity);
        this.c = new LinearLayout(activity);
        this.c.setOrientation(1);
        this.c.setVisibility(8);
        this.c.setGravity(17);
        this.c.addView(new ProgressBar(activity, (AttributeSet) null, 16842874), new FrameLayout.LayoutParams(-2, -2));
        frameLayout.addView(this.c, new FrameLayout.LayoutParams(-1, -1));
        this.b = new GridView(activity);
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i < 60; i++) {
            arrayList.add(Integer.valueOf(i));
        }
        this.b.setNumColumns(5);
        this.b.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent launchIntentForPackage;
                b bVar = (b) ((ch.arnab.simplelauncher.a) adapterView.getAdapter()).getItem(i);
                if (bVar != null && (launchIntentForPackage = a.this.getActivity().getPackageManager().getLaunchIntentForPackage(bVar.b())) != null) {
                    a.this.startActivity(launchIntentForPackage);
                }
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(10, 10, 10, 10);
        frameLayout.addView(this.b, layoutParams);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.a = new ch.arnab.simplelauncher.a(getActivity());
        this.b.setAdapter(this.a);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView((View) frameLayout);
        builder.setTitle((CharSequence) activity.getString(R.string.appsListDialogTitle));
        a(false);
        getLoaderManager().a(0, (Bundle) null, this);
        return builder.create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ru.dienet.wolfy.tv.androidstb.util.a.a(new HideUiEvent());
    }
}
