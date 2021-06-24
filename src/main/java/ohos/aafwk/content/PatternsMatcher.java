package ohos.aafwk.content;

import java.util.regex.Pattern;
import ohos.annotation.SystemApi;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

@SystemApi
public class PatternsMatcher implements Sequenceable {
    public static final Sequenceable.Producer<PatternsMatcher> PRODUCER = $$Lambda$PatternsMatcher$2JO6Ks_75UUJqaiLLcgzRLauqg.INSTANCE;
    private MatchType matchType;
    private String pattern;
    private Pattern patternMatcher;
    private String start = "\\*";

    static /* synthetic */ PatternsMatcher lambda$static$0(Parcel parcel) {
        PatternsMatcher patternsMatcher = new PatternsMatcher();
        patternsMatcher.unmarshalling(parcel);
        return patternsMatcher;
    }

    public enum MatchType {
        DEFAULT(0),
        PREFIX(1),
        PATTERN(2),
        GLOBAL(3);
        
        private int value;

        private MatchType(int i) {
            this.value = i;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int getValue() {
            return this.value;
        }
    }

    private PatternsMatcher() {
    }

    public PatternsMatcher(String str, MatchType matchType2) {
        this.pattern = str;
        if (matchType2 != null) {
            this.matchType = matchType2;
            if (matchType2 == MatchType.PATTERN && str != null) {
                this.patternMatcher = Pattern.compile(str);
            }
        } else if (str == null || !this.pattern.contains("*")) {
            this.matchType = MatchType.DEFAULT;
        } else {
            this.matchType = MatchType.GLOBAL;
        }
    }

    public PatternsMatcher(String str) {
        this.pattern = str;
        if (str == null || !this.pattern.contains("*")) {
            this.matchType = MatchType.DEFAULT;
        } else {
            this.matchType = MatchType.GLOBAL;
        }
    }

    public String getPattern() {
        return this.pattern;
    }

    public boolean match(String str) {
        if (str == null) {
            return this.pattern == null;
        }
        if (this.pattern == null) {
            return false;
        }
        int i = AnonymousClass1.$SwitchMap$ohos$aafwk$content$PatternsMatcher$MatchType[this.matchType.ordinal()];
        if (i == 1) {
            return this.pattern.equals(str);
        }
        if (i == 2) {
            return str.startsWith(this.pattern);
        }
        if (i == 3) {
            return this.patternMatcher.matcher(str).matches();
        }
        if (i != 4) {
            return false;
        }
        return globalMatch(str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.aafwk.content.PatternsMatcher$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$aafwk$content$PatternsMatcher$MatchType = new int[MatchType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.aafwk.content.PatternsMatcher$MatchType[] r0 = ohos.aafwk.content.PatternsMatcher.MatchType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.aafwk.content.PatternsMatcher.AnonymousClass1.$SwitchMap$ohos$aafwk$content$PatternsMatcher$MatchType = r0
                int[] r0 = ohos.aafwk.content.PatternsMatcher.AnonymousClass1.$SwitchMap$ohos$aafwk$content$PatternsMatcher$MatchType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.aafwk.content.PatternsMatcher$MatchType r1 = ohos.aafwk.content.PatternsMatcher.MatchType.DEFAULT     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.aafwk.content.PatternsMatcher.AnonymousClass1.$SwitchMap$ohos$aafwk$content$PatternsMatcher$MatchType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.aafwk.content.PatternsMatcher$MatchType r1 = ohos.aafwk.content.PatternsMatcher.MatchType.PREFIX     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.aafwk.content.PatternsMatcher.AnonymousClass1.$SwitchMap$ohos$aafwk$content$PatternsMatcher$MatchType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.aafwk.content.PatternsMatcher$MatchType r1 = ohos.aafwk.content.PatternsMatcher.MatchType.PATTERN     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.aafwk.content.PatternsMatcher.AnonymousClass1.$SwitchMap$ohos$aafwk$content$PatternsMatcher$MatchType     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.aafwk.content.PatternsMatcher$MatchType r1 = ohos.aafwk.content.PatternsMatcher.MatchType.GLOBAL     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.content.PatternsMatcher.AnonymousClass1.<clinit>():void");
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PatternsMatcher)) {
            return false;
        }
        PatternsMatcher patternsMatcher = (PatternsMatcher) obj;
        String str = this.pattern;
        if (str == null) {
            if (patternsMatcher.pattern == null && this.matchType == patternsMatcher.matchType) {
                return true;
            }
            return false;
        } else if (!str.equals(patternsMatcher.pattern) || this.matchType != patternsMatcher.matchType) {
            return false;
        } else {
            return true;
        }
    }

    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        parcel.writeString(this.pattern);
        parcel.writeInt(this.matchType.value);
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.pattern = parcel.readString();
        int readInt = parcel.readInt();
        if (readInt < MatchType.DEFAULT.getValue() || readInt > MatchType.GLOBAL.getValue()) {
            return false;
        }
        this.matchType = MatchType.values()[readInt];
        if (this.matchType != MatchType.PATTERN) {
            return true;
        }
        this.patternMatcher = Pattern.compile(this.pattern);
        return true;
    }

    private boolean globalMatch(String str) {
        String str2 = this.pattern;
        if (str2 == null) {
            return str == null;
        }
        if (str == null) {
            return str2 == null;
        }
        String[] split = str2.split(this.start);
        int i = 0;
        for (String str3 : split) {
            if (!str3.isEmpty()) {
                int indexOf = str.indexOf(str3);
                if (indexOf == -1) {
                    return false;
                }
                i = indexOf + str3.length();
            }
        }
        return str.length() <= i || this.pattern.endsWith("*");
    }
}
