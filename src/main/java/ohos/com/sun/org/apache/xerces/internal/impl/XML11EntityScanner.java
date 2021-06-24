package ohos.com.sun.org.apache.xerces.internal.impl;

import java.io.IOException;
import ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner;
import ohos.com.sun.xml.internal.stream.Entity;

public class XML11EntityScanner extends XMLEntityScanner {
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    public int peekChar() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        char c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (!this.fCurrentEntity.isExternal()) {
            return c;
        }
        if (c == '\r' || c == 133 || c == 8232) {
            return 10;
        }
        return c;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0037, code lost:
        if (r7 == false) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002f, code lost:
        if (r1 != 8232) goto L_0x007c;
     */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int scanChar(ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r11) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 146
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanChar(ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):int");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0123, code lost:
        r3 = r0;
     */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String scanNmtoken() throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 318
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanNmtoken():java.lang.String");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ed, code lost:
        if (load(r1, false, false) == false) goto L_0x00f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x013d, code lost:
        if (load(r1, false, false) == false) goto L_0x00f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0147, code lost:
        r3 = r0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0156  */
    /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String scanName(ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r9) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 373
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanName(ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):java.lang.String");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00f1, code lost:
        r1 = r8.fCurrentEntity.position - r0;
        invokeListeners(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00fe, code lost:
        if (r1 != r8.fCurrentEntity.ch.length) goto L_0x0114;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0100, code lost:
        r4 = new char[(r8.fCurrentEntity.ch.length << 1)];
        java.lang.System.arraycopy(r8.fCurrentEntity.ch, r0, r4, 0, r1);
        r8.fCurrentEntity.ch = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0114, code lost:
        java.lang.System.arraycopy(r8.fCurrentEntity.ch, r0, r8.fCurrentEntity.ch, 0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0123, code lost:
        if (load(r1, false, false) == false) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x01a6, code lost:
        r1 = r8.fCurrentEntity.position - r0;
        invokeListeners(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x01b3, code lost:
        if (r1 != r8.fCurrentEntity.ch.length) goto L_0x01c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x01b5, code lost:
        r4 = new char[(r8.fCurrentEntity.ch.length << 1)];
        java.lang.System.arraycopy(r8.fCurrentEntity.ch, r0, r4, 0, r1);
        r8.fCurrentEntity.ch = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01c9, code lost:
        java.lang.System.arraycopy(r8.fCurrentEntity.ch, r0, r8.fCurrentEntity.ch, 0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x01d8, code lost:
        if (load(r1, false, false) == false) goto L_0x004e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01f1  */
    /* JADX WARNING: Removed duplicated region for block: B:83:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String scanNCName() throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 516
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanNCName():java.lang.String");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x011c, code lost:
        r0 = 0;
        r7 = false;
     */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00f6  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0190  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01fa A[RETURN] */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanQName(ohos.com.sun.org.apache.xerces.internal.xni.QName r13, ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType r14) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 515
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanQName(ohos.com.sun.org.apache.xerces.internal.xni.QName, ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType):boolean");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00db, code lost:
        r18.fCurrentEntity.position--;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0130, code lost:
        r4 = r2;
        r2 = r3;
        r3 = false;
     */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0130 A[EDGE_INSN: B:77:0x0130->B:41:0x0130 ?: BREAK  , SYNTHETIC] */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int scanContent(ohos.com.sun.org.apache.xerces.internal.xni.XMLString r19) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 505
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanContent(ohos.com.sun.org.apache.xerces.internal.xni.XMLString):int");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0113, code lost:
        r5 = r3;
        r3 = 0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x018c  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0125 A[EDGE_INSN: B:81:0x0125->B:40:0x0125 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x01b1 A[EDGE_INSN: B:96:0x01b1->B:71:0x01b1 ?: BREAK  , SYNTHETIC] */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int scanLiteral(int r18, ohos.com.sun.org.apache.xerces.internal.xni.XMLString r19, boolean r20) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 500
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanLiteral(int, ohos.com.sun.org.apache.xerces.internal.xni.XMLString, boolean):int");
    }

    /* JADX DEBUG: Multi-variable search result rejected for r17v0, resolved type: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner */
    /* JADX DEBUG: Multi-variable search result rejected for r4v37, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r4v39, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r4v49, resolved type: boolean */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r4v6 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0154, code lost:
        r17.fCurrentEntity.position--;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0195, code lost:
        r9 = r8;
        r4 = r4 ? 1 : 0;
        r4 = r4 ? 1 : 0;
        r8 = r4;
     */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0332  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x033f A[LOOP:0: B:1:0x0016->B:121:0x033f, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0342 A[LOOP:2: B:21:0x00db->B:122:0x0342, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x033c A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x01a4 A[EDGE_INSN: B:134:0x01a4->B:51:0x01a4 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanData(java.lang.String r18, ohos.com.sun.org.apache.xerces.internal.util.XMLStringBuffer r19) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 844
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.scanData(java.lang.String, ohos.com.sun.org.apache.xerces.internal.util.XMLStringBuffer):boolean");
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    public boolean skipChar(int i, XMLScanner.NameType nameType) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        int i2 = this.fCurrentEntity.position;
        char c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (c == i) {
            this.fCurrentEntity.position++;
            if (i == 10) {
                this.fCurrentEntity.lineNumber++;
                this.fCurrentEntity.columnNumber = 1;
            } else {
                this.fCurrentEntity.columnNumber++;
            }
            checkEntityLimit(nameType, this.fCurrentEntity, i2, this.fCurrentEntity.position - i2);
            return true;
        } else if (i == 10 && ((c == 8232 || c == 133) && this.fCurrentEntity.isExternal())) {
            this.fCurrentEntity.position++;
            this.fCurrentEntity.lineNumber++;
            this.fCurrentEntity.columnNumber = 1;
            checkEntityLimit(nameType, this.fCurrentEntity, i2, this.fCurrentEntity.position - i2);
            return true;
        } else if (i != 10 || c != '\r' || !this.fCurrentEntity.isExternal()) {
            return false;
        } else {
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                invokeListeners(1);
                this.fCurrentEntity.ch[0] = (char) c;
                load(1, false, false);
            }
            char[] cArr = this.fCurrentEntity.ch;
            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
            int i3 = scannedEntity.position + 1;
            scannedEntity.position = i3;
            char c2 = cArr[i3];
            if (c2 == '\n' || c2 == 133) {
                this.fCurrentEntity.position++;
            }
            this.fCurrentEntity.lineNumber++;
            this.fCurrentEntity.columnNumber = 1;
            checkEntityLimit(nameType, this.fCurrentEntity, i2, this.fCurrentEntity.position - i2);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0129  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x013a  */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean skipSpaces() throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 340
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11EntityScanner.skipSpaces():boolean");
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
    public boolean skipString(String str) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            load(0, true, true);
        }
        int length = str.length();
        int i = this.fCurrentEntity.position;
        for (int i2 = 0; i2 < length; i2++) {
            char[] cArr = this.fCurrentEntity.ch;
            Entity.ScannedEntity scannedEntity = this.fCurrentEntity;
            int i3 = scannedEntity.position;
            scannedEntity.position = i3 + 1;
            if (cArr[i3] != str.charAt(i2)) {
                this.fCurrentEntity.position -= i2 + 1;
                return false;
            }
            if (i2 < length - 1 && this.fCurrentEntity.position == this.fCurrentEntity.count) {
                invokeListeners(0);
                int i4 = i2 + 1;
                System.arraycopy(this.fCurrentEntity.ch, (this.fCurrentEntity.count - i2) - 1, this.fCurrentEntity.ch, 0, i4);
                if (load(i4, false, false)) {
                    this.fCurrentEntity.startPosition -= i4;
                    this.fCurrentEntity.position -= i4;
                    return false;
                }
            }
        }
        this.fCurrentEntity.columnNumber += length;
        if (!this.detectingVersion) {
            checkEntityLimit(null, this.fCurrentEntity, i, length);
        }
        return true;
    }
}
