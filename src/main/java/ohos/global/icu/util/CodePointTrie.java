package ohos.global.icu.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import ohos.global.icu.impl.Normalizer2Impl;
import ohos.global.icu.util.CodePointMap;

public abstract class CodePointTrie extends CodePointMap {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ASCII_LIMIT = 128;
    private static final int BMP_INDEX_LENGTH = 1024;
    static final int CP_PER_INDEX_2_ENTRY = 512;
    private static final int ERROR_VALUE_NEG_DATA_OFFSET = 1;
    static final int FAST_DATA_BLOCK_LENGTH = 64;
    private static final int FAST_DATA_MASK = 63;
    static final int FAST_SHIFT = 6;
    private static final int HIGH_VALUE_NEG_DATA_OFFSET = 2;
    static final int INDEX_2_BLOCK_LENGTH = 32;
    static final int INDEX_2_MASK = 31;
    static final int INDEX_3_BLOCK_LENGTH = 32;
    private static final int INDEX_3_MASK = 31;
    private static final int MAX_UNICODE = 1114111;
    static final int NO_DATA_NULL_OFFSET = 1048575;
    static final int NO_INDEX3_NULL_OFFSET = 32767;
    private static final int OMITTED_BMP_INDEX_1_LENGTH = 4;
    private static final int OPTIONS_DATA_LENGTH_MASK = 61440;
    private static final int OPTIONS_DATA_NULL_OFFSET_MASK = 3840;
    private static final int OPTIONS_RESERVED_MASK = 56;
    private static final int OPTIONS_VALUE_BITS_MASK = 7;
    private static final int SHIFT_1 = 14;
    static final int SHIFT_1_2 = 5;
    private static final int SHIFT_2 = 9;
    static final int SHIFT_2_3 = 5;
    static final int SHIFT_3 = 4;
    static final int SMALL_DATA_BLOCK_LENGTH = 16;
    static final int SMALL_DATA_MASK = 15;
    private static final int SMALL_INDEX_LENGTH = 64;
    static final int SMALL_LIMIT = 4096;
    private static final int SMALL_MAX = 4095;
    private final int[] ascii;
    @Deprecated
    protected final Data data;
    @Deprecated
    protected final int dataLength;
    private final int dataNullOffset;
    @Deprecated
    protected final int highStart;
    private final char[] index;
    private final int index3NullOffset;
    private final int nullValue;

    public enum Type {
        FAST,
        SMALL
    }

    public enum ValueWidth {
        BITS_16,
        BITS_32,
        BITS_8
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public abstract int cpIndex(int i);

    public abstract Type getType();

    /* synthetic */ CodePointTrie(char[] cArr, Data data2, int i, int i2, int i3, AnonymousClass1 r6) {
        this(cArr, data2, i, i2, i3);
    }

    private CodePointTrie(char[] cArr, Data data2, int i, int i2, int i3) {
        this.ascii = new int[128];
        this.index = cArr;
        this.data = data2;
        this.dataLength = data2.getDataLength();
        this.highStart = i;
        this.index3NullOffset = i2;
        this.dataNullOffset = i3;
        for (int i4 = 0; i4 < 128; i4++) {
            this.ascii[i4] = data2.getFromIndex(i4);
        }
        int i5 = this.dataLength;
        this.nullValue = data2.getFromIndex(i3 >= i5 ? i5 - 2 : i3);
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x00b9 A[Catch:{ all -> 0x0144 }] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0124  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static ohos.global.icu.util.CodePointTrie fromBinary(ohos.global.icu.util.CodePointTrie.Type r16, ohos.global.icu.util.CodePointTrie.ValueWidth r17, java.nio.ByteBuffer r18) {
        /*
        // Method dump skipped, instructions count: 329
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.CodePointTrie.fromBinary(ohos.global.icu.util.CodePointTrie$Type, ohos.global.icu.util.CodePointTrie$ValueWidth, java.nio.ByteBuffer):ohos.global.icu.util.CodePointTrie");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.global.icu.util.CodePointTrie$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$global$icu$util$CodePointTrie$ValueWidth = new int[ValueWidth.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.global.icu.util.CodePointTrie$ValueWidth[] r0 = ohos.global.icu.util.CodePointTrie.ValueWidth.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.global.icu.util.CodePointTrie.AnonymousClass1.$SwitchMap$ohos$global$icu$util$CodePointTrie$ValueWidth = r0
                int[] r0 = ohos.global.icu.util.CodePointTrie.AnonymousClass1.$SwitchMap$ohos$global$icu$util$CodePointTrie$ValueWidth     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.global.icu.util.CodePointTrie$ValueWidth r1 = ohos.global.icu.util.CodePointTrie.ValueWidth.BITS_16     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.global.icu.util.CodePointTrie.AnonymousClass1.$SwitchMap$ohos$global$icu$util$CodePointTrie$ValueWidth     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.global.icu.util.CodePointTrie$ValueWidth r1 = ohos.global.icu.util.CodePointTrie.ValueWidth.BITS_32     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.global.icu.util.CodePointTrie.AnonymousClass1.$SwitchMap$ohos$global$icu$util$CodePointTrie$ValueWidth     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.global.icu.util.CodePointTrie$ValueWidth r1 = ohos.global.icu.util.CodePointTrie.ValueWidth.BITS_8     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.CodePointTrie.AnonymousClass1.<clinit>():void");
        }
    }

    public final ValueWidth getValueWidth() {
        return this.data.getValueWidth();
    }

    @Override // ohos.global.icu.util.CodePointMap
    public int get(int i) {
        return this.data.getFromIndex(cpIndex(i));
    }

    public final int asciiGet(int i) {
        return this.ascii[i];
    }

    private static final int maybeFilterValue(int i, int i2, int i3, CodePointMap.ValueFilter valueFilter) {
        if (i == i2) {
            return i3;
        }
        return valueFilter != null ? valueFilter.apply(i) : i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0121, code lost:
        return true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x015f A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x0158 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0186 A[LOOP:1: B:44:0x00a6->B:95:0x0186, LOOP_END] */
    @Override // ohos.global.icu.util.CodePointMap
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean getRange(int r24, ohos.global.icu.util.CodePointMap.ValueFilter r25, ohos.global.icu.util.CodePointMap.Range r26) {
        /*
        // Method dump skipped, instructions count: 397
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.util.CodePointTrie.getRange(int, ohos.global.icu.util.CodePointMap$ValueFilter, ohos.global.icu.util.CodePointMap$Range):boolean");
    }

    public final int toBinary(OutputStream outputStream) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(1416784179);
            dataOutputStream.writeChar(((this.dataLength & 983040) >> 4) | ((983040 & this.dataNullOffset) >> 8) | (getType().ordinal() << 6) | getValueWidth().ordinal());
            dataOutputStream.writeChar(this.index.length);
            dataOutputStream.writeChar(this.dataLength);
            dataOutputStream.writeChar(this.index3NullOffset);
            dataOutputStream.writeChar(this.dataNullOffset);
            dataOutputStream.writeChar(this.highStart >> 9);
            for (char c : this.index) {
                dataOutputStream.writeChar(c);
            }
            return 16 + (this.index.length * 2) + this.data.write(dataOutputStream);
        } catch (IOException e) {
            throw new ICUUncheckedIOException(e);
        }
    }

    /* access modifiers changed from: private */
    public static abstract class Data {
        /* access modifiers changed from: package-private */
        public abstract int getDataLength();

        /* access modifiers changed from: package-private */
        public abstract int getFromIndex(int i);

        /* access modifiers changed from: package-private */
        public abstract ValueWidth getValueWidth();

        /* access modifiers changed from: package-private */
        public abstract int write(DataOutputStream dataOutputStream) throws IOException;

        private Data() {
        }

        /* synthetic */ Data(AnonymousClass1 r1) {
            this();
        }
    }

    private static final class Data16 extends Data {
        char[] array;

        Data16(char[] cArr) {
            super(null);
            this.array = cArr;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public ValueWidth getValueWidth() {
            return ValueWidth.BITS_16;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int getDataLength() {
            return this.array.length;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int getFromIndex(int i) {
            return this.array[i];
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int write(DataOutputStream dataOutputStream) throws IOException {
            for (char c : this.array) {
                dataOutputStream.writeChar(c);
            }
            return this.array.length * 2;
        }
    }

    private static final class Data32 extends Data {
        int[] array;

        Data32(int[] iArr) {
            super(null);
            this.array = iArr;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public ValueWidth getValueWidth() {
            return ValueWidth.BITS_32;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int getDataLength() {
            return this.array.length;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int getFromIndex(int i) {
            return this.array[i];
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int write(DataOutputStream dataOutputStream) throws IOException {
            for (int i : this.array) {
                dataOutputStream.writeInt(i);
            }
            return this.array.length * 4;
        }
    }

    private static final class Data8 extends Data {
        byte[] array;

        Data8(byte[] bArr) {
            super(null);
            this.array = bArr;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public ValueWidth getValueWidth() {
            return ValueWidth.BITS_8;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int getDataLength() {
            return this.array.length;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int getFromIndex(int i) {
            return this.array[i] & 255;
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.global.icu.util.CodePointTrie.Data
        public int write(DataOutputStream dataOutputStream) throws IOException {
            for (byte b : this.array) {
                dataOutputStream.writeByte(b);
            }
            return this.array.length;
        }
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final int fastIndex(int i) {
        return this.index[i >> 6] + (i & 63);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final int smallIndex(Type type, int i) {
        if (i >= this.highStart) {
            return this.dataLength - 2;
        }
        return internalSmallIndex(type, i);
    }

    private final int internalSmallIndex(Type type, int i) {
        char c;
        int i2 = i >> 14;
        int i3 = type == Type.FAST ? i2 + 1020 : i2 + 64;
        char[] cArr = this.index;
        char c2 = cArr[cArr[i3] + ((i >> 9) & 31)];
        int i4 = (i >> 4) & 31;
        if ((32768 & c2) == 0) {
            c = cArr[c2 + i4];
        } else {
            int i5 = (c2 & 32767) + (i4 & -8) + (i4 >> 3);
            int i6 = i4 & 7;
            c = cArr[i5 + 1 + i6] | ((cArr[i5] << ((i6 * 2) + 2)) & 196608);
        }
        return c + (i & 15);
    }

    public static abstract class Fast extends CodePointTrie {
        public abstract int bmpGet(int i);

        public abstract int suppGet(int i);

        /* synthetic */ Fast(char[] cArr, Data data, int i, int i2, int i3, AnonymousClass1 r6) {
            this(cArr, data, i, i2, i3);
        }

        private Fast(char[] cArr, Data data, int i, int i2, int i3) {
            super(cArr, data, i, i2, i3, null);
        }

        public static Fast fromBinary(ValueWidth valueWidth, ByteBuffer byteBuffer) {
            return (Fast) CodePointTrie.fromBinary(Type.FAST, valueWidth, byteBuffer);
        }

        @Override // ohos.global.icu.util.CodePointTrie
        public final Type getType() {
            return Type.FAST;
        }

        /* access modifiers changed from: protected */
        @Override // ohos.global.icu.util.CodePointTrie
        @Deprecated
        public final int cpIndex(int i) {
            if (i >= 0) {
                if (i <= 65535) {
                    return fastIndex(i);
                }
                if (i <= 1114111) {
                    return smallIndex(Type.FAST, i);
                }
            }
            return this.dataLength - 1;
        }

        @Override // ohos.global.icu.util.CodePointMap
        public final CodePointMap.StringIterator stringIterator(CharSequence charSequence, int i) {
            return new FastStringIterator(this, charSequence, i, null);
        }

        private final class FastStringIterator extends CodePointMap.StringIterator {
            /* synthetic */ FastStringIterator(Fast fast, CharSequence charSequence, int i, AnonymousClass1 r4) {
                this(charSequence, i);
            }

            private FastStringIterator(CharSequence charSequence, int i) {
                super(charSequence, i);
            }

            @Override // ohos.global.icu.util.CodePointMap.StringIterator
            public boolean next() {
                int i;
                if (this.sIndex >= this.s.length()) {
                    return false;
                }
                CharSequence charSequence = this.s;
                int i2 = this.sIndex;
                this.sIndex = i2 + 1;
                char charAt = charSequence.charAt(i2);
                this.c = charAt;
                if (!Character.isSurrogate(charAt)) {
                    i = Fast.this.fastIndex(this.c);
                } else {
                    if (Normalizer2Impl.UTF16Plus.isSurrogateLead(charAt) && this.sIndex < this.s.length()) {
                        char charAt2 = this.s.charAt(this.sIndex);
                        if (Character.isLowSurrogate(charAt2)) {
                            this.sIndex++;
                            this.c = Character.toCodePoint(charAt, charAt2);
                            i = Fast.this.smallIndex(Type.FAST, this.c);
                        }
                    }
                    i = Fast.this.dataLength - 1;
                }
                this.value = Fast.this.data.getFromIndex(i);
                return true;
            }

            @Override // ohos.global.icu.util.CodePointMap.StringIterator
            public boolean previous() {
                int i;
                if (this.sIndex <= 0) {
                    return false;
                }
                CharSequence charSequence = this.s;
                int i2 = this.sIndex - 1;
                this.sIndex = i2;
                char charAt = charSequence.charAt(i2);
                this.c = charAt;
                if (!Character.isSurrogate(charAt)) {
                    i = Fast.this.fastIndex(this.c);
                } else {
                    if (!Normalizer2Impl.UTF16Plus.isSurrogateLead(charAt) && this.sIndex > 0) {
                        char charAt2 = this.s.charAt(this.sIndex - 1);
                        if (Character.isHighSurrogate(charAt2)) {
                            this.sIndex--;
                            this.c = Character.toCodePoint(charAt2, charAt);
                            i = Fast.this.smallIndex(Type.FAST, this.c);
                        }
                    }
                    i = Fast.this.dataLength - 1;
                }
                this.value = Fast.this.data.getFromIndex(i);
                return true;
            }
        }
    }

    public static abstract class Small extends CodePointTrie {
        /* synthetic */ Small(char[] cArr, Data data, int i, int i2, int i3, AnonymousClass1 r6) {
            this(cArr, data, i, i2, i3);
        }

        private Small(char[] cArr, Data data, int i, int i2, int i3) {
            super(cArr, data, i, i2, i3, null);
        }

        public static Small fromBinary(ValueWidth valueWidth, ByteBuffer byteBuffer) {
            return (Small) CodePointTrie.fromBinary(Type.SMALL, valueWidth, byteBuffer);
        }

        @Override // ohos.global.icu.util.CodePointTrie
        public final Type getType() {
            return Type.SMALL;
        }

        /* access modifiers changed from: protected */
        @Override // ohos.global.icu.util.CodePointTrie
        @Deprecated
        public final int cpIndex(int i) {
            if (i >= 0) {
                if (i <= CodePointTrie.SMALL_MAX) {
                    return fastIndex(i);
                }
                if (i <= 1114111) {
                    return smallIndex(Type.SMALL, i);
                }
            }
            return this.dataLength - 1;
        }

        @Override // ohos.global.icu.util.CodePointMap
        public final CodePointMap.StringIterator stringIterator(CharSequence charSequence, int i) {
            return new SmallStringIterator(this, charSequence, i, null);
        }

        private final class SmallStringIterator extends CodePointMap.StringIterator {
            /* synthetic */ SmallStringIterator(Small small, CharSequence charSequence, int i, AnonymousClass1 r4) {
                this(charSequence, i);
            }

            private SmallStringIterator(CharSequence charSequence, int i) {
                super(charSequence, i);
            }

            @Override // ohos.global.icu.util.CodePointMap.StringIterator
            public boolean next() {
                int i;
                if (this.sIndex >= this.s.length()) {
                    return false;
                }
                CharSequence charSequence = this.s;
                int i2 = this.sIndex;
                this.sIndex = i2 + 1;
                char charAt = charSequence.charAt(i2);
                this.c = charAt;
                if (!Character.isSurrogate(charAt)) {
                    i = Small.this.cpIndex(this.c);
                } else {
                    if (Normalizer2Impl.UTF16Plus.isSurrogateLead(charAt) && this.sIndex < this.s.length()) {
                        char charAt2 = this.s.charAt(this.sIndex);
                        if (Character.isLowSurrogate(charAt2)) {
                            this.sIndex++;
                            this.c = Character.toCodePoint(charAt, charAt2);
                            i = Small.this.smallIndex(Type.SMALL, this.c);
                        }
                    }
                    i = Small.this.dataLength - 1;
                }
                this.value = Small.this.data.getFromIndex(i);
                return true;
            }

            @Override // ohos.global.icu.util.CodePointMap.StringIterator
            public boolean previous() {
                int i;
                if (this.sIndex <= 0) {
                    return false;
                }
                CharSequence charSequence = this.s;
                int i2 = this.sIndex - 1;
                this.sIndex = i2;
                char charAt = charSequence.charAt(i2);
                this.c = charAt;
                if (!Character.isSurrogate(charAt)) {
                    i = Small.this.cpIndex(this.c);
                } else {
                    if (!Normalizer2Impl.UTF16Plus.isSurrogateLead(charAt) && this.sIndex > 0) {
                        char charAt2 = this.s.charAt(this.sIndex - 1);
                        if (Character.isHighSurrogate(charAt2)) {
                            this.sIndex--;
                            this.c = Character.toCodePoint(charAt2, charAt);
                            i = Small.this.smallIndex(Type.SMALL, this.c);
                        }
                    }
                    i = Small.this.dataLength - 1;
                }
                this.value = Small.this.data.getFromIndex(i);
                return true;
            }
        }
    }

    public static final class Fast16 extends Fast {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final char[] dataArray;

        Fast16(char[] cArr, char[] cArr2, int i, int i2, int i3) {
            super(cArr, new Data16(cArr2), i, i2, i3, null);
            this.dataArray = cArr2;
        }

        public static Fast16 fromBinary(ByteBuffer byteBuffer) {
            return (Fast16) CodePointTrie.fromBinary(Type.FAST, ValueWidth.BITS_16, byteBuffer);
        }

        @Override // ohos.global.icu.util.CodePointMap, ohos.global.icu.util.CodePointTrie
        public final int get(int i) {
            return this.dataArray[cpIndex(i)];
        }

        @Override // ohos.global.icu.util.CodePointTrie.Fast
        public final int bmpGet(int i) {
            return this.dataArray[fastIndex(i)];
        }

        @Override // ohos.global.icu.util.CodePointTrie.Fast
        public final int suppGet(int i) {
            return this.dataArray[smallIndex(Type.FAST, i)];
        }
    }

    public static final class Fast32 extends Fast {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final int[] dataArray;

        Fast32(char[] cArr, int[] iArr, int i, int i2, int i3) {
            super(cArr, new Data32(iArr), i, i2, i3, null);
            this.dataArray = iArr;
        }

        public static Fast32 fromBinary(ByteBuffer byteBuffer) {
            return (Fast32) CodePointTrie.fromBinary(Type.FAST, ValueWidth.BITS_32, byteBuffer);
        }

        @Override // ohos.global.icu.util.CodePointMap, ohos.global.icu.util.CodePointTrie
        public final int get(int i) {
            return this.dataArray[cpIndex(i)];
        }

        @Override // ohos.global.icu.util.CodePointTrie.Fast
        public final int bmpGet(int i) {
            return this.dataArray[fastIndex(i)];
        }

        @Override // ohos.global.icu.util.CodePointTrie.Fast
        public final int suppGet(int i) {
            return this.dataArray[smallIndex(Type.FAST, i)];
        }
    }

    public static final class Fast8 extends Fast {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final byte[] dataArray;

        Fast8(char[] cArr, byte[] bArr, int i, int i2, int i3) {
            super(cArr, new Data8(bArr), i, i2, i3, null);
            this.dataArray = bArr;
        }

        public static Fast8 fromBinary(ByteBuffer byteBuffer) {
            return (Fast8) CodePointTrie.fromBinary(Type.FAST, ValueWidth.BITS_8, byteBuffer);
        }

        @Override // ohos.global.icu.util.CodePointMap, ohos.global.icu.util.CodePointTrie
        public final int get(int i) {
            return this.dataArray[cpIndex(i)] & 255;
        }

        @Override // ohos.global.icu.util.CodePointTrie.Fast
        public final int bmpGet(int i) {
            return this.dataArray[fastIndex(i)] & 255;
        }

        @Override // ohos.global.icu.util.CodePointTrie.Fast
        public final int suppGet(int i) {
            return this.dataArray[smallIndex(Type.FAST, i)] & 255;
        }
    }

    public static final class Small16 extends Small {
        Small16(char[] cArr, char[] cArr2, int i, int i2, int i3) {
            super(cArr, new Data16(cArr2), i, i2, i3, null);
        }

        public static Small16 fromBinary(ByteBuffer byteBuffer) {
            return (Small16) CodePointTrie.fromBinary(Type.SMALL, ValueWidth.BITS_16, byteBuffer);
        }
    }

    public static final class Small32 extends Small {
        Small32(char[] cArr, int[] iArr, int i, int i2, int i3) {
            super(cArr, new Data32(iArr), i, i2, i3, null);
        }

        public static Small32 fromBinary(ByteBuffer byteBuffer) {
            return (Small32) CodePointTrie.fromBinary(Type.SMALL, ValueWidth.BITS_32, byteBuffer);
        }
    }

    public static final class Small8 extends Small {
        Small8(char[] cArr, byte[] bArr, int i, int i2, int i3) {
            super(cArr, new Data8(bArr), i, i2, i3, null);
        }

        public static Small8 fromBinary(ByteBuffer byteBuffer) {
            return (Small8) CodePointTrie.fromBinary(Type.SMALL, ValueWidth.BITS_8, byteBuffer);
        }
    }
}
