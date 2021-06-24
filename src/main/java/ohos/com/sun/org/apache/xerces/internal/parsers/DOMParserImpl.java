package ohos.com.sun.org.apache.xerces.internal.parsers;

import java.io.StringReader;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import ohos.com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
import ohos.com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import ohos.com.sun.org.apache.xerces.internal.dom.DOMStringListImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.Constants;
import ohos.com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import ohos.com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser;
import ohos.com.sun.org.apache.xerces.internal.util.DOMUtil;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolTable;
import ohos.com.sun.org.apache.xerces.internal.util.XMLSymbols;
import ohos.com.sun.org.apache.xerces.internal.xni.Augmentations;
import ohos.com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import ohos.com.sun.org.apache.xerces.internal.xni.QName;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLLocator;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLString;
import ohos.com.sun.org.apache.xerces.internal.xni.XNIException;
import ohos.com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import ohos.org.w3c.dom.DOMConfiguration;
import ohos.org.w3c.dom.DOMErrorHandler;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.DOMStringList;
import ohos.org.w3c.dom.Document;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.ls.LSException;
import ohos.org.w3c.dom.ls.LSInput;
import ohos.org.w3c.dom.ls.LSParser;
import ohos.org.w3c.dom.ls.LSParserFilter;
import ohos.org.w3c.dom.ls.LSResourceResolver;

public class DOMParserImpl extends AbstractDOMParser implements LSParser, DOMConfiguration {
    protected static final boolean DEBUG = false;
    protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String PSVI_AUGMENT = "http://apache.org/xml/features/validation/schema/augment-psvi";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    protected static final String XMLSCHEMA = "http://apache.org/xml/features/validation/schema";
    protected static final String XMLSCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    private AbortHandler abortHandler;
    private boolean abortNow;
    private Thread currentThread;
    protected boolean fBusy;
    protected boolean fNamespaceDeclarations;
    private DOMStringList fRecognizedParameters;
    private String fSchemaLocation;
    private Vector fSchemaLocations;
    protected String fSchemaType;

    @Override // ohos.org.w3c.dom.ls.LSParser
    public boolean getAsync() {
        return false;
    }

    @Override // ohos.org.w3c.dom.ls.LSParser
    public DOMConfiguration getDomConfig() {
        return this;
    }

    public DOMParserImpl(XMLParserConfiguration xMLParserConfiguration, String str) {
        this(xMLParserConfiguration);
        if (str == null) {
            return;
        }
        if (str.equals(Constants.NS_DTD)) {
            this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, Constants.NS_DTD);
            this.fSchemaType = Constants.NS_DTD;
        } else if (str.equals(Constants.NS_XMLSCHEMA)) {
            this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, Constants.NS_XMLSCHEMA);
        }
    }

    public DOMParserImpl(XMLParserConfiguration xMLParserConfiguration) {
        super(xMLParserConfiguration);
        this.fNamespaceDeclarations = true;
        this.fSchemaType = null;
        this.fBusy = false;
        this.abortNow = false;
        this.fSchemaLocations = new Vector();
        this.fSchemaLocation = null;
        this.abortHandler = null;
        this.fConfiguration.addRecognizedFeatures(new String[]{Constants.DOM_CANONICAL_FORM, Constants.DOM_CDATA_SECTIONS, Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING, Constants.DOM_INFOSET, Constants.DOM_NAMESPACE_DECLARATIONS, Constants.DOM_SPLIT_CDATA, Constants.DOM_SUPPORTED_MEDIATYPES_ONLY, Constants.DOM_CERTIFIED, Constants.DOM_WELLFORMED, Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS});
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        this.fConfiguration.setFeature(Constants.DOM_NAMESPACE_DECLARATIONS, true);
        this.fConfiguration.setFeature(Constants.DOM_WELLFORMED, true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
        this.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
        this.fConfiguration.setFeature(DYNAMIC_VALIDATION, false);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
        this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
        this.fConfiguration.setFeature(Constants.DOM_CANONICAL_FORM, false);
        this.fConfiguration.setFeature(Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING, true);
        this.fConfiguration.setFeature(Constants.DOM_SPLIT_CDATA, true);
        this.fConfiguration.setFeature(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY, false);
        this.fConfiguration.setFeature(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS, true);
        this.fConfiguration.setFeature(Constants.DOM_CERTIFIED, true);
        try {
            this.fConfiguration.setFeature(NORMALIZE_DATA, false);
        } catch (XMLConfigurationException unused) {
        }
    }

    public DOMParserImpl(SymbolTable symbolTable) {
        this(new XIncludeAwareParserConfiguration());
        this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
    }

    public DOMParserImpl(SymbolTable symbolTable, XMLGrammarPool xMLGrammarPool) {
        this(new XIncludeAwareParserConfiguration());
        this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
        this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", xMLGrammarPool);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.parsers.XMLParser
    public void reset() {
        super.reset();
        this.fNamespaceDeclarations = this.fConfiguration.getFeature(Constants.DOM_NAMESPACE_DECLARATIONS);
        if (this.fSkippedElemStack != null) {
            this.fSkippedElemStack.removeAllElements();
        }
        this.fSchemaLocations.clear();
        this.fRejectedElementDepth = 0;
        this.fFilterReject = false;
        this.fSchemaType = null;
    }

    @Override // ohos.org.w3c.dom.ls.LSParser
    public LSParserFilter getFilter() {
        return this.fDOMFilter;
    }

    @Override // ohos.org.w3c.dom.ls.LSParser
    public void setFilter(LSParserFilter lSParserFilter) {
        this.fDOMFilter = lSParserFilter;
        if (this.fSkippedElemStack == null) {
            this.fSkippedElemStack = new Stack();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:136|137|(2:139|(1:141))(1:142)|143|144) */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x032e, code lost:
        if (r13.equals(ohos.com.sun.org.apache.xerces.internal.parsers.DOMParserImpl.NAMESPACE_GROWTH) == false) goto L_0x0330;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x0334, code lost:
        if (r13.equals(r2) != false) goto L_0x0336;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x0336, code lost:
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0338, code lost:
        r0 = ohos.com.sun.org.apache.xerces.internal.parsers.DOMParserImpl.NAMESPACE_GROWTH;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0339, code lost:
        r12.fConfiguration.getFeature(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x0342, code lost:
        throw newTypeMismatchError(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0347, code lost:
        throw newFeatureNotFoundError(r13);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:136:0x032a */
    @Override // ohos.org.w3c.dom.DOMConfiguration
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setParameter(java.lang.String r13, java.lang.Object r14) throws ohos.org.w3c.dom.DOMException {
        /*
        // Method dump skipped, instructions count: 840
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.parsers.DOMParserImpl.setParameter(java.lang.String, java.lang.Object):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:142:0x021c, code lost:
        return r9.fConfiguration.getProperty(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x0221, code lost:
        throw newFeatureNotFoundError(r10);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:140:0x0216 */
    @Override // ohos.org.w3c.dom.DOMConfiguration
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object getParameter(java.lang.String r10) throws ohos.org.w3c.dom.DOMException {
        /*
        // Method dump skipped, instructions count: 569
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.parsers.DOMParserImpl.getParameter(java.lang.String):java.lang.Object");
    }

    @Override // ohos.org.w3c.dom.DOMConfiguration
    public boolean canSetParameter(String str, Object obj) {
        String str2 = NAMESPACE_GROWTH;
        if (obj == null) {
            return true;
        }
        if (!(obj instanceof Boolean)) {
            return str.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER) ? obj instanceof DOMErrorHandler : str.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER) ? obj instanceof LSResourceResolver : str.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE) ? (obj instanceof String) && (obj.equals(Constants.NS_XMLSCHEMA) || obj.equals(Constants.NS_DTD)) : str.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION) ? obj instanceof String : str.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name");
        }
        boolean booleanValue = ((Boolean) obj).booleanValue();
        if (str.equalsIgnoreCase(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY) || str.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) || str.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || str.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM)) {
            return !booleanValue;
        }
        if (str.equalsIgnoreCase(Constants.DOM_WELLFORMED) || str.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
            return booleanValue;
        }
        if (str.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS) || str.equalsIgnoreCase(Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING) || str.equalsIgnoreCase(Constants.DOM_COMMENTS) || str.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION) || str.equalsIgnoreCase(Constants.DOM_DISALLOW_DOCTYPE) || str.equalsIgnoreCase(Constants.DOM_ENTITIES) || str.equalsIgnoreCase(Constants.DOM_INFOSET) || str.equalsIgnoreCase("namespaces") || str.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS) || str.equalsIgnoreCase(Constants.DOM_VALIDATE) || str.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || str.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || str.equalsIgnoreCase(Constants.DOM_XMLDECL)) {
            return true;
        }
        try {
            if (!str.equalsIgnoreCase(str2)) {
                str2 = str.equalsIgnoreCase(TOLERATE_DUPLICATES) ? TOLERATE_DUPLICATES : str.toLowerCase(Locale.ENGLISH);
            }
            this.fConfiguration.getFeature(str2);
            return true;
        } catch (XMLConfigurationException unused) {
            return false;
        }
    }

    @Override // ohos.org.w3c.dom.DOMConfiguration
    public DOMStringList getParameterNames() {
        if (this.fRecognizedParameters == null) {
            Vector vector = new Vector();
            vector.add("namespaces");
            vector.add(Constants.DOM_CDATA_SECTIONS);
            vector.add(Constants.DOM_CANONICAL_FORM);
            vector.add(Constants.DOM_NAMESPACE_DECLARATIONS);
            vector.add(Constants.DOM_SPLIT_CDATA);
            vector.add(Constants.DOM_ENTITIES);
            vector.add(Constants.DOM_VALIDATE_IF_SCHEMA);
            vector.add(Constants.DOM_VALIDATE);
            vector.add(Constants.DOM_DATATYPE_NORMALIZATION);
            vector.add(Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING);
            vector.add(Constants.DOM_CHECK_CHAR_NORMALIZATION);
            vector.add(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY);
            vector.add(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS);
            vector.add(Constants.DOM_NORMALIZE_CHARACTERS);
            vector.add(Constants.DOM_WELLFORMED);
            vector.add(Constants.DOM_INFOSET);
            vector.add(Constants.DOM_DISALLOW_DOCTYPE);
            vector.add(Constants.DOM_ELEMENT_CONTENT_WHITESPACE);
            vector.add(Constants.DOM_COMMENTS);
            vector.add(Constants.DOM_ERROR_HANDLER);
            vector.add(Constants.DOM_RESOURCE_RESOLVER);
            vector.add(Constants.DOM_SCHEMA_LOCATION);
            vector.add(Constants.DOM_SCHEMA_TYPE);
            this.fRecognizedParameters = new DOMStringListImpl(vector);
        }
        return this.fRecognizedParameters;
    }

    @Override // ohos.org.w3c.dom.ls.LSParser
    public Document parseURI(String str) throws LSException {
        if (!this.fBusy) {
            XMLInputSource xMLInputSource = new XMLInputSource(null, str, null);
            try {
                this.currentThread = Thread.currentThread();
                this.fBusy = true;
                parse(xMLInputSource);
                this.fBusy = false;
                if (this.abortNow && this.currentThread.isInterrupted()) {
                    this.abortNow = false;
                    Thread.interrupted();
                }
            } catch (Exception e) {
                this.fBusy = false;
                if (this.abortNow && this.currentThread.isInterrupted()) {
                    Thread.interrupted();
                }
                if (this.abortNow) {
                    this.abortNow = false;
                    restoreHandlers();
                    return null;
                } else if (e != AbstractDOMParser.Abort.INSTANCE) {
                    if (!(e instanceof XMLParseException) && this.fErrorHandler != null) {
                        DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                        dOMErrorImpl.fException = e;
                        dOMErrorImpl.fMessage = e.getMessage();
                        dOMErrorImpl.fSeverity = 3;
                        this.fErrorHandler.getErrorHandler().handleError(dOMErrorImpl);
                    }
                    throw ((LSException) DOMUtil.createLSException(81, e).fillInStackTrace());
                }
            }
            Document document = getDocument();
            dropDocumentReferences();
            return document;
        }
        throw new DOMException(11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
    }

    @Override // ohos.org.w3c.dom.ls.LSParser
    public Document parse(LSInput lSInput) throws LSException {
        XMLInputSource dom2xmlInputSource = dom2xmlInputSource(lSInput);
        if (!this.fBusy) {
            try {
                this.currentThread = Thread.currentThread();
                this.fBusy = true;
                parse(dom2xmlInputSource);
                this.fBusy = false;
                if (this.abortNow && this.currentThread.isInterrupted()) {
                    this.abortNow = false;
                    Thread.interrupted();
                }
            } catch (Exception e) {
                this.fBusy = false;
                if (this.abortNow && this.currentThread.isInterrupted()) {
                    Thread.interrupted();
                }
                if (this.abortNow) {
                    this.abortNow = false;
                    restoreHandlers();
                    return null;
                } else if (e != AbstractDOMParser.Abort.INSTANCE) {
                    if (!(e instanceof XMLParseException) && this.fErrorHandler != null) {
                        DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
                        dOMErrorImpl.fException = e;
                        dOMErrorImpl.fMessage = e.getMessage();
                        dOMErrorImpl.fSeverity = 3;
                        this.fErrorHandler.getErrorHandler().handleError(dOMErrorImpl);
                    }
                    throw ((LSException) DOMUtil.createLSException(81, e).fillInStackTrace());
                }
            }
            Document document = getDocument();
            dropDocumentReferences();
            return document;
        }
        throw new DOMException(11, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null));
    }

    private void restoreHandlers() {
        this.fConfiguration.setDocumentHandler(this);
        this.fConfiguration.setDTDHandler(this);
        this.fConfiguration.setDTDContentModelHandler(this);
    }

    @Override // ohos.org.w3c.dom.ls.LSParser
    public Node parseWithContext(LSInput lSInput, Node node, short s) throws DOMException, LSException {
        throw new DOMException(9, "Not supported");
    }

    /* access modifiers changed from: package-private */
    public XMLInputSource dom2xmlInputSource(LSInput lSInput) {
        if (lSInput.getCharacterStream() != null) {
            return new XMLInputSource(lSInput.getPublicId(), lSInput.getSystemId(), lSInput.getBaseURI(), lSInput.getCharacterStream(), "UTF-16");
        }
        if (lSInput.getByteStream() != null) {
            return new XMLInputSource(lSInput.getPublicId(), lSInput.getSystemId(), lSInput.getBaseURI(), lSInput.getByteStream(), lSInput.getEncoding());
        }
        if (lSInput.getStringData() != null && lSInput.getStringData().length() > 0) {
            return new XMLInputSource(lSInput.getPublicId(), lSInput.getSystemId(), lSInput.getBaseURI(), new StringReader(lSInput.getStringData()), "UTF-16");
        }
        if ((lSInput.getSystemId() != null && lSInput.getSystemId().length() > 0) || (lSInput.getPublicId() != null && lSInput.getPublicId().length() > 0)) {
            return new XMLInputSource(lSInput.getPublicId(), lSInput.getSystemId(), lSInput.getBaseURI());
        }
        if (this.fErrorHandler != null) {
            DOMErrorImpl dOMErrorImpl = new DOMErrorImpl();
            dOMErrorImpl.fType = "no-input-specified";
            dOMErrorImpl.fMessage = "no-input-specified";
            dOMErrorImpl.fSeverity = 3;
            this.fErrorHandler.getErrorHandler().handleError(dOMErrorImpl);
        }
        throw new LSException(81, "no-input-specified");
    }

    @Override // ohos.org.w3c.dom.ls.LSParser
    public boolean getBusy() {
        return this.fBusy;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser, ohos.org.w3c.dom.ls.LSParser
    public void abort() {
        if (this.fBusy) {
            this.fBusy = false;
            if (this.currentThread != null) {
                this.abortNow = true;
                if (this.abortHandler == null) {
                    this.abortHandler = new AbortHandler();
                }
                this.fConfiguration.setDocumentHandler(this.abortHandler);
                this.fConfiguration.setDTDHandler(this.abortHandler);
                this.fConfiguration.setDTDContentModelHandler(this.abortHandler);
                if (this.currentThread != Thread.currentThread()) {
                    this.currentThread.interrupt();
                    return;
                }
                throw AbstractDOMParser.Abort.INSTANCE;
            }
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) {
        if (!this.fNamespaceDeclarations && this.fNamespaceAware) {
            for (int length = xMLAttributes.getLength() - 1; length >= 0; length--) {
                if (XMLSymbols.PREFIX_XMLNS == xMLAttributes.getPrefix(length) || XMLSymbols.PREFIX_XMLNS == xMLAttributes.getQName(length)) {
                    xMLAttributes.removeAttributeAt(length);
                }
            }
        }
        super.startElement(qName, xMLAttributes, augmentations);
    }

    private class AbortHandler implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler {
        private XMLDocumentSource documentSource;
        private XMLDTDContentModelSource dtdContentSource;
        private XMLDTDSource dtdSource;

        private AbortHandler() {
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startDocument(XMLLocator xMLLocator, String str, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void xmlDecl(String str, String str2, String str3, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void doctypeDecl(String str, String str2, String str3, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void processingInstruction(String str, XMLString xMLString, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startGeneralEntity(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void textDecl(String str, String str2, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endGeneralEntity(String str, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endElement(QName qName, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startCDATA(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endCDATA(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endDocument(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
            this.documentSource = xMLDocumentSource;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public XMLDocumentSource getDocumentSource() {
            return this.documentSource;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startDTD(XMLLocator xMLLocator, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startParameterEntity(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endParameterEntity(String str, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startExternalSubset(XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endExternalSubset(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void elementDecl(String str, String str2, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startAttlist(String str, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void attributeDecl(String str, String str2, String str3, String[] strArr, String str4, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endAttlist(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void internalEntityDecl(String str, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void externalEntityDecl(String str, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void unparsedEntityDecl(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void notationDecl(String str, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startConditional(short s, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void ignoredCharacters(XMLString xMLString, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endConditional(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endDTD(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void setDTDSource(XMLDTDSource xMLDTDSource) {
            this.dtdSource = xMLDTDSource;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public XMLDTDSource getDTDSource() {
            return this.dtdSource;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void startContentModel(String str, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void any(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void empty(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void startGroup(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void pcdata(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void element(String str, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void separator(short s, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void occurrence(short s, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void endGroup(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void endContentModel(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void setDTDContentModelSource(XMLDTDContentModelSource xMLDTDContentModelSource) {
            this.dtdContentSource = xMLDTDContentModelSource;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public XMLDTDContentModelSource getDTDContentModelSource() {
            return this.dtdContentSource;
        }
    }

    private static DOMException newFeatureNotFoundError(String str) {
        return new DOMException(8, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{str}));
    }

    private static DOMException newTypeMismatchError(String str) {
        return new DOMException(17, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{str}));
    }
}
