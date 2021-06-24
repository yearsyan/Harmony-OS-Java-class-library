package ohos.event.intentagent;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.app.Context;
import ohos.bundle.AbilityInfo;
import ohos.bundle.ElementName;
import ohos.bundle.ShellInfo;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentAdapter;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.event.notification.EventConstant;
import ohos.event.notification.IntentConverter;
import ohos.eventhandler.EventHandler;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class IntentAgentAdapter {
    private static final String ABILITY_SHELL_SUFFIX = "ShellActivity";
    private static final int ERROR_FLAG = 0;
    private static final int ERROR_VALUE = -1;
    private static final IntentAgentAdapter INSTANCE = new IntentAgentAdapter();
    private static final int INTENT_SIZE = 1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, EventConstant.INTENTAGENT_DOMAIN, TAG);
    private static final int MAX_INTENT_NUM = 3;
    private static final String SERVICE_SHELL_SUFFIX = "ShellService";
    private static final String STR_POINT = ".";
    private static final String TAG = "IntentAgentAdapter";

    private IntentAgentAdapter() {
    }

    public static IntentAgentAdapter getInstance() {
        return INSTANCE;
    }

    public Optional<IntentAgent> getIntentAgent(Context context, IntentAgentInfo intentAgentInfo) {
        Optional<PendingIntent> optional;
        if (context == null || intentAgentInfo == null) {
            HiLog.error(LABEL, "IntentAgentAdapter::getIntentAgent invalid input param", new Object[0]);
            return Optional.empty();
        }
        Optional<android.content.Context> aospContext = getAospContext(context);
        if (!aospContext.isPresent()) {
            return Optional.empty();
        }
        android.content.Context context2 = aospContext.get();
        int flagsTransformer = flagsTransformer(intentAgentInfo.getFlags());
        if (flagsTransformer == 0) {
            return Optional.empty();
        }
        int operationType = intentAgentInfo.getOperationType();
        IntentParams extraInfo = intentAgentInfo.getExtraInfo();
        List<Intent> intents = intentAgentInfo.getIntents();
        Optional.empty();
        int requestCode = intentAgentInfo.getRequestCode();
        if (operationType == IntentAgentConstant.OperationType.START_ABILITY.ordinal()) {
            optional = getAbility(context2, flagsTransformer, intents, requestCode, extraInfo);
        } else if (operationType == IntentAgentConstant.OperationType.START_ABILITIES.ordinal()) {
            optional = getAbilities(context2, flagsTransformer, intents, requestCode, extraInfo);
        } else if (operationType == IntentAgentConstant.OperationType.START_SERVICE.ordinal()) {
            optional = getService(context2, flagsTransformer, intents, requestCode);
        } else if (operationType == IntentAgentConstant.OperationType.SEND_COMMON_EVENT.ordinal()) {
            optional = getCommonEvent(context2, flagsTransformer, intents, requestCode);
        } else if (operationType == IntentAgentConstant.OperationType.START_FOREGROUND_SERVICE.ordinal()) {
            optional = getForegroundService(context2, flagsTransformer, intents, requestCode);
        } else {
            HiLog.error(LABEL, "IntentAgentAdapter::getIntentAgent operation type is error.", new Object[0]);
            return Optional.empty();
        }
        if (optional.isPresent()) {
            return Optional.of(new IntentAgent(optional.get()));
        }
        HiLog.error(LABEL, "IntentAgentAdapter::getIntentAgent the intents does not meet the requirements.", new Object[0]);
        return Optional.empty();
    }

    public void triggerIntentAgent(Context context, IntentAgent intentAgent, IntentAgent.OnCompleted onCompleted, EventHandler eventHandler, TriggerInfo triggerInfo) {
        if (context == null || intentAgent == null) {
            HiLog.error(LABEL, "IntentAgentAdapter::triggerIntentAgent invalid input param", new Object[0]);
            return;
        }
        Optional<android.content.Context> aospContext = getAospContext(context);
        if (!aospContext.isPresent()) {
            HiLog.error(LABEL, "IntentAgentAdapter::triggerIntentAgent get aosp context failed", new Object[0]);
            return;
        }
        Object object = intentAgent.getObject();
        if (object instanceof PendingIntent) {
            PendingIntent pendingIntent = (PendingIntent) object;
            IntentAgentConstant.OperationType type = getType(pendingIntent);
            CompletedDispatcher completedDispatcher = null;
            if (onCompleted != null) {
                completedDispatcher = new CompletedDispatcher(onCompleted, eventHandler, intentAgent, type);
            }
            send(aospContext.get(), pendingIntent, type, completedDispatcher, triggerInfo);
        }
    }

    public boolean judgeEquality(IntentAgent intentAgent, IntentAgent intentAgent2) {
        if (intentAgent == null && intentAgent2 == null) {
            return true;
        }
        if (intentAgent == null || intentAgent2 == null) {
            return false;
        }
        Object object = intentAgent.getObject();
        if (!(object instanceof PendingIntent)) {
            return false;
        }
        PendingIntent pendingIntent = (PendingIntent) object;
        Object object2 = intentAgent2.getObject();
        if (!(object2 instanceof PendingIntent)) {
            return false;
        }
        return pendingIntent.equals((PendingIntent) object2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void send(android.content.Context r11, android.app.PendingIntent r12, ohos.event.intentagent.IntentAgentConstant.OperationType r13, ohos.event.intentagent.IntentAgentAdapter.CompletedDispatcher r14, ohos.event.intentagent.TriggerInfo r15) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.event.intentagent.IntentAgentAdapter.send(android.content.Context, android.app.PendingIntent, ohos.event.intentagent.IntentAgentConstant$OperationType, ohos.event.intentagent.IntentAgentAdapter$CompletedDispatcher, ohos.event.intentagent.TriggerInfo):void");
    }

    /* access modifiers changed from: package-private */
    public IntentAgentConstant.OperationType getType(PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            return IntentAgentConstant.OperationType.UNKNOWN_TYPE;
        }
        if (pendingIntent.isActivity()) {
            return IntentAgentConstant.OperationType.START_ABILITY;
        }
        if (pendingIntent.isBroadcast()) {
            return IntentAgentConstant.OperationType.SEND_COMMON_EVENT;
        }
        if (pendingIntent.isForegroundService()) {
            return IntentAgentConstant.OperationType.START_FOREGROUND_SERVICE;
        }
        if (pendingIntent.isActivity() || pendingIntent.isBroadcast() || pendingIntent.isForegroundService()) {
            return IntentAgentConstant.OperationType.UNKNOWN_TYPE;
        }
        return IntentAgentConstant.OperationType.START_SERVICE;
    }

    private Optional<PendingIntent> getAbility(android.content.Context context, int i, List<Intent> list, int i2, IntentParams intentParams) {
        if (!isIntentMatch(list, IntentAgentConstant.OperationType.START_ABILITY)) {
            HiLog.error(LABEL, "IntentAgentAdapter::getAbility intents is not match.", new Object[0]);
            return Optional.empty();
        }
        Optional<android.content.Intent> convertZosIntentToAosIntent = convertZosIntentToAosIntent(list, IntentAgentConstant.OperationType.START_ABILITY);
        if (!convertZosIntentToAosIntent.isPresent()) {
            return Optional.empty();
        }
        convertZosIntentToAosIntent.get().addFlags(268435456);
        Bundle bundle = null;
        if (intentParams != null) {
            Optional<Bundle> convertIntentParamsToBundle = IntentConverter.convertIntentParamsToBundle(intentParams.getParams());
            if (convertIntentParamsToBundle.isPresent()) {
                bundle = convertIntentParamsToBundle.get();
            }
        }
        PendingIntent activity = PendingIntent.getActivity(context, i2, convertZosIntentToAosIntent.get(), i, bundle);
        if (activity == null) {
            return Optional.empty();
        }
        return Optional.of(activity);
    }

    private Optional<PendingIntent> getAbilities(android.content.Context context, int i, List<Intent> list, int i2, IntentParams intentParams) {
        if (!isIntentMatch(list, IntentAgentConstant.OperationType.START_ABILITIES)) {
            HiLog.error(LABEL, "IntentAgentAdapter::getAbilities intents is not match.", new Object[0]);
            return Optional.empty();
        }
        ArrayList arrayList = new ArrayList();
        for (Intent intent : list) {
            getAospIntent(intent, IntentAgentConstant.OperationType.START_ABILITIES).ifPresent(new Consumer(arrayList) {
                /* class ohos.event.intentagent.$$Lambda$oHuon0sxg27MKIHlOHNqTBxHQR4 */
                private final /* synthetic */ List f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.add((android.content.Intent) obj);
                }
            });
            if (arrayList.size() >= 3) {
                break;
            }
        }
        if (arrayList.isEmpty()) {
            return Optional.empty();
        }
        android.content.Intent[] intentArr = new android.content.Intent[arrayList.size()];
        arrayList.toArray(intentArr);
        Bundle bundle = null;
        if (intentParams != null) {
            Optional<Bundle> convertIntentParamsToBundle = IntentConverter.convertIntentParamsToBundle(intentParams.getParams());
            if (convertIntentParamsToBundle.isPresent()) {
                bundle = convertIntentParamsToBundle.get();
            }
        }
        PendingIntent activities = PendingIntent.getActivities(context, i2, intentArr, i, bundle);
        if (activities == null) {
            return Optional.empty();
        }
        return Optional.of(activities);
    }

    private Optional<PendingIntent> getService(android.content.Context context, int i, List<Intent> list, int i2) {
        if (!isIntentMatch(list, IntentAgentConstant.OperationType.START_SERVICE)) {
            HiLog.error(LABEL, "IntentAgentAdapter::getService intents is not match.", new Object[0]);
            return Optional.empty();
        }
        Optional<android.content.Intent> convertZosIntentToAosIntent = convertZosIntentToAosIntent(list, IntentAgentConstant.OperationType.START_SERVICE);
        if (!convertZosIntentToAosIntent.isPresent()) {
            return Optional.empty();
        }
        PendingIntent service = PendingIntent.getService(context, i2, convertZosIntentToAosIntent.get(), i);
        if (service == null) {
            return Optional.empty();
        }
        return Optional.of(service);
    }

    private Optional<PendingIntent> getForegroundService(android.content.Context context, int i, List<Intent> list, int i2) {
        if (!isIntentMatch(list, IntentAgentConstant.OperationType.START_FOREGROUND_SERVICE)) {
            HiLog.error(LABEL, "IntentAgentAdapter::getForegroundService intents is not match.", new Object[0]);
            return Optional.empty();
        }
        Optional<android.content.Intent> convertZosIntentToAosIntent = convertZosIntentToAosIntent(list, IntentAgentConstant.OperationType.START_FOREGROUND_SERVICE);
        if (!convertZosIntentToAosIntent.isPresent()) {
            return Optional.empty();
        }
        PendingIntent foregroundService = PendingIntent.getForegroundService(context, i2, convertZosIntentToAosIntent.get(), i);
        if (foregroundService == null) {
            return Optional.empty();
        }
        return Optional.of(foregroundService);
    }

    private Optional<PendingIntent> getCommonEvent(android.content.Context context, int i, List<Intent> list, int i2) {
        if (!isIntentMatch(list, IntentAgentConstant.OperationType.SEND_COMMON_EVENT)) {
            HiLog.error(LABEL, "IntentAgentAdapter::getCommonEvent intents is not match.", new Object[0]);
            return Optional.empty();
        }
        Optional<android.content.Intent> convertZosIntentToAosIntent = convertZosIntentToAosIntent(list, IntentAgentConstant.OperationType.SEND_COMMON_EVENT);
        if (!convertZosIntentToAosIntent.isPresent()) {
            return Optional.empty();
        }
        PendingIntent broadcast = PendingIntent.getBroadcast(context, i2, convertZosIntentToAosIntent.get(), i);
        if (broadcast == null) {
            return Optional.empty();
        }
        return Optional.of(broadcast);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.event.intentagent.IntentAgentAdapter$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType = new int[IntentAgentConstant.OperationType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                ohos.event.intentagent.IntentAgentConstant$OperationType[] r0 = ohos.event.intentagent.IntentAgentConstant.OperationType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.event.intentagent.IntentAgentAdapter.AnonymousClass1.$SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType = r0
                int[] r0 = ohos.event.intentagent.IntentAgentAdapter.AnonymousClass1.$SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.event.intentagent.IntentAgentConstant$OperationType r1 = ohos.event.intentagent.IntentAgentConstant.OperationType.START_ABILITY     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.event.intentagent.IntentAgentAdapter.AnonymousClass1.$SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.event.intentagent.IntentAgentConstant$OperationType r1 = ohos.event.intentagent.IntentAgentConstant.OperationType.START_SERVICE     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.event.intentagent.IntentAgentAdapter.AnonymousClass1.$SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.event.intentagent.IntentAgentConstant$OperationType r1 = ohos.event.intentagent.IntentAgentConstant.OperationType.SEND_COMMON_EVENT     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.event.intentagent.IntentAgentAdapter.AnonymousClass1.$SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.event.intentagent.IntentAgentConstant$OperationType r1 = ohos.event.intentagent.IntentAgentConstant.OperationType.START_FOREGROUND_SERVICE     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = ohos.event.intentagent.IntentAgentAdapter.AnonymousClass1.$SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType     // Catch:{ NoSuchFieldError -> 0x0040 }
                ohos.event.intentagent.IntentAgentConstant$OperationType r1 = ohos.event.intentagent.IntentAgentConstant.OperationType.START_ABILITIES     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.event.intentagent.IntentAgentAdapter.AnonymousClass1.<clinit>():void");
        }
    }

    private boolean isIntentMatch(List<Intent> list, IntentAgentConstant.OperationType operationType) {
        if (list == null) {
            return false;
        }
        int i = AnonymousClass1.$SwitchMap$ohos$event$intentagent$IntentAgentConstant$OperationType[operationType.ordinal()];
        if (i == 1 || i == 2 || i == 3 || i == 4) {
            return list.size() == 1;
        }
        if (i != 5) {
            return false;
        }
        return !list.isEmpty();
    }

    private Optional<android.content.Intent> convertZosIntentToAosIntent(List<Intent> list, IntentAgentConstant.OperationType operationType) {
        Optional<android.content.Intent> empty = Optional.empty();
        for (Intent intent : list) {
            empty = getAospIntent(intent, operationType);
            if (!empty.isPresent()) {
                HiLog.error(LABEL, "IntentAgentAdapter::Error translating zIntent to aIntent.", new Object[0]);
                return Optional.empty();
            }
        }
        return empty;
    }

    private static int flagsTransformer(List<IntentAgentConstant.Flags> list) {
        int i;
        if (list == null) {
            return 0;
        }
        int i2 = 0;
        for (IntentAgentConstant.Flags flags : list) {
            if (flags == IntentAgentConstant.Flags.ONE_TIME_FLAG) {
                i = 1073741824;
            } else if (flags == IntentAgentConstant.Flags.NO_BUILD_FLAG) {
                i = 536870912;
            } else if (flags == IntentAgentConstant.Flags.CANCEL_PRESENT_FLAG) {
                i = 268435456;
            } else if (flags == IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG) {
                i = 134217728;
            } else if (flags == IntentAgentConstant.Flags.CONSTANT_FLAG) {
                i = 67108864;
            } else if (flags == IntentAgentConstant.Flags.REPLACE_ELEMENT) {
                i2 |= 8;
            } else if (flags == IntentAgentConstant.Flags.REPLACE_ACTION) {
                i2 |= 1;
            } else if (flags == IntentAgentConstant.Flags.REPLACE_URI) {
                i2 |= 2;
            } else if (flags == IntentAgentConstant.Flags.REPLACE_ENTITIES) {
                i2 |= 4;
            } else if (flags == IntentAgentConstant.Flags.REPLACE_BUNDLE) {
                i2 |= 16;
            } else {
                HiLog.error(LABEL, "IntentAgentAdapter::flag is error.", new Object[0]);
            }
            i2 |= i;
        }
        return i2;
    }

    private static Optional<android.content.Intent> getAospIntent(Intent intent, IntentAgentConstant.OperationType operationType) {
        if (intent == null) {
            HiLog.error(LABEL, "IntentAgentAdapter::zIntent is null.", new Object[0]);
            return Optional.empty();
        } else if (operationType == IntentAgentConstant.OperationType.SEND_COMMON_EVENT) {
            return IntentConverter.createAndroidIntent(intent, null);
        } else {
            ElementName element = intent.getElement();
            if (element != null) {
                String bundleName = element.getBundleName();
                String abilityName = element.getAbilityName();
                if (bundleName == null || abilityName == null) {
                    HiLog.error(LABEL, "IntentAgentAdapter::package name or class name are null.", new Object[0]);
                    return Optional.empty();
                } else if ((intent.getFlags() & 16) == 0) {
                    return IntentConverter.createAndroidIntent(intent, getShellInfo(bundleName, abilityName, operationType));
                } else {
                    HiLog.info(LABEL, "IntentAgentAdapter::set shellInfo null with FLAG_NOT_OHOS_COMPONENT.", new Object[0]);
                    return IntentConverter.createAndroidIntent(intent, null);
                }
            } else if ((intent.getFlags() & 16) == 0 || !(operationType == IntentAgentConstant.OperationType.START_ABILITIES || operationType == IntentAgentConstant.OperationType.START_ABILITY)) {
                HiLog.error(LABEL, "IntentAgentAdapter::Can't get element name from zIntent.", new Object[0]);
                return Optional.empty();
            } else {
                HiLog.info(LABEL, "IntentAgentAdapter::cerate with elementName is null, type is START_ABILITY.", new Object[0]);
                return IntentConverter.createAndroidIntent(intent, null);
            }
        }
    }

    private static ShellInfo getShellInfo(String str, String str2, IntentAgentConstant.OperationType operationType) {
        ShellInfo shellInfo = new ShellInfo();
        shellInfo.setPackageName(str);
        if (operationType == IntentAgentConstant.OperationType.START_ABILITY || operationType == IntentAgentConstant.OperationType.START_ABILITIES) {
            shellInfo.setType(ShellInfo.ShellType.ACTIVITY);
            shellInfo.setName(str2 + "ShellActivity");
        } else if (operationType == IntentAgentConstant.OperationType.START_SERVICE || operationType == IntentAgentConstant.OperationType.START_FOREGROUND_SERVICE) {
            shellInfo.setType(ShellInfo.ShellType.SERVICE);
            shellInfo.setName(str2 + "ShellService");
        } else {
            shellInfo.setType(ShellInfo.ShellType.UNKNOWN);
            shellInfo.setName(str2);
        }
        return shellInfo;
    }

    private static Optional<android.content.Context> getAospContext(Context context) {
        Object hostContext = context.getHostContext();
        if (hostContext instanceof android.content.Context) {
            return Optional.of((android.content.Context) hostContext);
        }
        HiLog.error(LABEL, "IntentAgentAdapter::Can not convert zContext to aContext.", new Object[0]);
        return Optional.empty();
    }

    static Optional<Intent> getZidaneIntent(android.content.Intent intent, IntentAgentConstant.OperationType operationType) {
        if (intent == null) {
            return Optional.empty();
        }
        if (operationType == IntentAgentConstant.OperationType.SEND_COMMON_EVENT) {
            return IntentConverter.createZidaneIntent(intent, null);
        }
        ComponentName component = intent.getComponent();
        if (component == null) {
            return Optional.empty();
        }
        String packageName = component.getPackageName();
        String shortClassName = component.getShortClassName();
        if (packageName == null || shortClassName == null) {
            return Optional.empty();
        }
        int lastIndexOf = shortClassName.lastIndexOf(".");
        if (lastIndexOf != -1) {
            shortClassName = shortClassName.substring(lastIndexOf + 1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(packageName);
        sb.append(".");
        AbilityInfo abilityInfo = new AbilityInfo();
        abilityInfo.setBundleName(packageName);
        if (operationType == IntentAgentConstant.OperationType.START_ABILITY || operationType == IntentAgentConstant.OperationType.START_ABILITIES) {
            abilityInfo.setType(AbilityInfo.AbilityType.PAGE);
            int lastIndexOf2 = shortClassName.lastIndexOf("ShellActivity");
            if (lastIndexOf2 == -1) {
                sb.append(shortClassName);
            } else {
                sb.append(shortClassName.substring(0, lastIndexOf2));
            }
        } else if (operationType == IntentAgentConstant.OperationType.START_SERVICE || operationType == IntentAgentConstant.OperationType.START_FOREGROUND_SERVICE) {
            abilityInfo.setType(AbilityInfo.AbilityType.SERVICE);
            int lastIndexOf3 = shortClassName.lastIndexOf("ShellService");
            if (lastIndexOf3 == -1) {
                sb.append(shortClassName);
            } else {
                sb.append(shortClassName.substring(0, lastIndexOf3));
            }
        } else {
            abilityInfo.setType(AbilityInfo.AbilityType.UNKNOWN);
            sb.append(shortClassName);
        }
        abilityInfo.setClassName(sb.toString());
        return IntentConverter.createZidaneIntent(intent, abilityInfo);
    }

    /* access modifiers changed from: private */
    public static class CompletedDispatcher implements PendingIntent.OnFinished, Runnable {
        private IntentParams extraInfo = null;
        private final EventHandler handler;
        private final IntentAgent intentAgent;
        private final IntentAgent.OnCompleted onCompleted;
        private int resultCode;
        private String resultData = null;
        private final IntentAgentConstant.OperationType type;
        private Intent zIntent = null;

        public CompletedDispatcher(IntentAgent.OnCompleted onCompleted2, EventHandler eventHandler, IntentAgent intentAgent2, IntentAgentConstant.OperationType operationType) {
            this.onCompleted = onCompleted2;
            this.handler = eventHandler;
            this.intentAgent = intentAgent2;
            this.type = operationType;
        }

        public void onSendFinished(PendingIntent pendingIntent, android.content.Intent intent, int i, String str, Bundle bundle) {
            this.resultCode = i;
            this.resultData = str;
            IntentAgentAdapter.getZidaneIntent(intent, this.type).ifPresent(new Consumer() {
                /* class ohos.event.intentagent.$$Lambda$IntentAgentAdapter$CompletedDispatcher$YaundkysgpmaEmC7pvMq2insw */

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    IntentAgentAdapter.CompletedDispatcher.this.lambda$onSendFinished$0$IntentAgentAdapter$CompletedDispatcher((Intent) obj);
                }
            });
            setIntentParam(bundle);
            EventHandler eventHandler = this.handler;
            if (eventHandler == null) {
                IntentAgent.OnCompleted onCompleted2 = this.onCompleted;
                if (onCompleted2 != null) {
                    onCompleted2.onSendCompleted(this.intentAgent, this.zIntent, this.resultCode, this.resultData, this.extraInfo);
                    return;
                }
                return;
            }
            eventHandler.postTask(this);
        }

        public /* synthetic */ void lambda$onSendFinished$0$IntentAgentAdapter$CompletedDispatcher(Intent intent) {
            this.zIntent = intent;
        }

        public void run() {
            IntentAgent.OnCompleted onCompleted2 = this.onCompleted;
            if (onCompleted2 != null) {
                onCompleted2.onSendCompleted(this.intentAgent, this.zIntent, this.resultCode, this.resultData, this.extraInfo);
            }
        }

        private void setIntentParam(Bundle bundle) {
            Set<String> keySet;
            if (!(bundle == null || (keySet = bundle.keySet()) == null)) {
                this.extraInfo = new IntentParams();
                for (String str : keySet) {
                    if (str != null) {
                        this.extraInfo.setParam(str, bundle.get(str));
                    }
                }
            }
        }
    }
}
