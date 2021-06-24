package ohos.utils.fastjson.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import ohos.agp.window.service.WindowManager;
import ohos.com.sun.org.apache.xml.internal.serializer.CharInfo;
import ohos.global.icu.impl.PatternTokenizer;
import ohos.utils.fastjson.JSON;
import ohos.utils.fastjson.JSONException;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

public final class JSONLexer {
    public static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    public static final int END = 4;
    public static final char EOI = 26;
    static final int[] IA = new int[256];
    public static final int NOT_MATCH = -1;
    public static final int NOT_MATCH_NAME = -2;
    public static final int UNKNOWN = 0;
    private static boolean V6 = false;
    public static final int VALUE = 3;
    protected static final int[] digits = new int[103];
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];
    private static final ThreadLocal<char[]> sbufLocal = new ThreadLocal<>();
    protected int bp;
    public Calendar calendar;
    protected char ch;
    public boolean disableCircularReferenceDetect;
    protected int eofPos;
    protected boolean exp;
    public int features;
    protected long fieldHash;
    protected boolean hasSpecial;
    protected boolean isDouble;
    protected final int len;
    public Locale locale;
    public int matchStat;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected String stringDefaultValue;
    protected final String text;
    public TimeZone timeZone;
    protected int token;

    static boolean checkDate(char c, char c2, char c3, char c4, char c5, char c6, int i, int i2) {
        if (c >= '1' && c <= '3' && c2 >= '0' && c2 <= '9' && c3 >= '0' && c3 <= '9' && c4 >= '0' && c4 <= '9') {
            if (c5 == '0') {
                if (c6 < '1' || c6 > '9') {
                    return false;
                }
            } else if (!(c5 == '1' && (c6 == '0' || c6 == '1' || c6 == '2'))) {
                return false;
            }
            return i == 48 ? i2 >= 49 && i2 <= 57 : (i == 49 || i == 50) ? i2 >= 48 && i2 <= 57 : i == 51 && (i2 == 48 || i2 == 49);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
        if (r5 <= '4') goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean checkTime(char r4, char r5, char r6, char r7, char r8, char r9) {
        /*
            r0 = 57
            r1 = 0
            r2 = 48
            if (r4 != r2) goto L_0x000c
            if (r5 < r2) goto L_0x000b
            if (r5 <= r0) goto L_0x0020
        L_0x000b:
            return r1
        L_0x000c:
            r3 = 49
            if (r4 != r3) goto L_0x0015
            if (r5 < r2) goto L_0x0014
            if (r5 <= r0) goto L_0x0020
        L_0x0014:
            return r1
        L_0x0015:
            r3 = 50
            if (r4 != r3) goto L_0x0042
            if (r5 < r2) goto L_0x0042
            r4 = 52
            if (r5 <= r4) goto L_0x0020
            goto L_0x0042
        L_0x0020:
            r4 = 53
            r5 = 54
            if (r6 < r2) goto L_0x002d
            if (r6 > r4) goto L_0x002d
            if (r7 < r2) goto L_0x002c
            if (r7 <= r0) goto L_0x0032
        L_0x002c:
            return r1
        L_0x002d:
            if (r6 != r5) goto L_0x0042
            if (r7 == r2) goto L_0x0032
            return r1
        L_0x0032:
            if (r8 < r2) goto L_0x003b
            if (r8 > r4) goto L_0x003b
            if (r9 < r2) goto L_0x003a
            if (r9 <= r0) goto L_0x0040
        L_0x003a:
            return r1
        L_0x003b:
            if (r8 != r5) goto L_0x0042
            if (r9 == r2) goto L_0x0040
            return r1
        L_0x0040:
            r4 = 1
            return r4
        L_0x0042:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.checkTime(char, char, char, char, char, char):boolean");
    }

    static {
        int i;
        try {
            i = Class.forName("android.os.Build$VERSION").getField("SDK_INT").getInt(null);
        } catch (Exception unused) {
            i = -1;
        }
        char c = 0;
        V6 = i >= 23;
        for (int i2 = 48; i2 <= 57; i2++) {
            digits[i2] = i2 - 48;
        }
        for (int i3 = 97; i3 <= 102; i3++) {
            digits[i3] = (i3 - 97) + 10;
        }
        for (int i4 = 65; i4 <= 70; i4++) {
            digits[i4] = (i4 - 65) + 10;
        }
        Arrays.fill(IA, -1);
        int length = CA.length;
        for (int i5 = 0; i5 < length; i5++) {
            IA[CA[i5]] = i5;
        }
        IA[61] = 0;
        char c2 = 0;
        while (true) {
            boolean[] zArr = firstIdentifierFlags;
            if (c2 >= zArr.length) {
                break;
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                zArr[c2] = true;
            } else if (c2 >= 'a' && c2 <= 'z') {
                firstIdentifierFlags[c2] = true;
            } else if (c2 == '_') {
                firstIdentifierFlags[c2] = true;
            }
            c2 = (char) (c2 + 1);
        }
        while (true) {
            boolean[] zArr2 = identifierFlags;
            if (c < zArr2.length) {
                if (c >= 'A' && c <= 'Z') {
                    zArr2[c] = true;
                } else if (c >= 'a' && c <= 'z') {
                    identifierFlags[c] = true;
                } else if (c == '_') {
                    identifierFlags[c] = true;
                } else if (c >= '0' && c <= '9') {
                    identifierFlags[c] = true;
                }
                c = (char) (c + 1);
            } else {
                return;
            }
        }
    }

    public JSONLexer(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    public JSONLexer(String str, int i) {
        char c;
        this.features = JSON.DEFAULT_PARSER_FEATURE;
        boolean z = false;
        this.exp = false;
        this.isDouble = false;
        this.timeZone = JSON.defaultTimeZone;
        this.locale = JSON.defaultLocale;
        String str2 = null;
        this.calendar = null;
        this.matchStat = 0;
        this.sbuf = sbufLocal.get();
        if (this.sbuf == null) {
            this.sbuf = new char[512];
        }
        this.features = i;
        this.text = str;
        this.len = this.text.length();
        this.bp = -1;
        int i2 = this.bp + 1;
        this.bp = i2;
        if (i2 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i2);
        }
        this.ch = c;
        if (this.ch == 65279) {
            next();
        }
        this.stringDefaultValue = (Feature.InitStringFieldAsEmpty.mask & i) != 0 ? "" : str2;
        this.disableCircularReferenceDetect = (Feature.DisableCircularReferenceDetect.mask & i) != 0 ? true : z;
    }

    public final int token() {
        return this.token;
    }

    public void close() {
        char[] cArr = this.sbuf;
        if (cArr.length <= 8196) {
            sbufLocal.set(cArr);
        }
        this.sbuf = null;
    }

    public char next() {
        char c;
        int i = this.bp + 1;
        this.bp = i;
        if (i >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i);
        }
        this.ch = c;
        return c;
    }

    public final void config(Feature feature, boolean z) {
        if (z) {
            this.features |= feature.mask;
        } else {
            this.features &= ~feature.mask;
        }
        if (feature == Feature.InitStringFieldAsEmpty) {
            this.stringDefaultValue = z ? "" : null;
        }
        this.disableCircularReferenceDetect = (this.features & Feature.DisableCircularReferenceDetect.mask) != 0;
    }

    public final boolean isEnabled(Feature feature) {
        return (this.features & feature.mask) != 0;
    }

    public final void nextTokenWithChar(char c) {
        char c2;
        this.sp = 0;
        while (true) {
            char c3 = this.ch;
            if (c3 == c) {
                int i = this.bp + 1;
                this.bp = i;
                if (i >= this.len) {
                    c2 = EOI;
                } else {
                    c2 = this.text.charAt(i);
                }
                this.ch = c2;
                nextToken();
                return;
            } else if (c3 == ' ' || c3 == '\n' || c3 == '\r' || c3 == '\t' || c3 == '\f' || c3 == '\b') {
                next();
            } else {
                throw new JSONException("not match " + c + " - " + this.ch);
            }
        }
    }

    public final String numberString() {
        char charAt = this.text.charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        return subString(this.np, i);
    }

    /* access modifiers changed from: protected */
    public char charAt(int i) {
        if (i >= this.len) {
            return EOI;
        }
        return this.text.charAt(i);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0027, code lost:
        scanNumber();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void nextToken() {
        /*
        // Method dump skipped, instructions count: 524
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.nextToken():void");
    }

    public final void nextToken(int i) {
        this.sp = 0;
        while (true) {
            if (i != 2) {
                char c = EOI;
                if (i == 4) {
                    char c2 = this.ch;
                    if (c2 == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (c2 >= '0' && c2 <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        int i2 = this.bp + 1;
                        this.bp = i2;
                        if (i2 < this.len) {
                            c = this.text.charAt(i2);
                        }
                        this.ch = c;
                        return;
                    }
                } else if (i == 12) {
                    char c3 = this.ch;
                    if (c3 == '{') {
                        this.token = 12;
                        int i3 = this.bp + 1;
                        this.bp = i3;
                        if (i3 < this.len) {
                            c = this.text.charAt(i3);
                        }
                        this.ch = c;
                        return;
                    } else if (c3 == '[') {
                        this.token = 14;
                        int i4 = this.bp + 1;
                        this.bp = i4;
                        if (i4 < this.len) {
                            c = this.text.charAt(i4);
                        }
                        this.ch = c;
                        return;
                    }
                } else if (i != 18) {
                    if (i != 20) {
                        switch (i) {
                            case 14:
                                char c4 = this.ch;
                                if (c4 == '[') {
                                    this.token = 14;
                                    next();
                                    return;
                                } else if (c4 == '{') {
                                    this.token = 12;
                                    next();
                                    return;
                                }
                                break;
                            case 15:
                                if (this.ch == ']') {
                                    this.token = 15;
                                    next();
                                    return;
                                }
                                break;
                            case 16:
                                char c5 = this.ch;
                                if (c5 == ',') {
                                    this.token = 16;
                                    int i5 = this.bp + 1;
                                    this.bp = i5;
                                    if (i5 < this.len) {
                                        c = this.text.charAt(i5);
                                    }
                                    this.ch = c;
                                    return;
                                } else if (c5 == '}') {
                                    this.token = 13;
                                    int i6 = this.bp + 1;
                                    this.bp = i6;
                                    if (i6 < this.len) {
                                        c = this.text.charAt(i6);
                                    }
                                    this.ch = c;
                                    return;
                                } else if (c5 == ']') {
                                    this.token = 15;
                                    int i7 = this.bp + 1;
                                    this.bp = i7;
                                    if (i7 < this.len) {
                                        c = this.text.charAt(i7);
                                    }
                                    this.ch = c;
                                    return;
                                } else if (c5 == 26) {
                                    this.token = 20;
                                    return;
                                }
                                break;
                        }
                    }
                    if (this.ch == 26) {
                        this.token = 20;
                        return;
                    }
                } else {
                    nextIdent();
                    return;
                }
            } else {
                char c6 = this.ch;
                if (c6 < '0' || c6 > '9') {
                    char c7 = this.ch;
                    if (c7 == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (c7 == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (c7 == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                } else {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                }
            }
            char c8 = this.ch;
            if (c8 == ' ' || c8 == '\n' || c8 == '\r' || c8 == '\t' || c8 == '\f' || c8 == '\b') {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public final void nextIdent() {
        while (true) {
            char c = this.ch;
            if (!(c <= ' ' && (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b'))) {
                break;
            }
            next();
        }
        char c2 = this.ch;
        if (c2 == '_' || Character.isLetter(c2)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final Number integerValue() throws NumberFormatException {
        char c;
        char c2;
        char c3;
        boolean z;
        long j;
        long j2;
        char c4;
        char c5;
        int i = this.np;
        int i2 = this.sp + i;
        int i3 = i2 - 1;
        if (i3 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i3);
        }
        if (c == 'B') {
            i2--;
            c2 = 'B';
        } else if (c == 'L') {
            i2--;
            c2 = 'L';
        } else if (c != 'S') {
            c2 = ' ';
        } else {
            i2--;
            c2 = 'S';
        }
        int i4 = this.np;
        if (i4 >= this.len) {
            c3 = EOI;
        } else {
            c3 = this.text.charAt(i4);
        }
        if (c3 == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = -9223372036854775807L;
            z = false;
        }
        if (i < i2) {
            int i5 = i + 1;
            if (i >= this.len) {
                c5 = EOI;
            } else {
                c5 = this.text.charAt(i);
            }
            j2 = (long) (-(c5 - '0'));
            i = i5;
        } else {
            j2 = 0;
        }
        while (i < i2) {
            int i6 = i + 1;
            if (i >= this.len) {
                c4 = EOI;
            } else {
                c4 = this.text.charAt(i);
            }
            int i7 = c4 - '0';
            if (j2 < -922337203685477580L) {
                return new BigInteger(numberString());
            }
            long j3 = j2 * 10;
            long j4 = (long) i7;
            if (j3 < j + j4) {
                return new BigInteger(numberString());
            }
            j2 = j3 - j4;
            i = i6;
        }
        if (!z) {
            long j5 = -j2;
            if (j5 > 2147483647L || c2 == 'L') {
                return Long.valueOf(j5);
            }
            if (c2 == 'S') {
                return Short.valueOf((short) ((int) j5));
            }
            if (c2 == 'B') {
                return Byte.valueOf((byte) ((int) j5));
            }
            return Integer.valueOf((int) j5);
        } else if (i <= this.np + 1) {
            throw new NumberFormatException(numberString());
        } else if (j2 < -2147483648L || c2 == 'L') {
            return Long.valueOf(j2);
        } else {
            if (c2 == 'S') {
                return Short.valueOf((short) ((int) j2));
            }
            if (c2 == 'B') {
                return Byte.valueOf((byte) ((int) j2));
            }
            return Integer.valueOf((int) j2);
        }
    }

    public final String scanSymbol(SymbolTable symbolTable) {
        char c;
        while (true) {
            c = this.ch;
            if (c != ' ' && c != '\n' && c != '\r' && c != '\t' && c != '\f' && c != '\b') {
                break;
            }
            next();
        }
        if (c == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (c == '\'') {
            return scanSymbol(symbolTable, PatternTokenizer.SINGLE_QUOTE);
        }
        if (c == '}') {
            next();
            this.token = 13;
            return null;
        } else if (c == ',') {
            next();
            this.token = 16;
            return null;
        } else if (c != 26) {
            return scanSymbolUnQuoted(symbolTable);
        } else {
            this.token = 20;
            return null;
        }
    }

    public String scanSymbol(SymbolTable symbolTable, char c) {
        String str;
        char c2;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c, i);
        if (indexOf != -1) {
            int i2 = indexOf - i;
            char[] sub_chars = sub_chars(this.bp + 1, i2);
            int i3 = indexOf;
            boolean z = false;
            while (i2 > 0 && sub_chars[i2 - 1] == '\\') {
                int i4 = i2 - 2;
                int i5 = 1;
                while (i4 >= 0 && sub_chars[i4] == '\\') {
                    i5++;
                    i4--;
                }
                if (i5 % 2 == 0) {
                    break;
                }
                int indexOf2 = this.text.indexOf(c, i3 + 1);
                int i6 = (indexOf2 - i3) + i2;
                if (i6 >= sub_chars.length) {
                    int length = (sub_chars.length * 3) / 2;
                    if (length < i6) {
                        length = i6;
                    }
                    char[] cArr = new char[length];
                    System.arraycopy(sub_chars, 0, cArr, 0, sub_chars.length);
                    sub_chars = cArr;
                }
                this.text.getChars(i3, indexOf2, sub_chars, i2);
                i3 = indexOf2;
                i2 = i6;
                z = true;
            }
            if (!z) {
                int i7 = 0;
                for (int i8 = 0; i8 < i2; i8++) {
                    char c3 = sub_chars[i8];
                    i7 = (i7 * 31) + c3;
                    if (c3 == '\\') {
                        z = true;
                    }
                }
                if (z) {
                    str = readString(sub_chars, i2);
                } else if (i2 < 20) {
                    str = symbolTable.addSymbol(sub_chars, 0, i2, i7);
                } else {
                    str = new String(sub_chars, 0, i2);
                }
            } else {
                str = readString(sub_chars, i2);
            }
            this.bp = i3 + 1;
            int i9 = this.bp;
            if (i9 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i9);
            }
            this.ch = c2;
            return str;
        }
        throw new JSONException("unclosed str, " + info());
    }

    private static String readString(char[] cArr, int i) {
        int i2;
        char[] cArr2 = new char[i];
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            char c = cArr[i3];
            if (c != '\\') {
                cArr2[i4] = c;
                i4++;
            } else {
                i3++;
                char c2 = cArr[i3];
                if (c2 == '\"') {
                    i2 = i4 + 1;
                    cArr2[i4] = '\"';
                } else if (c2 != '\'') {
                    if (c2 != 'F') {
                        if (c2 == '\\') {
                            i2 = i4 + 1;
                            cArr2[i4] = PatternTokenizer.BACK_SLASH;
                        } else if (c2 == 'b') {
                            i2 = i4 + 1;
                            cArr2[i4] = '\b';
                        } else if (c2 != 'f') {
                            if (c2 == 'n') {
                                i2 = i4 + 1;
                                cArr2[i4] = '\n';
                            } else if (c2 == 'r') {
                                i2 = i4 + 1;
                                cArr2[i4] = CharInfo.S_CARRIAGERETURN;
                            } else if (c2 != 'x') {
                                switch (c2) {
                                    case '/':
                                        i2 = i4 + 1;
                                        cArr2[i4] = '/';
                                        break;
                                    case '0':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 0;
                                        break;
                                    case '1':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 1;
                                        break;
                                    case '2':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 2;
                                        break;
                                    case '3':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 3;
                                        break;
                                    case '4':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 4;
                                        break;
                                    case '5':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 5;
                                        break;
                                    case '6':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 6;
                                        break;
                                    case '7':
                                        i2 = i4 + 1;
                                        cArr2[i4] = 7;
                                        break;
                                    default:
                                        switch (c2) {
                                            case 't':
                                                i2 = i4 + 1;
                                                cArr2[i4] = '\t';
                                                break;
                                            case 'u':
                                                i2 = i4 + 1;
                                                int i5 = i3 + 1;
                                                int i6 = i5 + 1;
                                                int i7 = i6 + 1;
                                                i3 = i7 + 1;
                                                cArr2[i4] = (char) Integer.parseInt(new String(new char[]{cArr[i5], cArr[i6], cArr[i7], cArr[i3]}), 16);
                                                break;
                                            case 'v':
                                                i2 = i4 + 1;
                                                cArr2[i4] = 11;
                                                break;
                                            default:
                                                throw new JSONException("unclosed.str.lit");
                                        }
                                }
                            } else {
                                i2 = i4 + 1;
                                int[] iArr = digits;
                                int i8 = i3 + 1;
                                i3 = i8 + 1;
                                cArr2[i4] = (char) ((iArr[cArr[i8]] * 16) + iArr[cArr[i3]]);
                            }
                        }
                    }
                    i2 = i4 + 1;
                    cArr2[i4] = '\f';
                } else {
                    i2 = i4 + 1;
                    cArr2[i4] = PatternTokenizer.SINGLE_QUOTE;
                }
                i4 = i2;
            }
            i3++;
        }
        return new String(cArr2, 0, i4);
    }

    public String info() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("pos ");
        sb.append(this.bp);
        sb.append(", json : ");
        if (this.len < 65536) {
            str = this.text;
        } else {
            str = this.text.substring(0, 65536);
        }
        sb.append(str);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void skipComment() {
        next();
        char c = this.ch;
        if (c == '/') {
            do {
                next();
            } while (this.ch != '\n');
            next();
        } else if (c == '*') {
            next();
            while (true) {
                char c2 = this.ch;
                if (c2 == 26) {
                    return;
                }
                if (c2 == '*') {
                    next();
                    if (this.ch == '/') {
                        next();
                        return;
                    }
                } else {
                    next();
                }
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        int i = this.ch;
        boolean[] zArr = firstIdentifierFlags;
        if (i >= zArr.length || zArr[i]) {
            this.np = this.bp;
            this.sp = 1;
            while (true) {
                char next = next();
                boolean[] zArr2 = identifierFlags;
                if (next < zArr2.length && !zArr2[next]) {
                    break;
                }
                i = (i * 31) + next;
                this.sp++;
            }
            this.ch = charAt(this.bp);
            this.token = 18;
            if (this.sp != 4 || !this.text.startsWith("null", this.np)) {
                return symbolTable.addSymbol(this.text, this.np, this.sp, i);
            }
            return null;
        }
        throw new JSONException("illegal identifier : " + this.ch + ", " + info());
    }

    public final void scanString() {
        char c;
        char c2 = this.ch;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c2, i);
        if (indexOf != -1) {
            int i2 = indexOf - i;
            char[] sub_chars = sub_chars(this.bp + 1, i2);
            boolean z = false;
            while (i2 > 0 && sub_chars[i2 - 1] == '\\') {
                int i3 = i2 - 2;
                int i4 = 1;
                while (i3 >= 0 && sub_chars[i3] == '\\') {
                    i4++;
                    i3--;
                }
                if (i4 % 2 == 0) {
                    break;
                }
                int indexOf2 = this.text.indexOf(c2, indexOf + 1);
                int i5 = (indexOf2 - indexOf) + i2;
                if (i5 >= sub_chars.length) {
                    int length = (sub_chars.length * 3) / 2;
                    if (length < i5) {
                        length = i5;
                    }
                    char[] cArr = new char[length];
                    System.arraycopy(sub_chars, 0, cArr, 0, sub_chars.length);
                    sub_chars = cArr;
                }
                this.text.getChars(indexOf, indexOf2, sub_chars, i2);
                indexOf = indexOf2;
                i2 = i5;
                z = true;
            }
            if (!z) {
                for (int i6 = 0; i6 < i2; i6++) {
                    if (sub_chars[i6] == '\\') {
                        z = true;
                    }
                }
            }
            this.sbuf = sub_chars;
            this.sp = i2;
            this.np = this.bp;
            this.hasSpecial = z;
            this.bp = indexOf + 1;
            int i7 = this.bp;
            if (i7 >= this.len) {
                c = EOI;
            } else {
                c = this.text.charAt(i7);
            }
            this.ch = c;
            this.token = 4;
            return;
        }
        throw new JSONException("unclosed str, " + info());
    }

    public String scanStringValue(char c) {
        String str;
        char c2;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c, i);
        if (indexOf != -1) {
            if (V6) {
                str = this.text.substring(i, indexOf);
            } else {
                int i2 = indexOf - i;
                str = new String(sub_chars(this.bp + 1, i2), 0, i2);
            }
            if (str.indexOf(92) != -1) {
                while (true) {
                    int i3 = indexOf - 1;
                    int i4 = 0;
                    while (i3 >= 0 && this.text.charAt(i3) == '\\') {
                        i4++;
                        i3--;
                    }
                    if (i4 % 2 == 0) {
                        break;
                    }
                    indexOf = this.text.indexOf(c, indexOf + 1);
                }
                int i5 = indexOf - i;
                str = readString(sub_chars(this.bp + 1, i5), i5);
            }
            this.bp = indexOf + 1;
            int i6 = this.bp;
            if (i6 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i6);
            }
            this.ch = c2;
            return str;
        }
        throw new JSONException("unclosed str, " + info());
    }

    public final int intValue() {
        char c;
        boolean z;
        int i;
        char c2;
        char c3;
        int i2 = this.np;
        int i3 = this.sp + i2;
        if (i2 >= this.len) {
            c = 26;
        } else {
            c = this.text.charAt(i2);
        }
        int i4 = 0;
        if (c == '-') {
            i = Integer.MIN_VALUE;
            i2++;
            z = true;
        } else {
            i = WindowManager.LayoutConfig.INPUT_STATE_ALWAYS_HIDDEN;
            z = false;
        }
        if (i2 < i3) {
            int i5 = i2 + 1;
            if (i2 >= this.len) {
                c3 = 26;
            } else {
                c3 = this.text.charAt(i2);
            }
            i4 = -(c3 - '0');
            i2 = i5;
        }
        while (true) {
            if (i2 >= i3) {
                break;
            }
            int i6 = i2 + 1;
            if (i2 >= this.len) {
                c2 = 26;
            } else {
                c2 = this.text.charAt(i2);
            }
            if (c2 == 'L' || c2 == 'S' || c2 == 'B') {
                i2 = i6;
            } else {
                int i7 = c2 - '0';
                if (i4 >= -214748364) {
                    int i8 = i4 * 10;
                    if (i8 >= i + i7) {
                        i4 = i8 - i7;
                        i2 = i6;
                    } else {
                        throw new NumberFormatException(numberString());
                    }
                } else {
                    throw new NumberFormatException(numberString());
                }
            }
        }
        if (!z) {
            return -i4;
        }
        if (i2 > this.np + 1) {
            return i4;
        }
        throw new NumberFormatException(numberString());
    }

    public byte[] bytesValue() {
        return decodeFast(this.text, this.np + 1, this.sp);
    }

    private void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String stringVal = stringVal();
        if (stringVal.equals("null")) {
            this.token = 8;
        } else if (stringVal.equals("true")) {
            this.token = 6;
        } else if (stringVal.equals("false")) {
            this.token = 7;
        } else if (stringVal.equals("new")) {
            this.token = 9;
        } else if (stringVal.equals("undefined")) {
            this.token = 23;
        } else if (stringVal.equals("Set")) {
            this.token = 21;
        } else if (stringVal.equals("TreeSet")) {
            this.token = 22;
        } else {
            this.token = 18;
        }
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return readString(this.sbuf, this.sp);
        }
        return subString(this.np + 1, this.sp);
    }

    private final String subString(int i, int i2) {
        char[] cArr = this.sbuf;
        if (i2 < cArr.length) {
            this.text.getChars(i, i + i2, cArr, 0);
            return new String(this.sbuf, 0, i2);
        }
        char[] cArr2 = new char[i2];
        this.text.getChars(i, i2 + i, cArr2, 0);
        return new String(cArr2);
    }

    /* access modifiers changed from: package-private */
    public final char[] sub_chars(int i, int i2) {
        char[] cArr = this.sbuf;
        if (i2 < cArr.length) {
            this.text.getChars(i, i2 + i, cArr, 0);
            return this.sbuf;
        }
        char[] cArr2 = new char[i2];
        this.sbuf = cArr2;
        this.text.getChars(i, i2 + i, cArr2, 0);
        return cArr2;
    }

    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char charAt = charAt(i);
            boolean z = true;
            if (charAt == 26) {
                return true;
            }
            if (charAt > ' ' || !(charAt == ' ' || charAt == '\n' || charAt == '\r' || charAt == '\t' || charAt == '\f' || charAt == '\b')) {
                z = false;
            }
            if (!z) {
                return false;
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    public final void skipWhitespace() {
        while (true) {
            char c = this.ch;
            if (c > '/') {
                return;
            }
            if (c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '\f' || c == '\b') {
                next();
            } else if (c == '/') {
                skipComment();
            } else {
                return;
            }
        }
    }

    public final void scanNumber() {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        int i = this.bp;
        this.np = i;
        this.exp = false;
        if (this.ch == '-') {
            this.sp++;
            int i2 = i + 1;
            this.bp = i2;
            if (i2 >= this.len) {
                c7 = 26;
            } else {
                c7 = this.text.charAt(i2);
            }
            this.ch = c7;
        }
        while (true) {
            char c8 = this.ch;
            if (c8 < '0' || c8 > '9') {
                this.isDouble = false;
            } else {
                this.sp++;
                int i3 = this.bp + 1;
                this.bp = i3;
                if (i3 >= this.len) {
                    c6 = 26;
                } else {
                    c6 = this.text.charAt(i3);
                }
                this.ch = c6;
            }
        }
        this.isDouble = false;
        if (this.ch == '.') {
            this.sp++;
            int i4 = this.bp + 1;
            this.bp = i4;
            if (i4 >= this.len) {
                c4 = 26;
            } else {
                c4 = this.text.charAt(i4);
            }
            this.ch = c4;
            this.isDouble = true;
            while (true) {
                char c9 = this.ch;
                if (c9 < '0' || c9 > '9') {
                    break;
                }
                this.sp++;
                int i5 = this.bp + 1;
                this.bp = i5;
                if (i5 >= this.len) {
                    c5 = 26;
                } else {
                    c5 = this.text.charAt(i5);
                }
                this.ch = c5;
            }
        }
        char c10 = this.ch;
        if (c10 == 'L') {
            this.sp++;
            next();
        } else if (c10 == 'S') {
            this.sp++;
            next();
        } else if (c10 == 'B') {
            this.sp++;
            next();
        } else if (c10 == 'F') {
            this.sp++;
            next();
            this.isDouble = true;
        } else if (c10 == 'D') {
            this.sp++;
            next();
            this.isDouble = true;
        } else if (c10 == 'e' || c10 == 'E') {
            this.sp++;
            int i6 = this.bp + 1;
            this.bp = i6;
            if (i6 >= this.len) {
                c = 26;
            } else {
                c = this.text.charAt(i6);
            }
            this.ch = c;
            char c11 = this.ch;
            if (c11 == '+' || c11 == '-') {
                this.sp++;
                int i7 = this.bp + 1;
                this.bp = i7;
                if (i7 >= this.len) {
                    c3 = 26;
                } else {
                    c3 = this.text.charAt(i7);
                }
                this.ch = c3;
            }
            while (true) {
                char c12 = this.ch;
                if (c12 < '0' || c12 > '9') {
                    char c13 = this.ch;
                } else {
                    this.sp++;
                    int i8 = this.bp + 1;
                    this.bp = i8;
                    if (i8 >= this.len) {
                        c2 = 26;
                    } else {
                        c2 = this.text.charAt(i8);
                    }
                    this.ch = c2;
                }
            }
            char c132 = this.ch;
            if (c132 == 'D' || c132 == 'F') {
                this.sp++;
                next();
            }
            this.exp = true;
            this.isDouble = true;
        }
        if (this.isDouble) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    public boolean scanBoolean() {
        boolean z = false;
        int i = 1;
        if (this.text.startsWith("false", this.bp)) {
            i = 5;
        } else if (this.text.startsWith("true", this.bp)) {
            z = true;
            i = 4;
        } else {
            char c = this.ch;
            if (c == '1') {
                z = true;
            } else if (c != '0') {
                this.matchStat = -1;
                return false;
            }
        }
        this.bp += i;
        this.ch = charAt(this.bp);
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:145:0x0288 A[Catch:{ NumberFormatException -> 0x02ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x029e A[Catch:{ NumberFormatException -> 0x02ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x02a9 A[Catch:{ NumberFormatException -> 0x02ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00ee  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Number scanNumberValue() {
        /*
        // Method dump skipped, instructions count: 749
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanNumberValue():java.lang.Number");
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final long scanLongValue() {
        /*
        // Method dump skipped, instructions count: 201
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanLongValue():long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final long longValue() throws java.lang.NumberFormatException {
        /*
        // Method dump skipped, instructions count: 136
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.longValue():long");
    }

    public final Number decimalValue(boolean z) {
        char c;
        char[] cArr;
        boolean z2;
        int i = (this.np + this.sp) - 1;
        if (i >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i);
        }
        if (c == 'F') {
            try {
                return Float.valueOf(Float.parseFloat(numberString()));
            } catch (NumberFormatException e) {
                throw new JSONException(e.getMessage() + ", " + info());
            }
        } else if (c == 'D') {
            return Double.valueOf(Double.parseDouble(numberString()));
        } else {
            if (z) {
                return decimalValue();
            }
            char charAt = this.text.charAt((this.np + this.sp) - 1);
            int i2 = this.sp;
            if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
                i2--;
            }
            int i3 = this.np;
            int i4 = 0;
            if (i2 < this.sbuf.length) {
                this.text.getChars(i3, i3 + i2, this.sbuf, 0);
                cArr = this.sbuf;
            } else {
                char[] cArr2 = new char[i2];
                this.text.getChars(i3, i3 + i2, cArr2, 0);
                cArr = cArr2;
            }
            if (i2 > 9 || this.exp) {
                return Double.valueOf(Double.parseDouble(new String(cArr, 0, i2)));
            }
            char c2 = cArr[0];
            int i5 = 2;
            if (c2 == '-') {
                c2 = cArr[1];
                z2 = true;
            } else {
                if (c2 == '+') {
                    c2 = cArr[1];
                } else {
                    i5 = 1;
                }
                z2 = false;
            }
            int i6 = c2 - '0';
            while (i5 < i2) {
                char c3 = cArr[i5];
                if (c3 == '.') {
                    i4 = 1;
                } else {
                    i6 = (i6 * 10) + (c3 - '0');
                    if (i4 != 0) {
                        i4 *= 10;
                    }
                }
                i5++;
            }
            double d = ((double) i6) / ((double) i4);
            if (z2) {
                d = -d;
            }
            return Double.valueOf(d);
        }
    }

    public final BigDecimal decimalValue() {
        char charAt = this.text.charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        int i2 = this.np;
        char[] cArr = this.sbuf;
        if (i < cArr.length) {
            this.text.getChars(i2, i2 + i, cArr, 0);
            return new BigDecimal(this.sbuf, 0, i);
        }
        char[] cArr2 = new char[i];
        this.text.getChars(i2, i + i2, cArr2, 0);
        return new BigDecimal(cArr2);
    }

    public boolean matchField(long j) {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6 = this.ch;
        int i = this.bp + 1;
        int i2 = 1;
        while (c6 != '\"' && c6 != '\'') {
            if (c6 > ' ' || !(c6 == ' ' || c6 == '\n' || c6 == '\r' || c6 == '\t' || c6 == '\f' || c6 == '\b')) {
                this.fieldHash = 0;
                this.matchStat = -2;
                return false;
            }
            int i3 = i2 + 1;
            int i4 = this.bp + i2;
            if (i4 >= this.len) {
                c6 = EOI;
            } else {
                c6 = this.text.charAt(i4);
            }
            i2 = i3;
        }
        int i5 = i;
        long j2 = -3750763034362895579L;
        while (true) {
            if (i5 >= this.len) {
                break;
            }
            char charAt = this.text.charAt(i5);
            if (charAt == c6) {
                i2 += (i5 - i) + 1;
                break;
            }
            j2 = 1099511628211L * (j2 ^ ((long) charAt));
            i5++;
        }
        if (j2 != j) {
            this.matchStat = -2;
            this.fieldHash = j2;
            return false;
        }
        int i6 = i2 + 1;
        int i7 = this.bp + i2;
        if (i7 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i7);
        }
        while (c != ':') {
            if (c > ' ' || !(c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b')) {
                throw new JSONException("match feild error expect ':'");
            }
            int i8 = i6 + 1;
            int i9 = this.bp + i6;
            if (i9 >= this.len) {
                c = EOI;
            } else {
                c = this.text.charAt(i9);
            }
            i6 = i8;
        }
        int i10 = this.bp + i6;
        if (i10 >= this.len) {
            c2 = EOI;
        } else {
            c2 = this.text.charAt(i10);
        }
        if (c2 == '{') {
            this.bp = i10 + 1;
            int i11 = this.bp;
            if (i11 >= this.len) {
                c5 = EOI;
            } else {
                c5 = this.text.charAt(i11);
            }
            this.ch = c5;
            this.token = 12;
        } else if (c2 == '[') {
            this.bp = i10 + 1;
            int i12 = this.bp;
            if (i12 >= this.len) {
                c4 = EOI;
            } else {
                c4 = this.text.charAt(i12);
            }
            this.ch = c4;
            this.token = 14;
        } else {
            this.bp = i10;
            int i13 = this.bp;
            if (i13 >= this.len) {
                c3 = EOI;
            } else {
                c3 = this.text.charAt(i13);
            }
            this.ch = c3;
            nextToken();
        }
        return true;
    }

    private int matchFieldHash(long j) {
        char c;
        char c2 = this.ch;
        int i = this.bp;
        int i2 = 1;
        while (c2 != '\"' && c2 != '\'') {
            if (c2 == ' ' || c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == '\f' || c2 == '\b') {
                int i3 = i2 + 1;
                int i4 = this.bp + i2;
                if (i4 >= this.len) {
                    c2 = EOI;
                } else {
                    c2 = this.text.charAt(i4);
                }
                i2 = i3;
            } else {
                this.fieldHash = 0;
                this.matchStat = -2;
                return 0;
            }
        }
        long j2 = -3750763034362895579L;
        int i5 = this.bp + i2;
        while (true) {
            if (i5 >= this.len) {
                break;
            }
            char charAt = this.text.charAt(i5);
            if (charAt == c2) {
                i2 += (i5 - this.bp) - i2;
                break;
            }
            j2 = 1099511628211L * (((long) charAt) ^ j2);
            i5++;
        }
        if (j2 != j) {
            this.fieldHash = j2;
            this.matchStat = -2;
            return 0;
        }
        int i6 = i2 + 1;
        int i7 = this.bp + i6;
        if (i7 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i7);
        }
        while (c != ':') {
            if (c > ' ' || !(c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b')) {
                throw new JSONException("match feild error expect ':'");
            }
            int i8 = i6 + 1;
            int i9 = this.bp + i6;
            if (i9 >= this.len) {
                c = EOI;
            } else {
                c = this.text.charAt(i9);
            }
            i6 = i8;
        }
        return i6 + 1;
    }

    public int scanFieldInt(long j) {
        char c;
        int i;
        char c2;
        char c3;
        int i2;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0;
        }
        int i3 = matchFieldHash + 1;
        int i4 = this.bp + matchFieldHash;
        int i5 = this.len;
        char c4 = EOI;
        if (i4 >= i5) {
            c = 26;
        } else {
            c = this.text.charAt(i4);
        }
        boolean z = c == '\"';
        if (z) {
            int i6 = i3 + 1;
            int i7 = this.bp + i3;
            if (i7 >= this.len) {
                c = 26;
            } else {
                c = this.text.charAt(i7);
            }
            i3 = i6;
            z = true;
        }
        boolean z2 = c == '-';
        if (z2) {
            int i8 = i3 + 1;
            int i9 = this.bp + i3;
            if (i9 >= this.len) {
                c = 26;
            } else {
                c = this.text.charAt(i9);
            }
            i3 = i8;
        }
        if (c < '0' || c > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i10 = c - '0';
        while (true) {
            i = i3 + 1;
            int i11 = this.bp + i3;
            if (i11 >= this.len) {
                c2 = 26;
            } else {
                c2 = this.text.charAt(i11);
            }
            if (c2 >= '0' && c2 <= '9') {
                i10 = (i10 * 10) + (c2 - '0');
                i3 = i;
            }
        }
        if (c2 == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (c2 != '\"') {
            c3 = c2;
            i2 = i;
        } else if (!z) {
            this.matchStat = -1;
            return 0;
        } else {
            i2 = i + 1;
            int i12 = this.bp + i;
            c3 = i12 >= this.len ? 26 : this.text.charAt(i12);
        }
        if (i10 < 0) {
            this.matchStat = -1;
            return 0;
        }
        while (c3 != ',') {
            if (c3 <= ' ' && (c3 == ' ' || c3 == '\n' || c3 == '\r' || c3 == '\t' || c3 == '\f' || c3 == '\b')) {
                int i13 = i2 + 1;
                int i14 = this.bp + i2;
                if (i14 >= this.len) {
                    c3 = 26;
                } else {
                    c3 = this.text.charAt(i14);
                }
                i2 = i13;
            } else if (c3 == '}') {
                int i15 = i2 + 1;
                char charAt = charAt(this.bp + i2);
                if (charAt == ',') {
                    this.token = 16;
                    this.bp += i15 - 1;
                    int i16 = this.bp + 1;
                    this.bp = i16;
                    if (i16 < this.len) {
                        c4 = this.text.charAt(i16);
                    }
                    this.ch = c4;
                } else if (charAt == ']') {
                    this.token = 15;
                    this.bp += i15 - 1;
                    int i17 = this.bp + 1;
                    this.bp = i17;
                    if (i17 < this.len) {
                        c4 = this.text.charAt(i17);
                    }
                    this.ch = c4;
                } else if (charAt == '}') {
                    this.token = 13;
                    this.bp += i15 - 1;
                    int i18 = this.bp + 1;
                    this.bp = i18;
                    if (i18 < this.len) {
                        c4 = this.text.charAt(i18);
                    }
                    this.ch = c4;
                } else if (charAt == 26) {
                    this.token = 20;
                    this.bp += i15 - 1;
                    this.ch = EOI;
                } else {
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
                return z2 ? -i10 : i10;
            } else {
                this.matchStat = -1;
                return 0;
            }
        }
        this.bp += i2 - 1;
        int i19 = this.bp + 1;
        this.bp = i19;
        if (i19 < this.len) {
            c4 = this.text.charAt(i19);
        }
        this.ch = c4;
        this.matchStat = 3;
        this.token = 16;
        return z2 ? -i10 : i10;
    }

    public final int[] scanFieldIntArray(long j) {
        char c;
        char c2;
        int[] iArr;
        int i;
        int i2;
        char c3;
        int i3;
        boolean z;
        int[] iArr2;
        int i4;
        int i5;
        char c4;
        char c5;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        int[] iArr3 = null;
        if (matchFieldHash == 0) {
            return null;
        }
        int i6 = matchFieldHash + 1;
        int i7 = this.bp + matchFieldHash;
        if (i7 >= this.len) {
            c = 26;
        } else {
            c = this.text.charAt(i7);
        }
        int i8 = -1;
        if (c != '[') {
            this.matchStat = -1;
            return null;
        }
        int i9 = i6 + 1;
        int i10 = this.bp + i6;
        if (i10 >= this.len) {
            c2 = 26;
        } else {
            c2 = this.text.charAt(i10);
        }
        int[] iArr4 = new int[16];
        if (c2 == ']') {
            int i11 = i9 + 1;
            int i12 = this.bp + i9;
            if (i12 >= this.len) {
                c3 = 26;
            } else {
                c3 = this.text.charAt(i12);
            }
            i2 = 0;
            i = i11;
            iArr = iArr4;
        } else {
            iArr = iArr4;
            int i13 = 0;
            while (true) {
                if (c2 == '-') {
                    i3 = i9 + 1;
                    int i14 = this.bp + i9;
                    if (i14 >= this.len) {
                        c2 = 26;
                    } else {
                        c2 = this.text.charAt(i14);
                    }
                    z = true;
                } else {
                    i3 = i9;
                    z = false;
                }
                if (c2 >= '0') {
                    if (c2 > '9') {
                        i4 = i8;
                        iArr2 = null;
                        break;
                    }
                    int i15 = c2 - '0';
                    while (true) {
                        i5 = i3 + 1;
                        int i16 = this.bp + i3;
                        if (i16 >= this.len) {
                            c4 = 26;
                        } else {
                            c4 = this.text.charAt(i16);
                        }
                        if (c4 >= '0' && c4 <= '9') {
                            i15 = (i15 * 10) + (c4 - '0');
                            i3 = i5;
                        }
                    }
                    if (i13 >= iArr.length) {
                        int[] iArr5 = new int[((iArr.length * 3) / 2)];
                        System.arraycopy(iArr, 0, iArr5, 0, i13);
                        iArr = iArr5;
                    }
                    i2 = i13 + 1;
                    if (z) {
                        i15 = -i15;
                    }
                    iArr[i13] = i15;
                    if (c4 == ',') {
                        int i17 = i5 + 1;
                        int i18 = this.bp + i5;
                        if (i18 >= this.len) {
                            c5 = 26;
                        } else {
                            c5 = this.text.charAt(i18);
                        }
                        c4 = c5;
                        i5 = i17;
                    } else if (c4 == ']') {
                        i = i5 + 1;
                        int i19 = this.bp + i5;
                        if (i19 >= this.len) {
                            c3 = 26;
                        } else {
                            c3 = this.text.charAt(i19);
                        }
                    }
                    i13 = i2;
                    iArr3 = null;
                    i9 = i5;
                    i8 = -1;
                    c2 = c4;
                } else {
                    iArr2 = iArr3;
                    i4 = i8;
                    break;
                }
            }
            this.matchStat = i4;
            return iArr2;
        }
        if (i2 != iArr.length) {
            int[] iArr6 = new int[i2];
            System.arraycopy(iArr, 0, iArr6, 0, i2);
            iArr = iArr6;
        }
        if (c3 == ',') {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return iArr;
        } else if (c3 == '}') {
            int i20 = i + 1;
            char charAt = charAt(this.bp + i);
            if (charAt == ',') {
                this.token = 16;
                this.bp += i20 - 1;
                next();
            } else if (charAt == ']') {
                this.token = 15;
                this.bp += i20 - 1;
                next();
            } else if (charAt == '}') {
                this.token = 13;
                this.bp += i20 - 1;
                next();
            } else if (charAt == 26) {
                this.bp += i20 - 1;
                this.token = 20;
                this.ch = EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return iArr;
        } else {
            this.matchStat = -1;
            return null;
        }
    }

    public long scanFieldLong(long j) {
        char c;
        int i;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        boolean z = false;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0;
        }
        int i2 = matchFieldHash + 1;
        int i3 = this.bp + matchFieldHash;
        if (i3 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i3);
        }
        boolean z2 = c == '\"';
        if (z2) {
            int i4 = i2 + 1;
            int i5 = this.bp + i2;
            if (i5 >= this.len) {
                c = EOI;
            } else {
                c = this.text.charAt(i5);
            }
            i2 = i4;
        }
        if (c == '-') {
            z = true;
        }
        if (z) {
            int i6 = i2 + 1;
            int i7 = this.bp + i2;
            if (i7 >= this.len) {
                c = EOI;
            } else {
                c = this.text.charAt(i7);
            }
            i2 = i6;
        }
        if (c < '0' || c > '9') {
            this.matchStat = -1;
            return 0;
        }
        long j2 = (long) (c - '0');
        while (true) {
            i = i2 + 1;
            int i8 = this.bp + i2;
            if (i8 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i8);
            }
            if (c2 >= '0' && c2 <= '9') {
                j2 = (j2 * 10) + ((long) (c2 - '0'));
                i2 = i;
            }
        }
        if (c2 == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (c2 == '\"') {
            if (!z2) {
                this.matchStat = -1;
                return 0;
            }
            int i9 = i + 1;
            int i10 = this.bp + i;
            if (i10 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i10);
            }
            i = i9;
        }
        if (j2 < 0) {
            this.matchStat = -1;
            return 0;
        } else if (c2 == ',') {
            this.bp += i - 1;
            int i11 = this.bp + 1;
            this.bp = i11;
            if (i11 >= this.len) {
                c6 = EOI;
            } else {
                c6 = this.text.charAt(i11);
            }
            this.ch = c6;
            this.matchStat = 3;
            this.token = 16;
            return z ? -j2 : j2;
        } else if (c2 == '}') {
            int i12 = i + 1;
            char charAt = charAt(this.bp + i);
            if (charAt == ',') {
                this.token = 16;
                this.bp += i12 - 1;
                int i13 = this.bp + 1;
                this.bp = i13;
                if (i13 >= this.len) {
                    c5 = EOI;
                } else {
                    c5 = this.text.charAt(i13);
                }
                this.ch = c5;
            } else if (charAt == ']') {
                this.token = 15;
                this.bp += i12 - 1;
                int i14 = this.bp + 1;
                this.bp = i14;
                if (i14 >= this.len) {
                    c4 = EOI;
                } else {
                    c4 = this.text.charAt(i14);
                }
                this.ch = c4;
            } else if (charAt == '}') {
                this.token = 13;
                this.bp += i12 - 1;
                int i15 = this.bp + 1;
                this.bp = i15;
                if (i15 >= this.len) {
                    c3 = EOI;
                } else {
                    c3 = this.text.charAt(i15);
                }
                this.ch = c3;
            } else if (charAt == 26) {
                this.token = 20;
                this.bp += i12 - 1;
                this.ch = EOI;
            } else {
                this.matchStat = -1;
                return 0;
            }
            this.matchStat = 4;
            return z ? -j2 : j2;
        } else {
            this.matchStat = -1;
            return 0;
        }
    }

    public String scanFieldString(long j) {
        String str;
        char c;
        char c2;
        boolean z;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return null;
        }
        int i = matchFieldHash + 1;
        int i2 = this.bp + matchFieldHash;
        if (i2 >= this.len) {
            throw new JSONException("unclosed str, " + info());
        } else if (this.text.charAt(i2) != '\"') {
            this.matchStat = -1;
            return this.stringDefaultValue;
        } else {
            int i3 = this.bp + i;
            int indexOf = this.text.indexOf(34, i3);
            if (indexOf != -1) {
                if (V6) {
                    str = this.text.substring(i3, indexOf);
                } else {
                    int i4 = indexOf - i3;
                    str = new String(sub_chars(this.bp + i, i4), 0, i4);
                }
                if (str.indexOf(92) != -1) {
                    boolean z2 = false;
                    while (true) {
                        int i5 = indexOf - 1;
                        z = z2;
                        int i6 = 0;
                        while (i5 >= 0 && this.text.charAt(i5) == '\\') {
                            i6++;
                            i5--;
                            z = true;
                        }
                        if (i6 % 2 == 0) {
                            break;
                        }
                        indexOf = this.text.indexOf(34, indexOf + 1);
                        z2 = z;
                    }
                    int i7 = indexOf - i3;
                    char[] sub_chars = sub_chars(this.bp + i, i7);
                    if (z) {
                        str = readString(sub_chars, i7);
                    } else {
                        str = new String(sub_chars, 0, i7);
                        if (str.indexOf(92) != -1) {
                            str = readString(sub_chars, i7);
                        }
                    }
                }
                int i8 = indexOf + 1;
                int i9 = this.len;
                char c3 = EOI;
                if (i8 >= i9) {
                    c = 26;
                } else {
                    c = this.text.charAt(i8);
                }
                if (c == ',') {
                    this.bp = i8;
                    int i10 = this.bp + 1;
                    this.bp = i10;
                    if (i10 < this.len) {
                        c3 = this.text.charAt(i10);
                    }
                    this.ch = c3;
                    this.matchStat = 3;
                    this.token = 16;
                    return str;
                } else if (c == '}') {
                    int i11 = i8 + 1;
                    if (i11 >= this.len) {
                        c2 = 26;
                    } else {
                        c2 = this.text.charAt(i11);
                    }
                    if (c2 == ',') {
                        this.token = 16;
                        this.bp = i11;
                        next();
                    } else if (c2 == ']') {
                        this.token = 15;
                        this.bp = i11;
                        next();
                    } else if (c2 == '}') {
                        this.token = 13;
                        this.bp = i11;
                        next();
                    } else if (c2 == 26) {
                        this.token = 20;
                        this.bp = i11;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return this.stringDefaultValue;
                    }
                    this.matchStat = 4;
                    return str;
                } else {
                    this.matchStat = -1;
                    return this.stringDefaultValue;
                }
            } else {
                throw new JSONException("unclosed str, " + info());
            }
        }
    }

    public Date scanFieldDate(long j) {
        char c;
        int i;
        char c2;
        Date date;
        int i2;
        char c3;
        char c4;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return null;
        }
        int i3 = this.bp;
        char c5 = this.ch;
        int i4 = matchFieldHash + 1;
        int i5 = matchFieldHash + i3;
        int i6 = this.len;
        char c6 = EOI;
        if (i5 >= i6) {
            c = 26;
        } else {
            c = this.text.charAt(i5);
        }
        if (c == '\"') {
            int i7 = this.bp;
            int i8 = i7 + i4;
            int i9 = i4 + 1;
            int i10 = i7 + i4;
            if (i10 < this.len) {
                this.text.charAt(i10);
            }
            int indexOf = this.text.indexOf(34, this.bp + i9);
            if (indexOf != -1) {
                int i11 = indexOf - i8;
                this.bp = i8;
                if (scanISO8601DateIfMatch(false, i11)) {
                    date = this.calendar.getTime();
                    int i12 = i9 + i11;
                    i = i12 + 1;
                    c2 = charAt(i12 + i3);
                    this.bp = i3;
                } else {
                    this.bp = i3;
                    this.matchStat = -1;
                    return null;
                }
            } else {
                throw new JSONException("unclosed str");
            }
        } else if (c < '0' || c > '9') {
            this.matchStat = -1;
            return null;
        } else {
            long j2 = (long) (c - '0');
            while (true) {
                i2 = i4 + 1;
                int i13 = this.bp + i4;
                if (i13 >= this.len) {
                    c3 = 26;
                } else {
                    c3 = this.text.charAt(i13);
                }
                if (c3 >= '0' && c3 <= '9') {
                    j2 = (j2 * 10) + ((long) (c3 - '0'));
                    i4 = i2;
                }
            }
            if (c3 == '.') {
                this.matchStat = -1;
                return null;
            }
            if (c3 == '\"') {
                int i14 = i2 + 1;
                int i15 = this.bp + i2;
                if (i15 >= this.len) {
                    c4 = 26;
                } else {
                    c4 = this.text.charAt(i15);
                }
                c2 = c4;
                i = i14;
            } else {
                c2 = c3;
                i = i2;
            }
            if (j2 < 0) {
                this.matchStat = -1;
                return null;
            }
            date = new Date(j2);
        }
        if (c2 == ',') {
            this.bp += i - 1;
            int i16 = this.bp + 1;
            this.bp = i16;
            if (i16 < this.len) {
                c6 = this.text.charAt(i16);
            }
            this.ch = c6;
            this.matchStat = 3;
            this.token = 16;
            return date;
        } else if (c2 == '}') {
            int i17 = i + 1;
            char charAt = charAt(this.bp + i);
            if (charAt == ',') {
                this.token = 16;
                this.bp += i17 - 1;
                int i18 = this.bp + 1;
                this.bp = i18;
                if (i18 < this.len) {
                    c6 = this.text.charAt(i18);
                }
                this.ch = c6;
            } else if (charAt == ']') {
                this.token = 15;
                this.bp += i17 - 1;
                int i19 = this.bp + 1;
                this.bp = i19;
                if (i19 < this.len) {
                    c6 = this.text.charAt(i19);
                }
                this.ch = c6;
            } else if (charAt == '}') {
                this.token = 13;
                this.bp += i17 - 1;
                int i20 = this.bp + 1;
                this.bp = i20;
                if (i20 < this.len) {
                    c6 = this.text.charAt(i20);
                }
                this.ch = c6;
            } else if (charAt == 26) {
                this.token = 20;
                this.bp += i17 - 1;
                this.ch = EOI;
            } else {
                this.bp = i3;
                this.ch = c5;
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return date;
        } else {
            this.bp = i3;
            this.ch = c5;
            this.matchStat = -1;
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanFieldBoolean(long r13) {
        /*
        // Method dump skipped, instructions count: 369
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanFieldBoolean(long):boolean");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    public final float scanFieldFloat(long r18) {
        /*
        // Method dump skipped, instructions count: 326
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanFieldFloat(long):float");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0124  */
    public final float[] scanFieldFloatArray(long r19) {
        /*
        // Method dump skipped, instructions count: 538
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanFieldFloatArray(long):float[]");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x013b  */
    public final float[][] scanFieldFloatArray2(long r21) {
        /*
        // Method dump skipped, instructions count: 663
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanFieldFloatArray2(long):float[][]");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    public final double scanFieldDouble(long r18) {
        /*
        // Method dump skipped, instructions count: 332
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanFieldDouble(long):double");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0124  */
    public final double[] scanFieldDoubleArray(long r19) {
        /*
        // Method dump skipped, instructions count: 538
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanFieldDoubleArray(long):double[]");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x013b  */
    public final double[][] scanFieldDoubleArray2(long r21) {
        /*
        // Method dump skipped, instructions count: 662
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanFieldDoubleArray2(long):double[][]");
    }

    public long scanFieldSymbol(long j) {
        char c;
        char c2;
        char c3;
        char c4;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0;
        }
        int i = matchFieldHash + 1;
        int i2 = this.bp + matchFieldHash;
        int i3 = this.len;
        char c5 = EOI;
        if (i2 >= i3) {
            c = 26;
        } else {
            c = this.text.charAt(i2);
        }
        if (c != '\"') {
            this.matchStat = -1;
            return 0;
        }
        long j2 = -3750763034362895579L;
        int i4 = this.bp;
        while (true) {
            int i5 = i + 1;
            int i6 = this.bp + i;
            if (i6 >= this.len) {
                c2 = 26;
            } else {
                c2 = this.text.charAt(i6);
            }
            if (c2 == '\"') {
                int i7 = i5 + 1;
                int i8 = this.bp + i5;
                if (i8 >= this.len) {
                    c3 = 26;
                } else {
                    c3 = this.text.charAt(i8);
                }
                if (c3 == ',') {
                    this.bp += i7 - 1;
                    int i9 = this.bp + 1;
                    this.bp = i9;
                    if (i9 < this.len) {
                        c5 = this.text.charAt(i9);
                    }
                    this.ch = c5;
                    this.matchStat = 3;
                    return j2;
                } else if (c3 == '}') {
                    int i10 = i7 + 1;
                    int i11 = this.bp + i7;
                    if (i11 >= this.len) {
                        c4 = 26;
                    } else {
                        c4 = this.text.charAt(i11);
                    }
                    if (c4 == ',') {
                        this.token = 16;
                        this.bp += i10 - 1;
                        next();
                    } else if (c4 == ']') {
                        this.token = 15;
                        this.bp += i10 - 1;
                        next();
                    } else if (c4 == '}') {
                        this.token = 13;
                        this.bp += i10 - 1;
                        next();
                    } else if (c4 == 26) {
                        this.token = 20;
                        this.bp += i10 - 1;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return 0;
                    }
                    this.matchStat = 4;
                    return j2;
                } else {
                    this.matchStat = -1;
                    return 0;
                }
            } else {
                j2 = (j2 ^ ((long) c2)) * 1099511628211L;
                if (c2 == '\\') {
                    this.matchStat = -1;
                    return 0;
                }
                i = i5;
            }
        }
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        return scanISO8601DateIfMatch(z, this.len - this.bp);
    }

    /* JADX WARNING: Removed duplicated region for block: B:111:0x0225 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0226  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanISO8601DateIfMatch(boolean r36, int r37) {
        /*
        // Method dump skipped, instructions count: 1652
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JSONLexer.scanISO8601DateIfMatch(boolean, int):boolean");
    }

    /* access modifiers changed from: protected */
    public void setTime(char c, char c2, char c3, char c4, char c5, char c6) {
        this.calendar.set(11, ((c - '0') * 10) + (c2 - '0'));
        this.calendar.set(12, ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(13, ((c5 - '0') * 10) + (c6 - '0'));
    }

    /* access modifiers changed from: protected */
    public void setTimeZone(char c, char c2, char c3) {
        int i = (((c2 - '0') * 10) + (c3 - '0')) * SystemAbilityDefinition.SUBSYS_SENSORS_SYS_ABILITY_ID_BEGIN * 1000;
        if (c == '-') {
            i = -i;
        }
        if (this.calendar.getTimeZone().getRawOffset() != i) {
            String[] availableIDs = TimeZone.getAvailableIDs(i);
            if (availableIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
            }
        }
    }

    private void setCalendar(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        this.calendar.set(1, ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(2, (((c5 - '0') * 10) + (c6 - '0')) - 1);
        this.calendar.set(5, ((c7 - '0') * 10) + (c8 - '0'));
    }

    public static final byte[] decodeFast(String str, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[str.charAt(i)] < 0) {
            i++;
        }
        while (i5 > 0 && IA[str.charAt(i5)] < 0) {
            i5--;
        }
        int i6 = str.charAt(i5) == '=' ? str.charAt(i5 + -1) == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (str.charAt(76) == '\r' ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = i;
        int i11 = 0;
        int i12 = 0;
        while (i11 < i9) {
            int i13 = i10 + 1;
            int i14 = i13 + 1;
            int i15 = i14 + 1;
            int i16 = i15 + 1;
            int i17 = (IA[str.charAt(i10)] << 18) | (IA[str.charAt(i13)] << 12) | (IA[str.charAt(i14)] << 6) | IA[str.charAt(i15)];
            int i18 = i11 + 1;
            bArr[i11] = (byte) (i17 >> 16);
            int i19 = i18 + 1;
            bArr[i18] = (byte) (i17 >> 8);
            int i20 = i19 + 1;
            bArr[i19] = (byte) i17;
            if (i3 > 0 && (i12 = i12 + 1) == 19) {
                i16 += 2;
                i12 = 0;
            }
            i10 = i16;
            i11 = i20;
        }
        if (i11 < i8) {
            int i21 = 0;
            while (i10 <= i5 - i6) {
                i4 |= IA[str.charAt(i10)] << (18 - (i21 * 6));
                i21++;
                i10++;
            }
            int i22 = 16;
            while (i11 < i8) {
                bArr[i11] = (byte) (i4 >> i22);
                i22 -= 8;
                i11++;
            }
        }
        return bArr;
    }
}
