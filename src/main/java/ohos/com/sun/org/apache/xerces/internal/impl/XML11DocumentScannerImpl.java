package ohos.com.sun.org.apache.xerces.internal.impl;

import java.io.IOException;
import ohos.com.sun.org.apache.xerces.internal.util.XML11Char;
import ohos.com.sun.org.apache.xerces.internal.util.XMLChar;
import ohos.com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLString;
import ohos.com.sun.org.apache.xerces.internal.xni.XNIException;
import ohos.com.sun.org.apache.xml.internal.serializer.SerializerConstants;

public class XML11DocumentScannerImpl extends XMLDocumentScannerImpl {
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
    private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();

    /* access modifiers changed from: protected */
    public String getVersionNotSupportedKey() {
        return "VersionNotSupported11";
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl
    public int scanContent(XMLStringBuffer xMLStringBuffer) throws IOException, XNIException {
        this.fTempString.length = 0;
        int scanContent = this.fEntityScanner.scanContent(this.fTempString);
        xMLStringBuffer.append(this.fTempString);
        if (scanContent == 13 || scanContent == 133 || scanContent == 8232) {
            this.fEntityScanner.scanChar(null);
            xMLStringBuffer.append((char) scanContent);
            scanContent = -1;
        }
        if (scanContent != 93) {
            return scanContent;
        }
        xMLStringBuffer.append((char) this.fEntityScanner.scanChar(null));
        this.fInScanContent = true;
        if (this.fEntityScanner.skipChar(93, null)) {
            xMLStringBuffer.append(']');
            while (this.fEntityScanner.skipChar(93, null)) {
                xMLStringBuffer.append(']');
            }
            if (this.fEntityScanner.skipChar(62, null)) {
                reportFatalError("CDEndInContent", null);
            }
        }
        this.fInScanContent = false;
        return -1;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x01fc  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x020d A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01f2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanAttributeValue(ohos.com.sun.org.apache.xerces.internal.xni.XMLString r17, ohos.com.sun.org.apache.xerces.internal.xni.XMLString r18, java.lang.String r19, boolean r20, java.lang.String r21, boolean r22) throws java.io.IOException, ohos.com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
        // Method dump skipped, instructions count: 561
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.XML11DocumentScannerImpl.scanAttributeValue(ohos.com.sun.org.apache.xerces.internal.xni.XMLString, ohos.com.sun.org.apache.xerces.internal.xni.XMLString, java.lang.String, boolean, java.lang.String, boolean):boolean");
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean scanPubidLiteral(XMLString xMLString) throws IOException, XNIException {
        int scanChar = this.fEntityScanner.scanChar(null);
        if (scanChar == 39 || scanChar == 34) {
            this.fStringBuffer.clear();
            boolean z = true;
            boolean z2 = true;
            while (true) {
                int scanChar2 = this.fEntityScanner.scanChar(null);
                if (scanChar2 == 32 || scanChar2 == 10 || scanChar2 == 13 || scanChar2 == 133 || scanChar2 == 8232) {
                    if (!z) {
                        this.fStringBuffer.append(' ');
                        z = true;
                    }
                } else if (scanChar2 == scanChar) {
                    if (z) {
                        this.fStringBuffer.length--;
                    }
                    xMLString.setValues(this.fStringBuffer);
                    return z2;
                } else if (XMLChar.isPubid(scanChar2)) {
                    this.fStringBuffer.append((char) scanChar2);
                    z = false;
                } else if (scanChar2 == -1) {
                    reportFatalError("PublicIDUnterminated", null);
                    return false;
                } else {
                    reportFatalError("InvalidCharInPublicID", new Object[]{Integer.toHexString(scanChar2)});
                    z2 = false;
                }
            }
        } else {
            reportFatalError("QuoteRequiredInPublicID", null);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public void normalizeWhitespace(XMLString xMLString) {
        int i = xMLString.offset + xMLString.length;
        for (int i2 = xMLString.offset; i2 < i; i2++) {
            if (XMLChar.isSpace(xMLString.ch[i2])) {
                xMLString.ch[i2] = ' ';
            }
        }
    }

    /* access modifiers changed from: protected */
    public void normalizeWhitespace(XMLString xMLString, int i) {
        int i2 = xMLString.offset + xMLString.length;
        for (int i3 = xMLString.offset + i; i3 < i2; i3++) {
            if (XMLChar.isSpace(xMLString.ch[i3])) {
                xMLString.ch[i3] = ' ';
            }
        }
    }

    /* access modifiers changed from: protected */
    public int isUnchangedByNormalization(XMLString xMLString) {
        int i = xMLString.offset + xMLString.length;
        for (int i2 = xMLString.offset; i2 < i; i2++) {
            if (XMLChar.isSpace(xMLString.ch[i2])) {
                return i2 - xMLString.offset;
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean isInvalid(int i) {
        return XML11Char.isXML11Invalid(i);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean isInvalidLiteral(int i) {
        return !XML11Char.isXML11ValidLiteral(i);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean isValidNameChar(int i) {
        return XML11Char.isXML11Name(i);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean isValidNameStartChar(int i) {
        return XML11Char.isXML11NameStart(i);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean isValidNCName(int i) {
        return XML11Char.isXML11NCName(i);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean isValidNameStartHighSurrogate(int i) {
        return XML11Char.isXML11NameHighSurrogate(i);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.impl.XMLScanner
    public boolean versionSupported(String str) {
        return str.equals(SerializerConstants.XMLVERSION11) || str.equals("1.0");
    }
}
