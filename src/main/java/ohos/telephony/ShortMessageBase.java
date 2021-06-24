package ohos.telephony;

import android.content.res.Resources;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsConstants;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.cdma.SmsMessage;
import java.util.ArrayList;
import java.util.Optional;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ShortMessageBase {
    private static final int ENCODING_16BIT = 3;
    private static final int ENCODING_7BIT = 1;
    private static final int ENCODING_8BIT = 2;
    private static final int ENCODING_UNKNOWN = 0;
    private static final String FORMAT_3GPP = "3gpp";
    private static final String FORMAT_3GPP2 = "3gpp2";
    private static final int MAX_USER_DATA_BYTES = 140;
    private static final int MAX_USER_DATA_BYTES_WITH_HEADER = 134;
    private static final int MAX_USER_DATA_SEPTETS = 160;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218111744, "ShortMessageBase");
    private static boolean hasCheckedShortMessageConfig = false;
    private static ShortMessageConfig[] shortMessageConfigList = null;
    private SmsMessageBase wrappedShortMessage;

    /* access modifiers changed from: private */
    public static class ShortMessageConfig {
        String gid1;
        boolean isPrefix;
        String operator;

        public ShortMessageConfig(String[] strArr) {
            if (strArr != null) {
                boolean z = false;
                String str = "";
                this.operator = strArr.length > 0 ? strArr[0] : str;
                this.isPrefix = strArr.length > 1 ? "prefix".equals(strArr[1]) : z;
                this.gid1 = strArr.length > 2 ? strArr[2] : str;
            }
        }

        public String toString() {
            return "ShortMessageConfig { operator = " + this.operator + ", isPrefix = " + this.isPrefix + ", gid1 = " + this.gid1 + " }";
        }
    }

    private ShortMessageBase(SmsMessageBase smsMessageBase) {
        this.wrappedShortMessage = smsMessageBase;
    }

    public int getProtocolId() {
        return this.wrappedShortMessage.getProtocolIdentifier();
    }

    /* renamed from: ohos.telephony.ShortMessageBase$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass = new int[SmsConstants.MessageClass.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                com.android.internal.telephony.SmsConstants$MessageClass[] r0 = com.android.internal.telephony.SmsConstants.MessageClass.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.telephony.ShortMessageBase.AnonymousClass1.$SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass = r0
                int[] r0 = ohos.telephony.ShortMessageBase.AnonymousClass1.$SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.android.internal.telephony.SmsConstants$MessageClass r1 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_0     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.telephony.ShortMessageBase.AnonymousClass1.$SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass     // Catch:{ NoSuchFieldError -> 0x001f }
                com.android.internal.telephony.SmsConstants$MessageClass r1 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_1     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.telephony.ShortMessageBase.AnonymousClass1.$SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass     // Catch:{ NoSuchFieldError -> 0x002a }
                com.android.internal.telephony.SmsConstants$MessageClass r1 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_2     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.telephony.ShortMessageBase.AnonymousClass1.$SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.android.internal.telephony.SmsConstants$MessageClass r1 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_3     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.ShortMessageBase.AnonymousClass1.<clinit>():void");
        }
    }

    public int getMessageClass() {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass[this.wrappedShortMessage.getMessageClass().ordinal()];
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    i2 = 4;
                    if (i != 4) {
                        return 0;
                    }
                }
            }
        }
        return i2;
    }

    public static Optional<ShortMessageBase> createMessage(byte[] bArr, String str) {
        SmsMessage smsMessage;
        if (bArr == null) {
            HiLog.error(TAG, "createMessage(): pdu is null", new Object[0]);
            return Optional.empty();
        }
        if (FORMAT_3GPP2.equals(str)) {
            smsMessage = SmsMessage.createFromPdu(bArr);
        } else if (FORMAT_3GPP.equals(str)) {
            smsMessage = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(bArr);
        } else {
            HiLog.error(TAG, "createMessage(): unsupported message format ", new Object[0]);
            return Optional.empty();
        }
        if (smsMessage != null) {
            return Optional.of(new ShortMessageBase(smsMessage));
        }
        return Optional.empty();
    }

    private static ArrayList<String> emptyList() {
        ArrayList<String> arrayList = new ArrayList<>(1);
        arrayList.add("");
        return arrayList;
    }

    private static int computeGsmSeptetLimit(GsmAlphabet.TextEncodingDetails textEncodingDetails) {
        int i;
        if (textEncodingDetails.codeUnitSize == 1) {
            if (textEncodingDetails.languageTable == 0 && textEncodingDetails.languageShiftTable == 0) {
                i = 160;
            } else {
                i = (textEncodingDetails.languageTable == 0 || textEncodingDetails.languageShiftTable == 0) ? 156 : 153;
            }
            if (textEncodingDetails.msgCount > 1) {
                i -= 6;
            }
            return i != 160 ? i - 1 : i;
        } else if (textEncodingDetails.msgCount <= 1) {
            return 140;
        } else {
            if (isLongShortMessageSupported() || textEncodingDetails.msgCount >= 10) {
                return 134;
            }
            return 132;
        }
    }

    public static ArrayList<String> splitMessageBySlotId(String str, int i) {
        GsmAlphabet.TextEncodingDetails textEncodingDetails;
        int i2;
        if (str == null || str.length() == 0) {
            return emptyList();
        }
        boolean isMoSmsInCdmaSpecification = isMoSmsInCdmaSpecification(i);
        if (isMoSmsInCdmaSpecification) {
            textEncodingDetails = SmsMessage.calculateLength(str, false, true);
        } else {
            textEncodingDetails = com.android.internal.telephony.gsm.SmsMessage.calculateLength(str, false);
        }
        if (textEncodingDetails == null) {
            return emptyList();
        }
        int computeGsmSeptetLimit = computeGsmSeptetLimit(textEncodingDetails);
        String str2 = null;
        Resources system = Resources.getSystem();
        if (system != null && system.getBoolean(17891529)) {
            str2 = Sms7BitEncodingTranslator.translate(str, isMoSmsInCdmaSpecification);
        }
        if (!(str2 == null || str2.length() == 0)) {
            str = str2;
        }
        int length = str.length();
        ArrayList<String> arrayList = new ArrayList<>(textEncodingDetails.msgCount);
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                break;
            }
            if (textEncodingDetails.codeUnitSize != 1) {
                i2 = SmsMessageBase.findNextUnicodePosition(i3, computeGsmSeptetLimit, str);
            } else if (!isMoSmsInCdmaSpecification) {
                i2 = GsmAlphabet.findGsmSeptetLimitIndex(str, i3, computeGsmSeptetLimit, textEncodingDetails.languageTable, textEncodingDetails.languageShiftTable);
            } else if (!isMoSmsInCdmaSpecification(i)) {
                i2 = GsmAlphabet.findGsmSeptetLimitIndex(str, i3, computeGsmSeptetLimit, textEncodingDetails.languageTable, textEncodingDetails.languageShiftTable);
            } else if (textEncodingDetails.msgCount != 1) {
                i2 = GsmAlphabet.findGsmSeptetLimitIndex(str, i3, computeGsmSeptetLimit, textEncodingDetails.languageTable, textEncodingDetails.languageShiftTable);
            } else {
                i2 = Math.min(computeGsmSeptetLimit, length - i3) + i3;
            }
            if (i2 <= i3 || i2 > length) {
                HiLog.error(TAG, "splitMessage failed (%{public}d <= %{public}d or >= %{public}d)", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(length));
            } else {
                arrayList.add(str.substring(i3, i2));
                i3 = i2;
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002e A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isMoSmsInCdmaSpecification(int r3) {
        /*
            r0 = 0
            ohos.telephony.ShortMessageManager r0 = ohos.telephony.ShortMessageManager.getInstance(r0)
            boolean r1 = r0.isImsSmsSupported()
            if (r1 != 0) goto L_0x0030
            java.lang.String r0 = "gsm.current.phone-type"
            java.lang.String r1 = ""
            java.lang.String r3 = ohos.telephony.TelephonyUtils.getTelephonyProperty(r3, r0, r1)
            r0 = 0
            if (r3 == 0) goto L_0x002a
            boolean r1 = r3.isEmpty()
            if (r1 != 0) goto L_0x002a
            int r3 = java.lang.Integer.parseInt(r3)     // Catch:{ NumberFormatException -> 0x0021 }
            goto L_0x002b
        L_0x0021:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.telephony.ShortMessageBase.TAG
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "prop value is invalid !"
            ohos.hiviewdfx.HiLog.error(r3, r2, r1)
        L_0x002a:
            r3 = r0
        L_0x002b:
            r1 = 2
            if (r3 != r1) goto L_0x002f
            r0 = 1
        L_0x002f:
            return r0
        L_0x0030:
            java.lang.String r3 = r0.getImsShortMessageFormat()
            java.lang.String r0 = "3gpp2"
            boolean r3 = r0.equals(r3)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.telephony.ShortMessageBase.isMoSmsInCdmaSpecification(int):boolean");
    }

    private static boolean isLongShortMessageSupported() {
        if (!initShortMessageConfig()) {
            return true;
        }
        SimInfoManager instance = SimInfoManager.getInstance(null);
        ShortMessageManager instance2 = ShortMessageManager.getInstance(null);
        String simOperatorNumeric = instance.getSimOperatorNumeric(instance2.getDefaultSmsSlotId());
        String simGid1 = instance.getSimGid1(instance2.getDefaultSmsSlotId());
        if (!(simOperatorNumeric == null || simOperatorNumeric.length() == 0)) {
            ShortMessageConfig[] shortMessageConfigArr = shortMessageConfigList;
            for (ShortMessageConfig shortMessageConfig : shortMessageConfigArr) {
                if (simOperatorNumeric.startsWith(shortMessageConfig.operator) && (shortMessageConfig.gid1 == null || shortMessageConfig.gid1.length() == 0 || shortMessageConfig.gid1.equalsIgnoreCase(simGid1))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean initShortMessageConfig() {
        Resources system = Resources.getSystem();
        if (hasCheckedShortMessageConfig || system == null) {
            ShortMessageConfig[] shortMessageConfigArr = shortMessageConfigList;
            return (shortMessageConfigArr == null || shortMessageConfigArr.length == 0) ? false : true;
        }
        hasCheckedShortMessageConfig = true;
        String[] stringArray = system.getStringArray(17236094);
        if (stringArray == null || stringArray.length <= 0) {
            return false;
        }
        shortMessageConfigList = new ShortMessageConfig[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            shortMessageConfigList[i] = new ShortMessageConfig(stringArray[i].split(";"));
        }
        return true;
    }

    public String getVisibleMessageBody() {
        return this.wrappedShortMessage.getDisplayMessageBody();
    }

    public String getVisibleRawAddress() {
        return this.wrappedShortMessage.getDisplayOriginatingAddress();
    }

    public String getScAddress() {
        return this.wrappedShortMessage.getServiceCenterAddress();
    }

    public long getScTimestamp() {
        return this.wrappedShortMessage.getTimestampMillis();
    }

    public byte[] getUserRawData() {
        return this.wrappedShortMessage.getUserData();
    }

    public boolean isEmailMessage() {
        return this.wrappedShortMessage.isEmail();
    }

    public boolean isReplaceMessage() {
        return this.wrappedShortMessage.isReplace();
    }

    public boolean hasReplyPath() {
        return this.wrappedShortMessage.isReplyPathPresent();
    }

    public String getEmailMessageBody() {
        return this.wrappedShortMessage.getEmailBody();
    }

    public String getEmailAddress() {
        return this.wrappedShortMessage.getEmailFrom();
    }

    public int getStatus() {
        return this.wrappedShortMessage.getStatus();
    }

    public byte[] getPdu() {
        return this.wrappedShortMessage.getPdu();
    }

    public boolean isStatusReportMessage() {
        return this.wrappedShortMessage.isStatusReportMessage();
    }
}
