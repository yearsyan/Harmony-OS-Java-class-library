package ohos.utils.fastjson;

import ohos.com.sun.org.apache.xml.internal.utils.LocaleUtility;

public enum PropertyNamingStrategy {
    CamelCase,
    PascalCase,
    SnakeCase,
    KebabCase;

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.utils.fastjson.PropertyNamingStrategy$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$utils$fastjson$PropertyNamingStrategy = new int[PropertyNamingStrategy.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                ohos.utils.fastjson.PropertyNamingStrategy[] r0 = ohos.utils.fastjson.PropertyNamingStrategy.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.utils.fastjson.PropertyNamingStrategy.AnonymousClass1.$SwitchMap$ohos$utils$fastjson$PropertyNamingStrategy = r0
                int[] r0 = ohos.utils.fastjson.PropertyNamingStrategy.AnonymousClass1.$SwitchMap$ohos$utils$fastjson$PropertyNamingStrategy     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.utils.fastjson.PropertyNamingStrategy r1 = ohos.utils.fastjson.PropertyNamingStrategy.SnakeCase     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.utils.fastjson.PropertyNamingStrategy.AnonymousClass1.$SwitchMap$ohos$utils$fastjson$PropertyNamingStrategy     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.utils.fastjson.PropertyNamingStrategy r1 = ohos.utils.fastjson.PropertyNamingStrategy.KebabCase     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.utils.fastjson.PropertyNamingStrategy.AnonymousClass1.$SwitchMap$ohos$utils$fastjson$PropertyNamingStrategy     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.utils.fastjson.PropertyNamingStrategy r1 = ohos.utils.fastjson.PropertyNamingStrategy.PascalCase     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = ohos.utils.fastjson.PropertyNamingStrategy.AnonymousClass1.$SwitchMap$ohos$utils$fastjson$PropertyNamingStrategy     // Catch:{ NoSuchFieldError -> 0x0035 }
                ohos.utils.fastjson.PropertyNamingStrategy r1 = ohos.utils.fastjson.PropertyNamingStrategy.CamelCase     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.PropertyNamingStrategy.AnonymousClass1.<clinit>():void");
        }
    }

    public String translate(String str) {
        char charAt;
        int i = AnonymousClass1.$SwitchMap$ohos$utils$fastjson$PropertyNamingStrategy[ordinal()];
        int i2 = 0;
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            while (i2 < str.length()) {
                char charAt2 = str.charAt(i2);
                if (charAt2 < 'A' || charAt2 > 'Z') {
                    sb.append(charAt2);
                } else {
                    char c = (char) (charAt2 + ' ');
                    if (i2 > 0) {
                        sb.append('_');
                    }
                    sb.append(c);
                }
                i2++;
            }
            return sb.toString();
        } else if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            while (i2 < str.length()) {
                char charAt3 = str.charAt(i2);
                if (charAt3 < 'A' || charAt3 > 'Z') {
                    sb2.append(charAt3);
                } else {
                    char c2 = (char) (charAt3 + ' ');
                    if (i2 > 0) {
                        sb2.append(LocaleUtility.IETF_SEPARATOR);
                    }
                    sb2.append(c2);
                }
                i2++;
            }
            return sb2.toString();
        } else if (i == 3) {
            char charAt4 = str.charAt(0);
            if (charAt4 < 'a' || charAt4 > 'z') {
                return str;
            }
            char[] charArray = str.toCharArray();
            charArray[0] = (char) (charArray[0] - ' ');
            return new String(charArray);
        } else if (i != 4 || (charAt = str.charAt(0)) < 'A' || charAt > 'Z') {
            return str;
        } else {
            char[] charArray2 = str.toCharArray();
            charArray2[0] = (char) (charArray2[0] + ' ');
            return new String(charArray2);
        }
    }
}
