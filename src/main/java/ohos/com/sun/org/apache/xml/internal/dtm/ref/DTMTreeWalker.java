package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import ohos.com.sun.org.apache.xml.internal.dtm.DTM;
import ohos.com.sun.org.apache.xml.internal.utils.NodeConsumer;
import ohos.com.sun.org.apache.xml.internal.utils.XMLString;
import ohos.javax.xml.transform.Result;
import ohos.org.xml.sax.ContentHandler;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.ext.LexicalHandler;
import ohos.org.xml.sax.helpers.AttributesImpl;

public class DTMTreeWalker {
    private ContentHandler m_contentHandler = null;
    protected DTM m_dtm;
    boolean nextIsRaw = false;

    public void setDTM(DTM dtm) {
        this.m_dtm = dtm;
    }

    public ContentHandler getcontentHandler() {
        return this.m_contentHandler;
    }

    public void setcontentHandler(ContentHandler contentHandler) {
        this.m_contentHandler = contentHandler;
    }

    public DTMTreeWalker() {
    }

    public DTMTreeWalker(ContentHandler contentHandler, DTM dtm) {
        this.m_contentHandler = contentHandler;
        this.m_dtm = dtm;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0027, code lost:
        if (-1 == r0) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        endNode(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        r0 = -1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void traverse(int r5) throws ohos.org.xml.sax.SAXException {
        /*
            r4 = this;
            r0 = r5
        L_0x0001:
            r1 = -1
            if (r1 == r0) goto L_0x0030
            r4.startNode(r0)
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r2 = r4.m_dtm
            int r2 = r2.getFirstChild(r0)
        L_0x000d:
            if (r1 != r2) goto L_0x002e
            r4.endNode(r0)
            if (r5 != r0) goto L_0x0015
            goto L_0x002e
        L_0x0015:
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r2 = r4.m_dtm
            int r2 = r2.getNextSibling(r0)
            if (r1 != r2) goto L_0x000d
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r3 = r4.m_dtm
            int r0 = r3.getParent(r0)
            if (r1 == r0) goto L_0x0027
            if (r5 != r0) goto L_0x000d
        L_0x0027:
            if (r1 == r0) goto L_0x002c
            r4.endNode(r0)
        L_0x002c:
            r0 = r1
            goto L_0x0001
        L_0x002e:
            r0 = r2
            goto L_0x0001
        L_0x0030:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMTreeWalker.traverse(int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        r4 = -1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void traverse(int r4, int r5) throws ohos.org.xml.sax.SAXException {
        /*
            r3 = this;
        L_0x0000:
            r0 = -1
            if (r0 == r4) goto L_0x002e
            r3.startNode(r4)
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r1 = r3.m_dtm
            int r1 = r1.getFirstChild(r4)
        L_0x000c:
            if (r0 != r1) goto L_0x002c
            r3.endNode(r4)
            if (r0 == r5) goto L_0x0016
            if (r5 != r4) goto L_0x0016
            goto L_0x002c
        L_0x0016:
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r1 = r3.m_dtm
            int r1 = r1.getNextSibling(r4)
            if (r0 != r1) goto L_0x000c
            ohos.com.sun.org.apache.xml.internal.dtm.DTM r2 = r3.m_dtm
            int r4 = r2.getParent(r4)
            if (r0 == r4) goto L_0x002a
            if (r0 == r5) goto L_0x000c
            if (r5 != r4) goto L_0x000c
        L_0x002a:
            r4 = r0
            goto L_0x0000
        L_0x002c:
            r4 = r1
            goto L_0x0000
        L_0x002e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMTreeWalker.traverse(int, int):void");
    }

    private final void dispatachChars(int i) throws SAXException {
        this.m_dtm.dispatchCharactersEvents(i, this.m_contentHandler, false);
    }

    /* access modifiers changed from: protected */
    public void startNode(int i) throws SAXException {
        boolean z = this.m_contentHandler instanceof NodeConsumer;
        short nodeType = this.m_dtm.getNodeType(i);
        String str = "";
        if (nodeType == 1) {
            DTM dtm = this.m_dtm;
            int firstNamespaceNode = dtm.getFirstNamespaceNode(i, true);
            while (-1 != firstNamespaceNode) {
                this.m_contentHandler.startPrefixMapping(dtm.getNodeNameX(firstNamespaceNode), dtm.getNodeValue(firstNamespaceNode));
                firstNamespaceNode = dtm.getNextNamespaceNode(i, firstNamespaceNode, true);
            }
            String namespaceURI = dtm.getNamespaceURI(i);
            if (namespaceURI != null) {
                str = namespaceURI;
            }
            AttributesImpl attributesImpl = new AttributesImpl();
            for (int firstAttribute = dtm.getFirstAttribute(i); firstAttribute != -1; firstAttribute = dtm.getNextAttribute(firstAttribute)) {
                attributesImpl.addAttribute(dtm.getNamespaceURI(firstAttribute), dtm.getLocalName(firstAttribute), dtm.getNodeName(firstAttribute), "CDATA", dtm.getNodeValue(firstAttribute));
            }
            this.m_contentHandler.startElement(str, this.m_dtm.getLocalName(i), this.m_dtm.getNodeName(i), attributesImpl);
        } else if (nodeType != 11) {
            if (nodeType != 3) {
                if (nodeType == 4) {
                    ContentHandler contentHandler = this.m_contentHandler;
                    boolean z2 = contentHandler instanceof LexicalHandler;
                    LexicalHandler lexicalHandler = z2 ? (LexicalHandler) contentHandler : null;
                    if (z2) {
                        lexicalHandler.startCDATA();
                    }
                    dispatachChars(i);
                    if (z2) {
                        lexicalHandler.endCDATA();
                    }
                } else if (nodeType == 5) {
                    ContentHandler contentHandler2 = this.m_contentHandler;
                    if (contentHandler2 instanceof LexicalHandler) {
                        ((LexicalHandler) contentHandler2).startEntity(this.m_dtm.getNodeName(i));
                    }
                } else if (nodeType == 7) {
                    String nodeName = this.m_dtm.getNodeName(i);
                    if (nodeName.equals("xslt-next-is-raw")) {
                        this.nextIsRaw = true;
                    } else {
                        this.m_contentHandler.processingInstruction(nodeName, this.m_dtm.getNodeValue(i));
                    }
                } else if (nodeType == 8) {
                    XMLString stringValue = this.m_dtm.getStringValue(i);
                    ContentHandler contentHandler3 = this.m_contentHandler;
                    if (contentHandler3 instanceof LexicalHandler) {
                        stringValue.dispatchAsComment((LexicalHandler) contentHandler3);
                    }
                } else if (nodeType == 9) {
                    this.m_contentHandler.startDocument();
                }
            } else if (this.nextIsRaw) {
                this.nextIsRaw = false;
                this.m_contentHandler.processingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, str);
                dispatachChars(i);
                this.m_contentHandler.processingInstruction(Result.PI_ENABLE_OUTPUT_ESCAPING, str);
            } else {
                dispatachChars(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void endNode(int i) throws SAXException {
        short nodeType = this.m_dtm.getNodeType(i);
        if (nodeType == 1) {
            String namespaceURI = this.m_dtm.getNamespaceURI(i);
            if (namespaceURI == null) {
                namespaceURI = "";
            }
            this.m_contentHandler.endElement(namespaceURI, this.m_dtm.getLocalName(i), this.m_dtm.getNodeName(i));
            int firstNamespaceNode = this.m_dtm.getFirstNamespaceNode(i, true);
            while (-1 != firstNamespaceNode) {
                this.m_contentHandler.endPrefixMapping(this.m_dtm.getNodeNameX(firstNamespaceNode));
                firstNamespaceNode = this.m_dtm.getNextNamespaceNode(i, firstNamespaceNode, true);
            }
        } else if (nodeType == 9) {
            this.m_contentHandler.endDocument();
        } else if (nodeType != 4 && nodeType == 5) {
            ContentHandler contentHandler = this.m_contentHandler;
            if (contentHandler instanceof LexicalHandler) {
                ((LexicalHandler) contentHandler).endEntity(this.m_dtm.getNodeName(i));
            }
        }
    }
}
