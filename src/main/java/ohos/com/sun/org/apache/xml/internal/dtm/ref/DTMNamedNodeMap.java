package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import ohos.com.sun.org.apache.xml.internal.dtm.DTM;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;

public class DTMNamedNodeMap implements NamedNodeMap {
    DTM dtm;
    int element;
    short m_count = -1;

    public DTMNamedNodeMap(DTM dtm2, int i) {
        this.dtm = dtm2;
        this.element = i;
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public int getLength() {
        if (this.m_count == -1) {
            short s = 0;
            int firstAttribute = this.dtm.getFirstAttribute(this.element);
            while (firstAttribute != -1) {
                s = (short) (s + 1);
                firstAttribute = this.dtm.getNextAttribute(firstAttribute);
            }
            this.m_count = s;
        }
        return this.m_count;
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public Node getNamedItem(String str) {
        int firstAttribute = this.dtm.getFirstAttribute(this.element);
        while (firstAttribute != -1) {
            if (this.dtm.getNodeName(firstAttribute).equals(str)) {
                return this.dtm.getNode(firstAttribute);
            }
            firstAttribute = this.dtm.getNextAttribute(firstAttribute);
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public Node item(int i) {
        int firstAttribute = this.dtm.getFirstAttribute(this.element);
        int i2 = 0;
        while (firstAttribute != -1) {
            if (i2 == i) {
                return this.dtm.getNode(firstAttribute);
            }
            i2++;
            firstAttribute = this.dtm.getNextAttribute(firstAttribute);
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public Node setNamedItem(Node node) {
        throw new DTMException(7);
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public Node removeNamedItem(String str) {
        throw new DTMException(7);
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public Node getNamedItemNS(String str, String str2) {
        int firstAttribute = this.dtm.getFirstAttribute(this.element);
        while (firstAttribute != -1) {
            if (str2.equals(this.dtm.getLocalName(firstAttribute))) {
                String namespaceURI = this.dtm.getNamespaceURI(firstAttribute);
                if ((str == null && namespaceURI == null) || (str != null && str.equals(namespaceURI))) {
                    return this.dtm.getNode(firstAttribute);
                }
            }
            firstAttribute = this.dtm.getNextAttribute(firstAttribute);
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public Node setNamedItemNS(Node node) throws DOMException {
        throw new DTMException(7);
    }

    @Override // ohos.org.w3c.dom.NamedNodeMap
    public Node removeNamedItemNS(String str, String str2) throws DOMException {
        throw new DTMException(7);
    }

    public class DTMException extends DOMException {
        static final long serialVersionUID = -8290238117162437678L;

        public DTMException(short s, String str) {
            super(s, str);
        }

        public DTMException(short s) {
            super(s, "");
        }
    }
}
