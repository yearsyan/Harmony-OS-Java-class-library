package ohos.media.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class SoundEffect {
    private static final Logger LOGGER = LoggerFactory.getAudioLogger(SoundEffect.class);
    public static final String SOUND_EFFECT_MODE_AUXILIARY = "Auxiliary";
    public static final String SOUND_EFFECT_MODE_INSERT = "Insert";
    public static final String SOUND_EFFECT_MODE_PRE_PROCESSING = "Pre Processing";
    public static final UUID SOUND_EFFECT_TYPE_AE = UUID.fromString("0bed4300-ddd6-11db-8f34-0002a5d5c51b");
    public static final UUID SOUND_EFFECT_TYPE_EC = UUID.fromString("7b491460-8d4d-11e0-bd61-0002a5d5c51b");
    public static final UUID SOUND_EFFECT_TYPE_GC = UUID.fromString("0a8abfe0-654c-11e0-ba26-0002a5d5c51b");
    public static final UUID SOUND_EFFECT_TYPE_INVALID = UUID.fromString("ec7178ec-e5e1-4432-a3f4-4657e6795210");
    public static final UUID SOUND_EFFECT_TYPE_NS = UUID.fromString("58b4b260-8e06-11e0-aa8e-0002a5d5c51b");
    private final int effectId;
    private final SoundEffectInfo effectInfo;
    private long nativeSoundEffect;

    private static native Object[] nativeAcquireEffects();

    private native boolean nativeGetActivated();

    private native boolean nativeGetPara(int i, byte[] bArr, int i2, byte[] bArr2);

    private native boolean nativeRelease();

    private native boolean nativeSetActivated(boolean z);

    private native boolean nativeSetPara(int i, byte[] bArr, int i2, byte[] bArr2);

    private native int nativeSetup(String str, String str2, int i, int i2, int[] iArr, Object[] objArr, String str3);

    static {
        System.loadLibrary("soundeffect_jni.z");
    }

    public SoundEffect(UUID uuid, UUID uuid2, int i, int i2, String str) throws UnsupportedOperationException {
        int[] iArr = new int[1];
        Object[] objArr = new SoundEffectInfo[1];
        if (nativeSetup(uuid.toString(), uuid2.toString(), i, i2, iArr, objArr, str) == 0) {
            this.effectId = iArr[0];
            this.effectInfo = objArr[0];
            return;
        }
        throw new UnsupportedOperationException("Sound Effect setup failed.");
    }

    public static boolean isEffectAvailable(UUID uuid) {
        for (SoundEffectInfo soundEffectInfo : acquireEffects()) {
            if (soundEffectInfo.getType().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static SoundEffectInfo[] acquireEffects() {
        Object[] nativeAcquireEffects = nativeAcquireEffects();
        if (nativeAcquireEffects == null) {
            return new SoundEffectInfo[0];
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : nativeAcquireEffects) {
            if (obj instanceof SoundEffectInfo) {
                arrayList.add((SoundEffectInfo) obj);
            }
        }
        return (SoundEffectInfo[]) arrayList.stream().toArray($$Lambda$SoundEffect$SqyMKeNZ7ZHPpvBiWE6MotY9NTw.INSTANCE);
    }

    static /* synthetic */ SoundEffectInfo[] lambda$acquireEffects$0(int i) {
        return new SoundEffectInfo[i];
    }

    public SoundEffectInfo getEffectInfo() {
        return this.effectInfo;
    }

    public int getEffectId() {
        return this.effectId;
    }

    public boolean setActivated(boolean z) {
        return nativeSetActivated(z);
    }

    public boolean getActivated() {
        return nativeGetActivated();
    }

    public boolean release() {
        return nativeRelease();
    }

    public boolean setPara(int[] iArr, short[] sArr) {
        if (iArr.length > 2 || sArr.length > 2) {
            LOGGER.error("setPara: invalid input.", new Object[0]);
            return false;
        }
        byte[] convertIntToByteArray = convertIntToByteArray(iArr[0]);
        if (iArr.length > 1) {
            convertIntToByteArray = joinByteArrays(convertIntToByteArray, convertIntToByteArray(iArr[1]));
        }
        byte[] convertShortToByteArray = convertShortToByteArray(sArr[0]);
        if (sArr.length > 1) {
            convertShortToByteArray = joinByteArrays(convertShortToByteArray, convertShortToByteArray(sArr[1]));
        }
        return nativeSetPara(convertIntToByteArray.length, convertIntToByteArray, convertShortToByteArray.length, convertShortToByteArray);
    }

    public boolean getPara(int[] iArr, short[] sArr) {
        if (iArr.length > 2 || sArr.length > 2) {
            LOGGER.error("getPara: invalid input.", new Object[0]);
            return false;
        }
        byte[] convertIntToByteArray = convertIntToByteArray(iArr[0]);
        if (iArr.length > 1) {
            convertIntToByteArray = joinByteArrays(convertIntToByteArray, convertIntToByteArray(iArr[1]));
        }
        byte[] bArr = new byte[(sArr.length * 2)];
        return nativeGetPara(convertIntToByteArray.length, convertIntToByteArray, bArr.length, bArr);
    }

    private static byte[] convertIntToByteArray(int i) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.order(ByteOrder.nativeOrder());
        allocate.putInt(i);
        return allocate.array();
    }

    private static byte[] joinByteArrays(byte[]... bArr) {
        int i = 0;
        for (byte[] bArr2 : bArr) {
            i += bArr2.length;
        }
        byte[] bArr3 = new byte[i];
        int i2 = 0;
        for (byte[] bArr4 : bArr) {
            System.arraycopy(bArr4, 0, bArr3, i2, bArr4.length);
            i2 += bArr4.length;
        }
        return bArr3;
    }

    private static byte[] convertShortToByteArray(short s) {
        ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.order(ByteOrder.nativeOrder());
        allocate.putShort(s);
        return allocate.array();
    }

    public static class SoundEffectInfo {
        private String mode;
        private String name;
        private UUID type;
        private UUID uid;

        public SoundEffectInfo() {
        }

        public SoundEffectInfo(String str, String str2, String str3, String str4) {
            this.uid = UUID.fromString(str);
            this.type = UUID.fromString(str2);
            this.mode = str3;
            this.name = str4;
        }

        public UUID getUid() {
            return this.uid;
        }

        public void setUid(UUID uuid) {
            this.uid = uuid;
        }

        public UUID getType() {
            return this.type;
        }

        public void setType(UUID uuid) {
            this.type = uuid;
        }

        public String getMode() {
            return this.mode;
        }

        public void setMode(String str) {
            this.mode = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SoundEffectInfo)) {
                return false;
            }
            SoundEffectInfo soundEffectInfo = (SoundEffectInfo) obj;
            return this.uid.equals(soundEffectInfo.getUid()) && this.type.equals(soundEffectInfo.getType()) && this.mode.equals(soundEffectInfo.getMode()) && this.name.equals(soundEffectInfo.getName());
        }

        public int hashCode() {
            return Objects.hash(this.uid, this.type, this.mode, this.name);
        }
    }
}
