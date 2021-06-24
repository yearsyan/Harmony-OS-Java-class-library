package ohos.com.sun.org.apache.xml.internal.resolver.readers;

import java.io.IOException;
import ohos.org.xml.sax.Attributes;
import ohos.org.xml.sax.ContentHandler;
import ohos.org.xml.sax.EntityResolver;
import ohos.org.xml.sax.InputSource;
import ohos.org.xml.sax.Locator;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.helpers.DefaultHandler;

public class SAXParserHandler extends DefaultHandler {
    private ContentHandler ch = null;
    private EntityResolver er = null;

    public void setEntityResolver(EntityResolver entityResolver) {
        this.er = entityResolver;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.ch = contentHandler;
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.EntityResolver
    public InputSource resolveEntity(String str, String str2) throws SAXException {
        EntityResolver entityResolver = this.er;
        if (entityResolver != null) {
            try {
                return entityResolver.resolveEntity(str, str2);
            } catch (IOException unused) {
                System.out.println("resolveEntity threw IOException!");
            }
        }
        return null;
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.characters(cArr, i, i2);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.endDocument();
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.endElement(str, str2, str3);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void endPrefixMapping(String str) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.endPrefixMapping(str);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.ignorableWhitespace(cArr, i, i2);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void processingInstruction(String str, String str2) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.processingInstruction(str, str2);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.setDocumentLocator(locator);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void skippedEntity(String str) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.skippedEntity(str);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.startDocument();
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.startElement(str, str2, str3, attributes);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void startPrefixMapping(String str, String str2) throws SAXException {
        ContentHandler contentHandler = this.ch;
        if (contentHandler != null) {
            contentHandler.startPrefixMapping(str, str2);
        }
    }
}
