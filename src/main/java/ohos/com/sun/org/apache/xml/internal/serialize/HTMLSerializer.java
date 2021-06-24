package ohos.com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;
import ohos.com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.Element;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.xml.sax.AttributeList;
import ohos.org.xml.sax.SAXException;

public class HTMLSerializer extends BaseMarkupSerializer {
    public static final String XHTMLNamespace = "http://www.w3.org/1999/xhtml";
    private boolean _xhtml;
    private String fUserXHTMLNamespace;

    protected HTMLSerializer(boolean z, OutputFormat outputFormat) {
        super(outputFormat);
        this.fUserXHTMLNamespace = null;
        this._xhtml = z;
    }

    public HTMLSerializer() {
        this(false, new OutputFormat("html", "ISO-8859-1", false));
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public HTMLSerializer(OutputFormat outputFormat) {
        this(false, outputFormat == null ? new OutputFormat("html", "ISO-8859-1", false) : outputFormat);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public HTMLSerializer(Writer writer, OutputFormat outputFormat) {
        this(false, outputFormat == null ? new OutputFormat("html", "ISO-8859-1", false) : outputFormat);
        setOutputCharStream(writer);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public HTMLSerializer(OutputStream outputStream, OutputFormat outputFormat) {
        this(false, outputFormat == null ? new OutputFormat("html", "ISO-8859-1", false) : outputFormat);
        setOutputByteStream(outputStream);
    }

    @Override // ohos.com.sun.org.apache.xml.internal.serialize.Serializer, ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void setOutputFormat(OutputFormat outputFormat) {
        if (outputFormat == null) {
            outputFormat = new OutputFormat("html", "ISO-8859-1", false);
        }
        super.setOutputFormat(outputFormat);
    }

    public void setXHTMLNamespace(String str) {
        this.fUserXHTMLNamespace = str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:106:0x01ef  */
    /* JADX WARNING: Removed duplicated region for block: B:131:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x008e A[Catch:{ IOException -> 0x0219 }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0090 A[Catch:{ IOException -> 0x0219 }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00b0 A[Catch:{ IOException -> 0x0219 }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00bc A[Catch:{ IOException -> 0x0219 }] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0175  */
    @Override // ohos.org.xml.sax.ContentHandler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startElement(java.lang.String r17, java.lang.String r18, java.lang.String r19, ohos.org.xml.sax.Attributes r20) throws ohos.org.xml.sax.SAXException {
        /*
        // Method dump skipped, instructions count: 544
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.serialize.HTMLSerializer.startElement(java.lang.String, java.lang.String, java.lang.String, ohos.org.xml.sax.Attributes):void");
    }

    @Override // ohos.org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        try {
            endElementIO(str, str2, str3);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    public void endElementIO(String str, String str2, String str3) throws IOException {
        String str4;
        String str5;
        this._printer.unindent();
        ElementState elementState = getElementState();
        if (elementState.namespaceURI == null || elementState.namespaceURI.length() == 0) {
            str4 = elementState.rawName;
        } else if (elementState.namespaceURI.equals("http://www.w3.org/1999/xhtml") || ((str5 = this.fUserXHTMLNamespace) != null && str5.equals(elementState.namespaceURI))) {
            str4 = elementState.localName;
        } else {
            str4 = null;
        }
        if (!this._xhtml) {
            if (elementState.empty) {
                this._printer.printText('>');
            }
            if (str4 == null || !HTMLdtd.isOnlyOpening(str4)) {
                if (this._indenting && !elementState.preserveSpace && elementState.afterElement) {
                    this._printer.breakLine();
                }
                if (elementState.inCData) {
                    this._printer.printText("]]>");
                }
                this._printer.printText("</");
                this._printer.printText(elementState.rawName);
                this._printer.printText('>');
            }
        } else if (elementState.empty) {
            this._printer.printText(" />");
        } else {
            if (elementState.inCData) {
                this._printer.printText("]]>");
            }
            this._printer.printText("</");
            this._printer.printText(elementState.rawName.toLowerCase(Locale.ENGLISH));
            this._printer.printText('>');
        }
        ElementState leaveElementState = leaveElementState();
        if (str4 == null || (!str4.equalsIgnoreCase("A") && !str4.equalsIgnoreCase("TD"))) {
            leaveElementState.afterElement = true;
        }
        leaveElementState.empty = false;
        if (isDocumentState()) {
            this._printer.flush();
        }
    }

    @Override // ohos.org.xml.sax.DocumentHandler, ohos.org.xml.sax.ContentHandler, ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        try {
            content().doCData = false;
            super.characters(cArr, i, i2);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // ohos.org.xml.sax.DocumentHandler
    public void startElement(String str, AttributeList attributeList) throws SAXException {
        try {
            if (this._printer != null) {
                ElementState elementState = getElementState();
                if (!isDocumentState()) {
                    if (elementState.empty) {
                        this._printer.printText('>');
                    }
                    if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement)) {
                        this._printer.breakLine();
                    }
                } else if (!this._started) {
                    startDocument(str);
                }
                boolean z = elementState.preserveSpace;
                this._printer.printText('<');
                if (this._xhtml) {
                    this._printer.printText(str.toLowerCase(Locale.ENGLISH));
                } else {
                    this._printer.printText(str);
                }
                this._printer.indent();
                if (attributeList != null) {
                    for (int i = 0; i < attributeList.getLength(); i++) {
                        this._printer.printSpace();
                        String lowerCase = attributeList.getName(i).toLowerCase(Locale.ENGLISH);
                        String value = attributeList.getValue(i);
                        if (!this._xhtml) {
                            if (value == null) {
                                value = "";
                            }
                            if (!this._format.getPreserveEmptyAttributes() && value.length() == 0) {
                                this._printer.printText(lowerCase);
                            } else if (HTMLdtd.isURI(str, lowerCase)) {
                                this._printer.printText(lowerCase);
                                this._printer.printText("=\"");
                                this._printer.printText(escapeURI(value));
                                this._printer.printText('\"');
                            } else if (HTMLdtd.isBoolean(str, lowerCase)) {
                                this._printer.printText(lowerCase);
                            } else {
                                this._printer.printText(lowerCase);
                                this._printer.printText("=\"");
                                printEscaped(value);
                                this._printer.printText('\"');
                            }
                        } else if (value == null) {
                            this._printer.printText(lowerCase);
                            this._printer.printText("=\"\"");
                        } else {
                            this._printer.printText(lowerCase);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    }
                }
                if (HTMLdtd.isPreserveSpace(str)) {
                    z = true;
                }
                ElementState enterElementState = enterElementState(null, null, str, z);
                if (str.equalsIgnoreCase("A") || str.equalsIgnoreCase("TD")) {
                    enterElementState.empty = false;
                    this._printer.printText('>');
                }
                if (!(str.equalsIgnoreCase("SCRIPT") || str.equalsIgnoreCase("STYLE"))) {
                    return;
                }
                if (this._xhtml) {
                    enterElementState.doCData = true;
                } else {
                    enterElementState.unescaped = true;
                }
            } else {
                throw new IllegalStateException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null));
            }
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // ohos.org.xml.sax.DocumentHandler
    public void endElement(String str) throws SAXException {
        endElement(null, null, str);
    }

    /* access modifiers changed from: protected */
    public void startDocument(String str) throws IOException {
        this._printer.leaveDTD();
        if (!this._started) {
            if (this._docTypePublicId == null && this._docTypeSystemId == null) {
                if (this._xhtml) {
                    this._docTypePublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
                    this._docTypeSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
                } else {
                    this._docTypePublicId = "-//W3C//DTD HTML 4.01//EN";
                    this._docTypeSystemId = "http://www.w3.org/TR/html4/strict.dtd";
                }
            }
            if (!this._format.getOmitDocumentType()) {
                if (this._docTypePublicId != null && (!this._xhtml || this._docTypeSystemId != null)) {
                    if (this._xhtml) {
                        this._printer.printText("<!DOCTYPE html PUBLIC ");
                    } else {
                        this._printer.printText("<!DOCTYPE HTML PUBLIC ");
                    }
                    printDoctypeURL(this._docTypePublicId);
                    if (this._docTypeSystemId != null) {
                        if (this._indenting) {
                            this._printer.breakLine();
                            this._printer.printText("                      ");
                        } else {
                            this._printer.printText(' ');
                        }
                        printDoctypeURL(this._docTypeSystemId);
                    }
                    this._printer.printText('>');
                    this._printer.breakLine();
                } else if (this._docTypeSystemId != null) {
                    if (this._xhtml) {
                        this._printer.printText("<!DOCTYPE html SYSTEM ");
                    } else {
                        this._printer.printText("<!DOCTYPE HTML SYSTEM ");
                    }
                    printDoctypeURL(this._docTypeSystemId);
                    this._printer.printText('>');
                    this._printer.breakLine();
                }
            }
        }
        this._started = true;
        serializePreRoot();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void serializeElement(Element element) throws IOException {
        String tagName = element.getTagName();
        ElementState elementState = getElementState();
        if (!isDocumentState()) {
            if (elementState.empty) {
                this._printer.printText('>');
            }
            if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement)) {
                this._printer.breakLine();
            }
        } else if (!this._started) {
            startDocument(tagName);
        }
        boolean z = elementState.preserveSpace;
        this._printer.printText('<');
        if (this._xhtml) {
            this._printer.printText(tagName.toLowerCase(Locale.ENGLISH));
        } else {
            this._printer.printText(tagName);
        }
        this._printer.indent();
        NamedNodeMap attributes = element.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Attr attr = (Attr) attributes.item(i);
                String lowerCase = attr.getName().toLowerCase(Locale.ENGLISH);
                String value = attr.getValue();
                if (attr.getSpecified()) {
                    this._printer.printSpace();
                    if (!this._xhtml) {
                        if (value == null) {
                            value = "";
                        }
                        if (!this._format.getPreserveEmptyAttributes() && value.length() == 0) {
                            this._printer.printText(lowerCase);
                        } else if (HTMLdtd.isURI(tagName, lowerCase)) {
                            this._printer.printText(lowerCase);
                            this._printer.printText("=\"");
                            this._printer.printText(escapeURI(value));
                            this._printer.printText('\"');
                        } else if (HTMLdtd.isBoolean(tagName, lowerCase)) {
                            this._printer.printText(lowerCase);
                        } else {
                            this._printer.printText(lowerCase);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    } else if (value == null) {
                        this._printer.printText(lowerCase);
                        this._printer.printText("=\"\"");
                    } else {
                        this._printer.printText(lowerCase);
                        this._printer.printText("=\"");
                        printEscaped(value);
                        this._printer.printText('\"');
                    }
                }
            }
        }
        if (HTMLdtd.isPreserveSpace(tagName)) {
            z = true;
        }
        if (element.hasChildNodes() || !HTMLdtd.isEmptyTag(tagName)) {
            ElementState enterElementState = enterElementState(null, null, tagName, z);
            if (tagName.equalsIgnoreCase("A") || tagName.equalsIgnoreCase("TD")) {
                enterElementState.empty = false;
                this._printer.printText('>');
            }
            if (tagName.equalsIgnoreCase("SCRIPT") || tagName.equalsIgnoreCase("STYLE")) {
                if (this._xhtml) {
                    enterElementState.doCData = true;
                } else {
                    enterElementState.unescaped = true;
                }
            }
            for (Node firstChild = element.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                serializeNode(firstChild);
            }
            endElementIO(null, null, tagName);
            return;
        }
        this._printer.unindent();
        if (this._xhtml) {
            this._printer.printText(" />");
        } else {
            this._printer.printText('>');
        }
        elementState.afterElement = true;
        elementState.empty = false;
        if (isDocumentState()) {
            this._printer.flush();
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void characters(String str) throws IOException {
        content();
        super.characters(str);
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public String getEntityRef(int i) {
        return HTMLdtd.fromChar(i);
    }

    /* access modifiers changed from: protected */
    public String escapeURI(String str) {
        int indexOf = str.indexOf("\"");
        return indexOf >= 0 ? str.substring(0, indexOf) : str;
    }
}
