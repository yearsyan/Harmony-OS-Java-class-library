package ohos.dcall;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.DisconnectCause;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/* access modifiers changed from: package-private */
public class CallSerializationUtils {
    private static final String BLUETOOTH_CALL_KEY = "isBluetoothCall";
    private static final int CALL_CAPABILITY_OFFSET = 8;
    private static final int CALL_PROPERTY_MASK = 17;
    private static final int FLAG_EMPTY_BUNDLE = -1;
    private static final int LOG_DOMAIN_DCALL = 218111744;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218111744, TAG);
    private static final String TAG = "CallSerializationUtils";
    private static final int VALUE_TYPE_ARRAY_LIST = 20;
    private static final int VALUE_TYPE_STRING = 8;

    CallSerializationUtils() {
    }

    static Parcel writePreCallToParcel(Parcel parcel, int i, String str, int i2, int i3) {
        if (!(parcel == null || str == null)) {
            parcel.writeInt(i);
            parcel.writeInt(i2);
            writeDisconnectCauseToParcel(parcel, new DisconnectCause(0));
            parcel.writeInt(0);
            parcel.writeLong(0);
            parcel.writeString(str);
            parcel.writeInt(0);
            parcel.writeString("");
            parcel.writeInt(1);
            writeBundleToParcel(parcel, null);
            writeBundleToParcel(parcel, null);
            parcel.writeLong(0);
            parcel.writeInt(1);
            parcel.writeInt(i3);
        }
        return parcel;
    }

    static Parcel writeCallToParcel(Parcel parcel, Call call, int i) {
        String str;
        if (!(parcel == null || call == null || call.getDetails() == null)) {
            Call.Details details = call.getDetails();
            parcel.writeInt(i);
            parcel.writeInt(call.getState());
            writeDisconnectCauseToParcel(parcel, details.getDisconnectCause());
            parcel.writeInt((details.getCallCapabilities() << 8) | (details.getCallProperties() & 17));
            parcel.writeLong(details.getConnectTimeMillis());
            Uri handle = details.getHandle();
            if (handle == null) {
                str = "";
            } else {
                str = handle.getSchemeSpecificPart();
            }
            parcel.writeString(str);
            parcel.writeInt(details.getHandlePresentation());
            parcel.writeString(details.getCallerDisplayName());
            parcel.writeInt(details.getCallerDisplayNamePresentation());
            writeBundleToParcel(parcel, details.getIntentExtras());
            Bundle extras = details.getExtras();
            if (extras == null) {
                extras = new Bundle();
            }
            extras.putBoolean(BLUETOOTH_CALL_KEY, AospInCallService.isConnectedBluetoothCall(call));
            writeBundleToParcel(parcel, extras);
            parcel.writeLong(details.getCreationTimeMillis());
            parcel.writeInt(details.getCallDirection());
            parcel.writeInt(details.getVideoState());
        }
        return parcel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0069  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static android.os.Parcel writeBundleToParcel(android.os.Parcel r5, android.os.Bundle r6) {
        /*
        // Method dump skipped, instructions count: 110
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallSerializationUtils.writeBundleToParcel(android.os.Parcel, android.os.Bundle):android.os.Parcel");
    }

    static Map<String, Object> convertToSpecificMap(Object obj) {
        HashMap hashMap = new HashMap();
        if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                if (entry.getKey() instanceof String) {
                    hashMap.put((String) String.class.cast(entry.getKey()), entry.getValue());
                }
            }
        }
        return hashMap;
    }

    static Parcel writeSpecificMapToParcel(Parcel parcel, Map<String, Object> map) {
        if (!(parcel == null || map == null)) {
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            if (entrySet == null) {
                HiLog.error(LOG_LABEL, "writeSpecificMapToParcel: entries is null.", new Object[0]);
                return parcel;
            }
            parcel.writeInt(entrySet.size());
            for (Map.Entry<String, Object> entry : entrySet) {
                parcel.writeInt(8);
                parcel.writeString(entry.getKey());
                Object value = entry.getValue();
                if (value instanceof ArrayList) {
                    writeArrayListToParcel(parcel, (ArrayList) value);
                } else {
                    parcel.writeInt(8);
                    if (value != null) {
                        parcel.writeString(value.toString());
                    } else {
                        parcel.writeString("");
                    }
                }
            }
        }
        return parcel;
    }

    static Parcel writeArrayListToParcel(Parcel parcel, ArrayList<?> arrayList) {
        if (!(parcel == null || arrayList == null)) {
            parcel.writeInt(20);
            int size = arrayList.size();
            parcel.writeInt(size);
            if (size <= 0) {
                return parcel;
            }
            Iterator<?> it = arrayList.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                parcel.writeInt(8);
                if (next instanceof String) {
                    parcel.writeString((String) next);
                } else {
                    parcel.writeString(next.toString());
                }
            }
        }
        return parcel;
    }

    static Parcel writeCallAudioStateToParcel(Parcel parcel, CallAudioState callAudioState) {
        if (!(parcel == null || callAudioState == null)) {
            parcel.writeByte(callAudioState.isMuted() ? (byte) 1 : 0);
            parcel.writeInt(callAudioState.getRoute());
            parcel.writeInt(callAudioState.getSupportedRouteMask());
        }
        return parcel;
    }

    static Parcel writeDisconnectCauseToParcel(Parcel parcel, DisconnectCause disconnectCause) {
        if (!(parcel == null || disconnectCause == null)) {
            CharSequence label = disconnectCause.getLabel();
            String str = null;
            String charSequence = label != null ? label.toString() : null;
            CharSequence description = disconnectCause.getDescription();
            if (description != null) {
                str = description.toString();
            }
            String reason = disconnectCause.getReason();
            parcel.writeInt(DistributedCallUtils.toOhosDisconnectCode(reason));
            parcel.writeString(charSequence);
            parcel.writeString(str);
            parcel.writeString(reason);
        }
        return parcel;
    }
}
