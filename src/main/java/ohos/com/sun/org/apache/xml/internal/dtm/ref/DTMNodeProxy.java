package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import java.util.Objects;
import java.util.Vector;
import ohos.com.sun.org.apache.xml.internal.dtm.DTM;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMDOMException;
import ohos.com.sun.org.apache.xpath.internal.NodeSet;
import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.CDATASection;
import ohos.org.w3c.dom.Comment;
import ohos.org.w3c.dom.DOMConfiguration;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.DOMImplementation;
import ohos.org.w3c.dom.Document;
import ohos.org.w3c.dom.DocumentFragment;
import ohos.org.w3c.dom.DocumentType;
import ohos.org.w3c.dom.Element;
import ohos.org.w3c.dom.EntityReference;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;
import ohos.org.w3c.dom.ProcessingInstruction;
import ohos.org.w3c.dom.Text;
import ohos.org.w3c.dom.TypeInfo;
import ohos.org.w3c.dom.UserDataHandler;

public class DTMNodeProxy implements Node, Document, Text, Element, Attr, ProcessingInstruction, Comment, DocumentFragment {
    private static final String EMPTYSTRING = "";
    static final DOMImplementation implementation = new DTMNodeProxyImplementation();
    protected String actualEncoding;
    public DTM dtm;
    protected String fDocumentURI;
    int node;
    private String xmlEncoding;
    private boolean xmlStandalone;
    private String xmlVersion;

    @Override // ohos.org.w3c.dom.Node
    public short compareDocumentPosition(Node node2) throws DOMException {
        return 0;
    }

    @Override // ohos.org.w3c.dom.Node
    public String getBaseURI() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public final DocumentType getDoctype() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Element, ohos.org.w3c.dom.Attr
    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Attr
    public final boolean getSpecified() {
        return true;
    }

    @Override // ohos.org.w3c.dom.Text
    public String getWholeText() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isDefaultNamespace(String str) {
        return false;
    }

    @Override // ohos.org.w3c.dom.Text
    public boolean isElementContentWhitespace() {
        return false;
    }

    @Override // ohos.org.w3c.dom.Attr
    public boolean isId() {
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isSameNode(Node node2) {
        return this == node2;
    }

    @Override // ohos.org.w3c.dom.Document
    public void normalizeDocument() {
    }

    @Override // ohos.org.w3c.dom.Document
    public Node renameNode(Node node2, String str, String str2) throws DOMException {
        return node2;
    }

    @Override // ohos.org.w3c.dom.Text
    public Text replaceWholeText(String str) throws DOMException {
        return null;
    }

    @Override // ohos.org.w3c.dom.Element
    public void setIdAttribute(String str, boolean z) {
    }

    public void setIdAttribute(boolean z) {
    }

    @Override // ohos.org.w3c.dom.Element
    public void setIdAttributeNS(String str, String str2, boolean z) {
    }

    @Override // ohos.org.w3c.dom.Element
    public void setIdAttributeNode(Attr attr, boolean z) {
    }

    public DTMNodeProxy(DTM dtm2, int i) {
        this.dtm = dtm2;
        this.node = i;
    }

    public final DTM getDTM() {
        return this.dtm;
    }

    public final int getDTMNodeNumber() {
        return this.node;
    }

    public final boolean equals(Node node2) {
        try {
            DTMNodeProxy dTMNodeProxy = (DTMNodeProxy) node2;
            if (dTMNodeProxy.node == this.node && dTMNodeProxy.dtm == this.dtm) {
                return true;
            }
            return false;
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public final boolean equals(Object obj) {
        return (obj instanceof Node) && equals((Node) obj);
    }

    public int hashCode() {
        return ((203 + Objects.hashCode(this.dtm)) * 29) + this.node;
    }

    public final boolean sameNodeAs(Node node2) {
        if (!(node2 instanceof DTMNodeProxy)) {
            return false;
        }
        DTMNodeProxy dTMNodeProxy = (DTMNodeProxy) node2;
        if (this.dtm == dTMNodeProxy.dtm && this.node == dTMNodeProxy.node) {
            return true;
        }
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public final String getNodeName() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // ohos.org.w3c.dom.ProcessingInstruction
    public final String getTarget() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // ohos.org.w3c.dom.Node
    public final String getLocalName() {
        return this.dtm.getLocalName(this.node);
    }

    @Override // ohos.org.w3c.dom.Node
    public final String getPrefix() {
        return this.dtm.getPrefix(this.node);
    }

    @Override // ohos.org.w3c.dom.Node
    public final void setPrefix(String str) throws DOMException {
        throw new DTMDOMException(7);
    }

    @Override // ohos.org.w3c.dom.Node
    public final String getNamespaceURI() {
        return this.dtm.getNamespaceURI(this.node);
    }

    public final boolean supports(String str, String str2) {
        return implementation.hasFeature(str, str2);
    }

    @Override // ohos.org.w3c.dom.Node
    public final boolean isSupported(String str, String str2) {
        return implementation.hasFeature(str, str2);
    }

    @Override // ohos.org.w3c.dom.Node
    public final String getNodeValue() throws DOMException {
        return this.dtm.getNodeValue(this.node);
    }

    public final String getStringValue() throws DOMException {
        return this.dtm.getStringValue(this.node).toString();
    }

    @Override // ohos.org.w3c.dom.Node
    public final void setNodeValue(String str) throws DOMException {
        throw new DTMDOMException(7);
    }

    @Override // ohos.org.w3c.dom.Node
    public final short getNodeType() {
        return this.dtm.getNodeType(this.node);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node getParentNode() {
        int parent;
        if (getNodeType() == 2 || (parent = this.dtm.getParent(this.node)) == -1) {
            return null;
        }
        return this.dtm.getNode(parent);
    }

    public final Node getOwnerNode() {
        int parent = this.dtm.getParent(this.node);
        if (parent == -1) {
            return null;
        }
        return this.dtm.getNode(parent);
    }

    @Override // ohos.org.w3c.dom.Node
    public final NodeList getChildNodes() {
        return new DTMChildIterNodeList(this.dtm, this.node);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node getFirstChild() {
        int firstChild = this.dtm.getFirstChild(this.node);
        if (firstChild == -1) {
            return null;
        }
        return this.dtm.getNode(firstChild);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node getLastChild() {
        int lastChild = this.dtm.getLastChild(this.node);
        if (lastChild == -1) {
            return null;
        }
        return this.dtm.getNode(lastChild);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node getPreviousSibling() {
        int previousSibling = this.dtm.getPreviousSibling(this.node);
        if (previousSibling == -1) {
            return null;
        }
        return this.dtm.getNode(previousSibling);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node getNextSibling() {
        int nextSibling;
        if (this.dtm.getNodeType(this.node) == 2 || (nextSibling = this.dtm.getNextSibling(this.node)) == -1) {
            return null;
        }
        return this.dtm.getNode(nextSibling);
    }

    @Override // ohos.org.w3c.dom.Node
    public final NamedNodeMap getAttributes() {
        return new DTMNamedNodeMap(this.dtm, this.node);
    }

    @Override // ohos.org.w3c.dom.Element
    public boolean hasAttribute(String str) {
        return -1 != this.dtm.getAttributeNode(this.node, null, str);
    }

    @Override // ohos.org.w3c.dom.Element
    public boolean hasAttributeNS(String str, String str2) {
        return -1 != this.dtm.getAttributeNode(this.node, str, str2);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Document getOwnerDocument() {
        DTM dtm2 = this.dtm;
        return (Document) dtm2.getNode(dtm2.getOwnerDocument(this.node));
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node insertBefore(Node node2, Node node3) throws DOMException {
        throw new DTMDOMException(7);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node replaceChild(Node node2, Node node3) throws DOMException {
        throw new DTMDOMException(7);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node removeChild(Node node2) throws DOMException {
        throw new DTMDOMException(7);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node appendChild(Node node2) throws DOMException {
        throw new DTMDOMException(7);
    }

    @Override // ohos.org.w3c.dom.Node
    public final boolean hasChildNodes() {
        return -1 != this.dtm.getFirstChild(this.node);
    }

    @Override // ohos.org.w3c.dom.Node
    public final Node cloneNode(boolean z) {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final DOMImplementation getImplementation() {
        return implementation;
    }

    @Override // ohos.org.w3c.dom.Document
    public final Element getDocumentElement() {
        int document = this.dtm.getDocument();
        int firstChild = this.dtm.getFirstChild(document);
        int i = -1;
        while (firstChild != -1) {
            short nodeType = this.dtm.getNodeType(firstChild);
            if (nodeType != 1) {
                if (!(nodeType == 10 || nodeType == 7 || nodeType == 8)) {
                    firstChild = this.dtm.getLastChild(document);
                }
                firstChild = this.dtm.getNextSibling(firstChild);
            } else if (i != -1) {
                firstChild = this.dtm.getLastChild(document);
            } else {
                i = firstChild;
                firstChild = this.dtm.getNextSibling(firstChild);
            }
            i = -1;
            firstChild = this.dtm.getNextSibling(firstChild);
        }
        if (i != -1) {
            return (Element) this.dtm.getNode(i);
        }
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final Element createElement(String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final DocumentFragment createDocumentFragment() {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final Text createTextNode(String str) {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final Comment createComment(String str) {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final CDATASection createCDATASection(String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final ProcessingInstruction createProcessingInstruction(String str, String str2) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final Attr createAttribute(String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final EntityReference createEntityReference(String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element, ohos.org.w3c.dom.Document
    public final NodeList getElementsByTagName(String str) {
        Vector vector = new Vector();
        Node node2 = this.dtm.getNode(this.node);
        if (node2 != null) {
            boolean equals = "*".equals(str);
            if (1 == node2.getNodeType()) {
                NodeList childNodes = node2.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    traverseChildren(vector, childNodes.item(i), str, equals);
                }
            } else if (9 == node2.getNodeType()) {
                traverseChildren(vector, this.dtm.getNode(this.node), str, equals);
            }
        }
        int size = vector.size();
        NodeSet nodeSet = new NodeSet(size);
        for (int i2 = 0; i2 < size; i2++) {
            nodeSet.addNode((Node) vector.elementAt(i2));
        }
        return nodeSet;
    }

    private final void traverseChildren(Vector vector, Node node2, String str, boolean z) {
        if (node2 != null) {
            if (node2.getNodeType() == 1 && (z || node2.getNodeName().equals(str))) {
                vector.add(node2);
            }
            if (node2.hasChildNodes()) {
                NodeList childNodes = node2.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    traverseChildren(vector, childNodes.item(i), str, z);
                }
            }
        }
    }

    @Override // ohos.org.w3c.dom.Document
    public final Node importNode(Node node2, boolean z) throws DOMException {
        throw new DTMDOMException(7);
    }

    @Override // ohos.org.w3c.dom.Document
    public final Element createElementNS(String str, String str2) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public final Attr createAttributeNS(String str, String str2) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element, ohos.org.w3c.dom.Document
    public final NodeList getElementsByTagNameNS(String str, String str2) {
        Vector vector = new Vector();
        Node node2 = this.dtm.getNode(this.node);
        if (node2 != null) {
            boolean equals = "*".equals(str);
            boolean equals2 = "*".equals(str2);
            if (1 == node2.getNodeType()) {
                NodeList childNodes = node2.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    traverseChildren(vector, childNodes.item(i), str, str2, equals, equals2);
                }
            } else if (9 == node2.getNodeType()) {
                traverseChildren(vector, this.dtm.getNode(this.node), str, str2, equals, equals2);
            }
        }
        int size = vector.size();
        NodeSet nodeSet = new NodeSet(size);
        for (int i2 = 0; i2 < size; i2++) {
            nodeSet.addNode((Node) vector.elementAt(i2));
        }
        return nodeSet;
    }

    private final void traverseChildren(Vector vector, Node node2, String str, String str2, boolean z, boolean z2) {
        if (node2 != null) {
            if (node2.getNodeType() == 1 && (z2 || node2.getLocalName().equals(str2))) {
                String namespaceURI = node2.getNamespaceURI();
                if ((str == null && namespaceURI == null) || z || (str != null && str.equals(namespaceURI))) {
                    vector.add(node2);
                }
            }
            if (node2.hasChildNodes()) {
                NodeList childNodes = node2.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    traverseChildren(vector, childNodes.item(i), str, str2, z, z2);
                }
            }
        }
    }

    @Override // ohos.org.w3c.dom.Document
    public final Element getElementById(String str) {
        DTM dtm2 = this.dtm;
        return (Element) dtm2.getNode(dtm2.getElementById(str));
    }

    @Override // ohos.org.w3c.dom.Text
    public final Text splitText(int i) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.CharacterData, ohos.org.w3c.dom.ProcessingInstruction
    public final String getData() throws DOMException {
        return this.dtm.getNodeValue(this.node);
    }

    @Override // ohos.org.w3c.dom.CharacterData, ohos.org.w3c.dom.ProcessingInstruction
    public final void setData(String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public final int getLength() {
        return this.dtm.getNodeValue(this.node).length();
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public final String substringData(int i, int i2) throws DOMException {
        return getData().substring(i, i2 + i);
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public final void appendData(String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public final void insertData(int i, String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public final void deleteData(int i, int i2) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public final void replaceData(int i, int i2, String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element
    public final String getTagName() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // ohos.org.w3c.dom.Element
    public final String getAttribute(String str) {
        Node namedItem = new DTMNamedNodeMap(this.dtm, this.node).getNamedItem(str);
        if (namedItem == null) {
            return "";
        }
        return namedItem.getNodeValue();
    }

    @Override // ohos.org.w3c.dom.Element
    public final void setAttribute(String str, String str2) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element
    public final void removeAttribute(String str) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element
    public final Attr getAttributeNode(String str) {
        return (Attr) new DTMNamedNodeMap(this.dtm, this.node).getNamedItem(str);
    }

    @Override // ohos.org.w3c.dom.Element
    public final Attr setAttributeNode(Attr attr) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element
    public final Attr removeAttributeNode(Attr attr) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean hasAttributes() {
        return -1 != this.dtm.getFirstAttribute(this.node);
    }

    @Override // ohos.org.w3c.dom.Node
    public final void normalize() {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element
    public final String getAttributeNS(String str, String str2) {
        int attributeNode = this.dtm.getAttributeNode(this.node, str, str2);
        Node node2 = attributeNode != -1 ? this.dtm.getNode(attributeNode) : null;
        if (node2 == null) {
            return "";
        }
        return node2.getNodeValue();
    }

    @Override // ohos.org.w3c.dom.Element
    public final void setAttributeNS(String str, String str2, String str3) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element
    public final void removeAttributeNS(String str, String str2) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Element
    public final Attr getAttributeNodeNS(String str, String str2) {
        int attributeNode = this.dtm.getAttributeNode(this.node, str, str2);
        if (attributeNode != -1) {
            return (Attr) this.dtm.getNode(attributeNode);
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.Element
    public final Attr setAttributeNodeNS(Attr attr) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Attr
    public final String getName() {
        return this.dtm.getNodeName(this.node);
    }

    @Override // ohos.org.w3c.dom.Attr
    public final String getValue() {
        return this.dtm.getNodeValue(this.node);
    }

    @Override // ohos.org.w3c.dom.Attr
    public final void setValue(String str) {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Attr
    public final Element getOwnerElement() {
        int parent;
        if (getNodeType() == 2 && (parent = this.dtm.getParent(this.node)) != -1) {
            return (Element) this.dtm.getNode(parent);
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public Node adoptNode(Node node2) throws DOMException {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public String getInputEncoding() {
        throw new DTMDOMException(9);
    }

    public void setEncoding(String str) {
        throw new DTMDOMException(9);
    }

    public boolean getStandalone() {
        throw new DTMDOMException(9);
    }

    public void setStandalone(boolean z) {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        throw new DTMDOMException(9);
    }

    @Override // ohos.org.w3c.dom.Document
    public void setStrictErrorChecking(boolean z) {
        throw new DTMDOMException(9);
    }

    public String getVersion() {
        throw new DTMDOMException(9);
    }

    public void setVersion(String str) {
        throw new DTMDOMException(9);
    }

    static class DTMNodeProxyImplementation implements DOMImplementation {
        @Override // ohos.org.w3c.dom.DOMImplementation
        public Object getFeature(String str, String str2) {
            return null;
        }

        DTMNodeProxyImplementation() {
        }

        @Override // ohos.org.w3c.dom.DOMImplementation
        public DocumentType createDocumentType(String str, String str2, String str3) {
            throw new DTMDOMException(9);
        }

        @Override // ohos.org.w3c.dom.DOMImplementation
        public Document createDocument(String str, String str2, DocumentType documentType) {
            throw new DTMDOMException(9);
        }

        @Override // ohos.org.w3c.dom.DOMImplementation
        public boolean hasFeature(String str, String str2) {
            if ("CORE".equals(str.toUpperCase()) || "XML".equals(str.toUpperCase())) {
                return "1.0".equals(str2) || "2.0".equals(str2);
            }
            return false;
        }
    }

    @Override // ohos.org.w3c.dom.Node
    public Object setUserData(String str, Object obj, UserDataHandler userDataHandler) {
        return getOwnerDocument().setUserData(str, obj, userDataHandler);
    }

    @Override // ohos.org.w3c.dom.Node
    public Object getUserData(String str) {
        return getOwnerDocument().getUserData(str);
    }

    @Override // ohos.org.w3c.dom.Node
    public Object getFeature(String str, String str2) {
        if (isSupported(str, str2)) {
            return this;
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isEqualNode(Node node2) {
        if (node2 == this) {
            return true;
        }
        if (node2.getNodeType() != getNodeType()) {
            return false;
        }
        if (getNodeName() == null) {
            if (node2.getNodeName() != null) {
                return false;
            }
        } else if (!getNodeName().equals(node2.getNodeName())) {
            return false;
        }
        if (getLocalName() == null) {
            if (node2.getLocalName() != null) {
                return false;
            }
        } else if (!getLocalName().equals(node2.getLocalName())) {
            return false;
        }
        if (getNamespaceURI() == null) {
            if (node2.getNamespaceURI() != null) {
                return false;
            }
        } else if (!getNamespaceURI().equals(node2.getNamespaceURI())) {
            return false;
        }
        if (getPrefix() == null) {
            if (node2.getPrefix() != null) {
                return false;
            }
        } else if (!getPrefix().equals(node2.getPrefix())) {
            return false;
        }
        if (getNodeValue() == null) {
            if (node2.getNodeValue() != null) {
                return false;
            }
        } else if (!getNodeValue().equals(node2.getNodeValue())) {
            return false;
        }
        return true;
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
            return null;
        } else if (nodeType != 2) {
            if (nodeType != 6) {
                switch (nodeType) {
                }
            }
            return null;
        } else if (getOwnerElement().getNodeType() == 1) {
            return getOwnerElement().lookupNamespaceURI(str);
        } else {
            return null;
        }
    }

    @Override // ohos.org.w3c.dom.Node
    public String lookupPrefix(String str) {
        if (str == null) {
            return null;
        }
        short nodeType = getNodeType();
        if (nodeType != 2) {
            if (nodeType != 6) {
                switch (nodeType) {
                }
            }
            return null;
        } else if (getOwnerElement().getNodeType() == 1) {
            return getOwnerElement().lookupPrefix(str);
        } else {
            return null;
        }
    }

    @Override // ohos.org.w3c.dom.Node
    public void setTextContent(String str) throws DOMException {
        setNodeValue(str);
    }

    @Override // ohos.org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return this.dtm.getStringValue(this.node).toString();
    }

    @Override // ohos.org.w3c.dom.Document
    public void setDocumentURI(String str) {
        this.fDocumentURI = str;
    }

    @Override // ohos.org.w3c.dom.Document
    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    public String getActualEncoding() {
        return this.actualEncoding;
    }

    public void setActualEncoding(String str) {
        this.actualEncoding = str;
    }

    @Override // ohos.org.w3c.dom.Document
    public String getXmlEncoding() {
        return this.xmlEncoding;
    }

    public void setXmlEncoding(String str) {
        this.xmlEncoding = str;
    }

    @Override // ohos.org.w3c.dom.Document
    public boolean getXmlStandalone() {
        return this.xmlStandalone;
    }

    @Override // ohos.org.w3c.dom.Document
    public void setXmlStandalone(boolean z) throws DOMException {
        this.xmlStandalone = z;
    }

    @Override // ohos.org.w3c.dom.Document
    public String getXmlVersion() {
        return this.xmlVersion;
    }

    @Override // ohos.org.w3c.dom.Document
    public void setXmlVersion(String str) throws DOMException {
        this.xmlVersion = str;
    }
}
