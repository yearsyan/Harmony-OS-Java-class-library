package ohos.com.sun.org.apache.xpath.internal.domapi;

import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Document;
import ohos.org.w3c.dom.Element;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;
import ohos.org.w3c.dom.UserDataHandler;
import ohos.org.w3c.dom.xpath.XPathNamespace;

class XPathNamespaceImpl implements XPathNamespace {
    private final Node m_attributeNode;
    private String textContent;

    @Override // ohos.org.w3c.dom.Node
    public Node appendChild(Node node) throws DOMException {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public short compareDocumentPosition(Node node) throws DOMException {
        return 0;
    }

    @Override // ohos.org.w3c.dom.Node
    public String getBaseURI() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Object getFeature(String str, String str2) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public String getNodeName() {
        return "#namespace";
    }

    @Override // ohos.org.w3c.dom.Node
    public short getNodeType() {
        return 13;
    }

    @Override // ohos.org.w3c.dom.Node
    public Object getUserData(String str) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean hasChildNodes() {
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public Node insertBefore(Node node, Node node2) throws DOMException {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isDefaultNamespace(String str) {
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isEqualNode(Node node) {
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isSameNode(Node node) {
        return false;
    }

    @Override // ohos.org.w3c.dom.Node
    public String lookupNamespaceURI(String str) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public String lookupPrefix(String str) {
        return "";
    }

    @Override // ohos.org.w3c.dom.Node
    public Node removeChild(Node node) throws DOMException {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public Node replaceChild(Node node, Node node2) throws DOMException {
        return null;
    }

    @Override // ohos.org.w3c.dom.Node
    public void setNodeValue(String str) throws DOMException {
    }

    @Override // ohos.org.w3c.dom.Node
    public void setPrefix(String str) throws DOMException {
    }

    @Override // ohos.org.w3c.dom.Node
    public Object setUserData(String str, Object obj, UserDataHandler userDataHandler) {
        return null;
    }

    XPathNamespaceImpl(Node node) {
        this.m_attributeNode = node;
    }

    @Override // ohos.org.w3c.dom.xpath.XPathNamespace
    public Element getOwnerElement() {
        return ((Attr) this.m_attributeNode).getOwnerElement();
    }

    @Override // ohos.org.w3c.dom.Node
    public String getNodeValue() throws DOMException {
        return this.m_attributeNode.getNodeValue();
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getParentNode() {
        return this.m_attributeNode.getParentNode();
    }

    @Override // ohos.org.w3c.dom.Node
    public NodeList getChildNodes() {
        return this.m_attributeNode.getChildNodes();
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getFirstChild() {
        return this.m_attributeNode.getFirstChild();
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getLastChild() {
        return this.m_attributeNode.getLastChild();
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getPreviousSibling() {
        return this.m_attributeNode.getPreviousSibling();
    }

    @Override // ohos.org.w3c.dom.Node
    public Node getNextSibling() {
        return this.m_attributeNode.getNextSibling();
    }

    @Override // ohos.org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return this.m_attributeNode.getAttributes();
    }

    @Override // ohos.org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.m_attributeNode.getOwnerDocument();
    }

    @Override // ohos.org.w3c.dom.Node
    public Node cloneNode(boolean z) {
        throw new DOMException(9, null);
    }

    @Override // ohos.org.w3c.dom.Node
    public void normalize() {
        this.m_attributeNode.normalize();
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean isSupported(String str, String str2) {
        return this.m_attributeNode.isSupported(str, str2);
    }

    @Override // ohos.org.w3c.dom.Node
    public String getNamespaceURI() {
        return this.m_attributeNode.getNodeValue();
    }

    @Override // ohos.org.w3c.dom.Node
    public String getPrefix() {
        return this.m_attributeNode.getPrefix();
    }

    @Override // ohos.org.w3c.dom.Node
    public String getLocalName() {
        return this.m_attributeNode.getPrefix();
    }

    @Override // ohos.org.w3c.dom.Node
    public boolean hasAttributes() {
        return this.m_attributeNode.hasAttributes();
    }

    @Override // ohos.org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return this.textContent;
    }

    @Override // ohos.org.w3c.dom.Node
    public void setTextContent(String str) throws DOMException {
        this.textContent = str;
    }
}
