package ohos.com.sun.org.apache.xerces.internal.impl;

import java.io.PrintStream;
import ohos.javax.xml.namespace.NamespaceContext;
import ohos.javax.xml.namespace.QName;
import ohos.javax.xml.stream.Location;
import ohos.javax.xml.stream.StreamFilter;
import ohos.javax.xml.stream.XMLStreamException;
import ohos.javax.xml.stream.XMLStreamReader;

public class XMLStreamFilterImpl implements XMLStreamReader {
    private int fCurrentEvent;
    private boolean fEventAccepted = false;
    private boolean fStreamAdvancedByHasNext = false;
    private StreamFilter fStreamFilter = null;
    private XMLStreamReader fStreamReader = null;

    public XMLStreamFilterImpl(XMLStreamReader xMLStreamReader, StreamFilter streamFilter) {
        this.fStreamReader = xMLStreamReader;
        this.fStreamFilter = streamFilter;
        try {
            if (this.fStreamFilter.accept(this.fStreamReader)) {
                this.fEventAccepted = true;
            } else {
                findNextEvent();
            }
        } catch (XMLStreamException e) {
            PrintStream printStream = System.err;
            printStream.println("Error while creating a stream Filter" + e);
        }
    }

    /* access modifiers changed from: protected */
    public void setStreamFilter(StreamFilter streamFilter) {
        this.fStreamFilter = streamFilter;
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int next() throws XMLStreamException {
        if (!this.fStreamAdvancedByHasNext || !this.fEventAccepted) {
            int findNextEvent = findNextEvent();
            if (findNextEvent != -1) {
                return findNextEvent;
            }
            throw new IllegalStateException("The stream reader has reached the end of the document, or there are no more  items to return");
        }
        this.fStreamAdvancedByHasNext = false;
        return this.fCurrentEvent;
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int nextTag() throws XMLStreamException {
        int i;
        if (!this.fStreamAdvancedByHasNext || !this.fEventAccepted || !((i = this.fCurrentEvent) == 1 || i == 1)) {
            int findNextTag = findNextTag();
            if (findNextTag != -1) {
                return findNextTag;
            }
            throw new IllegalStateException("The stream reader has reached the end of the document, or there are no more  items to return");
        }
        this.fStreamAdvancedByHasNext = false;
        return this.fCurrentEvent;
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean hasNext() throws XMLStreamException {
        if (!this.fStreamReader.hasNext()) {
            return false;
        }
        if (!this.fEventAccepted) {
            int findNextEvent = findNextEvent();
            this.fCurrentEvent = findNextEvent;
            if (findNextEvent == -1) {
                return false;
            }
            this.fStreamAdvancedByHasNext = true;
        }
        return true;
    }

    private int findNextEvent() throws XMLStreamException {
        this.fStreamAdvancedByHasNext = false;
        while (this.fStreamReader.hasNext()) {
            this.fCurrentEvent = this.fStreamReader.next();
            if (this.fStreamFilter.accept(this.fStreamReader)) {
                this.fEventAccepted = true;
                return this.fCurrentEvent;
            }
        }
        int i = this.fCurrentEvent;
        if (i == 8) {
            return i;
        }
        return -1;
    }

    private int findNextTag() throws XMLStreamException {
        this.fStreamAdvancedByHasNext = false;
        while (this.fStreamReader.hasNext()) {
            this.fCurrentEvent = this.fStreamReader.nextTag();
            if (this.fStreamFilter.accept(this.fStreamReader)) {
                this.fEventAccepted = true;
                return this.fCurrentEvent;
            }
        }
        int i = this.fCurrentEvent;
        if (i == 8) {
            return i;
        }
        return -1;
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
        this.fStreamReader.close();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int getAttributeCount() {
        return this.fStreamReader.getAttributeCount();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public QName getAttributeName(int i) {
        return this.fStreamReader.getAttributeName(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getAttributeNamespace(int i) {
        return this.fStreamReader.getAttributeNamespace(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getAttributePrefix(int i) {
        return this.fStreamReader.getAttributePrefix(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getAttributeType(int i) {
        return this.fStreamReader.getAttributeType(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getAttributeValue(int i) {
        return this.fStreamReader.getAttributeValue(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getAttributeValue(String str, String str2) {
        return this.fStreamReader.getAttributeValue(str, str2);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getCharacterEncodingScheme() {
        return this.fStreamReader.getCharacterEncodingScheme();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getElementText() throws XMLStreamException {
        return this.fStreamReader.getElementText();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getEncoding() {
        return this.fStreamReader.getEncoding();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int getEventType() {
        return this.fStreamReader.getEventType();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getLocalName() {
        return this.fStreamReader.getLocalName();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public Location getLocation() {
        return this.fStreamReader.getLocation();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public QName getName() {
        return this.fStreamReader.getName();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public NamespaceContext getNamespaceContext() {
        return this.fStreamReader.getNamespaceContext();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int getNamespaceCount() {
        return this.fStreamReader.getNamespaceCount();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getNamespacePrefix(int i) {
        return this.fStreamReader.getNamespacePrefix(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getNamespaceURI() {
        return this.fStreamReader.getNamespaceURI();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(int i) {
        return this.fStreamReader.getNamespaceURI(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(String str) {
        return this.fStreamReader.getNamespaceURI(str);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getPIData() {
        return this.fStreamReader.getPIData();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getPITarget() {
        return this.fStreamReader.getPITarget();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getPrefix() {
        return this.fStreamReader.getPrefix();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public Object getProperty(String str) throws IllegalArgumentException {
        return this.fStreamReader.getProperty(str);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getText() {
        return this.fStreamReader.getText();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public char[] getTextCharacters() {
        return this.fStreamReader.getTextCharacters();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int getTextCharacters(int i, char[] cArr, int i2, int i3) throws XMLStreamException {
        return this.fStreamReader.getTextCharacters(i, cArr, i2, i3);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int getTextLength() {
        return this.fStreamReader.getTextLength();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public int getTextStart() {
        return this.fStreamReader.getTextStart();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getVersion() {
        return this.fStreamReader.getVersion();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean hasName() {
        return this.fStreamReader.hasName();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean hasText() {
        return this.fStreamReader.hasText();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean isAttributeSpecified(int i) {
        return this.fStreamReader.isAttributeSpecified(i);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean isCharacters() {
        return this.fStreamReader.isCharacters();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean isEndElement() {
        return this.fStreamReader.isEndElement();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean isStandalone() {
        return this.fStreamReader.isStandalone();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean isStartElement() {
        return this.fStreamReader.isStartElement();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean isWhiteSpace() {
        return this.fStreamReader.isWhiteSpace();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public void require(int i, String str, String str2) throws XMLStreamException {
        this.fStreamReader.require(i, str, str2);
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public boolean standaloneSet() {
        return this.fStreamReader.standaloneSet();
    }

    @Override // ohos.javax.xml.stream.XMLStreamReader
    public String getAttributeLocalName(int i) {
        return this.fStreamReader.getAttributeLocalName(i);
    }
}
