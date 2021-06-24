package ohos.com.sun.org.apache.xerces.internal.impl.xs.opti;

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
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;
import ohos.org.w3c.dom.ProcessingInstruction;
import ohos.org.w3c.dom.Text;

public class DefaultDocument extends NodeImpl implements Document {
    private String fDocumentURI = null;

    @Override // ohos.org.w3c.dom.Document
    public Comment createComment(String str) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public Text createTextNode(String str) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public DocumentType getDoctype() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public Element getDocumentElement() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public Element getElementById(String str) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public NodeList getElementsByTagName(String str) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public NodeList getElementsByTagNameNS(String str, String str2) {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public String getInputEncoding() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        return false;
    }

    @Override // ohos.org.w3c.dom.Document
    public String getXmlEncoding() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public String getXmlVersion() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public Node importNode(Node node, boolean z) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public Element createElement(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public CDATASection createCDATASection(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String str, String str2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public Attr createAttribute(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public EntityReference createEntityReference(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public Element createElementNS(String str, String str2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public Attr createAttributeNS(String str, String str2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public boolean getXmlStandalone() {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public void setXmlStandalone(boolean z) {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public void setXmlVersion(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public void setStrictErrorChecking(boolean z) {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    @Override // ohos.org.w3c.dom.Document
    public void setDocumentURI(String str) {
        this.fDocumentURI = str;
    }

    @Override // ohos.org.w3c.dom.Document
    public Node adoptNode(Node node) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public void normalizeDocument() {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Document
    public Node renameNode(Node node, String str, String str2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }
}
