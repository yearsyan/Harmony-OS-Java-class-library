package ohos.media.common;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Format {
    public static final String AAC_PROFILE = "aac-profile";
    public static final String AUDIO_AAC = "audio/mp4a-latm";
    public static final String AUDIO_AC3 = "audio/ac3";
    public static final String AUDIO_AMR_NB = "audio/3gpp";
    public static final String AUDIO_AMR_WB = "audio/amr-wb";
    public static final String AUDIO_EAC3 = "audio/eac3";
    public static final String AUDIO_FLAC = "audio/flac";
    public static final String AUDIO_G711_ALAW = "audio/g711-alaw";
    public static final String AUDIO_G711_MLAW = "audio/g711-mlaw";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_OPUS = "audio/opus";
    public static final String AUDIO_RAW = "audio/raw";
    public static final String AUDIO_VORBIS = "audio/vorbis";
    public static final String BITRATE_MODE = "bitrate-mode";
    public static final String BIT_RATE = "bitrate";
    public static final String CAPTURE_RATE = "capture-rate";
    public static final String CHANNEL = "channel-count";
    public static final String CODEC_COMPLEXITY = "complexity";
    public static final String CODEC_FEATURE = "feature-";
    public static final String CODEC_FLAC_COMPRESSION_LEVEL = "flac-compression-level";
    public static final String CODEC_LEVEL = "level";
    public static final String CODEC_PROFILE = "profile";
    public static final String CODEC_QUALITY = "quality";
    public static final String COLOR_MODEL = "color-format";
    public static final String DURATION = "durationUs";
    private static final double ERROR_DOUBLE_VALUE = -1.0d;
    private static final float ERROR_FLOAT_VALUE = -1.0f;
    private static final int ERROR_INT_VALUE = -1;
    private static final long ERROR_LONG_VALUE = -1;
    public static final String FRAME_INTERVAL = "i-frame-interval";
    public static final String FRAME_RATE = "frame-rate";
    public static final String HEIGHT = "height";
    public static final String IMAGE_AOSP_HEIC = "image/vnd.android.heic";
    public static final String IS_AUTOSELECT = "is-autoselect";
    public static final String IS_DEFAULT = "is-default";
    public static final String IS_FORCED_SUBTITLE = "is-forced-subtitle";
    public static final String KEY_BIT_RATE_SCOPE = "bitrate.scope";
    public static final String KEY_CODEC_COMPLEXITY_SCOPE = "complexity.scope";
    public static final String KEY_CODEC_QUALITY_SCOPE = "quality.scope";
    public static final String KEY_FRAME_RATE_SCOPE = "frame-rate.scope";
    public static final String KEY_HEIGHT_SCOPE = "height.scope";
    public static final String KEY_SAMPLE_RATE_LIST = "sample-rate.list";
    public static final String KEY_SAMPLE_RATE_SCOPE = "sample-rate.scope";
    public static final String KEY_WIDTH_SCOPE = "width.scope";
    public static final String LANGUAGE = "language";
    public static final String MAX_BIT_RATE = "max-bitrate";
    public static final String MAX_HEIGHT = "max-height";
    public static final String MAX_INPUT_SIZE = "max-input-size";
    public static final String MAX_WIDTH = "max-width";
    public static final String MIME = "mime";
    public static final String MIME_AUDIO_MSGSM = "audio/gsm";
    public static final String MIME_VIDEO_AV1 = "video/av01";
    public static final String SAMPLE_RATE = "sample-rate";
    private static final List<String> SUPPORTED_AUDIO_KEYS;
    private static final List<String> SUPPORTED_ENCODER_KEYS;
    private static final List<String> SUPPORTED_VIDEO_KEYS;
    public static final String VIDEO_AVC = "video/avc";
    public static final String VIDEO_DOLBY_VISION = "video/dolby-vision";
    public static final String VIDEO_H263 = "video/3gpp";
    public static final String VIDEO_HEVC = "video/hevc";
    public static final String VIDEO_MPEG2 = "video/mpeg2";
    public static final String VIDEO_MPEG4 = "video/mp4v-es";
    public static final String VIDEO_RAW = "video/raw";
    public static final String VIDEO_VP8 = "video/x-vnd.on2.vp8";
    public static final String VIDEO_VP9 = "video/x-vnd.on2.vp9";
    public static final String WIDTH = "width";
    private HashMap<String, Object> formatMap;

    public static class FormatArrays {
        public String[] keys = null;
        public Object[] values = null;
    }

    public Format() {
        this.formatMap = null;
        this.formatMap = new HashMap<>();
    }

    public Format(HashMap<String, Object> hashMap) {
        this.formatMap = null;
        if (hashMap == null) {
            this.formatMap = new HashMap<>();
        } else {
            this.formatMap = hashMap;
        }
    }

    public Format(Map<String, Object> map) {
        this.formatMap = null;
        if (map == null) {
            this.formatMap = new HashMap<>();
            return;
        }
        if (this.formatMap == null) {
            this.formatMap = new HashMap<>();
        }
        this.formatMap.putAll(map);
    }

    static {
        ArrayList arrayList = new ArrayList();
        arrayList.add(MIME);
        arrayList.add(CHANNEL);
        arrayList.add(SAMPLE_RATE);
        SUPPORTED_AUDIO_KEYS = Collections.unmodifiableList(arrayList);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(MIME);
        arrayList2.add("width");
        arrayList2.add("height");
        arrayList2.add(BIT_RATE);
        arrayList2.add(CODEC_QUALITY);
        SUPPORTED_VIDEO_KEYS = Collections.unmodifiableList(arrayList2);
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(MIME);
        arrayList3.add(FRAME_INTERVAL);
        SUPPORTED_ENCODER_KEYS = Collections.unmodifiableList(arrayList3);
    }

    public static List<String> getSupportedAudioProperties() {
        return SUPPORTED_AUDIO_KEYS;
    }

    public static List<String> getSupportedVideoProperties() {
        return SUPPORTED_VIDEO_KEYS;
    }

    public static List<String> getSupportedEncoderProperties() {
        return SUPPORTED_ENCODER_KEYS;
    }

    public HashMap<String, Object> getFormatMap() {
        return this.formatMap;
    }

    public void putIntValue(String str, int i) {
        this.formatMap.put(str, Integer.valueOf(i));
    }

    public void putLongValue(String str, long j) {
        this.formatMap.put(str, Long.valueOf(j));
    }

    public void putFloatValue(String str, float f) {
        this.formatMap.put(str, Float.valueOf(f));
    }

    public void putDoubleValue(String str, double d) {
        this.formatMap.put(str, Double.valueOf(d));
    }

    public void putStringValue(String str, String str2) {
        this.formatMap.put(str, str2);
    }

    public void setObjectFormat(String str, Object obj) {
        this.formatMap.put(str, obj);
    }

    public void putObjectValue(String str, Object obj) {
        this.formatMap.put(str, obj);
    }

    public boolean hasKey(String str) {
        HashMap<String, Object> hashMap = this.formatMap;
        if (hashMap == null) {
            return false;
        }
        return hashMap.containsKey(str);
    }

    public int getIntValue(String str) {
        HashMap<String, Object> hashMap = this.formatMap;
        if (hashMap == null) {
            return -1;
        }
        return ((Integer) hashMap.get(str)).intValue();
    }

    public long getLongValue(String str) {
        HashMap<String, Object> hashMap = this.formatMap;
        if (hashMap == null) {
            return -1;
        }
        return ((Long) hashMap.get(str)).longValue();
    }

    public float getFloatValue(String str) {
        HashMap<String, Object> hashMap = this.formatMap;
        if (hashMap == null) {
            return -1.0f;
        }
        return ((Float) hashMap.get(str)).floatValue();
    }

    public double getDoubleValue(String str) {
        HashMap<String, Object> hashMap = this.formatMap;
        if (hashMap == null) {
            return ERROR_DOUBLE_VALUE;
        }
        return ((Double) hashMap.get(str)).doubleValue();
    }

    public String getStringValue(String str) {
        HashMap<String, Object> hashMap = this.formatMap;
        if (hashMap == null) {
            return "";
        }
        return (String) hashMap.get(str);
    }

    public Object getObjectValue(String str) {
        HashMap<String, Object> hashMap = this.formatMap;
        if (hashMap == null) {
            return null;
        }
        return hashMap.get(str);
    }

    public FormatArrays getFormatArrays() {
        FormatArrays formatArrays = new FormatArrays();
        Object[] array = this.formatMap.keySet().toArray();
        formatArrays.keys = (String[]) Arrays.copyOf(array, array.length, String[].class);
        formatArrays.values = this.formatMap.values().toArray();
        for (int i = 0; i < formatArrays.values.length; i++) {
            if (formatArrays.values[i] instanceof byte[]) {
                formatArrays.values[i] = ByteBuffer.wrap((byte[]) formatArrays.values[i]);
            }
        }
        return formatArrays;
    }

    public String toString() {
        return this.formatMap.toString();
    }

    public static final class Key<T> {
        private final String name;
        private final Class<T> type;

        public Key(String str, Class<T> cls) {
            this.name = str;
            this.type = cls;
        }

        public String getName() {
            return this.name;
        }

        public boolean checkType(Class<?> cls) {
            return this.type.equals(cls);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            if (!this.name.equals(key.name) || !this.type.equals(key.type)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.name, this.type);
        }

        public String toString() {
            return String.format(Locale.ENGLISH, "format key:%s, type:%s", this.name, this.type);
        }
    }
}
