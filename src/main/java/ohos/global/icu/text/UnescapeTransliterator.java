package ohos.global.icu.text;

import ohos.global.icu.impl.PatternTokenizer;
import ohos.global.icu.impl.Utility;
import ohos.global.icu.text.Transliterator;
import ohos.global.icu.util.ULocale;
import ohos.telephony.TelephoneNumberUtils;

class UnescapeTransliterator extends Transliterator {
    private static final char END = 65535;
    private char[] spec;

    static void register() {
        Transliterator.registerFactory("Hex-Any/Unicode", new Transliterator.Factory() {
            /* class ohos.global.icu.text.UnescapeTransliterator.AnonymousClass1 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new UnescapeTransliterator("Hex-Any/Unicode", new char[]{2, 0, 16, 4, 6, 'U', '+', 65535});
            }
        });
        Transliterator.registerFactory("Hex-Any/Java", new Transliterator.Factory() {
            /* class ohos.global.icu.text.UnescapeTransliterator.AnonymousClass2 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new UnescapeTransliterator("Hex-Any/Java", new char[]{2, 0, 16, 4, 4, PatternTokenizer.BACK_SLASH, 'u', 65535});
            }
        });
        Transliterator.registerFactory("Hex-Any/C", new Transliterator.Factory() {
            /* class ohos.global.icu.text.UnescapeTransliterator.AnonymousClass3 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new UnescapeTransliterator("Hex-Any/C", new char[]{2, 0, 16, 4, 4, PatternTokenizer.BACK_SLASH, 'u', 2, 0, 16, '\b', '\b', PatternTokenizer.BACK_SLASH, 'U', 65535});
            }
        });
        Transliterator.registerFactory("Hex-Any/XML", new Transliterator.Factory() {
            /* class ohos.global.icu.text.UnescapeTransliterator.AnonymousClass4 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new UnescapeTransliterator("Hex-Any/XML", new char[]{3, 1, 16, 1, 6, '&', '#', ULocale.PRIVATE_USE_EXTENSION, TelephoneNumberUtils.WAIT, 65535});
            }
        });
        Transliterator.registerFactory("Hex-Any/XML10", new Transliterator.Factory() {
            /* class ohos.global.icu.text.UnescapeTransliterator.AnonymousClass5 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new UnescapeTransliterator("Hex-Any/XML10", new char[]{2, 1, '\n', 1, 7, '&', '#', TelephoneNumberUtils.WAIT, 65535});
            }
        });
        Transliterator.registerFactory("Hex-Any/Perl", new Transliterator.Factory() {
            /* class ohos.global.icu.text.UnescapeTransliterator.AnonymousClass6 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new UnescapeTransliterator("Hex-Any/Perl", new char[]{3, 1, 16, 1, 6, PatternTokenizer.BACK_SLASH, ULocale.PRIVATE_USE_EXTENSION, '{', '}', 65535});
            }
        });
        Transliterator.registerFactory("Hex-Any", new Transliterator.Factory() {
            /* class ohos.global.icu.text.UnescapeTransliterator.AnonymousClass7 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new UnescapeTransliterator("Hex-Any", new char[]{2, 0, 16, 4, 6, 'U', '+', 2, 0, 16, 4, 4, PatternTokenizer.BACK_SLASH, 'u', 2, 0, 16, '\b', '\b', PatternTokenizer.BACK_SLASH, 'U', 3, 1, 16, 1, 6, '&', '#', ULocale.PRIVATE_USE_EXTENSION, TelephoneNumberUtils.WAIT, 2, 1, '\n', 1, 7, '&', '#', TelephoneNumberUtils.WAIT, 3, 1, 16, 1, 6, PatternTokenizer.BACK_SLASH, ULocale.PRIVATE_USE_EXTENSION, '{', '}', 65535});
            }
        });
    }

    UnescapeTransliterator(String str, char[] cArr) {
        super(str, null);
        this.spec = cArr;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00aa, code lost:
        if (r3 >= r4) goto L_0x000a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ac, code lost:
        r3 = r3 + ohos.global.icu.text.UTF16.getCharCount(r18.char32At(r3));
     */
    @Override // ohos.global.icu.text.Transliterator
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleTransliterate(ohos.global.icu.text.Replaceable r18, ohos.global.icu.text.Transliterator.Position r19, boolean r20) {
        /*
        // Method dump skipped, instructions count: 197
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.UnescapeTransliterator.handleTransliterate(ohos.global.icu.text.Replaceable, ohos.global.icu.text.Transliterator$Position, boolean):void");
    }

    @Override // ohos.global.icu.text.Transliterator
    public void addSourceTargetSet(UnicodeSet unicodeSet, UnicodeSet unicodeSet2, UnicodeSet unicodeSet3) {
        UnicodeSet filterAsUnicodeSet = getFilterAsUnicodeSet(unicodeSet);
        UnicodeSet unicodeSet4 = new UnicodeSet();
        StringBuilder sb = new StringBuilder();
        char c = 0;
        while (true) {
            char[] cArr = this.spec;
            if (cArr[c] == 65535) {
                break;
            }
            int i = cArr[c] + c + cArr[c + 1] + 5;
            char c2 = cArr[c + 2];
            for (int i2 = 0; i2 < c2; i2++) {
                Utility.appendNumber(sb, i2, c2, 0);
            }
            for (int i3 = c + 5; i3 < i; i3++) {
                unicodeSet4.add(this.spec[i3]);
            }
            c = i;
        }
        unicodeSet4.addAll(sb.toString());
        unicodeSet4.retainAll(filterAsUnicodeSet);
        if (unicodeSet4.size() > 0) {
            unicodeSet2.addAll(unicodeSet4);
            unicodeSet3.addAll(0, 1114111);
        }
    }
}
