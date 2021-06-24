package ohos.com.sun.xml.internal.stream;

import java.io.InputStream;
import java.io.Reader;
import ohos.com.sun.org.apache.xerces.internal.impl.Constants;
import ohos.com.sun.org.apache.xerces.internal.impl.PropertyManager;
import ohos.com.sun.org.apache.xerces.internal.impl.XMLStreamFilterImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import ohos.javax.xml.stream.EventFilter;
import ohos.javax.xml.stream.StreamFilter;
import ohos.javax.xml.stream.XMLEventReader;
import ohos.javax.xml.stream.XMLInputFactory;
import ohos.javax.xml.stream.XMLReporter;
import ohos.javax.xml.stream.XMLResolver;
import ohos.javax.xml.stream.XMLStreamException;
import ohos.javax.xml.stream.XMLStreamReader;
import ohos.javax.xml.stream.util.XMLEventAllocator;
import ohos.javax.xml.transform.Source;
import ohos.javax.xml.transform.stream.StreamSource;

public class XMLInputFactoryImpl extends XMLInputFactory {
    private static final boolean DEBUG = false;
    boolean fPropertyChanged = false;
    private PropertyManager fPropertyManager = new PropertyManager(1);
    boolean fReuseInstance = false;
    private XMLStreamReaderImpl fTempReader = null;

    /* access modifiers changed from: package-private */
    public void initEventReader() {
        this.fPropertyChanged = true;
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(InputStream inputStream) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(inputStream));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(Reader reader) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(reader));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(Source source) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(source));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(String str, InputStream inputStream) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(str, inputStream));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(InputStream inputStream, String str) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(inputStream, str));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(String str, Reader reader) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(str, reader));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(XMLStreamReader xMLStreamReader) throws XMLStreamException {
        return new XMLEventReaderImpl(xMLStreamReader);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException {
        return getXMLStreamReaderImpl(new XMLInputSource((String) null, (String) null, (String) null, inputStream, (String) null));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(Reader reader) throws XMLStreamException {
        return getXMLStreamReaderImpl(new XMLInputSource((String) null, (String) null, (String) null, reader, (String) null));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(String str, Reader reader) throws XMLStreamException {
        return getXMLStreamReaderImpl(new XMLInputSource((String) null, str, (String) null, reader, (String) null));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException {
        return new XMLStreamReaderImpl(jaxpSourcetoXMLInputSource(source), new PropertyManager(this.fPropertyManager));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(String str, InputStream inputStream) throws XMLStreamException {
        return getXMLStreamReaderImpl(new XMLInputSource((String) null, str, (String) null, inputStream, (String) null));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(InputStream inputStream, String str) throws XMLStreamException {
        return getXMLStreamReaderImpl(new XMLInputSource((String) null, (String) null, (String) null, inputStream, str));
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventAllocator getEventAllocator() {
        return (XMLEventAllocator) getProperty(XMLInputFactory.ALLOCATOR);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLReporter getXMLReporter() {
        return (XMLReporter) this.fPropertyManager.getProperty(XMLInputFactory.REPORTER);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLResolver getXMLResolver() {
        return (XMLResolver) this.fPropertyManager.getProperty(XMLInputFactory.RESOLVER);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public void setXMLReporter(XMLReporter xMLReporter) {
        this.fPropertyManager.setProperty(XMLInputFactory.REPORTER, xMLReporter);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public void setXMLResolver(XMLResolver xMLResolver) {
        this.fPropertyManager.setProperty(XMLInputFactory.RESOLVER, xMLResolver);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLEventReader createFilteredReader(XMLEventReader xMLEventReader, EventFilter eventFilter) throws XMLStreamException {
        return new EventFilterSupport(xMLEventReader, eventFilter);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public XMLStreamReader createFilteredReader(XMLStreamReader xMLStreamReader, StreamFilter streamFilter) throws XMLStreamException {
        if (xMLStreamReader == null || streamFilter == null) {
            return null;
        }
        return new XMLStreamFilterImpl(xMLStreamReader, streamFilter);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public Object getProperty(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Property not supported");
        } else if (this.fPropertyManager.containsProperty(str)) {
            return this.fPropertyManager.getProperty(str);
        } else {
            throw new IllegalArgumentException("Property not supported");
        }
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public boolean isPropertySupported(String str) {
        if (str == null) {
            return false;
        }
        return this.fPropertyManager.containsProperty(str);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public void setEventAllocator(XMLEventAllocator xMLEventAllocator) {
        this.fPropertyManager.setProperty(XMLInputFactory.ALLOCATOR, xMLEventAllocator);
    }

    @Override // ohos.javax.xml.stream.XMLInputFactory
    public void setProperty(String str, Object obj) throws IllegalArgumentException {
        if (str == null || obj == null || !this.fPropertyManager.containsProperty(str)) {
            throw new IllegalArgumentException("Property " + str + " is not supported");
        }
        if (str == Constants.REUSE_INSTANCE || str.equals(Constants.REUSE_INSTANCE)) {
            this.fReuseInstance = ((Boolean) obj).booleanValue();
        } else {
            this.fPropertyChanged = true;
        }
        this.fPropertyManager.setProperty(str, obj);
    }

    /* access modifiers changed from: package-private */
    public XMLStreamReader getXMLStreamReaderImpl(XMLInputSource xMLInputSource) throws XMLStreamException {
        XMLStreamReaderImpl xMLStreamReaderImpl = this.fTempReader;
        if (xMLStreamReaderImpl == null) {
            this.fPropertyChanged = false;
            XMLStreamReaderImpl xMLStreamReaderImpl2 = new XMLStreamReaderImpl(xMLInputSource, new PropertyManager(this.fPropertyManager));
            this.fTempReader = xMLStreamReaderImpl2;
            return xMLStreamReaderImpl2;
        } else if (!this.fReuseInstance || !xMLStreamReaderImpl.canReuse() || this.fPropertyChanged) {
            this.fPropertyChanged = false;
            XMLStreamReaderImpl xMLStreamReaderImpl3 = new XMLStreamReaderImpl(xMLInputSource, new PropertyManager(this.fPropertyManager));
            this.fTempReader = xMLStreamReaderImpl3;
            return xMLStreamReaderImpl3;
        } else {
            this.fTempReader.reset();
            this.fTempReader.setInputSource(xMLInputSource);
            this.fPropertyChanged = false;
            return this.fTempReader;
        }
    }

    /* access modifiers changed from: package-private */
    public XMLInputSource jaxpSourcetoXMLInputSource(Source source) {
        if (source instanceof StreamSource) {
            StreamSource streamSource = (StreamSource) source;
            String systemId = streamSource.getSystemId();
            String publicId = streamSource.getPublicId();
            InputStream inputStream = streamSource.getInputStream();
            Reader reader = streamSource.getReader();
            if (inputStream != null) {
                return new XMLInputSource(publicId, systemId, (String) null, inputStream, (String) null);
            }
            if (reader != null) {
                return new XMLInputSource(publicId, systemId, (String) null, reader, (String) null);
            }
            return new XMLInputSource(publicId, systemId, null);
        }
        throw new UnsupportedOperationException("Cannot create XMLStreamReader or XMLEventReader from a " + source.getClass().getName());
    }
}
