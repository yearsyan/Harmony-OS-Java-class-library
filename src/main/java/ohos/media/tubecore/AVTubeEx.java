package ohos.media.tubecore;

import ohos.media.audio.AudioStreamInfo;
import ohos.media.tubecore.AVTube;
import ohos.media.tubecore.adapter.AVTubeExAdapter;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class AVTubeEx extends AVTube {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVTubeEx.class);
    AVTubeExAdapter tubeExAdapter;

    AVTubeEx(Object obj) {
        super(obj);
        if (obj instanceof AVTubeExAdapter) {
            this.tubeExAdapter = (AVTubeExAdapter) obj;
            return;
        }
        throw new IllegalArgumentException("adp is not instance of AVTubeExAdapter");
    }

    public void setCurVolume(int i) {
        this.curVolume = i;
        if (i < 0) {
            LOGGER.warn("[setCurVolume]invalid volume = %{public}d, change to zero", Integer.valueOf(i));
            this.curVolume = 0;
        }
        this.tubeExAdapter.setCurVolume(this.curVolume);
    }

    public void setMaxVolume(int i) {
        this.maxVolume = i;
        if (i < 0) {
            LOGGER.warn("[setMaxVolume]invalid volume = %{public}d, change to zero", Integer.valueOf(i));
            this.maxVolume = 0;
        }
        this.tubeExAdapter.setMaxVolume(this.maxVolume);
    }

    public void setTubeName(int i) {
        this.nameResId = i;
        this.tubeExAdapter.setTubeName(i);
    }

    public void setTubeName(String str) {
        this.tubeName = str;
        this.tubeExAdapter.setTubeName(str);
    }

    public void setDetails(String str) {
        this.details = str;
        this.tubeExAdapter.setDetails(str);
    }

    public void setPlayMode(AVTube.PlayMode playMode) {
        this.playMode = playMode;
        this.tubeExAdapter.setPlayMode(playMode);
    }

    public void setStreamType(AudioStreamInfo.StreamType streamType) {
        this.streamType = streamType;
        this.tubeExAdapter.setStreamType(streamType);
    }

    @Override // ohos.media.tubecore.AVTube
    public void cmdIncVolume(int i) {
        if (i <= 0) {
            LOGGER.warn("[cmdIncVolume]increment<=0, do nothing", new Object[0]);
            return;
        }
        this.curVolume = curVolume();
        this.maxVolume = maxVolume();
        int i2 = this.maxVolume;
        if (this.maxVolume - this.curVolume < i) {
            LOGGER.warn("[cmdIncVolume]increment too big, ensure if input para is right", new Object[0]);
        } else {
            i2 = this.curVolume + i;
        }
        this.tubeExAdapter.cmdSetVolume(i2);
    }

    @Override // ohos.media.tubecore.AVTube
    public void cmdDecVolume(int i) {
        if (i <= 0) {
            LOGGER.warn("[cmdDecVolume]decrement<=0, do nothing", new Object[0]);
            return;
        }
        this.tubeExAdapter.cmdSetVolume(Math.max(0, curVolume() - i));
    }

    @Override // ohos.media.tubecore.AVTube
    public void cmdSetVolume(int i) {
        this.tubeExAdapter.cmdSetVolume(Math.max(0, Math.min(i, maxVolume())));
    }
}
