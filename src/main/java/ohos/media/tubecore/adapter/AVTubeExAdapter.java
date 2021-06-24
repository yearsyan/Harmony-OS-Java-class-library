package ohos.media.tubecore.adapter;

import android.media.MediaRouter;
import ohos.media.audio.AudioStreamInfo;
import ohos.media.tubecore.AVTube;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class AVTubeExAdapter extends AVTubeAdapter {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVTubeExAdapter.class);
    MediaRouter.UserRouteInfo userRouteInfo;

    AVTubeExAdapter(MediaRouter.UserRouteInfo userRouteInfo2) {
        super(userRouteInfo2);
        this.userRouteInfo = userRouteInfo2;
    }

    public void setCurVolume(int i) {
        this.userRouteInfo.setVolume(i);
    }

    public void setMaxVolume(int i) {
        this.userRouteInfo.setVolumeMax(i);
    }

    public void setTubeName(int i) {
        this.userRouteInfo.setName(i);
    }

    public void setTubeName(String str) {
        this.userRouteInfo.setName(str);
    }

    public void setDetails(String str) {
        this.userRouteInfo.setDescription(str);
    }

    public void setPlayMode(AVTube.PlayMode playMode) {
        int i = AnonymousClass1.$SwitchMap$ohos$media$tubecore$AVTube$PlayMode[playMode.ordinal()];
        if (i == 1) {
            this.userRouteInfo.setPlaybackType(0);
        } else if (i != 2) {
            LOGGER.warn("setPlayMode: unknow playMode %{public}d.", playMode);
        } else {
            this.userRouteInfo.setPlaybackType(1);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.media.tubecore.adapter.AVTubeExAdapter$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$media$tubecore$AVTube$PlayMode = new int[AVTube.PlayMode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|(2:1|2)|3|5|6|7|8|9|10|11|12|13|14|15|16|(2:17|18)|19|21|22|23|24|26) */
        /* JADX WARNING: Can't wrap try/catch for region: R(23:0|1|2|3|5|6|7|8|9|10|11|12|13|14|15|16|(2:17|18)|19|21|22|23|24|26) */
        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|1|2|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|21|22|23|24|26) */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0035 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0075 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x002a */
        static {
            /*
            // Method dump skipped, instructions count: 128
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.media.tubecore.adapter.AVTubeExAdapter.AnonymousClass1.<clinit>():void");
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public void setStreamType(AudioStreamInfo.StreamType streamType) {
        int i = 0;
        switch (streamType) {
            case STREAM_TYPE_VOICE_CALL:
                break;
            case STREAM_TYPE_SYSTEM:
                i = 1;
                break;
            case STREAM_TYPE_RING:
                i = 2;
                break;
            case STREAM_TYPE_MUSIC:
                i = 3;
                break;
            case STREAM_TYPE_ALARM:
                i = 4;
                break;
            case STREAM_TYPE_NOTIFICATION:
                i = 5;
                break;
            case STREAM_TYPE_DTMF:
                i = 8;
                break;
            case STREAM_TYPE_ACCESSIBILITY:
                i = 10;
                break;
            default:
                LOGGER.warn("setPlaybackStreamType: unexpected stream type(%{public}d).", streamType);
                i = 3;
                break;
        }
        this.userRouteInfo.setPlaybackStream(i);
    }

    @Override // ohos.media.tubecore.adapter.AVTubeAdapter
    public void cmdSetVolume(int i) {
        this.userRouteInfo.requestSetVolume(i);
    }

    @Override // ohos.media.tubecore.adapter.AVTubeAdapter
    public void cmdAdjustVolume(int i) {
        this.userRouteInfo.requestUpdateVolume(i);
    }
}
