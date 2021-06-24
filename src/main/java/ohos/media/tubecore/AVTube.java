package ohos.media.tubecore;

import ohos.media.audio.AudioStreamInfo;
import ohos.media.tubecore.adapter.AVTubeAdapter;
import ohos.media.tubecore.adapter.TubeMappingTable;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class AVTube {
    private static final int DEFAULT_MAX_VOLUME = 15;
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVTube.class);
    int curVolume = 15;
    String details;
    DeviceType deviceType;
    boolean isReady = true;
    Object label;
    int maxVolume = 15;
    int nameResId;
    PlayMode playMode = PlayMode.LOCAL;
    AudioStreamInfo.StreamType streamType = AudioStreamInfo.StreamType.STREAM_TYPE_MUSIC;
    AVTubeAdapter tubeAdapter;
    String tubeName;
    int usageScenes;

    public enum DeviceType {
        UNKNOWN(0),
        BLUETOOTH(1);
        
        private final int deviceType;

        private DeviceType(int i) {
            this.deviceType = i;
        }

        public int getValue() {
            return this.deviceType;
        }
    }

    public enum PlayMode {
        LOCAL(0),
        DISTRIBUTE(1);
        
        private final int playMode;

        private PlayMode(int i) {
            this.playMode = i;
        }

        public int getValue() {
            return this.playMode;
        }
    }

    public static class UsageScene {
        public static final int ANY = 8388615;
        public static final int DISTRIBUTE = 4;
        public static final int REAL_TIME_AUDIO = 1;
        public static final int REAL_TIME_VIDEO = 2;
        public static final int USER_EXTENDED = 8388608;
        private static int[] sceneSet = {1, 2, 4, 8388608};

        private UsageScene() {
        }

        static int sanitize(int i) {
            if (i <= 0) {
                AVTube.LOGGER.warn("[sanitize]invalid usageScenes: %{public}d", Integer.valueOf(i));
                return 0;
            }
            int i2 = 0;
            int i3 = 0;
            while (true) {
                int[] iArr = sceneSet;
                if (i2 >= iArr.length) {
                    return i3;
                }
                i3 |= (iArr[i2] & i) != 0 ? iArr[i2] : 0;
                i2++;
            }
        }
    }

    public static class SetInfo {
        AVTubeAdapter.SetInfoAdapter setInfoAdapter;

        SetInfo(Object obj) {
            if (obj instanceof AVTubeAdapter.SetInfoAdapter) {
                this.setInfoAdapter = (AVTubeAdapter.SetInfoAdapter) obj;
                return;
            }
            throw new IllegalArgumentException("adp is not instance of SetInfoAdapter");
        }

        public boolean canGroup() {
            AVTubeAdapter.SetInfoAdapter setInfoAdapter2 = this.setInfoAdapter;
            if (setInfoAdapter2 == null) {
                return false;
            }
            return setInfoAdapter2.canGroup();
        }

        public String name() {
            AVTubeAdapter.SetInfoAdapter setInfoAdapter2 = this.setInfoAdapter;
            if (setInfoAdapter2 == null) {
                return "";
            }
            return setInfoAdapter2.name();
        }
    }

    public AVTube(Object obj) {
        if (obj instanceof AVTubeAdapter) {
            this.tubeAdapter = (AVTubeAdapter) obj;
            TubeMappingTable.addKeyValuePair(this.tubeAdapter, this);
            return;
        }
        throw new IllegalArgumentException("adp is not instance of AVTubeAdapter");
    }

    public DeviceType deviceType() {
        this.deviceType = this.tubeAdapter.deviceType();
        return this.deviceType;
    }

    public int usageScenes() {
        this.usageScenes = this.tubeAdapter.usageScenes();
        return this.usageScenes;
    }

    public String tubeName() {
        this.tubeName = this.tubeAdapter.tubeName();
        return this.tubeName;
    }

    public boolean isReady() {
        this.isReady = this.tubeAdapter.isReady();
        return this.isReady;
    }

    public boolean isConnecting() {
        return this.tubeAdapter.isConnecting();
    }

    public int maxVolume() {
        this.maxVolume = this.tubeAdapter.maxVolume();
        return this.maxVolume;
    }

    public int curVolume() {
        this.curVolume = this.tubeAdapter.curVolume();
        return this.curVolume;
    }

    public void setLabel(Object obj) {
        this.label = obj;
        this.tubeAdapter.setLabel(obj);
    }

    public Object label() {
        this.label = this.tubeAdapter.label();
        return this.label;
    }

    public String details() {
        this.details = this.tubeAdapter.details();
        return this.details;
    }

    public PlayMode playMode() {
        this.playMode = this.tubeAdapter.playMode();
        return this.playMode;
    }

    public AudioStreamInfo.StreamType streamType() {
        this.streamType = this.tubeAdapter.streamType();
        return this.streamType;
    }

    public void cmdIncVolume(int i) {
        if (i <= 0) {
            LOGGER.warn("[cmdIncVolume]increment<=0, do nothing", new Object[0]);
            return;
        }
        this.curVolume = curVolume();
        this.maxVolume = maxVolume();
        int i2 = this.maxVolume;
        int i3 = this.curVolume;
        if (i2 - i3 < i) {
            LOGGER.warn("[cmdIncVolume]increment too big, ensure if input para is right", new Object[0]);
        } else {
            i2 = i3 + i;
        }
        this.tubeAdapter.cmdSetVolume(i2);
    }

    public void cmdDecVolume(int i) {
        if (i <= 0) {
            LOGGER.warn("[cmdDecVolume]decrement<=0, do nothing", new Object[0]);
            return;
        }
        this.tubeAdapter.cmdSetVolume(Math.max(0, curVolume() - i));
    }

    public void cmdSetVolume(int i) {
        this.tubeAdapter.cmdSetVolume(Math.max(0, Math.min(i, maxVolume())));
    }

    public String toString() {
        return getClass().getSimpleName() + "{ name=" + tubeName() + ", streamType=" + streamType() + ", label=" + label() + ", details=" + details() + "}";
    }
}
