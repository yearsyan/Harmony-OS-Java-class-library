package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import ohos.com.sun.org.apache.xml.internal.dtm.DTM;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMException;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMFilter;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMIterator;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMManager;
import ohos.com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM;
import ohos.com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode;
import ohos.com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM;
import ohos.com.sun.org.apache.xml.internal.res.XMLMessages;
import ohos.com.sun.org.apache.xml.internal.utils.PrefixResolver;
import ohos.com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
import ohos.com.sun.org.apache.xml.internal.utils.XMLReaderManager;
import ohos.javax.xml.transform.Source;
import ohos.javax.xml.transform.dom.DOMSource;
import ohos.javax.xml.transform.sax.SAXSource;
import ohos.jdk.xml.internal.JdkXmlUtils;
import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.Node;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.XMLReader;
import ohos.org.xml.sax.helpers.DefaultHandler;

public class DTMManagerDefault extends DTMManager {
    private static final boolean DEBUG = false;
    private static final boolean DUMPTREE = false;
    protected DefaultHandler m_defaultHandler = new DefaultHandler();
    int[] m_dtm_offsets = new int[256];
    protected DTM[] m_dtms = new DTM[256];
    private ExpandedNameTable m_expandedNameTable = new ExpandedNameTable();
    protected XMLReaderManager m_readerManager = null;

    public synchronized void addDTM(DTM dtm, int i) {
        addDTM(dtm, i, 0);
    }

    public synchronized void addDTM(DTM dtm, int i, int i2) {
        if (i < 65536) {
            int length = this.m_dtms.length;
            if (length <= i) {
                int min = Math.min(i + 256, 65536);
                DTM[] dtmArr = new DTM[min];
                System.arraycopy(this.m_dtms, 0, dtmArr, 0, length);
                this.m_dtms = dtmArr;
                int[] iArr = new int[min];
                System.arraycopy(this.m_dtm_offsets, 0, iArr, 0, length);
                this.m_dtm_offsets = iArr;
            }
            this.m_dtms[i] = dtm;
            this.m_dtm_offsets[i] = i2;
            dtm.documentRegistration();
        } else {
            throw new DTMException(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
        }
    }

    public synchronized int getFirstFreeDTMID() {
        int length = this.m_dtms.length;
        for (int i = 1; i < length; i++) {
            if (this.m_dtms[i] == null) {
                return i;
            }
        }
        return length;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:60:0x00da */
    /* JADX DEBUG: Multi-variable search result rejected for r0v20, resolved type: ohos.com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter */
    /* JADX DEBUG: Multi-variable search result rejected for r0v21, resolved type: ohos.com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter */
    /* JADX DEBUG: Multi-variable search result rejected for r0v30, resolved type: ohos.com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter */
    /* JADX DEBUG: Multi-variable search result rejected for r0v31, resolved type: ohos.com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter */
    /* JADX DEBUG: Multi-variable search result rejected for r0v32, resolved type: ohos.com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter */
    /* JADX DEBUG: Multi-variable search result rejected for r0v45, resolved type: ohos.com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:107:0x016a */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f1 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0112  */
    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized ohos.com.sun.org.apache.xml.internal.dtm.DTM getDTM(ohos.javax.xml.transform.Source r17, boolean r18, ohos.com.sun.org.apache.xml.internal.dtm.DTMWSFilter r19, boolean r20, boolean r21) {
        /*
        // Method dump skipped, instructions count: 465
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault.getDTM(ohos.javax.xml.transform.Source, boolean, ohos.com.sun.org.apache.xml.internal.dtm.DTMWSFilter, boolean, boolean):ohos.com.sun.org.apache.xml.internal.dtm.DTM");
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized int getDTMHandleFromNode(Node node) {
        int i;
        int handleOfNode;
        if (node == null) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_NODE_NON_NULL", null));
        } else if (node instanceof DTMNodeProxy) {
            return ((DTMNodeProxy) node).getDTMNodeNumber();
        } else {
            int length = this.m_dtms.length;
            for (int i2 = 0; i2 < length; i2++) {
                DTM dtm = this.m_dtms[i2];
                if (!(dtm == null || !(dtm instanceof DOM2DTM) || (handleOfNode = ((DOM2DTM) dtm).getHandleOfNode(node)) == -1)) {
                    return handleOfNode;
                }
            }
            Node node2 = node;
            for (Node ownerElement = node.getNodeType() == 2 ? ((Attr) node).getOwnerElement() : node.getParentNode(); ownerElement != null; ownerElement = ownerElement.getParentNode()) {
                node2 = ownerElement;
            }
            DOM2DTM dom2dtm = (DOM2DTM) getDTM(new DOMSource(node2), false, null, true, true);
            if (node instanceof DOM2DTMdefaultNamespaceDeclarationNode) {
                i = dom2dtm.getAttributeNode(dom2dtm.getHandleOfNode(((Attr) node).getOwnerElement()), node.getNamespaceURI(), node.getLocalName());
            } else {
                i = dom2dtm.getHandleOfNode(node);
            }
            if (-1 != i) {
                return i;
            }
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_COULD_NOT_RESOLVE_NODE", null));
        }
    }

    public synchronized XMLReader getXMLReader(Source source) {
        XMLReader xMLReader;
        try {
            xMLReader = source instanceof SAXSource ? ((SAXSource) source).getXMLReader() : null;
            if (xMLReader == null) {
                if (this.m_readerManager == null) {
                    this.m_readerManager = XMLReaderManager.getInstance(super.overrideDefaultParser());
                }
                xMLReader = this.m_readerManager.getXMLReader();
            }
        } catch (SAXException e) {
            throw new DTMException(e.getMessage(), e);
        }
        return xMLReader;
    }

    public synchronized void releaseXMLReader(XMLReader xMLReader) {
        if (this.m_readerManager != null) {
            this.m_readerManager.releaseXMLReader(xMLReader);
        }
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTM getDTM(int i) {
        try {
        } catch (ArrayIndexOutOfBoundsException e) {
            if (i == -1) {
                return null;
            }
            throw e;
        }
        return this.m_dtms[i >>> 16];
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized int getDTMIdentity(DTM dtm) {
        if (dtm instanceof DTMDefaultBase) {
            DTMDefaultBase dTMDefaultBase = (DTMDefaultBase) dtm;
            if (dTMDefaultBase.getManager() != this) {
                return -1;
            }
            return dTMDefaultBase.getDTMIDs().elementAt(0);
        }
        int length = this.m_dtms.length;
        for (int i = 0; i < length; i++) {
            if (this.m_dtms[i] == dtm && this.m_dtm_offsets[i] == 0) {
                return i << 16;
            }
        }
        return -1;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized boolean release(DTM dtm, boolean z) {
        if (dtm instanceof SAX2DTM) {
            ((SAX2DTM) dtm).clearCoRoutine();
        }
        if (dtm instanceof DTMDefaultBase) {
            SuballocatedIntVector dTMIDs = ((DTMDefaultBase) dtm).getDTMIDs();
            for (int size = dTMIDs.size() - 1; size >= 0; size--) {
                this.m_dtms[dTMIDs.elementAt(size) >>> 16] = null;
            }
        } else {
            int dTMIdentity = getDTMIdentity(dtm);
            if (dTMIdentity >= 0) {
                this.m_dtms[dTMIdentity >>> 16] = null;
            }
        }
        dtm.documentRelease();
        return true;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTM createDocumentFragment() {
        try {
        } catch (Exception e) {
            throw new DTMException(e);
        }
        return getDTM(new DOMSource(JdkXmlUtils.getDOMFactory(super.overrideDefaultParser()).newDocumentBuilder().newDocument().createDocumentFragment()), true, null, false, false);
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(int i, DTMFilter dTMFilter, boolean z) {
        return null;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(String str, PrefixResolver prefixResolver) {
        return null;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(int i) {
        return null;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMManager
    public synchronized DTMIterator createDTMIterator(Object obj, int i) {
        return null;
    }

    public ExpandedNameTable getExpandedNameTable(DTM dtm) {
        return this.m_expandedNameTable;
    }
}
