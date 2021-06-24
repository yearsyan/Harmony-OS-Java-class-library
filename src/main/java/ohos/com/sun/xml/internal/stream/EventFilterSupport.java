package ohos.com.sun.xml.internal.stream;

import java.util.NoSuchElementException;
import ohos.javax.xml.stream.EventFilter;
import ohos.javax.xml.stream.XMLEventReader;
import ohos.javax.xml.stream.XMLStreamException;
import ohos.javax.xml.stream.events.XMLEvent;
import ohos.javax.xml.stream.util.EventReaderDelegate;

public class EventFilterSupport extends EventReaderDelegate {
    EventFilter fEventFilter;

    public EventFilterSupport(XMLEventReader xMLEventReader, EventFilter eventFilter) {
        setParent(xMLEventReader);
        this.fEventFilter = eventFilter;
    }

    @Override // ohos.javax.xml.stream.util.EventReaderDelegate, java.util.Iterator
    public Object next() {
        try {
            return nextEvent();
        } catch (XMLStreamException unused) {
            throw new NoSuchElementException();
        }
    }

    @Override // ohos.javax.xml.stream.XMLEventReader, ohos.javax.xml.stream.util.EventReaderDelegate
    public boolean hasNext() {
        try {
            return peek() != null;
        } catch (XMLStreamException unused) {
            return false;
        }
    }

    @Override // ohos.javax.xml.stream.XMLEventReader, ohos.javax.xml.stream.util.EventReaderDelegate
    public XMLEvent nextEvent() throws XMLStreamException {
        if (super.hasNext()) {
            XMLEvent nextEvent = super.nextEvent();
            if (this.fEventFilter.accept(nextEvent)) {
                return nextEvent;
            }
            return nextEvent();
        }
        throw new NoSuchElementException();
    }

    @Override // ohos.javax.xml.stream.XMLEventReader, ohos.javax.xml.stream.util.EventReaderDelegate
    public XMLEvent nextTag() throws XMLStreamException {
        if (super.hasNext()) {
            XMLEvent nextTag = super.nextTag();
            if (this.fEventFilter.accept(nextTag)) {
                return nextTag;
            }
            return nextTag();
        }
        throw new NoSuchElementException();
    }

    @Override // ohos.javax.xml.stream.XMLEventReader, ohos.javax.xml.stream.util.EventReaderDelegate
    public XMLEvent peek() throws XMLStreamException {
        while (true) {
            XMLEvent peek = super.peek();
            if (peek == null) {
                return null;
            }
            if (this.fEventFilter.accept(peek)) {
                return peek;
            }
            super.next();
        }
    }
}
