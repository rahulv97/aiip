package ru.dienet.wolfy.tv.appcore.video;

import android.widget.FrameLayout;

public interface a {

    /* renamed from: ru.dienet.wolfy.tv.appcore.video.a$a  reason: collision with other inner class name */
    public interface C0026a {
        void a(String[] strArr, int[] iArr, int i);
    }

    public interface b {
        void a();

        void b();
    }

    public interface c {
        boolean a(String str);
    }

    public interface d {
        void a(int i);
    }

    public interface e {
    }

    public interface f {
        void a();
    }

    void a(int i, int i2);

    boolean a();

    boolean a(int i);

    void b();

    boolean b(int i);

    void c();

    void c(int i);

    void d();

    void destroyDrawingCache();

    int getCurrentPosition();

    int getVideoViewAspect();

    void setLayoutParams(FrameLayout.LayoutParams layoutParams);

    void setOnAudioTrackInfoListener(C0026a aVar);

    void setOnBufferingEventListener(b bVar);

    void setOnCompletionListener(f fVar);

    void setOnErrorListener(c cVar);

    void setOnPlayingStartListener(d dVar);

    void setOnSpuTrackInfoListener(e eVar);

    void setVideoPath(String str);

    void setVideoViewAspect(int i);
}
