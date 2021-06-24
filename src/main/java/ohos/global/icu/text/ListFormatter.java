package ohos.global.icu.text;

import java.io.InvalidObjectException;
import java.text.AttributedCharacterIterator;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;
import ohos.global.icu.impl.FormattedStringBuilder;
import ohos.global.icu.impl.FormattedValueStringBuilderImpl;
import ohos.global.icu.impl.ICUCache;
import ohos.global.icu.impl.ICUData;
import ohos.global.icu.impl.ICUResourceBundle;
import ohos.global.icu.impl.SimpleCache;
import ohos.global.icu.impl.SimpleFormatterImpl;
import ohos.global.icu.impl.Utility;
import ohos.global.icu.text.UFormat;
import ohos.global.icu.util.ULocale;
import ohos.global.icu.util.UResourceBundle;

public final class ListFormatter {
    static Cache cache = new Cache(null);
    private static final Pattern changeToE = Pattern.compile("(i.*|hi|hi[^ae].*)", 2);
    private static final Pattern changeToU = Pattern.compile("((o|ho|8).*|11)", 2);
    private static final Pattern changeToVavDash = Pattern.compile("^[\\P{InHebrew}].*$");
    private static final String compiledE = compilePattern("{0} e {1}", new StringBuilder());
    private static final String compiledO = compilePattern("{0} o {1}", new StringBuilder());
    private static final String compiledU = compilePattern("{0} u {1}", new StringBuilder());
    private static final String compiledVav = compilePattern("{0} ו{1}", new StringBuilder());
    private static final String compiledVavDash = compilePattern("{0} ו-{1}", new StringBuilder());
    private static final String compiledY = compilePattern("{0} y {1}", new StringBuilder());
    private final ULocale locale;
    private final String middle;
    private final PatternHandler patternHandler;
    private final String start;

    /* access modifiers changed from: private */
    public interface PatternHandler {
        String getEndPattern(String str);

        String getTwoPattern(String str);
    }

    public enum Type {
        AND,
        OR,
        UNITS
    }

    public enum Width {
        WIDE,
        SHORT,
        NARROW
    }

    /* synthetic */ ListFormatter(String str, String str2, String str3, String str4, ULocale uLocale, AnonymousClass1 r6) {
        this(str, str2, str3, str4, uLocale);
    }

    @Deprecated
    public enum Style {
        STANDARD("standard"),
        OR("or"),
        UNIT("unit"),
        UNIT_SHORT("unit-short"),
        UNIT_NARROW("unit-narrow");
        
        private final String name;

        private Style(String str) {
            this.name = str;
        }

        @Deprecated
        public String getName() {
            return this.name;
        }
    }

    public static final class SpanField extends UFormat.SpanField {
        public static final SpanField LIST_SPAN = new SpanField("list-span");
        private static final long serialVersionUID = 3563544214705634403L;

        private SpanField(String str) {
            super(str);
        }

        /* access modifiers changed from: protected */
        @Override // java.text.AttributedCharacterIterator.Attribute
        @Deprecated
        public Object readResolve() throws InvalidObjectException {
            if (getName().equals(LIST_SPAN.getName())) {
                return LIST_SPAN;
            }
            throw new InvalidObjectException("An invalid object.");
        }
    }

    public static final class Field extends Format.Field {
        public static Field ELEMENT = new Field("element");
        public static Field LITERAL = new Field("literal");
        private static final long serialVersionUID = -8071145668708265437L;

        private Field(String str) {
            super(str);
        }

        /* access modifiers changed from: protected */
        @Override // java.text.AttributedCharacterIterator.Attribute
        public Object readResolve() throws InvalidObjectException {
            if (getName().equals(LITERAL.getName())) {
                return LITERAL;
            }
            if (getName().equals(ELEMENT.getName())) {
                return ELEMENT;
            }
            throw new InvalidObjectException("An invalid object.");
        }
    }

    public static final class FormattedList implements FormattedValue {
        private final FormattedStringBuilder string;

        FormattedList(FormattedStringBuilder formattedStringBuilder) {
            this.string = formattedStringBuilder;
        }

        @Override // ohos.global.icu.text.FormattedValue
        public String toString() {
            return this.string.toString();
        }

        public int length() {
            return this.string.length();
        }

        public char charAt(int i) {
            return this.string.charAt(i);
        }

        public CharSequence subSequence(int i, int i2) {
            return this.string.subString(i, i2);
        }

        @Override // ohos.global.icu.text.FormattedValue
        public <A extends Appendable> A appendTo(A a) {
            return (A) Utility.appendTo(this.string, a);
        }

        @Override // ohos.global.icu.text.FormattedValue
        public boolean nextPosition(ConstrainedFieldPosition constrainedFieldPosition) {
            return FormattedValueStringBuilderImpl.nextPosition(this.string, constrainedFieldPosition, null);
        }

        @Override // ohos.global.icu.text.FormattedValue
        public AttributedCharacterIterator toCharacterIterator() {
            return FormattedValueStringBuilderImpl.toCharacterIterator(this.string, null);
        }
    }

    @Deprecated
    public ListFormatter(String str, String str2, String str3, String str4) {
        this(compilePattern(str, new StringBuilder()), compilePattern(str2, new StringBuilder()), compilePattern(str3, new StringBuilder()), compilePattern(str4, new StringBuilder()), null);
    }

    private ListFormatter(String str, String str2, String str3, String str4, ULocale uLocale) {
        this.start = str2;
        this.middle = str3;
        this.locale = uLocale;
        this.patternHandler = createPatternHandler(str, str4);
    }

    /* access modifiers changed from: private */
    public static String compilePattern(String str, StringBuilder sb) {
        return SimpleFormatterImpl.compileToStringMinMaxArguments(str, sb, 2, 2);
    }

    public static ListFormatter getInstance(ULocale uLocale, Type type, Width width) {
        String typeWidthToStyleString = typeWidthToStyleString(type, width);
        if (typeWidthToStyleString != null) {
            return cache.get(uLocale, typeWidthToStyleString);
        }
        throw new IllegalArgumentException("Invalid list format type/width");
    }

    public static ListFormatter getInstance(Locale locale2, Type type, Width width) {
        return getInstance(ULocale.forLocale(locale2), type, width);
    }

    @Deprecated
    public static ListFormatter getInstance(ULocale uLocale, Style style) {
        return cache.get(uLocale, style.getName());
    }

    public static ListFormatter getInstance(ULocale uLocale) {
        return getInstance(uLocale, Style.STANDARD);
    }

    public static ListFormatter getInstance(Locale locale2) {
        return getInstance(ULocale.forLocale(locale2), Style.STANDARD);
    }

    public static ListFormatter getInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public String format(Object... objArr) {
        return format(Arrays.asList(objArr));
    }

    public String format(Collection<?> collection) {
        return formatImpl(collection, false).toString();
    }

    public FormattedList formatToValue(Object... objArr) {
        return formatToValue(Arrays.asList(objArr));
    }

    public FormattedList formatToValue(Collection<?> collection) {
        return formatImpl(collection, true).toValue();
    }

    /* access modifiers changed from: package-private */
    public FormattedListBuilder formatImpl(Collection<?> collection, boolean z) {
        Iterator<?> it = collection.iterator();
        int size = collection.size();
        if (size == 0) {
            return new FormattedListBuilder("", z);
        }
        if (size == 1) {
            return new FormattedListBuilder(it.next(), z);
        }
        int i = 2;
        if (size != 2) {
            FormattedListBuilder formattedListBuilder = new FormattedListBuilder(it.next(), z);
            formattedListBuilder.append(this.start, it.next(), 1);
            while (true) {
                int i2 = size - 1;
                if (i < i2) {
                    formattedListBuilder.append(this.middle, it.next(), i);
                    i++;
                } else {
                    Object next = it.next();
                    return formattedListBuilder.append(this.patternHandler.getEndPattern(String.valueOf(next)), next, i2);
                }
            }
        } else {
            Object next2 = it.next();
            Object next3 = it.next();
            return new FormattedListBuilder(next2, z).append(this.patternHandler.getTwoPattern(String.valueOf(next3)), next3, 1);
        }
    }

    /* access modifiers changed from: private */
    public class StaticHandler implements PatternHandler {
        private final String endPattern;
        private final String twoPattern;

        StaticHandler(String str, String str2) {
            this.twoPattern = str;
            this.endPattern = str2;
        }

        @Override // ohos.global.icu.text.ListFormatter.PatternHandler
        public String getTwoPattern(String str) {
            return this.twoPattern;
        }

        @Override // ohos.global.icu.text.ListFormatter.PatternHandler
        public String getEndPattern(String str) {
            return this.endPattern;
        }
    }

    /* access modifiers changed from: private */
    public class ContextualHandler implements PatternHandler {
        private final String elseEndPattern;
        private final String elseTwoPattern;
        private final Pattern regexp;
        private final String thenEndPattern;
        private final String thenTwoPattern;

        ContextualHandler(Pattern pattern, String str, String str2, String str3, String str4) {
            this.regexp = pattern;
            this.thenTwoPattern = str;
            this.elseTwoPattern = str2;
            this.thenEndPattern = str3;
            this.elseEndPattern = str4;
        }

        @Override // ohos.global.icu.text.ListFormatter.PatternHandler
        public String getTwoPattern(String str) {
            if (this.regexp.matcher(str).matches()) {
                return this.thenTwoPattern;
            }
            return this.elseTwoPattern;
        }

        @Override // ohos.global.icu.text.ListFormatter.PatternHandler
        public String getEndPattern(String str) {
            if (this.regexp.matcher(str).matches()) {
                return this.thenEndPattern;
            }
            return this.elseEndPattern;
        }
    }

    private PatternHandler createPatternHandler(String str, String str2) {
        String str3;
        ULocale uLocale = this.locale;
        if (uLocale != null) {
            String language = uLocale.getLanguage();
            if (language.equals("es")) {
                boolean equals = str.equals(compiledY);
                boolean equals2 = str2.equals(compiledY);
                if (equals || equals2) {
                    return new ContextualHandler(changeToE, equals ? compiledE : str, str, equals2 ? compiledE : str2, str2);
                }
                boolean equals3 = str.equals(compiledO);
                boolean equals4 = str2.equals(compiledO);
                if (equals3 || equals4) {
                    Pattern pattern = changeToU;
                    if (equals3) {
                        str3 = compiledU;
                    } else {
                        str3 = str;
                    }
                    return new ContextualHandler(pattern, str3, str, equals4 ? compiledU : str2, str2);
                }
            } else if (language.equals("he") || language.equals("iw")) {
                boolean equals5 = str.equals(compiledVav);
                boolean equals6 = str2.equals(compiledVav);
                if (equals5 || equals6) {
                    return new ContextualHandler(changeToVavDash, equals5 ? compiledVavDash : str, str, equals6 ? compiledVavDash : str2, str2);
                }
            }
        }
        return new StaticHandler(str, str2);
    }

    public String getPatternForNumItems(int i) {
        if (i > 0) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < i; i2++) {
                arrayList.add(String.format("{%d}", Integer.valueOf(i2)));
            }
            return format(arrayList);
        }
        throw new IllegalArgumentException("count must be > 0");
    }

    @Deprecated
    public ULocale getLocale() {
        return this.locale;
    }

    /* access modifiers changed from: package-private */
    public static class FormattedListBuilder {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean needsFields;
        private FormattedStringBuilder string = new FormattedStringBuilder();

        public FormattedListBuilder(Object obj, boolean z) {
            this.needsFields = z;
            this.string.setAppendableField(Field.LITERAL);
            appendElement(obj, 0);
        }

        public FormattedListBuilder append(String str, Object obj, int i) {
            this.string.setAppendIndex(0);
            long j = 0;
            while (true) {
                j = SimpleFormatterImpl.IterInternal.step(j, str, this.string);
                if (j == -1) {
                    return this;
                }
                if (SimpleFormatterImpl.IterInternal.getArgIndex(j) == 0) {
                    FormattedStringBuilder formattedStringBuilder = this.string;
                    formattedStringBuilder.setAppendIndex(formattedStringBuilder.length());
                } else {
                    appendElement(obj, i);
                }
            }
        }

        private void appendElement(Object obj, int i) {
            if (this.needsFields) {
                FormattedValueStringBuilderImpl.SpanFieldPlaceholder spanFieldPlaceholder = new FormattedValueStringBuilderImpl.SpanFieldPlaceholder();
                spanFieldPlaceholder.spanField = SpanField.LIST_SPAN;
                spanFieldPlaceholder.normalField = Field.ELEMENT;
                spanFieldPlaceholder.value = Integer.valueOf(i);
                this.string.append(obj.toString(), spanFieldPlaceholder);
                return;
            }
            this.string.append(obj.toString(), (Object) null);
        }

        public void appendTo(Appendable appendable) {
            Utility.appendTo(this.string, appendable);
        }

        public int getOffset(int i) {
            return FormattedValueStringBuilderImpl.findSpan(this.string, Integer.valueOf(i));
        }

        public String toString() {
            return this.string.toString();
        }

        public FormattedList toValue() {
            return new FormattedList(this.string);
        }
    }

    /* access modifiers changed from: private */
    public static class Cache {
        private final ICUCache<String, ListFormatter> cache;

        private Cache() {
            this.cache = new SimpleCache();
        }

        /* synthetic */ Cache(AnonymousClass1 r1) {
            this();
        }

        public ListFormatter get(ULocale uLocale, String str) {
            String format = String.format("%s:%s", uLocale.toString(), str);
            ListFormatter listFormatter = this.cache.get(format);
            if (listFormatter != null) {
                return listFormatter;
            }
            ListFormatter load = load(uLocale, str);
            this.cache.put(format, load);
            return load;
        }

        private static ListFormatter load(ULocale uLocale, String str) {
            ICUResourceBundle iCUResourceBundle = (ICUResourceBundle) UResourceBundle.getBundleInstance(ICUData.ICU_BASE_NAME, uLocale);
            StringBuilder sb = new StringBuilder();
            String compilePattern = ListFormatter.compilePattern(iCUResourceBundle.getWithFallback("listPattern/" + str + "/2").getString(), sb);
            String compilePattern2 = ListFormatter.compilePattern(iCUResourceBundle.getWithFallback("listPattern/" + str + "/start").getString(), sb);
            String compilePattern3 = ListFormatter.compilePattern(iCUResourceBundle.getWithFallback("listPattern/" + str + "/middle").getString(), sb);
            return new ListFormatter(compilePattern, compilePattern2, compilePattern3, ListFormatter.compilePattern(iCUResourceBundle.getWithFallback("listPattern/" + str + "/end").getString(), sb), uLocale, null);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.text.ListFormatter$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$text$ListFormatter$Type = new int[Type.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$text$ListFormatter$Width = new int[Width.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        static {
            /*
                ohos.global.icu.text.ListFormatter$Type[] r0 = ohos.global.icu.text.ListFormatter.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Type = r0
                r0 = 1
                int[] r1 = ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Type     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.text.ListFormatter$Type r2 = ohos.global.icu.text.ListFormatter.Type.AND     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                r1 = 2
                int[] r2 = ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Type     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.text.ListFormatter$Type r3 = ohos.global.icu.text.ListFormatter.Type.OR     // Catch:{ NoSuchFieldError -> 0x001f }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                r2 = 3
                int[] r3 = ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Type     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.text.ListFormatter$Type r4 = ohos.global.icu.text.ListFormatter.Type.UNITS     // Catch:{ NoSuchFieldError -> 0x002a }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                ohos.global.icu.text.ListFormatter$Width[] r3 = ohos.global.icu.text.ListFormatter.Width.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Width = r3
                int[] r3 = ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Width     // Catch:{ NoSuchFieldError -> 0x003d }
                ohos.global.icu.text.ListFormatter$Width r4 = ohos.global.icu.text.ListFormatter.Width.WIDE     // Catch:{ NoSuchFieldError -> 0x003d }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x003d }
                r3[r4] = r0     // Catch:{ NoSuchFieldError -> 0x003d }
            L_0x003d:
                int[] r0 = ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Width     // Catch:{ NoSuchFieldError -> 0x0047 }
                ohos.global.icu.text.ListFormatter$Width r3 = ohos.global.icu.text.ListFormatter.Width.SHORT     // Catch:{ NoSuchFieldError -> 0x0047 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0047 }
                r0[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0047 }
            L_0x0047:
                int[] r0 = ohos.global.icu.text.ListFormatter.AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Width     // Catch:{ NoSuchFieldError -> 0x0051 }
                ohos.global.icu.text.ListFormatter$Width r1 = ohos.global.icu.text.ListFormatter.Width.NARROW     // Catch:{ NoSuchFieldError -> 0x0051 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0051 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0051 }
            L_0x0051:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.text.ListFormatter.AnonymousClass1.<clinit>():void");
        }
    }

    static String typeWidthToStyleString(Type type, Width width) {
        int i = AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Type[type.ordinal()];
        if (i == 1) {
            int i2 = AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Width[width.ordinal()];
            if (i2 == 1) {
                return "standard";
            }
            if (i2 == 2) {
                return "standard-short";
            }
            if (i2 != 3) {
                return null;
            }
            return "standard-narrow";
        } else if (i == 2) {
            int i3 = AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Width[width.ordinal()];
            if (i3 == 1) {
                return "or";
            }
            if (i3 == 2) {
                return "or-short";
            }
            if (i3 != 3) {
                return null;
            }
            return "or-narrow";
        } else if (i != 3) {
            return null;
        } else {
            int i4 = AnonymousClass1.$SwitchMap$ohos$global$icu$text$ListFormatter$Width[width.ordinal()];
            if (i4 == 1) {
                return "unit";
            }
            if (i4 == 2) {
                return "unit-short";
            }
            if (i4 != 3) {
                return null;
            }
            return "unit-narrow";
        }
    }
}
