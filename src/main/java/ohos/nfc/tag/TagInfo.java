package ohos.nfc.tag;

import java.util.Arrays;
import ohos.interwork.utils.PacMapEx;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class TagInfo implements Sequenceable {
    public static final String EXTRA_TAG_EXTRAS = "extra_nfc_TAG_EXTRAS";
    public static final String EXTRA_TAG_HANDLE = "extra_nfc_TAG_HANDLE";
    private static int LIMIT_FOR_DATA = 1024;
    private PacMapEx[] mProfileExtras = null;
    private int mTagHandle;
    private byte[] mTagId = null;
    private int[] mTagSupportedProfiles = null;

    public TagInfo(byte[] bArr, int[] iArr, PacMapEx[] pacMapExArr, int i) {
        if (bArr != null && bArr.length < LIMIT_FOR_DATA) {
            this.mTagId = Arrays.copyOf(bArr, bArr.length);
        }
        if (iArr != null && iArr.length < LIMIT_FOR_DATA) {
            this.mTagSupportedProfiles = Arrays.copyOf(iArr, iArr.length);
        }
        if (pacMapExArr != null && pacMapExArr.length < LIMIT_FOR_DATA) {
            this.mProfileExtras = (PacMapEx[]) Arrays.copyOf(pacMapExArr, pacMapExArr.length);
        }
        this.mTagHandle = i;
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        parcel.writeByteArray(this.mTagId);
        parcel.writeIntArray(this.mTagSupportedProfiles);
        parcel.writeSequenceableArray(this.mProfileExtras);
        parcel.writeInt(this.mTagHandle);
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        int readInt = parcel.readInt();
        this.mProfileExtras = new PacMapEx[readInt];
        for (int i = 0; i < readInt; i++) {
            this.mProfileExtras[i] = new PacMapEx();
            parcel.readPacMapEx(this.mProfileExtras[i]);
        }
        this.mTagHandle = parcel.readInt();
        int readInt2 = parcel.readInt();
        this.mTagId = new byte[readInt2];
        for (int i2 = 0; i2 < readInt2; i2++) {
            this.mTagId[i2] = parcel.readByte();
        }
        int readInt3 = parcel.readInt();
        this.mTagSupportedProfiles = new int[readInt3];
        for (int i3 = 0; i3 < readInt3; i3++) {
            this.mTagSupportedProfiles[i3] = parcel.readInt();
        }
        return true;
    }

    public byte[] getTagId() {
        byte[] bArr = this.mTagId;
        return bArr != null ? Arrays.copyOf(bArr, bArr.length) : new byte[0];
    }

    public int[] getTagSupportedProfiles() {
        int[] iArr = this.mTagSupportedProfiles;
        return iArr != null ? Arrays.copyOf(iArr, iArr.length) : new int[0];
    }

    public int getTagHandle() {
        return this.mTagHandle;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0015  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x001a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<ohos.interwork.utils.PacMapEx> getProfileExtras(int r4) {
        /*
            r3 = this;
            int[] r0 = r3.mTagSupportedProfiles
            if (r0 == 0) goto L_0x0012
            r0 = 0
        L_0x0005:
            int[] r1 = r3.mTagSupportedProfiles
            int r2 = r1.length
            if (r0 >= r2) goto L_0x0012
            r1 = r1[r0]
            if (r1 != r4) goto L_0x000f
            goto L_0x0013
        L_0x000f:
            int r0 = r0 + 1
            goto L_0x0005
        L_0x0012:
            r0 = -1
        L_0x0013:
            if (r0 >= 0) goto L_0x001a
            java.util.Optional r3 = java.util.Optional.empty()
            return r3
        L_0x001a:
            ohos.interwork.utils.PacMapEx[] r3 = r3.mProfileExtras
            r3 = r3[r0]
            java.util.Optional r3 = java.util.Optional.of(r3)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.nfc.tag.TagInfo.getProfileExtras(int):java.util.Optional");
    }

    public boolean isProfileSupported(int i) {
        int[] iArr = this.mTagSupportedProfiles;
        if (iArr != null) {
            for (int i2 : iArr) {
                if (i2 == i) {
                    return true;
                }
            }
        }
        return false;
    }
}
