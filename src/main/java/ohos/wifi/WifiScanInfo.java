package ohos.wifi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Parcel;
import ohos.utils.ParcelException;
import ohos.utils.Sequenceable;

public final class WifiScanInfo implements Sequenceable {
    private static final long FLAG_80211MC_RESPONDER = 2;
    private static final long FLAG_PASSPOINT_NETWORK = 1;
    private static final int FRE_2G_LOWER = 2400;
    private static final int FRE_2G_UPPER = 2500;
    private static final int FRE_5G_LOWER = 4900;
    private static final int FRE_5G_UPPER = 5900;
    private static final HiLogLabel LABEL = new HiLogLabel(3, InnerUtils.LOG_ID_WIFI, "WifiScanInfo");
    private int band;
    private String bssid;
    private String capabilities;
    private int[] centerFrequencies;
    private int channelWidth;
    private long features;
    private int frequency;
    private Map<Integer, byte[]> informationElements;
    private boolean isHilink;
    private int rssi;
    private int securityType;
    private String ssid;
    private long timestamp;

    public static int convertBandByFreq(int i) {
        return ((i <= 2400 || i >= 2500) && i > FRE_5G_LOWER && i < FRE_5G_UPPER) ? 1 : 0;
    }

    public WifiScanInfo(String str, String str2, String str3, int i, int i2, int i3, int i4, long j, int i5, int[] iArr) {
        this.frequency = 0;
        this.centerFrequencies = new int[]{-1, -1};
        this.ssid = str;
        this.bssid = str2;
        this.capabilities = str3;
        this.securityType = i;
        this.rssi = i2;
        this.band = i3;
        this.channelWidth = i4;
        this.timestamp = j;
        this.informationElements = new HashMap();
        this.features = 0;
        this.isHilink = false;
        this.frequency = i5;
        this.centerFrequencies = iArr;
    }

    public WifiScanInfo() {
        this(null, null, null, 0, 0, 0, 0, 0, 0, new int[]{-1, -1});
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:79:0x00a2 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:80:0x00d9 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:82:0x00a2 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARN: Type inference failed for: r5v6 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v17 */
    /* JADX WARN: Type inference failed for: r0v21 */
    /* JADX WARN: Type inference failed for: r0v22 */
    /* JADX WARN: Type inference failed for: r0v27 */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r0v32 */
    /* JADX WARN: Type inference failed for: r0v33 */
    /* JADX WARN: Type inference failed for: r0v36 */
    /* JADX WARN: Type inference failed for: r0v37 */
    /* JADX WARN: Type inference failed for: r0v39 */
    /* JADX WARN: Type inference failed for: r0v40 */
    /* JADX WARN: Type inference failed for: r0v42 */
    /* JADX WARN: Type inference failed for: r0v43 */
    /* JADX WARN: Type inference failed for: r0v46 */
    /* JADX WARN: Type inference failed for: r0v47 */
    /* JADX WARN: Type inference failed for: r0v50 */
    /* JADX WARN: Type inference failed for: r0v51 */
    /* JADX WARN: Type inference failed for: r0v54 */
    /* JADX WARN: Type inference failed for: r0v55 */
    /* JADX WARN: Type inference failed for: r0v58 */
    /* JADX WARN: Type inference failed for: r0v59 */
    /* JADX WARN: Type inference failed for: r0v62 */
    /* JADX WARN: Type inference failed for: r0v63 */
    /* JADX WARN: Type inference failed for: r0v66 */
    /* JADX WARNING: Unknown variable types count: 11 */
    @Override // ohos.utils.Sequenceable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean marshalling(ohos.utils.Parcel r6) {
        /*
        // Method dump skipped, instructions count: 246
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.wifi.WifiScanInfo.marshalling(ohos.utils.Parcel):boolean");
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.ssid = parcel.readString();
        this.bssid = parcel.readString();
        this.capabilities = parcel.readString();
        this.securityType = parcel.readInt();
        this.rssi = parcel.readInt();
        this.frequency = parcel.readInt();
        this.channelWidth = parcel.readInt();
        this.timestamp = parcel.readLong();
        this.features = parcel.readLong();
        this.isHilink = parcel.readInt() == 1;
        int readInt = parcel.readInt();
        if (readInt > 0) {
            for (int i = 0; i < readInt; i++) {
                if (parcel.getReadableBytes() <= 0) {
                    HiLog.warn(LABEL, "read parcel failed ", new Object[0]);
                    return false;
                }
                try {
                    this.informationElements.put(Integer.valueOf(parcel.readInt()), parcel.readByteArray());
                } catch (ParcelException unused) {
                    HiLog.warn(LABEL, "Read informationElements bytes error", new Object[0]);
                }
            }
        } else {
            this.informationElements.clear();
            HiLog.warn(LABEL, "Read informationElements length is invalid!", new Object[0]);
        }
        try {
            int[] iArr = {-1, -1};
            int[] readIntArray = parcel.readIntArray();
            if (readIntArray.length >= 2) {
                this.centerFrequencies = readIntArray;
            }
        } catch (ParcelException unused2) {
            HiLog.warn(LABEL, "Read centerFrequencies bytes error", new Object[0]);
        }
        this.band = convertBandByFreq(this.frequency);
        return true;
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getBssid() {
        return this.bssid;
    }

    public String getCapabilities() {
        return this.capabilities;
    }

    public int getSecurityType() {
        return this.securityType;
    }

    public int getRssi() {
        return this.rssi;
    }

    public int getBand() {
        return this.band;
    }

    public int getBandWidth() {
        return this.channelWidth;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int[] getCenterFrequencies() {
        return this.centerFrequencies;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public Map<Integer, byte[]> getInformationElements() {
        return this.informationElements;
    }

    @SystemApi
    public boolean is80211mcResponder() {
        return (this.features & 2) != 0;
    }

    @SystemApi
    public boolean isPasspointNetwork() {
        return (this.features & 1) != 0;
    }

    @SystemApi
    public boolean isHiLinkNetwork() {
        return this.isHilink;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SSID: ");
        String str = this.ssid;
        String str2 = "<none>";
        stringBuffer.append(str == null ? str2 : InnerUtils.safeDisplaySsid(str));
        stringBuffer.append(", BSSID: ");
        String str3 = this.bssid;
        if (str3 != null) {
            str2 = InnerUtils.safeDisplayBssid(str3);
        }
        stringBuffer.append(str2);
        stringBuffer.append(", Capabilities: ");
        stringBuffer.append(this.capabilities);
        stringBuffer.append(", SecurityType: ");
        stringBuffer.append(this.securityType);
        stringBuffer.append(", RSSI: ");
        stringBuffer.append(this.rssi);
        stringBuffer.append(", Band: ");
        stringBuffer.append(this.band);
        stringBuffer.append(", ChannelWidth: ");
        stringBuffer.append(this.channelWidth);
        stringBuffer.append(", Timestamp: ");
        stringBuffer.append(this.timestamp);
        stringBuffer.append(", features: ");
        stringBuffer.append(this.features);
        stringBuffer.append(", isHilink: ");
        stringBuffer.append(this.isHilink);
        stringBuffer.append(", frequency: ");
        stringBuffer.append(this.frequency);
        stringBuffer.append(", centerFrequencies[0]: ");
        int i = 0;
        stringBuffer.append(this.centerFrequencies[0]);
        stringBuffer.append(", centerFrequencies[1]: ");
        stringBuffer.append(this.centerFrequencies[1]);
        for (Map.Entry<Integer, byte[]> entry : this.informationElements.entrySet()) {
            stringBuffer.append(", id: ");
            stringBuffer.append(entry.getKey());
            stringBuffer.append(", information: ");
            stringBuffer.append(Arrays.toString(entry.getValue()));
            i++;
            if (i > 5) {
                break;
            }
        }
        return stringBuffer.toString();
    }
}
