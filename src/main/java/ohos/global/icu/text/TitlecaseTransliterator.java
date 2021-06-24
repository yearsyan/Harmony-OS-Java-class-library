package ohos.global.icu.text;

import ohos.global.icu.impl.UCaseProps;
import ohos.global.icu.lang.UCharacter;
import ohos.global.icu.text.Transliterator;
import ohos.global.icu.util.ULocale;

/* access modifiers changed from: package-private */
public class TitlecaseTransliterator extends Transliterator {
    static final String _ID = "Any-Title";
    private int caseLocale;
    private final UCaseProps csp;
    private ReplaceableContextIterator iter;
    private final ULocale locale;
    private StringBuilder result;
    SourceTargetUtility sourceTargetUtility = null;

    static void register() {
        Transliterator.registerFactory(_ID, new Transliterator.Factory() {
            /* class ohos.global.icu.text.TitlecaseTransliterator.AnonymousClass1 */

            @Override // ohos.global.icu.text.Transliterator.Factory
            public Transliterator getInstance(String str) {
                return new TitlecaseTransliterator(ULocale.US);
            }
        });
        registerSpecialInverse("Title", "Lower", false);
    }

    public TitlecaseTransliterator(ULocale uLocale) {
        super(_ID, null);
        this.locale = uLocale;
        setMaximumContextLength(2);
        this.csp = UCaseProps.INSTANCE;
        this.iter = new ReplaceableContextIterator();
        this.result = new StringBuilder();
        this.caseLocale = UCaseProps.getCaseLocale(this.locale);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0029, code lost:
        r0 = true;
     */
    @Override // ohos.global.icu.text.Transliterator
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void handleTransliterate(ohos.global.icu.text.Replaceable r8, ohos.global.icu.text.Transliterator.Position r9, boolean r10) {
        /*
        // Method dump skipped, instructions count: 200
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.TitlecaseTransliterator.handleTransliterate(ohos.global.icu.text.Replaceable, ohos.global.icu.text.Transliterator$Position, boolean):void");
    }

    @Override // ohos.global.icu.text.Transliterator
    public void addSourceTargetSet(UnicodeSet unicodeSet, UnicodeSet unicodeSet2, UnicodeSet unicodeSet3) {
        synchronized (this) {
            if (this.sourceTargetUtility == null) {
                this.sourceTargetUtility = new SourceTargetUtility(new Transform<String, String>() {
                    /* class ohos.global.icu.text.TitlecaseTransliterator.AnonymousClass2 */

                    public String transform(String str) {
                        return UCharacter.toTitleCase(TitlecaseTransliterator.this.locale, str, (BreakIterator) null);
                    }
                });
            }
        }
        this.sourceTargetUtility.addSourceTargetSet(this, unicodeSet, unicodeSet2, unicodeSet3);
    }
}
