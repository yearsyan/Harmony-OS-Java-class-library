package ohos.dcall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.telecom.PhoneAccountHandle;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AospInCallService extends InCallService {
    private static final String BLUETOOTH_CONNECTION_SERVICE_NAME = "com.android.bluetooth/.hfpclient.connserv.HfpClientConnectionService";
    private static final int CAAS_VOIP_NUMBER_WITHOUT_PLUS_SIGN_LENGTH = 14;
    private static final int CAAS_VOIP_NUMBER_WITH_PLUS_SIGN_LENGTH = 15;
    static final int CALL_TYPE_CARRIER = 0;
    static final int CALL_TYPE_HICALL = 1;
    static final int CALL_TYPE_UNKNOWN = -1;
    private static final String EXTRA_IS_HICALL = "extra_is_hicall";
    private static final int LOG_DOMAIN_DCALL = 218111744;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218111744, TAG);
    private static final String PREFIX_CAAS_VOIP_NUMBER_WITHOUT_PLUS_SIGN = "887";
    private static final String PREFIX_CAAS_VOIP_NUMBER_WITH_PLUS_SIGN = "+887";
    private static final String TAG = "AospInCallService";
    private CallAppAbilityProxy mProxy;
    private final Call.Callback mTelecomCallCallback = new Call.Callback() {
        /* class ohos.dcall.AospInCallService.AnonymousClass1 */

        public void onStateChanged(Call call, int i) {
            if (AospInCallService.this.mProxy != null) {
                AospInCallService.this.mProxy.onStateChanged(call, i);
            }
        }

        public void onDetailsChanged(Call call, Call.Details details) {
            if (AospInCallService.this.mProxy != null) {
                AospInCallService.this.mProxy.onDetailsChanged(call, details);
            }
        }

        public void onPostDialWait(Call call, String str) {
            if (AospInCallService.this.mProxy != null) {
                AospInCallService.this.mProxy.onPostDialWait(call, str);
            }
        }

        public void onCallDestroyed(Call call) {
            if (AospInCallService.this.mProxy != null) {
                AospInCallService.this.mProxy.onCallDestroyed(call);
            }
        }

        public void onConnectionEvent(Call call, String str, Bundle bundle) {
            if (AospInCallService.this.mProxy != null) {
                AospInCallService.this.mProxy.onConnectionEvent(call, str, bundle);
            }
        }
    };

    public IBinder onBind(Intent intent) {
        HiLog.info(LOG_LABEL, "onBind", new Object[0]);
        AospCallAdapter.getInstance().setAospInCallService(this);
        this.mProxy = CallAppAbilityProxy.getInstance();
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        if (callAppAbilityProxy != null) {
            callAppAbilityProxy.setContext(this);
            this.mProxy.onInCallServiceBind();
        }
        return super.onBind(intent);
    }

    public boolean onUnbind(Intent intent) {
        HiLog.info(LOG_LABEL, "onUnbind", new Object[0]);
        AospCallAdapter.getInstance().setAospInCallService(null);
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        if (callAppAbilityProxy != null) {
            callAppAbilityProxy.onInCallServiceUnbind();
            this.mProxy = null;
        }
        return super.onUnbind(intent);
    }

    public void onCallAudioStateChanged(CallAudioState callAudioState) {
        HiLog.info(LOG_LABEL, "onCallAudioStateChanged", new Object[0]);
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        if (callAppAbilityProxy != null) {
            callAppAbilityProxy.onCallAudioStateChanged(callAudioState);
        }
    }

    public void onCallAdded(Call call) {
        HiLogLabel hiLogLabel = LOG_LABEL;
        Object[] objArr = new Object[1];
        objArr[0] = call == null ? "" : call.toString();
        HiLog.info(hiLogLabel, "onCallAdded: call = %{public}s", objArr);
        if (call != null) {
            call.registerCallback(this.mTelecomCallCallback);
            CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
            if (callAppAbilityProxy != null) {
                callAppAbilityProxy.onCallAdded(call);
            }
        }
    }

    public void onCallRemoved(Call call) {
        HiLogLabel hiLogLabel = LOG_LABEL;
        Object[] objArr = new Object[1];
        objArr[0] = call == null ? "" : call.toString();
        HiLog.info(hiLogLabel, "onCallRemoved: call = %{public}s", objArr);
        if (call != null) {
            call.unregisterCallback(this.mTelecomCallCallback);
            CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
            if (callAppAbilityProxy != null) {
                callAppAbilityProxy.onCallRemoved(call);
            }
        }
    }

    public void onCanAddCallChanged(boolean z) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        if (callAppAbilityProxy != null) {
            callAppAbilityProxy.onCanAddCallChanged(z);
        }
    }

    public void onSilenceRinger() {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        if (callAppAbilityProxy != null) {
            callAppAbilityProxy.onSilenceRinger();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean answer(int i, int i2) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.answer(i2);
            return true;
        }
        HiLog.error(LOG_LABEL, "answer, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean disconnect(int i) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.disconnect();
            return true;
        }
        HiLog.error(LOG_LABEL, "disconnectCall, no matched call, callId is %{public}d.", Integer.valueOf(i));
        CallAppAbilityProxy callAppAbilityProxy2 = this.mProxy;
        if (callAppAbilityProxy2 != null) {
            return callAppAbilityProxy2.disconnectPreAddedCall(i);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean playDtmfTone(int i, char c) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.playDtmfTone(c);
            return true;
        }
        HiLog.error(LOG_LABEL, "playDtmfTone, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean stopDtmfTone(int i) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.stopDtmfTone();
            return true;
        }
        HiLog.error(LOG_LABEL, "stopDtmfTone, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean postDialContinue(int i, boolean z) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.postDialContinue(z);
            return true;
        }
        HiLog.error(LOG_LABEL, "postDialContinue, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean reject(int i, boolean z, String str) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.reject(z, str);
            return true;
        }
        HiLog.error(LOG_LABEL, "rejectCall, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean hold(int i) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.hold();
            return true;
        }
        HiLog.error(LOG_LABEL, "hold, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean unHold(int i) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.unhold();
            return true;
        }
        HiLog.error(LOG_LABEL, "unhold, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public List<String> getPredefinedRejectMessages(int i) {
        ArrayList arrayList = new ArrayList();
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            return callById.getCannedTextResponses();
        }
        HiLog.error(LOG_LABEL, "getPredefinedRejectMessages, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public boolean sendCallEvent(int i, String str, Bundle bundle) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.sendCallEvent(str, bundle);
            return true;
        }
        HiLog.error(LOG_LABEL, "sendCallEvent, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean joinConference(int i, int i2) {
        Call call;
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call call2 = null;
        if (callAppAbilityProxy != null) {
            call2 = callAppAbilityProxy.getCallById(Integer.valueOf(i));
            call = this.mProxy.getCallById(Integer.valueOf(i2));
        } else {
            call = null;
        }
        if (call2 == null) {
            HiLog.error(LOG_LABEL, "joinConference, no matched call, callId is %{public}d.", Integer.valueOf(i));
            return false;
        } else if (call == null) {
            HiLog.error(LOG_LABEL, "joinConference, no call to joinConference with, callId is %{public}d.", Integer.valueOf(i2));
            return false;
        } else {
            call2.conference(call);
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean combineConference(int i) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            HiLog.info(LOG_LABEL, "combineConference success", new Object[0]);
            callById.mergeConference();
            return true;
        }
        HiLog.error(LOG_LABEL, "combineConference, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean separateConference(int i) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.splitFromConference();
            return true;
        }
        HiLog.error(LOG_LABEL, "separateConference, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean switchCdmaConference(int i) {
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            callById.swapConference();
            return true;
        }
        HiLog.error(LOG_LABEL, "switchCdmaConference, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return false;
    }

    /* access modifiers changed from: package-private */
    public List<String> getSubCallIdList(int i) {
        ArrayList arrayList = new ArrayList();
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        if (callAppAbilityProxy == null) {
            HiLog.error(LOG_LABEL, "getSubCallIdList, no proxy.", new Object[0]);
            return arrayList;
        }
        Call callById = callAppAbilityProxy.getCallById(Integer.valueOf(i));
        if (callById == null) {
            HiLog.error(LOG_LABEL, "getSubCallIdList, no matched call, callId is %{public}d.", Integer.valueOf(i));
            return arrayList;
        }
        List<Call> children = callById.getChildren();
        if (children == null) {
            HiLog.error(LOG_LABEL, "getSubCallIdList, no children.", new Object[0]);
            return arrayList;
        }
        for (Call call : children) {
            Integer idByCall = this.mProxy.getIdByCall(call);
            if (idByCall == CallAppAbilityProxy.INVALID_KEY_SET_IN_MAP) {
                HiLog.error(LOG_LABEL, "getSubCallIdList, no matched child in map.", new Object[0]);
            } else {
                arrayList.add(String.valueOf(idByCall));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<String> getCallIdListForConference(int i) {
        HiLog.info(LOG_LABEL, "getCallIdListForConference, callId is %{public}d.", Integer.valueOf(i));
        ArrayList arrayList = new ArrayList();
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        if (callAppAbilityProxy == null) {
            HiLog.error(LOG_LABEL, "getCallIdListForConference, no proxy.", new Object[0]);
            return arrayList;
        }
        Call callById = callAppAbilityProxy.getCallById(Integer.valueOf(i));
        if (callById == null) {
            HiLog.error(LOG_LABEL, "getCallIdListForConference, no matched call, callId is %{public}d.", Integer.valueOf(i));
            return arrayList;
        }
        List<Call> conferenceableCalls = callById.getConferenceableCalls();
        if (conferenceableCalls == null) {
            HiLog.error(LOG_LABEL, "getCallIdListForConference, no conferenceable call.", new Object[0]);
            return arrayList;
        }
        for (Call call : conferenceableCalls) {
            Integer idByCall = this.mProxy.getIdByCall(call);
            if (idByCall == CallAppAbilityProxy.INVALID_KEY_SET_IN_MAP) {
                HiLog.error(LOG_LABEL, "getCallIdListForConference, no matched conferenceable call in map.", new Object[0]);
            } else {
                arrayList.add(String.valueOf(idByCall));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public int getMainCallId(int i) {
        int intValue = CallAppAbilityProxy.INVALID_KEY_SET_IN_MAP.intValue() + 1;
        CallAppAbilityProxy callAppAbilityProxy = this.mProxy;
        Call callById = callAppAbilityProxy != null ? callAppAbilityProxy.getCallById(Integer.valueOf(i)) : null;
        if (callById != null) {
            Call parent = callById.getParent();
            if (parent != null) {
                return this.mProxy.getIdByCall(parent).intValue();
            }
            HiLog.error(LOG_LABEL, "getMainCallId, no parent call, callId is %{public}d.", Integer.valueOf(i));
            return intValue;
        }
        HiLog.error(LOG_LABEL, "getMainCallId, no matched call, callId is %{public}d.", Integer.valueOf(i));
        return intValue;
    }

    static boolean isCaasVoipCall(Call call) {
        if (call == null) {
            return false;
        }
        boolean isCaasVoipCallNumber = isCaasVoipCallNumber(getNumber(call));
        Bundle intentExtras = getIntentExtras(call);
        if (intentExtras != null) {
            isCaasVoipCallNumber |= intentExtras.getBoolean(EXTRA_IS_HICALL);
        }
        HiLog.info(LOG_LABEL, "isCaasVoipCall: %{public}b", Boolean.valueOf(isCaasVoipCallNumber));
        return isCaasVoipCallNumber;
    }

    static boolean isConnectedBluetoothCall(Call call) {
        if (!(call == null || call.getDetails() == null)) {
            PhoneAccountHandle accountHandle = call.getDetails().getAccountHandle();
            String str = null;
            if (accountHandle != null) {
                str = accountHandle.getComponentName().flattenToShortString();
            }
            if (str != null && str.equals(BLUETOOTH_CONNECTION_SERVICE_NAME)) {
                HiLog.info(LOG_LABEL, "isConnectedBluetoothCall: true", new Object[0]);
                return true;
            }
        }
        return false;
    }

    static int getCallType(Call call) {
        if (call == null) {
            return -1;
        }
        if (isCaasVoipCall(call)) {
            HiLog.info(LOG_LABEL, "getCallType: CALL_TYPE_HICALL", new Object[0]);
            return 1;
        }
        HiLog.info(LOG_LABEL, "getCallType: CALL_TYPE_CARRIER", new Object[0]);
        return 0;
    }

    static String getNumber(Call call) {
        if (call == null) {
            return null;
        }
        if (call.getDetails() != null && call.getDetails().getGatewayInfo() != null && call.getDetails().getGatewayInfo().getOriginalAddress() != null) {
            return call.getDetails().getGatewayInfo().getOriginalAddress().getSchemeSpecificPart();
        }
        Uri handle = getHandle(call);
        if (handle == null) {
            return null;
        }
        return handle.getSchemeSpecificPart();
    }

    static boolean isCaasVoipCallNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int indexOf = str.indexOf(PREFIX_CAAS_VOIP_NUMBER_WITH_PLUS_SIGN);
        int length = str.length();
        int indexOf2 = str.indexOf(PREFIX_CAAS_VOIP_NUMBER_WITHOUT_PLUS_SIGN);
        if (indexOf != 0 && indexOf2 != 0) {
            return false;
        }
        if (length == 15 || length == 14) {
            return true;
        }
        return false;
    }

    static Uri getHandle(Call call) {
        if (call == null || call.getDetails() == null) {
            return null;
        }
        return call.getDetails().getHandle();
    }

    static Bundle getIntentExtras(Call call) {
        if (call == null || call.getDetails() == null) {
            return null;
        }
        Bundle intentExtras = call.getDetails().getIntentExtras();
        if (areCallExtrasCorrupted(intentExtras)) {
            return null;
        }
        return intentExtras;
    }

    static boolean areCallExtrasCorrupted(Bundle bundle) {
        if (bundle == null) {
            return true;
        }
        try {
            bundle.containsKey("android.telecom.extra.CHILD_ADDRESS");
            return false;
        } catch (IllegalArgumentException unused) {
            HiLog.error(LOG_LABEL, "CallExtras is corrupted, ignoring exception.", new Object[0]);
            return true;
        }
    }
}
