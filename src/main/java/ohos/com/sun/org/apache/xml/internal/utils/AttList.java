package ohos.com.sun.org.apache.xml.internal.utils;

import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.xml.sax.Attributes;

public class AttList implements Attributes {
    NamedNodeMap m_attrs;
    int m_lastIndex = (this.m_attrs.getLength() - 1);

    @Override // ohos.org.xml.sax.Attributes
    public String getType(int i) {
        return "CDATA";
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getType(String str) {
        return "CDATA";
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getType(String str, String str2) {
        return "CDATA";
    }

    public AttList(NamedNodeMap namedNodeMap) {
        this.m_attrs = namedNodeMap;
    }

    @Override // ohos.org.xml.sax.Attributes
    public int getLength() {
        return this.m_attrs.getLength();
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getURI(int i) {
        String namespaceOfNode = DOM2Helper.getNamespaceOfNode((Attr) this.m_attrs.item(i));
        return namespaceOfNode == null ? "" : namespaceOfNode;
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getLocalName(int i) {
        return DOM2Helper.getLocalNameOfNode((Attr) this.m_attrs.item(i));
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getQName(int i) {
        return ((Attr) this.m_attrs.item(i)).getName();
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getValue(int i) {
        return ((Attr) this.m_attrs.item(i)).getValue();
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getValue(String str) {
        Attr attr = (Attr) this.m_attrs.getNamedItem(str);
        if (attr != null) {
            return attr.getValue();
        }
        return null;
    }

    @Override // ohos.org.xml.sax.Attributes
    public String getValue(String str, String str2) {
        Node namedItemNS = this.m_attrs.getNamedItemNS(str, str2);
        if (namedItemNS == null) {
            return null;
        }
        return namedItemNS.getNodeValue();
    }

    @Override // ohos.org.xml.sax.Attributes
    public int getIndex(String str, String str2) {
        for (int length = this.m_attrs.getLength() - 1; length >= 0; length--) {
            Node item = this.m_attrs.item(length);
            String namespaceURI = item.getNamespaceURI();
            if (namespaceURI == null) {
                if (str != null) {
                    continue;
                }
            } else if (!namespaceURI.equals(str)) {
                continue;
            }
            if (item.getLocalName().equals(str2)) {
                return length;
            }
        }
        return -1;
    }

    @Override // ohos.org.xml.sax.Attributes
    public int getIndex(String str) {
        for (int length = this.m_attrs.getLength() - 1; length >= 0; length--) {
            if (this.m_attrs.item(length).getNodeName().equals(str)) {
                return length;
            }
        }
        return -1;
    }
}
