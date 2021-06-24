package ohos.com.sun.org.apache.xerces.internal.dom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import ohos.com.sun.org.apache.xerces.internal.dom.ParentNode;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Document;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;
import ohos.org.w3c.dom.UserDataHandler;
import ohos.org.w3c.dom.events.Event;
import ohos.org.w3c.dom.events.EventListener;
import ohos.org.w3c.dom.events.EventTarget;

public abstract class NodeImpl implements Node, NodeList, EventTarget, Cloneable, Serializable {
    public static final short DOCUMENT_POSITION_CONTAINS = 8;
    public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
    public static final short DOCUMENT_POSITION_FOLLOWING = 4;
    public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
    public static final short DOCUMENT_POSITION_IS_CONTAINED = 16;
    public static final short DOCUMENT_POSITION_PRECEDING = 2;
    public static final short ELEMENT_DEFINITION_NODE = 21;
    protected static final short FIRSTCHILD = 16;
    protected static final short HASSTRING = 128;
    protected static final short ID = 512;
    protected static final short IGNORABLEWS = 64;
    protected static final short NORMALIZED = 256;
    protected static final short OWNED = 8;
    protected static final short READONLY = 1;
    protected static final short SPECIFIED = 32;
    protected static final short SYNCCHILDREN = 4;
    protected static final short SYNCDATA = 2;
    public static final short TREE_POSITION_ANCESTOR = 4;
    public static final short TREE_POSITION_DESCENDANT = 8;
    public static final short TREE_POSITION_DISCONNECTED = 0;
    public static final short TREE_POSITION_EQUIVALENT = 16;
    public static final short TREE_POSITION_FOLLOWING = 2;
    public static final short TREE_POSITION_PRECEDING = 1;
    public static final short TREE_POSITION_SAME_NODE = 32;
    static final long serialVersionUID = -6316591992167219696L;
    protected short flags;
    protected NodeImpl ownerNode;

    @Override // ohos.org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public String getBaseURI() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public NodeList getChildNodes() {
        return this;
    }

    /* access modifiers changed from: protected */
    public Node getContainer() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getFirstChild() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getLastChild() {
        return null;
    }

    @Override // ohos.org.w3c.dom.NodeList
    public int getLength() {
        return 0;
    }

    @Override // ohos.org.w3c.dom.Node
    public String getLocalName() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public String getNamespaceURI() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getNextSibling() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public abstract String getNodeName();

    @Override // ohos.org.w3c.dom.Node
    public abstract short getNodeType();

    @Override // ohos.org.w3c.dom.Node
    public String getNodeValue() throws DOMException {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getParentNode() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public String getPrefix() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getPreviousSibling() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean hasAttributes() {
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean hasChildNodes() {
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isSameNode(Node node) {
        return this == node;
    }

    @Override // ohos.org.w3c.dom.NodeList
    public Node item(int i) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public void normalize() {
    }

    /* access modifiers changed from: package-private */
    public NodeImpl parentNode() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public ChildNode previousSibling() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public void setNodeValue(String str) throws DOMException {
    }

    protected NodeImpl(CoreDocumentImpl coreDocumentImpl) {
        this.ownerNode = coreDocumentImpl;
    }

    public NodeImpl() {
    }

    @Override // ohos.org.w3c.dom.Node
    public Node appendChild(Node node) throws DOMException {
        return insertBefore(node, null);
    }

    @Override // ohos.org.w3c.dom.Node
    public Node cloneNode(boolean z) {
        if (needsSyncData()) {
            synchronizeData();
        }
        try {
            NodeImpl nodeImpl = (NodeImpl) clone();
            nodeImpl.ownerNode = ownerDocument();
            nodeImpl.isOwned(false);
            nodeImpl.isReadOnly(false);
            ownerDocument().callUserDataHandlers(this, nodeImpl, 1);
            return nodeImpl;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("**Internal Error**" + e);
        }
    }

    @Override // ohos.org.w3c.dom.Node
    public Document getOwnerDocument() {
        if (isOwned()) {
            return this.ownerNode.ownerDocument();
        }
        return (Document) this.ownerNode;
    }

    /* access modifiers changed from: package-private */
    public CoreDocumentImpl ownerDocument() {
        if (isOwned()) {
            return this.ownerNode.ownerDocument();
        }
        return (CoreDocumentImpl) this.ownerNode;
    }

    /* access modifiers changed from: package-private */
    public void setOwnerDocument(CoreDocumentImpl coreDocumentImpl) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (!isOwned()) {
            this.ownerNode = coreDocumentImpl;
        }
    }

    /* access modifiers changed from: protected */
    public int getNodeNumber() {
        return ((CoreDocumentImpl) getOwnerDocument()).getNodeNumber(this);
    }

    @Override // ohos.org.w3c.dom.Node
    public Node insertBefore(Node node, Node node2) throws DOMException {
        throw new DOMException(3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Node
    public Node removeChild(Node node) throws DOMException {
        throw new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_FOUND_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Node
    public Node replaceChild(Node node, Node node2) throws DOMException {
        throw new DOMException(3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isSupported(String str, String str2) {
        return ownerDocument().getImplementation().hasFeature(str, str2);
    }

    @Override // ohos.org.w3c.dom.Node
    public void setPrefix(String str) throws DOMException {
        throw new DOMException(14, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null));
    }

    @Override // ohos.org.w3c.dom.events.EventTarget
    public void addEventListener(String str, EventListener eventListener, boolean z) {
        ownerDocument().addEventListener(this, str, eventListener, z);
    }

    @Override // ohos.org.w3c.dom.events.EventTarget
    public void removeEventListener(String str, EventListener eventListener, boolean z) {
        ownerDocument().removeEventListener(this, str, eventListener, z);
    }

    @Override // ohos.org.w3c.dom.events.EventTarget
    public boolean dispatchEvent(Event event) {
        return ownerDocument().dispatchEvent(this, event);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v16 */
    public short compareTreePosition(Node node) {
        Node node2;
        Node node3;
        if (this == node) {
            return 48;
        }
        short nodeType = getNodeType();
        short nodeType2 = node.getNodeType();
        if (!(nodeType == 6 || nodeType == 12 || nodeType2 == 6 || nodeType2 == 12)) {
            Node node4 = this;
            Node node5 = node4;
            int i = 0;
            for (Node node6 = node4; node6 != null; node6 = node6.getParentNode()) {
                i++;
                if (node6 == node) {
                    return 5;
                }
                node5 = node6;
            }
            Node node7 = node;
            Node node8 = node7;
            int i2 = 0;
            while (node7 != null) {
                i2++;
                if (node7 == this) {
                    return 10;
                }
                node8 = node7;
                node7 = node7.getParentNode();
            }
            short nodeType3 = node5.getNodeType();
            short nodeType4 = node8.getNodeType();
            ?? r11 = this;
            if (nodeType3 == 2) {
                r11 = ((AttrImpl) node5).getOwnerElement();
            }
            if (nodeType4 == 2) {
                node = ((AttrImpl) node8).getOwnerElement();
            }
            if (nodeType3 == 2 && nodeType4 == 2 && r11 == node) {
                return 16;
            }
            if (nodeType3 == 2) {
                i = 0;
                for (Node node9 = r11; node9 != null; node9 = node9.getParentNode()) {
                    i++;
                    if (node9 == node) {
                        return 1;
                    }
                    node5 = node9;
                }
            }
            if (nodeType4 == 2) {
                i2 = 0;
                for (Node node10 = node; node10 != null; node10 = node10.getParentNode()) {
                    i2++;
                    if (node10 == r11) {
                        return 2;
                    }
                    node8 = node10;
                }
            }
            if (node5 != node8) {
                return 0;
            }
            if (i > i2) {
                Node node11 = r11;
                for (int i3 = 0; i3 < i - i2; i3++) {
                    node11 = node11.getParentNode();
                }
                if (node11 == node) {
                    return 1;
                }
                node2 = node11;
            } else {
                Node node12 = node;
                for (int i4 = 0; i4 < i2 - i; i4++) {
                    node12 = node12.getParentNode();
                }
                if (node12 == r11) {
                    return 2;
                }
                node = node12;
                node2 = r11;
            }
            Node parentNode = node2.getParentNode();
            Node parentNode2 = node.getParentNode();
            Node node13 = node2;
            while (true) {
                node3 = parentNode;
                node = parentNode2;
                if (node3 == node) {
                    break;
                }
                parentNode = node3.getParentNode();
                parentNode2 = node.getParentNode();
                node13 = node3;
            }
            for (Node firstChild = node3.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                if (firstChild == node) {
                    return 1;
                }
                if (firstChild == node13) {
                    return 2;
                }
            }
        }
        return 0;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:141:0x0139 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:118:0x016c */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3, types: [ohos.org.w3c.dom.Document] */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v10, types: [ohos.org.w3c.dom.Element] */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0087, code lost:
        if (r7 != 12) goto L_0x0092;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0105, code lost:
        if (r14 != 12) goto L_0x0115;
     */
    @Override // ohos.org.w3c.dom.Node
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public short compareDocumentPosition(ohos.org.w3c.dom.Node r21) throws ohos.org.w3c.dom.DOMException {
        /*
        // Method dump skipped, instructions count: 430
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl.compareDocumentPosition(ohos.org.w3c.dom.Node):short");
    }

    @Override // ohos.org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return getNodeValue();
    }

    /* access modifiers changed from: package-private */
    public void getTextContent(StringBuffer stringBuffer) throws DOMException {
        String nodeValue = getNodeValue();
        if (nodeValue != null) {
            stringBuffer.append(nodeValue);
        }
    }

    @Override // ohos.org.w3c.dom.Node
    public void setTextContent(String str) throws DOMException {
        setNodeValue(str);
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isDefaultNamespace(String str) {
        NodeImpl nodeImpl;
        short nodeType = getNodeType();
        if (nodeType == 1) {
            String namespaceURI = getNamespaceURI();
            String prefix = getPrefix();
            if (prefix == null || prefix.length() == 0) {
                if (str == null) {
                    return namespaceURI == str;
                }
                return str.equals(namespaceURI);
            } else if (!hasAttributes() || (nodeImpl = (NodeImpl) ((ElementImpl) this).getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns")) == null) {
                NodeImpl nodeImpl2 = (NodeImpl) getElementAncestor(this);
                if (nodeImpl2 != null) {
                    return nodeImpl2.isDefaultNamespace(str);
                }
                return false;
            } else {
                String nodeValue = nodeImpl.getNodeValue();
                if (str == null) {
                    return namespaceURI == nodeValue;
                }
                return str.equals(nodeValue);
            }
        } else if (nodeType != 2) {
            if (nodeType != 6) {
                switch (nodeType) {
                    case 9:
                        return ((NodeImpl) ((Document) this).getDocumentElement()).isDefaultNamespace(str);
                    case 10:
                    case 11:
                    case 12:
                        break;
                    default:
                        NodeImpl nodeImpl3 = (NodeImpl) getElementAncestor(this);
                        if (nodeImpl3 != null) {
                            return nodeImpl3.isDefaultNamespace(str);
                        }
                        return false;
                }
            }
            return false;
        } else if (this.ownerNode.getNodeType() == 1) {
            return this.ownerNode.isDefaultNamespace(str);
        } else {
            return false;
        }
    }

    @Override // ohos.org.w3c.dom.Node
    public String lookupPrefix(String str) {
        if (str == null) {
            return null;
        }
        short nodeType = getNodeType();
        if (nodeType == 1) {
            getNamespaceURI();
            return lookupNamespacePrefix(str, (ElementImpl) this);
        } else if (nodeType != 2) {
            if (nodeType != 6) {
                switch (nodeType) {
                    case 9:
                        return ((NodeImpl) ((Document) this).getDocumentElement()).lookupPrefix(str);
                    case 10:
                    case 11:
                    case 12:
                        break;
                    default:
                        NodeImpl nodeImpl = (NodeImpl) getElementAncestor(this);
                        if (nodeImpl != null) {
                            return nodeImpl.lookupPrefix(str);
                        }
                        return null;
                }
            }
            return null;
        } else if (this.ownerNode.getNodeType() == 1) {
            return this.ownerNode.lookupPrefix(str);
        } else {
            return null;
        }
    }

    @Override // ohos.org.w3c.dom.Node
    public String lookupNamespaceURI(String str) {
        short nodeType = getNodeType();
        if (nodeType == 1) {
            String namespaceURI = getNamespaceURI();
            String prefix = getPrefix();
            if (namespaceURI != null) {
                if (str == null && prefix == str) {
                    return namespaceURI;
                }
                if (prefix != null && prefix.equals(str)) {
                    return namespaceURI;
                }
            }
            if (hasAttributes()) {
                NamedNodeMap attributes = getAttributes();
                int length = attributes.getLength();
                for (int i = 0; i < length; i++) {
                    Node item = attributes.item(i);
                    String prefix2 = item.getPrefix();
                    String nodeValue = item.getNodeValue();
                    String namespaceURI2 = item.getNamespaceURI();
                    if (namespaceURI2 != null && namespaceURI2.equals("http://www.w3.org/2000/xmlns/")) {
                        if (str == null && item.getNodeName().equals("xmlns")) {
                            return nodeValue;
                        }
                        if (prefix2 != null && prefix2.equals("xmlns") && item.getLocalName().equals(str)) {
                            return nodeValue;
                        }
                    }
                }
            }
            NodeImpl nodeImpl = (NodeImpl) getElementAncestor(this);
            if (nodeImpl != null) {
                return nodeImpl.lookupNamespaceURI(str);
            }
            return null;
        } else if (nodeType != 2) {
            if (nodeType != 6) {
                switch (nodeType) {
                    case 9:
                        return ((NodeImpl) ((Document) this).getDocumentElement()).lookupNamespaceURI(str);
                    case 10:
                    case 11:
                    case 12:
                        break;
                    default:
                        NodeImpl nodeImpl2 = (NodeImpl) getElementAncestor(this);
                        if (nodeImpl2 != null) {
                            return nodeImpl2.lookupNamespaceURI(str);
                        }
                        return null;
                }
            }
            return null;
        } else if (this.ownerNode.getNodeType() == 1) {
            return this.ownerNode.lookupNamespaceURI(str);
        } else {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public Node getElementAncestor(Node node) {
        Node parentNode = node.getParentNode();
        if (parentNode == null) {
            return null;
        }
        if (parentNode.getNodeType() == 1) {
            return parentNode;
        }
        return getElementAncestor(parentNode);
    }

    /* access modifiers changed from: package-private */
    public String lookupNamespacePrefix(String str, ElementImpl elementImpl) {
        String localName;
        String lookupNamespaceURI;
        String lookupNamespaceURI2;
        String namespaceURI = getNamespaceURI();
        String prefix = getPrefix();
        if (!(namespaceURI == null || !namespaceURI.equals(str) || prefix == null || (lookupNamespaceURI2 = elementImpl.lookupNamespaceURI(prefix)) == null || !lookupNamespaceURI2.equals(str))) {
            return prefix;
        }
        if (hasAttributes()) {
            NamedNodeMap attributes = getAttributes();
            int length = attributes.getLength();
            for (int i = 0; i < length; i++) {
                Node item = attributes.item(i);
                String prefix2 = item.getPrefix();
                String nodeValue = item.getNodeValue();
                String namespaceURI2 = item.getNamespaceURI();
                if (namespaceURI2 != null && namespaceURI2.equals("http://www.w3.org/2000/xmlns/") && ((item.getNodeName().equals("xmlns") || (prefix2 != null && prefix2.equals("xmlns") && nodeValue.equals(str))) && (lookupNamespaceURI = elementImpl.lookupNamespaceURI((localName = item.getLocalName()))) != null && lookupNamespaceURI.equals(str))) {
                    return localName;
                }
            }
        }
        NodeImpl nodeImpl = (NodeImpl) getElementAncestor(this);
        if (nodeImpl != null) {
            return nodeImpl.lookupNamespacePrefix(str, elementImpl);
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isEqualNode(Node node) {
        if (node == this) {
            return true;
        }
        if (node.getNodeType() != getNodeType()) {
            return false;
        }
        if (getNodeName() == null) {
            if (node.getNodeName() != null) {
                return false;
            }
        } else if (!getNodeName().equals(node.getNodeName())) {
            return false;
        }
        if (getLocalName() == null) {
            if (node.getLocalName() != null) {
                return false;
            }
        } else if (!getLocalName().equals(node.getLocalName())) {
            return false;
        }
        if (getNamespaceURI() == null) {
            if (node.getNamespaceURI() != null) {
                return false;
            }
        } else if (!getNamespaceURI().equals(node.getNamespaceURI())) {
            return false;
        }
        if (getPrefix() == null) {
            if (node.getPrefix() != null) {
                return false;
            }
        } else if (!getPrefix().equals(node.getPrefix())) {
            return false;
        }
        if (getNodeValue() == null) {
            if (node.getNodeValue() != null) {
                return false;
            }
        } else if (!getNodeValue().equals(node.getNodeValue())) {
            return false;
        }
        return true;
    }

    @Override // ohos.org.w3c.dom.Node
    public Object getFeature(String str, String str2) {
        if (isSupported(str, str2)) {
            return this;
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Object setUserData(String str, Object obj, UserDataHandler userDataHandler) {
        return ownerDocument().setUserData(this, str, obj, userDataHandler);
    }

    @Override // ohos.org.w3c.dom.Node
    public Object getUserData(String str) {
        return ownerDocument().getUserData(this, str);
    }

    /* access modifiers changed from: protected */
    public Map<String, ParentNode.UserDataRecord> getUserDataRecord() {
        return ownerDocument().getUserDataRecord(this);
    }

    public void setReadOnly(boolean z, boolean z2) {
        if (needsSyncData()) {
            synchronizeData();
        }
        isReadOnly(z);
    }

    public boolean getReadOnly() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return isReadOnly();
    }

    public void setUserData(Object obj) {
        ownerDocument().setUserData(this, obj);
    }

    public Object getUserData() {
        return ownerDocument().getUserData(this);
    }

    /* access modifiers changed from: protected */
    public void changed() {
        ownerDocument().changed();
    }

    /* access modifiers changed from: protected */
    public int changes() {
        return ownerDocument().changes();
    }

    /* access modifiers changed from: protected */
    public void synchronizeData() {
        needsSyncData(false);
    }

    /* access modifiers changed from: package-private */
    public final boolean isReadOnly() {
        return (this.flags & 1) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void isReadOnly(boolean z) {
        this.flags = (short) (z ? this.flags | 1 : this.flags & -2);
    }

    /* access modifiers changed from: package-private */
    public final boolean needsSyncData() {
        return (this.flags & 2) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void needsSyncData(boolean z) {
        this.flags = (short) (z ? this.flags | 2 : this.flags & -3);
    }

    /* access modifiers changed from: package-private */
    public final boolean needsSyncChildren() {
        return (this.flags & 4) != 0;
    }

    public final void needsSyncChildren(boolean z) {
        this.flags = (short) (z ? this.flags | 4 : this.flags & -5);
    }

    /* access modifiers changed from: package-private */
    public final boolean isOwned() {
        return (this.flags & 8) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void isOwned(boolean z) {
        this.flags = (short) (z ? this.flags | 8 : this.flags & -9);
    }

    /* access modifiers changed from: package-private */
    public final boolean isFirstChild() {
        return (this.flags & 16) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void isFirstChild(boolean z) {
        this.flags = (short) (z ? this.flags | 16 : this.flags & -17);
    }

    /* access modifiers changed from: package-private */
    public final boolean isSpecified() {
        return (this.flags & 32) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void isSpecified(boolean z) {
        this.flags = (short) (z ? this.flags | 32 : this.flags & -33);
    }

    /* access modifiers changed from: package-private */
    public final boolean internalIsIgnorableWhitespace() {
        return (this.flags & 64) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void isIgnorableWhitespace(boolean z) {
        this.flags = (short) (z ? this.flags | 64 : this.flags & -65);
    }

    /* access modifiers changed from: package-private */
    public final boolean hasStringValue() {
        return (this.flags & 128) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void hasStringValue(boolean z) {
        this.flags = (short) (z ? this.flags | 128 : this.flags & -129);
    }

    /* access modifiers changed from: package-private */
    public final boolean isNormalized() {
        return (this.flags & 256) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void isNormalized(boolean z) {
        NodeImpl nodeImpl;
        if (!z && isNormalized() && (nodeImpl = this.ownerNode) != null) {
            nodeImpl.isNormalized(false);
        }
        this.flags = (short) (z ? this.flags | 256 : this.flags & -257);
    }

    /* access modifiers changed from: package-private */
    public final boolean isIdAttribute() {
        return (this.flags & 512) != 0;
    }

    /* access modifiers changed from: package-private */
    public final void isIdAttribute(boolean z) {
        this.flags = (short) (z ? this.flags | 512 : this.flags & -513);
    }

    public String toString() {
        return "[" + getNodeName() + ": " + getNodeValue() + "]";
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (needsSyncData()) {
            synchronizeData();
        }
        objectOutputStream.defaultWriteObject();
    }
}
