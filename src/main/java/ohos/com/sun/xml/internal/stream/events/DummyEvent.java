package ohos.com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import ohos.com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import ohos.javax.xml.namespace.QName;
import ohos.javax.xml.stream.Location;
import ohos.javax.xml.stream.XMLStreamException;
import ohos.javax.xml.stream.events.Characters;
import ohos.javax.xml.stream.events.EndElement;
import ohos.javax.xml.stream.events.StartElement;
import ohos.javax.xml.stream.events.XMLEvent;

public abstract class DummyEvent implements XMLEvent {
    private static DummyLocation nowhere = new DummyLocation();
    private int fEventType;
    protected Location fLocation = nowhere;

    static class DummyLocation implements Location {
        @Override // ohos.javax.xml.stream.Location
        public int getCharacterOffset() {
            return -1;
        }

        @Override // ohos.javax.xml.stream.Location
        public int getColumnNumber() {
            return -1;
        }

        @Override // ohos.javax.xml.stream.Location
        public int getLineNumber() {
            return -1;
        }

        @Override // ohos.javax.xml.stream.Location
        public String getPublicId() {
            return null;
        }

        @Override // ohos.javax.xml.stream.Location
        public String getSystemId() {
            return null;
        }
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public QName getSchemaType() {
        return null;
    }

    /* access modifiers changed from: protected */
    public abstract void writeAsEncodedUnicodeEx(Writer writer) throws IOException, XMLStreamException;

    public DummyEvent() {
    }

    public DummyEvent(int i) {
        this.fEventType = i;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public int getEventType() {
        return this.fEventType;
    }

    /* access modifiers changed from: protected */
    public void setEventType(int i) {
        this.fEventType = i;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isStartElement() {
        return this.fEventType == 1;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isEndElement() {
        return this.fEventType == 2;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isEntityReference() {
        return this.fEventType == 9;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isProcessingInstruction() {
        return this.fEventType == 3;
    }

    public boolean isCharacterData() {
        return this.fEventType == 4;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isStartDocument() {
        return this.fEventType == 7;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isEndDocument() {
        return this.fEventType == 8;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public Location getLocation() {
        return this.fLocation;
    }

    /* access modifiers changed from: package-private */
    public void setLocation(Location location) {
        if (location == null) {
            this.fLocation = nowhere;
        } else {
            this.fLocation = location;
        }
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public Characters asCharacters() {
        return (Characters) this;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public EndElement asEndElement() {
        return (EndElement) this;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public StartElement asStartElement() {
        return (StartElement) this;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isAttribute() {
        return this.fEventType == 10;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isCharacters() {
        return this.fEventType == 4;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public boolean isNamespace() {
        return this.fEventType == 13;
    }

    @Override // ohos.javax.xml.stream.events.XMLEvent
    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
        try {
            writeAsEncodedUnicodeEx(writer);
        } catch (IOException e) {
            throw new XMLStreamException(e);
        }
    }

    /* access modifiers changed from: protected */
    public void charEncode(Writer writer, String str) throws IOException {
        if (!(str == null || str == "")) {
            int length = str.length();
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                char charAt = str.charAt(i2);
                if (charAt == '\"') {
                    writer.write(str, i, i2 - i);
                    writer.write(SerializerConstants.ENTITY_QUOT);
                } else if (charAt == '&') {
                    writer.write(str, i, i2 - i);
                    writer.write(SerializerConstants.ENTITY_AMP);
                } else if (charAt == '<') {
                    writer.write(str, i, i2 - i);
                    writer.write(SerializerConstants.ENTITY_LT);
                } else if (charAt == '>') {
                    writer.write(str, i, i2 - i);
                    writer.write(SerializerConstants.ENTITY_GT);
                }
                i = i2 + 1;
            }
            writer.write(str, i, length - i);
        }
    }
}
