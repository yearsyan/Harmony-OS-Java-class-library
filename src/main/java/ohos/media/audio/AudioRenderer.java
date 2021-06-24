package ohos.media.audio;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Locale;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.audio.AudioDeviceDescriptor;
import ohos.media.audio.AudioStreamInfo;
import ohos.media.audioimpl.adapter.AudioRendererBaseAdapter;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;
import ohos.media.utils.trace.Tracer;
import ohos.media.utils.trace.TracerFactory;

public class AudioRenderer {
    private static final int DEFAULT_DEVICE_ID = 0;
    public static final int ERROR = -1;
    public static final int ERROR_INVALID_OPERATION = -2;
    private static final int JNI_EVENT_MARKED_POS_REACH = 0;
    private static final int JNI_EVENT_PERIODIC_NOTIFICATION = 1;
    private static final Logger LOGGER = LoggerFactory.getAudioLogger(AudioRenderer.class);
    public static final float MAX_PLAYBACK_SPEED = 2.0f;
    public static final float MIN_PLAYBACK_SPEED = 0.5f;
    public static final int RENDERER_SESSION_ID_NONE = 0;
    public static final int SUCCESS = 0;
    private static final float THRESHOLD = 1.0E-6f;
    private static final Tracer TRACER = TracerFactory.getAudioTracer();
    private AudioRendererInfo audioRendererInfo;
    private long bufferSizeInFrames;
    private FrameIntervalObserver frameIntervalObserver;
    private FramePositionObserver framePositionObserver;
    private long jniAudioRenderer;
    private long jniData;
    private JniFrameIntervalEventHandler jniFrameIntervalEventHandler;
    private JniFramePositionEventHandler jniFramePositionEventHandler;
    private final int playMode;
    private SpeedPara playbackSpeed;
    private AudioRendererBaseAdapter rendererBaseAdapter;
    private volatile State state;

    public static float getMaxVolume() {
        return 1.0f;
    }

    public static float getMinVolume() {
        return 0.0f;
    }

    private native boolean nativeDuckVolume();

    private native boolean nativeFlush();

    private native int nativeGetBufferFrameSize();

    private native int nativeGetBufferUnderflowTimes();

    private native int nativeGetCurrentDeviceId();

    private static native int nativeGetMinBufferSize(int i, int i2, int i3);

    private native int nativeGetPosition();

    private native int nativeGetRendererSessionId();

    private native int nativeGetSampleRate();

    private native boolean nativeGetTimestamp(long[] jArr);

    private native boolean nativePause();

    private native boolean nativePlay();

    private native boolean nativeRelease();

    private native boolean nativeSetFrameInterval(int i);

    private native boolean nativeSetFramePosition(int i);

    private native boolean nativeSetOutputDevice(int i);

    private native boolean nativeSetPlaybackHeadPosition(int i);

    private native boolean nativeSetPlaybackSpeed(float f, float f2, int i, int i2);

    private native boolean nativeSetSampleRate(int i);

    private native boolean nativeSetSendEffect(int i);

    private native void nativeSetSendEffectLevel(float f);

    private native boolean nativeSetVolume(ChannelVolume channelVolume);

    private native boolean nativeSetup(Object obj, AudioRendererInfo audioRendererInfo2, int i);

    private native boolean nativeStop();

    private native boolean nativeUnduckVolume();

    private native boolean nativeWrite(byte[] bArr, int i, int i2);

    private native int nativeWriteByteData(byte[] bArr, int i, int i2);

    private native int nativeWriteDirectBuffer(ByteBuffer byteBuffer, int i, int i2);

    private native boolean nativeWriteFloatData(float[] fArr, int i, int i2);

    private native boolean nativeWriteShortData(short[] sArr, int i, int i2);

    static {
        System.loadLibrary("audiorenderer_jni.z");
    }

    public enum State {
        STATE_UNINITIALIZED(0),
        STATE_INITIALIZED(1),
        STATE_BUFFER_PREPARED(2),
        STATE_BUFFER_PREPARED_ERROR(3),
        STATE_PLAYING(4),
        STATE_PAUSED(5),
        STATE_STOP(6);
        
        private final int state;

        private State(int i) {
            this.state = i;
        }

        public int getValue() {
            return this.state;
        }
    }

    public AudioRenderer(AudioRendererInfo audioRendererInfo2, PlayMode playMode2) throws IllegalArgumentException {
        this(audioRendererInfo2, playMode2, null);
    }

    public AudioRenderer(AudioRendererInfo audioRendererInfo2, PlayMode playMode2, AudioDeviceDescriptor audioDeviceDescriptor) throws IllegalArgumentException {
        this.state = State.STATE_UNINITIALIZED;
        this.bufferSizeInFrames = 0;
        this.playMode = playMode2.getModeValue();
        if (checkAndSetAudioRendererInfo(audioRendererInfo2)) {
            TRACER.startTrace("AudioRenderer_setAudioRendererInfo_kits");
            boolean nativeSetup = nativeSetup(new WeakReference(this), audioRendererInfo2, this.playMode);
            TRACER.finishTrace("AudioRenderer_setAudioRendererInfo_kits");
            if (nativeSetup) {
                bufferSizeCheck(audioRendererInfo2);
                this.playbackSpeed = new SpeedPara.Builder().build();
                this.rendererBaseAdapter = new AudioRendererBaseAdapter(audioRendererInfo2, 1);
                this.rendererBaseAdapter.registerRendererBase();
                this.state = State.STATE_INITIALIZED;
                return;
            }
            LOGGER.error("native setup failed.", new Object[0]);
            throw new IllegalArgumentException("Initial AudioRenderer Failed.");
        }
        LOGGER.error("check AudioRendererInfo failed.", new Object[0]);
        throw new IllegalArgumentException("Illegal AudioRendererInfo");
    }

    public State getState() {
        return this.state;
    }

    private boolean stateCheckBeforePlay() {
        if (this.state == State.STATE_PLAYING) {
            LOGGER.error("state error, renderer is playing now", new Object[0]);
            return false;
        } else if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("in stream mode, AudioRender is not ready.", new Object[0]);
            return false;
        } else if (this.playMode != PlayMode.MODE_STATIC.getModeValue() || this.state == State.STATE_BUFFER_PREPARED) {
            return true;
        } else {
            LOGGER.error("in static mode, need write first", new Object[0]);
            return false;
        }
    }

    public boolean start() {
        if (!stateCheckBeforePlay()) {
            LOGGER.error("can not play, state is %{public}d.", Integer.valueOf(this.state.getValue()));
            return false;
        }
        TRACER.startTrace("AudioRenderer_play_kits");
        if (!nativePlay()) {
            LOGGER.error("native play failed.", new Object[0]);
            TRACER.finishTrace("AudioRenderer_play_kits");
            return false;
        }
        TRACER.finishTrace("AudioRenderer_play_kits");
        this.state = State.STATE_PLAYING;
        this.rendererBaseAdapter.updateState(convertBaseState(this.state));
        return true;
    }

    public boolean pause() {
        if (this.state == State.STATE_PLAYING || this.state == State.STATE_STOP) {
            TRACER.startTrace("AudioRenderer_pause_kits");
            if (!nativePause()) {
                LOGGER.error("native pause failed.", new Object[0]);
                TRACER.finishTrace("AudioRenderer_pause_kits");
                return false;
            }
            TRACER.finishTrace("AudioRenderer_pause_kits");
            if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                this.state = State.STATE_BUFFER_PREPARED;
            } else {
                this.state = State.STATE_PAUSED;
            }
            this.rendererBaseAdapter.updateState(convertBaseState(this.state));
            return true;
        }
        LOGGER.error("can not pause, state is %{public}d.", Integer.valueOf(this.state.getValue()));
        return false;
    }

    public boolean stop() {
        if (this.state == State.STATE_PLAYING || this.state == State.STATE_PAUSED) {
            TRACER.startTrace("AudioRenderer_stop_kits");
            if (!nativeStop()) {
                LOGGER.error("native stop failed.", new Object[0]);
                TRACER.finishTrace("AudioRenderer_stop_kits");
                return false;
            }
            TRACER.finishTrace("AudioRenderer_stop_kits");
            if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                this.state = State.STATE_BUFFER_PREPARED;
            } else {
                this.state = State.STATE_STOP;
            }
            this.rendererBaseAdapter.updateState(convertBaseState(this.state));
            return true;
        }
        LOGGER.error("can not stop, state is %{public}d now.", Integer.valueOf(this.state.getValue()));
        return false;
    }

    public boolean release() {
        if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("state is uninitialized, does not need release.", new Object[0]);
            return false;
        }
        TRACER.startTrace("AudioRenderer_release_kits");
        if (!nativeRelease()) {
            LOGGER.error("native release failed.", new Object[0]);
            TRACER.finishTrace("AudioRenderer_release_kits");
            return false;
        }
        TRACER.finishTrace("AudioRenderer_release_kits");
        this.rendererBaseAdapter.unregisterRendererBase();
        this.state = State.STATE_UNINITIALIZED;
        return true;
    }

    public static int getMinBufferSize(int i, AudioStreamInfo.EncodingFormat encodingFormat, AudioStreamInfo.ChannelMask channelMask) {
        if ((i < 4000 || i > 192000) && i != 384000) {
            LOGGER.error("getMinBufferSize invalid sample rate, rate = %{public}d", Integer.valueOf(i));
            return -2;
        } else if (!AudioStreamInfo.isValidEncodingFormat(encodingFormat)) {
            LOGGER.error("getMinBufferSize invalid encoding format, format = %{public}d", Integer.valueOf(encodingFormat.getValue()));
            return -2;
        } else if (!AudioStreamInfo.isValidOutChannelMask(channelMask)) {
            LOGGER.error("getMinBufferSize invalid channel mask, mask = %{public}d", Integer.valueOf(channelMask.getValue()));
            return -2;
        } else {
            int nativeGetMinBufferSize = nativeGetMinBufferSize(i, encodingFormat.getValue(), AudioStreamInfo.getChannelCount(channelMask));
            if (nativeGetMinBufferSize <= 0) {
                return -1;
            }
            return nativeGetMinBufferSize;
        }
    }

    public boolean write(short[] sArr, int i, int i2) {
        int i3;
        if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not write, state is uninitialized", new Object[0]);
            return false;
        } else if (sArr == null || i < 0 || i2 < 0 || (i3 = i + i2) < 0 || i3 > sArr.length) {
            LOGGER.error("can not write, invalid parameters", new Object[0]);
            return false;
        } else {
            String format = String.format(Locale.ROOT, "AudioRenderer_write_short_kits:offset=%d,size=%d", Integer.valueOf(i), Integer.valueOf(i2));
            TRACER.startTrace(format);
            if (!nativeWriteShortData(sArr, i, i2)) {
                LOGGER.error("native write short data failed, offset is %{public}d, size is %{public}d", Integer.valueOf(i), Integer.valueOf(i2));
                if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                    this.state = State.STATE_BUFFER_PREPARED_ERROR;
                }
                TRACER.finishTrace(format);
                return false;
            }
            TRACER.finishTrace(format);
            if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                this.state = State.STATE_BUFFER_PREPARED;
            }
            return true;
        }
    }

    public boolean write(byte[] bArr, int i, int i2) {
        int i3;
        if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not write, state is %{public}d.", Integer.valueOf(this.state.getValue()));
            return false;
        } else if (bArr == null || i < 0 || i2 < 0 || (i3 = i + i2) < 0 || i3 > bArr.length) {
            LOGGER.error("can not write, invalid parameters", new Object[0]);
            return false;
        } else {
            String format = String.format(Locale.ROOT, "AudioRenderer_write_byte_kits:offset=%d,size=%d", Integer.valueOf(i), Integer.valueOf(i2));
            TRACER.startTrace(format);
            if (!nativeWrite(bArr, i, i2)) {
                LOGGER.error("native write failed, offset is %{public}d, size is %{public}d", Integer.valueOf(i), Integer.valueOf(i2));
                if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                    this.state = State.STATE_BUFFER_PREPARED_ERROR;
                }
                TRACER.finishTrace(format);
                return false;
            }
            TRACER.finishTrace(format);
            if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                this.state = State.STATE_BUFFER_PREPARED;
            }
            return true;
        }
    }

    public boolean write(float[] fArr, int i, int i2) {
        int i3;
        if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not write, state is %{public}d.", Integer.valueOf(this.state.getValue()));
            return false;
        } else if (fArr == null || i < 0 || i2 <= 0 || (i3 = i + i2) < 0 || i3 > fArr.length) {
            LOGGER.error("can not write, invalid parameters", new Object[0]);
            return false;
        } else if (!nativeWriteFloatData(fArr, i, i2)) {
            LOGGER.error("native write failed, offset is %{public}d, size is %{public}d", Integer.valueOf(i), Integer.valueOf(i2));
            if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                this.state = State.STATE_BUFFER_PREPARED_ERROR;
            }
            return false;
        } else {
            if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                this.state = State.STATE_BUFFER_PREPARED;
            }
            return true;
        }
    }

    public boolean write(ByteBuffer byteBuffer, int i) {
        int i2;
        if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not write, state is %{public}d.", Integer.valueOf(this.state.getValue()));
            return false;
        } else if (byteBuffer == null || i <= 0 || i > byteBuffer.remaining()) {
            LOGGER.error("can not write, invalid parameters, size = %{public}d.", Integer.valueOf(i));
            return false;
        } else {
            if (byteBuffer.isDirect()) {
                i2 = nativeWriteDirectBuffer(byteBuffer, byteBuffer.position(), i);
            } else {
                i2 = nativeWriteByteData(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), i);
            }
            if (i2 <= 0 || i2 > byteBuffer.remaining()) {
                if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                    this.state = State.STATE_BUFFER_PREPARED_ERROR;
                }
                LOGGER.error("write failed, writtenSize = %{public}d", Integer.valueOf(i2));
                return false;
            }
            byteBuffer.position(byteBuffer.position() + i2);
            if (this.playMode == PlayMode.MODE_STATIC.getModeValue()) {
                this.state = State.STATE_BUFFER_PREPARED;
            }
            return true;
        }
    }

    public boolean setVolume(float f) {
        if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not set volume, state is uninitialized.", new Object[0]);
            return false;
        } else if (!checkVolume(f)) {
            LOGGER.error("volume invalid, volume = %{public}f.", Float.valueOf(f));
            return false;
        } else {
            String format = String.format(Locale.ROOT, "AudioRenderer_setVolume_kits:vol=%f", Float.valueOf(f));
            TRACER.startTrace(format);
            boolean volume = setVolume(new ChannelVolume(f, f));
            TRACER.finishTrace(format);
            return volume;
        }
    }

    public boolean setVolume(ChannelVolume channelVolume) {
        if (channelVolume == null) {
            LOGGER.error("channelVolume can not be null.", new Object[0]);
            return false;
        } else if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not set volume, state is uninitialized.", new Object[0]);
            return false;
        } else if (!checkVolume(channelVolume.leftVolume) || !checkVolume(channelVolume.rightVolume)) {
            LOGGER.error("volume invalid, leftVolume = %{public}f, rightVolume = %{public}f.", Float.valueOf(channelVolume.leftVolume), Float.valueOf(channelVolume.rightVolume));
            return false;
        } else {
            TRACER.startTrace("AudioRenderer_setVolume_ChannelVolume_kits");
            boolean nativeSetVolume = nativeSetVolume(channelVolume);
            TRACER.finishTrace("AudioRenderer_setVolume_ChannelVolume_kits");
            return nativeSetVolume;
        }
    }

    public boolean setPosition(int i) {
        if (this.playMode != PlayMode.MODE_STATIC.getModeValue()) {
            LOGGER.error("setPlaybackHeadPosition error, called only in static mode", new Object[0]);
            return false;
        } else if (this.state == State.STATE_UNINITIALIZED || this.state == State.STATE_PLAYING) {
            LOGGER.error("setPlaybackHeadPosition error, renderer state is %{public}d", Integer.valueOf(this.state.getValue()));
            return false;
        } else if (i >= 0 && ((long) i) <= this.bufferSizeInFrames) {
            return nativeSetPlaybackHeadPosition(i);
        } else {
            LOGGER.error("setPlaybackHeadPosition error, invalid position = %{public}d, bufferSizeInFrames = %{public}d", Integer.valueOf(i), Long.valueOf(this.bufferSizeInFrames));
            return false;
        }
    }

    public boolean setSpeed(float f) {
        if (Math.abs(f - 0.0f) <= 1.0E-6f || f < 0.0f) {
            LOGGER.error("setPlaybackSpeed error, speed = %{public}f", Float.valueOf(f));
            return false;
        }
        SpeedPara.Builder builder = new SpeedPara.Builder();
        builder.speed(f);
        return setPlaybackSpeed(builder.build());
    }

    public boolean setPlaybackSpeed(SpeedPara speedPara) {
        if (speedPara == null) {
            LOGGER.error("setPlaybackSpeed error, parameter is null", new Object[0]);
            return false;
        } else if (Math.abs(speedPara.pitch - 0.0f) <= 1.0E-6f || speedPara.pitch < 0.0f || Math.abs(speedPara.speed - 0.0f) <= 1.0E-6f || speedPara.speed < 0.0f) {
            LOGGER.error("setPlaybackSpeed error, speed = %{public}f, pitch = %{public}f", Float.valueOf(speedPara.speed), Float.valueOf(speedPara.pitch));
            return false;
        } else if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not set speed, state is uninitialized.", new Object[0]);
            return false;
        } else {
            SpeedPara speedPara2 = this.playbackSpeed;
            if (speedPara2 != null && speedPara2.getSpeed() == speedPara.getSpeed() && this.playbackSpeed.getPitch() == speedPara.getPitch()) {
                LOGGER.debug("setPlaybackSpeed not change, return", new Object[0]);
                return true;
            }
            boolean nativeSetPlaybackSpeed = nativeSetPlaybackSpeed(speedPara.getSpeed(), speedPara.getPitch(), 0, 0);
            this.playbackSpeed = speedPara;
            return nativeSetPlaybackSpeed;
        }
    }

    public boolean setSampleRate(int i) {
        if (this.state == State.STATE_UNINITIALIZED) {
            LOGGER.error("can not set sample rate, state is uninitialized.", new Object[0]);
            return false;
        } else if (i <= 0) {
            LOGGER.error("can not set sample rate, sampleRate is %{public}d", Integer.valueOf(i));
            return false;
        } else if (nativeSetSampleRate(i)) {
            return true;
        } else {
            LOGGER.error("can not set sample rate, native set failed", new Object[0]);
            return false;
        }
    }

    public boolean setOutputDevice(AudioDeviceDescriptor audioDeviceDescriptor) {
        if (audioDeviceDescriptor == null) {
            LOGGER.error("setOutputDevice error, device is null.", new Object[0]);
            return false;
        } else if (audioDeviceDescriptor.getRole() == AudioDeviceDescriptor.DeviceRole.OUTPUT_DEVICE) {
            return nativeSetOutputDevice(audioDeviceDescriptor.getId());
        } else {
            LOGGER.error("setOutputDevice error, device is not output device.", new Object[0]);
            return false;
        }
    }

    public AudioDeviceDescriptor getCurrentDevice() {
        int nativeGetCurrentDeviceId = nativeGetCurrentDeviceId();
        if (nativeGetCurrentDeviceId == 0) {
            LOGGER.error("device id is 0.", new Object[0]);
            return null;
        }
        AudioDeviceDescriptor[] devices = AudioManager.getDevices(AudioDeviceDescriptor.DeviceFlag.OUTPUT_DEVICES_FLAG);
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].getId() == nativeGetCurrentDeviceId) {
                return devices[i];
            }
        }
        return null;
    }

    public int getRendererSessionId() {
        TRACER.startTrace("AudioRenderer_getRendererSessionId_kits");
        int nativeGetRendererSessionId = nativeGetRendererSessionId();
        TRACER.finishTrace("AudioRenderer_getRendererSessionId_kits");
        return nativeGetRendererSessionId;
    }

    public int getSampleRate() {
        TRACER.startTrace("AudioRenderer_getSampleRate_kits");
        int nativeGetSampleRate = nativeGetSampleRate();
        TRACER.finishTrace("AudioRenderer_getSampleRate_kits");
        return nativeGetSampleRate;
    }

    public int getPosition() {
        TRACER.startTrace("AudioRenderer_getPosition_kits");
        int nativeGetPosition = nativeGetPosition();
        TRACER.finishTrace("AudioRenderer_getPosition_kits");
        return nativeGetPosition;
    }

    public Timestamp getAudioTime() {
        long[] jArr = new long[2];
        Timestamp timestamp = new Timestamp();
        if (!nativeGetTimestamp(jArr)) {
            LOGGER.error("get time stamp failed.", new Object[0]);
            return timestamp;
        }
        timestamp.setFramePosition(jArr[0]);
        timestamp.setNanoTimestamp(jArr[1]);
        return timestamp;
    }

    public boolean flush() {
        if (this.playMode != PlayMode.MODE_STREAM.getModeValue()) {
            LOGGER.error("can not flush in mode %{public}d", Integer.valueOf(this.playMode));
            return false;
        } else if (this.state == State.STATE_STOP || this.state == State.STATE_PAUSED || this.state == State.STATE_INITIALIZED) {
            return nativeFlush();
        } else {
            LOGGER.error("can not flush in state %{public}s", this.state);
            return false;
        }
    }

    public SpeedPara getPlaybackSpeed() {
        return this.playbackSpeed;
    }

    public AudioStreamInfo.StreamType getStreamType() {
        return this.audioRendererInfo.getAudioStreamInfo().getStreamType();
    }

    public AudioRendererInfo getRendererInfo() {
        return this.audioRendererInfo;
    }

    public int getBufferFrameSize() {
        return nativeGetBufferFrameSize();
    }

    public boolean setSendEffect(int i) {
        if (this.state != State.STATE_UNINITIALIZED) {
            return nativeSetSendEffect(i);
        }
        LOGGER.info("setSendEffect: state invalid", new Object[0]);
        return false;
    }

    public AudioStreamInfo.ChannelMask getAudioChannel() {
        return this.audioRendererInfo.getAudioStreamInfo().getChannelMask();
    }

    public int getBufferUnderflowTimes() {
        return nativeGetBufferUnderflowTimes();
    }

    public void setSendEffectLevel(float f) {
        LOGGER.info("setSendEffectLevel: state invalid", new Object[0]);
        if (this.state != State.STATE_UNINITIALIZED) {
            nativeSetSendEffectLevel(f);
        }
    }

    public void setFramePositionObserver(FramePositionObserver framePositionObserver2, int i, EventHandler eventHandler) {
        EventRunner eventRunner;
        if (framePositionObserver2 == null) {
            LOGGER.error("observer is null", new Object[0]);
            return;
        }
        this.framePositionObserver = framePositionObserver2;
        if (!nativeSetFramePosition(i)) {
            LOGGER.error("set frame position in native fail", new Object[0]);
            return;
        }
        if (eventHandler != null) {
            eventRunner = eventHandler.getEventRunner();
            if (eventRunner == null) {
                LOGGER.warn("get event runner form handler fail, type to create it", new Object[0]);
                eventRunner = EventRunner.create();
            }
        } else {
            eventRunner = EventRunner.create();
        }
        if (eventRunner == null) {
            LOGGER.error("fail to get event runner", new Object[0]);
        } else {
            this.jniFramePositionEventHandler = new JniFramePositionEventHandler(eventRunner, this);
        }
    }

    public void setFrameIntervalObserver(FrameIntervalObserver frameIntervalObserver2, int i, EventHandler eventHandler) {
        EventRunner eventRunner;
        if (frameIntervalObserver2 == null) {
            LOGGER.error("observer is null", new Object[0]);
            return;
        }
        this.frameIntervalObserver = frameIntervalObserver2;
        if (!nativeSetFrameInterval(i)) {
            LOGGER.error("set frame interval in native fail", new Object[0]);
            return;
        }
        if (eventHandler != null) {
            eventRunner = eventHandler.getEventRunner();
            if (eventRunner == null) {
                LOGGER.warn("get event runner form handler fail, type to create it", new Object[0]);
                eventRunner = EventRunner.create();
            }
        } else {
            eventRunner = EventRunner.create();
        }
        if (eventRunner == null) {
            LOGGER.error("fail to get event runner", new Object[0]);
        } else {
            this.jniFrameIntervalEventHandler = new JniFrameIntervalEventHandler(eventRunner, this);
        }
    }

    private class JniFramePositionEventHandler extends EventHandler {
        private AudioRenderer audioRenderer;

        public JniFramePositionEventHandler(EventRunner eventRunner, AudioRenderer audioRenderer2) {
            super(eventRunner);
            this.audioRenderer = audioRenderer2;
        }

        @Override // ohos.eventhandler.EventHandler
        public void processEvent(InnerEvent innerEvent) {
            AudioRenderer.LOGGER.info("JniFramePositionEventHandler.processEvent called", new Object[0]);
            if (this.audioRenderer.framePositionObserver == null) {
                AudioRenderer.LOGGER.error("observer is null", new Object[0]);
            } else if (innerEvent == null) {
                AudioRenderer.LOGGER.error("event object is null", new Object[0]);
            } else if (innerEvent.eventId != 0) {
                AudioRenderer.LOGGER.error("Unexpected message %{public}d", Integer.valueOf(innerEvent.eventId));
            } else {
                this.audioRenderer.framePositionObserver.onFramePosition();
            }
        }
    }

    private class JniFrameIntervalEventHandler extends EventHandler {
        private AudioRenderer audioRenderer;

        public JniFrameIntervalEventHandler(EventRunner eventRunner, AudioRenderer audioRenderer2) {
            super(eventRunner);
            this.audioRenderer = audioRenderer2;
        }

        @Override // ohos.eventhandler.EventHandler
        public void processEvent(InnerEvent innerEvent) {
            AudioRenderer.LOGGER.info("JniFrameIntervalEventHandler.processEvent called", new Object[0]);
            if (this.audioRenderer.frameIntervalObserver == null) {
                AudioRenderer.LOGGER.error("observer is null", new Object[0]);
            } else if (innerEvent == null) {
                AudioRenderer.LOGGER.error("event object is null", new Object[0]);
            } else if (innerEvent.eventId != 1) {
                AudioRenderer.LOGGER.error("Unexpected message %{public}d", Integer.valueOf(innerEvent.eventId));
            } else {
                this.audioRenderer.frameIntervalObserver.onFrameInterval();
            }
        }
    }

    private static void postJniEventToJava(Object obj, int i, int i2, int i3, Object obj2) {
        LOGGER.debug("postJniEventToJava is called", new Object[0]);
        if (obj == null || !(obj instanceof WeakReference)) {
            LOGGER.error("nativeRef is null or not instance of WeakReference, return", new Object[0]);
            return;
        }
        AudioRenderer audioRenderer = (AudioRenderer) ((WeakReference) obj).get();
        if (audioRenderer == null) {
            LOGGER.error("audioRenderer is null, return", new Object[0]);
        } else if (i == 0) {
            JniFramePositionEventHandler jniFramePositionEventHandler2 = audioRenderer.jniFramePositionEventHandler;
            if (jniFramePositionEventHandler2 == null) {
                LOGGER.error("jniFramePositionEventHandler is not set, return", new Object[0]);
            } else {
                jniFramePositionEventHandler2.sendEvent(i);
            }
        } else if (i != 1) {
            LOGGER.error("Unexpected message %{public}d", Integer.valueOf(i));
        } else {
            JniFrameIntervalEventHandler jniFrameIntervalEventHandler2 = audioRenderer.jniFrameIntervalEventHandler;
            if (jniFrameIntervalEventHandler2 == null) {
                LOGGER.error("jniFrameIntervalEventHandler is not set, return", new Object[0]);
            } else {
                jniFrameIntervalEventHandler2.sendEvent(i);
            }
        }
    }

    public boolean duckVolume() {
        return nativeDuckVolume();
    }

    public boolean unduckVolume() {
        return nativeUnduckVolume();
    }

    private boolean checkVolume(float f) {
        if (f >= 0.0f && f <= 1.0f) {
            return true;
        }
        LOGGER.error("input volume is invalid, volume is %{public}f.", Float.valueOf(f));
        return false;
    }

    private boolean checkAndSetAudioRendererInfo(AudioRendererInfo audioRendererInfo2) {
        if (audioRendererInfo2 == null) {
            LOGGER.error("audioRendererInfo is null.", new Object[0]);
            return false;
        }
        AudioStreamInfo audioStreamInfo = audioRendererInfo2.getAudioStreamInfo();
        if (audioStreamInfo == null) {
            LOGGER.error("audioStreamInfo is null.", new Object[0]);
            return false;
        } else if (audioStreamInfo.getEncodingFormat() == AudioStreamInfo.EncodingFormat.ENCODING_INVALID) {
            LOGGER.error("encoding format is invalid, format is %{public}s.", audioStreamInfo.getEncodingFormat());
            return false;
        } else if (!checkSampleRate(audioStreamInfo)) {
            return false;
        } else {
            if (audioStreamInfo.getChannelMask() == AudioStreamInfo.ChannelMask.CHANNEL_INVALID) {
                LOGGER.error("channel mask is invalid, channel mask is %{public}s.", audioStreamInfo.getChannelMask());
                return false;
            }
            this.audioRendererInfo = audioRendererInfo2;
            return true;
        }
    }

    private boolean checkSampleRate(AudioStreamInfo audioStreamInfo) {
        if (!((audioStreamInfo.getSampleRate() < 4000 || audioStreamInfo.getSampleRate() > 192000) && audioStreamInfo.getSampleRate() != 4000)) {
            return true;
        }
        LOGGER.error("sample rate is invalid, rate is %{public}d.", Integer.valueOf(audioStreamInfo.getSampleRate()));
        return false;
    }

    private void bufferSizeCheck(AudioRendererInfo audioRendererInfo2) {
        if (audioRendererInfo2 == null) {
            LOGGER.error("bufferSizeCheck failed, can not get AudioRendererInfo", new Object[0]);
            return;
        }
        AudioStreamInfo audioStreamInfo = audioRendererInfo2.getAudioStreamInfo();
        if (audioStreamInfo == null) {
            LOGGER.error("bufferSizeCheck failed, can not get AudioStreamInfo", new Object[0]);
            return;
        }
        AudioStreamInfo.EncodingFormat encodingFormat = audioStreamInfo.getEncodingFormat();
        int channelCount = (!AudioStreamInfo.isValidEncodingFormat(encodingFormat) || encodingFormat == AudioStreamInfo.EncodingFormat.ENCODING_MP3) ? 1 : AudioStreamInfo.getChannelCount(audioStreamInfo.getChannelMask()) * AudioStreamInfo.getBytesCountForFormat(encodingFormat);
        if (channelCount == 0) {
            LOGGER.error("bufferSizeCheck failed, frame size is 0", new Object[0]);
        } else {
            this.bufferSizeInFrames = audioRendererInfo2.getBufferSizeInBytes() / ((long) channelCount);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.media.audio.AudioRenderer$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$media$audio$AudioRenderer$State = new int[State.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                ohos.media.audio.AudioRenderer$State[] r0 = ohos.media.audio.AudioRenderer.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.media.audio.AudioRenderer.AnonymousClass1.$SwitchMap$ohos$media$audio$AudioRenderer$State = r0
                int[] r0 = ohos.media.audio.AudioRenderer.AnonymousClass1.$SwitchMap$ohos$media$audio$AudioRenderer$State     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.media.audio.AudioRenderer$State r1 = ohos.media.audio.AudioRenderer.State.STATE_INITIALIZED     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.media.audio.AudioRenderer.AnonymousClass1.$SwitchMap$ohos$media$audio$AudioRenderer$State     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.media.audio.AudioRenderer$State r1 = ohos.media.audio.AudioRenderer.State.STATE_PLAYING     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.media.audio.AudioRenderer.AnonymousClass1.$SwitchMap$ohos$media$audio$AudioRenderer$State     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.media.audio.AudioRenderer$State r1 = ohos.media.audio.AudioRenderer.State.STATE_PAUSED     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.media.audio.AudioRenderer.AnonymousClass1.$SwitchMap$ohos$media$audio$AudioRenderer$State     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.media.audio.AudioRenderer$State r1 = ohos.media.audio.AudioRenderer.State.STATE_STOP     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = ohos.media.audio.AudioRenderer.AnonymousClass1.$SwitchMap$ohos$media$audio$AudioRenderer$State     // Catch:{ NoSuchFieldError -> 0x0040 }
                ohos.media.audio.AudioRenderer$State r1 = ohos.media.audio.AudioRenderer.State.STATE_BUFFER_PREPARED     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.media.audio.AudioRenderer.AnonymousClass1.<clinit>():void");
        }
    }

    private int convertBaseState(State state2) {
        int i = AnonymousClass1.$SwitchMap$ohos$media$audio$AudioRenderer$State[state2.ordinal()];
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    i2 = 4;
                    if (i == 4 || i == 5) {
                        return i2;
                    }
                    return -1;
                }
            }
        }
        return i2;
    }

    public enum PlayMode {
        MODE_STATIC(0),
        MODE_STREAM(1);
        
        private final int playModeValue;

        private PlayMode(int i) {
            this.playModeValue = i;
        }

        public int getModeValue() {
            return this.playModeValue;
        }
    }

    public static class ChannelVolume {
        public static final float VOLUME_MAX = 1.0f;
        public static final float VOLUME_MIN = 0.0f;
        private final float leftVolume;
        private final float rightVolume;

        public ChannelVolume(float f, float f2) {
            this.leftVolume = f;
            this.rightVolume = f2;
        }
    }

    public static class SpeedPara {
        private float pitch;
        private float speed;

        /* synthetic */ SpeedPara(float f, float f2, AnonymousClass1 r3) {
            this(f, f2);
        }

        private SpeedPara(float f, float f2) {
            this.speed = 1.0f;
            this.pitch = 1.0f;
            this.speed = f;
            this.pitch = f2;
        }

        public float getSpeed() {
            return this.speed;
        }

        public float getPitch() {
            return this.pitch;
        }

        public static class Builder {
            private float pitch = 1.0f;
            private float speed = 1.0f;

            public Builder speed(float f) {
                this.speed = f;
                return this;
            }

            public Builder pitch(float f) {
                this.pitch = f;
                return this;
            }

            public SpeedPara build() {
                return new SpeedPara(this.speed, this.pitch, null);
            }
        }
    }
}
