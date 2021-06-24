package ohos.nfc.tag;

import java.util.Arrays;
import ohos.interwork.utils.PacMapEx;

public class NfcATag extends TagManager {
    private static final int ATQA_LENGTH = 2;
    public static final String EXTRA_ATQA = "atqa";
    public static final String EXTRA_SAK = "sak";
    private byte[] mAtqa;
    private short mSak;

    public static NfcATag getInstance(TagInfo tagInfo) {
        if (tagInfo == null) {
            throw new NullPointerException("NfcATag tagInfo is null");
        } else if (!tagInfo.isProfileSupported(1)) {
            return null;
        } else {
            return new NfcATag(tagInfo);
        }
    }

    private NfcATag(TagInfo tagInfo) {
        super(tagInfo, 1);
        PacMapEx orElse;
        PacMapEx orElse2;
        this.mSak = 0;
        this.mAtqa = new byte[2];
        this.mSak = 0;
        if (tagInfo.isProfileSupported(8) && (orElse2 = tagInfo.getProfileExtras(8).orElse(null)) != null) {
            this.mSak = ((Short) orElse2.getObjectValue(EXTRA_SAK).get()).shortValue();
        }
        if (tagInfo.isProfileSupported(1) && (orElse = tagInfo.getProfileExtras(1).orElse(null)) != null) {
            this.mSak = (short) (((Short) orElse.getObjectValue(EXTRA_SAK).get()).shortValue() | this.mSak);
            if (orElse.getObjectValue(EXTRA_ATQA).get() instanceof byte[]) {
                this.mAtqa = (byte[]) orElse.getObjectValue(EXTRA_ATQA).get();
            }
        }
    }

    public short getSak() {
        return this.mSak;
    }

    public byte[] getAtqa() {
        byte[] bArr = this.mAtqa;
        return bArr != null ? Arrays.copyOf(bArr, bArr.length) : new byte[0];
    }
}
