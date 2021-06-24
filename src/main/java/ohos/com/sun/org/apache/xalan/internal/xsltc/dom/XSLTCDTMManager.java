package ohos.com.sun.org.apache.xalan.internal.xsltc.dom;

import ohos.com.sun.org.apache.xml.internal.dtm.DTM;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
import ohos.javax.xml.transform.Source;

public class XSLTCDTMManager extends DTMManagerDefault {
    private static final boolean DEBUG = false;
    private static final boolean DUMPTREE = false;

    public static XSLTCDTMManager newInstance() {
        return new XSLTCDTMManager();
    }

    public static XSLTCDTMManager createNewDTMManagerInstance() {
        return newInstance();
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault, ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTM getDTM(Source source, boolean z, DTMWSFilter dTMWSFilter, boolean z2, boolean z3) {
        return getDTM(source, z, dTMWSFilter, z2, z3, false, 0, true, false);
    }

    public DTM getDTM(Source source, boolean z, DTMWSFilter dTMWSFilter, boolean z2, boolean z3, boolean z4) {
        return getDTM(source, z, dTMWSFilter, z2, z3, false, 0, z4, false);
    }

    public DTM getDTM(Source source, boolean z, DTMWSFilter dTMWSFilter, boolean z2, boolean z3, boolean z4, boolean z5) {
        return getDTM(source, z, dTMWSFilter, z2, z3, false, 0, z4, z5);
    }

    public DTM getDTM(Source source, boolean z, DTMWSFilter dTMWSFilter, boolean z2, boolean z3, boolean z4, int i, boolean z5) {
        return getDTM(source, z, dTMWSFilter, z2, z3, z4, i, z5, false);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:72|(1:76)|(1:80)|81|82|83|84|85|(1:87)|88) */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x01b9, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01a7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01a9, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x01b0, code lost:
        throw new ohos.com.sun.org.apache.xml.internal.utils.WrappedRuntimeException(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x01b1, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01b3, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x01b4, code lost:
        if (r15 == false) goto L_0x01b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x01b6, code lost:
        releaseXMLReader(r14);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:84:0x019e */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01a3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xml.internal.dtm.DTM getDTM(ohos.javax.xml.transform.Source r18, boolean r19, ohos.com.sun.org.apache.xml.internal.dtm.DTMWSFilter r20, boolean r21, boolean r22, boolean r23, int r24, boolean r25, boolean r26) {
        /*
        // Method dump skipped, instructions count: 442
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager.getDTM(ohos.javax.xml.transform.Source, boolean, ohos.com.sun.org.apache.xml.internal.dtm.DTMWSFilter, boolean, boolean, boolean, int, boolean, boolean):ohos.com.sun.org.apache.xml.internal.dtm.DTM");
    }
}
