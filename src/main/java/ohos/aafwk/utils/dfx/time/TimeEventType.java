package ohos.aafwk.utils.dfx.time;

public enum TimeEventType {
    LIFECYCLE_START,
    LIFECYCLE_ACTIVE,
    LIFECYCLE_INACTIVE,
    LIFECYCLE_BACKGROUND,
    LIFECYCLE_FOREGROUND,
    LIFECYCLE_STOP,
    LIFECYCLE(20),
    LOAD(5),
    ANONYMOUS(1),
    WINDOW_LOAD,
    WINDOW_SHOW,
    WINDOW_CREATE,
    WINDOW_DESTROY,
    WINDOW_SET_UI(5),
    EVENT(5);
    
    private int size;

    private TimeEventType(int i) {
        this.size = i;
    }

    private TimeEventType() {
        this.size = 0;
    }

    /* access modifiers changed from: package-private */
    public int getSize() {
        return this.size;
    }

    /* access modifiers changed from: package-private */
    public void setSize(int i) {
        this.size = i;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.aafwk.utils.dfx.time.TimeEventType$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$aafwk$utils$dfx$time$TimeEventType = new int[TimeEventType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                ohos.aafwk.utils.dfx.time.TimeEventType[] r0 = ohos.aafwk.utils.dfx.time.TimeEventType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.aafwk.utils.dfx.time.TimeEventType.AnonymousClass1.$SwitchMap$ohos$aafwk$utils$dfx$time$TimeEventType = r0
                int[] r0 = ohos.aafwk.utils.dfx.time.TimeEventType.AnonymousClass1.$SwitchMap$ohos$aafwk$utils$dfx$time$TimeEventType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.aafwk.utils.dfx.time.TimeEventType r1 = ohos.aafwk.utils.dfx.time.TimeEventType.LIFECYCLE     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.aafwk.utils.dfx.time.TimeEventType.AnonymousClass1.$SwitchMap$ohos$aafwk$utils$dfx$time$TimeEventType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.aafwk.utils.dfx.time.TimeEventType r1 = ohos.aafwk.utils.dfx.time.TimeEventType.LOAD     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.utils.dfx.time.TimeEventType.AnonymousClass1.<clinit>():void");
        }
    }

    static TimeEventType[] getSubEvents(TimeEventType timeEventType) {
        int i = AnonymousClass1.$SwitchMap$ohos$aafwk$utils$dfx$time$TimeEventType[timeEventType.ordinal()];
        if (i == 1) {
            return new TimeEventType[]{LIFECYCLE_START, LIFECYCLE_ACTIVE, LIFECYCLE_INACTIVE, LIFECYCLE_BACKGROUND, LIFECYCLE_FOREGROUND, LIFECYCLE_STOP};
        } else if (i != 2) {
            return new TimeEventType[0];
        } else {
            return new TimeEventType[]{LOAD};
        }
    }
}
