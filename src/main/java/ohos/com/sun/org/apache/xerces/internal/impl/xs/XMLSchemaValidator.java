package ohos.com.sun.org.apache.xerces.internal.impl.xs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import ohos.com.sun.org.apache.xerces.internal.impl.Constants;
import ohos.com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
import ohos.com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import ohos.com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import ohos.com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import ohos.com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import ohos.com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.Field;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.KeyRef;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.UniqueOrKey;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.models.CMNodeFactory;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator;
import ohos.com.sun.org.apache.xerces.internal.parsers.XMLParser;
import ohos.com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import ohos.com.sun.org.apache.xerces.internal.util.IntStack;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolTable;
import ohos.com.sun.org.apache.xerces.internal.util.URI;
import ohos.com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import ohos.com.sun.org.apache.xerces.internal.util.XMLChar;
import ohos.com.sun.org.apache.xerces.internal.util.XMLSymbols;
import ohos.com.sun.org.apache.xerces.internal.xni.Augmentations;
import ohos.com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import ohos.com.sun.org.apache.xerces.internal.xni.QName;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLLocator;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLString;
import ohos.com.sun.org.apache.xerces.internal.xni.XNIException;
import ohos.com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import ohos.com.sun.org.apache.xerces.internal.xs.ShortList;
import ohos.com.sun.org.apache.xerces.internal.xs.StringList;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList;
import ohos.com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import ohos.jdk.xml.internal.JdkXmlUtils;
import ohos.telephony.TelephoneNumberUtils;

public class XMLSchemaValidator implements XMLComponent, XMLDocumentFilter, FieldActivator, RevalidationHandler {
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    private static final int BUFFER_SIZE = 20;
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_NORMALIZATION = false;
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final Boolean[] FEATURE_DEFAULTS = {null, null, null, null, null, null, null, null, null, null, null, null, null, Boolean.valueOf(JdkXmlUtils.OVERRIDE_PARSER_DEFAULT)};
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
    protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
    protected static final int ID_CONSTRAINT_NUM = 1;
    static final int INC_STACK_SIZE = 8;
    static final int INITIAL_STACK_SIZE = 8;
    protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String OVERRIDE_PARSER = "jdk.xml.overrideDefaultParser";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, null, null, null, null, null, null, null, null, null, null};
    private static final String[] RECOGNIZED_FEATURES = {VALIDATION, SCHEMA_VALIDATION, DYNAMIC_VALIDATION, SCHEMA_FULL_CHECKING, ALLOW_JAVA_ENCODINGS, CONTINUE_AFTER_FATAL_ERROR, STANDARD_URI_CONFORMANT_FEATURE, "http://apache.org/xml/features/generate-synthetic-annotations", VALIDATE_ANNOTATIONS, HONOUR_ALL_SCHEMALOCATIONS, USE_GRAMMAR_POOL_ONLY, NAMESPACE_GROWTH, TOLERATE_DUPLICATES, "jdk.xml.overrideDefaultParser"};
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", VALIDATION_MANAGER, SCHEMA_LOCATION, SCHEMA_NONS_LOCATION, "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", SCHEMA_DV_FACTORY, "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"};
    protected static final String REPORT_WHITESPACE = "http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace";
    protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
    protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
    protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
    protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
    protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
    protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
    public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    protected static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    private boolean fAppendBuffer = true;
    protected boolean fAugPSVI = true;
    protected final AugmentationsImpl fAugmentations = new AugmentationsImpl();
    private final StringBuffer fBuffer = new StringBuffer();
    private final CMBuilder fCMBuilder = new CMBuilder(this.nodeFactory);
    private XSCMValidator[] fCMStack = new XSCMValidator[8];
    private int[][] fCMStateStack = new int[8][];
    private int[] fCurrCMState;
    private XSCMValidator fCurrentCM;
    private XSElementDecl fCurrentElemDecl;
    protected ElementPSVImpl fCurrentPSVI = new ElementPSVImpl();
    private XSTypeDefinition fCurrentType;
    protected XMLString fDefaultValue;
    protected boolean fDoValidation = false;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    protected boolean fDynamicValidation = false;
    private XSElementDecl[] fElemDeclStack = new XSElementDecl[8];
    private int fElementDepth;
    private final XMLString fEmptyXMLStr = new XMLString(null, 0, -1);
    protected boolean fEntityRef = false;
    protected XMLEntityResolver fEntityResolver;
    protected String fExternalNoNamespaceSchema = null;
    protected String fExternalSchemas = null;
    private boolean fFirstChunk = true;
    protected boolean fFullChecking = false;
    private final XSGrammarBucket fGrammarBucket = new XSGrammarBucket();
    protected XMLGrammarPool fGrammarPool;
    protected boolean fIdConstraint = false;
    protected boolean fInCDATA = false;
    protected Object fJaxpSchemaSource = null;
    protected final Map<String, XMLSchemaLoader.LocationArray> fLocationPairs = new HashMap();
    private XMLLocator fLocator;
    protected XPathMatcherStack fMatcherStack = new XPathMatcherStack();
    protected final HashMap fMayMatchFieldMap = new HashMap();
    private int fNFullValidationDepth;
    private int fNNoneValidationDepth;
    protected boolean fNamespaceGrowth = false;
    private boolean fNil;
    private boolean[] fNilStack = new boolean[8];
    protected boolean fNormalizeData = true;
    private final XMLString fNormalizedStr = new XMLString();
    private XSNotationDecl fNotation;
    private XSNotationDecl[] fNotationStack = new XSNotationDecl[8];
    private final XSSimpleType fQNameDV = ((XSSimpleType) SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(SchemaSymbols.ATTVAL_QNAME));
    private boolean fSawCharacters = false;
    protected boolean fSawOnlyWhitespaceInElementContent = false;
    private boolean fSawText = false;
    private boolean[] fSawTextStack = new boolean[8];
    protected boolean fSchemaDynamicValidation = false;
    protected boolean fSchemaElementDefault = true;
    private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader(this.fXSIErrorReporter.fErrorReporter, this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder);
    private String fSchemaType = null;
    private int fSkipValidationDepth;
    private ValidationState fState4ApplyDefault = new ValidationState();
    private ValidationState fState4XsiType = new ValidationState();
    private boolean fStrictAssess = true;
    private boolean[] fStrictAssessStack = new boolean[8];
    private boolean[] fStringContent = new boolean[8];
    private boolean fSubElement;
    private boolean[] fSubElementStack = new boolean[8];
    private final SubstitutionGroupHandler fSubGroupHandler = new SubstitutionGroupHandler(this.fGrammarBucket);
    protected SymbolTable fSymbolTable;
    private final QName fTempQName = new QName();
    private boolean fTrailing = false;
    private XSTypeDefinition[] fTypeStack = new XSTypeDefinition[8];
    private boolean fUnionType = false;
    protected boolean fUseGrammarPoolOnly = false;
    private ValidatedInfo fValidatedInfo = new ValidatedInfo();
    protected ValidationManager fValidationManager = null;
    private String fValidationRoot;
    protected ValidationState fValidationState = new ValidationState();
    protected ValueStoreCache fValueStoreCache = new ValueStoreCache();
    private short fWhiteSpace = -1;
    protected final XSDDescription fXSDDescription = new XSDDescription();
    protected final XSIErrorReporter fXSIErrorReporter = new XSIErrorReporter();
    private final CMNodeFactory nodeFactory = new CMNodeFactory();
    boolean reportWhitespace = false;

    private short convertToPrimitiveKind(short s) {
        if (s <= 20) {
            return s;
        }
        if (s <= 29) {
            return 2;
        }
        if (s <= 42) {
            return 4;
        }
        return s;
    }

    public void elementDefault(String str) {
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String str, boolean z) throws XMLConfigurationException {
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String str, Object obj) throws XMLConfigurationException {
    }

    /* access modifiers changed from: protected */
    public final class XSIErrorReporter {
        int[] fContext = new int[8];
        int fContextCount;
        XMLErrorReporter fErrorReporter;
        Vector fErrors = new Vector();

        protected XSIErrorReporter() {
        }

        public void reset(XMLErrorReporter xMLErrorReporter) {
            this.fErrorReporter = xMLErrorReporter;
            this.fErrors.removeAllElements();
            this.fContextCount = 0;
        }

        public void pushContext() {
            if (XMLSchemaValidator.this.fAugPSVI) {
                int i = this.fContextCount;
                int[] iArr = this.fContext;
                if (i == iArr.length) {
                    int[] iArr2 = new int[(i + 8)];
                    System.arraycopy(iArr, 0, iArr2, 0, i);
                    this.fContext = iArr2;
                }
                int[] iArr3 = this.fContext;
                int i2 = this.fContextCount;
                this.fContextCount = i2 + 1;
                iArr3[i2] = this.fErrors.size();
            }
        }

        public String[] popContext() {
            if (!XMLSchemaValidator.this.fAugPSVI) {
                return null;
            }
            int[] iArr = this.fContext;
            int i = this.fContextCount - 1;
            this.fContextCount = i;
            int i2 = iArr[i];
            int size = this.fErrors.size() - i2;
            if (size == 0) {
                return null;
            }
            String[] strArr = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                strArr[i3] = (String) this.fErrors.elementAt(i2 + i3);
            }
            this.fErrors.setSize(i2);
            return strArr;
        }

        public String[] mergeContext() {
            if (!XMLSchemaValidator.this.fAugPSVI) {
                return null;
            }
            int[] iArr = this.fContext;
            int i = this.fContextCount - 1;
            this.fContextCount = i;
            int i2 = iArr[i];
            int size = this.fErrors.size() - i2;
            if (size == 0) {
                return null;
            }
            String[] strArr = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                strArr[i3] = (String) this.fErrors.elementAt(i2 + i3);
            }
            return strArr;
        }

        public void reportError(String str, String str2, Object[] objArr, short s) throws XNIException {
            this.fErrorReporter.reportError(str, str2, objArr, s);
            if (XMLSchemaValidator.this.fAugPSVI) {
                this.fErrors.addElement(str2);
            }
        }

        public void reportError(XMLLocator xMLLocator, String str, String str2, Object[] objArr, short s) throws XNIException {
            this.fErrorReporter.reportError(xMLLocator, str, str2, objArr, s);
            if (XMLSchemaValidator.this.fAugPSVI) {
                this.fErrors.addElement(str2);
            }
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String str) {
        int i = 0;
        while (true) {
            String[] strArr = RECOGNIZED_FEATURES;
            if (i >= strArr.length) {
                return null;
            }
            if (strArr[i].equals(str)) {
                return FEATURE_DEFAULTS[i];
            }
            i++;
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String str) {
        int i = 0;
        while (true) {
            String[] strArr = RECOGNIZED_PROPERTIES;
            if (i >= strArr.length) {
                return null;
            }
            if (strArr[i].equals(str)) {
                return PROPERTY_DEFAULTS[i];
            }
            i++;
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler xMLDocumentHandler) {
        this.fDocumentHandler = xMLDocumentHandler;
        if (xMLDocumentHandler instanceof XMLParser) {
            try {
                this.reportWhitespace = ((XMLParser) xMLDocumentHandler).getFeature(REPORT_WHITESPACE);
            } catch (Exception unused) {
                this.reportWhitespace = false;
            }
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource xMLDocumentSource) {
        this.fDocumentSource = xMLDocumentSource;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator xMLLocator, String str, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        this.fValidationState.setNamespaceSupport(namespaceContext);
        this.fState4XsiType.setNamespaceSupport(namespaceContext);
        this.fState4ApplyDefault.setNamespaceSupport(namespaceContext);
        this.fLocator = xMLLocator;
        handleStartDocument(xMLLocator, str);
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.startDocument(xMLLocator, str, namespaceContext, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String str, String str2, String str3, Augmentations augmentations) throws XNIException {
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.xmlDecl(str, str2, str3, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String str, String str2, String str3, Augmentations augmentations) throws XNIException {
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.doctypeDecl(str, str2, str3, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        Augmentations handleStartElement = handleStartElement(qName, xMLAttributes, augmentations);
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.startElement(qName, xMLAttributes, handleStartElement);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        Augmentations handleStartElement = handleStartElement(qName, xMLAttributes, augmentations);
        this.fDefaultValue = null;
        if (this.fElementDepth != -2) {
            handleStartElement = handleEndElement(qName, handleStartElement);
        }
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler == null) {
            return;
        }
        if (!this.fSchemaElementDefault || this.fDefaultValue == null) {
            this.fDocumentHandler.emptyElement(qName, xMLAttributes, handleStartElement);
            return;
        }
        xMLDocumentHandler.startElement(qName, xMLAttributes, handleStartElement);
        this.fDocumentHandler.characters(this.fDefaultValue, null);
        this.fDocumentHandler.endElement(qName, handleStartElement);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        XMLString handleCharacters = handleCharacters(xMLString);
        if (this.fSawOnlyWhitespaceInElementContent) {
            this.fSawOnlyWhitespaceInElementContent = false;
            if (!this.reportWhitespace) {
                ignorableWhitespace(handleCharacters, augmentations);
                return;
            }
        }
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler == null) {
            return;
        }
        if (!this.fNormalizeData || !this.fUnionType) {
            this.fDocumentHandler.characters(handleCharacters, augmentations);
        } else if (augmentations != null) {
            xMLDocumentHandler.characters(this.fEmptyXMLStr, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        handleIgnorableWhitespace(xMLString);
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.ignorableWhitespace(xMLString, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        XMLString xMLString;
        this.fDefaultValue = null;
        Augmentations handleEndElement = handleEndElement(qName, augmentations);
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler == null) {
            return;
        }
        if (!this.fSchemaElementDefault || (xMLString = this.fDefaultValue) == null) {
            this.fDocumentHandler.endElement(qName, handleEndElement);
            return;
        }
        xMLDocumentHandler.characters(xMLString, null);
        this.fDocumentHandler.endElement(qName, handleEndElement);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = true;
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.startCDATA(augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augmentations) throws XNIException {
        this.fInCDATA = false;
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.endCDATA(augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augmentations) throws XNIException {
        handleEndDocument();
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.endDocument(augmentations);
        }
        this.fLocator = null;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.RevalidationHandler
    public boolean characterData(String str, Augmentations augmentations) {
        short s;
        this.fSawText = this.fSawText || str.length() > 0;
        if (this.fNormalizeData && (s = this.fWhiteSpace) != -1 && s != 0) {
            normalizeWhitespace(str, s == 2);
            this.fBuffer.append(this.fNormalizedStr.ch, this.fNormalizedStr.offset, this.fNormalizedStr.length);
        } else if (this.fAppendBuffer) {
            this.fBuffer.append(str);
        }
        XSTypeDefinition xSTypeDefinition = this.fCurrentType;
        if (xSTypeDefinition != null && xSTypeDefinition.getTypeCategory() == 15 && ((XSComplexTypeDecl) this.fCurrentType).fContentType == 2) {
            for (int i = 0; i < str.length(); i++) {
                if (!XMLChar.isSpace(str.charAt(i))) {
                    this.fSawCharacters = true;
                    return false;
                }
            }
        }
        return true;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException {
        this.fEntityRef = true;
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.startGeneralEntity(str, xMLResourceIdentifier, str2, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String str, String str2, Augmentations augmentations) throws XNIException {
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.textDecl(str, str2, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.comment(xMLString, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String str, XMLString xMLString, Augmentations augmentations) throws XNIException {
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.processingInstruction(str, xMLString, augmentations);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String str, Augmentations augmentations) throws XNIException {
        this.fEntityRef = false;
        XMLDocumentHandler xMLDocumentHandler = this.fDocumentHandler;
        if (xMLDocumentHandler != null) {
            xMLDocumentHandler.endGeneralEntity(str, augmentations);
        }
    }

    public XMLSchemaValidator() {
        this.fState4XsiType.setExtraChecking(false);
        this.fState4ApplyDefault.setFacetChecking(false);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        this.fIdConstraint = false;
        this.fLocationPairs.clear();
        this.fValidationState.resetIDTables();
        this.nodeFactory.reset(xMLComponentManager);
        this.fSchemaLoader.reset(xMLComponentManager);
        this.fCurrentElemDecl = null;
        this.fCurrentCM = null;
        this.fCurrCMState = null;
        this.fSkipValidationDepth = -1;
        this.fNFullValidationDepth = -1;
        this.fNNoneValidationDepth = -1;
        this.fElementDepth = -1;
        this.fSubElement = false;
        this.fSchemaDynamicValidation = false;
        this.fEntityRef = false;
        this.fInCDATA = false;
        this.fMatcherStack.clear();
        if (!this.fMayMatchFieldMap.isEmpty()) {
            this.fMayMatchFieldMap.clear();
        }
        this.fXSIErrorReporter.reset((XMLErrorReporter) xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
        if (!xMLComponentManager.getFeature(PARSER_SETTINGS, true)) {
            this.fValidationManager.addValidationState(this.fValidationState);
            XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
            return;
        }
        SymbolTable symbolTable = (SymbolTable) xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        if (symbolTable != this.fSymbolTable) {
            this.fSymbolTable = symbolTable;
        }
        this.fNamespaceGrowth = xMLComponentManager.getFeature(NAMESPACE_GROWTH, false);
        this.fDynamicValidation = xMLComponentManager.getFeature(DYNAMIC_VALIDATION, false);
        if (this.fDynamicValidation) {
            this.fDoValidation = true;
        } else {
            this.fDoValidation = xMLComponentManager.getFeature(VALIDATION, false);
        }
        boolean z = this.fDoValidation;
        if (z) {
            this.fDoValidation = z | xMLComponentManager.getFeature(SCHEMA_VALIDATION, false);
        }
        this.fFullChecking = xMLComponentManager.getFeature(SCHEMA_FULL_CHECKING, false);
        this.fNormalizeData = xMLComponentManager.getFeature(NORMALIZE_DATA, false);
        this.fSchemaElementDefault = xMLComponentManager.getFeature(SCHEMA_ELEMENT_DEFAULT, false);
        this.fAugPSVI = xMLComponentManager.getFeature(SCHEMA_AUGMENT_PSVI, true);
        this.fSchemaType = (String) xMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null);
        this.fUseGrammarPoolOnly = xMLComponentManager.getFeature(USE_GRAMMAR_POOL_ONLY, false);
        this.fEntityResolver = (XMLEntityResolver) xMLComponentManager.getProperty(ENTITY_MANAGER);
        this.fValidationManager = (ValidationManager) xMLComponentManager.getProperty(VALIDATION_MANAGER);
        this.fValidationManager.addValidationState(this.fValidationState);
        this.fValidationState.setSymbolTable(this.fSymbolTable);
        try {
            this.fExternalSchemas = (String) xMLComponentManager.getProperty(SCHEMA_LOCATION);
            this.fExternalNoNamespaceSchema = (String) xMLComponentManager.getProperty(SCHEMA_NONS_LOCATION);
        } catch (XMLConfigurationException unused) {
            this.fExternalSchemas = null;
            this.fExternalNoNamespaceSchema = null;
        }
        XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
        this.fJaxpSchemaSource = xMLComponentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", null);
        this.fGrammarPool = (XMLGrammarPool) xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null);
        this.fState4XsiType.setSymbolTable(symbolTable);
        this.fState4ApplyDefault.setSymbolTable(symbolTable);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public void startValueScopeFor(IdentityConstraint identityConstraint, int i) {
        this.fValueStoreCache.getValueStoreFor(identityConstraint, i).startValueScope();
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public XPathMatcher activateField(Field field, int i) {
        ValueStoreBase valueStoreFor = this.fValueStoreCache.getValueStoreFor(field.getIdentityConstraint(), i);
        setMayMatch(field, Boolean.TRUE);
        XPathMatcher createMatcher = field.createMatcher(this, valueStoreFor);
        this.fMatcherStack.addMatcher(createMatcher);
        createMatcher.startDocumentFragment();
        return createMatcher;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public void endValueScopeFor(IdentityConstraint identityConstraint, int i) {
        this.fValueStoreCache.getValueStoreFor(identityConstraint, i).endValueScope();
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public void setMayMatch(Field field, Boolean bool) {
        this.fMayMatchFieldMap.put(field, bool);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public Boolean mayMatch(Field field) {
        return (Boolean) this.fMayMatchFieldMap.get(field);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void activateSelectorFor(IdentityConstraint identityConstraint) {
        Selector selector = identityConstraint.getSelector();
        if (selector != null) {
            XPathMatcher createMatcher = selector.createMatcher(this, this.fElementDepth);
            this.fMatcherStack.addMatcher(createMatcher);
            createMatcher.startDocumentFragment();
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureStackCapacity() {
        int i = this.fElementDepth;
        if (i == this.fElemDeclStack.length) {
            int i2 = i + 8;
            boolean[] zArr = new boolean[i2];
            System.arraycopy(this.fSubElementStack, 0, zArr, 0, i);
            this.fSubElementStack = zArr;
            XSElementDecl[] xSElementDeclArr = new XSElementDecl[i2];
            System.arraycopy(this.fElemDeclStack, 0, xSElementDeclArr, 0, this.fElementDepth);
            this.fElemDeclStack = xSElementDeclArr;
            boolean[] zArr2 = new boolean[i2];
            System.arraycopy(this.fNilStack, 0, zArr2, 0, this.fElementDepth);
            this.fNilStack = zArr2;
            XSNotationDecl[] xSNotationDeclArr = new XSNotationDecl[i2];
            System.arraycopy(this.fNotationStack, 0, xSNotationDeclArr, 0, this.fElementDepth);
            this.fNotationStack = xSNotationDeclArr;
            XSTypeDefinition[] xSTypeDefinitionArr = new XSTypeDefinition[i2];
            System.arraycopy(this.fTypeStack, 0, xSTypeDefinitionArr, 0, this.fElementDepth);
            this.fTypeStack = xSTypeDefinitionArr;
            XSCMValidator[] xSCMValidatorArr = new XSCMValidator[i2];
            System.arraycopy(this.fCMStack, 0, xSCMValidatorArr, 0, this.fElementDepth);
            this.fCMStack = xSCMValidatorArr;
            boolean[] zArr3 = new boolean[i2];
            System.arraycopy(this.fSawTextStack, 0, zArr3, 0, this.fElementDepth);
            this.fSawTextStack = zArr3;
            boolean[] zArr4 = new boolean[i2];
            System.arraycopy(this.fStringContent, 0, zArr4, 0, this.fElementDepth);
            this.fStringContent = zArr4;
            boolean[] zArr5 = new boolean[i2];
            System.arraycopy(this.fStrictAssessStack, 0, zArr5, 0, this.fElementDepth);
            this.fStrictAssessStack = zArr5;
            int[][] iArr = new int[i2][];
            System.arraycopy(this.fCMStateStack, 0, iArr, 0, this.fElementDepth);
            this.fCMStateStack = iArr;
        }
    }

    /* access modifiers changed from: package-private */
    public void handleStartDocument(XMLLocator xMLLocator, String str) {
        this.fValueStoreCache.startDocument();
        if (this.fAugPSVI) {
            ElementPSVImpl elementPSVImpl = this.fCurrentPSVI;
            elementPSVImpl.fGrammars = null;
            elementPSVImpl.fSchemaInformation = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void handleEndDocument() {
        this.fValueStoreCache.endDocument();
    }

    /* access modifiers changed from: package-private */
    public XMLString handleCharacters(XMLString xMLString) {
        short s;
        if (this.fSkipValidationDepth >= 0) {
            return xMLString;
        }
        this.fSawText = this.fSawText || xMLString.length > 0;
        if (!(!this.fNormalizeData || (s = this.fWhiteSpace) == -1 || s == 0)) {
            normalizeWhitespace(xMLString, s == 2);
            xMLString = this.fNormalizedStr;
        }
        if (this.fAppendBuffer) {
            this.fBuffer.append(xMLString.ch, xMLString.offset, xMLString.length);
        }
        this.fSawOnlyWhitespaceInElementContent = false;
        XSTypeDefinition xSTypeDefinition = this.fCurrentType;
        if (xSTypeDefinition != null && xSTypeDefinition.getTypeCategory() == 15 && ((XSComplexTypeDecl) this.fCurrentType).fContentType == 2) {
            int i = xMLString.offset;
            while (true) {
                if (i >= xMLString.offset + xMLString.length) {
                    break;
                } else if (!XMLChar.isSpace(xMLString.ch[i])) {
                    this.fSawCharacters = true;
                    break;
                } else {
                    this.fSawOnlyWhitespaceInElementContent = !this.fSawCharacters;
                    i++;
                }
            }
        }
        return xMLString;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0076, code lost:
        if (r11.fFirstChunk == false) goto L_0x0070;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0096  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void normalizeWhitespace(ohos.com.sun.org.apache.xerces.internal.xni.XMLString r12, boolean r13) {
        /*
        // Method dump skipped, instructions count: 180
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.normalizeWhitespace(ohos.com.sun.org.apache.xerces.internal.xni.XMLString, boolean):void");
    }

    private void normalizeWhitespace(String str, boolean z) {
        int length = str.length();
        if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < length) {
            this.fNormalizedStr.ch = new char[length];
        }
        XMLString xMLString = this.fNormalizedStr;
        xMLString.offset = 0;
        xMLString.length = 0;
        boolean z2 = z;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (!XMLChar.isSpace(charAt)) {
                char[] cArr = this.fNormalizedStr.ch;
                XMLString xMLString2 = this.fNormalizedStr;
                int i2 = xMLString2.length;
                xMLString2.length = i2 + 1;
                cArr[i2] = charAt;
                z2 = false;
            } else if (!z2) {
                char[] cArr2 = this.fNormalizedStr.ch;
                XMLString xMLString3 = this.fNormalizedStr;
                int i3 = xMLString3.length;
                xMLString3.length = i3 + 1;
                cArr2[i3] = ' ';
                z2 = z;
            }
        }
        if (z2 && this.fNormalizedStr.length != 0) {
            XMLString xMLString4 = this.fNormalizedStr;
            xMLString4.length--;
        }
    }

    /* access modifiers changed from: package-private */
    public void handleIgnorableWhitespace(XMLString xMLString) {
        if (this.fSkipValidationDepth >= 0) {
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x0202  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x0215  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x0246  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0267  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x0279  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x029b  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x02c3  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x02ce A[LOOP:0: B:148:0x02cc->B:149:0x02ce, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:161:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01a1  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01d1  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01d4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.xni.Augmentations handleStartElement(ohos.com.sun.org.apache.xerces.internal.xni.QName r11, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes r12, ohos.com.sun.org.apache.xerces.internal.xni.Augmentations r13) {
        /*
        // Method dump skipped, instructions count: 757
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.handleStartElement(ohos.com.sun.org.apache.xerces.internal.xni.QName, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes, ohos.com.sun.org.apache.xerces.internal.xni.Augmentations):ohos.com.sun.org.apache.xerces.internal.xni.Augmentations");
    }

    /* access modifiers changed from: package-private */
    public Augmentations handleEndElement(QName qName, Augmentations augmentations) {
        Selector.Matcher matcher;
        IdentityConstraint identityConstraint;
        ValueStoreBase valueStoreFor;
        Selector.Matcher matcher2;
        IdentityConstraint identityConstraint2;
        Object obj;
        short s;
        ShortList shortList;
        int i = this.fSkipValidationDepth;
        if (i >= 0) {
            int i2 = this.fElementDepth;
            if (i != i2 || i <= 0) {
                this.fElementDepth--;
            } else {
                this.fNFullValidationDepth = i - 1;
                this.fSkipValidationDepth = -1;
                this.fElementDepth = i2 - 1;
                boolean[] zArr = this.fSubElementStack;
                int i3 = this.fElementDepth;
                this.fSubElement = zArr[i3];
                this.fCurrentElemDecl = this.fElemDeclStack[i3];
                this.fNil = this.fNilStack[i3];
                this.fNotation = this.fNotationStack[i3];
                this.fCurrentType = this.fTypeStack[i3];
                this.fCurrentCM = this.fCMStack[i3];
                this.fStrictAssess = this.fStrictAssessStack[i3];
                this.fCurrCMState = this.fCMStateStack[i3];
                this.fSawText = this.fSawTextStack[i3];
                this.fSawCharacters = this.fStringContent[i3];
            }
            if (this.fElementDepth == -1 && this.fFullChecking) {
                XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
            }
            return this.fAugPSVI ? getEmptyAugs(augmentations) : augmentations;
        }
        processElementContent(qName);
        int matcherCount = this.fMatcherStack.getMatcherCount() - 1;
        for (int i4 = matcherCount; i4 >= 0; i4--) {
            XPathMatcher matcherAt = this.fMatcherStack.getMatcherAt(i4);
            XSElementDecl xSElementDecl = this.fCurrentElemDecl;
            if (xSElementDecl == null) {
                matcherAt.endElement(qName, null, false, this.fValidatedInfo.actualValue, this.fValidatedInfo.actualValueType, this.fValidatedInfo.itemValueTypes);
            } else {
                XSTypeDefinition xSTypeDefinition = this.fCurrentType;
                boolean nillable = xSElementDecl.getNillable();
                if (this.fDefaultValue == null) {
                    obj = this.fValidatedInfo.actualValue;
                } else {
                    obj = this.fCurrentElemDecl.fDefault.actualValue;
                }
                if (this.fDefaultValue == null) {
                    s = this.fValidatedInfo.actualValueType;
                } else {
                    s = this.fCurrentElemDecl.fDefault.actualValueType;
                }
                if (this.fDefaultValue == null) {
                    shortList = this.fValidatedInfo.itemValueTypes;
                } else {
                    shortList = this.fCurrentElemDecl.fDefault.itemValueTypes;
                }
                matcherAt.endElement(qName, xSTypeDefinition, nillable, obj, s, shortList);
            }
        }
        if (this.fMatcherStack.size() > 0) {
            this.fMatcherStack.popContext();
        }
        int matcherCount2 = this.fMatcherStack.getMatcherCount();
        for (int i5 = matcherCount; i5 >= matcherCount2; i5--) {
            XPathMatcher matcherAt2 = this.fMatcherStack.getMatcherAt(i5);
            if (!(!(matcherAt2 instanceof Selector.Matcher) || (identityConstraint2 = (matcher2 = (Selector.Matcher) matcherAt2).getIdentityConstraint()) == null || identityConstraint2.getCategory() == 2)) {
                this.fValueStoreCache.transplant(identityConstraint2, matcher2.getInitialDepth());
            }
        }
        while (matcherCount >= matcherCount2) {
            XPathMatcher matcherAt3 = this.fMatcherStack.getMatcherAt(matcherCount);
            if ((matcherAt3 instanceof Selector.Matcher) && (identityConstraint = (matcher = (Selector.Matcher) matcherAt3).getIdentityConstraint()) != null && identityConstraint.getCategory() == 2 && (valueStoreFor = this.fValueStoreCache.getValueStoreFor(identityConstraint, matcher.getInitialDepth())) != null) {
                valueStoreFor.endDocumentFragment();
            }
            matcherCount--;
        }
        this.fValueStoreCache.endElement();
        if (this.fElementDepth == 0) {
            String checkIDRefID = this.fValidationState.checkIDRefID();
            this.fValidationState.resetIDTables();
            if (checkIDRefID != null) {
                reportSchemaError("cvc-id.1", new Object[]{checkIDRefID});
            }
            if (this.fFullChecking) {
                XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
            }
            SchemaGrammar[] grammars = this.fGrammarBucket.getGrammars();
            if (this.fGrammarPool != null) {
                for (SchemaGrammar schemaGrammar : grammars) {
                    schemaGrammar.setImmutable(true);
                }
                this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", grammars);
            }
            return endElementPSVI(true, grammars, augmentations);
        }
        Augmentations endElementPSVI = endElementPSVI(false, null, augmentations);
        this.fElementDepth--;
        boolean[] zArr2 = this.fSubElementStack;
        int i6 = this.fElementDepth;
        this.fSubElement = zArr2[i6];
        this.fCurrentElemDecl = this.fElemDeclStack[i6];
        this.fNil = this.fNilStack[i6];
        this.fNotation = this.fNotationStack[i6];
        this.fCurrentType = this.fTypeStack[i6];
        this.fCurrentCM = this.fCMStack[i6];
        this.fStrictAssess = this.fStrictAssessStack[i6];
        this.fCurrCMState = this.fCMStateStack[i6];
        this.fSawText = this.fSawTextStack[i6];
        this.fSawCharacters = this.fStringContent[i6];
        this.fWhiteSpace = -1;
        this.fAppendBuffer = false;
        this.fUnionType = false;
        return endElementPSVI;
    }

    /* access modifiers changed from: package-private */
    public final Augmentations endElementPSVI(boolean z, SchemaGrammar[] schemaGrammarArr, Augmentations augmentations) {
        if (this.fAugPSVI) {
            augmentations = getEmptyAugs(augmentations);
            ElementPSVImpl elementPSVImpl = this.fCurrentPSVI;
            elementPSVImpl.fDeclaration = this.fCurrentElemDecl;
            elementPSVImpl.fTypeDecl = this.fCurrentType;
            elementPSVImpl.fNotation = this.fNotation;
            elementPSVImpl.fValidationContext = this.fValidationRoot;
            int i = this.fElementDepth;
            short s = 2;
            if (i > this.fNFullValidationDepth) {
                elementPSVImpl.fValidationAttempted = 2;
            } else if (i > this.fNNoneValidationDepth) {
                elementPSVImpl.fValidationAttempted = 0;
            } else {
                elementPSVImpl.fValidationAttempted = 1;
                int i2 = i - 1;
                this.fNNoneValidationDepth = i2;
                this.fNFullValidationDepth = i2;
            }
            if (this.fDefaultValue != null) {
                this.fCurrentPSVI.fSpecified = true;
            }
            ElementPSVImpl elementPSVImpl2 = this.fCurrentPSVI;
            elementPSVImpl2.fNil = this.fNil;
            elementPSVImpl2.fMemberType = this.fValidatedInfo.memberType;
            this.fCurrentPSVI.fNormalizedValue = this.fValidatedInfo.normalizedValue;
            this.fCurrentPSVI.fActualValue = this.fValidatedInfo.actualValue;
            this.fCurrentPSVI.fActualValueType = this.fValidatedInfo.actualValueType;
            this.fCurrentPSVI.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
            if (this.fStrictAssess) {
                String[] mergeContext = this.fXSIErrorReporter.mergeContext();
                ElementPSVImpl elementPSVImpl3 = this.fCurrentPSVI;
                elementPSVImpl3.fErrorCodes = mergeContext;
                if (mergeContext != null) {
                    s = 1;
                }
                elementPSVImpl3.fValidity = s;
            } else {
                this.fCurrentPSVI.fValidity = 0;
                this.fXSIErrorReporter.popContext();
            }
            if (z) {
                ElementPSVImpl elementPSVImpl4 = this.fCurrentPSVI;
                elementPSVImpl4.fGrammars = schemaGrammarArr;
                elementPSVImpl4.fSchemaInformation = null;
            }
        }
        return augmentations;
    }

    /* access modifiers changed from: package-private */
    public Augmentations getEmptyAugs(Augmentations augmentations) {
        if (augmentations == null) {
            augmentations = this.fAugmentations;
            augmentations.removeAllItems();
        }
        augmentations.putItem(Constants.ELEMENT_PSVI, this.fCurrentPSVI);
        this.fCurrentPSVI.reset();
        return augmentations;
    }

    /* access modifiers changed from: package-private */
    public void storeLocations(String str, String str2) {
        if (str != null && !XMLSchemaLoader.tokenizeSchemaLocationStr(str, this.fLocationPairs)) {
            this.fXSIErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "SchemaLocation", new Object[]{str}, 0);
        }
        if (str2 != null) {
            XMLSchemaLoader.LocationArray locationArray = this.fLocationPairs.get(XMLSymbols.EMPTY_STRING);
            if (locationArray == null) {
                locationArray = new XMLSchemaLoader.LocationArray();
                this.fLocationPairs.put(XMLSymbols.EMPTY_STRING, locationArray);
            }
            locationArray.addLocation(str2);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar findSchemaGrammar(short r8, java.lang.String r9, ohos.com.sun.org.apache.xerces.internal.xni.QName r10, ohos.com.sun.org.apache.xerces.internal.xni.QName r11, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes r12) {
        /*
        // Method dump skipped, instructions count: 211
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.findSchemaGrammar(short, java.lang.String, ohos.com.sun.org.apache.xerces.internal.xni.QName, ohos.com.sun.org.apache.xerces.internal.xni.QName, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes):ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar");
    }

    private void setLocationHints(XSDDescription xSDDescription, String[] strArr, SchemaGrammar schemaGrammar) {
        int length = strArr.length;
        if (schemaGrammar == null) {
            XSDDescription xSDDescription2 = this.fXSDDescription;
            xSDDescription2.fLocationHints = new String[length];
            System.arraycopy(strArr, 0, xSDDescription2.fLocationHints, 0, length);
            return;
        }
        setLocationHints(xSDDescription, strArr, schemaGrammar.getDocumentLocations());
    }

    private void setLocationHints(XSDDescription xSDDescription, String[] strArr, StringList stringList) {
        int length = strArr.length;
        String[] strArr2 = new String[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            try {
                if (!stringList.contains(XMLEntityManager.expandSystemId(strArr[i2], xSDDescription.getBaseSystemId(), false))) {
                    int i3 = i + 1;
                    try {
                        strArr2[i] = strArr[i2];
                    } catch (URI.MalformedURIException unused) {
                    }
                    i = i3;
                }
            } catch (URI.MalformedURIException unused2) {
            }
        }
        if (i <= 0) {
            return;
        }
        if (i == length) {
            this.fXSDDescription.fLocationHints = strArr2;
            return;
        }
        XSDDescription xSDDescription2 = this.fXSDDescription;
        xSDDescription2.fLocationHints = new String[i];
        System.arraycopy(strArr2, 0, xSDDescription2.fLocationHints, 0, i);
    }

    /* access modifiers changed from: package-private */
    public XSTypeDefinition getAndCheckXsiType(QName qName, String str, XMLAttributes xMLAttributes) {
        SchemaGrammar findSchemaGrammar;
        try {
            QName qName2 = (QName) this.fQNameDV.validate(str, (ValidationContext) this.fValidationState, (ValidatedInfo) null);
            XSTypeDefinition globalTypeDecl = qName2.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA ? SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(qName2.localpart) : null;
            if (globalTypeDecl == null && (findSchemaGrammar = findSchemaGrammar(7, qName2.uri, qName, qName2, xMLAttributes)) != null) {
                globalTypeDecl = findSchemaGrammar.getGlobalTypeDecl(qName2.localpart);
            }
            if (globalTypeDecl == null) {
                reportSchemaError("cvc-elt.4.2", new Object[]{qName.rawname, str});
                return null;
            }
            if (this.fCurrentType != null) {
                short s = this.fCurrentElemDecl.fBlock;
                if (this.fCurrentType.getTypeCategory() == 15) {
                    s = (short) (s | ((XSComplexTypeDecl) this.fCurrentType).fBlock);
                }
                if (!XSConstraints.checkTypeDerivationOk(globalTypeDecl, this.fCurrentType, s)) {
                    reportSchemaError("cvc-elt.4.3", new Object[]{qName.rawname, str, this.fCurrentType.getName()});
                }
            }
            return globalTypeDecl;
        } catch (InvalidDatatypeValueException e) {
            reportSchemaError(e.getKey(), e.getArgs());
            reportSchemaError("cvc-elt.4.1", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_TYPE, str});
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean getXsiNil(QName qName, String str) {
        XSElementDecl xSElementDecl = this.fCurrentElemDecl;
        if (xSElementDecl == null || xSElementDecl.getNillable()) {
            String trim = XMLChar.trim(str);
            if (trim.equals("true") || trim.equals("1")) {
                XSElementDecl xSElementDecl2 = this.fCurrentElemDecl;
                if (xSElementDecl2 != null && xSElementDecl2.getConstraintType() == 2) {
                    reportSchemaError("cvc-elt.3.2.2", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
                }
                return true;
            }
        } else {
            reportSchemaError("cvc-elt.3.1", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00be  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processAttributes(ohos.com.sun.org.apache.xerces.internal.xni.QName r27, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes r28, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl r29) {
        /*
        // Method dump skipped, instructions count: 555
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.processAttributes(ohos.com.sun.org.apache.xerces.internal.xni.QName, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processOneAttribute(ohos.com.sun.org.apache.xerces.internal.xni.QName r17, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes r18, int r19, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl r20, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl r21, ohos.com.sun.org.apache.xerces.internal.impl.xs.AttributePSVImpl r22) {
        /*
        // Method dump skipped, instructions count: 323
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.processOneAttribute(ohos.com.sun.org.apache.xerces.internal.xni.QName, ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes, int, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl, ohos.com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl, ohos.com.sun.org.apache.xerces.internal.impl.xs.AttributePSVImpl):void");
    }

    /* access modifiers changed from: package-private */
    public void addDefaultAttributes(QName qName, XMLAttributes xMLAttributes, XSAttributeGroupDecl xSAttributeGroupDecl) {
        boolean z;
        XSObjectList attributeUses = xSAttributeGroupDecl.getAttributeUses();
        int length = attributeUses.getLength();
        for (int i = 0; i < length; i++) {
            XSAttributeUseImpl xSAttributeUseImpl = (XSAttributeUseImpl) attributeUses.item(i);
            XSAttributeDecl xSAttributeDecl = xSAttributeUseImpl.fAttrDecl;
            short s = xSAttributeUseImpl.fConstraintType;
            ValidatedInfo validatedInfo = xSAttributeUseImpl.fDefault;
            if (s == 0) {
                s = xSAttributeDecl.getConstraintType();
                validatedInfo = xSAttributeDecl.fDefault;
            }
            boolean z2 = xMLAttributes.getValue(xSAttributeDecl.fTargetNamespace, xSAttributeDecl.fName) != null;
            if (xSAttributeUseImpl.fUse == 1 && !z2) {
                reportSchemaError("cvc-complex-type.4", new Object[]{qName.rawname, xSAttributeDecl.fName});
            }
            if (!z2 && s != 0) {
                QName qName2 = new QName(null, xSAttributeDecl.fName, xSAttributeDecl.fName, xSAttributeDecl.fTargetNamespace);
                String stringValue = validatedInfo != null ? validatedInfo.stringValue() : "";
                int addAttribute = xMLAttributes.addAttribute(qName2, "CDATA", stringValue);
                if (xMLAttributes instanceof XMLAttributesImpl) {
                    XMLAttributesImpl xMLAttributesImpl = (XMLAttributesImpl) xMLAttributes;
                    if (validatedInfo == null || validatedInfo.memberType == null) {
                        z = xSAttributeDecl.fType.isIDType();
                    } else {
                        z = validatedInfo.memberType.isIDType();
                    }
                    xMLAttributesImpl.setSchemaId(addAttribute, z);
                }
                if (this.fAugPSVI) {
                    Augmentations augmentations = xMLAttributes.getAugmentations(addAttribute);
                    AttributePSVImpl attributePSVImpl = new AttributePSVImpl();
                    augmentations.putItem(Constants.ATTRIBUTE_PSVI, attributePSVImpl);
                    attributePSVImpl.fDeclaration = xSAttributeDecl;
                    attributePSVImpl.fTypeDecl = xSAttributeDecl.fType;
                    attributePSVImpl.fMemberType = validatedInfo.memberType;
                    attributePSVImpl.fNormalizedValue = stringValue;
                    attributePSVImpl.fActualValue = validatedInfo.actualValue;
                    attributePSVImpl.fActualValueType = validatedInfo.actualValueType;
                    attributePSVImpl.fItemValueTypes = validatedInfo.itemValueTypes;
                    attributePSVImpl.fValidationContext = this.fValidationRoot;
                    attributePSVImpl.fValidity = 2;
                    attributePSVImpl.fValidationAttempted = 2;
                    attributePSVImpl.fSpecified = true;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void processElementContent(QName qName) {
        XSElementDecl xSElementDecl = this.fCurrentElemDecl;
        if (xSElementDecl != null && xSElementDecl.fDefault != null && !this.fSawText && !this.fSubElement && !this.fNil) {
            String stringValue = this.fCurrentElemDecl.fDefault.stringValue();
            int length = stringValue.length();
            if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < length) {
                this.fNormalizedStr.ch = new char[length];
            }
            stringValue.getChars(0, length, this.fNormalizedStr.ch, 0);
            XMLString xMLString = this.fNormalizedStr;
            xMLString.offset = 0;
            xMLString.length = length;
            this.fDefaultValue = xMLString;
        }
        this.fValidatedInfo.normalizedValue = null;
        if (this.fNil && (this.fSubElement || this.fSawText)) {
            reportSchemaError("cvc-elt.3.2.1", new Object[]{qName.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
        }
        this.fValidatedInfo.reset();
        XSElementDecl xSElementDecl2 = this.fCurrentElemDecl;
        if (xSElementDecl2 == null || xSElementDecl2.getConstraintType() == 0 || this.fSubElement || this.fSawText || this.fNil) {
            Object elementLocallyValidType = elementLocallyValidType(qName, this.fBuffer);
            XSElementDecl xSElementDecl3 = this.fCurrentElemDecl;
            if (xSElementDecl3 != null && xSElementDecl3.getConstraintType() == 2 && !this.fNil) {
                String stringBuffer = this.fBuffer.toString();
                if (this.fSubElement) {
                    reportSchemaError("cvc-elt.5.2.2.1", new Object[]{qName.rawname});
                }
                if (this.fCurrentType.getTypeCategory() == 15) {
                    XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl) this.fCurrentType;
                    if (xSComplexTypeDecl.fContentType == 3) {
                        if (!this.fCurrentElemDecl.fDefault.normalizedValue.equals(stringBuffer)) {
                            reportSchemaError("cvc-elt.5.2.2.2.1", new Object[]{qName.rawname, stringBuffer, this.fCurrentElemDecl.fDefault.normalizedValue});
                        }
                    } else if (xSComplexTypeDecl.fContentType == 1 && elementLocallyValidType != null && (!isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) || !elementLocallyValidType.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
                        reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{qName.rawname, stringBuffer, this.fCurrentElemDecl.fDefault.stringValue()});
                    }
                } else if (this.fCurrentType.getTypeCategory() == 16 && elementLocallyValidType != null && (!isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) || !elementLocallyValidType.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
                    reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{qName.rawname, stringBuffer, this.fCurrentElemDecl.fDefault.stringValue()});
                }
            }
        } else {
            if (this.fCurrentType != this.fCurrentElemDecl.fType && XSConstraints.ElementDefaultValidImmediate(this.fCurrentType, this.fCurrentElemDecl.fDefault.stringValue(), this.fState4XsiType, null) == null) {
                reportSchemaError("cvc-elt.5.1.1", new Object[]{qName.rawname, this.fCurrentType.getName(), this.fCurrentElemDecl.fDefault.stringValue()});
            }
            elementLocallyValidType(qName, this.fCurrentElemDecl.fDefault.stringValue());
        }
        if (this.fDefaultValue == null && this.fNormalizeData && this.fDocumentHandler != null && this.fUnionType) {
            String str = this.fValidatedInfo.normalizedValue;
            if (str == null) {
                str = this.fBuffer.toString();
            }
            int length2 = str.length();
            if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < length2) {
                this.fNormalizedStr.ch = new char[length2];
            }
            str.getChars(0, length2, this.fNormalizedStr.ch, 0);
            XMLString xMLString2 = this.fNormalizedStr;
            xMLString2.offset = 0;
            xMLString2.length = length2;
            this.fDocumentHandler.characters(xMLString2, null);
        }
    }

    /* access modifiers changed from: package-private */
    public Object elementLocallyValidType(QName qName, Object obj) {
        XSTypeDefinition xSTypeDefinition = this.fCurrentType;
        if (xSTypeDefinition == null) {
            return null;
        }
        if (xSTypeDefinition.getTypeCategory() != 16) {
            return elementLocallyValidComplexType(qName, obj);
        }
        if (this.fSubElement) {
            reportSchemaError("cvc-type.3.1.2", new Object[]{qName.rawname});
        }
        if (this.fNil) {
            return null;
        }
        XSSimpleType xSSimpleType = (XSSimpleType) this.fCurrentType;
        try {
            if (!this.fNormalizeData || this.fUnionType) {
                this.fValidationState.setNormalizationRequired(true);
            }
            return xSSimpleType.validate(obj, this.fValidationState, this.fValidatedInfo);
        } catch (InvalidDatatypeValueException e) {
            reportSchemaError(e.getKey(), e.getArgs());
            reportSchemaError("cvc-type.3.1.3", new Object[]{qName.rawname, obj});
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public Object elementLocallyValidComplexType(QName qName, Object obj) {
        XSComplexTypeDecl xSComplexTypeDecl = (XSComplexTypeDecl) this.fCurrentType;
        Object obj2 = null;
        if (!this.fNil) {
            if (xSComplexTypeDecl.fContentType == 0 && (this.fSubElement || this.fSawText)) {
                reportSchemaError("cvc-complex-type.2.1", new Object[]{qName.rawname});
            } else if (xSComplexTypeDecl.fContentType == 1) {
                if (this.fSubElement) {
                    reportSchemaError("cvc-complex-type.2.2", new Object[]{qName.rawname});
                }
                XSSimpleType xSSimpleType = xSComplexTypeDecl.fXSSimpleType;
                try {
                    if (!this.fNormalizeData || this.fUnionType) {
                        this.fValidationState.setNormalizationRequired(true);
                    }
                    obj2 = xSSimpleType.validate(obj, this.fValidationState, this.fValidatedInfo);
                } catch (InvalidDatatypeValueException e) {
                    reportSchemaError(e.getKey(), e.getArgs());
                    reportSchemaError("cvc-complex-type.2.2", new Object[]{qName.rawname});
                }
            } else if (xSComplexTypeDecl.fContentType == 2 && this.fSawCharacters) {
                reportSchemaError("cvc-complex-type.2.3", new Object[]{qName.rawname});
            }
            if (xSComplexTypeDecl.fContentType == 2 || xSComplexTypeDecl.fContentType == 3) {
                int[] iArr = this.fCurrCMState;
                if (iArr[0] < 0 || this.fCurrentCM.endContentModel(iArr)) {
                    ArrayList checkMinMaxBounds = this.fCurrentCM.checkMinMaxBounds();
                    if (checkMinMaxBounds != null) {
                        for (int i = 0; i < checkMinMaxBounds.size(); i += 2) {
                            reportSchemaError((String) checkMinMaxBounds.get(i), new Object[]{qName.rawname, checkMinMaxBounds.get(i + 1)});
                        }
                    }
                } else {
                    reportSchemaError("cvc-complex-type.2.4.b", new Object[]{qName.rawname, expectedStr(this.fCurrentCM.whatCanGoHere(this.fCurrCMState))});
                }
            }
        }
        return obj2;
    }

    /* access modifiers changed from: package-private */
    public void reportSchemaError(String str, Object[] objArr) {
        if (this.fDoValidation) {
            this.fXSIErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, str, objArr, 1);
        }
    }

    private boolean isComparable(ValidatedInfo validatedInfo, ValidatedInfo validatedInfo2) {
        short convertToPrimitiveKind = convertToPrimitiveKind(validatedInfo.actualValueType);
        short convertToPrimitiveKind2 = convertToPrimitiveKind(validatedInfo2.actualValueType);
        if (convertToPrimitiveKind != convertToPrimitiveKind2) {
            return (convertToPrimitiveKind == 1 && convertToPrimitiveKind2 == 2) || (convertToPrimitiveKind == 2 && convertToPrimitiveKind2 == 1);
        }
        if (convertToPrimitiveKind == 44 || convertToPrimitiveKind == 43) {
            ShortList shortList = validatedInfo.itemValueTypes;
            ShortList shortList2 = validatedInfo2.itemValueTypes;
            int length = shortList != null ? shortList.getLength() : 0;
            if (length != (shortList2 != null ? shortList2.getLength() : 0)) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                short convertToPrimitiveKind3 = convertToPrimitiveKind(shortList.item(i));
                short convertToPrimitiveKind4 = convertToPrimitiveKind(shortList2.item(i));
                if (!(convertToPrimitiveKind3 == convertToPrimitiveKind4 || ((convertToPrimitiveKind3 == 1 && convertToPrimitiveKind4 == 2) || (convertToPrimitiveKind3 == 2 && convertToPrimitiveKind4 == 1)))) {
                    return false;
                }
            }
        }
        return true;
    }

    private String expectedStr(Vector vector) {
        StringBuffer stringBuffer = new StringBuffer("{");
        int size = vector.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(vector.elementAt(i).toString());
        }
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    public static class XPathMatcherStack {
        protected IntStack fContextStack = new IntStack();
        protected XPathMatcher[] fMatchers = new XPathMatcher[4];
        protected int fMatchersCount;

        public void clear() {
            for (int i = 0; i < this.fMatchersCount; i++) {
                this.fMatchers[i] = null;
            }
            this.fMatchersCount = 0;
            this.fContextStack.clear();
        }

        public int size() {
            return this.fContextStack.size();
        }

        public int getMatcherCount() {
            return this.fMatchersCount;
        }

        public void addMatcher(XPathMatcher xPathMatcher) {
            ensureMatcherCapacity();
            XPathMatcher[] xPathMatcherArr = this.fMatchers;
            int i = this.fMatchersCount;
            this.fMatchersCount = i + 1;
            xPathMatcherArr[i] = xPathMatcher;
        }

        public XPathMatcher getMatcherAt(int i) {
            return this.fMatchers[i];
        }

        public void pushContext() {
            this.fContextStack.push(this.fMatchersCount);
        }

        public void popContext() {
            this.fMatchersCount = this.fContextStack.pop();
        }

        private void ensureMatcherCapacity() {
            int i = this.fMatchersCount;
            XPathMatcher[] xPathMatcherArr = this.fMatchers;
            if (i == xPathMatcherArr.length) {
                XPathMatcher[] xPathMatcherArr2 = new XPathMatcher[(xPathMatcherArr.length * 2)];
                System.arraycopy(xPathMatcherArr, 0, xPathMatcherArr2, 0, xPathMatcherArr.length);
                this.fMatchers = xPathMatcherArr2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public abstract class ValueStoreBase implements ValueStore {
        protected int fFieldCount = 0;
        protected Field[] fFields = null;
        protected IdentityConstraint fIdentityConstraint;
        private ShortList fItemValueType = null;
        public Vector fItemValueTypes = null;
        private int fItemValueTypesLength = 0;
        protected ShortList[] fLocalItemValueTypes = null;
        protected short[] fLocalValueTypes = null;
        protected Object[] fLocalValues = null;
        final StringBuffer fTempBuffer = new StringBuffer();
        private boolean fUseItemValueTypeVector = false;
        private boolean fUseValueTypeVector = false;
        private short fValueType = 0;
        public ShortVector fValueTypes = null;
        private int fValueTypesLength = 0;
        public final Vector fValues = new Vector();
        protected int fValuesCount;

        /* access modifiers changed from: protected */
        public void checkDuplicateValues() {
        }

        public void endDocument() {
        }

        public void endDocumentFragment() {
        }

        protected ValueStoreBase(IdentityConstraint identityConstraint) {
            this.fIdentityConstraint = identityConstraint;
            this.fFieldCount = this.fIdentityConstraint.getFieldCount();
            int i = this.fFieldCount;
            this.fFields = new Field[i];
            this.fLocalValues = new Object[i];
            this.fLocalValueTypes = new short[i];
            this.fLocalItemValueTypes = new ShortList[i];
            for (int i2 = 0; i2 < this.fFieldCount; i2++) {
                this.fFields[i2] = this.fIdentityConstraint.getFieldAt(i2);
            }
        }

        public void clear() {
            this.fValuesCount = 0;
            this.fUseValueTypeVector = false;
            this.fValueTypesLength = 0;
            this.fValueType = 0;
            this.fUseItemValueTypeVector = false;
            this.fItemValueTypesLength = 0;
            this.fItemValueType = null;
            this.fValues.setSize(0);
            ShortVector shortVector = this.fValueTypes;
            if (shortVector != null) {
                shortVector.clear();
            }
            Vector vector = this.fItemValueTypes;
            if (vector != null) {
                vector.setSize(0);
            }
        }

        public void append(ValueStoreBase valueStoreBase) {
            for (int i = 0; i < valueStoreBase.fValues.size(); i++) {
                this.fValues.addElement(valueStoreBase.fValues.elementAt(i));
            }
        }

        public void startValueScope() {
            this.fValuesCount = 0;
            for (int i = 0; i < this.fFieldCount; i++) {
                this.fLocalValues[i] = null;
                this.fLocalValueTypes[i] = 0;
                this.fLocalItemValueTypes[i] = null;
            }
        }

        public void endValueScope() {
            int i = this.fValuesCount;
            if (i == 0) {
                if (this.fIdentityConstraint.getCategory() == 1) {
                    String elementName = this.fIdentityConstraint.getElementName();
                    String identityConstraintName = this.fIdentityConstraint.getIdentityConstraintName();
                    XMLSchemaValidator.this.reportSchemaError("AbsentKeyValue", new Object[]{elementName, identityConstraintName});
                }
            } else if (i != this.fFieldCount && this.fIdentityConstraint.getCategory() == 1) {
                IdentityConstraint identityConstraint = this.fIdentityConstraint;
                UniqueOrKey uniqueOrKey = (UniqueOrKey) identityConstraint;
                String elementName2 = identityConstraint.getElementName();
                String identityConstraintName2 = uniqueOrKey.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("KeyNotEnoughValues", new Object[]{elementName2, identityConstraintName2});
            }
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore
        public void reportError(String str, Object[] objArr) {
            XMLSchemaValidator.this.reportSchemaError(str, objArr);
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore
        public void addValue(Field field, Object obj, short s, ShortList shortList) {
            int i = this.fFieldCount - 1;
            while (i > -1 && this.fFields[i] != field) {
                i--;
            }
            if (i == -1) {
                XMLSchemaValidator.this.reportSchemaError("UnknownField", new Object[]{field.toString(), this.fIdentityConstraint.getElementName(), this.fIdentityConstraint.getIdentityConstraintName()});
                return;
            }
            if (Boolean.TRUE != XMLSchemaValidator.this.mayMatch(field)) {
                XMLSchemaValidator.this.reportSchemaError("FieldMultipleMatch", new Object[]{field.toString(), this.fIdentityConstraint.getIdentityConstraintName()});
            } else {
                this.fValuesCount++;
            }
            this.fLocalValues[i] = obj;
            this.fLocalValueTypes[i] = s;
            this.fLocalItemValueTypes[i] = shortList;
            if (this.fValuesCount == this.fFieldCount) {
                checkDuplicateValues();
                for (int i2 = 0; i2 < this.fFieldCount; i2++) {
                    this.fValues.addElement(this.fLocalValues[i2]);
                    addValueType(this.fLocalValueTypes[i2]);
                    addItemValueType(this.fLocalItemValueTypes[i2]);
                }
            }
        }

        public boolean contains() {
            int size = this.fValues.size();
            int i = 0;
            while (i < size) {
                int i2 = this.fFieldCount + i;
                int i3 = i;
                for (int i4 = 0; i4 < this.fFieldCount; i4++) {
                    Object obj = this.fLocalValues[i4];
                    Object elementAt = this.fValues.elementAt(i3);
                    short s = this.fLocalValueTypes[i4];
                    short valueTypeAt = getValueTypeAt(i3);
                    if (obj != null && elementAt != null && s == valueTypeAt && obj.equals(elementAt)) {
                        if (s == 44 || s == 43) {
                            ShortList shortList = this.fLocalItemValueTypes[i4];
                            ShortList itemValueTypeAt = getItemValueTypeAt(i3);
                            if (!(shortList == null || itemValueTypeAt == null || !shortList.equals(itemValueTypeAt))) {
                            }
                        }
                        i3++;
                    }
                    i = i2;
                }
                return true;
            }
            return false;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0092, code lost:
            r7 = r7 + r11;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int contains(ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase r17) {
            /*
            // Method dump skipped, instructions count: 151
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase.contains(ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator$ValueStoreBase):int");
        }

        /* access modifiers changed from: protected */
        public String toString(Object[] objArr) {
            int length = objArr.length;
            if (length == 0) {
                return "";
            }
            this.fTempBuffer.setLength(0);
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    this.fTempBuffer.append(TelephoneNumberUtils.PAUSE);
                }
                this.fTempBuffer.append(objArr[i]);
            }
            return this.fTempBuffer.toString();
        }

        /* access modifiers changed from: protected */
        public String toString(Vector vector, int i, int i2) {
            if (i2 == 0) {
                return "";
            }
            if (i2 == 1) {
                return String.valueOf(vector.elementAt(i));
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i3 = 0; i3 < i2; i3++) {
                if (i3 > 0) {
                    stringBuffer.append(TelephoneNumberUtils.PAUSE);
                }
                stringBuffer.append(vector.elementAt(i + i3));
            }
            return stringBuffer.toString();
        }

        public String toString() {
            String obj = super.toString();
            int lastIndexOf = obj.lastIndexOf(36);
            if (lastIndexOf != -1) {
                obj = obj.substring(lastIndexOf + 1);
            }
            int lastIndexOf2 = obj.lastIndexOf(46);
            if (lastIndexOf2 != -1) {
                obj = obj.substring(lastIndexOf2 + 1);
            }
            return obj + '[' + this.fIdentityConstraint + ']';
        }

        private void addValueType(short s) {
            if (this.fUseValueTypeVector) {
                this.fValueTypes.add(s);
                return;
            }
            int i = this.fValueTypesLength;
            this.fValueTypesLength = i + 1;
            if (i == 0) {
                this.fValueType = s;
            } else if (this.fValueType != s) {
                this.fUseValueTypeVector = true;
                if (this.fValueTypes == null) {
                    this.fValueTypes = new ShortVector(this.fValueTypesLength * 2);
                }
                for (int i2 = 1; i2 < this.fValueTypesLength; i2++) {
                    this.fValueTypes.add(this.fValueType);
                }
                this.fValueTypes.add(s);
            }
        }

        private short getValueTypeAt(int i) {
            if (this.fUseValueTypeVector) {
                return this.fValueTypes.valueAt(i);
            }
            return this.fValueType;
        }

        private boolean valueTypeContains(short s) {
            if (this.fUseValueTypeVector) {
                return this.fValueTypes.contains(s);
            }
            return this.fValueType == s;
        }

        private void addItemValueType(ShortList shortList) {
            if (this.fUseItemValueTypeVector) {
                this.fItemValueTypes.add(shortList);
                return;
            }
            int i = this.fItemValueTypesLength;
            this.fItemValueTypesLength = i + 1;
            if (i == 0) {
                this.fItemValueType = shortList;
                return;
            }
            ShortList shortList2 = this.fItemValueType;
            if (shortList2 == shortList) {
                return;
            }
            if (shortList2 == null || !shortList2.equals(shortList)) {
                this.fUseItemValueTypeVector = true;
                if (this.fItemValueTypes == null) {
                    this.fItemValueTypes = new Vector(this.fItemValueTypesLength * 2);
                }
                for (int i2 = 1; i2 < this.fItemValueTypesLength; i2++) {
                    this.fItemValueTypes.add(this.fItemValueType);
                }
                this.fItemValueTypes.add(shortList);
            }
        }

        private ShortList getItemValueTypeAt(int i) {
            if (this.fUseItemValueTypeVector) {
                return (ShortList) this.fItemValueTypes.elementAt(i);
            }
            return this.fItemValueType;
        }

        private boolean itemValueTypeContains(ShortList shortList) {
            if (this.fUseItemValueTypeVector) {
                return this.fItemValueTypes.contains(shortList);
            }
            ShortList shortList2 = this.fItemValueType;
            return shortList2 == shortList || (shortList2 != null && shortList2.equals(shortList));
        }
    }

    /* access modifiers changed from: protected */
    public class UniqueValueStore extends ValueStoreBase {
        public UniqueValueStore(UniqueOrKey uniqueOrKey) {
            super(uniqueOrKey);
        }

        /* access modifiers changed from: protected */
        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        public void checkDuplicateValues() {
            if (contains()) {
                String uniqueValueStore = toString(this.fLocalValues);
                String elementName = this.fIdentityConstraint.getElementName();
                String identityConstraintName = this.fIdentityConstraint.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("DuplicateUnique", new Object[]{uniqueValueStore, elementName, identityConstraintName});
            }
        }
    }

    /* access modifiers changed from: protected */
    public class KeyValueStore extends ValueStoreBase {
        public KeyValueStore(UniqueOrKey uniqueOrKey) {
            super(uniqueOrKey);
        }

        /* access modifiers changed from: protected */
        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        public void checkDuplicateValues() {
            if (contains()) {
                String keyValueStore = toString(this.fLocalValues);
                String elementName = this.fIdentityConstraint.getElementName();
                String identityConstraintName = this.fIdentityConstraint.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("DuplicateKey", new Object[]{keyValueStore, elementName, identityConstraintName});
            }
        }
    }

    /* access modifiers changed from: protected */
    public class KeyRefValueStore extends ValueStoreBase {
        protected ValueStoreBase fKeyValueStore;

        public KeyRefValueStore(KeyRef keyRef, KeyValueStore keyValueStore) {
            super(keyRef);
            this.fKeyValueStore = keyValueStore;
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        public void endDocumentFragment() {
            super.endDocumentFragment();
            this.fKeyValueStore = XMLSchemaValidator.this.fValueStoreCache.fGlobalIDConstraintMap.get(((KeyRef) this.fIdentityConstraint).getKey());
            ValueStoreBase valueStoreBase = this.fKeyValueStore;
            if (valueStoreBase == null) {
                String identityConstraint = this.fIdentityConstraint.toString();
                XMLSchemaValidator.this.reportSchemaError("KeyRefOutOfScope", new Object[]{identityConstraint});
                return;
            }
            int contains = valueStoreBase.contains(this);
            if (contains != -1) {
                String keyRefValueStore = toString(this.fValues, contains, this.fFieldCount);
                String elementName = this.fIdentityConstraint.getElementName();
                String name = this.fIdentityConstraint.getName();
                XMLSchemaValidator.this.reportSchemaError("KeyNotFound", new Object[]{name, keyRefValueStore, elementName});
            }
        }

        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        public void endDocument() {
            super.endDocument();
        }
    }

    /* access modifiers changed from: protected */
    public class ValueStoreCache {
        protected final Map<IdentityConstraint, ValueStoreBase> fGlobalIDConstraintMap = new HashMap();
        protected final Stack<Map<IdentityConstraint, ValueStoreBase>> fGlobalMapStack = new Stack<>();
        protected final Map<LocalIDKey, ValueStoreBase> fIdentityConstraint2ValueStoreMap = new HashMap();
        final LocalIDKey fLocalId = new LocalIDKey();
        protected final Vector fValueStores = new Vector();

        public ValueStoreCache() {
        }

        public void startDocument() {
            this.fValueStores.removeAllElements();
            this.fIdentityConstraint2ValueStoreMap.clear();
            this.fGlobalIDConstraintMap.clear();
            this.fGlobalMapStack.removeAllElements();
        }

        public void startElement() {
            if (this.fGlobalIDConstraintMap.size() > 0) {
                this.fGlobalMapStack.push((Map) ((HashMap) this.fGlobalIDConstraintMap).clone());
            } else {
                this.fGlobalMapStack.push(null);
            }
            this.fGlobalIDConstraintMap.clear();
        }

        public void endElement() {
            Map<IdentityConstraint, ValueStoreBase> pop;
            if (!this.fGlobalMapStack.isEmpty() && (pop = this.fGlobalMapStack.pop()) != null) {
                for (Map.Entry<IdentityConstraint, ValueStoreBase> entry : pop.entrySet()) {
                    IdentityConstraint key = entry.getKey();
                    ValueStoreBase value = entry.getValue();
                    if (value != null) {
                        ValueStoreBase valueStoreBase = this.fGlobalIDConstraintMap.get(key);
                        if (valueStoreBase == null) {
                            this.fGlobalIDConstraintMap.put(key, value);
                        } else if (valueStoreBase != value) {
                            valueStoreBase.append(value);
                        }
                    }
                }
            }
        }

        public void initValueStoresFor(XSElementDecl xSElementDecl, FieldActivator fieldActivator) {
            IdentityConstraint[] identityConstraintArr = xSElementDecl.fIDConstraints;
            int i = xSElementDecl.fIDCPos;
            for (int i2 = 0; i2 < i; i2++) {
                short category = identityConstraintArr[i2].getCategory();
                if (category == 1) {
                    UniqueOrKey uniqueOrKey = (UniqueOrKey) identityConstraintArr[i2];
                    XMLSchemaValidator xMLSchemaValidator = XMLSchemaValidator.this;
                    LocalIDKey localIDKey = new LocalIDKey(uniqueOrKey, xMLSchemaValidator.fElementDepth);
                    KeyValueStore keyValueStore = (KeyValueStore) this.fIdentityConstraint2ValueStoreMap.get(localIDKey);
                    if (keyValueStore == null) {
                        keyValueStore = new KeyValueStore(uniqueOrKey);
                        this.fIdentityConstraint2ValueStoreMap.put(localIDKey, keyValueStore);
                    } else {
                        keyValueStore.clear();
                    }
                    this.fValueStores.addElement(keyValueStore);
                    XMLSchemaValidator.this.activateSelectorFor(identityConstraintArr[i2]);
                } else if (category == 2) {
                    KeyRef keyRef = (KeyRef) identityConstraintArr[i2];
                    XMLSchemaValidator xMLSchemaValidator2 = XMLSchemaValidator.this;
                    LocalIDKey localIDKey2 = new LocalIDKey(keyRef, xMLSchemaValidator2.fElementDepth);
                    KeyRefValueStore keyRefValueStore = (KeyRefValueStore) this.fIdentityConstraint2ValueStoreMap.get(localIDKey2);
                    if (keyRefValueStore == null) {
                        keyRefValueStore = new KeyRefValueStore(keyRef, null);
                        this.fIdentityConstraint2ValueStoreMap.put(localIDKey2, keyRefValueStore);
                    } else {
                        keyRefValueStore.clear();
                    }
                    this.fValueStores.addElement(keyRefValueStore);
                    XMLSchemaValidator.this.activateSelectorFor(identityConstraintArr[i2]);
                } else if (category == 3) {
                    UniqueOrKey uniqueOrKey2 = (UniqueOrKey) identityConstraintArr[i2];
                    XMLSchemaValidator xMLSchemaValidator3 = XMLSchemaValidator.this;
                    LocalIDKey localIDKey3 = new LocalIDKey(uniqueOrKey2, xMLSchemaValidator3.fElementDepth);
                    UniqueValueStore uniqueValueStore = (UniqueValueStore) this.fIdentityConstraint2ValueStoreMap.get(localIDKey3);
                    if (uniqueValueStore == null) {
                        uniqueValueStore = new UniqueValueStore(uniqueOrKey2);
                        this.fIdentityConstraint2ValueStoreMap.put(localIDKey3, uniqueValueStore);
                    } else {
                        uniqueValueStore.clear();
                    }
                    this.fValueStores.addElement(uniqueValueStore);
                    XMLSchemaValidator.this.activateSelectorFor(identityConstraintArr[i2]);
                }
            }
        }

        public ValueStoreBase getValueStoreFor(IdentityConstraint identityConstraint, int i) {
            LocalIDKey localIDKey = this.fLocalId;
            localIDKey.fDepth = i;
            localIDKey.fId = identityConstraint;
            return this.fIdentityConstraint2ValueStoreMap.get(localIDKey);
        }

        public ValueStoreBase getGlobalValueStoreFor(IdentityConstraint identityConstraint) {
            return this.fGlobalIDConstraintMap.get(identityConstraint);
        }

        public void transplant(IdentityConstraint identityConstraint, int i) {
            LocalIDKey localIDKey = this.fLocalId;
            localIDKey.fDepth = i;
            localIDKey.fId = identityConstraint;
            ValueStoreBase valueStoreBase = this.fIdentityConstraint2ValueStoreMap.get(localIDKey);
            if (identityConstraint.getCategory() != 2) {
                ValueStoreBase valueStoreBase2 = this.fGlobalIDConstraintMap.get(identityConstraint);
                if (valueStoreBase2 != null) {
                    valueStoreBase2.append(valueStoreBase);
                    this.fGlobalIDConstraintMap.put(identityConstraint, valueStoreBase2);
                    return;
                }
                this.fGlobalIDConstraintMap.put(identityConstraint, valueStoreBase);
            }
        }

        public void endDocument() {
            int size = this.fValueStores.size();
            for (int i = 0; i < size; i++) {
                ((ValueStoreBase) this.fValueStores.elementAt(i)).endDocument();
            }
        }

        public String toString() {
            String obj = super.toString();
            int lastIndexOf = obj.lastIndexOf(36);
            if (lastIndexOf != -1) {
                return obj.substring(lastIndexOf + 1);
            }
            int lastIndexOf2 = obj.lastIndexOf(46);
            return lastIndexOf2 != -1 ? obj.substring(lastIndexOf2 + 1) : obj;
        }
    }

    /* access modifiers changed from: protected */
    public class LocalIDKey {
        public int fDepth;
        public IdentityConstraint fId;

        public LocalIDKey() {
        }

        public LocalIDKey(IdentityConstraint identityConstraint, int i) {
            this.fId = identityConstraint;
            this.fDepth = i;
        }

        public int hashCode() {
            return this.fId.hashCode() + this.fDepth;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof LocalIDKey)) {
                return false;
            }
            LocalIDKey localIDKey = (LocalIDKey) obj;
            if (localIDKey.fId == this.fId && localIDKey.fDepth == this.fDepth) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public static final class ShortVector {
        private short[] fData;
        private int fLength;

        public ShortVector() {
        }

        public ShortVector(int i) {
            this.fData = new short[i];
        }

        public int length() {
            return this.fLength;
        }

        public void add(short s) {
            ensureCapacity(this.fLength + 1);
            short[] sArr = this.fData;
            int i = this.fLength;
            this.fLength = i + 1;
            sArr[i] = s;
        }

        public short valueAt(int i) {
            return this.fData[i];
        }

        public void clear() {
            this.fLength = 0;
        }

        public boolean contains(short s) {
            for (int i = 0; i < this.fLength; i++) {
                if (this.fData[i] == s) {
                    return true;
                }
            }
            return false;
        }

        private void ensureCapacity(int i) {
            short[] sArr = this.fData;
            if (sArr == null) {
                this.fData = new short[8];
            } else if (sArr.length <= i) {
                short[] sArr2 = new short[(sArr.length * 2)];
                System.arraycopy(sArr, 0, sArr2, 0, sArr.length);
                this.fData = sArr2;
            }
        }
    }
}
