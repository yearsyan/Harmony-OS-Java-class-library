package ohos.telephony;

import java.util.ArrayList;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ShortMessageManager {
    private static final String FORMAT_3GPP = "3gpp";
    private static final String FORMAT_3GPP2 = "3gpp2";
    private static final String FORMAT_UNKNOWN = "unknown";
    private static final int PERMISSION_GRANTED = 0;
    private static final String PERMISSION_SEND_SMS = "ohos.permission.SEND_MESSAGES";
    private static final int SMS_MESSAGE_MAX_LENGTH = 70;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218111744, "ShortMessageManager");
    private static volatile ShortMessageManager instance;
    private final Context context;
    private final TelephonyProxy telephonyProxy = TelephonyProxy.getInstance();

    private ShortMessageManager(Context context2) {
        this.context = context2;
    }

    public static ShortMessageManager getInstance(Context context2) {
        if (instance == null) {
            synchronized (ShortMessageManager.class) {
                if (instance == null) {
                    instance = new ShortMessageManager(context2);
                }
            }
        }
        return instance;
    }

    @SystemApi
    public boolean setDefaultSmsSlotId(int i) {
        return this.telephonyProxy.setDefaultSmsSlotId(i);
    }

    public int getDefaultSmsSlotId() {
        return this.telephonyProxy.getDefaultSmsSlotId();
    }

    public boolean hasSmsCapability() {
        return TelephonyAdapt.hasSmsCapability();
    }

    public ArrayList<String> splitMessage(String str) {
        if (str == null || str.length() == 0) {
            HiLog.error(TAG, "Content is empty!", new Object[0]);
            ArrayList<String> arrayList = new ArrayList<>(1);
            arrayList.add("");
            return arrayList;
        }
        Context context2 = this.context;
        if (context2 != null && context2.verifySelfPermission("ohos.permission.SEND_MESSAGES") == 0) {
            return ShortMessage.splitMessageBySlotId(str, getDefaultSmsSlotId());
        }
        HiLog.error(TAG, "No send sms permission!", new Object[0]);
        ArrayList<String> arrayList2 = new ArrayList<>(1);
        arrayList2.add("");
        return arrayList2;
    }

    public void sendMessage(int i, String str, String str2, String str3, ISendShortMessageCallback iSendShortMessageCallback, IDeliveryShortMessageCallback iDeliveryShortMessageCallback) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("invalid destinationHost");
        } else if (str3 == null || str3.length() == 0) {
            throw new IllegalArgumentException("invalid content");
        } else if (TelephonyUtils.isValidSlotId(i)) {
            this.telephonyProxy.sendMessage(this.context, i, str, str2, str3, iSendShortMessageCallback, iDeliveryShortMessageCallback);
        } else {
            throw new IllegalArgumentException("invalid slotId");
        }
    }

    public void sendMessage(int i, String str, String str2, byte[] bArr, short s, ISendShortMessageCallback iSendShortMessageCallback, IDeliveryShortMessageCallback iDeliveryShortMessageCallback) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("invalid destinationHost");
        } else if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("invalid data");
        } else if (TelephonyUtils.isValidSlotId(i)) {
            this.telephonyProxy.sendMessage(this.context, i, str, str2, s, bArr, iSendShortMessageCallback, iDeliveryShortMessageCallback);
        } else {
            throw new IllegalArgumentException("invalid slotId");
        }
    }

    public String getImsShortMessageFormat() {
        String imsShortMessageFormat = this.telephonyProxy.getImsShortMessageFormat();
        return (FORMAT_3GPP.equals(imsShortMessageFormat) || FORMAT_3GPP2.equals(imsShortMessageFormat)) ? imsShortMessageFormat : FORMAT_UNKNOWN;
    }

    public boolean isImsSmsSupported() {
        return this.telephonyProxy.isImsSmsSupported();
    }

    @SystemApi
    public boolean setSmscAddr(int i, String str) {
        return this.telephonyProxy.setSmscAddr(i, str);
    }

    @SystemApi
    public String getSmscAddr(int i) {
        return this.telephonyProxy.getSmscAddr(i);
    }
}
