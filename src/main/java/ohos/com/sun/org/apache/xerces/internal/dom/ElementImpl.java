package ohos.com.sun.org.apache.xerces.internal.dom;

import ohos.com.sun.org.apache.xerces.internal.util.URI;
import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Element;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;
import ohos.org.w3c.dom.Text;
import ohos.org.w3c.dom.TypeInfo;

public class ElementImpl extends ParentNode implements Element, TypeInfo {
    static final long serialVersionUID = 3717253516652722278L;
    protected AttributeMap attributes;
    protected String name;

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public short getNodeType() {
        return 1;
    }

    @Override // ohos.org.w3c.dom.TypeInfo
    public String getTypeName() {
        return null;
    }

    @Override // ohos.org.w3c.dom.TypeInfo
    public String getTypeNamespace() {
        return null;
    }

    @Override // ohos.org.w3c.dom.TypeInfo
    public boolean isDerivedFrom(String str, String str2, int i) {
        return false;
    }

    public ElementImpl(CoreDocumentImpl coreDocumentImpl, String str) {
        super(coreDocumentImpl);
        this.name = str;
        needsSyncData(true);
    }

    protected ElementImpl() {
    }

    /* access modifiers changed from: package-private */
    public void rename(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.name = str;
        reconcileDefaultAttributes();
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return this.attributes;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode, ohos.com.sun.org.apache.xerces.internal.dom.ChildNode
    public Node cloneNode(boolean z) {
        ElementImpl elementImpl = (ElementImpl) super.cloneNode(z);
        AttributeMap attributeMap = this.attributes;
        if (attributeMap != null) {
            elementImpl.attributes = (AttributeMap) attributeMap.cloneMap(elementImpl);
        }
        return elementImpl;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getBaseURI() {
        Attr attr;
        if (needsSyncData()) {
            synchronizeData();
        }
        AttributeMap attributeMap = this.attributes;
        if (!(attributeMap == null || (attr = (Attr) attributeMap.getNamedItem("xml:base")) == null)) {
            String nodeValue = attr.getNodeValue();
            if (nodeValue.length() != 0) {
                try {
                    return new URI(nodeValue).toString();
                } catch (URI.MalformedURIException unused) {
                    String baseURI = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
                    if (baseURI != null) {
                        try {
                            return new URI(new URI(baseURI), nodeValue).toString();
                        } catch (URI.MalformedURIException unused2) {
                            return null;
                        }
                    }
                    return null;
                }
            }
        }
        String baseURI2 = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
        if (baseURI2 != null) {
            try {
                return new URI(baseURI2).toString();
            } catch (URI.MalformedURIException unused3) {
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public void setOwnerDocument(CoreDocumentImpl coreDocumentImpl) {
        super.setOwnerDocument(coreDocumentImpl);
        AttributeMap attributeMap = this.attributes;
        if (attributeMap != null) {
            attributeMap.setOwnerDocument(coreDocumentImpl);
        }
    }

    @Override // ohos.org.w3c.dom.Element
    public String getAttribute(String str) {
        Attr attr;
        if (needsSyncData()) {
            synchronizeData();
        }
        AttributeMap attributeMap = this.attributes;
        if (attributeMap == null || (attr = (Attr) attributeMap.getNamedItem(str)) == null) {
            return "";
        }
        return attr.getValue();
    }

    @Override // ohos.org.w3c.dom.Element
    public Attr getAttributeNode(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        AttributeMap attributeMap = this.attributes;
        if (attributeMap == null) {
            return null;
        }
        return (Attr) attributeMap.getNamedItem(str);
    }

    @Override // ohos.org.w3c.dom.Element
    public NodeList getElementsByTagName(String str) {
        return new DeepNodeListImpl(this, str);
    }

    @Override // ohos.org.w3c.dom.Element
    public String getTagName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public void normalize() {
        if (!isNormalized()) {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            ChildNode childNode = this.firstChild;
            while (childNode != null) {
                ChildNode childNode2 = childNode.nextSibling;
                if (childNode.getNodeType() == 3) {
                    if (childNode2 != null && childNode2.getNodeType() == 3) {
                        ((Text) childNode).appendData(childNode2.getNodeValue());
                        removeChild(childNode2);
                    } else if (childNode.getNodeValue() == null || childNode.getNodeValue().length() == 0) {
                        removeChild(childNode);
                    }
                } else if (childNode.getNodeType() == 1) {
                    childNode.normalize();
                }
                childNode = childNode2;
            }
            if (this.attributes != null) {
                for (int i = 0; i < this.attributes.getLength(); i++) {
                    this.attributes.item(i).normalize();
                }
            }
            isNormalized(true);
        }
    }

    @Override // ohos.org.w3c.dom.Element
    public void removeAttribute(String str) {
        if (!this.ownerDocument.errorChecking || !isReadOnly()) {
            if (needsSyncData()) {
                synchronizeData();
            }
            AttributeMap attributeMap = this.attributes;
            if (attributeMap != null) {
                attributeMap.safeRemoveNamedItem(str);
                return;
            }
            return;
        }
        throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Element
    public Attr removeAttributeNode(Attr attr) throws DOMException {
        if (!this.ownerDocument.errorChecking || !isReadOnly()) {
            if (needsSyncData()) {
                synchronizeData();
            }
            AttributeMap attributeMap = this.attributes;
            if (attributeMap != null) {
                return (Attr) attributeMap.removeItem(attr, true);
            }
            throw new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
        }
        throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Element
    public void setAttribute(String str, String str2) {
        if (!this.ownerDocument.errorChecking || !isReadOnly()) {
            if (needsSyncData()) {
                synchronizeData();
            }
            Attr attributeNode = getAttributeNode(str);
            if (attributeNode == null) {
                Attr createAttribute = getOwnerDocument().createAttribute(str);
                if (this.attributes == null) {
                    this.attributes = new AttributeMap(this, null);
                }
                createAttribute.setNodeValue(str2);
                this.attributes.setNamedItem(createAttribute);
                return;
            }
            attributeNode.setNodeValue(str2);
            return;
        }
        throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Element
    public Attr setAttributeNode(Attr attr) throws DOMException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            } else if (attr.getOwnerDocument() != this.ownerDocument) {
                throw new DOMException(4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return (Attr) this.attributes.setNamedItem(attr);
    }

    @Override // ohos.org.w3c.dom.Element
    public String getAttributeNS(String str, String str2) {
        Attr attr;
        if (needsSyncData()) {
            synchronizeData();
        }
        AttributeMap attributeMap = this.attributes;
        if (attributeMap == null || (attr = (Attr) attributeMap.getNamedItemNS(str, str2)) == null) {
            return "";
        }
        return attr.getValue();
    }

    @Override // ohos.org.w3c.dom.Element
    public void setAttributeNS(String str, String str2, String str3) {
        String str4;
        String str5;
        if (!this.ownerDocument.errorChecking || !isReadOnly()) {
            if (needsSyncData()) {
                synchronizeData();
            }
            int indexOf = str2.indexOf(58);
            if (indexOf < 0) {
                str5 = str2;
                str4 = null;
            } else {
                str4 = str2.substring(0, indexOf);
                str5 = str2.substring(indexOf + 1);
            }
            Attr attributeNodeNS = getAttributeNodeNS(str, str5);
            if (attributeNodeNS == null) {
                Attr createAttributeNS = getOwnerDocument().createAttributeNS(str, str2);
                if (this.attributes == null) {
                    this.attributes = new AttributeMap(this, null);
                }
                createAttributeNS.setNodeValue(str3);
                this.attributes.setNamedItemNS(createAttributeNS);
                return;
            }
            if (attributeNodeNS instanceof AttrNSImpl) {
                AttrNSImpl attrNSImpl = (AttrNSImpl) attributeNodeNS;
                String str6 = attrNSImpl.name;
                if (str4 != null) {
                    str5 = str4 + ":" + str5;
                }
                attrNSImpl.name = str5;
                if (!str5.equals(str6)) {
                    attributeNodeNS = (Attr) this.attributes.removeItem(attributeNodeNS, false);
                    this.attributes.addItem(attributeNodeNS);
                }
            } else {
                attributeNodeNS = new AttrNSImpl((CoreDocumentImpl) getOwnerDocument(), str, str2, str5);
                this.attributes.setNamedItemNS(attributeNodeNS);
            }
            attributeNodeNS.setNodeValue(str3);
            return;
        }
        throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Element
    public void removeAttributeNS(String str, String str2) {
        if (!this.ownerDocument.errorChecking || !isReadOnly()) {
            if (needsSyncData()) {
                synchronizeData();
            }
            AttributeMap attributeMap = this.attributes;
            if (attributeMap != null) {
                attributeMap.safeRemoveNamedItemNS(str, str2);
                return;
            }
            return;
        }
        throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Element
    public Attr getAttributeNodeNS(String str, String str2) {
        if (needsSyncData()) {
            synchronizeData();
        }
        AttributeMap attributeMap = this.attributes;
        if (attributeMap == null) {
            return null;
        }
        return (Attr) attributeMap.getNamedItemNS(str, str2);
    }

    @Override // ohos.org.w3c.dom.Element
    public Attr setAttributeNodeNS(Attr attr) throws DOMException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            } else if (attr.getOwnerDocument() != this.ownerDocument) {
                throw new DOMException(4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return (Attr) this.attributes.setNamedItemNS(attr);
    }

    /* access modifiers changed from: protected */
    public int setXercesAttributeNode(Attr attr) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes == null) {
            this.attributes = new AttributeMap(this, null);
        }
        return this.attributes.addItem(attr);
    }

    /* access modifiers changed from: protected */
    public int getXercesAttribute(String str, String str2) {
        if (needsSyncData()) {
            synchronizeData();
        }
        AttributeMap attributeMap = this.attributes;
        if (attributeMap == null) {
            return -1;
        }
        return attributeMap.getNamedItemIndex(str, str2);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public boolean hasAttributes() {
        if (needsSyncData()) {
            synchronizeData();
        }
        AttributeMap attributeMap = this.attributes;
        return (attributeMap == null || attributeMap.getLength() == 0) ? false : true;
    }

    @Override // ohos.org.w3c.dom.Element
    public boolean hasAttribute(String str) {
        return getAttributeNode(str) != null;
    }

    @Override // ohos.org.w3c.dom.Element
    public boolean hasAttributeNS(String str, String str2) {
        return getAttributeNodeNS(str, str2) != null;
    }

    @Override // ohos.org.w3c.dom.Element
    public NodeList getElementsByTagNameNS(String str, String str2) {
        return new DeepNodeListImpl(this, str, str2);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public boolean isEqualNode(Node node) {
        if (!super.isEqualNode(node)) {
            return false;
        }
        boolean hasAttributes = hasAttributes();
        Element element = (Element) node;
        if (hasAttributes != element.hasAttributes()) {
            return false;
        }
        if (!hasAttributes) {
            return true;
        }
        NamedNodeMap attributes2 = getAttributes();
        NamedNodeMap attributes3 = element.getAttributes();
        int length = attributes2.getLength();
        if (length != attributes3.getLength()) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            Node item = attributes2.item(i);
            if (item.getLocalName() == null) {
                Node namedItem = attributes3.getNamedItem(item.getNodeName());
                if (namedItem == null || !((NodeImpl) item).isEqualNode(namedItem)) {
                    return false;
                }
            } else {
                Node namedItemNS = attributes3.getNamedItemNS(item.getNamespaceURI(), item.getLocalName());
                if (namedItemNS == null || !((NodeImpl) item).isEqualNode(namedItemNS)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // ohos.org.w3c.dom.Element
    public void setIdAttributeNode(Attr attr, boolean z) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            } else if (attr.getOwnerElement() != this) {
                throw new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
            }
        }
        ((AttrImpl) attr).isIdAttribute(z);
        if (!z) {
            this.ownerDocument.removeIdentifier(attr.getValue());
        } else {
            this.ownerDocument.putIdentifier(attr.getValue(), this);
        }
    }

    @Override // ohos.org.w3c.dom.Element
    public void setIdAttribute(String str, boolean z) {
        if (needsSyncData()) {
            synchronizeData();
        }
        Attr attributeNode = getAttributeNode(str);
        if (attributeNode != null) {
            if (this.ownerDocument.errorChecking) {
                if (isReadOnly()) {
                    throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
                } else if (attributeNode.getOwnerElement() != this) {
                    throw new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
                }
            }
            ((AttrImpl) attributeNode).isIdAttribute(z);
            if (!z) {
                this.ownerDocument.removeIdentifier(attributeNode.getValue());
            } else {
                this.ownerDocument.putIdentifier(attributeNode.getValue(), this);
            }
        } else {
            throw new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
        }
    }

    @Override // ohos.org.w3c.dom.Element
    public void setIdAttributeNS(String str, String str2, boolean z) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (str != null && str.length() == 0) {
            str = null;
        }
        Attr attributeNodeNS = getAttributeNodeNS(str, str2);
        if (attributeNodeNS != null) {
            if (this.ownerDocument.errorChecking) {
                if (isReadOnly()) {
                    throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
                } else if (attributeNodeNS.getOwnerElement() != this) {
                    throw new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
                }
            }
            ((AttrImpl) attributeNodeNS).isIdAttribute(z);
            if (!z) {
                this.ownerDocument.removeIdentifier(attributeNodeNS.getValue());
            } else {
                this.ownerDocument.putIdentifier(attributeNodeNS.getValue(), this);
            }
        } else {
            throw new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
        }
    }

    @Override // ohos.org.w3c.dom.Element
    public TypeInfo getSchemaTypeInfo() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public void setReadOnly(boolean z, boolean z2) {
        super.setReadOnly(z, z2);
        AttributeMap attributeMap = this.attributes;
        if (attributeMap != null) {
            attributeMap.setReadOnly(z, true);
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl
    public void synchronizeData() {
        needsSyncData(false);
        boolean mutationEvents = this.ownerDocument.getMutationEvents();
        this.ownerDocument.setMutationEvents(false);
        setupDefaultAttributes();
        this.ownerDocument.setMutationEvents(mutationEvents);
    }

    /* access modifiers changed from: package-private */
    public void moveSpecifiedAttributes(ElementImpl elementImpl) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (elementImpl.hasAttributes()) {
            if (this.attributes == null) {
                this.attributes = new AttributeMap(this, null);
            }
            this.attributes.moveSpecifiedAttributes(elementImpl.attributes);
        }
    }

    /* access modifiers changed from: protected */
    public void setupDefaultAttributes() {
        NamedNodeMapImpl defaultAttributes = getDefaultAttributes();
        if (defaultAttributes != null) {
            this.attributes = new AttributeMap(this, defaultAttributes);
        }
    }

    /* access modifiers changed from: protected */
    public void reconcileDefaultAttributes() {
        if (this.attributes != null) {
            this.attributes.reconcileDefaults(getDefaultAttributes());
        }
    }

    /* access modifiers changed from: protected */
    public NamedNodeMapImpl getDefaultAttributes() {
        ElementDefinitionImpl elementDefinitionImpl;
        DocumentTypeImpl documentTypeImpl = (DocumentTypeImpl) this.ownerDocument.getDoctype();
        if (documentTypeImpl == null || (elementDefinitionImpl = (ElementDefinitionImpl) documentTypeImpl.getElements().getNamedItem(getNodeName())) == null) {
            return null;
        }
        return (NamedNodeMapImpl) elementDefinitionImpl.getAttributes();
    }
}
