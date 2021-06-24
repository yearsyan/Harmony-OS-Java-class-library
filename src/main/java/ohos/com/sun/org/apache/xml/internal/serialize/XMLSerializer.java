package ohos.com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolTable;
import ohos.com.sun.org.apache.xerces.internal.util.XMLChar;
import ohos.com.sun.org.apache.xerces.internal.util.XMLSymbols;
import ohos.com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.xml.sax.AttributeList;
import ohos.org.xml.sax.Attributes;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.helpers.AttributesImpl;

public class XMLSerializer extends BaseMarkupSerializer {
    protected static final boolean DEBUG = false;
    protected static final String PREFIX = "NS";
    protected NamespaceSupport fLocalNSBinder;
    protected NamespaceSupport fNSBinder;
    protected boolean fNamespacePrefixes;
    protected boolean fNamespaces;
    private boolean fPreserveSpace;
    protected SymbolTable fSymbolTable;

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public String getEntityRef(int i) {
        if (i == 34) {
            return "quot";
        }
        if (i == 60) {
            return "lt";
        }
        if (i == 62) {
            return "gt";
        }
        if (i == 38) {
            return "amp";
        }
        if (i != 39) {
            return null;
        }
        return "apos";
    }

    public XMLSerializer() {
        super(new OutputFormat("xml", (String) null, false));
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public XMLSerializer(OutputFormat outputFormat) {
        super(outputFormat == null ? new OutputFormat("xml", (String) null, false) : outputFormat);
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
        this._format.setMethod("xml");
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public XMLSerializer(Writer writer, OutputFormat outputFormat) {
        super(outputFormat == null ? new OutputFormat("xml", (String) null, false) : outputFormat);
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
        this._format.setMethod("xml");
        setOutputCharStream(writer);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public XMLSerializer(OutputStream outputStream, OutputFormat outputFormat) {
        super(outputFormat == null ? new OutputFormat("xml", (String) null, false) : outputFormat);
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
        this._format.setMethod("xml");
        setOutputByteStream(outputStream);
    }

    @Override // ohos.com.sun.org.apache.xml.internal.serialize.Serializer, ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void setOutputFormat(OutputFormat outputFormat) {
        if (outputFormat == null) {
            outputFormat = new OutputFormat("xml", (String) null, false);
        }
        super.setOutputFormat(outputFormat);
    }

    public void setNamespaces(boolean z) {
        this.fNamespaces = z;
        if (this.fNSBinder == null) {
            this.fNSBinder = new NamespaceSupport();
            this.fLocalNSBinder = new NamespaceSupport();
            this.fSymbolTable = new SymbolTable();
        }
    }

    @Override // ohos.org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        String prefix;
        String prefix2;
        try {
            if (this._printer != null) {
                ElementState elementState = getElementState();
                if (!isDocumentState()) {
                    if (elementState.empty) {
                        this._printer.printText('>');
                    }
                    if (elementState.inCData) {
                        this._printer.printText("]]>");
                        elementState.inCData = false;
                    }
                    if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement || elementState.afterComment)) {
                        this._printer.breakLine();
                    }
                } else if (!this._started) {
                    startDocument((str2 == null || str2.length() == 0) ? str3 : str2);
                }
                boolean z = elementState.preserveSpace;
                Attributes extractNamespaces = extractNamespaces(attributes);
                if (str3 == null || str3.length() == 0) {
                    if (str2 == null) {
                        throw new SAXException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoName", null));
                    } else if (str == null || str.equals("") || (prefix2 = getPrefix(str)) == null || prefix2.length() <= 0) {
                        str3 = str2;
                    } else {
                        str3 = prefix2 + ":" + str2;
                    }
                }
                this._printer.printText('<');
                this._printer.printText(str3);
                this._printer.indent();
                if (extractNamespaces != null) {
                    for (int i = 0; i < extractNamespaces.getLength(); i++) {
                        this._printer.printSpace();
                        String qName = extractNamespaces.getQName(i);
                        if (qName != null && qName.length() == 0) {
                            qName = extractNamespaces.getLocalName(i);
                            String uri = extractNamespaces.getURI(i);
                            if (!(uri == null || uri.length() == 0 || (!(str == null || str.length() == 0 || !uri.equals(str)) || (prefix = getPrefix(uri)) == null || prefix.length() <= 0))) {
                                qName = prefix + ":" + qName;
                            }
                        }
                        String value = extractNamespaces.getValue(i);
                        if (value == null) {
                            value = "";
                        }
                        this._printer.printText(qName);
                        this._printer.printText("=\"");
                        printEscaped(value);
                        this._printer.printText('\"');
                        if (qName.equals(Constants.ATTRNAME_XMLSPACE)) {
                            if (value.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
                                z = true;
                            } else {
                                z = this._format.getPreserveSpace();
                            }
                        }
                    }
                }
                if (this._prefixes != null) {
                    for (Map.Entry entry : this._prefixes.entrySet()) {
                        this._printer.printSpace();
                        String str4 = (String) entry.getKey();
                        String str5 = (String) entry.getValue();
                        if (str5.length() == 0) {
                            this._printer.printText("xmlns=\"");
                            printEscaped(str4);
                            this._printer.printText('\"');
                        } else {
                            this._printer.printText("xmlns:");
                            this._printer.printText(str5);
                            this._printer.printText("=\"");
                            printEscaped(str4);
                            this._printer.printText('\"');
                        }
                    }
                }
                ElementState enterElementState = enterElementState(str, str2, str3, z);
                if (!(str2 == null || str2.length() == 0)) {
                    str3 = str + "^" + str2;
                }
                enterElementState.doCData = this._format.isCDataElement(str3);
                enterElementState.unescaped = this._format.isNonEscapingElement(str3);
                return;
            }
            throw new IllegalStateException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null));
        } catch (IOException e) {
            throw new SAXException(e);
        }
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
        this._printer.unindent();
        ElementState elementState = getElementState();
        if (elementState.empty) {
            this._printer.printText("/>");
        } else {
            if (elementState.inCData) {
                this._printer.printText("]]>");
            }
            if (this._indenting && !elementState.preserveSpace && (elementState.afterElement || elementState.afterComment)) {
                this._printer.breakLine();
            }
            this._printer.printText("</");
            this._printer.printText(elementState.rawName);
            this._printer.printText('>');
        }
        ElementState leaveElementState = leaveElementState();
        leaveElementState.afterElement = true;
        leaveElementState.afterComment = false;
        leaveElementState.empty = false;
        if (isDocumentState()) {
            this._printer.flush();
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
                    if (elementState.inCData) {
                        this._printer.printText("]]>");
                        elementState.inCData = false;
                    }
                    if (this._indenting && !elementState.preserveSpace && (elementState.empty || elementState.afterElement || elementState.afterComment)) {
                        this._printer.breakLine();
                    }
                } else if (!this._started) {
                    startDocument(str);
                }
                boolean z = elementState.preserveSpace;
                this._printer.printText('<');
                this._printer.printText(str);
                this._printer.indent();
                if (attributeList != null) {
                    for (int i = 0; i < attributeList.getLength(); i++) {
                        this._printer.printSpace();
                        String name = attributeList.getName(i);
                        String value = attributeList.getValue(i);
                        if (value != null) {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                        if (name.equals(Constants.ATTRNAME_XMLSPACE)) {
                            if (value.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
                                z = true;
                            } else {
                                z = this._format.getPreserveSpace();
                            }
                        }
                    }
                }
                ElementState enterElementState = enterElementState(null, null, str, z);
                enterElementState.doCData = this._format.isCDataElement(str);
                enterElementState.unescaped = this._format.isNonEscapingElement(str);
                return;
            }
            throw new IllegalStateException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null));
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
        String leaveDTD = this._printer.leaveDTD();
        if (!this._started) {
            if (!this._format.getOmitXMLDeclaration()) {
                StringBuffer stringBuffer = new StringBuffer("<?xml version=\"");
                if (this._format.getVersion() != null) {
                    stringBuffer.append(this._format.getVersion());
                } else {
                    stringBuffer.append("1.0");
                }
                stringBuffer.append('\"');
                String encoding = this._format.getEncoding();
                if (encoding != null) {
                    stringBuffer.append(" encoding=\"");
                    stringBuffer.append(encoding);
                    stringBuffer.append('\"');
                }
                if (this._format.getStandalone() && this._docTypeSystemId == null && this._docTypePublicId == null) {
                    stringBuffer.append(" standalone=\"yes\"");
                }
                stringBuffer.append("?>");
                this._printer.printText(stringBuffer);
                this._printer.breakLine();
            }
            if (!this._format.getOmitDocumentType()) {
                if (this._docTypeSystemId != null) {
                    this._printer.printText("<!DOCTYPE ");
                    this._printer.printText(str);
                    if (this._docTypePublicId != null) {
                        this._printer.printText(" PUBLIC ");
                        printDoctypeURL(this._docTypePublicId);
                        if (this._indenting) {
                            this._printer.breakLine();
                            for (int i = 0; i < str.length() + 18; i++) {
                                this._printer.printText(" ");
                            }
                        } else {
                            this._printer.printText(" ");
                        }
                        printDoctypeURL(this._docTypeSystemId);
                    } else {
                        this._printer.printText(" SYSTEM ");
                        printDoctypeURL(this._docTypeSystemId);
                    }
                    if (leaveDTD != null && leaveDTD.length() > 0) {
                        this._printer.printText(" [");
                        printText(leaveDTD, true, true);
                        this._printer.printText(']');
                    }
                    this._printer.printText(">");
                    this._printer.breakLine();
                } else if (leaveDTD != null && leaveDTD.length() > 0) {
                    this._printer.printText("<!DOCTYPE ");
                    this._printer.printText(str);
                    this._printer.printText(" [");
                    printText(leaveDTD, true, true);
                    this._printer.printText("]>");
                    this._printer.breakLine();
                }
            }
        }
        this._started = true;
        serializePreRoot();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0214  */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void serializeElement(ohos.org.w3c.dom.Element r23) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 1103
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.serialize.XMLSerializer.serializeElement(ohos.org.w3c.dom.Element):void");
    }

    private void printNamespaceAttr(String str, String str2) throws IOException {
        this._printer.printSpace();
        if (str == XMLSymbols.EMPTY_STRING) {
            this._printer.printText(XMLSymbols.PREFIX_XMLNS);
        } else {
            Printer printer = this._printer;
            printer.printText("xmlns:" + str);
        }
        this._printer.printText("=\"");
        printEscaped(str2);
        this._printer.printText('\"');
    }

    private void printAttribute(String str, String str2, boolean z, Attr attr) throws IOException {
        short acceptNode;
        if (z || (this.features & 64) == 0) {
            if (this.fDOMFilter == null || (this.fDOMFilter.getWhatToShow() & 2) == 0 || !((acceptNode = this.fDOMFilter.acceptNode(attr)) == 2 || acceptNode == 3)) {
                this._printer.printSpace();
                this._printer.printText(str);
                this._printer.printText("=\"");
                printEscaped(str2);
                this._printer.printText('\"');
            } else {
                return;
            }
        }
        if (!str.equals(Constants.ATTRNAME_XMLSPACE)) {
            return;
        }
        if (str2.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
            this.fPreserveSpace = true;
        } else {
            this.fPreserveSpace = this._format.getPreserveSpace();
        }
    }

    private Attributes extractNamespaces(Attributes attributes) throws SAXException {
        if (attributes == null) {
            return null;
        }
        int length = attributes.getLength();
        AttributesImpl attributesImpl = new AttributesImpl(attributes);
        for (int i = length - 1; i >= 0; i--) {
            String qName = attributesImpl.getQName(i);
            if (qName.startsWith("xmlns")) {
                if (qName.length() == 5) {
                    startPrefixMapping("", attributes.getValue(i));
                    attributesImpl.removeAttribute(i);
                } else if (qName.charAt(5) == ':') {
                    startPrefixMapping(qName.substring(6), attributes.getValue(i));
                    attributesImpl.removeAttribute(i);
                }
            }
        }
        return attributesImpl;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void printEscaped(String str) throws IOException {
        int length = str.length();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (!XMLChar.isValid(charAt)) {
                i++;
                if (i < length) {
                    surrogates(charAt, str.charAt(i));
                } else {
                    fatalError("The character '" + ((char) charAt) + "' is an invalid XML character");
                }
            } else if (charAt == '\n' || charAt == '\r' || charAt == '\t') {
                printHex(charAt);
            } else if (charAt == '<') {
                this._printer.printText(SerializerConstants.ENTITY_LT);
            } else if (charAt == '&') {
                this._printer.printText(SerializerConstants.ENTITY_AMP);
            } else if (charAt == '\"') {
                this._printer.printText(SerializerConstants.ENTITY_QUOT);
            } else {
                if (charAt >= ' ') {
                    char c = (char) charAt;
                    if (this._encodingInfo.isPrintable(c)) {
                        this._printer.printText(c);
                    }
                }
                printHex(charAt);
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public void printXMLChar(int i) throws IOException {
        if (i == 13) {
            printHex(i);
        } else if (i == 60) {
            this._printer.printText(SerializerConstants.ENTITY_LT);
        } else if (i == 38) {
            this._printer.printText(SerializerConstants.ENTITY_AMP);
        } else if (i == 62) {
            this._printer.printText(SerializerConstants.ENTITY_GT);
        } else if (i == 10 || i == 9 || (i >= 32 && this._encodingInfo.isPrintable((char) i))) {
            this._printer.printText((char) i);
        } else {
            printHex(i);
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void printText(String str, boolean z, boolean z2) throws IOException {
        int length = str.length();
        int i = 0;
        if (z) {
            while (i < length) {
                char charAt = str.charAt(i);
                if (!XMLChar.isValid(charAt)) {
                    i++;
                    if (i < length) {
                        surrogates(charAt, str.charAt(i));
                    } else {
                        fatalError("The character '" + charAt + "' is an invalid XML character");
                    }
                } else if (z2) {
                    this._printer.printText(charAt);
                } else {
                    printXMLChar(charAt);
                }
                i++;
            }
            return;
        }
        while (i < length) {
            char charAt2 = str.charAt(i);
            if (!XMLChar.isValid(charAt2)) {
                i++;
                if (i < length) {
                    surrogates(charAt2, str.charAt(i));
                } else {
                    fatalError("The character '" + charAt2 + "' is an invalid XML character");
                }
            } else if (z2) {
                this._printer.printText(charAt2);
            } else {
                printXMLChar(charAt2);
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void printText(char[] cArr, int i, int i2, boolean z, boolean z2) throws IOException {
        if (z) {
            while (true) {
                int i3 = i2 - 1;
                if (i2 > 0) {
                    int i4 = i + 1;
                    char c = cArr[i];
                    if (!XMLChar.isValid(c)) {
                        int i5 = i3 - 1;
                        if (i3 > 0) {
                            surrogates(c, cArr[i4]);
                            i = i4 + 1;
                        } else {
                            fatalError("The character '" + c + "' is an invalid XML character");
                            i = i4;
                        }
                        i2 = i5;
                    } else {
                        if (z2) {
                            this._printer.printText(c);
                        } else {
                            printXMLChar(c);
                        }
                        i = i4;
                        i2 = i3;
                    }
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                int i6 = i2 - 1;
                if (i2 > 0) {
                    int i7 = i + 1;
                    char c2 = cArr[i];
                    if (!XMLChar.isValid(c2)) {
                        int i8 = i6 - 1;
                        if (i6 > 0) {
                            surrogates(c2, cArr[i7]);
                            i = i7 + 1;
                        } else {
                            fatalError("The character '" + c2 + "' is an invalid XML character");
                            i = i7;
                        }
                        i2 = i8;
                    } else {
                        if (z2) {
                            this._printer.printText(c2);
                        } else {
                            printXMLChar(c2);
                        }
                        i = i7;
                        i2 = i6;
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void checkUnboundNamespacePrefixedNode(Node node) throws IOException {
        if (this.fNamespaces) {
            Node firstChild = node.getFirstChild();
            while (firstChild != null) {
                Node nextSibling = firstChild.getNextSibling();
                String prefix = firstChild.getPrefix();
                String addSymbol = (prefix == null || prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
                if (this.fNSBinder.getURI(addSymbol) == null && addSymbol != null) {
                    fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + firstChild.getNodeName() + "' with an undeclared prefix '" + addSymbol + "'.");
                }
                if (firstChild.getNodeType() == 1) {
                    NamedNodeMap attributes = firstChild.getAttributes();
                    for (int i = 0; i < attributes.getLength(); i++) {
                        String prefix2 = attributes.item(i).getPrefix();
                        String addSymbol2 = (prefix2 == null || prefix2.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix2);
                        if (this.fNSBinder.getURI(addSymbol2) == null && addSymbol2 != null) {
                            fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + firstChild.getNodeName() + "' with an attribute '" + attributes.item(i).getNodeName() + "' an undeclared prefix '" + addSymbol2 + "'.");
                        }
                    }
                }
                if (firstChild.hasChildNodes()) {
                    checkUnboundNamespacePrefixedNode(firstChild);
                }
                firstChild = nextSibling;
            }
        }
    }

    @Override // ohos.com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public boolean reset() {
        super.reset();
        NamespaceSupport namespaceSupport = this.fNSBinder;
        if (namespaceSupport == null) {
            return true;
        }
        namespaceSupport.reset();
        this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
        return true;
    }
}
