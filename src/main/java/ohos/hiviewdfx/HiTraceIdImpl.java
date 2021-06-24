package ohos.hiviewdfx;

import java.util.Locale;

public final class HiTraceIdImpl {
    private static final int BITS_IN_ONE_BYTE = 8;
    private static final byte HITRACE_ID_VALID = 1;
    private static final byte HITRACE_VER_1 = 0;
    private static final int TRACEID_CHAINID_FROM_BIT = 0;
    private static final int TRACEID_CHAINID_TO_BIT = 59;
    private static final int TRACEID_FLAG_FROM_BIT = 116;
    private static final int TRACEID_FLAG_TO_BIT = 127;
    private static final int TRACEID_PARENTSPANID_FROM_BIT = 64;
    private static final int TRACEID_PARENTSPANID_TO_BIT = 89;
    private static final int TRACEID_SPANID_FROM_BIT = 90;
    private static final int TRACEID_SPANID_TO_BIT = 115;
    private static final int TRACEID_VALID_FROM_BIT = 63;
    private static final int TRACEID_VALID_TO_BIT = 63;
    private static final int TRACEID_VERSION_FROM_BIT = 60;
    private static final int TRACEID_VERSION_TO_BIT = 62;
    private byte[] idArray;

    private int getHighBitMask(int i) {
        int i2 = 128;
        byte b = 0;
        for (int i3 = 0; i3 < i; i3++) {
            b = (byte) (b | i2);
            i2 >>>= 1;
        }
        return b;
    }

    private int getLowBitMask(int i) {
        byte b = 0;
        int i2 = 1;
        for (int i3 = 0; i3 < i; i3++) {
            b = (byte) (b | i2);
            i2 <<= 1;
        }
        return b;
    }

    public HiTraceIdImpl(byte[] bArr) {
        initHiTraceId(bArr);
    }

    private void initHiTraceId(byte[] bArr) {
        this.idArray = new byte[16];
        if (bArr != null && bArr.length == 16) {
            System.arraycopy(bArr, 0, this.idArray, 0, 16);
        }
    }

    private boolean isValidIdArray() {
        byte[] bArr = this.idArray;
        return bArr != null && bArr.length == 16;
    }

    public boolean isValid() {
        if (isValidIdArray() && (getValueFromByte(63, 63) & 1) == 1) {
            return true;
        }
        return false;
    }

    private void setValid() {
        if (isValidIdArray()) {
            setValueToByte(1, 63, 63);
        }
    }

    private long getValueFromOneBit(int i) {
        return (long) (this.idArray[i >>> 3] & (1 << (8 - ((i % 8) + 1))));
    }

    private long getValueFromOneByte(int i, int i2) {
        int i3 = 8 - ((i2 % 8) + 1);
        return ((long) (this.idArray[i >>> 3] & (getLowBitMask((i2 - i) + 1) << i3))) >>> i3;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0029  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long getValueFromByte(int r8, int r9) {
        /*
            r7 = this;
            int r0 = r8 >>> 3
            int r1 = r9 >>> 3
            if (r8 != r9) goto L_0x000b
            long r7 = r7.getValueFromOneBit(r8)
            return r7
        L_0x000b:
            if (r0 != r1) goto L_0x0012
            long r7 = r7.getValueFromOneByte(r8, r9)
            return r7
        L_0x0012:
            r2 = 8
            int r8 = r8 % r2
            int r9 = r9 % r2
            if (r8 == 0) goto L_0x0025
            int r8 = 8 - r8
            int r8 = r7.getLowBitMask(r8)
            byte[] r3 = r7.idArray
            byte r3 = r3[r0]
            r8 = r8 & r3
            long r3 = (long) r8
            goto L_0x0032
        L_0x0025:
            r3 = 0
        L_0x0027:
            if (r0 >= r1) goto L_0x0035
            long r3 = r3 << r2
            byte[] r8 = r7.idArray
            byte r8 = r8[r0]
            r8 = r8 & 255(0xff, float:3.57E-43)
            long r5 = (long) r8
            long r3 = r3 | r5
        L_0x0032:
            int r0 = r0 + 1
            goto L_0x0027
        L_0x0035:
            r8 = 7
            if (r9 != r8) goto L_0x0044
            long r8 = r3 << r2
            byte[] r7 = r7.idArray
            byte r7 = r7[r1]
            r7 = r7 & 255(0xff, float:3.57E-43)
            long r0 = (long) r7
            long r7 = r8 | r0
            return r7
        L_0x0044:
            int r9 = r9 + 1
            int r8 = r7.getHighBitMask(r9)
            long r3 = r3 << r9
            byte[] r7 = r7.idArray
            byte r7 = r7[r1]
            r7 = r7 & r8
            r7 = r7 & 255(0xff, float:3.57E-43)
            int r2 = r2 - r9
            int r7 = r7 >>> r2
            long r7 = (long) r7
            long r7 = r7 | r3
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.hiviewdfx.HiTraceIdImpl.getValueFromByte(int, int):long");
    }

    private void setValueToOneBit(long j, int i) {
        int i2 = i >>> 3;
        byte[] bArr = this.idArray;
        bArr[i2] = (byte) ((((int) (j & 1)) << (8 - ((i % 8) + 1))) | bArr[i2]);
    }

    private void setValueToOneByte(long j, int i, int i2) {
        int i3 = i >>> 3;
        byte[] bArr = this.idArray;
        bArr[i3] = (byte) ((((int) (j & ((long) getLowBitMask((i2 - i) + 1)))) << (8 - ((i2 % 8) + 1))) | bArr[i3]);
    }

    private void setValueToByte(long j, int i, int i2) {
        int i3 = i >>> 3;
        int i4 = i2 >>> 3;
        if (i == i2) {
            setValueToOneBit(j, i);
        } else if (i3 == i4) {
            setValueToOneByte(j, i, i2);
        } else {
            int i5 = i2 % 8;
            if (i5 != 7) {
                int i6 = i5 + 1;
                byte[] bArr = this.idArray;
                bArr[i4] = (byte) (bArr[i4] & (~getHighBitMask(i6)));
                byte[] bArr2 = this.idArray;
                bArr2[i4] = (byte) (((byte) (((byte) ((int) (((long) getLowBitMask(i6)) & j))) << (8 - i6))) | bArr2[i4]);
                i4--;
                j >>>= i6;
            }
            while (i4 > i3) {
                j >>>= 8;
                this.idArray[i4] = (byte) ((int) (255 & j));
                i4--;
            }
            int i7 = i % 8;
            if (i7 == 0) {
                this.idArray[i3] = (byte) ((int) j);
                return;
            }
            int lowBitMask = getLowBitMask(8 - i7);
            byte[] bArr3 = this.idArray;
            bArr3[i3] = (byte) (bArr3[i3] & (~lowBitMask));
            bArr3[i3] = (byte) ((int) ((j & ((long) lowBitMask)) | ((long) bArr3[i3])));
        }
    }

    private void setVersion(byte b) {
        if (isValid()) {
            setValueToByte((long) b, 60, 62);
        }
    }

    private byte getVersion() {
        return (byte) ((int) getValueFromByte(60, 62));
    }

    public boolean isFlagEnabled(int i) {
        if (isValid() && i >= 0 && i < 127 && (getFlags() & i) != 0) {
            return true;
        }
        return false;
    }

    public void enableFlag(int i) {
        if (isValid() && i >= 0 && i < 127) {
            setFlags(i | getFlags());
        }
    }

    public int getFlags() {
        if (!isValid()) {
            return 0;
        }
        return (int) getValueFromByte(116, 127);
    }

    public void setFlags(int i) {
        if (isValid() && i >= 0 && i < 127) {
            setValueToByte((long) i, 116, 127);
        }
    }

    public long getChainId() {
        if (!isValid()) {
            return 0;
        }
        return getValueFromByte(0, 59);
    }

    public void setChainId(long j) {
        if (isValidIdArray()) {
            if (!isValid()) {
                setValid();
                setVersion((byte) 0);
                setFlags(0);
                setSpanId(0);
                setParentSpanId(0);
            }
            setValueToByte(j, 0, 59);
        }
    }

    public long getSpanId() {
        if (!isValid()) {
            return 0;
        }
        return getValueFromByte(90, 115);
    }

    public void setSpanId(long j) {
        if (isValid()) {
            setValueToByte(j, 90, 115);
        }
    }

    public long getParentSpanId() {
        if (!isValid()) {
            return 0;
        }
        return getValueFromByte(64, 89);
    }

    public void setParentSpanId(long j) {
        if (isValid()) {
            setValueToByte(j, 64, 89);
        }
    }

    public byte[] toBytes() {
        if (!isValidIdArray()) {
            return new byte[0];
        }
        byte[] bArr = new byte[16];
        System.arraycopy(this.idArray, 0, bArr, 0, 16);
        return bArr;
    }

    public String toString() {
        if (!isValidIdArray()) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(((int) getVersion()) + ":");
        byte[] bArr = this.idArray;
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            byte b = bArr[i];
            String format = String.format(Locale.ENGLISH, "%s", Integer.toHexString(b & 255));
            if (format.length() == 1) {
                stringBuffer.append(0);
            }
            stringBuffer.append(format);
        }
        return stringBuffer.toString();
    }
}
