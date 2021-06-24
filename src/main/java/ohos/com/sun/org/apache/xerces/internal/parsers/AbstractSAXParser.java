package ohos.com.sun.org.apache.xerces.internal.parsers;

import java.io.IOException;
import java.util.Locale;
import ohos.com.sun.org.apache.xerces.internal.impl.Constants;
import ohos.com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;
import ohos.com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import ohos.com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
import ohos.com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import ohos.com.sun.org.apache.xerces.internal.util.Status;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolHash;
import ohos.com.sun.org.apache.xerces.internal.util.XMLSymbols;
import ohos.com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import ohos.com.sun.org.apache.xerces.internal.xni.Augmentations;
import ohos.com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import ohos.com.sun.org.apache.xerces.internal.xni.QName;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLLocator;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import ohos.com.sun.org.apache.xerces.internal.xni.XMLString;
import ohos.com.sun.org.apache.xerces.internal.xni.XNIException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import ohos.com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import ohos.com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import ohos.com.sun.org.apache.xerces.internal.xs.PSVIProvider;
import ohos.org.xml.sax.AttributeList;
import ohos.org.xml.sax.ContentHandler;
import ohos.org.xml.sax.DTDHandler;
import ohos.org.xml.sax.DocumentHandler;
import ohos.org.xml.sax.EntityResolver;
import ohos.org.xml.sax.ErrorHandler;
import ohos.org.xml.sax.InputSource;
import ohos.org.xml.sax.Parser;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXNotRecognizedException;
import ohos.org.xml.sax.SAXNotSupportedException;
import ohos.org.xml.sax.SAXParseException;
import ohos.org.xml.sax.XMLReader;
import ohos.org.xml.sax.ext.Attributes2;
import ohos.org.xml.sax.ext.DeclHandler;
import ohos.org.xml.sax.ext.EntityResolver2;
import ohos.org.xml.sax.ext.LexicalHandler;
import ohos.org.xml.sax.ext.Locator2;
import ohos.org.xml.sax.helpers.LocatorImpl;

public abstract class AbstractSAXParser extends AbstractXMLDocumentParser implements PSVIProvider, Parser, XMLReader {
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
    private static final int BUFFER_SIZE = 20;
    protected static final String DECLARATION_HANDLER = "http://xml.org/sax/properties/declaration-handler";
    protected static final String DOM_NODE = "http://xml.org/sax/properties/dom-node";
    protected static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    private static final String[] RECOGNIZED_FEATURES = {"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/namespace-prefixes", STRING_INTERNING};
    private static final String[] RECOGNIZED_PROPERTIES = {LEXICAL_HANDLER, DECLARATION_HANDLER, DOM_NODE};
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    private final AttributesProxy fAttributesProxy = new AttributesProxy();
    private Augmentations fAugmentations = null;
    private char[] fCharBuffer = new char[20];
    protected ContentHandler fContentHandler;
    protected DTDHandler fDTDHandler;
    protected DeclHandler fDeclHandler;
    protected SymbolHash fDeclaredAttrs = null;
    protected DocumentHandler fDocumentHandler;
    protected LexicalHandler fLexicalHandler;
    protected boolean fLexicalHandlerParameterEntities = true;
    protected NamespaceContext fNamespaceContext;
    protected boolean fNamespacePrefixes = false;
    protected boolean fNamespaces;
    protected boolean fParseInProgress = false;
    protected QName fQName = new QName();
    protected boolean fResolveDTDURIs = true;
    protected boolean fStandalone;
    protected boolean fUseEntityResolver2 = true;
    protected String fVersion;
    protected boolean fXMLNSURIs = false;

    protected AbstractSAXParser(XMLParserConfiguration xMLParserConfiguration) {
        super(xMLParserConfiguration);
        xMLParserConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
        xMLParserConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
        try {
            xMLParserConfiguration.setFeature(ALLOW_UE_AND_NOTATION_EVENTS, false);
        } catch (XMLConfigurationException unused) {
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void startDocument(XMLLocator xMLLocator, String str, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException {
        this.fNamespaceContext = namespaceContext;
        try {
            if (this.fDocumentHandler != null) {
                if (xMLLocator != null) {
                    this.fDocumentHandler.setDocumentLocator(new LocatorProxy(xMLLocator));
                }
                this.fDocumentHandler.startDocument();
            }
            if (this.fContentHandler != null) {
                if (xMLLocator != null) {
                    this.fContentHandler.setDocumentLocator(new LocatorProxy(xMLLocator));
                }
                this.fContentHandler.startDocument();
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void xmlDecl(String str, String str2, String str3, Augmentations augmentations) throws XNIException {
        this.fVersion = str;
        this.fStandalone = "yes".equals(str3);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void doctypeDecl(String str, String str2, String str3, Augmentations augmentations) throws XNIException {
        this.fInDTD = true;
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.startDTD(str, str2, str3);
            }
            if (this.fDeclHandler != null) {
                this.fDeclaredAttrs = new SymbolHash();
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void startGeneralEntity(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException {
        if (augmentations != null) {
            try {
                if (Boolean.TRUE.equals(augmentations.getItem(Constants.ENTITY_SKIPPED))) {
                    if (this.fContentHandler != null) {
                        this.fContentHandler.skippedEntity(str);
                        return;
                    }
                    return;
                }
            } catch (SAXException e) {
                throw new XNIException(e);
            }
        }
        if (this.fLexicalHandler != null) {
            this.fLexicalHandler.startEntity(str);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void endGeneralEntity(String str, Augmentations augmentations) throws XNIException {
        if (augmentations != null) {
            try {
                if (Boolean.TRUE.equals(augmentations.getItem(Constants.ENTITY_SKIPPED))) {
                    return;
                }
            } catch (SAXException e) {
                throw new XNIException(e);
            }
        }
        if (this.fLexicalHandler != null) {
            this.fLexicalHandler.endEntity(str);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fAttributesProxy.setAttributes(xMLAttributes);
                this.fDocumentHandler.startElement(qName.rawname, this.fAttributesProxy);
            }
            if (this.fContentHandler != null) {
                String str = "";
                if (this.fNamespaces) {
                    startNamespaceMapping();
                    int length = xMLAttributes.getLength();
                    if (!this.fNamespacePrefixes) {
                        for (int i = length - 1; i >= 0; i--) {
                            xMLAttributes.getName(i, this.fQName);
                            if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                                xMLAttributes.removeAttributeAt(i);
                            }
                        }
                    } else if (!this.fXMLNSURIs) {
                        for (int i2 = length - 1; i2 >= 0; i2--) {
                            xMLAttributes.getName(i2, this.fQName);
                            if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                                this.fQName.prefix = str;
                                this.fQName.uri = str;
                                this.fQName.localpart = str;
                                xMLAttributes.setName(i2, this.fQName);
                            }
                        }
                    }
                }
                this.fAugmentations = augmentations;
                String str2 = qName.uri != null ? qName.uri : str;
                if (this.fNamespaces) {
                    str = qName.localpart;
                }
                this.fAttributesProxy.setAttributes(xMLAttributes);
                this.fContentHandler.startElement(str2, str, qName.rawname, this.fAttributesProxy);
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (xMLString.length != 0) {
            try {
                if (this.fDocumentHandler != null) {
                    this.fDocumentHandler.characters(xMLString.ch, xMLString.offset, xMLString.length);
                }
                if (this.fContentHandler != null) {
                    this.fContentHandler.characters(xMLString.ch, xMLString.offset, xMLString.length);
                }
            } catch (SAXException e) {
                throw new XNIException(e);
            }
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.ignorableWhitespace(xMLString.ch, xMLString.offset, xMLString.length);
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.ignorableWhitespace(xMLString.ch, xMLString.offset, xMLString.length);
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endElement(qName.rawname);
            }
            if (this.fContentHandler != null) {
                this.fAugmentations = augmentations;
                String str = "";
                String str2 = qName.uri != null ? qName.uri : str;
                if (this.fNamespaces) {
                    str = qName.localpart;
                }
                this.fContentHandler.endElement(str2, str, qName.rawname);
                if (this.fNamespaces) {
                    endNamespaceMapping();
                }
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void startCDATA(Augmentations augmentations) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.startCDATA();
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void endCDATA(Augmentations augmentations) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.endCDATA();
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.comment(xMLString.ch, 0, xMLString.length);
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void processingInstruction(String str, XMLString xMLString, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.processingInstruction(str, xMLString.toString());
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.processingInstruction(str, xMLString.toString());
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler, ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser
    public void endDocument(Augmentations augmentations) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endDocument();
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.endDocument();
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startExternalSubset(XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        startParameterEntity("[dtd]", null, null, augmentations);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endExternalSubset(Augmentations augmentations) throws XNIException {
        endParameterEntity("[dtd]", augmentations);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startParameterEntity(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException {
        if (augmentations != null) {
            try {
                if (Boolean.TRUE.equals(augmentations.getItem(Constants.ENTITY_SKIPPED))) {
                    if (this.fContentHandler != null) {
                        this.fContentHandler.skippedEntity(str);
                        return;
                    }
                    return;
                }
            } catch (SAXException e) {
                throw new XNIException(e);
            }
        }
        if (this.fLexicalHandler != null && this.fLexicalHandlerParameterEntities) {
            this.fLexicalHandler.startEntity(str);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endParameterEntity(String str, Augmentations augmentations) throws XNIException {
        if (augmentations != null) {
            try {
                if (Boolean.TRUE.equals(augmentations.getItem(Constants.ENTITY_SKIPPED))) {
                    return;
                }
            } catch (SAXException e) {
                throw new XNIException(e);
            }
        }
        if (this.fLexicalHandler != null && this.fLexicalHandlerParameterEntities) {
            this.fLexicalHandler.endEntity(str);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void elementDecl(String str, String str2, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                this.fDeclHandler.elementDecl(str, str2);
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0073 A[Catch:{ SAXException -> 0x0083 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0075 A[Catch:{ SAXException -> 0x0083 }] */
    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void attributeDecl(java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String[] r10, java.lang.String r11, ohos.com.sun.org.apache.xerces.internal.xni.XMLString r12, ohos.com.sun.org.apache.xerces.internal.xni.XMLString r13, ohos.com.sun.org.apache.xerces.internal.xni.Augmentations r14) throws ohos.com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
        // Method dump skipped, instructions count: 138
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.attributeDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, ohos.com.sun.org.apache.xerces.internal.xni.XMLString, ohos.com.sun.org.apache.xerces.internal.xni.XMLString, ohos.com.sun.org.apache.xerces.internal.xni.Augmentations):void");
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void internalEntityDecl(String str, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                this.fDeclHandler.internalEntityDecl(str, xMLString.toString());
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void externalEntityDecl(String str, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                this.fDeclHandler.externalEntityDecl(str, xMLResourceIdentifier.getPublicId(), this.fResolveDTDURIs ? xMLResourceIdentifier.getExpandedSystemId() : xMLResourceIdentifier.getLiteralSystemId());
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void unparsedEntityDecl(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDTDHandler != null) {
                this.fDTDHandler.unparsedEntityDecl(str, xMLResourceIdentifier.getPublicId(), this.fResolveDTDURIs ? xMLResourceIdentifier.getExpandedSystemId() : xMLResourceIdentifier.getLiteralSystemId(), str2);
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void notationDecl(String str, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
        try {
            if (this.fDTDHandler != null) {
                this.fDTDHandler.notationDecl(str, xMLResourceIdentifier.getPublicId(), this.fResolveDTDURIs ? xMLResourceIdentifier.getExpandedSystemId() : xMLResourceIdentifier.getLiteralSystemId());
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endDTD(Augmentations augmentations) throws XNIException {
        this.fInDTD = false;
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.endDTD();
            }
            SymbolHash symbolHash = this.fDeclaredAttrs;
            if (symbolHash != null) {
                symbolHash.clear();
            }
        } catch (SAXException e) {
            throw new XNIException(e);
        }
    }

    @Override // ohos.org.xml.sax.Parser, ohos.org.xml.sax.XMLReader
    public void parse(String str) throws SAXException, IOException {
        try {
            parse(new XMLInputSource(null, str, null));
        } catch (XMLParseException e) {
            Exception exception = e.getException();
            if (exception == null) {
                AnonymousClass1 r0 = new LocatorImpl() {
                    /* class ohos.com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.AnonymousClass1 */

                    public String getEncoding() {
                        return null;
                    }

                    public String getXMLVersion() {
                        return AbstractSAXParser.this.fVersion;
                    }
                };
                r0.setPublicId(e.getPublicId());
                r0.setSystemId(e.getExpandedSystemId());
                r0.setLineNumber(e.getLineNumber());
                r0.setColumnNumber(e.getColumnNumber());
                throw new SAXParseException(e.getMessage(), r0);
            } else if (exception instanceof SAXException) {
                throw ((SAXException) exception);
            } else if (exception instanceof IOException) {
                throw ((IOException) exception);
            } else {
                throw new SAXException(exception);
            }
        } catch (XNIException e2) {
            Exception exception2 = e2.getException();
            if (exception2 == null) {
                throw new SAXException(e2.getMessage());
            } else if (exception2 instanceof SAXException) {
                throw ((SAXException) exception2);
            } else if (exception2 instanceof IOException) {
                throw ((IOException) exception2);
            } else {
                throw new SAXException(exception2);
            }
        }
    }

    @Override // ohos.org.xml.sax.Parser, ohos.org.xml.sax.XMLReader
    public void parse(InputSource inputSource) throws SAXException, IOException {
        try {
            XMLInputSource xMLInputSource = new XMLInputSource(inputSource.getPublicId(), inputSource.getSystemId(), null);
            xMLInputSource.setByteStream(inputSource.getByteStream());
            xMLInputSource.setCharacterStream(inputSource.getCharacterStream());
            xMLInputSource.setEncoding(inputSource.getEncoding());
            parse(xMLInputSource);
        } catch (XMLParseException e) {
            Exception exception = e.getException();
            if (exception == null) {
                AnonymousClass2 r0 = new LocatorImpl() {
                    /* class ohos.com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.AnonymousClass2 */

                    public String getEncoding() {
                        return null;
                    }

                    public String getXMLVersion() {
                        return AbstractSAXParser.this.fVersion;
                    }
                };
                r0.setPublicId(e.getPublicId());
                r0.setSystemId(e.getExpandedSystemId());
                r0.setLineNumber(e.getLineNumber());
                r0.setColumnNumber(e.getColumnNumber());
                throw new SAXParseException(e.getMessage(), r0);
            } else if (exception instanceof SAXException) {
                throw ((SAXException) exception);
            } else if (exception instanceof IOException) {
                throw ((IOException) exception);
            } else {
                throw new SAXException(exception);
            }
        } catch (XNIException e2) {
            Exception exception2 = e2.getException();
            if (exception2 == null) {
                throw new SAXException(e2.getMessage());
            } else if (exception2 instanceof SAXException) {
                throw ((SAXException) exception2);
            } else if (exception2 instanceof IOException) {
                throw ((IOException) exception2);
            } else {
                throw new SAXException(exception2);
            }
        }
    }

    @Override // ohos.org.xml.sax.Parser, ohos.org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver entityResolver) {
        try {
            XMLEntityResolver xMLEntityResolver = (XMLEntityResolver) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            if (!this.fUseEntityResolver2 || !(entityResolver instanceof EntityResolver2)) {
                if (xMLEntityResolver instanceof EntityResolverWrapper) {
                    ((EntityResolverWrapper) xMLEntityResolver).setEntityResolver(entityResolver);
                } else {
                    this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(entityResolver));
                }
            } else if (xMLEntityResolver instanceof EntityResolver2Wrapper) {
                ((EntityResolver2Wrapper) xMLEntityResolver).setEntityResolver((EntityResolver2) entityResolver);
            } else {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2) entityResolver));
            }
        } catch (XMLConfigurationException unused) {
        }
    }

    @Override // ohos.org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        EntityResolver entityResolver;
        try {
            XMLEntityResolver xMLEntityResolver = (XMLEntityResolver) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            if (xMLEntityResolver == null) {
                return null;
            }
            if (xMLEntityResolver instanceof EntityResolverWrapper) {
                entityResolver = ((EntityResolverWrapper) xMLEntityResolver).getEntityResolver();
            } else if (!(xMLEntityResolver instanceof EntityResolver2Wrapper)) {
                return null;
            } else {
                entityResolver = ((EntityResolver2Wrapper) xMLEntityResolver).getEntityResolver();
            }
            return entityResolver;
        } catch (XMLConfigurationException unused) {
            return null;
        }
    }

    @Override // ohos.org.xml.sax.Parser, ohos.org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler errorHandler) {
        try {
            XMLErrorHandler xMLErrorHandler = (XMLErrorHandler) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
            if (xMLErrorHandler instanceof ErrorHandlerWrapper) {
                ((ErrorHandlerWrapper) xMLErrorHandler).setErrorHandler(errorHandler);
            } else {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(errorHandler));
            }
        } catch (XMLConfigurationException unused) {
        }
    }

    @Override // ohos.org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        try {
            XMLErrorHandler xMLErrorHandler = (XMLErrorHandler) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
            if (xMLErrorHandler == null || !(xMLErrorHandler instanceof ErrorHandlerWrapper)) {
                return null;
            }
            return ((ErrorHandlerWrapper) xMLErrorHandler).getErrorHandler();
        } catch (XMLConfigurationException unused) {
            return null;
        }
    }

    @Override // ohos.org.xml.sax.Parser
    public void setLocale(Locale locale) throws SAXException {
        this.fConfiguration.setLocale(locale);
    }

    @Override // ohos.org.xml.sax.Parser, ohos.org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler dTDHandler) {
        this.fDTDHandler = dTDHandler;
    }

    @Override // ohos.org.xml.sax.Parser
    public void setDocumentHandler(DocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler contentHandler) {
        this.fContentHandler = contentHandler;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this.fContentHandler;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (str.startsWith(Constants.SAX_FEATURE_PREFIX)) {
                int length = str.length() - 28;
                if (length == 10 && str.endsWith("namespaces")) {
                    this.fConfiguration.setFeature(str, z);
                    this.fNamespaces = z;
                    return;
                } else if (length == 18 && str.endsWith(Constants.NAMESPACE_PREFIXES_FEATURE)) {
                    this.fConfiguration.setFeature(str, z);
                    this.fNamespacePrefixes = z;
                    return;
                } else if (length != 16 || !str.endsWith(Constants.STRING_INTERNING_FEATURE)) {
                    if (length == 34 && str.endsWith(Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE)) {
                        this.fLexicalHandlerParameterEntities = z;
                        return;
                    } else if (length == 16 && str.endsWith(Constants.RESOLVE_DTD_URIS_FEATURE)) {
                        this.fResolveDTDURIs = z;
                        return;
                    } else if (length != 30 || !str.endsWith(Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE)) {
                        if (length == 10 && str.endsWith(Constants.XMLNS_URIS_FEATURE)) {
                            this.fXMLNSURIs = z;
                            return;
                        } else if (length != 20 || !str.endsWith(Constants.USE_ENTITY_RESOLVER2_FEATURE)) {
                            if ((length != 13 || !str.endsWith(Constants.IS_STANDALONE_FEATURE)) && ((length != 15 || !str.endsWith(Constants.USE_ATTRIBUTES2_FEATURE)) && (length != 12 || !str.endsWith(Constants.USE_LOCATOR2_FEATURE)))) {
                                if (length == 7) {
                                    if (!str.endsWith(Constants.XML_11_FEATURE)) {
                                    }
                                }
                            }
                            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-read-only", new Object[]{str}));
                        } else if (z != this.fUseEntityResolver2) {
                            this.fUseEntityResolver2 = z;
                            setEntityResolver(getEntityResolver());
                            return;
                        } else {
                            return;
                        }
                    } else if (z) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "true-not-supported", new Object[]{str}));
                    } else {
                        return;
                    }
                } else if (!z) {
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "false-not-supported", new Object[]{str}));
                } else {
                    return;
                }
            } else if (str.equals("http://ohos.javax.xml.XMLConstants/feature/secure-processing") && z && this.fConfiguration.getProperty("http://apache.org/xml/properties/security-manager") == null) {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager());
            }
            this.fConfiguration.setFeature(str, z);
        } catch (XMLConfigurationException e) {
            String identifier = e.getIdentifier();
            if (e.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{identifier}));
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.XMLParser, ohos.org.xml.sax.XMLReader
    public boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (str.startsWith(Constants.SAX_FEATURE_PREFIX)) {
                int length = str.length() - 28;
                if (length == 18 && str.endsWith(Constants.NAMESPACE_PREFIXES_FEATURE)) {
                    return this.fConfiguration.getFeature(str);
                }
                if (length == 16 && str.endsWith(Constants.STRING_INTERNING_FEATURE)) {
                    return true;
                }
                if (length == 13 && str.endsWith(Constants.IS_STANDALONE_FEATURE)) {
                    return this.fStandalone;
                }
                if (length == 7 && str.endsWith(Constants.XML_11_FEATURE)) {
                    return this.fConfiguration instanceof XML11Configurable;
                }
                if (length == 34 && str.endsWith(Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE)) {
                    return this.fLexicalHandlerParameterEntities;
                }
                if (length == 16 && str.endsWith(Constants.RESOLVE_DTD_URIS_FEATURE)) {
                    return this.fResolveDTDURIs;
                }
                if (length == 10 && str.endsWith(Constants.XMLNS_URIS_FEATURE)) {
                    return this.fXMLNSURIs;
                }
                if (length == 30 && str.endsWith(Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE)) {
                    return false;
                }
                if (length == 20 && str.endsWith(Constants.USE_ENTITY_RESOLVER2_FEATURE)) {
                    return this.fUseEntityResolver2;
                }
                if ((length == 15 && str.endsWith(Constants.USE_ATTRIBUTES2_FEATURE)) || (length == 12 && str.endsWith(Constants.USE_LOCATOR2_FEATURE))) {
                    return true;
                }
            }
            return this.fConfiguration.getFeature(str);
        } catch (XMLConfigurationException e) {
            String identifier = e.getIdentifier();
            if (e.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{identifier}));
        }
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (str.startsWith(Constants.SAX_PROPERTY_PREFIX)) {
                int length = str.length() - 30;
                if (length == 15 && str.endsWith(Constants.LEXICAL_HANDLER_PROPERTY)) {
                    try {
                        setLexicalHandler((LexicalHandler) obj);
                        return;
                    } catch (ClassCastException unused) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "incompatible-class", new Object[]{str, "ohos.org.xml.sax.ext.LexicalHandler"}));
                    }
                } else if (length == 19 && str.endsWith(Constants.DECLARATION_HANDLER_PROPERTY)) {
                    try {
                        setDeclHandler((DeclHandler) obj);
                        return;
                    } catch (ClassCastException unused2) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "incompatible-class", new Object[]{str, "ohos.org.xml.sax.ext.DeclHandler"}));
                    }
                } else if ((length == 8 && str.endsWith(Constants.DOM_NODE_PROPERTY)) || (length == 20 && str.endsWith(Constants.DOCUMENT_XML_VERSION_PROPERTY))) {
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-read-only", new Object[]{str}));
                }
            }
            this.fConfiguration.setProperty(str, obj);
        } catch (XMLConfigurationException e) {
            String identifier = e.getIdentifier();
            if (e.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[]{identifier}));
        }
    }

    @Override // ohos.org.xml.sax.XMLReader
    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (str.startsWith(Constants.SAX_PROPERTY_PREFIX)) {
                int length = str.length() - 30;
                if (length == 20 && str.endsWith(Constants.DOCUMENT_XML_VERSION_PROPERTY)) {
                    return this.fVersion;
                }
                if (length == 15 && str.endsWith(Constants.LEXICAL_HANDLER_PROPERTY)) {
                    return getLexicalHandler();
                }
                if (length == 19 && str.endsWith(Constants.DECLARATION_HANDLER_PROPERTY)) {
                    return getDeclHandler();
                }
                if (length == 8) {
                    if (str.endsWith(Constants.DOM_NODE_PROPERTY)) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "dom-node-read-not-supported", null));
                    }
                }
            }
            return this.fConfiguration.getProperty(str);
        } catch (XMLConfigurationException e) {
            String identifier = e.getIdentifier();
            if (e.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[]{identifier}));
        }
    }

    /* access modifiers changed from: protected */
    public void setDeclHandler(DeclHandler declHandler) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (!this.fParseInProgress) {
            this.fDeclHandler = declHandler;
            return;
        }
        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{DECLARATION_HANDLER}));
    }

    /* access modifiers changed from: protected */
    public DeclHandler getDeclHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.fDeclHandler;
    }

    /* access modifiers changed from: protected */
    public void setLexicalHandler(LexicalHandler lexicalHandler) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (!this.fParseInProgress) {
            this.fLexicalHandler = lexicalHandler;
            return;
        }
        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{LEXICAL_HANDLER}));
    }

    /* access modifiers changed from: protected */
    public LexicalHandler getLexicalHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.fLexicalHandler;
    }

    /* access modifiers changed from: protected */
    public final void startNamespaceMapping() throws SAXException {
        int declaredPrefixCount = this.fNamespaceContext.getDeclaredPrefixCount();
        if (declaredPrefixCount > 0) {
            for (int i = 0; i < declaredPrefixCount; i++) {
                String declaredPrefixAt = this.fNamespaceContext.getDeclaredPrefixAt(i);
                String uri = this.fNamespaceContext.getURI(declaredPrefixAt);
                ContentHandler contentHandler = this.fContentHandler;
                if (uri == null) {
                    uri = "";
                }
                contentHandler.startPrefixMapping(declaredPrefixAt, uri);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void endNamespaceMapping() throws SAXException {
        int declaredPrefixCount = this.fNamespaceContext.getDeclaredPrefixCount();
        if (declaredPrefixCount > 0) {
            for (int i = 0; i < declaredPrefixCount; i++) {
                this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(i));
            }
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, ohos.com.sun.org.apache.xerces.internal.parsers.XMLParser
    public void reset() throws XNIException {
        super.reset();
        this.fInDTD = false;
        this.fVersion = "1.0";
        this.fStandalone = false;
        this.fNamespaces = this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
        this.fNamespacePrefixes = this.fConfiguration.getFeature("http://xml.org/sax/features/namespace-prefixes");
        this.fAugmentations = null;
        this.fDeclaredAttrs = null;
    }

    protected class LocatorProxy implements Locator2 {
        protected XMLLocator fLocator;

        public LocatorProxy(XMLLocator xMLLocator) {
            this.fLocator = xMLLocator;
        }

        @Override // ohos.org.xml.sax.Locator
        public String getPublicId() {
            return this.fLocator.getPublicId();
        }

        @Override // ohos.org.xml.sax.Locator
        public String getSystemId() {
            return this.fLocator.getExpandedSystemId();
        }

        @Override // ohos.org.xml.sax.Locator
        public int getLineNumber() {
            return this.fLocator.getLineNumber();
        }

        @Override // ohos.org.xml.sax.Locator
        public int getColumnNumber() {
            return this.fLocator.getColumnNumber();
        }

        @Override // ohos.org.xml.sax.ext.Locator2
        public String getXMLVersion() {
            return this.fLocator.getXMLVersion();
        }

        @Override // ohos.org.xml.sax.ext.Locator2
        public String getEncoding() {
            return this.fLocator.getEncoding();
        }
    }

    protected static final class AttributesProxy implements AttributeList, Attributes2 {
        protected XMLAttributes fAttributes;

        protected AttributesProxy() {
        }

        public void setAttributes(XMLAttributes xMLAttributes) {
            this.fAttributes = xMLAttributes;
        }

        @Override // ohos.org.xml.sax.AttributeList, ohos.org.xml.sax.Attributes
        public int getLength() {
            return this.fAttributes.getLength();
        }

        @Override // ohos.org.xml.sax.AttributeList
        public String getName(int i) {
            return this.fAttributes.getQName(i);
        }

        @Override // ohos.org.xml.sax.Attributes
        public String getQName(int i) {
            return this.fAttributes.getQName(i);
        }

        @Override // ohos.org.xml.sax.Attributes
        public String getURI(int i) {
            String uri = this.fAttributes.getURI(i);
            return uri != null ? uri : "";
        }

        @Override // ohos.org.xml.sax.Attributes
        public String getLocalName(int i) {
            return this.fAttributes.getLocalName(i);
        }

        @Override // ohos.org.xml.sax.AttributeList, ohos.org.xml.sax.Attributes
        public String getType(int i) {
            return this.fAttributes.getType(i);
        }

        @Override // ohos.org.xml.sax.AttributeList, ohos.org.xml.sax.Attributes
        public String getType(String str) {
            return this.fAttributes.getType(str);
        }

        @Override // ohos.org.xml.sax.Attributes
        public String getType(String str, String str2) {
            if (str.equals("")) {
                return this.fAttributes.getType(null, str2);
            }
            return this.fAttributes.getType(str, str2);
        }

        @Override // ohos.org.xml.sax.AttributeList, ohos.org.xml.sax.Attributes
        public String getValue(int i) {
            return this.fAttributes.getValue(i);
        }

        @Override // ohos.org.xml.sax.AttributeList, ohos.org.xml.sax.Attributes
        public String getValue(String str) {
            return this.fAttributes.getValue(str);
        }

        @Override // ohos.org.xml.sax.Attributes
        public String getValue(String str, String str2) {
            if (str.equals("")) {
                return this.fAttributes.getValue(null, str2);
            }
            return this.fAttributes.getValue(str, str2);
        }

        @Override // ohos.org.xml.sax.Attributes
        public int getIndex(String str) {
            return this.fAttributes.getIndex(str);
        }

        @Override // ohos.org.xml.sax.Attributes
        public int getIndex(String str, String str2) {
            if (str.equals("")) {
                return this.fAttributes.getIndex(null, str2);
            }
            return this.fAttributes.getIndex(str, str2);
        }

        @Override // ohos.org.xml.sax.ext.Attributes2
        public boolean isDeclared(int i) {
            if (i >= 0 && i < this.fAttributes.getLength()) {
                return Boolean.TRUE.equals(this.fAttributes.getAugmentations(i).getItem(Constants.ATTRIBUTE_DECLARED));
            }
            throw new ArrayIndexOutOfBoundsException(i);
        }

        @Override // ohos.org.xml.sax.ext.Attributes2
        public boolean isDeclared(String str) {
            int index = getIndex(str);
            if (index != -1) {
                return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem(Constants.ATTRIBUTE_DECLARED));
            }
            throw new IllegalArgumentException(str);
        }

        @Override // ohos.org.xml.sax.ext.Attributes2
        public boolean isDeclared(String str, String str2) {
            int index = getIndex(str, str2);
            if (index != -1) {
                return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem(Constants.ATTRIBUTE_DECLARED));
            }
            throw new IllegalArgumentException(str2);
        }

        @Override // ohos.org.xml.sax.ext.Attributes2
        public boolean isSpecified(int i) {
            if (i >= 0 && i < this.fAttributes.getLength()) {
                return this.fAttributes.isSpecified(i);
            }
            throw new ArrayIndexOutOfBoundsException(i);
        }

        @Override // ohos.org.xml.sax.ext.Attributes2
        public boolean isSpecified(String str) {
            int index = getIndex(str);
            if (index != -1) {
                return this.fAttributes.isSpecified(index);
            }
            throw new IllegalArgumentException(str);
        }

        @Override // ohos.org.xml.sax.ext.Attributes2
        public boolean isSpecified(String str, String str2) {
            int index = getIndex(str, str2);
            if (index != -1) {
                return this.fAttributes.isSpecified(index);
            }
            throw new IllegalArgumentException(str2);
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public ElementPSVI getElementPSVI() {
        Augmentations augmentations = this.fAugmentations;
        if (augmentations != null) {
            return (ElementPSVI) augmentations.getItem(Constants.ELEMENT_PSVI);
        }
        return null;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVI(int i) {
        return (AttributePSVI) this.fAttributesProxy.fAttributes.getAugmentations(i).getItem(Constants.ATTRIBUTE_PSVI);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVIByName(String str, String str2) {
        return (AttributePSVI) this.fAttributesProxy.fAttributes.getAugmentations(str, str2).getItem(Constants.ATTRIBUTE_PSVI);
    }
}
