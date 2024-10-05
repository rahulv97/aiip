package ru.dienet.wolfy.tv.androidstb.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import ru.dienet.wolfy.tv.R;
import ru.dienet.wolfy.tv.androidstb.StbPlayer;
import ru.dienet.wolfy.tv.androidstb.util.d;

public class SplashActivity extends AppCompatActivity {
    MediaPlayer a;

    /* access modifiers changed from: private */
    public void a() {
        if (d.a(this)) {
            Intent intent = new Intent(this, StbPlayer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }
    }

    private void a(String str) {
        this.a = new MediaPlayer();
        this.a.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                StbPlayer.c = mediaPlayer;
                SplashActivity.this.a();
            }
        });
        try {
            this.a.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + getResources().getIdentifier(str, "raw", getPackageName())));
            this.a.prepare();
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.splash_screen_layout);
    }

    /* access modifiers changed from: protected */
    @TargetApi(16)
    public void onResume() {
        super.onResume();
        String string = getString(R.string.rawLogoFileNameNoExtensions);
        if ("".equals(string)) {
            a();
        } else {
            a(string);
        }
    }
}
