package ohos.com.sun.org.apache.xml.internal.resolver.readers;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import ohos.com.sun.org.apache.xml.internal.resolver.Catalog;
import ohos.com.sun.org.apache.xml.internal.resolver.CatalogEntry;
import ohos.com.sun.org.apache.xml.internal.resolver.CatalogException;
import ohos.com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import ohos.org.xml.sax.Attributes;
import ohos.org.xml.sax.Locator;
import ohos.org.xml.sax.SAXException;

public class OASISXMLCatalogReader extends SAXCatalogReader implements SAXCatalogParser {
    public static final String namespaceName = "urn:oasis:names:tc:entity:xmlns:xml:catalog";
    public static final String tr9401NamespaceName = "urn:oasis:names:tc:entity:xmlns:tr9401:catalog";
    protected Stack baseURIStack = new Stack();
    protected Catalog catalog = null;
    protected Stack namespaceStack = new Stack();
    protected Stack overrideStack = new Stack();

    @Override // ohos.org.xml.sax.DocumentHandler, ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void characters(char[] cArr, int i, int i2) throws SAXException {
    }

    @Override // ohos.org.xml.sax.DocumentHandler, ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void endDocument() throws SAXException {
    }

    @Override // ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void endPrefixMapping(String str) throws SAXException {
    }

    @Override // ohos.org.xml.sax.DocumentHandler, ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
    }

    @Override // ohos.org.xml.sax.DocumentHandler, ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void processingInstruction(String str, String str2) throws SAXException {
    }

    @Override // ohos.org.xml.sax.DocumentHandler, ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void setDocumentLocator(Locator locator) {
    }

    @Override // ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void skippedEntity(String str) throws SAXException {
    }

    @Override // ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void startPrefixMapping(String str, String str2) throws SAXException {
    }

    @Override // ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogParser
    public void setCatalog(Catalog catalog2) {
        this.catalog = catalog2;
        this.debug = catalog2.getCatalogManager().debug;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    /* access modifiers changed from: protected */
    public boolean inExtensionNamespace() {
        Enumeration elements = this.namespaceStack.elements();
        boolean z = false;
        while (!z && elements.hasMoreElements()) {
            String str = (String) elements.nextElement();
            boolean z2 = true;
            if (str != null && (str.equals(tr9401NamespaceName) || str.equals(namespaceName))) {
                z2 = false;
            }
            z = z2;
        }
        return z;
    }

    @Override // ohos.org.xml.sax.DocumentHandler, ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void startDocument() throws SAXException {
        this.baseURIStack.push(this.catalog.getCurrentBase());
        this.overrideStack.push(this.catalog.getDefaultOverride());
    }

    /* JADX WARNING: Removed duplicated region for block: B:107:0x035e A[SYNTHETIC, Splitter:B:107:0x035e] */
    @Override // ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startElement(java.lang.String r18, java.lang.String r19, java.lang.String r20, ohos.org.xml.sax.Attributes r21) throws ohos.org.xml.sax.SAXException {
        /*
        // Method dump skipped, instructions count: 1226
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader.startElement(java.lang.String, java.lang.String, java.lang.String, ohos.org.xml.sax.Attributes):void");
    }

    public boolean checkAttributes(Attributes attributes, String str) {
        if (attributes.getValue(str) != null) {
            return true;
        }
        Debug debug = this.debug;
        debug.message(1, "Error: required attribute " + str + " missing.");
        return false;
    }

    public boolean checkAttributes(Attributes attributes, String str, String str2) {
        return checkAttributes(attributes, str) && checkAttributes(attributes, str2);
    }

    @Override // ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
    public void endElement(String str, String str2, String str3) throws SAXException {
        Vector vector = new Vector();
        boolean inExtensionNamespace = inExtensionNamespace();
        if (str != null && !inExtensionNamespace && (namespaceName.equals(str) || tr9401NamespaceName.equals(str))) {
            String str4 = (String) this.baseURIStack.peek();
            if (!str4.equals((String) this.baseURIStack.pop())) {
                Catalog catalog2 = this.catalog;
                int i = Catalog.BASE;
                vector.add(str4);
                this.debug.message(4, "(reset) xml:base", str4);
                try {
                    this.catalog.addEntry(new CatalogEntry(i, vector));
                } catch (CatalogException e) {
                    if (e.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", str2);
                    } else if (e.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry (rbase)", str2);
                    }
                }
            }
        }
        if (str != null && namespaceName.equals(str) && !inExtensionNamespace && (str2.equals("catalog") || str2.equals("group"))) {
            String str5 = (String) this.overrideStack.peek();
            if (!str5.equals((String) this.overrideStack.pop())) {
                Catalog catalog3 = this.catalog;
                int i2 = Catalog.OVERRIDE;
                vector.add(str5);
                this.overrideStack.push(str5);
                this.debug.message(4, "(reset) override", str5);
                try {
                    this.catalog.addEntry(new CatalogEntry(i2, vector));
                } catch (CatalogException e2) {
                    if (e2.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", str2);
                    } else if (e2.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry (roverride)", str2);
                    }
                }
            }
        }
        this.namespaceStack.pop();
    }
}
