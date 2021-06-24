package ohos.global.icu.number;

import java.math.BigDecimal;
import java.math.RoundingMode;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.global.icu.impl.CacheBase;
import ohos.global.icu.impl.PatternProps;
import ohos.global.icu.impl.SoftCache;
import ohos.global.icu.impl.StringSegment;
import ohos.global.icu.impl.locale.LanguageTag;
import ohos.global.icu.impl.number.MacroProps;
import ohos.global.icu.impl.number.RoundingUtils;
import ohos.global.icu.number.NumberFormatter;
import ohos.global.icu.number.Precision;
import ohos.global.icu.text.NumberingSystem;
import ohos.global.icu.util.BytesTrie;
import ohos.global.icu.util.CharsTrie;
import ohos.global.icu.util.CharsTrieBuilder;
import ohos.global.icu.util.Currency;
import ohos.global.icu.util.MeasureUnit;
import ohos.global.icu.util.NoUnit;
import ohos.global.icu.util.StringTrieBuilder;

/* access modifiers changed from: package-private */
public class NumberSkeletonImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final char ALT_WILDCARD_CHAR = '+';
    static final String SERIALIZED_STEM_TRIE = buildStemTrie();
    static final StemEnum[] STEM_ENUM_VALUES = StemEnum.values();
    static final char WILDCARD_CHAR = '*';
    private static final CacheBase<String, UnlocalizedNumberFormatter, Void> cache = new SoftCache<String, UnlocalizedNumberFormatter, Void>() {
        /* class ohos.global.icu.number.NumberSkeletonImpl.AnonymousClass1 */

        /* access modifiers changed from: protected */
        public UnlocalizedNumberFormatter createInstance(String str, Void r2) {
            return NumberSkeletonImpl.create(str);
        }
    };

    /* access modifiers changed from: package-private */
    public enum ParseState {
        STATE_NULL,
        STATE_SCIENTIFIC,
        STATE_FRACTION_PRECISION,
        STATE_INCREMENT_PRECISION,
        STATE_MEASURE_UNIT,
        STATE_PER_MEASURE_UNIT,
        STATE_IDENTIFIER_UNIT,
        STATE_CURRENCY_UNIT,
        STATE_INTEGER_WIDTH,
        STATE_NUMBERING_SYSTEM,
        STATE_SCALE
    }

    /* access modifiers changed from: package-private */
    public enum StemEnum {
        STEM_COMPACT_SHORT,
        STEM_COMPACT_LONG,
        STEM_SCIENTIFIC,
        STEM_ENGINEERING,
        STEM_NOTATION_SIMPLE,
        STEM_BASE_UNIT,
        STEM_PERCENT,
        STEM_PERMILLE,
        STEM_PERCENT_100,
        STEM_PRECISION_INTEGER,
        STEM_PRECISION_UNLIMITED,
        STEM_PRECISION_CURRENCY_STANDARD,
        STEM_PRECISION_CURRENCY_CASH,
        STEM_ROUNDING_MODE_CEILING,
        STEM_ROUNDING_MODE_FLOOR,
        STEM_ROUNDING_MODE_DOWN,
        STEM_ROUNDING_MODE_UP,
        STEM_ROUNDING_MODE_HALF_EVEN,
        STEM_ROUNDING_MODE_HALF_DOWN,
        STEM_ROUNDING_MODE_HALF_UP,
        STEM_ROUNDING_MODE_UNNECESSARY,
        STEM_GROUP_OFF,
        STEM_GROUP_MIN2,
        STEM_GROUP_AUTO,
        STEM_GROUP_ON_ALIGNED,
        STEM_GROUP_THOUSANDS,
        STEM_LATIN,
        STEM_UNIT_WIDTH_NARROW,
        STEM_UNIT_WIDTH_SHORT,
        STEM_UNIT_WIDTH_FULL_NAME,
        STEM_UNIT_WIDTH_ISO_CODE,
        STEM_UNIT_WIDTH_FORMAL,
        STEM_UNIT_WIDTH_VARIANT,
        STEM_UNIT_WIDTH_HIDDEN,
        STEM_SIGN_AUTO,
        STEM_SIGN_ALWAYS,
        STEM_SIGN_NEVER,
        STEM_SIGN_ACCOUNTING,
        STEM_SIGN_ACCOUNTING_ALWAYS,
        STEM_SIGN_EXCEPT_ZERO,
        STEM_SIGN_ACCOUNTING_EXCEPT_ZERO,
        STEM_DECIMAL_AUTO,
        STEM_DECIMAL_ALWAYS,
        STEM_PRECISION_INCREMENT,
        STEM_MEASURE_UNIT,
        STEM_PER_MEASURE_UNIT,
        STEM_UNIT,
        STEM_CURRENCY,
        STEM_INTEGER_WIDTH,
        STEM_NUMBERING_SYSTEM,
        STEM_SCALE
    }

    static boolean isWildcardChar(char c) {
        return c == '*' || c == '+';
    }

    NumberSkeletonImpl() {
    }

    static String buildStemTrie() {
        CharsTrieBuilder charsTrieBuilder = new CharsTrieBuilder();
        charsTrieBuilder.add("compact-short", StemEnum.STEM_COMPACT_SHORT.ordinal());
        charsTrieBuilder.add("compact-long", StemEnum.STEM_COMPACT_LONG.ordinal());
        charsTrieBuilder.add("scientific", StemEnum.STEM_SCIENTIFIC.ordinal());
        charsTrieBuilder.add("engineering", StemEnum.STEM_ENGINEERING.ordinal());
        charsTrieBuilder.add("notation-simple", StemEnum.STEM_NOTATION_SIMPLE.ordinal());
        charsTrieBuilder.add("base-unit", StemEnum.STEM_BASE_UNIT.ordinal());
        charsTrieBuilder.add(Constants.ATTRNAME_PERCENT, StemEnum.STEM_PERCENT.ordinal());
        charsTrieBuilder.add("permille", StemEnum.STEM_PERMILLE.ordinal());
        charsTrieBuilder.add("precision-integer", StemEnum.STEM_PRECISION_INTEGER.ordinal());
        charsTrieBuilder.add("precision-unlimited", StemEnum.STEM_PRECISION_UNLIMITED.ordinal());
        charsTrieBuilder.add("precision-currency-standard", StemEnum.STEM_PRECISION_CURRENCY_STANDARD.ordinal());
        charsTrieBuilder.add("precision-currency-cash", StemEnum.STEM_PRECISION_CURRENCY_CASH.ordinal());
        charsTrieBuilder.add("rounding-mode-ceiling", StemEnum.STEM_ROUNDING_MODE_CEILING.ordinal());
        charsTrieBuilder.add("rounding-mode-floor", StemEnum.STEM_ROUNDING_MODE_FLOOR.ordinal());
        charsTrieBuilder.add("rounding-mode-down", StemEnum.STEM_ROUNDING_MODE_DOWN.ordinal());
        charsTrieBuilder.add("rounding-mode-up", StemEnum.STEM_ROUNDING_MODE_UP.ordinal());
        charsTrieBuilder.add("rounding-mode-half-even", StemEnum.STEM_ROUNDING_MODE_HALF_EVEN.ordinal());
        charsTrieBuilder.add("rounding-mode-half-down", StemEnum.STEM_ROUNDING_MODE_HALF_DOWN.ordinal());
        charsTrieBuilder.add("rounding-mode-half-up", StemEnum.STEM_ROUNDING_MODE_HALF_UP.ordinal());
        charsTrieBuilder.add("rounding-mode-unnecessary", StemEnum.STEM_ROUNDING_MODE_UNNECESSARY.ordinal());
        charsTrieBuilder.add("group-off", StemEnum.STEM_GROUP_OFF.ordinal());
        charsTrieBuilder.add("group-min2", StemEnum.STEM_GROUP_MIN2.ordinal());
        charsTrieBuilder.add("group-auto", StemEnum.STEM_GROUP_AUTO.ordinal());
        charsTrieBuilder.add("group-on-aligned", StemEnum.STEM_GROUP_ON_ALIGNED.ordinal());
        charsTrieBuilder.add("group-thousands", StemEnum.STEM_GROUP_THOUSANDS.ordinal());
        charsTrieBuilder.add("latin", StemEnum.STEM_LATIN.ordinal());
        charsTrieBuilder.add("unit-width-narrow", StemEnum.STEM_UNIT_WIDTH_NARROW.ordinal());
        charsTrieBuilder.add("unit-width-short", StemEnum.STEM_UNIT_WIDTH_SHORT.ordinal());
        charsTrieBuilder.add("unit-width-full-name", StemEnum.STEM_UNIT_WIDTH_FULL_NAME.ordinal());
        charsTrieBuilder.add("unit-width-iso-code", StemEnum.STEM_UNIT_WIDTH_ISO_CODE.ordinal());
        charsTrieBuilder.add("unit-width-formal", StemEnum.STEM_UNIT_WIDTH_FORMAL.ordinal());
        charsTrieBuilder.add("unit-width-variant", StemEnum.STEM_UNIT_WIDTH_VARIANT.ordinal());
        charsTrieBuilder.add("unit-width-hidden", StemEnum.STEM_UNIT_WIDTH_HIDDEN.ordinal());
        charsTrieBuilder.add("sign-auto", StemEnum.STEM_SIGN_AUTO.ordinal());
        charsTrieBuilder.add("sign-always", StemEnum.STEM_SIGN_ALWAYS.ordinal());
        charsTrieBuilder.add("sign-never", StemEnum.STEM_SIGN_NEVER.ordinal());
        charsTrieBuilder.add("sign-accounting", StemEnum.STEM_SIGN_ACCOUNTING.ordinal());
        charsTrieBuilder.add("sign-accounting-always", StemEnum.STEM_SIGN_ACCOUNTING_ALWAYS.ordinal());
        charsTrieBuilder.add("sign-except-zero", StemEnum.STEM_SIGN_EXCEPT_ZERO.ordinal());
        charsTrieBuilder.add("sign-accounting-except-zero", StemEnum.STEM_SIGN_ACCOUNTING_EXCEPT_ZERO.ordinal());
        charsTrieBuilder.add("decimal-auto", StemEnum.STEM_DECIMAL_AUTO.ordinal());
        charsTrieBuilder.add("decimal-always", StemEnum.STEM_DECIMAL_ALWAYS.ordinal());
        charsTrieBuilder.add("precision-increment", StemEnum.STEM_PRECISION_INCREMENT.ordinal());
        charsTrieBuilder.add("measure-unit", StemEnum.STEM_MEASURE_UNIT.ordinal());
        charsTrieBuilder.add("per-measure-unit", StemEnum.STEM_PER_MEASURE_UNIT.ordinal());
        charsTrieBuilder.add("unit", StemEnum.STEM_UNIT.ordinal());
        charsTrieBuilder.add("currency", StemEnum.STEM_CURRENCY.ordinal());
        charsTrieBuilder.add("integer-width", StemEnum.STEM_INTEGER_WIDTH.ordinal());
        charsTrieBuilder.add("numbering-system", StemEnum.STEM_NUMBERING_SYSTEM.ordinal());
        charsTrieBuilder.add("scale", StemEnum.STEM_SCALE.ordinal());
        charsTrieBuilder.add("K", StemEnum.STEM_COMPACT_SHORT.ordinal());
        charsTrieBuilder.add("KK", StemEnum.STEM_COMPACT_LONG.ordinal());
        charsTrieBuilder.add("%", StemEnum.STEM_PERCENT.ordinal());
        charsTrieBuilder.add("%x100", StemEnum.STEM_PERCENT_100.ordinal());
        charsTrieBuilder.add(",_", StemEnum.STEM_GROUP_OFF.ordinal());
        charsTrieBuilder.add(",?", StemEnum.STEM_GROUP_MIN2.ordinal());
        charsTrieBuilder.add(",!", StemEnum.STEM_GROUP_ON_ALIGNED.ordinal());
        charsTrieBuilder.add("+!", StemEnum.STEM_SIGN_ALWAYS.ordinal());
        charsTrieBuilder.add("+_", StemEnum.STEM_SIGN_NEVER.ordinal());
        charsTrieBuilder.add("()", StemEnum.STEM_SIGN_ACCOUNTING.ordinal());
        charsTrieBuilder.add("()!", StemEnum.STEM_SIGN_ACCOUNTING_ALWAYS.ordinal());
        charsTrieBuilder.add("+?", StemEnum.STEM_SIGN_EXCEPT_ZERO.ordinal());
        charsTrieBuilder.add("()?", StemEnum.STEM_SIGN_ACCOUNTING_EXCEPT_ZERO.ordinal());
        return charsTrieBuilder.buildCharSequence(StringTrieBuilder.Option.FAST).toString();
    }

    /* access modifiers changed from: package-private */
    public static final class StemToObject {
        StemToObject() {
        }

        /* access modifiers changed from: private */
        public static Notation notation(StemEnum stemEnum) {
            int i = AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberSkeletonImpl$StemEnum[stemEnum.ordinal()];
            if (i == 1) {
                return Notation.compactShort();
            }
            if (i == 2) {
                return Notation.compactLong();
            }
            if (i == 3) {
                return Notation.scientific();
            }
            if (i == 4) {
                return Notation.engineering();
            }
            if (i == 5) {
                return Notation.simple();
            }
            throw new AssertionError();
        }

        /* access modifiers changed from: private */
        public static MeasureUnit unit(StemEnum stemEnum) {
            int i = AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberSkeletonImpl$StemEnum[stemEnum.ordinal()];
            if (i == 6) {
                return NoUnit.BASE;
            }
            if (i == 7) {
                return NoUnit.PERCENT;
            }
            if (i == 8) {
                return NoUnit.PERMILLE;
            }
            throw new AssertionError();
        }

        /* access modifiers changed from: private */
        public static Precision precision(StemEnum stemEnum) {
            switch (stemEnum) {
                case STEM_PRECISION_INTEGER:
                    return Precision.integer();
                case STEM_PRECISION_UNLIMITED:
                    return Precision.unlimited();
                case STEM_PRECISION_CURRENCY_STANDARD:
                    return Precision.currency(Currency.CurrencyUsage.STANDARD);
                case STEM_PRECISION_CURRENCY_CASH:
                    return Precision.currency(Currency.CurrencyUsage.CASH);
                default:
                    throw new AssertionError();
            }
        }

        /* access modifiers changed from: private */
        public static RoundingMode roundingMode(StemEnum stemEnum) {
            switch (stemEnum) {
                case STEM_ROUNDING_MODE_CEILING:
                    return RoundingMode.CEILING;
                case STEM_ROUNDING_MODE_FLOOR:
                    return RoundingMode.FLOOR;
                case STEM_ROUNDING_MODE_DOWN:
                    return RoundingMode.DOWN;
                case STEM_ROUNDING_MODE_UP:
                    return RoundingMode.UP;
                case STEM_ROUNDING_MODE_HALF_EVEN:
                    return RoundingMode.HALF_EVEN;
                case STEM_ROUNDING_MODE_HALF_DOWN:
                    return RoundingMode.HALF_DOWN;
                case STEM_ROUNDING_MODE_HALF_UP:
                    return RoundingMode.HALF_UP;
                case STEM_ROUNDING_MODE_UNNECESSARY:
                    return RoundingMode.UNNECESSARY;
                default:
                    throw new AssertionError();
            }
        }

        /* access modifiers changed from: private */
        public static NumberFormatter.GroupingStrategy groupingStrategy(StemEnum stemEnum) {
            switch (stemEnum) {
                case STEM_GROUP_OFF:
                    return NumberFormatter.GroupingStrategy.OFF;
                case STEM_GROUP_MIN2:
                    return NumberFormatter.GroupingStrategy.MIN2;
                case STEM_GROUP_AUTO:
                    return NumberFormatter.GroupingStrategy.AUTO;
                case STEM_GROUP_ON_ALIGNED:
                    return NumberFormatter.GroupingStrategy.ON_ALIGNED;
                case STEM_GROUP_THOUSANDS:
                    return NumberFormatter.GroupingStrategy.THOUSANDS;
                default:
                    return null;
            }
        }

        /* access modifiers changed from: private */
        public static NumberFormatter.UnitWidth unitWidth(StemEnum stemEnum) {
            switch (stemEnum) {
                case STEM_UNIT_WIDTH_NARROW:
                    return NumberFormatter.UnitWidth.NARROW;
                case STEM_UNIT_WIDTH_SHORT:
                    return NumberFormatter.UnitWidth.SHORT;
                case STEM_UNIT_WIDTH_FULL_NAME:
                    return NumberFormatter.UnitWidth.FULL_NAME;
                case STEM_UNIT_WIDTH_ISO_CODE:
                    return NumberFormatter.UnitWidth.ISO_CODE;
                case STEM_UNIT_WIDTH_FORMAL:
                    return NumberFormatter.UnitWidth.FORMAL;
                case STEM_UNIT_WIDTH_VARIANT:
                    return NumberFormatter.UnitWidth.VARIANT;
                case STEM_UNIT_WIDTH_HIDDEN:
                    return NumberFormatter.UnitWidth.HIDDEN;
                default:
                    return null;
            }
        }

        /* access modifiers changed from: private */
        public static NumberFormatter.SignDisplay signDisplay(StemEnum stemEnum) {
            switch (stemEnum) {
                case STEM_SIGN_AUTO:
                    return NumberFormatter.SignDisplay.AUTO;
                case STEM_SIGN_ALWAYS:
                    return NumberFormatter.SignDisplay.ALWAYS;
                case STEM_SIGN_NEVER:
                    return NumberFormatter.SignDisplay.NEVER;
                case STEM_SIGN_ACCOUNTING:
                    return NumberFormatter.SignDisplay.ACCOUNTING;
                case STEM_SIGN_ACCOUNTING_ALWAYS:
                    return NumberFormatter.SignDisplay.ACCOUNTING_ALWAYS;
                case STEM_SIGN_EXCEPT_ZERO:
                    return NumberFormatter.SignDisplay.EXCEPT_ZERO;
                case STEM_SIGN_ACCOUNTING_EXCEPT_ZERO:
                    return NumberFormatter.SignDisplay.ACCOUNTING_EXCEPT_ZERO;
                default:
                    return null;
            }
        }

        /* access modifiers changed from: private */
        public static NumberFormatter.DecimalSeparatorDisplay decimalSeparatorDisplay(StemEnum stemEnum) {
            int i = AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberSkeletonImpl$StemEnum[stemEnum.ordinal()];
            if (i == 40) {
                return NumberFormatter.DecimalSeparatorDisplay.AUTO;
            }
            if (i != 41) {
                return null;
            }
            return NumberFormatter.DecimalSeparatorDisplay.ALWAYS;
        }
    }

    /* access modifiers changed from: package-private */
    public static final class EnumToStemString {
        EnumToStemString() {
        }

        /* access modifiers changed from: private */
        public static void roundingMode(RoundingMode roundingMode, StringBuilder sb) {
            switch (AnonymousClass2.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
                case 1:
                    sb.append("rounding-mode-ceiling");
                    return;
                case 2:
                    sb.append("rounding-mode-floor");
                    return;
                case 3:
                    sb.append("rounding-mode-down");
                    return;
                case 4:
                    sb.append("rounding-mode-up");
                    return;
                case 5:
                    sb.append("rounding-mode-half-even");
                    return;
                case 6:
                    sb.append("rounding-mode-half-down");
                    return;
                case 7:
                    sb.append("rounding-mode-half-up");
                    return;
                case 8:
                    sb.append("rounding-mode-unnecessary");
                    return;
                default:
                    throw new AssertionError();
            }
        }

        /* access modifiers changed from: private */
        public static void groupingStrategy(NumberFormatter.GroupingStrategy groupingStrategy, StringBuilder sb) {
            int i = AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberFormatter$GroupingStrategy[groupingStrategy.ordinal()];
            if (i == 1) {
                sb.append("group-off");
            } else if (i == 2) {
                sb.append("group-min2");
            } else if (i == 3) {
                sb.append("group-auto");
            } else if (i == 4) {
                sb.append("group-on-aligned");
            } else if (i == 5) {
                sb.append("group-thousands");
            } else {
                throw new AssertionError();
            }
        }

        /* access modifiers changed from: private */
        public static void unitWidth(NumberFormatter.UnitWidth unitWidth, StringBuilder sb) {
            switch (unitWidth) {
                case NARROW:
                    sb.append("unit-width-narrow");
                    return;
                case SHORT:
                    sb.append("unit-width-short");
                    return;
                case FULL_NAME:
                    sb.append("unit-width-full-name");
                    return;
                case ISO_CODE:
                    sb.append("unit-width-iso-code");
                    return;
                case FORMAL:
                    sb.append("unit-width-formal");
                    return;
                case VARIANT:
                    sb.append("unit-width-variant");
                    return;
                case HIDDEN:
                    sb.append("unit-width-hidden");
                    return;
                default:
                    throw new AssertionError();
            }
        }

        /* access modifiers changed from: private */
        public static void signDisplay(NumberFormatter.SignDisplay signDisplay, StringBuilder sb) {
            switch (signDisplay) {
                case AUTO:
                    sb.append("sign-auto");
                    return;
                case ALWAYS:
                    sb.append("sign-always");
                    return;
                case NEVER:
                    sb.append("sign-never");
                    return;
                case ACCOUNTING:
                    sb.append("sign-accounting");
                    return;
                case ACCOUNTING_ALWAYS:
                    sb.append("sign-accounting-always");
                    return;
                case EXCEPT_ZERO:
                    sb.append("sign-except-zero");
                    return;
                case ACCOUNTING_EXCEPT_ZERO:
                    sb.append("sign-accounting-except-zero");
                    return;
                default:
                    throw new AssertionError();
            }
        }

        /* access modifiers changed from: private */
        public static void decimalSeparatorDisplay(NumberFormatter.DecimalSeparatorDisplay decimalSeparatorDisplay, StringBuilder sb) {
            int i = AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberFormatter$DecimalSeparatorDisplay[decimalSeparatorDisplay.ordinal()];
            if (i == 1) {
                sb.append("decimal-auto");
            } else if (i == 2) {
                sb.append("decimal-always");
            } else {
                throw new AssertionError();
            }
        }
    }

    public static UnlocalizedNumberFormatter getOrCreate(String str) {
        return cache.getInstance(str, null);
    }

    public static UnlocalizedNumberFormatter create(String str) {
        return (UnlocalizedNumberFormatter) NumberFormatter.with().macros(parseSkeleton(str));
    }

    public static String generate(MacroProps macroProps) {
        StringBuilder sb = new StringBuilder();
        generateSkeleton(macroProps, sb);
        return sb.toString();
    }

    private static MacroProps parseSkeleton(String str) {
        MacroProps macroProps = new MacroProps();
        StringSegment stringSegment = new StringSegment(str + " ", false);
        CharsTrie charsTrie = new CharsTrie(SERIALIZED_STEM_TRIE, 0);
        ParseState parseState = ParseState.STATE_NULL;
        int i = 0;
        while (i < stringSegment.length()) {
            int codePointAt = stringSegment.codePointAt(i);
            boolean isWhiteSpace = PatternProps.isWhiteSpace(codePointAt);
            boolean z = codePointAt == 47;
            if (isWhiteSpace || z) {
                if (i != 0) {
                    stringSegment.setLength(i);
                    if (parseState == ParseState.STATE_NULL) {
                        parseState = parseStem(stringSegment, charsTrie, macroProps);
                        charsTrie.reset();
                    } else {
                        parseState = parseOption(parseState, stringSegment, macroProps);
                    }
                    stringSegment.resetLength();
                    stringSegment.adjustOffset(i);
                    i = 0;
                } else if (parseState != ParseState.STATE_NULL) {
                    stringSegment.setLength(Character.charCount(codePointAt));
                    throw new SkeletonSyntaxException("Unexpected separator character", stringSegment);
                }
                if (!z || parseState != ParseState.STATE_NULL) {
                    if (isWhiteSpace && parseState != ParseState.STATE_NULL) {
                        switch (parseState) {
                            case STATE_INCREMENT_PRECISION:
                            case STATE_MEASURE_UNIT:
                            case STATE_PER_MEASURE_UNIT:
                            case STATE_CURRENCY_UNIT:
                            case STATE_INTEGER_WIDTH:
                            case STATE_NUMBERING_SYSTEM:
                            case STATE_SCALE:
                                stringSegment.setLength(Character.charCount(codePointAt));
                                throw new SkeletonSyntaxException("Stem requires an option", stringSegment);
                            default:
                                parseState = ParseState.STATE_NULL;
                                break;
                        }
                    }
                    stringSegment.adjustOffset(Character.charCount(codePointAt));
                } else {
                    stringSegment.setLength(Character.charCount(codePointAt));
                    throw new SkeletonSyntaxException("Unexpected option separator", stringSegment);
                }
            } else {
                i += Character.charCount(codePointAt);
                if (parseState == ParseState.STATE_NULL) {
                    charsTrie.nextForCodePoint(codePointAt);
                }
            }
        }
        return macroProps;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.number.NumberSkeletonImpl$2  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$number$NumberFormatter$DecimalSeparatorDisplay = new int[NumberFormatter.DecimalSeparatorDisplay.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$number$NumberFormatter$GroupingStrategy = new int[NumberFormatter.GroupingStrategy.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(180:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|(2:21|22)|23|(2:25|26)|27|(2:29|30)|31|(2:33|34)|35|(2:37|38)|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|(2:59|60)|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|(2:87|88)|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|(2:105|106)|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(181:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|(2:21|22)|23|(2:25|26)|27|(2:29|30)|31|(2:33|34)|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|(2:59|60)|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|(2:87|88)|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|(2:105|106)|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(183:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|(2:21|22)|23|(2:25|26)|27|(2:29|30)|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|(2:59|60)|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|(2:105|106)|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(184:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|(2:21|22)|23|(2:25|26)|27|(2:29|30)|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|(2:105|106)|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(185:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|(2:21|22)|23|(2:25|26)|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|(2:105|106)|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(186:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|(2:21|22)|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|(2:105|106)|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(188:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(189:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(190:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|(2:75|76)|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(191:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(192:0|(2:1|2)|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|(3:209|210|212)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(194:0|(2:1|2)|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|209|210|212) */
        /* JADX WARNING: Can't wrap try/catch for region: R(195:0|(2:1|2)|3|5|6|7|9|10|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|(2:43|44)|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|209|210|212) */
        /* JADX WARNING: Can't wrap try/catch for region: R(197:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|21|22|23|25|26|27|29|30|31|33|34|35|37|38|39|41|42|43|44|45|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|79|80|81|82|83|84|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|171|172|173|174|175|176|177|178|179|180|181|182|183|184|185|186|187|188|189|190|191|192|193|194|195|196|197|198|199|200|201|202|203|204|205|206|207|208|209|210|212) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:101:0x01ab */
        /* JADX WARNING: Missing exception handler attribute for start block: B:103:0x01b5 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:105:0x01bf */
        /* JADX WARNING: Missing exception handler attribute for start block: B:111:0x01dc */
        /* JADX WARNING: Missing exception handler attribute for start block: B:113:0x01e6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:115:0x01f0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:117:0x01fa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:119:0x0204 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:121:0x020e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:123:0x0218 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:125:0x0222 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:127:0x022c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:129:0x0236 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:131:0x0242 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:133:0x024e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:135:0x025a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:137:0x0266 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:139:0x0272 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:141:0x027e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:143:0x028a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:145:0x0296 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:147:0x02a2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:149:0x02ae */
        /* JADX WARNING: Missing exception handler attribute for start block: B:151:0x02ba */
        /* JADX WARNING: Missing exception handler attribute for start block: B:153:0x02c6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:155:0x02d2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:157:0x02de */
        /* JADX WARNING: Missing exception handler attribute for start block: B:159:0x02ea */
        /* JADX WARNING: Missing exception handler attribute for start block: B:161:0x02f6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:163:0x0302 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:165:0x030e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:167:0x031a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:169:0x0326 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:171:0x0332 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:173:0x033e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:175:0x034a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:177:0x0356 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:179:0x0362 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:181:0x036e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:183:0x037a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:185:0x0386 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:187:0x0392 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:189:0x039e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:191:0x03aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:193:0x03b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:195:0x03c2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:197:0x03ce */
        /* JADX WARNING: Missing exception handler attribute for start block: B:199:0x03da */
        /* JADX WARNING: Missing exception handler attribute for start block: B:201:0x03e6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:203:0x03f2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:205:0x03fe */
        /* JADX WARNING: Missing exception handler attribute for start block: B:207:0x040a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:209:0x0416 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x008d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00b4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00be */
        /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x00c8 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x00d2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x00dc */
        /* JADX WARNING: Missing exception handler attribute for start block: B:65:0x00f9 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:67:0x0103 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:69:0x010d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:71:0x0117 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:73:0x0121 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:75:0x012b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:81:0x0148 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:83:0x0152 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:85:0x015c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:87:0x0166 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:93:0x0183 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:95:0x018d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:97:0x0197 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:99:0x01a1 */
        static {
            /*
            // Method dump skipped, instructions count: 1059
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.number.NumberSkeletonImpl.AnonymousClass2.<clinit>():void");
        }
    }

    private static ParseState parseStem(StringSegment stringSegment, CharsTrie charsTrie, MacroProps macroProps) {
        char charAt = stringSegment.charAt(0);
        if (charAt == '.') {
            checkNull(macroProps.precision, stringSegment);
            BlueprintHelpers.parseFractionStem(stringSegment, macroProps);
            return ParseState.STATE_FRACTION_PRECISION;
        } else if (charAt == '0') {
            checkNull(macroProps.notation, stringSegment);
            BlueprintHelpers.parseIntegerStem(stringSegment, macroProps);
            return ParseState.STATE_NULL;
        } else if (charAt == '@') {
            checkNull(macroProps.precision, stringSegment);
            BlueprintHelpers.parseDigitsStem(stringSegment, macroProps);
            return ParseState.STATE_NULL;
        } else if (charAt != 'E') {
            BytesTrie.Result current = charsTrie.current();
            if (current == BytesTrie.Result.INTERMEDIATE_VALUE || current == BytesTrie.Result.FINAL_VALUE) {
                StemEnum stemEnum = STEM_ENUM_VALUES[charsTrie.getValue()];
                switch (stemEnum) {
                    case STEM_COMPACT_SHORT:
                    case STEM_COMPACT_LONG:
                    case STEM_SCIENTIFIC:
                    case STEM_ENGINEERING:
                    case STEM_NOTATION_SIMPLE:
                        checkNull(macroProps.notation, stringSegment);
                        macroProps.notation = StemToObject.notation(stemEnum);
                        int i = AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberSkeletonImpl$StemEnum[stemEnum.ordinal()];
                        if (i == 3 || i == 4) {
                            return ParseState.STATE_SCIENTIFIC;
                        }
                        return ParseState.STATE_NULL;
                    case STEM_BASE_UNIT:
                    case STEM_PERCENT:
                    case STEM_PERMILLE:
                        checkNull(macroProps.unit, stringSegment);
                        macroProps.unit = StemToObject.unit(stemEnum);
                        return ParseState.STATE_NULL;
                    case STEM_PRECISION_INTEGER:
                    case STEM_PRECISION_UNLIMITED:
                    case STEM_PRECISION_CURRENCY_STANDARD:
                    case STEM_PRECISION_CURRENCY_CASH:
                        checkNull(macroProps.precision, stringSegment);
                        macroProps.precision = StemToObject.precision(stemEnum);
                        if (AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberSkeletonImpl$StemEnum[stemEnum.ordinal()] != 9) {
                            return ParseState.STATE_NULL;
                        }
                        return ParseState.STATE_FRACTION_PRECISION;
                    case STEM_ROUNDING_MODE_CEILING:
                    case STEM_ROUNDING_MODE_FLOOR:
                    case STEM_ROUNDING_MODE_DOWN:
                    case STEM_ROUNDING_MODE_UP:
                    case STEM_ROUNDING_MODE_HALF_EVEN:
                    case STEM_ROUNDING_MODE_HALF_DOWN:
                    case STEM_ROUNDING_MODE_HALF_UP:
                    case STEM_ROUNDING_MODE_UNNECESSARY:
                        checkNull(macroProps.roundingMode, stringSegment);
                        macroProps.roundingMode = StemToObject.roundingMode(stemEnum);
                        return ParseState.STATE_NULL;
                    case STEM_GROUP_OFF:
                    case STEM_GROUP_MIN2:
                    case STEM_GROUP_AUTO:
                    case STEM_GROUP_ON_ALIGNED:
                    case STEM_GROUP_THOUSANDS:
                        checkNull(macroProps.grouping, stringSegment);
                        macroProps.grouping = StemToObject.groupingStrategy(stemEnum);
                        return ParseState.STATE_NULL;
                    case STEM_UNIT_WIDTH_NARROW:
                    case STEM_UNIT_WIDTH_SHORT:
                    case STEM_UNIT_WIDTH_FULL_NAME:
                    case STEM_UNIT_WIDTH_ISO_CODE:
                    case STEM_UNIT_WIDTH_FORMAL:
                    case STEM_UNIT_WIDTH_VARIANT:
                    case STEM_UNIT_WIDTH_HIDDEN:
                        checkNull(macroProps.unitWidth, stringSegment);
                        macroProps.unitWidth = StemToObject.unitWidth(stemEnum);
                        return ParseState.STATE_NULL;
                    case STEM_SIGN_AUTO:
                    case STEM_SIGN_ALWAYS:
                    case STEM_SIGN_NEVER:
                    case STEM_SIGN_ACCOUNTING:
                    case STEM_SIGN_ACCOUNTING_ALWAYS:
                    case STEM_SIGN_EXCEPT_ZERO:
                    case STEM_SIGN_ACCOUNTING_EXCEPT_ZERO:
                        checkNull(macroProps.sign, stringSegment);
                        macroProps.sign = StemToObject.signDisplay(stemEnum);
                        return ParseState.STATE_NULL;
                    case STEM_DECIMAL_AUTO:
                    case STEM_DECIMAL_ALWAYS:
                        checkNull(macroProps.decimal, stringSegment);
                        macroProps.decimal = StemToObject.decimalSeparatorDisplay(stemEnum);
                        return ParseState.STATE_NULL;
                    case STEM_PERCENT_100:
                        checkNull(macroProps.scale, stringSegment);
                        checkNull(macroProps.unit, stringSegment);
                        macroProps.scale = Scale.powerOfTen(2);
                        macroProps.unit = NoUnit.PERCENT;
                        return ParseState.STATE_NULL;
                    case STEM_LATIN:
                        checkNull(macroProps.symbols, stringSegment);
                        macroProps.symbols = NumberingSystem.LATIN;
                        return ParseState.STATE_NULL;
                    case STEM_PRECISION_INCREMENT:
                        checkNull(macroProps.precision, stringSegment);
                        return ParseState.STATE_INCREMENT_PRECISION;
                    case STEM_MEASURE_UNIT:
                        checkNull(macroProps.unit, stringSegment);
                        return ParseState.STATE_MEASURE_UNIT;
                    case STEM_PER_MEASURE_UNIT:
                        checkNull(macroProps.perUnit, stringSegment);
                        return ParseState.STATE_PER_MEASURE_UNIT;
                    case STEM_UNIT:
                        checkNull(macroProps.unit, stringSegment);
                        checkNull(macroProps.perUnit, stringSegment);
                        return ParseState.STATE_IDENTIFIER_UNIT;
                    case STEM_CURRENCY:
                        checkNull(macroProps.unit, stringSegment);
                        return ParseState.STATE_CURRENCY_UNIT;
                    case STEM_INTEGER_WIDTH:
                        checkNull(macroProps.integerWidth, stringSegment);
                        return ParseState.STATE_INTEGER_WIDTH;
                    case STEM_NUMBERING_SYSTEM:
                        checkNull(macroProps.symbols, stringSegment);
                        return ParseState.STATE_NUMBERING_SYSTEM;
                    case STEM_SCALE:
                        checkNull(macroProps.scale, stringSegment);
                        return ParseState.STATE_SCALE;
                    default:
                        throw new AssertionError();
                }
            } else {
                throw new SkeletonSyntaxException("Unknown stem", stringSegment);
            }
        } else {
            checkNull(macroProps.notation, stringSegment);
            BlueprintHelpers.parseScientificStem(stringSegment, macroProps);
            return ParseState.STATE_NULL;
        }
    }

    private static ParseState parseOption(ParseState parseState, StringSegment stringSegment, MacroProps macroProps) {
        switch (parseState) {
            case STATE_INCREMENT_PRECISION:
                BlueprintHelpers.parseIncrementOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            case STATE_MEASURE_UNIT:
                BlueprintHelpers.parseMeasureUnitOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            case STATE_PER_MEASURE_UNIT:
                BlueprintHelpers.parseMeasurePerUnitOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            case STATE_CURRENCY_UNIT:
                BlueprintHelpers.parseCurrencyOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            case STATE_INTEGER_WIDTH:
                BlueprintHelpers.parseIntegerWidthOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            case STATE_NUMBERING_SYSTEM:
                BlueprintHelpers.parseNumberingSystemOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            case STATE_SCALE:
                BlueprintHelpers.parseScaleOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            case STATE_IDENTIFIER_UNIT:
                BlueprintHelpers.parseIdentifierUnitOption(stringSegment, macroProps);
                return ParseState.STATE_NULL;
            default:
                if (AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberSkeletonImpl$ParseState[parseState.ordinal()] == 9) {
                    if (BlueprintHelpers.parseExponentWidthOption(stringSegment, macroProps)) {
                        return ParseState.STATE_SCIENTIFIC;
                    }
                    if (BlueprintHelpers.parseExponentSignOption(stringSegment, macroProps)) {
                        return ParseState.STATE_SCIENTIFIC;
                    }
                }
                if (AnonymousClass2.$SwitchMap$ohos$global$icu$number$NumberSkeletonImpl$ParseState[parseState.ordinal()] == 10 && BlueprintHelpers.parseFracSigOption(stringSegment, macroProps)) {
                    return ParseState.STATE_NULL;
                }
                throw new SkeletonSyntaxException("Invalid option", stringSegment);
        }
    }

    private static void generateSkeleton(MacroProps macroProps, StringBuilder sb) {
        if (macroProps.notation != null && GeneratorHelpers.notation(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.unit != null && GeneratorHelpers.unit(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.perUnit != null && GeneratorHelpers.perUnit(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.precision != null && GeneratorHelpers.precision(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.roundingMode != null && GeneratorHelpers.roundingMode(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.grouping != null && GeneratorHelpers.grouping(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.integerWidth != null && GeneratorHelpers.integerWidth(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.symbols != null && GeneratorHelpers.symbols(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.unitWidth != null && GeneratorHelpers.unitWidth(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.sign != null && GeneratorHelpers.sign(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.decimal != null && GeneratorHelpers.decimal(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.scale != null && GeneratorHelpers.scale(macroProps, sb)) {
            sb.append(' ');
        }
        if (macroProps.padder != null) {
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom padder");
        } else if (macroProps.affixProvider != null) {
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom affix provider");
        } else if (macroProps.rules != null) {
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom plural rules");
        } else if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
    }

    /* access modifiers changed from: package-private */
    public static final class BlueprintHelpers {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        BlueprintHelpers() {
        }

        /* access modifiers changed from: private */
        public static boolean parseExponentWidthOption(StringSegment stringSegment, MacroProps macroProps) {
            if (!NumberSkeletonImpl.isWildcardChar(stringSegment.charAt(0))) {
                return false;
            }
            int i = 0;
            int i2 = 1;
            while (i2 < stringSegment.length() && stringSegment.charAt(i2) == 'e') {
                i++;
                i2++;
            }
            if (i2 < stringSegment.length()) {
                return false;
            }
            macroProps.notation = ((ScientificNotation) macroProps.notation).withMinExponentDigits(i);
            return true;
        }

        /* access modifiers changed from: private */
        public static void generateExponentWidthOption(int i, StringBuilder sb) {
            sb.append(NumberSkeletonImpl.WILDCARD_CHAR);
            NumberSkeletonImpl.appendMultiple(sb, 101, i);
        }

        /* access modifiers changed from: private */
        public static boolean parseExponentSignOption(StringSegment stringSegment, MacroProps macroProps) {
            NumberFormatter.SignDisplay signDisplay;
            CharsTrie charsTrie = new CharsTrie(NumberSkeletonImpl.SERIALIZED_STEM_TRIE, 0);
            BytesTrie.Result next = charsTrie.next(stringSegment, 0, stringSegment.length());
            if ((next != BytesTrie.Result.INTERMEDIATE_VALUE && next != BytesTrie.Result.FINAL_VALUE) || (signDisplay = StemToObject.signDisplay(NumberSkeletonImpl.STEM_ENUM_VALUES[charsTrie.getValue()])) == null) {
                return false;
            }
            macroProps.notation = ((ScientificNotation) macroProps.notation).withExponentSignDisplay(signDisplay);
            return true;
        }

        /* access modifiers changed from: private */
        public static void parseCurrencyOption(StringSegment stringSegment, MacroProps macroProps) {
            try {
                macroProps.unit = Currency.getInstance(stringSegment.subSequence(0, stringSegment.length()).toString());
            } catch (IllegalArgumentException e) {
                throw new SkeletonSyntaxException("Invalid currency", stringSegment, e);
            }
        }

        /* access modifiers changed from: private */
        public static void generateCurrencyOption(Currency currency, StringBuilder sb) {
            sb.append(currency.getCurrencyCode());
        }

        /* access modifiers changed from: private */
        public static void parseMeasureUnitOption(StringSegment stringSegment, MacroProps macroProps) {
            int i = 0;
            while (i < stringSegment.length() && stringSegment.charAt(i) != '-') {
                i++;
            }
            if (i != stringSegment.length()) {
                String charSequence = stringSegment.subSequence(0, i).toString();
                String charSequence2 = stringSegment.subSequence(i + 1, stringSegment.length()).toString();
                for (MeasureUnit measureUnit : MeasureUnit.getAvailable(charSequence)) {
                    if (charSequence2.equals(measureUnit.getSubtype())) {
                        macroProps.unit = measureUnit;
                        return;
                    }
                }
                throw new SkeletonSyntaxException("Unknown measure unit", stringSegment);
            }
            throw new SkeletonSyntaxException("Invalid measure unit option", stringSegment);
        }

        /* access modifiers changed from: private */
        public static void generateMeasureUnitOption(MeasureUnit measureUnit, StringBuilder sb) {
            sb.append(measureUnit.getType());
            sb.append(LanguageTag.SEP);
            sb.append(measureUnit.getSubtype());
        }

        /* access modifiers changed from: private */
        public static void parseMeasurePerUnitOption(StringSegment stringSegment, MacroProps macroProps) {
            MeasureUnit measureUnit = macroProps.unit;
            parseMeasureUnitOption(stringSegment, macroProps);
            macroProps.perUnit = macroProps.unit;
            macroProps.unit = measureUnit;
        }

        /* access modifiers changed from: private */
        public static void parseIdentifierUnitOption(StringSegment stringSegment, MacroProps macroProps) {
            MeasureUnit[] parseCoreUnitIdentifier = MeasureUnit.parseCoreUnitIdentifier(stringSegment.asString());
            if (parseCoreUnitIdentifier != null) {
                macroProps.unit = parseCoreUnitIdentifier[0];
                if (parseCoreUnitIdentifier.length == 2) {
                    macroProps.perUnit = parseCoreUnitIdentifier[1];
                    return;
                }
                return;
            }
            throw new SkeletonSyntaxException("Invalid core unit identifier", stringSegment);
        }

        /* access modifiers changed from: private */
        public static void parseFractionStem(StringSegment stringSegment, MacroProps macroProps) {
            int i;
            int i2 = 0;
            int i3 = 1;
            while (i3 < stringSegment.length() && stringSegment.charAt(i3) == '0') {
                i2++;
                i3++;
            }
            if (i3 >= stringSegment.length()) {
                i = i2;
            } else if (NumberSkeletonImpl.isWildcardChar(stringSegment.charAt(i3))) {
                i3++;
                i = -1;
            } else {
                i = i2;
                while (i3 < stringSegment.length() && stringSegment.charAt(i3) == '#') {
                    i++;
                    i3++;
                }
            }
            if (i3 < stringSegment.length()) {
                throw new SkeletonSyntaxException("Invalid fraction stem", stringSegment);
            } else if (i != -1) {
                macroProps.precision = Precision.minMaxFraction(i2, i);
            } else if (i2 == 0) {
                macroProps.precision = Precision.unlimited();
            } else {
                macroProps.precision = Precision.minFraction(i2);
            }
        }

        /* access modifiers changed from: private */
        public static void generateFractionStem(int i, int i2, StringBuilder sb) {
            if (i == 0 && i2 == 0) {
                sb.append("precision-integer");
                return;
            }
            sb.append('.');
            NumberSkeletonImpl.appendMultiple(sb, 48, i);
            if (i2 == -1) {
                sb.append(NumberSkeletonImpl.WILDCARD_CHAR);
            } else {
                NumberSkeletonImpl.appendMultiple(sb, 35, i2 - i);
            }
        }

        /* access modifiers changed from: private */
        public static void parseDigitsStem(StringSegment stringSegment, MacroProps macroProps) {
            int i;
            int i2 = 0;
            int i3 = 0;
            while (i2 < stringSegment.length() && stringSegment.charAt(i2) == '@') {
                i3++;
                i2++;
            }
            if (i2 >= stringSegment.length()) {
                i = i3;
            } else if (NumberSkeletonImpl.isWildcardChar(stringSegment.charAt(i2))) {
                i2++;
                i = -1;
            } else {
                i = i3;
                while (i2 < stringSegment.length() && stringSegment.charAt(i2) == '#') {
                    i++;
                    i2++;
                }
            }
            if (i2 < stringSegment.length()) {
                throw new SkeletonSyntaxException("Invalid significant digits stem", stringSegment);
            } else if (i == -1) {
                macroProps.precision = Precision.minSignificantDigits(i3);
            } else {
                macroProps.precision = Precision.minMaxSignificantDigits(i3, i);
            }
        }

        /* access modifiers changed from: private */
        public static void generateDigitsStem(int i, int i2, StringBuilder sb) {
            NumberSkeletonImpl.appendMultiple(sb, 64, i);
            if (i2 == -1) {
                sb.append(NumberSkeletonImpl.WILDCARD_CHAR);
            } else {
                NumberSkeletonImpl.appendMultiple(sb, 35, i2 - i);
            }
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0047, code lost:
            if (r7.length() != r0) goto L_0x0049;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void parseScientificStem(ohos.global.icu.impl.StringSegment r7, ohos.global.icu.impl.number.MacroProps r8) {
            /*
            // Method dump skipped, instructions count: 122
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.number.NumberSkeletonImpl.BlueprintHelpers.parseScientificStem(ohos.global.icu.impl.StringSegment, ohos.global.icu.impl.number.MacroProps):void");
        }

        /* access modifiers changed from: private */
        public static void parseIntegerStem(StringSegment stringSegment, MacroProps macroProps) {
            int i = 1;
            while (true) {
                if (i >= stringSegment.length()) {
                    break;
                } else if (stringSegment.charAt(i) != '0') {
                    i--;
                    break;
                } else {
                    i++;
                }
            }
            if (i >= stringSegment.length()) {
                macroProps.integerWidth = IntegerWidth.zeroFillTo(i);
                return;
            }
            throw new SkeletonSyntaxException("Invalid integer stem", stringSegment);
        }

        /* access modifiers changed from: private */
        public static boolean parseFracSigOption(StringSegment stringSegment, MacroProps macroProps) {
            int i;
            int i2 = 0;
            if (stringSegment.charAt(0) != '@') {
                return false;
            }
            int i3 = 0;
            while (i2 < stringSegment.length() && stringSegment.charAt(i2) == '@') {
                i3++;
                i2++;
            }
            if (i2 < stringSegment.length()) {
                if (NumberSkeletonImpl.isWildcardChar(stringSegment.charAt(i2))) {
                    i2++;
                    i = -1;
                } else if (i3 <= 1) {
                    i = i3;
                    while (i2 < stringSegment.length() && stringSegment.charAt(i2) == '#') {
                        i++;
                        i2++;
                    }
                } else {
                    throw new SkeletonSyntaxException("Invalid digits option for fraction rounder", stringSegment);
                }
                if (i2 >= stringSegment.length()) {
                    FractionPrecision fractionPrecision = (FractionPrecision) macroProps.precision;
                    if (i == -1) {
                        macroProps.precision = fractionPrecision.withMinDigits(i3);
                    } else {
                        macroProps.precision = fractionPrecision.withMaxDigits(i);
                    }
                    return true;
                }
                throw new SkeletonSyntaxException("Invalid digits option for fraction rounder", stringSegment);
            }
            throw new SkeletonSyntaxException("Invalid digits option for fraction rounder", stringSegment);
        }

        /* access modifiers changed from: private */
        public static void parseIncrementOption(StringSegment stringSegment, MacroProps macroProps) {
            try {
                macroProps.precision = Precision.increment(new BigDecimal(stringSegment.subSequence(0, stringSegment.length()).toString()));
            } catch (NumberFormatException e) {
                throw new SkeletonSyntaxException("Invalid rounding increment", stringSegment, e);
            }
        }

        /* access modifiers changed from: private */
        public static void generateIncrementOption(BigDecimal bigDecimal, StringBuilder sb) {
            sb.append(bigDecimal.toPlainString());
        }

        /* access modifiers changed from: private */
        public static void parseIntegerWidthOption(StringSegment stringSegment, MacroProps macroProps) {
            int i;
            int i2;
            int i3 = 0;
            if (NumberSkeletonImpl.isWildcardChar(stringSegment.charAt(0))) {
                i2 = 1;
                i = -1;
            } else {
                i2 = 0;
                i = 0;
            }
            while (i2 < stringSegment.length() && i != -1 && stringSegment.charAt(i2) == '#') {
                i++;
                i2++;
            }
            if (i2 < stringSegment.length()) {
                while (i2 < stringSegment.length() && stringSegment.charAt(i2) == '0') {
                    i3++;
                    i2++;
                }
            }
            if (i != -1) {
                i += i3;
            }
            if (i2 < stringSegment.length()) {
                throw new SkeletonSyntaxException("Invalid integer width stem", stringSegment);
            } else if (i == -1) {
                macroProps.integerWidth = IntegerWidth.zeroFillTo(i3);
            } else {
                macroProps.integerWidth = IntegerWidth.zeroFillTo(i3).truncateAt(i);
            }
        }

        /* access modifiers changed from: private */
        public static void generateIntegerWidthOption(int i, int i2, StringBuilder sb) {
            if (i2 == -1) {
                sb.append(NumberSkeletonImpl.WILDCARD_CHAR);
            } else {
                NumberSkeletonImpl.appendMultiple(sb, 35, i2 - i);
            }
            NumberSkeletonImpl.appendMultiple(sb, 48, i);
        }

        /* access modifiers changed from: private */
        public static void parseNumberingSystemOption(StringSegment stringSegment, MacroProps macroProps) {
            NumberingSystem instanceByName = NumberingSystem.getInstanceByName(stringSegment.subSequence(0, stringSegment.length()).toString());
            if (instanceByName != null) {
                macroProps.symbols = instanceByName;
                return;
            }
            throw new SkeletonSyntaxException("Unknown numbering system", stringSegment);
        }

        /* access modifiers changed from: private */
        public static void generateNumberingSystemOption(NumberingSystem numberingSystem, StringBuilder sb) {
            sb.append(numberingSystem.getName());
        }

        /* access modifiers changed from: private */
        public static void parseScaleOption(StringSegment stringSegment, MacroProps macroProps) {
            try {
                macroProps.scale = Scale.byBigDecimal(new BigDecimal(stringSegment.subSequence(0, stringSegment.length()).toString()));
            } catch (NumberFormatException e) {
                throw new SkeletonSyntaxException("Invalid scale", stringSegment, e);
            }
        }

        /* access modifiers changed from: private */
        public static void generateScaleOption(Scale scale, StringBuilder sb) {
            BigDecimal bigDecimal = scale.arbitrary;
            if (bigDecimal == null) {
                bigDecimal = BigDecimal.ONE;
            }
            sb.append(bigDecimal.scaleByPowerOfTen(scale.magnitude).toPlainString());
        }
    }

    /* access modifiers changed from: package-private */
    public static final class GeneratorHelpers {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        GeneratorHelpers() {
        }

        /* access modifiers changed from: private */
        public static boolean notation(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.notation instanceof CompactNotation) {
                if (macroProps.notation == Notation.compactLong()) {
                    sb.append("compact-long");
                    return true;
                } else if (macroProps.notation == Notation.compactShort()) {
                    sb.append("compact-short");
                    return true;
                } else {
                    throw new UnsupportedOperationException("Cannot generate number skeleton with custom compact data");
                }
            } else if (!(macroProps.notation instanceof ScientificNotation)) {
                return false;
            } else {
                ScientificNotation scientificNotation = (ScientificNotation) macroProps.notation;
                if (scientificNotation.engineeringInterval == 3) {
                    sb.append("engineering");
                } else {
                    sb.append("scientific");
                }
                if (scientificNotation.minExponentDigits > 1) {
                    sb.append('/');
                    BlueprintHelpers.generateExponentWidthOption(scientificNotation.minExponentDigits, sb);
                }
                if (scientificNotation.exponentSignDisplay != NumberFormatter.SignDisplay.AUTO) {
                    sb.append('/');
                    EnumToStemString.signDisplay(scientificNotation.exponentSignDisplay, sb);
                }
                return true;
            }
        }

        /* access modifiers changed from: private */
        public static boolean unit(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.unit instanceof Currency) {
                sb.append("currency/");
                BlueprintHelpers.generateCurrencyOption((Currency) macroProps.unit, sb);
                return true;
            } else if (!(macroProps.unit instanceof NoUnit)) {
                sb.append("measure-unit/");
                BlueprintHelpers.generateMeasureUnitOption(macroProps.unit, sb);
                return true;
            } else if (macroProps.unit == NoUnit.PERCENT) {
                sb.append(Constants.ATTRNAME_PERCENT);
                return true;
            } else if (macroProps.unit != NoUnit.PERMILLE) {
                return false;
            } else {
                sb.append("permille");
                return true;
            }
        }

        /* access modifiers changed from: private */
        public static boolean perUnit(MacroProps macroProps, StringBuilder sb) {
            if ((macroProps.perUnit instanceof Currency) || (macroProps.perUnit instanceof NoUnit)) {
                throw new UnsupportedOperationException("Cannot generate number skeleton with per-unit that is not a standard measure unit");
            }
            sb.append("per-measure-unit/");
            BlueprintHelpers.generateMeasureUnitOption(macroProps.perUnit, sb);
            return true;
        }

        /* access modifiers changed from: private */
        public static boolean precision(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.precision instanceof Precision.InfiniteRounderImpl) {
                sb.append("precision-unlimited");
            } else if (macroProps.precision instanceof Precision.FractionRounderImpl) {
                Precision.FractionRounderImpl fractionRounderImpl = (Precision.FractionRounderImpl) macroProps.precision;
                BlueprintHelpers.generateFractionStem(fractionRounderImpl.minFrac, fractionRounderImpl.maxFrac, sb);
            } else if (macroProps.precision instanceof Precision.SignificantRounderImpl) {
                Precision.SignificantRounderImpl significantRounderImpl = (Precision.SignificantRounderImpl) macroProps.precision;
                BlueprintHelpers.generateDigitsStem(significantRounderImpl.minSig, significantRounderImpl.maxSig, sb);
            } else if (macroProps.precision instanceof Precision.FracSigRounderImpl) {
                Precision.FracSigRounderImpl fracSigRounderImpl = (Precision.FracSigRounderImpl) macroProps.precision;
                BlueprintHelpers.generateFractionStem(fracSigRounderImpl.minFrac, fracSigRounderImpl.maxFrac, sb);
                sb.append('/');
                if (fracSigRounderImpl.minSig == -1) {
                    BlueprintHelpers.generateDigitsStem(1, fracSigRounderImpl.maxSig, sb);
                } else {
                    BlueprintHelpers.generateDigitsStem(fracSigRounderImpl.minSig, -1, sb);
                }
            } else if (macroProps.precision instanceof Precision.IncrementRounderImpl) {
                sb.append("precision-increment/");
                BlueprintHelpers.generateIncrementOption(((Precision.IncrementRounderImpl) macroProps.precision).increment, sb);
            } else if (((Precision.CurrencyRounderImpl) macroProps.precision).usage == Currency.CurrencyUsage.STANDARD) {
                sb.append("precision-currency-standard");
            } else {
                sb.append("precision-currency-cash");
            }
            return true;
        }

        /* access modifiers changed from: private */
        public static boolean roundingMode(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.roundingMode == RoundingUtils.DEFAULT_ROUNDING_MODE) {
                return false;
            }
            EnumToStemString.roundingMode(macroProps.roundingMode, sb);
            return true;
        }

        /* access modifiers changed from: private */
        public static boolean grouping(MacroProps macroProps, StringBuilder sb) {
            if (!(macroProps.grouping instanceof NumberFormatter.GroupingStrategy)) {
                throw new UnsupportedOperationException("Cannot generate number skeleton with custom Grouper");
            } else if (macroProps.grouping == NumberFormatter.GroupingStrategy.AUTO) {
                return false;
            } else {
                EnumToStemString.groupingStrategy((NumberFormatter.GroupingStrategy) macroProps.grouping, sb);
                return true;
            }
        }

        /* access modifiers changed from: private */
        public static boolean integerWidth(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.integerWidth.equals(IntegerWidth.DEFAULT)) {
                return false;
            }
            sb.append("integer-width/");
            BlueprintHelpers.generateIntegerWidthOption(macroProps.integerWidth.minInt, macroProps.integerWidth.maxInt, sb);
            return true;
        }

        /* access modifiers changed from: private */
        public static boolean symbols(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.symbols instanceof NumberingSystem) {
                NumberingSystem numberingSystem = (NumberingSystem) macroProps.symbols;
                if (numberingSystem.getName().equals("latn")) {
                    sb.append("latin");
                    return true;
                }
                sb.append("numbering-system/");
                BlueprintHelpers.generateNumberingSystemOption(numberingSystem, sb);
                return true;
            }
            throw new UnsupportedOperationException("Cannot generate number skeleton with custom DecimalFormatSymbols");
        }

        /* access modifiers changed from: private */
        public static boolean unitWidth(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.unitWidth == NumberFormatter.UnitWidth.SHORT) {
                return false;
            }
            EnumToStemString.unitWidth(macroProps.unitWidth, sb);
            return true;
        }

        /* access modifiers changed from: private */
        public static boolean sign(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.sign == NumberFormatter.SignDisplay.AUTO) {
                return false;
            }
            EnumToStemString.signDisplay(macroProps.sign, sb);
            return true;
        }

        /* access modifiers changed from: private */
        public static boolean decimal(MacroProps macroProps, StringBuilder sb) {
            if (macroProps.decimal == NumberFormatter.DecimalSeparatorDisplay.AUTO) {
                return false;
            }
            EnumToStemString.decimalSeparatorDisplay(macroProps.decimal, sb);
            return true;
        }

        /* access modifiers changed from: private */
        public static boolean scale(MacroProps macroProps, StringBuilder sb) {
            if (!macroProps.scale.isValid()) {
                return false;
            }
            sb.append("scale/");
            BlueprintHelpers.generateScaleOption(macroProps.scale, sb);
            return true;
        }
    }

    private static void checkNull(Object obj, CharSequence charSequence) {
        if (obj != null) {
            throw new SkeletonSyntaxException("Duplicated setting", charSequence);
        }
    }

    /* access modifiers changed from: private */
    public static void appendMultiple(StringBuilder sb, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            sb.appendCodePoint(i);
        }
    }
}
