package ohos.wifi.p2p;

import java.net.InetAddress;
import java.net.UnknownHostException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.wifi.InnerUtils;

public class WifiP2pLinkedInfo implements Sequenceable {
    private static final HiLogLabel LABEL = new HiLogLabel(3, InnerUtils.LOG_ID_WIFI, "WifiP2pLinkedInfo");
    private InetAddress groupOwnerAddress;
    private boolean isGroupFormed;
    private boolean isGroupOwner;

    public boolean isGroupFormed() {
        return this.isGroupFormed;
    }

    public boolean isGroupOwner() {
        return this.isGroupOwner;
    }

    public InetAddress getGroupOwnerAddress() {
        return this.groupOwnerAddress;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0030, code lost:
        if (r5.writeByteArray(r4.groupOwnerAddress.getAddress()) != false) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r5.writeByte((byte) 0) != false) goto L_0x0032;
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0045 A[RETURN] */
    @Override // ohos.utils.Sequenceable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean marshalling(ohos.utils.Parcel r5) {
        /*
            r4 = this;
            boolean r0 = r4.isGroupFormed
            boolean r0 = r5.writeByte(r0)
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0014
            boolean r0 = r4.isGroupOwner
            boolean r0 = r5.writeByte(r0)
            if (r0 == 0) goto L_0x0014
            r0 = r1
            goto L_0x0015
        L_0x0014:
            r0 = r2
        L_0x0015:
            java.net.InetAddress r3 = r4.groupOwnerAddress
            if (r3 == 0) goto L_0x0036
            if (r0 == 0) goto L_0x0023
            boolean r0 = r5.writeByte(r1)
            if (r0 == 0) goto L_0x0023
            r0 = r1
            goto L_0x0024
        L_0x0023:
            r0 = r2
        L_0x0024:
            if (r0 == 0) goto L_0x0034
            java.net.InetAddress r4 = r4.groupOwnerAddress
            byte[] r4 = r4.getAddress()
            boolean r4 = r5.writeByteArray(r4)
            if (r4 == 0) goto L_0x0034
        L_0x0032:
            r4 = r1
            goto L_0x003f
        L_0x0034:
            r4 = r2
            goto L_0x003f
        L_0x0036:
            if (r0 == 0) goto L_0x0034
            boolean r4 = r5.writeByte(r2)
            if (r4 == 0) goto L_0x0034
            goto L_0x0032
        L_0x003f:
            if (r4 != 0) goto L_0x0045
            r5.reclaim()
            return r2
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.wifi.p2p.WifiP2pLinkedInfo.marshalling(ohos.utils.Parcel):boolean");
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.isGroupFormed = parcel.readInt() == 1;
        this.isGroupOwner = parcel.readInt() == 1;
        if (parcel.readByte() == 1) {
            try {
                this.groupOwnerAddress = InetAddress.getByAddress(parcel.readByteArray());
            } catch (UnknownHostException unused) {
                HiLog.warn(LABEL, "UnknownHostException in WifiP2pLinkedInfo unmarshalling!", new Object[0]);
            }
        }
        return true;
    }
}
