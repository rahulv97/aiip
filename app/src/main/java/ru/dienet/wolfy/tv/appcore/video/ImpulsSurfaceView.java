package ru.dienet.wolfy.tv.appcore.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

public class ImpulsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "ImpulsSurfaceView";

    private MediaPlayer mediaPlayer;
    private Uri videoUri;
    private SurfaceHolder surfaceHolder;
    private boolean isPrepared;
    private FrameLayout videoContainer;
    private int videoWidth;
    private int videoHeight;
    private Context context;

    public ImpulsSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImpulsSurfaceView(Context context, FrameLayout videoContainer) {
        super(context);
        this.videoContainer = videoContainer;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.isPrepared = false;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
        if (surfaceHolder != null) {
            prepareMediaPlayer();
        }
    }

    private void prepareMediaPlayer() {
        if (videoUri == null) {
            Log.e(TAG, "Video URI is null");
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(context, videoUri);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                mediaPlayer.start();
                videoWidth = mediaPlayer.getVideoWidth();
                videoHeight = mediaPlayer.getVideoHeight();
                adjustVideoSize();
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "Error setting video source: " + e.getMessage());
        }
    }

    private void adjustVideoSize() {
        if (videoWidth == 0 || videoHeight == 0) {
            return;
        }

        int screenWidth = videoContainer.getWidth();
        int screenHeight = videoContainer.getHeight();
        float videoAspect = (float) videoWidth / videoHeight;
        float screenAspect = (float) screenWidth / screenHeight;

        if (screenAspect > videoAspect) {
            screenWidth = (int) (screenHeight * videoAspect);
        } else {
            screenHeight = (int) (screenWidth / videoAspect);
        }

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = screenHeight;
        setLayoutParams(layoutParams);
    }

    public void play() {
        if (isPrepared) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (isPrepared && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mediaPlayer == null || !isPrepared) {
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
            if (mediaPlayer.isPlaying()) {
                pause();
            } else {
                play();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mediaPlayer.isPlaying()) {
                pause();
            } else {
                play();
            }
            return true;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        prepareMediaPlayer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Handle surface changes
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }
}
