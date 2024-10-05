package ru.dienet.wolfy.tv.appcore.video;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

import ru.dienet.wolfy.tv.R;

public class ImpulsMediaController extends FrameLayout {
    // Media Control Listener Interface
    public interface MediaPlayerControl {
        boolean isPlaying();
        void pause();
        void start();
        void seekTo(int position);
        int getBufferPercentage();
        int getCurrentPosition();
        int getDuration();
        void toggleFullScreen();
    }

    private MediaPlayerControl player;
    private Context context;
    private ViewGroup anchor;
    private View controllerView;
    private ProgressBar progress;
    private TextView endTime, currentTime;
    private boolean showing = false;
    private boolean dragging = false;
    private boolean fullscreen = false;
    private boolean enableRewind = true;
    private ImageButton pauseButton, fullscreenButton, ffwdButton, rewButton;
    private final Handler handler = new ControllerHandler(this);
    private StringBuilder formatBuilder;
    private Formatter formatter;

    // Listeners for buttons
    private final OnClickListener playPauseListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
            updatePausePlay();
        }
    };

    private final OnClickListener fullscreenListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            player.toggleFullScreen();
            updateFullscreen();
        }
    };

    private final OnClickListener rewindListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            player.seekTo(player.getCurrentPosition() - 5000);
            updateProgress();
        }
    };

    private final OnClickListener fastForwardListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            player.seekTo(player.getCurrentPosition() + 15000);
            updateProgress();
        }
    };

    private final SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser && player != null) {
                int position = (int) ((progress / 1000f) * player.getDuration());
                player.seekTo(position);
                updateProgress();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            dragging = true;
            show(3600000);
            handler.removeMessages(ControllerHandler.SHOW_PROGRESS);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            dragging = false;
            updateProgress();
            show();
            handler.sendEmptyMessage(ControllerHandler.SHOW_PROGRESS);
        }
    };

    public ImpulsMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initControllerView();
    }

    public ImpulsMediaController(Context context) {
        super(context);
        this.context = context;
        initControllerView();
    }

    private void initControllerView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        controllerView = inflater.inflate(R.layout.layout_media_controller, this);
        initControllerButtons();
    }

    private void initControllerButtons() {
        pauseButton = controllerView.findViewById(R.id.pause);
        pauseButton.setOnClickListener(playPauseListener);

        fullscreenButton = controllerView.findViewById(R.id.fullscreen);
        fullscreenButton.setOnClickListener(fullscreenListener);

        ffwdButton = controllerView.findViewById(R.id.ffwd);
        if (ffwdButton != null) {
            ffwdButton.setOnClickListener(fastForwardListener);
        }

        rewButton = controllerView.findViewById(R.id.rew);
        if (rewButton != null) {
            rewButton.setOnClickListener(rewindListener);
        }

        progress = controllerView.findViewById(R.id.mediacontroller_progress);
        if (progress != null) {
            if (progress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) progress;
                seeker.setOnSeekBarChangeListener(seekListener);
            }
            progress.setMax(1000);
        }

        endTime = controllerView.findViewById(R.id.time);
        currentTime = controllerView.findViewById(R.id.time_current);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());

        updatePausePlay();
        updateFullscreen();
    }

    // Updates play/pause icon
    private void updatePausePlay() {
        if (controllerView == null || pauseButton == null || player == null) {
            return;
        }

        if (player.isPlaying()) {
            pauseButton.setImageResource(R.drawable.ic_pause);
        } else {
            pauseButton.setImageResource(R.drawable.ic_play);
        }
    }

    // Updates fullscreen icon
    private void updateFullscreen() {
        if (controllerView == null || fullscreenButton == null || player == null) {
            return;
        }

        if (fullscreen) {
            fullscreenButton.setImageResource(R.drawable.ic_fullscreen_exit);
        } else {
            fullscreenButton.setImageResource(R.drawable.ic_fullscreen);
        }
    }

    // Update progress bar and time labels
    private int updateProgress() {
        if (player == null || dragging) {
            return 0;
        }

        int position = player.getCurrentPosition();
        int duration = player.getDuration();
        if (progress != null && duration > 0) {
            long pos = 1000L * position / duration;
            progress.setProgress((int) pos);
        }

        if (endTime != null) {
            endTime.setText(formatTime(duration));
        }
        if (currentTime != null) {
            currentTime.setText(formatTime(position));
        }

        return position;
    }

    private String formatTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public void show() {
        show(3000);
    }

    public void show(int timeout) {
        if (!showing && anchor != null) {
            updateProgress();
            pauseButton.requestFocus();
            showing = true;
            handler.sendEmptyMessage(ControllerHandler.SHOW_PROGRESS);

            if (timeout != 0) {
                handler.removeMessages(ControllerHandler.FADE_OUT);
                handler.sendMessageDelayed(handler.obtainMessage(ControllerHandler.FADE_OUT), timeout);
            }
        }
    }

    public void hide() {
        if (anchor == null) return;

        try {
            anchor.removeView(this);
            handler.removeMessages(ControllerHandler.SHOW_PROGRESS);
            showing = false;
        } catch (IllegalArgumentException e) {
            // view not attached to window
        }
    }

    private static class ControllerHandler extends Handler {
        private final WeakReference<ImpulsMediaController> view;

        public static final int FADE_OUT = 1;
        public static final int SHOW_PROGRESS = 2;

        ControllerHandler(ImpulsMediaController view) {
            this.view = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            ImpulsMediaController controller = view.get();
            if (controller == null || controller.player == null) return;

            switch (msg.what) {
                case FADE_OUT:
                    controller.hide();
                    break;
                case SHOW_PROGRESS:
                    int pos = controller.updateProgress();
                    if (!controller.dragging && controller.showing && controller.player.isPlaying()) {
                        sendMessageDelayed(obtainMessage(SHOW_PROGRESS), 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        this.player = player;
        updatePausePlay();
        updateFullscreen();
    }

    public void setAnchorView(ViewGroup viewGroup) {
        this.anchor = viewGroup;
        removeAllViews();

        View controllerView = getControllerView();
        anchor.addView(controllerView);
    }

    protected View getControllerView() {
        return controllerView;
    }
}
