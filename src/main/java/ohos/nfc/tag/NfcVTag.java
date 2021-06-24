package ohos.nfc.tag;

import java.util.Optional;
import ohos.interwork.utils.PacMapEx;

public class NfcVTag extends TagManager {
    private static final String EXTRA_DSFID = "dsfid";
    private static final String EXTRA_RESP_FLAGS = "respflags";
    private byte mDsfId;
    private byte mResponseFlags;

    public static Optional<NfcVTag> getInstance(TagInfo tagInfo) {
        if (!tagInfo.isProfileSupported(4)) {
            return Optional.empty();
        }
        return Optional.of(new NfcVTag(tagInfo));
    }

    public NfcVTag(TagInfo tagInfo) {
        super(tagInfo, 5);
        PacMapEx orElse = tagInfo.getProfileExtras(5).orElse(null);
        if (orElse != null) {
            if (orElse.getObjectValue(EXTRA_RESP_FLAGS).get() instanceof Byte) {
                this.mResponseFlags = ((Byte) orElse.getObjectValue(EXTRA_RESP_FLAGS).get()).byteValue();
            }
            if (orElse.getObjectValue(EXTRA_DSFID).get() instanceof Byte) {
                this.mDsfId = ((Byte) orElse.getObjectValue(EXTRA_DSFID).get()).byteValue();
            }
        }
    }

    public byte getResponseFlags() {
        return this.mResponseFlags;
    }

    public byte getDsfId() {
        return this.mDsfId;
    }
}
