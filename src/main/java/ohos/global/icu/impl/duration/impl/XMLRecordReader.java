package ohos.global.icu.impl.duration.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class XMLRecordReader implements RecordReader {
    private boolean atTag;
    private List<String> nameStack = new ArrayList();
    private Reader r;
    private String tag;

    public XMLRecordReader(Reader reader) {
        this.r = reader;
        if (getTag().startsWith("?xml")) {
            advance();
        }
        if (getTag().startsWith("!--")) {
            advance();
        }
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public boolean open(String str) {
        if (!getTag().equals(str)) {
            return false;
        }
        this.nameStack.add(str);
        advance();
        return true;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public boolean close() {
        int size = this.nameStack.size() - 1;
        String tag2 = getTag();
        if (!tag2.equals("/" + this.nameStack.get(size))) {
            return false;
        }
        this.nameStack.remove(size);
        advance();
        return true;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public boolean bool(String str) {
        String string = string(str);
        if (string != null) {
            return "true".equals(string);
        }
        return false;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public boolean[] boolArray(String str) {
        String[] stringArray = stringArray(str);
        if (stringArray == null) {
            return null;
        }
        boolean[] zArr = new boolean[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            zArr[i] = "true".equals(stringArray[i]);
        }
        return zArr;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public char character(String str) {
        String string = string(str);
        if (string != null) {
            return string.charAt(0);
        }
        return 65535;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public char[] characterArray(String str) {
        String[] stringArray = stringArray(str);
        if (stringArray == null) {
            return null;
        }
        char[] cArr = new char[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            cArr[i] = stringArray[i].charAt(0);
        }
        return cArr;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public byte namedIndex(String str, String[] strArr) {
        String string = string(str);
        if (string == null) {
            return -1;
        }
        for (int i = 0; i < strArr.length; i++) {
            if (string.equals(strArr[i])) {
                return (byte) i;
            }
        }
        return -1;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public byte[] namedIndexArray(String str, String[] strArr) {
        String[] stringArray = stringArray(str);
        if (stringArray == null) {
            return null;
        }
        byte[] bArr = new byte[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            String str2 = stringArray[i];
            int i2 = 0;
            while (true) {
                if (i2 >= strArr.length) {
                    bArr[i] = -1;
                    break;
                } else if (strArr[i2].equals(str2)) {
                    bArr[i] = (byte) i2;
                    break;
                } else {
                    i2++;
                }
            }
        }
        return bArr;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public String string(String str) {
        if (!match(str)) {
            return null;
        }
        String readData = readData();
        if (match("/" + str)) {
            return readData;
        }
        return null;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public String[] stringArray(String str) {
        if (match(str + "List")) {
            ArrayList arrayList = new ArrayList();
            while (true) {
                String string = string(str);
                if (string == null) {
                    break;
                }
                if ("Null".equals(string)) {
                    string = null;
                }
                arrayList.add(string);
            }
            if (match("/" + str + "List")) {
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
        }
        return null;
    }

    @Override // ohos.global.icu.impl.duration.impl.RecordReader
    public String[][] stringTable(String str) {
        if (!match(str + "Table")) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        while (true) {
            String[] stringArray = stringArray(str);
            if (stringArray == null) {
                break;
            }
            arrayList.add(stringArray);
        }
        if (match("/" + str + "Table")) {
            return (String[][]) arrayList.toArray(new String[arrayList.size()][]);
        }
        return null;
    }

    private boolean match(String str) {
        if (!getTag().equals(str)) {
            return false;
        }
        advance();
        return true;
    }

    private String getTag() {
        if (this.tag == null) {
            this.tag = readNextTag();
        }
        return this.tag;
    }

    private void advance() {
        this.tag = null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x00f3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String readData() {
        /*
        // Method dump skipped, instructions count: 251
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.duration.impl.XMLRecordReader.readData():java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String readNextTag() {
        /*
            r5 = this;
        L_0x0000:
            boolean r0 = r5.atTag
            r1 = -1
            if (r0 != 0) goto L_0x0036
            int r0 = r5.readChar()
            r2 = 60
            if (r0 == r2) goto L_0x0031
            if (r0 != r1) goto L_0x0010
            goto L_0x0031
        L_0x0010:
            boolean r2 = ohos.global.icu.lang.UCharacter.isWhitespace(r0)
            if (r2 != 0) goto L_0x0000
            java.io.PrintStream r2 = java.lang.System.err
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unexpected non-whitespace character "
            r3.append(r4)
            java.lang.String r0 = java.lang.Integer.toHexString(r0)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.println(r0)
            goto L_0x0036
        L_0x0031:
            if (r0 != r2) goto L_0x0036
            r0 = 1
            r5.atTag = r0
        L_0x0036:
            boolean r0 = r5.atTag
            if (r0 == 0) goto L_0x0057
            r0 = 0
            r5.atTag = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
        L_0x0042:
            int r2 = r5.readChar()
            r3 = 62
            if (r2 == r3) goto L_0x0052
            if (r2 != r1) goto L_0x004d
            goto L_0x0052
        L_0x004d:
            char r2 = (char) r2
            r0.append(r2)
            goto L_0x0042
        L_0x0052:
            java.lang.String r5 = r0.toString()
            return r5
        L_0x0057:
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.duration.impl.XMLRecordReader.readNextTag():java.lang.String");
    }

    /* access modifiers changed from: package-private */
    public int readChar() {
        try {
            return this.r.read();
        } catch (IOException unused) {
            return -1;
        }
    }
}
