package ru.dienet.wolfy.tv.androidstb.view;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.dienet.wolfy.tv.R;

public class GreetingsDialog extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.layout_incompatability_device);
        ((TextView) findViewById(R.id.deviceName)).setText(Build.MODEL);
    }
}
