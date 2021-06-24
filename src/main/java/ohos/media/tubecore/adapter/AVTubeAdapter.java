package ohos.media.tubecore.adapter;

import android.media.MediaRouter;
import java.util.Objects;
import ohos.media.audio.AudioStreamInfo;
import ohos.media.tubecore.AVTube;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class AVTubeAdapter {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVTubeAdapter.class);
    MediaRouter.RouteInfo routeInfo;

    AVTubeAdapter(MediaRouter.RouteInfo routeInfo2) {
        Objects.requireNonNull(routeInfo2, "routeInfo must not be null");
        this.routeInfo = routeInfo2;
        TubeMappingTable.addKeyValuePair(routeInfo2, this);
    }

    public AVTube.DeviceType deviceType() {
        if (this.routeInfo.getDeviceType() == 3) {
            return AVTube.DeviceType.BLUETOOTH;
        }
        return AVTube.DeviceType.UNKNOWN;
    }

    public int usageScenes() {
        return this.routeInfo.getSupportedTypes();
    }

    public String tubeName() {
        CharSequence name = this.routeInfo.getName();
        return Objects.isNull(name) ? "" : name.toString();
    }

    public boolean isReady() {
        return this.routeInfo.isEnabled();
    }

    public boolean isConnecting() {
        return this.routeInfo.isConnecting();
    }

    public int maxVolume() {
        return this.routeInfo.getVolumeMax();
    }

    public int curVolume() {
        return this.routeInfo.getVolume();
    }

    public void setLabel(Object obj) {
        this.routeInfo.setTag(obj);
    }

    public Object label() {
        return this.routeInfo.getTag();
    }

    public String details() {
        CharSequence description = this.routeInfo.getDescription();
        return Objects.isNull(description) ? "" : description.toString();
    }

    public AVTube.PlayMode playMode() {
        int playbackType = this.routeInfo.getPlaybackType();
        if (playbackType == 0) {
            return AVTube.PlayMode.LOCAL;
        }
        if (playbackType == 1) {
            return AVTube.PlayMode.DISTRIBUTE;
        }
        LOGGER.warn("playbackType: unexpected playbackType: %{public}d", Integer.valueOf(playbackType));
        return AVTube.PlayMode.LOCAL;
    }

    public AudioStreamInfo.StreamType streamType() {
        int playbackStream = this.routeInfo.getPlaybackStream();
        if (playbackStream == 0) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_VOICE_CALL;
        }
        if (playbackStream == 1) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_SYSTEM;
        }
        if (playbackStream == 2) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_RING;
        }
        if (playbackStream == 3) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_MUSIC;
        }
        if (playbackStream == 4) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_ALARM;
        }
        if (playbackStream == 5) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_NOTIFICATION;
        }
        if (playbackStream == 8) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_DTMF;
        }
        if (playbackStream == 10) {
            return AudioStreamInfo.StreamType.STREAM_TYPE_ACCESSIBILITY;
        }
        LOGGER.warn("streamType: unexpected stream type(%{public}d).", Integer.valueOf(playbackStream));
        return AudioStreamInfo.StreamType.STREAM_TYPE_MUSIC;
    }

    public void cmdAdjustVolume(int i) {
        this.routeInfo.requestUpdateVolume(i);
    }

    public void cmdSetVolume(int i) {
        this.routeInfo.requestSetVolume(i);
    }

    public static class SetInfoAdapter {
        MediaRouter.RouteCategory routeCategory;

        SetInfoAdapter(MediaRouter.RouteCategory routeCategory2) {
            Objects.requireNonNull(routeCategory2, "init failed");
            this.routeCategory = routeCategory2;
        }

        public boolean canGroup() {
            return this.routeCategory.isGroupable();
        }

        public String name() {
            CharSequence name = this.routeCategory.getName();
            return Objects.isNull(name) ? "" : name.toString();
        }
    }
}
