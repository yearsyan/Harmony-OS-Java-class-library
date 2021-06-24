package ohos.com.sun.org.apache.xalan.internal.xsltc.trax;

import java.io.IOException;
import ohos.com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import ohos.javax.xml.namespace.QName;
import ohos.javax.xml.stream.XMLStreamException;
import ohos.javax.xml.stream.XMLStreamReader;
import ohos.org.xml.sax.Attributes;
import ohos.org.xml.sax.ContentHandler;
import ohos.org.xml.sax.DTDHandler;
import ohos.org.xml.sax.EntityResolver;
import ohos.org.xml.sax.ErrorHandler;
import ohos.org.xml.sax.InputSource;
import ohos.org.xml.sax.Locator;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXNotRecognizedException;
import ohos.org.xml.sax.SAXNotSupportedException;
import ohos.org.xml.sax.XMLReader;
import ohos.org.xml.sax.ext.LexicalHandler;
import ohos.org.xml.sax.ext.Locator2;
import ohos.org.xml.sax.helpers.AttributesImpl;

public class StAXStream2SAX implements XMLReader, Locator {
    private LexicalHandler _lex = null;
    private ContentHandler _sax = null;
    private SAXImpl _saxImpl = null;
    private final XMLStreamReader staxStreamReader;

    private void handleAttribute() {
    }

    private void handleCDATA() {
    }

    private void handleComment() {
    }

    private void handleDTD() {
    }

    private void handleEntityDecl() {
    }

    private void handleEntityReference() {
    }

    private void handleNamespace() {
    }

    private void handleNotationDecl() {
    }

    private void handleSpace() {
    }

    @Override // ohos.org.xml.sax.Locator
    public int getColumnNumber() {
        return 0;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public boolean getFeature(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    @Override // ohos.org.xml.sax.Locator
    public int getLineNumber() {
        return 0;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public Object getProperty(String str) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }

    @Override // ohos.org.xml.sax.Locator
    public String getPublicId() {
        return null;
    }

    @Override // ohos.org.xml.sax.Locator
    public String getSystemId() {
        return null;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler dTDHandler) throws NullPointerException {
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver entityResolver) throws NullPointerException {
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler errorHandler) throws NullPointerException {
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setFeature(String str, boolean z) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setProperty(String str, Object obj) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    public StAXStream2SAX(XMLStreamReader xMLStreamReader) {
        this.staxStreamReader = xMLStreamReader;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this._sax;
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler contentHandler) throws NullPointerException {
        this._sax = contentHandler;
        if (contentHandler instanceof LexicalHandler) {
            this._lex = (LexicalHandler) contentHandler;
        }
        if (contentHandler instanceof SAXImpl) {
            this._saxImpl = (SAXImpl) contentHandler;
        }
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void parse(InputSource inputSource) throws IOException, SAXException {
        try {
            bridge();
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    public void parse() throws IOException, SAXException, XMLStreamException {
        bridge();
    }

    @Override // ohos.org.xml.sax.XMLReader
    public void parse(String str) throws IOException, SAXException {
        throw new IOException("This method is not yet implemented.");
    }

    public void bridge() throws XMLStreamException {
        int i = 0;
        try {
            int eventType = this.staxStreamReader.getEventType();
            if (eventType == 7) {
                eventType = this.staxStreamReader.next();
            }
            if (eventType != 1) {
                eventType = this.staxStreamReader.nextTag();
                if (eventType != 1) {
                    throw new IllegalStateException("The current event is not START_ELEMENT\n but" + eventType);
                }
            }
            handleStartDocument();
            do {
                switch (eventType) {
                    case 1:
                        i++;
                        handleStartElement();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 2:
                        handleEndElement();
                        i--;
                        eventType = this.staxStreamReader.next();
                        break;
                    case 3:
                        handlePI();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 4:
                        handleCharacters();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 5:
                        handleComment();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 6:
                        handleSpace();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 7:
                    case 8:
                    default:
                        throw new InternalError("processing event: " + eventType);
                    case 9:
                        handleEntityReference();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 10:
                        handleAttribute();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 11:
                        handleDTD();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 12:
                        handleCDATA();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 13:
                        handleNamespace();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 14:
                        handleNotationDecl();
                        eventType = this.staxStreamReader.next();
                        break;
                    case 15:
                        handleEntityDecl();
                        eventType = this.staxStreamReader.next();
                        break;
                }
            } while (i != 0);
            handleEndDocument();
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    private void handleEndDocument() throws SAXException {
        this._sax.endDocument();
    }

    private void handleStartDocument() throws SAXException {
        this._sax.setDocumentLocator(new Locator2() {
            /* class ohos.com.sun.org.apache.xalan.internal.xsltc.trax.StAXStream2SAX.AnonymousClass1 */

            @Override // ohos.org.xml.sax.Locator
            public int getColumnNumber() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getColumnNumber();
            }

            @Override // ohos.org.xml.sax.Locator
            public int getLineNumber() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getLineNumber();
            }

            @Override // ohos.org.xml.sax.Locator
            public String getPublicId() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getPublicId();
            }

            @Override // ohos.org.xml.sax.Locator
            public String getSystemId() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getSystemId();
            }

            @Override // ohos.org.xml.sax.ext.Locator2
            public String getXMLVersion() {
                return StAXStream2SAX.this.staxStreamReader.getVersion();
            }

            @Override // ohos.org.xml.sax.ext.Locator2
            public String getEncoding() {
                return StAXStream2SAX.this.staxStreamReader.getEncoding();
            }
        });
        this._sax.startDocument();
    }

    private void handlePI() throws XMLStreamException {
        try {
            this._sax.processingInstruction(this.staxStreamReader.getPITarget(), this.staxStreamReader.getPIData());
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    private void handleCharacters() throws XMLStreamException {
        int textLength = this.staxStreamReader.getTextLength();
        char[] cArr = new char[textLength];
        this.staxStreamReader.getTextCharacters(0, cArr, 0, textLength);
        try {
            this._sax.characters(cArr, 0, cArr.length);
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    private void handleEndElement() throws XMLStreamException {
        QName name = this.staxStreamReader.getName();
        try {
            this._sax.endElement(name.getNamespaceURI(), name.getLocalPart(), ((name.getPrefix() == null || name.getPrefix().trim().length() == 0) ? "" : name.getPrefix() + ":") + name.getLocalPart());
            for (int namespaceCount = this.staxStreamReader.getNamespaceCount() + -1; namespaceCount >= 0; namespaceCount--) {
                String namespacePrefix = this.staxStreamReader.getNamespacePrefix(namespaceCount);
                if (namespacePrefix == null) {
                    namespacePrefix = "";
                }
                this._sax.endPrefixMapping(namespacePrefix);
            }
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    private void handleStartElement() throws XMLStreamException {
        String str;
        try {
            int namespaceCount = this.staxStreamReader.getNamespaceCount();
            for (int i = 0; i < namespaceCount; i++) {
                String namespacePrefix = this.staxStreamReader.getNamespacePrefix(i);
                if (namespacePrefix == null) {
                    namespacePrefix = "";
                }
                this._sax.startPrefixMapping(namespacePrefix, this.staxStreamReader.getNamespaceURI(i));
            }
            QName name = this.staxStreamReader.getName();
            String prefix = name.getPrefix();
            if (prefix != null) {
                if (prefix.length() != 0) {
                    str = prefix + ':' + name.getLocalPart();
                    this._sax.startElement(name.getNamespaceURI(), name.getLocalPart(), str, getAttributes());
                }
            }
            str = name.getLocalPart();
            this._sax.startElement(name.getNamespaceURI(), name.getLocalPart(), str, getAttributes());
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    private Attributes getAttributes() {
        AttributesImpl attributesImpl = new AttributesImpl();
        int eventType = this.staxStreamReader.getEventType();
        if (eventType == 10 || eventType == 1) {
            for (int i = 0; i < this.staxStreamReader.getAttributeCount(); i++) {
                String attributeNamespace = this.staxStreamReader.getAttributeNamespace(i);
                if (attributeNamespace == null) {
                    attributeNamespace = "";
                }
                String attributeLocalName = this.staxStreamReader.getAttributeLocalName(i);
                String attributePrefix = this.staxStreamReader.getAttributePrefix(i);
                attributesImpl.addAttribute(attributeNamespace, attributeLocalName, (attributePrefix == null || attributePrefix.length() == 0) ? attributeLocalName : attributePrefix + ':' + attributeLocalName, this.staxStreamReader.getAttributeType(i), this.staxStreamReader.getAttributeValue(i));
            }
            return attributesImpl;
        }
        throw new InternalError("getAttributes() attempting to process: " + eventType);
    }
}
