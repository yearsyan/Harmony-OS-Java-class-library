package ohos.nfc.tag;

import java.util.Arrays;
import java.util.Optional;
import ohos.interwork.utils.PacMapEx;

public class IsoDepTag extends TagManager {
    public static final String EXTRA_HILAYER_RESP = "hiresp";
    public static final String EXTRA_HIST_BYTES = "histbytes";
    private byte[] mHiLayerResponse = null;
    private byte[] mHistBytes = null;

    public static Optional<IsoDepTag> getInstance(TagInfo tagInfo) {
        if (!tagInfo.isProfileSupported(3)) {
            return Optional.empty();
        }
        return Optional.of(new IsoDepTag(tagInfo));
    }

    public IsoDepTag(TagInfo tagInfo) {
        super(tagInfo, 3);
        PacMapEx orElse;
        if (tagInfo.isProfileSupported(3) && (orElse = tagInfo.getProfileExtras(3).orElse(null)) != null) {
            if (orElse.getObjectValue(EXTRA_HILAYER_RESP).get() instanceof byte[]) {
                this.mHiLayerResponse = (byte[]) orElse.getObjectValue(EXTRA_HILAYER_RESP).get();
            }
            if (orElse.getObjectValue(EXTRA_HIST_BYTES).get() instanceof byte[]) {
                this.mHistBytes = (byte[]) orElse.getObjectValue(EXTRA_HIST_BYTES).get();
            }
        }
    }

    public byte[] getHistoricalBytes() {
        byte[] bArr = this.mHistBytes;
        return bArr != null ? Arrays.copyOf(bArr, bArr.length) : new byte[0];
    }

    public byte[] getHiLayerResponse() {
        byte[] bArr = this.mHiLayerResponse;
        return bArr != null ? Arrays.copyOf(bArr, bArr.length) : new byte[0];
    }
}
