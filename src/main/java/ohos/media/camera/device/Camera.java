package ohos.media.camera.device;

import java.util.List;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.FrameConfig;

public interface Camera {
    public static final int CAPTURE_TRIGGER_ERROR_ID = -1;

    public @interface FrameConfigType {
        public static final int FRAME_CONFIG_PICTURE = 2;
        public static final int FRAME_CONFIG_PREVIEW = 1;
        public static final int FRAME_CONFIG_RECORD = 3;
    }

    void configure(CameraConfig cameraConfig);

    void flushCaptures();

    CameraConfig.Builder getCameraConfigBuilder();

    String getCameraId();

    FrameConfig.Builder getFrameConfigBuilder(@FrameConfigType int i);

    void release();

    void stopLoopingCapture();

    int triggerLoopingCapture(FrameConfig frameConfig);

    int triggerLoopingGroupCapture(List<FrameConfig> list);

    int triggerMultiCapture(List<FrameConfig> list);

    int triggerSingleCapture(FrameConfig frameConfig);
}
