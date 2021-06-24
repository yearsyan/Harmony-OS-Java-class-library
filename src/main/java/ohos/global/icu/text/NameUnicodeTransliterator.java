package ohos.global.icu.text;

import ohos.global.icu.text.Transliterator;

class NameUnicodeTransliterator extends Transliterator {
    static final char CLOSE_DELIM = '}';
    static final char OPEN_DELIM = '\\';
    static final String OPEN_PAT = "\\N~{~";
    static final char SPACE = ' ';
    static final String _ID = "Name-Any";

    static void register() {
        Transliterator.registerFactory(_ID, new Transliterator.Factory() {
            /* class ohos.global.icu.text.NameUnicodeTransliterator.AnonymousClass1 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new NameUnicodeTransliterator(null);
            }
        });
    }

    public NameUnicodeTransliterator(UnicodeFilter unicodeFilter) {
        super(_ID, unicodeFilter);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004f, code lost:
        if (r4.length() > r2) goto L_0x0098;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0093, code lost:
        if (r4.length() >= r2) goto L_0x0098;
     */
    @Override // ohos.global.icu.text.Transliterator
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleTransliterate(ohos.global.icu.text.Replaceable r17, ohos.global.icu.text.Transliterator.Position r18, boolean r19) {
        /*
        // Method dump skipped, instructions count: 204
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.NameUnicodeTransliterator.handleTransliterate(ohos.global.icu.text.Replaceable, ohos.global.icu.text.Transliterator$Position, boolean):void");
    }

    @Override // ohos.global.icu.text.Transliterator
    public void addSourceTargetSet(UnicodeSet unicodeSet, UnicodeSet unicodeSet2, UnicodeSet unicodeSet3) {
        UnicodeSet filterAsUnicodeSet = getFilterAsUnicodeSet(unicodeSet);
        if (filterAsUnicodeSet.containsAll("\\N{") && filterAsUnicodeSet.contains(125)) {
            UnicodeSet add = new UnicodeSet().addAll(48, 57).addAll(65, 70).addAll(97, 122).add(60).add(62).add(40).add(41).add(45).add(32).addAll("\\N{").add(125);
            add.retainAll(filterAsUnicodeSet);
            if (add.size() > 0) {
                unicodeSet2.addAll(add);
                unicodeSet3.addAll(0, 1114111);
            }
        }
    }
}
