package ohos.com.sun.org.apache.xerces.internal.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import ohos.com.sun.org.apache.xerces.internal.dom.ParentNode;
import ohos.com.sun.org.apache.xerces.internal.util.URI;
import ohos.com.sun.org.apache.xerces.internal.util.XML11Char;
import ohos.com.sun.org.apache.xerces.internal.util.XMLChar;
import ohos.com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import ohos.com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import ohos.com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import ohos.com.sun.org.apache.xml.internal.serializer.SerializerConstants;
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
import ohos.org.w3c.dom.Entity;
import ohos.org.w3c.dom.EntityReference;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;
import ohos.org.w3c.dom.Notation;
import ohos.org.w3c.dom.ProcessingInstruction;
import ohos.org.w3c.dom.Text;
import ohos.org.w3c.dom.UserDataHandler;
import ohos.org.w3c.dom.events.Event;
import ohos.org.w3c.dom.events.EventListener;
import ohos.org.w3c.dom.ls.DOMImplementationLS;
import ohos.org.w3c.dom.ls.LSSerializer;

public class CoreDocumentImpl extends ParentNode implements Document {
    private static final int[] kidOK = new int[13];
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("docType", DocumentTypeImpl.class), new ObjectStreamField("docElement", ElementImpl.class), new ObjectStreamField("fFreeNLCache", NodeListCache.class), new ObjectStreamField("encoding", String.class), new ObjectStreamField("actualEncoding", String.class), new ObjectStreamField("version", String.class), new ObjectStreamField("standalone", Boolean.TYPE), new ObjectStreamField("fDocumentURI", String.class), new ObjectStreamField("userData", Hashtable.class), new ObjectStreamField("identifiers", Hashtable.class), new ObjectStreamField("changes", Integer.TYPE), new ObjectStreamField("allowGrammarAccess", Boolean.TYPE), new ObjectStreamField("errorChecking", Boolean.TYPE), new ObjectStreamField("ancestorChecking", Boolean.TYPE), new ObjectStreamField("xmlVersionChanged", Boolean.TYPE), new ObjectStreamField("documentNumber", Integer.TYPE), new ObjectStreamField("nodeCounter", Integer.TYPE), new ObjectStreamField("nodeTable", Hashtable.class), new ObjectStreamField("xml11Version", Boolean.TYPE)};
    static final long serialVersionUID = 0;
    protected String actualEncoding;
    protected boolean allowGrammarAccess;
    protected boolean ancestorChecking;
    protected int changes;
    protected ElementImpl docElement;
    protected DocumentTypeImpl docType;
    private int documentNumber;
    transient DOMNormalizer domNormalizer;
    protected String encoding;
    protected boolean errorChecking;
    transient DOMConfigurationImpl fConfiguration;
    protected String fDocumentURI;
    transient NodeListCache fFreeNLCache;
    transient Object fXPathEvaluator;
    protected Map<String, Node> identifiers;
    private int nodeCounter;
    private Map<Node, Integer> nodeTable;
    private Map<Node, Map<String, ParentNode.UserDataRecord>> nodeUserData;
    protected boolean standalone;
    protected String version;
    private boolean xml11Version;
    protected boolean xmlVersionChanged;

    public void abort() {
    }

    /* access modifiers changed from: protected */
    public void addEventListener(NodeImpl nodeImpl, String str, EventListener eventListener, boolean z) {
    }

    /* access modifiers changed from: protected */
    public void copyEventListeners(NodeImpl nodeImpl, NodeImpl nodeImpl2) {
    }

    /* access modifiers changed from: package-private */
    public void deletedText(NodeImpl nodeImpl, int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public boolean dispatchEvent(NodeImpl nodeImpl, Event event) {
        return false;
    }

    public boolean getAsync() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean getMutationEvents() {
        return false;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getNodeName() {
        return "#document";
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public short getNodeType() {
        return 9;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public final Document getOwnerDocument() {
        return null;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public String getTextContent() throws DOMException {
        return null;
    }

    /* access modifiers changed from: package-private */
    public void insertedNode(NodeImpl nodeImpl, NodeImpl nodeImpl2, boolean z) {
    }

    /* access modifiers changed from: package-private */
    public void insertedText(NodeImpl nodeImpl, int i, int i2) {
    }

    /* access modifiers changed from: package-private */
    public void insertingNode(NodeImpl nodeImpl, boolean z) {
    }

    /* access modifiers changed from: package-private */
    public boolean isNormalizeDocRequired() {
        return true;
    }

    public boolean load(String str) {
        return false;
    }

    public boolean loadXML(String str) {
        return false;
    }

    /* access modifiers changed from: package-private */
    public void modifiedAttrValue(AttrImpl attrImpl, String str) {
    }

    /* access modifiers changed from: package-private */
    public void modifiedCharacterData(NodeImpl nodeImpl, String str, String str2, boolean z) {
    }

    /* access modifiers changed from: package-private */
    public void modifyingCharacterData(NodeImpl nodeImpl, boolean z) {
    }

    /* access modifiers changed from: protected */
    public void removeEventListener(NodeImpl nodeImpl, String str, EventListener eventListener, boolean z) {
    }

    /* access modifiers changed from: package-private */
    public void removedAttrNode(AttrImpl attrImpl, NodeImpl nodeImpl, String str) {
    }

    /* access modifiers changed from: package-private */
    public void removedNode(NodeImpl nodeImpl, boolean z) {
    }

    /* access modifiers changed from: package-private */
    public void removingNode(NodeImpl nodeImpl, NodeImpl nodeImpl2, boolean z) {
    }

    /* access modifiers changed from: package-private */
    public void renamedAttrNode(Attr attr, Attr attr2) {
    }

    /* access modifiers changed from: package-private */
    public void renamedElement(Element element, Element element2) {
    }

    /* access modifiers changed from: package-private */
    public void replacedCharacterData(NodeImpl nodeImpl, String str, String str2) {
    }

    /* access modifiers changed from: package-private */
    public void replacedNode(NodeImpl nodeImpl) {
    }

    /* access modifiers changed from: package-private */
    public void replacedText(NodeImpl nodeImpl) {
    }

    /* access modifiers changed from: package-private */
    public void replacingData(NodeImpl nodeImpl) {
    }

    /* access modifiers changed from: package-private */
    public void replacingNode(NodeImpl nodeImpl) {
    }

    /* access modifiers changed from: package-private */
    public void setAttrNode(AttrImpl attrImpl, AttrImpl attrImpl2) {
    }

    /* access modifiers changed from: package-private */
    public void setMutationEvents(boolean z) {
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public void setTextContent(String str) throws DOMException {
    }

    static {
        int[] iArr = kidOK;
        iArr[9] = 1410;
        iArr[1] = 442;
        iArr[5] = 442;
        iArr[6] = 442;
        iArr[11] = 442;
        iArr[2] = 40;
        iArr[12] = 0;
        iArr[4] = 0;
        iArr[3] = 0;
        iArr[8] = 0;
        iArr[7] = 0;
        iArr[10] = 0;
    }

    public CoreDocumentImpl() {
        this(false);
    }

    public CoreDocumentImpl(boolean z) {
        super(null);
        this.domNormalizer = null;
        this.fConfiguration = null;
        this.fXPathEvaluator = null;
        this.changes = 0;
        this.errorChecking = true;
        this.ancestorChecking = true;
        this.xmlVersionChanged = false;
        this.documentNumber = 0;
        this.nodeCounter = 0;
        this.xml11Version = false;
        this.ownerDocument = this;
        this.allowGrammarAccess = z;
        String systemProperty = SecuritySupport.getSystemProperty("http://java.sun.com/xml/dom/properties/ancestor-check");
        if (systemProperty != null && systemProperty.equalsIgnoreCase("false")) {
            this.ancestorChecking = false;
        }
    }

    public CoreDocumentImpl(DocumentType documentType) {
        this(documentType, false);
    }

    public CoreDocumentImpl(DocumentType documentType, boolean z) {
        this(z);
        if (documentType != null) {
            try {
                ((DocumentTypeImpl) documentType).ownerDocument = this;
                appendChild(documentType);
            } catch (ClassCastException unused) {
                throw new DOMException(4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
            }
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode, ohos.com.sun.org.apache.xerces.internal.dom.ChildNode
    public Node cloneNode(boolean z) {
        CoreDocumentImpl coreDocumentImpl = new CoreDocumentImpl();
        callUserDataHandlers(this, coreDocumentImpl, 1);
        cloneNode(coreDocumentImpl, z);
        return coreDocumentImpl;
    }

    /* access modifiers changed from: protected */
    public void cloneNode(CoreDocumentImpl coreDocumentImpl, boolean z) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (z) {
            Map<Node, String> map = null;
            Map<String, Node> map2 = this.identifiers;
            if (map2 != null) {
                map = new HashMap<>(map2.size());
                for (String str : this.identifiers.keySet()) {
                    map.put(this.identifiers.get(str), str);
                }
            }
            for (ChildNode childNode = this.firstChild; childNode != null; childNode = childNode.nextSibling) {
                coreDocumentImpl.appendChild(coreDocumentImpl.importNode(childNode, true, true, map));
            }
        }
        coreDocumentImpl.allowGrammarAccess = this.allowGrammarAccess;
        coreDocumentImpl.errorChecking = this.errorChecking;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public Node insertBefore(Node node, Node node2) throws DOMException {
        short nodeType = node.getNodeType();
        if (!this.errorChecking || ((nodeType != 1 || this.docElement == null) && (nodeType != 10 || this.docType == null))) {
            if (node.getOwnerDocument() == null && (node instanceof DocumentTypeImpl)) {
                ((DocumentTypeImpl) node).ownerDocument = this;
            }
            super.insertBefore(node, node2);
            if (nodeType == 1) {
                this.docElement = (ElementImpl) node;
            } else if (nodeType == 10) {
                this.docType = (DocumentTypeImpl) node;
            }
            return node;
        }
        throw new DOMException(3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public Node removeChild(Node node) throws DOMException {
        super.removeChild(node);
        short nodeType = node.getNodeType();
        if (nodeType == 1) {
            this.docElement = null;
        } else if (nodeType == 10) {
            this.docType = null;
        }
        return node;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode
    public Node replaceChild(Node node, Node node2) throws DOMException {
        if (node.getOwnerDocument() == null && (node instanceof DocumentTypeImpl)) {
            ((DocumentTypeImpl) node).ownerDocument = this;
        }
        if (!this.errorChecking || ((this.docType == null || node2.getNodeType() == 10 || node.getNodeType() != 10) && (this.docElement == null || node2.getNodeType() == 1 || node.getNodeType() != 1))) {
            super.replaceChild(node, node2);
            short nodeType = node2.getNodeType();
            if (nodeType == 1) {
                this.docElement = (ElementImpl) node;
            } else if (nodeType == 10) {
                this.docType = (DocumentTypeImpl) node;
            }
            return node2;
        }
        throw new DOMException(3, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null));
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public Object getFeature(String str, String str2) {
        Class<?>[] interfaces;
        boolean z = str2 == null || str2.length() == 0;
        if (!str.equalsIgnoreCase("+XPath") || (!z && !str2.equals("3.0"))) {
            return super.getFeature(str, str2);
        }
        Object obj = this.fXPathEvaluator;
        if (obj != null) {
            return obj;
        }
        try {
            Class findProviderClass = ObjectFactory.findProviderClass("ohos.com.sun.org.apache.xpath.internal.domapi.XPathEvaluatorImpl", true);
            Constructor constructor = findProviderClass.getConstructor(Document.class);
            for (Class<?> cls : findProviderClass.getInterfaces()) {
                if (cls.getName().equals("ohos.org.w3c.dom.xpath.XPathEvaluator")) {
                    this.fXPathEvaluator = constructor.newInstance(this);
                    return this.fXPathEvaluator;
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    @Override // ohos.org.w3c.dom.Document
    public Attr createAttribute(String str) throws DOMException {
        if (!this.errorChecking || isXMLName(str, this.xml11Version)) {
            return new AttrImpl(this, str);
        }
        throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Document
    public CDATASection createCDATASection(String str) throws DOMException {
        return new CDATASectionImpl(this, str);
    }

    @Override // ohos.org.w3c.dom.Document
    public Comment createComment(String str) {
        return new CommentImpl(this, str);
    }

    @Override // ohos.org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        return new DocumentFragmentImpl(this);
    }

    @Override // ohos.org.w3c.dom.Document
    public Element createElement(String str) throws DOMException {
        if (!this.errorChecking || isXMLName(str, this.xml11Version)) {
            return new ElementImpl(this, str);
        }
        throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Document
    public EntityReference createEntityReference(String str) throws DOMException {
        if (!this.errorChecking || isXMLName(str, this.xml11Version)) {
            return new EntityReferenceImpl(this, str);
        }
        throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String str, String str2) throws DOMException {
        if (!this.errorChecking || isXMLName(str, this.xml11Version)) {
            return new ProcessingInstructionImpl(this, str, str2);
        }
        throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
    }

    @Override // ohos.org.w3c.dom.Document
    public Text createTextNode(String str) {
        return new TextImpl(this, str);
    }

    @Override // ohos.org.w3c.dom.Document
    public DocumentType getDoctype() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.docType;
    }

    @Override // ohos.org.w3c.dom.Document
    public Element getDocumentElement() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this.docElement;
    }

    @Override // ohos.org.w3c.dom.Document
    public NodeList getElementsByTagName(String str) {
        return new DeepNodeListImpl(this, str);
    }

    @Override // ohos.org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return CoreDOMImplementationImpl.getDOMImplementation();
    }

    public void setErrorChecking(boolean z) {
        this.errorChecking = z;
    }

    @Override // ohos.org.w3c.dom.Document
    public void setStrictErrorChecking(boolean z) {
        this.errorChecking = z;
    }

    public boolean getErrorChecking() {
        return this.errorChecking;
    }

    @Override // ohos.org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        return this.errorChecking;
    }

    @Override // ohos.org.w3c.dom.Document
    public String getInputEncoding() {
        return this.actualEncoding;
    }

    public void setInputEncoding(String str) {
        this.actualEncoding = str;
    }

    public void setXmlEncoding(String str) {
        this.encoding = str;
    }

    public void setEncoding(String str) {
        setXmlEncoding(str);
    }

    @Override // ohos.org.w3c.dom.Document
    public String getXmlEncoding() {
        return this.encoding;
    }

    public String getEncoding() {
        return getXmlEncoding();
    }

    @Override // ohos.org.w3c.dom.Document
    public void setXmlVersion(String str) {
        if (str.equals("1.0") || str.equals(SerializerConstants.XMLVERSION11)) {
            if (!getXmlVersion().equals(str)) {
                this.xmlVersionChanged = true;
                isNormalized(false);
                this.version = str;
            }
            if (getXmlVersion().equals(SerializerConstants.XMLVERSION11)) {
                this.xml11Version = true;
            } else {
                this.xml11Version = false;
            }
        } else {
            throw new DOMException(9, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null));
        }
    }

    public void setVersion(String str) {
        setXmlVersion(str);
    }

    @Override // ohos.org.w3c.dom.Document
    public String getXmlVersion() {
        String str = this.version;
        return str == null ? "1.0" : str;
    }

    public String getVersion() {
        return getXmlVersion();
    }

    @Override // ohos.org.w3c.dom.Document
    public void setXmlStandalone(boolean z) throws DOMException {
        this.standalone = z;
    }

    public void setStandalone(boolean z) {
        setXmlStandalone(z);
    }

    @Override // ohos.org.w3c.dom.Document
    public boolean getXmlStandalone() {
        return this.standalone;
    }

    public boolean getStandalone() {
        return getXmlStandalone();
    }

    @Override // ohos.org.w3c.dom.Document
    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    @Override // ohos.org.w3c.dom.Document
    public Node renameNode(Node node, String str, String str2) throws DOMException {
        if (!this.errorChecking || node.getOwnerDocument() == this || node == this) {
            short nodeType = node.getNodeType();
            if (nodeType == 1) {
                ElementImpl elementImpl = (ElementImpl) node;
                if (elementImpl instanceof ElementNSImpl) {
                    ((ElementNSImpl) elementImpl).rename(str, str2);
                    callUserDataHandlers(elementImpl, null, 4);
                } else if (str == null) {
                    if (this.errorChecking) {
                        if (str2.indexOf(58) != -1) {
                            throw new DOMException(14, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null));
                        } else if (!isXMLName(str2, this.xml11Version)) {
                            throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
                        }
                    }
                    elementImpl.rename(str2);
                    callUserDataHandlers(elementImpl, null, 4);
                } else {
                    ElementNSImpl elementNSImpl = new ElementNSImpl(this, str, str2);
                    copyEventListeners(elementImpl, elementNSImpl);
                    Map<String, ParentNode.UserDataRecord> removeUserDataTable = removeUserDataTable(elementImpl);
                    Node parentNode = elementImpl.getParentNode();
                    Node nextSibling = elementImpl.getNextSibling();
                    if (parentNode != null) {
                        parentNode.removeChild(elementImpl);
                    }
                    for (Node firstChild = elementImpl.getFirstChild(); firstChild != null; firstChild = elementImpl.getFirstChild()) {
                        elementImpl.removeChild(firstChild);
                        elementNSImpl.appendChild(firstChild);
                    }
                    elementNSImpl.moveSpecifiedAttributes(elementImpl);
                    setUserDataTable(elementNSImpl, removeUserDataTable);
                    callUserDataHandlers(elementImpl, elementNSImpl, 4);
                    if (parentNode != null) {
                        parentNode.insertBefore(elementNSImpl, nextSibling);
                    }
                    elementImpl = elementNSImpl;
                }
                renamedElement((Element) node, elementImpl);
                return elementImpl;
            } else if (nodeType == 2) {
                AttrImpl attrImpl = (AttrImpl) node;
                Element ownerElement = attrImpl.getOwnerElement();
                if (ownerElement != null) {
                    ownerElement.removeAttributeNode(attrImpl);
                }
                if (node instanceof AttrNSImpl) {
                    ((AttrNSImpl) attrImpl).rename(str, str2);
                    if (ownerElement != 0) {
                        ownerElement.setAttributeNodeNS(attrImpl);
                    }
                    callUserDataHandlers(attrImpl, null, 4);
                } else if (str == null) {
                    attrImpl.rename(str2);
                    if (ownerElement != 0) {
                        ownerElement.setAttributeNode(attrImpl);
                    }
                    callUserDataHandlers(attrImpl, null, 4);
                } else {
                    AttrNSImpl attrNSImpl = new AttrNSImpl(this, str, str2);
                    copyEventListeners(attrImpl, attrNSImpl);
                    Map<String, ParentNode.UserDataRecord> removeUserDataTable2 = removeUserDataTable(attrImpl);
                    for (Node firstChild2 = attrImpl.getFirstChild(); firstChild2 != null; firstChild2 = attrImpl.getFirstChild()) {
                        attrImpl.removeChild(firstChild2);
                        attrNSImpl.appendChild(firstChild2);
                    }
                    setUserDataTable(attrNSImpl, removeUserDataTable2);
                    callUserDataHandlers(attrImpl, attrNSImpl, 4);
                    if (ownerElement != null) {
                        ownerElement.setAttributeNode(attrNSImpl);
                    }
                    attrImpl = attrNSImpl;
                }
                renamedAttrNode((Attr) node, attrImpl);
                return attrImpl;
            } else {
                throw new DOMException(9, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null));
            }
        } else {
            throw new DOMException(4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
        }
    }

    @Override // ohos.org.w3c.dom.Document
    public void normalizeDocument() {
        if (!isNormalized() || isNormalizeDocRequired()) {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            if (this.domNormalizer == null) {
                this.domNormalizer = new DOMNormalizer();
            }
            DOMConfigurationImpl dOMConfigurationImpl = this.fConfiguration;
            if (dOMConfigurationImpl == null) {
                this.fConfiguration = new DOMConfigurationImpl();
            } else {
                dOMConfigurationImpl.reset();
            }
            this.domNormalizer.normalizeDocument(this, this.fConfiguration);
            isNormalized(true);
            this.xmlVersionChanged = false;
        }
    }

    @Override // ohos.org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        if (this.fConfiguration == null) {
            this.fConfiguration = new DOMConfigurationImpl();
        }
        return this.fConfiguration;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getBaseURI() {
        String str = this.fDocumentURI;
        if (str == null || str.length() == 0) {
            return this.fDocumentURI;
        }
        try {
            return new URI(this.fDocumentURI).toString();
        } catch (URI.MalformedURIException unused) {
            return null;
        }
    }

    @Override // ohos.org.w3c.dom.Document
    public void setDocumentURI(String str) {
        this.fDocumentURI = str;
    }

    public void setAsync(boolean z) {
        if (z) {
            throw new DOMException(9, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null));
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: ohos.org.w3c.dom.Node */
    /* JADX WARN: Multi-variable type inference failed */
    public String saveXML(Node node) throws DOMException {
        if (!this.errorChecking || node == 0 || this == node.getOwnerDocument()) {
            LSSerializer createLSSerializer = ((DOMImplementationLS) DOMImplementationImpl.getDOMImplementation()).createLSSerializer();
            if (node != 0) {
                this = node;
            }
            return createLSSerializer.writeToString(this);
        }
        throw new DOMException(4, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null));
    }

    public DocumentType createDocumentType(String str, String str2, String str3) throws DOMException {
        return new DocumentTypeImpl(this, str, str2, str3);
    }

    public Entity createEntity(String str) throws DOMException {
        if (!this.errorChecking || isXMLName(str, this.xml11Version)) {
            return new EntityImpl(this, str);
        }
        throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
    }

    public Notation createNotation(String str) throws DOMException {
        if (!this.errorChecking || isXMLName(str, this.xml11Version)) {
            return new NotationImpl(this, str);
        }
        throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
    }

    public ElementDefinitionImpl createElementDefinition(String str) throws DOMException {
        if (!this.errorChecking || isXMLName(str, this.xml11Version)) {
            return new ElementDefinitionImpl(this, str);
        }
        throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl
    public int getNodeNumber() {
        if (this.documentNumber == 0) {
            this.documentNumber = ((CoreDOMImplementationImpl) CoreDOMImplementationImpl.getDOMImplementation()).assignDocumentNumber();
        }
        return this.documentNumber;
    }

    /* access modifiers changed from: protected */
    public int getNodeNumber(Node node) {
        Map<Node, Integer> map = this.nodeTable;
        if (map == null) {
            this.nodeTable = new HashMap();
            int i = this.nodeCounter - 1;
            this.nodeCounter = i;
            this.nodeTable.put(node, new Integer(i));
            return i;
        }
        Integer num = map.get(node);
        if (num != null) {
            return num.intValue();
        }
        int i2 = this.nodeCounter - 1;
        this.nodeCounter = i2;
        this.nodeTable.put(node, Integer.valueOf(i2));
        return i2;
    }

    @Override // ohos.org.w3c.dom.Document
    public Node importNode(Node node, boolean z) throws DOMException {
        return importNode(node, z, false, null);
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:67:0x01c3 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX DEBUG: Type inference failed for r3v3. Raw type applied. Possible types: java.util.Map<java.lang.String, ohos.org.w3c.dom.Node>, java.util.Map */
    /* JADX WARN: Type inference failed for: r10v0, types: [ohos.com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl] */
    /* JADX WARN: Type inference failed for: r2v1, types: [ohos.org.w3c.dom.Node] */
    /* JADX WARN: Type inference failed for: r2v2, types: [ohos.org.w3c.dom.Element, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v8, types: [ohos.org.w3c.dom.Node] */
    /* JADX WARN: Type inference failed for: r2v9, types: [ohos.org.w3c.dom.Text] */
    /* JADX WARN: Type inference failed for: r2v10, types: [ohos.org.w3c.dom.CDATASection] */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v15, types: [ohos.com.sun.org.apache.xerces.internal.dom.EntityImpl] */
    /* JADX WARN: Type inference failed for: r2v17, types: [ohos.org.w3c.dom.ProcessingInstruction] */
    /* JADX WARN: Type inference failed for: r2v18, types: [ohos.org.w3c.dom.Comment] */
    /* JADX WARN: Type inference failed for: r2v21, types: [ohos.com.sun.org.apache.xerces.internal.dom.DocumentTypeImpl] */
    /* JADX WARN: Type inference failed for: r2v22, types: [ohos.org.w3c.dom.DocumentFragment] */
    /* JADX WARN: Type inference failed for: r2v25, types: [ohos.com.sun.org.apache.xerces.internal.dom.NotationImpl] */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v29 */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.org.w3c.dom.Node importNode(ohos.org.w3c.dom.Node r11, boolean r12, boolean r13, java.util.Map<ohos.org.w3c.dom.Node, java.lang.String> r14) throws ohos.org.w3c.dom.DOMException {
        /*
        // Method dump skipped, instructions count: 546
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl.importNode(ohos.org.w3c.dom.Node, boolean, boolean, java.util.Map):ohos.org.w3c.dom.Node");
    }

    @Override // ohos.org.w3c.dom.Document
    public Node adoptNode(Node node) {
        Map<String, ParentNode.UserDataRecord> map;
        Node namedItem;
        DOMImplementation implementation;
        DOMImplementation implementation2;
        try {
            NodeImpl nodeImpl = (NodeImpl) node;
            if (node == null) {
                return null;
            }
            if (!(node.getOwnerDocument() == null || (implementation = getImplementation()) == (implementation2 = node.getOwnerDocument().getImplementation()))) {
                if ((implementation instanceof DOMImplementationImpl) && (implementation2 instanceof DeferredDOMImplementationImpl)) {
                    undeferChildren(nodeImpl);
                } else if (!(implementation instanceof DeferredDOMImplementationImpl) || !(implementation2 instanceof DOMImplementationImpl)) {
                    return null;
                }
            }
            short nodeType = nodeImpl.getNodeType();
            if (nodeType == 1) {
                map = nodeImpl.getUserDataRecord();
                Node parentNode = nodeImpl.getParentNode();
                if (parentNode != null) {
                    parentNode.removeChild(node);
                }
                nodeImpl.setOwnerDocument(this);
                if (map != null) {
                    setUserDataTable(nodeImpl, map);
                }
                ((ElementImpl) nodeImpl).reconcileDefaultAttributes();
            } else if (nodeType == 2) {
                AttrImpl attrImpl = (AttrImpl) nodeImpl;
                if (attrImpl.getOwnerElement() != null) {
                    attrImpl.getOwnerElement().removeAttributeNode(attrImpl);
                }
                attrImpl.isSpecified(true);
                Map<String, ParentNode.UserDataRecord> userDataRecord = nodeImpl.getUserDataRecord();
                attrImpl.setOwnerDocument(this);
                if (userDataRecord != null) {
                    setUserDataTable(nodeImpl, userDataRecord);
                }
                map = userDataRecord;
            } else if (nodeType != 5) {
                if (nodeType != 6) {
                    if (nodeType == 9 || nodeType == 10) {
                        throw new DOMException(9, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null));
                    } else if (nodeType != 12) {
                        map = nodeImpl.getUserDataRecord();
                        Node parentNode2 = nodeImpl.getParentNode();
                        if (parentNode2 != null) {
                            parentNode2.removeChild(node);
                        }
                        nodeImpl.setOwnerDocument(this);
                        if (map != null) {
                            setUserDataTable(nodeImpl, map);
                        }
                    }
                }
                throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
            } else {
                map = nodeImpl.getUserDataRecord();
                Node parentNode3 = nodeImpl.getParentNode();
                if (parentNode3 != null) {
                    parentNode3.removeChild(node);
                }
                while (true) {
                    Node firstChild = nodeImpl.getFirstChild();
                    if (firstChild == null) {
                        break;
                    }
                    nodeImpl.removeChild(firstChild);
                }
                nodeImpl.setOwnerDocument(this);
                if (map != null) {
                    setUserDataTable(nodeImpl, map);
                }
                DocumentTypeImpl documentTypeImpl = this.docType;
                if (!(documentTypeImpl == null || (namedItem = documentTypeImpl.getEntities().getNamedItem(nodeImpl.getNodeName())) == null)) {
                    for (Node firstChild2 = namedItem.getFirstChild(); firstChild2 != null; firstChild2 = firstChild2.getNextSibling()) {
                        nodeImpl.appendChild(firstChild2.cloneNode(true));
                    }
                }
            }
            if (map != null) {
                callUserDataHandlers(node, null, 5, map);
            }
            return nodeImpl;
        } catch (ClassCastException unused) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void undeferChildren(Node node) {
        Node node2 = node;
        while (node2 != null) {
            NodeImpl nodeImpl = (NodeImpl) node2;
            if (nodeImpl.needsSyncData()) {
                nodeImpl.synchronizeData();
            }
            NamedNodeMap attributes = node2.getAttributes();
            if (attributes != null) {
                int length = attributes.getLength();
                for (int i = 0; i < length; i++) {
                    undeferChildren(attributes.item(i));
                }
            }
            Node firstChild = node2.getFirstChild();
            while (true) {
                if (firstChild != null || node.equals(node2)) {
                    node2 = firstChild;
                } else {
                    firstChild = node2.getNextSibling();
                    if (firstChild != null || ((node2 = node2.getParentNode()) != null && !node.equals(node2))) {
                    }
                }
            }
            node2 = null;
        }
    }

    @Override // ohos.org.w3c.dom.Document
    public Element getElementById(String str) {
        return getIdentifier(str);
    }

    /* access modifiers changed from: protected */
    public final void clearIdentifiers() {
        Map<String, Node> map = this.identifiers;
        if (map != null) {
            map.clear();
        }
    }

    public void putIdentifier(String str, Element element) {
        if (element == null) {
            removeIdentifier(str);
            return;
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.identifiers == null) {
            this.identifiers = new HashMap();
        }
        this.identifiers.put(str, element);
    }

    public Element getIdentifier(String str) {
        Element element;
        if (needsSyncData()) {
            synchronizeData();
        }
        Map<String, Node> map = this.identifiers;
        if (!(map == null || (element = (Element) map.get(str)) == null)) {
            for (Node parentNode = element.getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
                if (parentNode == this) {
                    return element;
                }
            }
        }
        return null;
    }

    public void removeIdentifier(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        Map<String, Node> map = this.identifiers;
        if (map != null) {
            map.remove(str);
        }
    }

    @Override // ohos.org.w3c.dom.Document
    public Element createElementNS(String str, String str2) throws DOMException {
        return new ElementNSImpl(this, str, str2);
    }

    public Element createElementNS(String str, String str2, String str3) throws DOMException {
        return new ElementNSImpl(this, str, str2, str3);
    }

    @Override // ohos.org.w3c.dom.Document
    public Attr createAttributeNS(String str, String str2) throws DOMException {
        return new AttrNSImpl(this, str, str2);
    }

    public Attr createAttributeNS(String str, String str2, String str3) throws DOMException {
        return new AttrNSImpl(this, str, str2, str3);
    }

    @Override // ohos.org.w3c.dom.Document
    public NodeList getElementsByTagNameNS(String str, String str2) {
        return new DeepNodeListImpl(this, str, str2);
    }

    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        CoreDocumentImpl coreDocumentImpl = (CoreDocumentImpl) super.clone();
        coreDocumentImpl.docType = null;
        coreDocumentImpl.docElement = null;
        return coreDocumentImpl;
    }

    public static final boolean isXMLName(String str, boolean z) {
        if (str == null) {
            return false;
        }
        if (!z) {
            return XMLChar.isValidName(str);
        }
        return XML11Char.isXML11ValidName(str);
    }

    public static final boolean isValidQName(String str, String str2, boolean z) {
        if (str2 == null) {
            return false;
        }
        if (!z) {
            if ((str != null && !XMLChar.isValidNCName(str)) || !XMLChar.isValidNCName(str2)) {
                return false;
            }
        } else if ((str != null && !XML11Char.isXML11ValidNCName(str)) || !XML11Char.isXML11ValidNCName(str2)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isKidOK(Node node, Node node2) {
        return (!this.allowGrammarAccess || node.getNodeType() != 10) ? (kidOK[node.getNodeType()] & (1 << node2.getNodeType())) != 0 : node2.getNodeType() == 1;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl
    public void changed() {
        this.changes++;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl
    public int changes() {
        return this.changes;
    }

    /* access modifiers changed from: package-private */
    public NodeListCache getNodeListCache(ParentNode parentNode) {
        NodeListCache nodeListCache = this.fFreeNLCache;
        if (nodeListCache == null) {
            return new NodeListCache(parentNode);
        }
        this.fFreeNLCache = nodeListCache.next;
        nodeListCache.fChild = null;
        nodeListCache.fChildIndex = -1;
        nodeListCache.fLength = -1;
        if (nodeListCache.fOwner != null) {
            nodeListCache.fOwner.fNodeListCache = null;
        }
        nodeListCache.fOwner = parentNode;
        return nodeListCache;
    }

    /* access modifiers changed from: package-private */
    public void freeNodeListCache(NodeListCache nodeListCache) {
        nodeListCache.next = this.fFreeNLCache;
        this.fFreeNLCache = nodeListCache;
    }

    public Object setUserData(Node node, String str, Object obj, UserDataHandler userDataHandler) {
        Map<String, ParentNode.UserDataRecord> map;
        Map<String, ParentNode.UserDataRecord> map2;
        ParentNode.UserDataRecord remove;
        if (obj == null) {
            Map<Node, Map<String, ParentNode.UserDataRecord>> map3 = this.nodeUserData;
            if (map3 == null || (map2 = map3.get(node)) == null || (remove = map2.remove(str)) == null) {
                return null;
            }
            return remove.fData;
        }
        Map<Node, Map<String, ParentNode.UserDataRecord>> map4 = this.nodeUserData;
        if (map4 == null) {
            this.nodeUserData = new HashMap();
            map = new HashMap<>();
            this.nodeUserData.put(node, map);
        } else {
            map = map4.get(node);
            if (map == null) {
                map = new HashMap<>();
                this.nodeUserData.put(node, map);
            }
        }
        ParentNode.UserDataRecord put = map.put(str, new ParentNode.UserDataRecord(obj, userDataHandler));
        if (put != null) {
            return put.fData;
        }
        return null;
    }

    public Object getUserData(Node node, String str) {
        Map<String, ParentNode.UserDataRecord> map;
        ParentNode.UserDataRecord userDataRecord;
        Map<Node, Map<String, ParentNode.UserDataRecord>> map2 = this.nodeUserData;
        if (map2 == null || (map = map2.get(node)) == null || (userDataRecord = map.get(str)) == null) {
            return null;
        }
        return userDataRecord.fData;
    }

    /* access modifiers changed from: protected */
    public Map<String, ParentNode.UserDataRecord> getUserDataRecord(Node node) {
        Map<String, ParentNode.UserDataRecord> map;
        Map<Node, Map<String, ParentNode.UserDataRecord>> map2 = this.nodeUserData;
        if (map2 == null || (map = map2.get(node)) == null) {
            return null;
        }
        return map;
    }

    /* access modifiers changed from: package-private */
    public Map<String, ParentNode.UserDataRecord> removeUserDataTable(Node node) {
        Map<Node, Map<String, ParentNode.UserDataRecord>> map = this.nodeUserData;
        if (map == null) {
            return null;
        }
        return map.get(node);
    }

    /* access modifiers changed from: package-private */
    public void setUserDataTable(Node node, Map<String, ParentNode.UserDataRecord> map) {
        if (this.nodeUserData == null) {
            this.nodeUserData = new HashMap();
        }
        if (map != null) {
            this.nodeUserData.put(node, map);
        }
    }

    /* access modifiers changed from: package-private */
    public void callUserDataHandlers(Node node, Node node2, short s) {
        Map<String, ParentNode.UserDataRecord> userDataRecord;
        if (this.nodeUserData != null && (node instanceof NodeImpl) && (userDataRecord = ((NodeImpl) node).getUserDataRecord()) != null && !userDataRecord.isEmpty()) {
            callUserDataHandlers(node, node2, s, userDataRecord);
        }
    }

    /* access modifiers changed from: package-private */
    public void callUserDataHandlers(Node node, Node node2, short s, Map<String, ParentNode.UserDataRecord> map) {
        if (!(map == null || map.isEmpty())) {
            for (String str : map.keySet()) {
                ParentNode.UserDataRecord userDataRecord = map.get(str);
                if (userDataRecord.fHandler != null) {
                    userDataRecord.fHandler.handle(s, str, userDataRecord.fData, node, node2);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void checkNamespaceWF(String str, int i, int i2) {
        if (this.errorChecking) {
            if (i == 0 || i == str.length() - 1 || i2 != i) {
                throw new DOMException(14, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null));
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void checkDOMNSErr(String str, String str2) {
        if (!this.errorChecking) {
            return;
        }
        if (str2 == null) {
            throw new DOMException(14, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null));
        } else if (str.equals("xml") && !str2.equals(NamespaceContext.XML_URI)) {
            throw new DOMException(14, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null));
        } else if ((str.equals("xmlns") && !str2.equals(NamespaceContext.XMLNS_URI)) || (!str.equals("xmlns") && str2.equals(NamespaceContext.XMLNS_URI))) {
            throw new DOMException(14, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null));
        }
    }

    /* access modifiers changed from: protected */
    public final void checkQName(String str, String str2) {
        if (this.errorChecking) {
            boolean z = true;
            if (this.xml11Version ? !((str == null || XML11Char.isXML11ValidNCName(str)) && XML11Char.isXML11ValidNCName(str2)) : !((str == null || XMLChar.isValidNCName(str)) && XMLChar.isValidNCName(str2))) {
                z = false;
            }
            if (!z) {
                throw new DOMException(5, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isXML11Version() {
        return this.xml11Version;
    }

    /* access modifiers changed from: package-private */
    public boolean isXMLVersionChanged() {
        return this.xmlVersionChanged;
    }

    /* access modifiers changed from: protected */
    public void setUserData(NodeImpl nodeImpl, Object obj) {
        setUserData(nodeImpl, "XERCES1DOMUSERDATA", obj, null);
    }

    /* access modifiers changed from: protected */
    public Object getUserData(NodeImpl nodeImpl) {
        return getUserData(nodeImpl, "XERCES1DOMUSERDATA");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable;
        Hashtable hashtable2 = null;
        if (this.nodeUserData != null) {
            hashtable = new Hashtable();
            for (Map.Entry<Node, Map<String, ParentNode.UserDataRecord>> entry : this.nodeUserData.entrySet()) {
                hashtable.put(entry.getKey(), new Hashtable(entry.getValue()));
            }
        } else {
            hashtable = null;
        }
        Map<String, Node> map = this.identifiers;
        Hashtable hashtable3 = map == null ? null : new Hashtable(map);
        Map<Node, Integer> map2 = this.nodeTable;
        if (map2 != null) {
            hashtable2 = new Hashtable(map2);
        }
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("docType", this.docType);
        putFields.put("docElement", this.docElement);
        putFields.put("fFreeNLCache", this.fFreeNLCache);
        putFields.put("encoding", this.encoding);
        putFields.put("actualEncoding", this.actualEncoding);
        putFields.put("version", this.version);
        putFields.put("standalone", this.standalone);
        putFields.put("fDocumentURI", this.fDocumentURI);
        putFields.put("userData", hashtable);
        putFields.put("identifiers", hashtable3);
        putFields.put("changes", this.changes);
        putFields.put("allowGrammarAccess", this.allowGrammarAccess);
        putFields.put("errorChecking", this.errorChecking);
        putFields.put("ancestorChecking", this.ancestorChecking);
        putFields.put("xmlVersionChanged", this.xmlVersionChanged);
        putFields.put("documentNumber", this.documentNumber);
        putFields.put("nodeCounter", this.nodeCounter);
        putFields.put("nodeTable", hashtable2);
        putFields.put("xml11Version", this.xml11Version);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        this.docType = (DocumentTypeImpl) readFields.get("docType", (Object) null);
        this.docElement = (ElementImpl) readFields.get("docElement", (Object) null);
        this.fFreeNLCache = (NodeListCache) readFields.get("fFreeNLCache", (Object) null);
        this.encoding = (String) readFields.get("encoding", (Object) null);
        this.actualEncoding = (String) readFields.get("actualEncoding", (Object) null);
        this.version = (String) readFields.get("version", (Object) null);
        this.standalone = readFields.get("standalone", false);
        this.fDocumentURI = (String) readFields.get("fDocumentURI", (Object) null);
        Hashtable hashtable = (Hashtable) readFields.get("userData", (Object) null);
        Hashtable hashtable2 = (Hashtable) readFields.get("identifiers", (Object) null);
        this.changes = readFields.get("changes", 0);
        this.allowGrammarAccess = readFields.get("allowGrammarAccess", false);
        this.errorChecking = readFields.get("errorChecking", true);
        this.ancestorChecking = readFields.get("ancestorChecking", true);
        this.xmlVersionChanged = readFields.get("xmlVersionChanged", false);
        this.documentNumber = readFields.get("documentNumber", 0);
        this.nodeCounter = readFields.get("nodeCounter", 0);
        Hashtable hashtable3 = (Hashtable) readFields.get("nodeTable", (Object) null);
        this.xml11Version = readFields.get("xml11Version", false);
        if (hashtable != null) {
            this.nodeUserData = new HashMap();
            for (Map.Entry entry : hashtable.entrySet()) {
                this.nodeUserData.put((Node) entry.getKey(), new HashMap((Map) entry.getValue()));
            }
        }
        if (hashtable2 != null) {
            this.identifiers = new HashMap(hashtable2);
        }
        if (hashtable3 != null) {
            this.nodeTable = new HashMap(hashtable3);
        }
    }
}
