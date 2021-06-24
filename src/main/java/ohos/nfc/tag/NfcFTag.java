package ohos.nfc.tag;

import java.util.Optional;
import ohos.interwork.utils.PacMapEx;

public class NfcFTag extends TagManager {
    private static final String EXTRA_PMM = "pmm";
    private static final String EXTRA_SC = "systemcode";
    private byte[] mPMm = null;
    private byte[] mSystemCode = null;

    public static Optional<NfcFTag> getInstance(TagInfo tagInfo) {
        if (!tagInfo.isProfileSupported(4)) {
            return Optional.empty();
        }
        return Optional.of(new NfcFTag(tagInfo));
    }

    public NfcFTag(TagInfo tagInfo) {
        super(tagInfo, 4);
        PacMapEx orElse;
        if (tagInfo.isProfileSupported(4) && (orElse = tagInfo.getProfileExtras(4).orElse(null)) != null) {
            if (orElse.getObjectValue(EXTRA_SC).get() instanceof byte[]) {
                this.mSystemCode = (byte[]) orElse.getObjectValue(EXTRA_SC).get();
            }
            if (orElse.getObjectValue(EXTRA_PMM).get() instanceof byte[]) {
                this.mPMm = (byte[]) orElse.getObjectValue(EXTRA_PMM).get();
            }
        }
    }

    public byte[] getSystemCode() {
        return this.mSystemCode;
    }

    public byte[] getPMm() {
        return this.mPMm;
    }
}
