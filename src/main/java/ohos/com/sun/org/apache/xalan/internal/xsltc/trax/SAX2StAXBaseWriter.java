package ohos.com.sun.org.apache.xalan.internal.xsltc.trax;

import java.util.Vector;
import ohos.javax.xml.stream.Location;
import ohos.javax.xml.stream.XMLReporter;
import ohos.javax.xml.stream.XMLStreamException;
import ohos.org.xml.sax.Attributes;
import ohos.org.xml.sax.Locator;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXParseException;
import ohos.org.xml.sax.ext.LexicalHandler;
import ohos.org.xml.sax.helpers.DefaultHandler;

public abstract class SAX2StAXBaseWriter extends DefaultHandler implements LexicalHandler {
    protected StringBuffer CDATABuffer;
    protected Locator docLocator;
    protected boolean isCDATA;
    protected Vector namespaces;
    protected XMLReporter reporter;

    @Override // ohos.org.xml.sax.ext.LexicalHandler
    public void comment(char[] cArr, int i, int i2) throws SAXException {
    }

    @Override // ohos.org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // ohos.org.xml.sax.ext.LexicalHandler
    public void endEntity(String str) throws SAXException {
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void endPrefixMapping(String str) throws SAXException {
    }

    @Override // ohos.org.xml.sax.ext.LexicalHandler
    public void startDTD(String str, String str2, String str3) throws SAXException {
    }

    @Override // ohos.org.xml.sax.ext.LexicalHandler
    public void startEntity(String str) throws SAXException {
    }

    public SAX2StAXBaseWriter() {
    }

    public SAX2StAXBaseWriter(XMLReporter xMLReporter) {
        this.reporter = xMLReporter;
    }

    public void setXMLReporter(XMLReporter xMLReporter) {
        this.reporter = xMLReporter;
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this.docLocator = locator;
    }

    public Location getCurrentLocation() {
        Locator locator = this.docLocator;
        if (locator != null) {
            return new SAXLocation(locator);
        }
        return null;
    }

    @Override // ohos.org.xml.sax.ErrorHandler, ohos.org.xml.sax.helpers.DefaultHandler
    public void error(SAXParseException sAXParseException) throws SAXException {
        reportException("ERROR", sAXParseException);
    }

    @Override // ohos.org.xml.sax.ErrorHandler, ohos.org.xml.sax.helpers.DefaultHandler
    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        reportException("FATAL", sAXParseException);
    }

    @Override // ohos.org.xml.sax.ErrorHandler, ohos.org.xml.sax.helpers.DefaultHandler
    public void warning(SAXParseException sAXParseException) throws SAXException {
        reportException("WARNING", sAXParseException);
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.namespaces = new Vector(2);
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        this.namespaces = null;
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        this.namespaces = null;
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        this.namespaces = null;
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void startPrefixMapping(String str, String str2) throws SAXException {
        if (str == null) {
            str = "";
        } else if (str.equals("xml")) {
            return;
        }
        if (this.namespaces == null) {
            this.namespaces = new Vector(2);
        }
        this.namespaces.addElement(str);
        this.namespaces.addElement(str2);
    }

    @Override // ohos.org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        this.isCDATA = true;
        StringBuffer stringBuffer = this.CDATABuffer;
        if (stringBuffer == null) {
            this.CDATABuffer = new StringBuffer();
        } else {
            stringBuffer.setLength(0);
        }
    }

    @Override // ohos.org.xml.sax.helpers.DefaultHandler, ohos.org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        if (this.isCDATA) {
            this.CDATABuffer.append(cArr, i, i2);
        }
    }

    @Override // ohos.org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this.isCDATA = false;
        this.CDATABuffer.setLength(0);
    }

    /* access modifiers changed from: protected */
    public void reportException(String str, SAXException sAXException) throws SAXException {
        XMLReporter xMLReporter = this.reporter;
        if (xMLReporter != null) {
            try {
                xMLReporter.report(sAXException.getMessage(), str, sAXException, getCurrentLocation());
            } catch (XMLStreamException e) {
                throw new SAXException(e);
            }
        }
    }

    public static final void parseQName(String str, String[] strArr) {
        String str2;
        int indexOf = str.indexOf(58);
        if (indexOf >= 0) {
            str2 = str.substring(0, indexOf);
            str = str.substring(indexOf + 1);
        } else {
            str2 = "";
        }
        strArr[0] = str2;
        strArr[1] = str;
    }

    /* access modifiers changed from: private */
    public static final class SAXLocation implements Location {
        private int columnNumber;
        private int lineNumber;
        private String publicId;
        private String systemId;

        @Override // ohos.javax.xml.stream.Location
        public int getCharacterOffset() {
            return -1;
        }

        private SAXLocation(Locator locator) {
            this.lineNumber = locator.getLineNumber();
            this.columnNumber = locator.getColumnNumber();
            this.publicId = locator.getPublicId();
            this.systemId = locator.getSystemId();
        }

        @Override // ohos.javax.xml.stream.Location
        public int getLineNumber() {
            return this.lineNumber;
        }

        @Override // ohos.javax.xml.stream.Location
        public int getColumnNumber() {
            return this.columnNumber;
        }

        @Override // ohos.javax.xml.stream.Location
        public String getPublicId() {
            return this.publicId;
        }

        @Override // ohos.javax.xml.stream.Location
        public String getSystemId() {
            return this.systemId;
        }
    }
}
