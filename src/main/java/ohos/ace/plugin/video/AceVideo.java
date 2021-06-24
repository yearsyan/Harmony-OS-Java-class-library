package ohos.ace.plugin.video;

import com.huawei.ace.plugin.video.AceVideoBase;
import com.huawei.ace.runtime.AEventReport;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.IAceOnResourceEvent;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import ohos.ace.AceDisplayManager;
import ohos.agp.graphics.Surface;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.common.Format;
import ohos.media.extractor.Extractor;
import ohos.media.player.Player;

public class AceVideo extends AceVideoBase implements Player.IPlayerCallback {
    private static final String LOG_TAG = "AceVideo";
    private final Context context;
    private Extractor extractor;
    private int frameRate = 0;
    private EventHandler handler;
    private String instanceName;
    private Object lock = new Object();
    private final Player mediaPlayer;
    private Surface surface;

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public void onActivityResume() {
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onBufferingChange(int i) {
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onMediaTimeIncontinuity(Player.MediaTimeInfo mediaTimeInfo) {
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onMessage(int i, int i2) {
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onNewTimedMetaData(Player.MediaTimedMetaData mediaTimedMetaData) {
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onPrepared() {
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onResolutionChanged(int i, int i2) {
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onRewindToComplete() {
    }

    public AceVideo(long j, String str, Surface surface2, Context context2, IAceOnResourceEvent iAceOnResourceEvent) {
        super(j, iAceOnResourceEvent);
        this.surface = surface2;
        this.instanceName = str;
        this.context = context2;
        this.mediaPlayer = new Player(context2);
        this.handler = new EventHandler(EventRunner.current());
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public void release() {
        CompletableFuture.runAsync(new Runnable() {
            /* class ohos.ace.plugin.video.$$Lambda$AceVideo$VRoeIjqeT5Naq9WfkmJVKDWM22k */

            public final void run() {
                AceVideo.this.lambda$release$0$AceVideo();
            }
        });
    }

    public /* synthetic */ void lambda$release$0$AceVideo() {
        synchronized (this.lock) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            if (this.handler != null) {
                this.handler.removeAllEvent();
                this.handler = null;
            }
        }
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public String initMediaPlayer(Map<String, String> map) {
        super.lambda$new$0$AceVideoBase(map);
        this.extractor = new Extractor();
        if (!map.containsKey("src") || !setSource(map.get("src"))) {
            ALog.e(LOG_TAG, "media setSource failed.");
            AEventReport.sendPluginException(0);
            return "fail";
        } else if (!this.mediaPlayer.setVideoSurface(this.surface)) {
            ALog.e(LOG_TAG, "media setSurface failed.");
            AEventReport.sendPluginException(0);
            return "fail";
        } else {
            this.mediaPlayer.setPlayerCallback(this);
            this.frameRate = getFrameRate();
            CompletableFuture.supplyAsync(new Supplier() {
                /* class ohos.ace.plugin.video.$$Lambda$AceVideo$E1GNT1kATVXW86Q1TaBIyvmVpGE */

                @Override // java.util.function.Supplier
                public final Object get() {
                    return AceVideo.this.lambda$initMediaPlayer$1$AceVideo();
                }
            }).thenAccept((Consumer) new Consumer() {
                /* class ohos.ace.plugin.video.$$Lambda$AceVideo$FUj5Ag_A7wPhLmvjKheDwbyzmc */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AceVideo.this.lambda$initMediaPlayer$2$AceVideo((Boolean) obj);
                }
            });
            return "success";
        }
    }

    public /* synthetic */ Boolean lambda$initMediaPlayer$1$AceVideo() {
        synchronized (this.lock) {
            if (this.mediaPlayer.prepare()) {
                return true;
            }
            ALog.e(LOG_TAG, "media prepare failed.");
            AEventReport.sendPluginException(0);
            return false;
        }
    }

    public /* synthetic */ void lambda$initMediaPlayer$2$AceVideo(Boolean bool) {
        synchronized (this.lock) {
            if (this.handler != null) {
                this.handler.postTask(new PerparedTask(bool.booleanValue()));
            }
        }
    }

    /* access modifiers changed from: private */
    public class PerparedTask implements Runnable {
        private final boolean result;

        PerparedTask(boolean z) {
            this.result = z;
        }

        public void run() {
            AceVideo.this.prepared(this.result);
        }
    }

    private int getFrameRate() {
        int totalStreams = this.extractor.getTotalStreams();
        int i = 0;
        for (int i2 = 0; i2 < totalStreams; i2++) {
            Format streamFormat = this.extractor.getStreamFormat(i2);
            if (streamFormat.hasKey(Format.FRAME_RATE)) {
                i = streamFormat.getIntValue(Format.FRAME_RATE);
            }
        }
        this.extractor.release();
        return i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
        $closeResource(r12, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004e, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00de, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00df, code lost:
        if (r13 != null) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00e1, code lost:
        $closeResource(r12, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00e4, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean setSource(java.lang.String r13) {
        /*
        // Method dump skipped, instructions count: 272
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.ace.plugin.video.AceVideo.setSource(java.lang.String):boolean");
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void prepared(boolean z) {
        if (!z) {
            firePrepared(0, 0, 0, false, false);
        }
        if (isAutoPlay()) {
            this.mediaPlayer.play();
        }
        if (isMute()) {
            this.mediaPlayer.setVolume(0.0f);
        }
        firePrepared(this.mediaPlayer.getVideoWidth(), this.mediaPlayer.getVideoHeight(), this.mediaPlayer.getDuration(), this.mediaPlayer.isNowPlaying(), Math.floor((double) AceDisplayManager.getRefreshRate()) <= ((double) this.frameRate));
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onError(int i, int i2) {
        fireError();
    }

    @Override // ohos.media.player.Player.IPlayerCallback
    public void onPlayBackComplete() {
        fireCompletion();
    }

    private void onSeekComplete() {
        fireSeekComplete(this.mediaPlayer.getCurrentTime() / 1000);
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public String start(Map<String, String> map) {
        return (this.mediaPlayer.isNowPlaying() || this.mediaPlayer.play()) ? "success" : "fail";
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public String pause(Map<String, String> map) {
        return (!this.mediaPlayer.isNowPlaying() || this.mediaPlayer.pause()) ? "success" : "fail";
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public String seekTo(Map<String, String> map) {
        if (!map.containsKey("value")) {
            return "fail";
        }
        try {
            if (!this.mediaPlayer.rewindTo((long) (Integer.parseInt(map.get("value")) * 1000 * 1000))) {
                return "success";
            }
            onSeekComplete();
            return "success";
        } catch (NumberFormatException unused) {
            ALog.w(LOG_TAG, "NumberFormatException, seek failed. value = " + map.get("value"));
            return "fail";
        }
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public String setVolume(Map<String, String> map) {
        if (!map.containsKey("value")) {
            return "fail";
        }
        try {
            this.mediaPlayer.setVolume(Float.parseFloat(map.get("value")));
            return "success";
        } catch (NumberFormatException unused) {
            ALog.w(LOG_TAG, "NumberFormatException, setVolume failed. value = " + map.get("value"));
            return "fail";
        }
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public String getPosition(Map<String, String> map) {
        int currentTime = this.mediaPlayer.getCurrentTime();
        return "currentpos=" + (currentTime / 1000);
    }

    @Override // com.huawei.ace.plugin.video.AceVideoBase
    public void onActivityPause() {
        if (this.mediaPlayer.isNowPlaying()) {
            this.mediaPlayer.pause();
            firePlayStatusChange(false);
        }
    }
}
