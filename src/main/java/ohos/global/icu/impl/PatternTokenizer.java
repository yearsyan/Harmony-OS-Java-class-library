package ohos.global.icu.impl;

import ohos.global.icu.text.UTF16;
import ohos.global.icu.text.UnicodeSet;

public class PatternTokenizer {
    private static final int AFTER_QUOTE = -1;
    public static final char BACK_SLASH = '\\';
    public static final int BROKEN_ESCAPE = 4;
    public static final int BROKEN_QUOTE = 3;
    public static final int DONE = 0;
    private static final int HEX = 4;
    private static int IN_QUOTE = -2;
    public static final int LITERAL = 2;
    private static final int NONE = 0;
    private static final int NORMAL_QUOTE = 2;
    private static int NO_QUOTE = -1;
    public static final char SINGLE_QUOTE = '\'';
    private static final int SLASH_START = 3;
    private static final int START_QUOTE = 1;
    public static final int SYNTAX = 1;
    public static final int UNKNOWN = 5;
    private UnicodeSet escapeCharacters = new UnicodeSet();
    private UnicodeSet extraQuotingCharacters = new UnicodeSet();
    private UnicodeSet ignorableCharacters = new UnicodeSet();
    private int limit;
    private transient UnicodeSet needingQuoteCharacters = null;
    private String pattern;
    private int start;
    private UnicodeSet syntaxCharacters = new UnicodeSet();
    private boolean usingQuote = false;
    private boolean usingSlash = false;

    public UnicodeSet getIgnorableCharacters() {
        return (UnicodeSet) this.ignorableCharacters.clone();
    }

    public PatternTokenizer setIgnorableCharacters(UnicodeSet unicodeSet) {
        this.ignorableCharacters = (UnicodeSet) unicodeSet.clone();
        this.needingQuoteCharacters = null;
        return this;
    }

    public UnicodeSet getSyntaxCharacters() {
        return (UnicodeSet) this.syntaxCharacters.clone();
    }

    public UnicodeSet getExtraQuotingCharacters() {
        return (UnicodeSet) this.extraQuotingCharacters.clone();
    }

    public PatternTokenizer setSyntaxCharacters(UnicodeSet unicodeSet) {
        this.syntaxCharacters = (UnicodeSet) unicodeSet.clone();
        this.needingQuoteCharacters = null;
        return this;
    }

    public PatternTokenizer setExtraQuotingCharacters(UnicodeSet unicodeSet) {
        this.extraQuotingCharacters = (UnicodeSet) unicodeSet.clone();
        this.needingQuoteCharacters = null;
        return this;
    }

    public UnicodeSet getEscapeCharacters() {
        return (UnicodeSet) this.escapeCharacters.clone();
    }

    public PatternTokenizer setEscapeCharacters(UnicodeSet unicodeSet) {
        this.escapeCharacters = (UnicodeSet) unicodeSet.clone();
        return this;
    }

    public boolean isUsingQuote() {
        return this.usingQuote;
    }

    public PatternTokenizer setUsingQuote(boolean z) {
        this.usingQuote = z;
        this.needingQuoteCharacters = null;
        return this;
    }

    public boolean isUsingSlash() {
        return this.usingSlash;
    }

    public PatternTokenizer setUsingSlash(boolean z) {
        this.usingSlash = z;
        this.needingQuoteCharacters = null;
        return this;
    }

    public int getLimit() {
        return this.limit;
    }

    public PatternTokenizer setLimit(int i) {
        this.limit = i;
        return this;
    }

    public int getStart() {
        return this.start;
    }

    public PatternTokenizer setStart(int i) {
        this.start = i;
        return this;
    }

    public PatternTokenizer setPattern(CharSequence charSequence) {
        return setPattern(charSequence.toString());
    }

    public PatternTokenizer setPattern(String str) {
        if (str != null) {
            this.start = 0;
            this.limit = str.length();
            this.pattern = str;
            return this;
        }
        throw new IllegalArgumentException("Inconsistent arguments");
    }

    public String quoteLiteral(CharSequence charSequence) {
        return quoteLiteral(charSequence.toString());
    }

    public String quoteLiteral(String str) {
        if (this.needingQuoteCharacters == null) {
            this.needingQuoteCharacters = new UnicodeSet().addAll(this.syntaxCharacters).addAll(this.ignorableCharacters).addAll(this.extraQuotingCharacters);
            if (this.usingSlash) {
                this.needingQuoteCharacters.add(92);
            }
            if (this.usingQuote) {
                this.needingQuoteCharacters.add(39);
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = NO_QUOTE;
        int i2 = 0;
        while (i2 < str.length()) {
            int charAt = UTF16.charAt(str, i2);
            if (this.escapeCharacters.contains(charAt)) {
                if (i == IN_QUOTE) {
                    stringBuffer.append(SINGLE_QUOTE);
                    i = NO_QUOTE;
                }
                appendEscaped(stringBuffer, charAt);
            } else if (!this.needingQuoteCharacters.contains(charAt)) {
                if (i == IN_QUOTE) {
                    stringBuffer.append(SINGLE_QUOTE);
                    i = NO_QUOTE;
                }
                UTF16.append(stringBuffer, charAt);
            } else if (i == IN_QUOTE) {
                UTF16.append(stringBuffer, charAt);
                if (this.usingQuote && charAt == 39) {
                    stringBuffer.append(SINGLE_QUOTE);
                }
            } else if (this.usingSlash) {
                stringBuffer.append(BACK_SLASH);
                UTF16.append(stringBuffer, charAt);
            } else if (!this.usingQuote) {
                appendEscaped(stringBuffer, charAt);
            } else if (charAt == 39) {
                stringBuffer.append(SINGLE_QUOTE);
                stringBuffer.append(SINGLE_QUOTE);
            } else {
                stringBuffer.append(SINGLE_QUOTE);
                UTF16.append(stringBuffer, charAt);
                i = IN_QUOTE;
            }
            i2 += UTF16.getCharCount(charAt);
        }
        if (i == IN_QUOTE) {
            stringBuffer.append(SINGLE_QUOTE);
        }
        return stringBuffer.toString();
    }

    private void appendEscaped(StringBuffer stringBuffer, int i) {
        if (i <= 65535) {
            stringBuffer.append("\\u");
            stringBuffer.append(Utility.hex((long) i, 4));
            return;
        }
        stringBuffer.append("\\U");
        stringBuffer.append(Utility.hex((long) i, 8));
    }

    public String normalize() {
        int i = this.start;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        while (true) {
            stringBuffer2.setLength(0);
            int next = next(stringBuffer2);
            if (next == 0) {
                this.start = i;
                return stringBuffer.toString();
            } else if (next != 1) {
                stringBuffer.append(quoteLiteral(stringBuffer2));
            } else {
                stringBuffer.append(stringBuffer2);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x0093  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x00bf A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int next(java.lang.StringBuffer r17) {
        /*
        // Method dump skipped, instructions count: 278
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.PatternTokenizer.next(java.lang.StringBuffer):int");
    }
}
