package ohos.nfc;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Skills;
import ohos.event.commonevent.CommonEventBaseConverter;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class NfcEventsConverter extends CommonEventBaseConverter {
    private static final String ADAPTER_STATE_CHANGED = "android.nfc.action.ADAPTER_STATE_CHANGED";
    private static final String EXTRA_ADAPTER_STATE = "android.nfc.extra.ADAPTER_STATE";
    private static final String EXTRA_TRANSACTION = "transactionIntent";
    private static final HiLogLabel LABEL = new HiLogLabel(3, NfcKitsUtils.NFC_DOMAIN_ID, "NfcEventsConverter");
    private static final Map<String, ZAction> MAP_ACTION_CONVERTER = Collections.unmodifiableMap(new HashMap<String, ZAction>() {
        /* class ohos.nfc.NfcEventsConverter.AnonymousClass3 */

        {
            put(NfcEventsConverter.ADAPTER_STATE_CHANGED, new ZAction("usual.event.nfc.action.ADAPTER_STATE_CHANGED", NfcEventsConverter.MAP_EXTRAS_ADAPTER_STATE));
            put("com.android.nfc_extras.action.RF_FIELD_ON_DETECTED", new ZAction("usual.event.nfc.action.RF_FIELD_ON_DETECTED", NfcEventsConverter.MAP_EXTRAS_EXTRA_TRANSACTION_INTENT));
            put("com.android.nfc_extras.action.RF_FIELD_OFF_DETECTED", new ZAction("usual.event.nfc.action.RF_FIELD_OFF_DETECTED", null));
        }
    });
    private static final Map<String, String> MAP_EXTRAS_ADAPTER_STATE = Collections.unmodifiableMap(new HashMap<String, String>() {
        /* class ohos.nfc.NfcEventsConverter.AnonymousClass1 */

        {
            put(NfcEventsConverter.EXTRA_ADAPTER_STATE, NfcController.EXTRA_NFC_STATE);
        }
    });
    private static final Map<String, String> MAP_EXTRAS_EXTRA_TRANSACTION_INTENT = Collections.unmodifiableMap(new HashMap<String, String>() {
        /* class ohos.nfc.NfcEventsConverter.AnonymousClass2 */

        {
            put(NfcEventsConverter.EXTRA_TRANSACTION, NfcController.EXTRA_NFC_TRANSACTION);
        }
    });
    private static final String RF_FIELD_OFF_DETECTED = "com.android.nfc_extras.action.RF_FIELD_OFF_DETECTED";
    private static final String RF_FIELD_ON_DETECTED = "com.android.nfc_extras.action.RF_FIELD_ON_DETECTED";

    private Object convertExtraValue(String str, String str2, Object obj) {
        return obj;
    }

    private static final class ZAction {
        public String action;
        public Map<String, String> extrasConverter;

        public ZAction(String str, Map<String, String> map) {
            this.action = str;
            this.extrasConverter = map;
        }
    }

    @Override // ohos.event.commonevent.CommonEventBaseConverter
    public Optional<Intent> convertIntentToAospIntent(ohos.aafwk.content.Intent intent) {
        return Optional.ofNullable(new Intent());
    }

    @Override // ohos.event.commonevent.CommonEventBaseConverter
    public Optional<ohos.aafwk.content.Intent> convertAospIntentToIntent(Intent intent) {
        HiLog.info(LABEL, "ENTER convertAospIntentToIntent", new Object[0]);
        if (intent == null) {
            HiLog.warn(LABEL, "convertAospIntentToIntent, aIntent is null", new Object[0]);
            return Optional.empty();
        }
        String action = intent.getAction();
        ZAction zAction = MAP_ACTION_CONVERTER.get(action);
        if (zAction == null) {
            HiLog.warn(LABEL, "convertAospIntentToIntent, zAction is null, aAction = %{public}s", action);
            return Optional.empty();
        }
        ohos.aafwk.content.Intent intent2 = new ohos.aafwk.content.Intent();
        intent2.setAction(zAction.action);
        Bundle extras = intent.getExtras();
        HiLog.info(LABEL, "aAction: %{public}s, zAction: %{public}s", action, zAction.action);
        if (!(extras == null || extras.keySet() == null)) {
            Set<String> keySet = extras.keySet();
            IntentParams intentParams = new IntentParams();
            for (String str : keySet) {
                if (str == null || !(str instanceof String)) {
                    HiLog.warn(LABEL, "aExtraKey is null.", new Object[0]);
                } else {
                    String str2 = str;
                    if (zAction.extrasConverter == null || zAction.extrasConverter.get(str2) == null) {
                        HiLog.warn(LABEL, "Cannot get z key from a key: %{public}s or extrasConverter is null", str2);
                    } else {
                        intentParams.setParam(zAction.extrasConverter.get(str2), convertExtraValue(action, str2, extras.get(str2)));
                    }
                }
            }
            intent2.setParams(intentParams);
        }
        HiLog.info(LABEL, "convertAospIntentToIntent return zIntent", new Object[0]);
        return Optional.ofNullable(intent2);
    }

    @Override // ohos.event.commonevent.CommonEventBaseConverter
    public void convertSkillsToAospIntentFilter(Skills skills, IntentFilter intentFilter) {
        HiLog.warn(LABEL, "unhandle convertSkillsToAospIntentFilter", new Object[0]);
    }
}
