package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import ohos.com.sun.org.apache.xml.internal.dtm.DTM;

public class DTMChildIterNodeList extends DTMNodeListBase {
    private int m_firstChild;
    private DTM m_parentDTM;

    private DTMChildIterNodeList() {
    }

    public DTMChildIterNodeList(DTM dtm, int i) {
        this.m_parentDTM = dtm;
        this.m_firstChild = dtm.getFirstChild(i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0011 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0013  */
    @Override // ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, ohos.org.w3c.dom.NodeList
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.org.w3c.dom.Node item(int r3) {
        /*
            r2 = this;
            int r0 = r2.m_firstChild
        L_0x0002:
            r1 = -1
            int r3 = r3 + r1
            if (r3 < 0) goto L_0x000f
            if (r0 == r1) goto L_0x000f
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r1 = r2.m_parentDTM
            int r0 = r1.getNextSibling(r0)
            goto L_0x0002
        L_0x000f:
            if (r0 != r1) goto L_0x0013
            r2 = 0
            return r2
        L_0x0013:
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r2 = r2.m_parentDTM
            ohos.org.w3c.dom.Node r2 = r2.getNode(r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMChildIterNodeList.item(int):ohos.org.w3c.dom.Node");
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMNodeListBase, ohos.org.w3c.dom.NodeList
    public int getLength() {
        int i = this.m_firstChild;
        int i2 = 0;
        while (i != -1) {
            i2++;
            i = this.m_parentDTM.getNextSibling(i);
        }
        return i2;
    }
}
