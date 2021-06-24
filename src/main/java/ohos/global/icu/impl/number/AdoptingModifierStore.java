package ohos.global.icu.impl.number;

import ohos.global.icu.impl.StandardPlural;
import ohos.global.icu.impl.number.Modifier;

public class AdoptingModifierStore implements ModifierStore {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    boolean frozen;
    final Modifier[] mods;
    private final Modifier negZero;
    private final Modifier negative;
    private final Modifier posZero;
    private final Modifier positive;

    public AdoptingModifierStore(Modifier modifier, Modifier modifier2, Modifier modifier3, Modifier modifier4) {
        this.positive = modifier;
        this.posZero = modifier2;
        this.negZero = modifier3;
        this.negative = modifier4;
        this.mods = null;
        this.frozen = true;
    }

    public AdoptingModifierStore() {
        this.positive = null;
        this.posZero = null;
        this.negZero = null;
        this.negative = null;
        this.mods = new Modifier[(StandardPlural.COUNT * 4)];
        this.frozen = false;
    }

    public void setModifier(Modifier.Signum signum, StandardPlural standardPlural, Modifier modifier) {
        this.mods[getModIndex(signum, standardPlural)] = modifier;
    }

    public void freeze() {
        this.frozen = true;
    }

    /* renamed from: ohos.global.icu.impl.number.AdoptingModifierStore$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$impl$number$Modifier$Signum = new int[Modifier.Signum.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.global.icu.impl.number.Modifier$Signum[] r0 = ohos.global.icu.impl.number.Modifier.Signum.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.impl.number.AdoptingModifierStore.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum = r0
                int[] r0 = ohos.global.icu.impl.number.AdoptingModifierStore.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.impl.number.Modifier$Signum r1 = ohos.global.icu.impl.number.Modifier.Signum.POS     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.impl.number.AdoptingModifierStore.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.impl.number.Modifier$Signum r1 = ohos.global.icu.impl.number.Modifier.Signum.POS_ZERO     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.impl.number.AdoptingModifierStore.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.impl.number.Modifier$Signum r1 = ohos.global.icu.impl.number.Modifier.Signum.NEG_ZERO     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.global.icu.impl.number.AdoptingModifierStore.AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.global.icu.impl.number.Modifier$Signum r1 = ohos.global.icu.impl.number.Modifier.Signum.NEG     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.number.AdoptingModifierStore.AnonymousClass1.<clinit>():void");
        }
    }

    public Modifier getModifierWithoutPlural(Modifier.Signum signum) {
        int i = AnonymousClass1.$SwitchMap$ohos$global$icu$impl$number$Modifier$Signum[signum.ordinal()];
        if (i == 1) {
            return this.positive;
        }
        if (i == 2) {
            return this.posZero;
        }
        if (i == 3) {
            return this.negZero;
        }
        if (i == 4) {
            return this.negative;
        }
        throw new AssertionError("Unreachable");
    }

    @Override // ohos.global.icu.impl.number.ModifierStore
    public Modifier getModifier(Modifier.Signum signum, StandardPlural standardPlural) {
        return this.mods[getModIndex(signum, standardPlural)];
    }

    private static int getModIndex(Modifier.Signum signum, StandardPlural standardPlural) {
        return (standardPlural.ordinal() * Modifier.Signum.COUNT) + signum.ordinal();
    }
}
