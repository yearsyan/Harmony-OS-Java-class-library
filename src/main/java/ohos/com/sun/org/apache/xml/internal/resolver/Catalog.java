package ohos.com.sun.org.apache.xml.internal.resolver;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import ohos.com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import ohos.com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import ohos.com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import ohos.com.sun.org.apache.xml.internal.resolver.readers.CatalogReader;
import ohos.com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader;
import ohos.com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
import ohos.com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
import ohos.data.search.model.IndexType;
import ohos.global.icu.impl.PatternTokenizer;
import ohos.javax.xml.parsers.SAXParserFactory;
import ohos.jdk.xml.internal.JdkXmlUtils;
import ohos.miscservices.pasteboard.PasteData;

public class Catalog {
    public static final int BASE = CatalogEntry.addEntryType("BASE", 1);
    public static final int CATALOG = CatalogEntry.addEntryType("CATALOG", 1);
    public static final int DELEGATE_PUBLIC = CatalogEntry.addEntryType("DELEGATE_PUBLIC", 2);
    public static final int DELEGATE_SYSTEM = CatalogEntry.addEntryType("DELEGATE_SYSTEM", 2);
    public static final int DELEGATE_URI = CatalogEntry.addEntryType("DELEGATE_URI", 2);
    public static final int DOCTYPE = CatalogEntry.addEntryType("DOCTYPE", 2);
    public static final int DOCUMENT = CatalogEntry.addEntryType("DOCUMENT", 1);
    public static final int DTDDECL = CatalogEntry.addEntryType("DTDDECL", 2);
    public static final int ENTITY = CatalogEntry.addEntryType(SchemaSymbols.ATTVAL_ENTITY, 2);
    public static final int LINKTYPE = CatalogEntry.addEntryType("LINKTYPE", 2);
    public static final int NOTATION = CatalogEntry.addEntryType(SchemaSymbols.ATTVAL_NOTATION, 2);
    public static final int OVERRIDE = CatalogEntry.addEntryType("OVERRIDE", 1);
    public static final int PUBLIC = CatalogEntry.addEntryType("PUBLIC", 2);
    public static final int REWRITE_SYSTEM = CatalogEntry.addEntryType("REWRITE_SYSTEM", 2);
    public static final int REWRITE_URI = CatalogEntry.addEntryType("REWRITE_URI", 2);
    public static final int SGMLDECL = CatalogEntry.addEntryType("SGMLDECL", 1);
    public static final int SYSTEM = CatalogEntry.addEntryType("SYSTEM", 2);
    public static final int SYSTEM_SUFFIX = CatalogEntry.addEntryType("SYSTEM_SUFFIX", 2);
    public static final int URI = CatalogEntry.addEntryType("URI", 2);
    public static final int URI_SUFFIX = CatalogEntry.addEntryType("URI_SUFFIX", 2);
    protected URL base;
    protected URL catalogCwd;
    protected Vector catalogEntries = new Vector();
    protected Vector catalogFiles = new Vector();
    protected CatalogManager catalogManager = CatalogManager.getStaticManager();
    protected Vector catalogs = new Vector();
    protected boolean default_override = true;
    protected Vector localCatalogFiles = new Vector();
    protected Vector localDelegate = new Vector();
    protected Vector readerArr = new Vector();
    protected Map<String, Integer> readerMap = new HashMap();

    public Catalog() {
    }

    public Catalog(CatalogManager catalogManager2) {
        this.catalogManager = catalogManager2;
    }

    public CatalogManager getCatalogManager() {
        return this.catalogManager;
    }

    public void setCatalogManager(CatalogManager catalogManager2) {
        this.catalogManager = catalogManager2;
    }

    public void setupReaders() {
        SAXParserFactory sAXFactory = JdkXmlUtils.getSAXFactory(this.catalogManager.overrideDefaultParser());
        sAXFactory.setValidating(false);
        SAXCatalogReader sAXCatalogReader = new SAXCatalogReader(sAXFactory);
        sAXCatalogReader.setCatalogParser(null, "XMLCatalog", "ohos.com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader");
        sAXCatalogReader.setCatalogParser(OASISXMLCatalogReader.namespaceName, "catalog", "ohos.com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader");
        addReader("application/xml", sAXCatalogReader);
        addReader(PasteData.MIMETYPE_TEXT_PLAIN, new TR9401CatalogReader());
    }

    public void addReader(String str, CatalogReader catalogReader) {
        if (this.readerMap.containsKey(str)) {
            this.readerArr.set(this.readerMap.get(str).intValue(), catalogReader);
            return;
        }
        this.readerArr.add(catalogReader);
        this.readerMap.put(str, Integer.valueOf(this.readerArr.size() - 1));
    }

    /* access modifiers changed from: protected */
    public void copyReaders(Catalog catalog) {
        Vector vector = new Vector(this.readerMap.size());
        for (int i = 0; i < this.readerMap.size(); i++) {
            vector.add(null);
        }
        for (Map.Entry<String, Integer> entry : this.readerMap.entrySet()) {
            vector.set(entry.getValue().intValue(), entry.getKey());
        }
        for (int i2 = 0; i2 < vector.size(); i2++) {
            String str = (String) vector.get(i2);
            catalog.addReader(str, (CatalogReader) this.readerArr.get(this.readerMap.get(str).intValue()));
        }
    }

    /* access modifiers changed from: protected */
    public Catalog newCatalog() {
        String name = getClass().getName();
        try {
            Catalog catalog = (Catalog) Class.forName(name).newInstance();
            catalog.setCatalogManager(this.catalogManager);
            copyReaders(catalog);
            return catalog;
        } catch (ClassNotFoundException unused) {
            Debug debug = this.catalogManager.debug;
            debug.message(1, "Class Not Found Exception: " + name);
            Catalog catalog2 = new Catalog();
            catalog2.setCatalogManager(this.catalogManager);
            copyReaders(catalog2);
            return catalog2;
        } catch (IllegalAccessException unused2) {
            Debug debug2 = this.catalogManager.debug;
            debug2.message(1, "Illegal Access Exception: " + name);
            Catalog catalog22 = new Catalog();
            catalog22.setCatalogManager(this.catalogManager);
            copyReaders(catalog22);
            return catalog22;
        } catch (InstantiationException unused3) {
            Debug debug3 = this.catalogManager.debug;
            debug3.message(1, "Instantiation Exception: " + name);
            Catalog catalog222 = new Catalog();
            catalog222.setCatalogManager(this.catalogManager);
            copyReaders(catalog222);
            return catalog222;
        } catch (ClassCastException unused4) {
            Debug debug4 = this.catalogManager.debug;
            debug4.message(1, "Class Cast Exception: " + name);
            Catalog catalog2222 = new Catalog();
            catalog2222.setCatalogManager(this.catalogManager);
            copyReaders(catalog2222);
            return catalog2222;
        } catch (Exception unused5) {
            Debug debug5 = this.catalogManager.debug;
            debug5.message(1, "Other Exception: " + name);
            Catalog catalog22222 = new Catalog();
            catalog22222.setCatalogManager(this.catalogManager);
            copyReaders(catalog22222);
            return catalog22222;
        }
    }

    public String getCurrentBase() {
        return this.base.toString();
    }

    public String getDefaultOverride() {
        return this.default_override ? "yes" : IndexType.NO;
    }

    public void loadSystemCatalogs() throws MalformedURLException, IOException {
        Vector catalogFiles2 = this.catalogManager.getCatalogFiles();
        if (catalogFiles2 != null) {
            for (int i = 0; i < catalogFiles2.size(); i++) {
                this.catalogFiles.addElement(catalogFiles2.elementAt(i));
            }
        }
        if (this.catalogFiles.size() > 0) {
            String str = (String) this.catalogFiles.lastElement();
            this.catalogFiles.removeElement(str);
            parseCatalog(str);
        }
    }

    public synchronized void parseCatalog(String str) throws MalformedURLException, IOException {
        this.default_override = this.catalogManager.getPreferPublic();
        Debug debug = this.catalogManager.debug;
        debug.message(4, "Parse catalog: " + str);
        this.catalogFiles.addElement(str);
        parsePendingCatalogs();
    }

    public synchronized void parseCatalog(String str, InputStream inputStream) throws IOException, CatalogException {
        this.default_override = this.catalogManager.getPreferPublic();
        this.catalogManager.debug.message(4, "Parse " + str + " catalog on input stream");
        CatalogReader catalogReader = null;
        if (this.readerMap.containsKey(str)) {
            catalogReader = (CatalogReader) this.readerArr.get(this.readerMap.get(str).intValue());
        }
        if (catalogReader != null) {
            catalogReader.readCatalog(this, inputStream);
            parsePendingCatalogs();
        } else {
            String str2 = "No CatalogReader for MIME type: " + str;
            this.catalogManager.debug.message(2, str2);
            throw new CatalogException(6, str2);
        }
    }

    public synchronized void parseCatalog(URL url) throws IOException {
        this.catalogCwd = url;
        this.base = url;
        this.default_override = this.catalogManager.getPreferPublic();
        Debug debug = this.catalogManager.debug;
        debug.message(4, "Parse catalog: " + url.toString());
        boolean z = false;
        int i = 0;
        while (!z && i < this.readerArr.size()) {
            CatalogReader catalogReader = (CatalogReader) this.readerArr.get(i);
            try {
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                try {
                    catalogReader.readCatalog(this, dataInputStream);
                    z = true;
                } catch (CatalogException e) {
                    if (e.getExceptionType() == 7) {
                        break;
                    }
                }
                try {
                    dataInputStream.close();
                } catch (IOException unused) {
                }
                i++;
            } catch (FileNotFoundException unused2) {
            }
        }
        if (z) {
            parsePendingCatalogs();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void parsePendingCatalogs() throws MalformedURLException, IOException {
        if (!this.localCatalogFiles.isEmpty()) {
            Vector vector = new Vector();
            Enumeration elements = this.localCatalogFiles.elements();
            while (elements.hasMoreElements()) {
                vector.addElement(elements.nextElement());
            }
            for (int i = 0; i < this.catalogFiles.size(); i++) {
                vector.addElement((String) this.catalogFiles.elementAt(i));
            }
            this.catalogFiles = vector;
            this.localCatalogFiles.clear();
        }
        if (this.catalogFiles.isEmpty() && !this.localDelegate.isEmpty()) {
            Enumeration elements2 = this.localDelegate.elements();
            while (elements2.hasMoreElements()) {
                this.catalogEntries.addElement(elements2.nextElement());
            }
            this.localDelegate.clear();
        }
        while (!this.catalogFiles.isEmpty()) {
            String str = (String) this.catalogFiles.elementAt(0);
            try {
                this.catalogFiles.remove(0);
            } catch (ArrayIndexOutOfBoundsException unused) {
            }
            if (this.catalogEntries.size() == 0 && this.catalogs.size() == 0) {
                try {
                    parseCatalogFile(str);
                } catch (CatalogException e) {
                    System.out.println("FIXME: " + e.toString());
                }
            } else {
                this.catalogs.addElement(str);
            }
            if (!this.localCatalogFiles.isEmpty()) {
                Vector vector2 = new Vector();
                Enumeration elements3 = this.localCatalogFiles.elements();
                while (elements3.hasMoreElements()) {
                    vector2.addElement(elements3.nextElement());
                }
                for (int i2 = 0; i2 < this.catalogFiles.size(); i2++) {
                    vector2.addElement((String) this.catalogFiles.elementAt(i2));
                }
                this.catalogFiles = vector2;
                this.localCatalogFiles.clear();
            }
            if (!this.localDelegate.isEmpty()) {
                Enumeration elements4 = this.localDelegate.elements();
                while (elements4.hasMoreElements()) {
                    this.catalogEntries.addElement(elements4.nextElement());
                }
                this.localDelegate.clear();
            }
        }
        this.catalogFiles.clear();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x000f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x002b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void parseCatalogFile(java.lang.String r8) throws java.net.MalformedURLException, java.io.IOException, ohos.com.sun.org.apache.xml.internal.resolver.CatalogException {
        /*
        // Method dump skipped, instructions count: 200
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.resolver.Catalog.parseCatalogFile(java.lang.String):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:2|(1:4)(1:5)|6|7|8|9|10|(1:14)|15|75) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005a, code lost:
        r6.catalogManager.debug.message(1, "Malformed URL on base", r7);
        r0 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0043 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addEntry(ohos.com.sun.org.apache.xml.internal.resolver.CatalogEntry r7) {
        /*
        // Method dump skipped, instructions count: 896
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.resolver.Catalog.addEntry(ohos.com.sun.org.apache.xml.internal.resolver.CatalogEntry):void");
    }

    public void unknownEntry(Vector vector) {
        if (vector != null && vector.size() > 0) {
            this.catalogManager.debug.message(2, "Unrecognized token parsing catalog", (String) vector.elementAt(0));
        }
    }

    public void parseAllCatalogs() throws MalformedURLException, IOException {
        for (int i = 0; i < this.catalogs.size(); i++) {
            try {
                Catalog catalog = (Catalog) this.catalogs.elementAt(i);
            } catch (ClassCastException unused) {
                Catalog newCatalog = newCatalog();
                newCatalog.parseCatalog((String) this.catalogs.elementAt(i));
                this.catalogs.setElementAt(newCatalog, i);
                newCatalog.parseAllCatalogs();
            }
        }
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == DELEGATE_PUBLIC || catalogEntry.getEntryType() == DELEGATE_SYSTEM || catalogEntry.getEntryType() == DELEGATE_URI) {
                newCatalog().parseCatalog(catalogEntry.getEntryArg(1));
            }
        }
    }

    public String resolveDoctype(String str, String str2, String str3) throws MalformedURLException, IOException {
        String resolveLocalPublic;
        String resolveLocalSystem;
        Debug debug = this.catalogManager.debug;
        debug.message(3, "resolveDoctype(" + str + "," + str2 + "," + str3 + ")");
        String normalizeURI = normalizeURI(str3);
        if (str2 != null && str2.startsWith("urn:publicid:")) {
            str2 = PublicId.decodeURN(str2);
        }
        String str4 = null;
        if (normalizeURI == null || !normalizeURI.startsWith("urn:publicid:")) {
            str4 = normalizeURI;
        } else {
            String decodeURN = PublicId.decodeURN(normalizeURI);
            if (str2 == null || str2.equals(decodeURN)) {
                str2 = decodeURN;
            } else {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            }
        }
        if (str4 != null && (resolveLocalSystem = resolveLocalSystem(str4)) != null) {
            return resolveLocalSystem;
        }
        if (str2 != null && (resolveLocalPublic = resolveLocalPublic(DOCTYPE, str, str2, str4)) != null) {
            return resolveLocalPublic;
        }
        boolean z = this.default_override;
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == OVERRIDE) {
                z = catalogEntry.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (catalogEntry.getEntryType() == DOCTYPE && catalogEntry.getEntryArg(0).equals(str)) {
                if (z || str4 == null) {
                    return catalogEntry.getEntryArg(1);
                }
            }
        }
        return resolveSubordinateCatalogs(DOCTYPE, str, str2, str4);
    }

    public String resolveDocument() throws MalformedURLException, IOException {
        this.catalogManager.debug.message(3, "resolveDocument");
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == DOCUMENT) {
                return catalogEntry.getEntryArg(0);
            }
        }
        return resolveSubordinateCatalogs(DOCUMENT, null, null, null);
    }

    public String resolveEntity(String str, String str2, String str3) throws MalformedURLException, IOException {
        String resolveLocalPublic;
        String resolveLocalSystem;
        Debug debug = this.catalogManager.debug;
        debug.message(3, "resolveEntity(" + str + "," + str2 + "," + str3 + ")");
        String normalizeURI = normalizeURI(str3);
        if (str2 != null && str2.startsWith("urn:publicid:")) {
            str2 = PublicId.decodeURN(str2);
        }
        String str4 = null;
        if (normalizeURI == null || !normalizeURI.startsWith("urn:publicid:")) {
            str4 = normalizeURI;
        } else {
            String decodeURN = PublicId.decodeURN(normalizeURI);
            if (str2 == null || str2.equals(decodeURN)) {
                str2 = decodeURN;
            } else {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            }
        }
        if (str4 != null && (resolveLocalSystem = resolveLocalSystem(str4)) != null) {
            return resolveLocalSystem;
        }
        if (str2 != null && (resolveLocalPublic = resolveLocalPublic(ENTITY, str, str2, str4)) != null) {
            return resolveLocalPublic;
        }
        boolean z = this.default_override;
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == OVERRIDE) {
                z = catalogEntry.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (catalogEntry.getEntryType() == ENTITY && catalogEntry.getEntryArg(0).equals(str)) {
                if (z || str4 == null) {
                    return catalogEntry.getEntryArg(1);
                }
            }
        }
        return resolveSubordinateCatalogs(ENTITY, str, str2, str4);
    }

    public String resolveNotation(String str, String str2, String str3) throws MalformedURLException, IOException {
        String resolveLocalPublic;
        String resolveLocalSystem;
        Debug debug = this.catalogManager.debug;
        debug.message(3, "resolveNotation(" + str + "," + str2 + "," + str3 + ")");
        String normalizeURI = normalizeURI(str3);
        if (str2 != null && str2.startsWith("urn:publicid:")) {
            str2 = PublicId.decodeURN(str2);
        }
        String str4 = null;
        if (normalizeURI == null || !normalizeURI.startsWith("urn:publicid:")) {
            str4 = normalizeURI;
        } else {
            String decodeURN = PublicId.decodeURN(normalizeURI);
            if (str2 == null || str2.equals(decodeURN)) {
                str2 = decodeURN;
            } else {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            }
        }
        if (str4 != null && (resolveLocalSystem = resolveLocalSystem(str4)) != null) {
            return resolveLocalSystem;
        }
        if (str2 != null && (resolveLocalPublic = resolveLocalPublic(NOTATION, str, str2, str4)) != null) {
            return resolveLocalPublic;
        }
        boolean z = this.default_override;
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == OVERRIDE) {
                z = catalogEntry.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (catalogEntry.getEntryType() == NOTATION && catalogEntry.getEntryArg(0).equals(str)) {
                if (z || str4 == null) {
                    return catalogEntry.getEntryArg(1);
                }
            }
        }
        return resolveSubordinateCatalogs(NOTATION, str, str2, str4);
    }

    public String resolvePublic(String str, String str2) throws MalformedURLException, IOException {
        String resolveLocalSystem;
        this.catalogManager.debug.message(3, "resolvePublic(" + str + "," + str2 + ")");
        String normalizeURI = normalizeURI(str2);
        if (str != null && str.startsWith("urn:publicid:")) {
            str = PublicId.decodeURN(str);
        }
        if (normalizeURI != null && normalizeURI.startsWith("urn:publicid:")) {
            String decodeURN = PublicId.decodeURN(normalizeURI);
            if (str == null || str.equals(decodeURN)) {
                str = decodeURN;
            } else {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            }
            normalizeURI = null;
        }
        if (normalizeURI != null && (resolveLocalSystem = resolveLocalSystem(normalizeURI)) != null) {
            return resolveLocalSystem;
        }
        String resolveLocalPublic = resolveLocalPublic(PUBLIC, null, str, normalizeURI);
        if (resolveLocalPublic != null) {
            return resolveLocalPublic;
        }
        return resolveSubordinateCatalogs(PUBLIC, null, str, normalizeURI);
    }

    /* access modifiers changed from: protected */
    public synchronized String resolveLocalPublic(int i, String str, String str2, String str3) throws MalformedURLException, IOException {
        String resolveLocalSystem;
        String normalize = PublicId.normalize(str2);
        if (str3 != null && (resolveLocalSystem = resolveLocalSystem(str3)) != null) {
            return resolveLocalSystem;
        }
        boolean z = this.default_override;
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == OVERRIDE) {
                z = catalogEntry.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (catalogEntry.getEntryType() == PUBLIC && catalogEntry.getEntryArg(0).equals(normalize)) {
                if (z || str3 == null) {
                    return catalogEntry.getEntryArg(1);
                }
            }
        }
        boolean z2 = this.default_override;
        Enumeration elements2 = this.catalogEntries.elements();
        Vector vector = new Vector();
        while (elements2.hasMoreElements()) {
            CatalogEntry catalogEntry2 = (CatalogEntry) elements2.nextElement();
            if (catalogEntry2.getEntryType() == OVERRIDE) {
                z2 = catalogEntry2.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (catalogEntry2.getEntryType() == DELEGATE_PUBLIC && (z2 || str3 == null)) {
                String entryArg = catalogEntry2.getEntryArg(0);
                if (entryArg.length() <= normalize.length() && entryArg.equals(normalize.substring(0, entryArg.length()))) {
                    vector.addElement(catalogEntry2.getEntryArg(1));
                }
            }
        }
        if (vector.size() <= 0) {
            return null;
        }
        Enumeration elements3 = vector.elements();
        if (this.catalogManager.debug.getDebug() > 1) {
            this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
            while (elements3.hasMoreElements()) {
                Debug debug = this.catalogManager.debug;
                debug.message(2, "\t" + ((String) elements3.nextElement()));
            }
        }
        Catalog newCatalog = newCatalog();
        Enumeration elements4 = vector.elements();
        while (elements4.hasMoreElements()) {
            newCatalog.parseCatalog((String) elements4.nextElement());
        }
        return newCatalog.resolvePublic(normalize, null);
    }

    public String resolveSystem(String str) throws MalformedURLException, IOException {
        String resolveLocalSystem;
        Debug debug = this.catalogManager.debug;
        debug.message(3, "resolveSystem(" + str + ")");
        String normalizeURI = normalizeURI(str);
        if (normalizeURI != null && normalizeURI.startsWith("urn:publicid:")) {
            return resolvePublic(PublicId.decodeURN(normalizeURI), null);
        }
        if (normalizeURI == null || (resolveLocalSystem = resolveLocalSystem(normalizeURI)) == null) {
            return resolveSubordinateCatalogs(SYSTEM, null, null, normalizeURI);
        }
        return resolveLocalSystem;
    }

    /* access modifiers changed from: protected */
    public String resolveLocalSystem(String str) throws MalformedURLException, IOException {
        boolean z = SecuritySupport.getSystemProperty("os.name").indexOf("Windows") >= 0;
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == SYSTEM && (catalogEntry.getEntryArg(0).equals(str) || (z && catalogEntry.getEntryArg(0).equalsIgnoreCase(str)))) {
                return catalogEntry.getEntryArg(1);
            }
        }
        Enumeration elements2 = this.catalogEntries.elements();
        String str2 = null;
        String str3 = null;
        while (elements2.hasMoreElements()) {
            CatalogEntry catalogEntry2 = (CatalogEntry) elements2.nextElement();
            if (catalogEntry2.getEntryType() == REWRITE_SYSTEM) {
                String entryArg = catalogEntry2.getEntryArg(0);
                if (entryArg.length() <= str.length() && entryArg.equals(str.substring(0, entryArg.length()))) {
                    if (str3 == null || entryArg.length() > str3.length()) {
                        str2 = catalogEntry2.getEntryArg(1);
                        str3 = entryArg;
                    }
                }
            }
        }
        if (str2 != null) {
            return str2 + str.substring(str3.length());
        }
        Enumeration elements3 = this.catalogEntries.elements();
        String str4 = null;
        String str5 = null;
        while (elements3.hasMoreElements()) {
            CatalogEntry catalogEntry3 = (CatalogEntry) elements3.nextElement();
            if (catalogEntry3.getEntryType() == SYSTEM_SUFFIX) {
                String entryArg2 = catalogEntry3.getEntryArg(0);
                if (entryArg2.length() <= str.length() && str.endsWith(entryArg2)) {
                    if (str5 == null || entryArg2.length() > str5.length()) {
                        str4 = catalogEntry3.getEntryArg(1);
                        str5 = entryArg2;
                    }
                }
            }
        }
        if (str4 != null) {
            return str4;
        }
        Enumeration elements4 = this.catalogEntries.elements();
        Vector vector = new Vector();
        while (elements4.hasMoreElements()) {
            CatalogEntry catalogEntry4 = (CatalogEntry) elements4.nextElement();
            if (catalogEntry4.getEntryType() == DELEGATE_SYSTEM) {
                String entryArg3 = catalogEntry4.getEntryArg(0);
                if (entryArg3.length() <= str.length() && entryArg3.equals(str.substring(0, entryArg3.length()))) {
                    vector.addElement(catalogEntry4.getEntryArg(1));
                }
            }
        }
        if (vector.size() <= 0) {
            return null;
        }
        Enumeration elements5 = vector.elements();
        if (this.catalogManager.debug.getDebug() > 1) {
            this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
            while (elements5.hasMoreElements()) {
                this.catalogManager.debug.message(2, "\t" + ((String) elements5.nextElement()));
            }
        }
        Catalog newCatalog = newCatalog();
        Enumeration elements6 = vector.elements();
        while (elements6.hasMoreElements()) {
            newCatalog.parseCatalog((String) elements6.nextElement());
        }
        return newCatalog.resolveSystem(str);
    }

    public String resolveURI(String str) throws MalformedURLException, IOException {
        String resolveLocalURI;
        Debug debug = this.catalogManager.debug;
        debug.message(3, "resolveURI(" + str + ")");
        String normalizeURI = normalizeURI(str);
        if (normalizeURI != null && normalizeURI.startsWith("urn:publicid:")) {
            return resolvePublic(PublicId.decodeURN(normalizeURI), null);
        }
        if (normalizeURI == null || (resolveLocalURI = resolveLocalURI(normalizeURI)) == null) {
            return resolveSubordinateCatalogs(URI, null, null, normalizeURI);
        }
        return resolveLocalURI;
    }

    /* access modifiers changed from: protected */
    public String resolveLocalURI(String str) throws MalformedURLException, IOException {
        Enumeration elements = this.catalogEntries.elements();
        while (elements.hasMoreElements()) {
            CatalogEntry catalogEntry = (CatalogEntry) elements.nextElement();
            if (catalogEntry.getEntryType() == URI && catalogEntry.getEntryArg(0).equals(str)) {
                return catalogEntry.getEntryArg(1);
            }
        }
        Enumeration elements2 = this.catalogEntries.elements();
        String str2 = null;
        String str3 = null;
        while (elements2.hasMoreElements()) {
            CatalogEntry catalogEntry2 = (CatalogEntry) elements2.nextElement();
            if (catalogEntry2.getEntryType() == REWRITE_URI) {
                String entryArg = catalogEntry2.getEntryArg(0);
                if (entryArg.length() <= str.length() && entryArg.equals(str.substring(0, entryArg.length()))) {
                    if (str3 == null || entryArg.length() > str3.length()) {
                        str2 = catalogEntry2.getEntryArg(1);
                        str3 = entryArg;
                    }
                }
            }
        }
        if (str2 != null) {
            return str2 + str.substring(str3.length());
        }
        Enumeration elements3 = this.catalogEntries.elements();
        String str4 = null;
        String str5 = null;
        while (elements3.hasMoreElements()) {
            CatalogEntry catalogEntry3 = (CatalogEntry) elements3.nextElement();
            if (catalogEntry3.getEntryType() == URI_SUFFIX) {
                String entryArg2 = catalogEntry3.getEntryArg(0);
                if (entryArg2.length() <= str.length() && str.endsWith(entryArg2)) {
                    if (str5 == null || entryArg2.length() > str5.length()) {
                        str4 = catalogEntry3.getEntryArg(1);
                        str5 = entryArg2;
                    }
                }
            }
        }
        if (str4 != null) {
            return str4;
        }
        Enumeration elements4 = this.catalogEntries.elements();
        Vector vector = new Vector();
        while (elements4.hasMoreElements()) {
            CatalogEntry catalogEntry4 = (CatalogEntry) elements4.nextElement();
            if (catalogEntry4.getEntryType() == DELEGATE_URI) {
                String entryArg3 = catalogEntry4.getEntryArg(0);
                if (entryArg3.length() <= str.length() && entryArg3.equals(str.substring(0, entryArg3.length()))) {
                    vector.addElement(catalogEntry4.getEntryArg(1));
                }
            }
        }
        if (vector.size() <= 0) {
            return null;
        }
        Enumeration elements5 = vector.elements();
        if (this.catalogManager.debug.getDebug() > 1) {
            this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
            while (elements5.hasMoreElements()) {
                this.catalogManager.debug.message(2, "\t" + ((String) elements5.nextElement()));
            }
        }
        Catalog newCatalog = newCatalog();
        Enumeration elements6 = vector.elements();
        while (elements6.hasMoreElements()) {
            newCatalog.parseCatalog((String) elements6.nextElement());
        }
        return newCatalog.resolveURI(str);
    }

    /* access modifiers changed from: protected */
    public synchronized String resolveSubordinateCatalogs(int i, String str, String str2, String str3) throws MalformedURLException, IOException {
        Catalog catalog;
        int i2 = 0;
        while (true) {
            String str4 = null;
            if (i2 >= this.catalogs.size()) {
                return null;
            }
            try {
                catalog = (Catalog) this.catalogs.elementAt(i2);
            } catch (ClassCastException unused) {
                String str5 = (String) this.catalogs.elementAt(i2);
                Catalog newCatalog = newCatalog();
                try {
                    newCatalog.parseCatalog(str5);
                } catch (MalformedURLException unused2) {
                    this.catalogManager.debug.message(1, "Malformed Catalog URL", str5);
                } catch (FileNotFoundException unused3) {
                    this.catalogManager.debug.message(1, "Failed to load catalog, file not found", str5);
                } catch (IOException unused4) {
                    this.catalogManager.debug.message(1, "Failed to load catalog, I/O error", str5);
                }
                this.catalogs.setElementAt(newCatalog, i2);
                catalog = newCatalog;
            }
            if (i == DOCTYPE) {
                str4 = catalog.resolveDoctype(str, str2, str3);
            } else if (i == DOCUMENT) {
                str4 = catalog.resolveDocument();
            } else if (i == ENTITY) {
                str4 = catalog.resolveEntity(str, str2, str3);
            } else if (i == NOTATION) {
                str4 = catalog.resolveNotation(str, str2, str3);
            } else if (i == PUBLIC) {
                str4 = catalog.resolvePublic(str2, str3);
            } else if (i == SYSTEM) {
                str4 = catalog.resolveSystem(str3);
            } else if (i == URI) {
                str4 = catalog.resolveURI(str3);
            }
            if (str4 != null) {
                return str4;
            }
            i2++;
        }
    }

    /* access modifiers changed from: protected */
    public String fixSlashes(String str) {
        return str.replace(PatternTokenizer.BACK_SLASH, '/');
    }

    /* access modifiers changed from: protected */
    public String makeAbsolute(String str) {
        URL url;
        String fixSlashes = fixSlashes(str);
        try {
            url = new URL(this.base, fixSlashes);
        } catch (MalformedURLException unused) {
            this.catalogManager.debug.message(1, "Malformed URL on system identifier", fixSlashes);
            url = null;
        }
        return url != null ? url.toString() : fixSlashes;
    }

    /* access modifiers changed from: protected */
    public String normalizeURI(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes("UTF-8");
            StringBuilder sb = new StringBuilder(bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                int i2 = bytes[i] & 255;
                if (i2 <= 32 || i2 > 127 || i2 == 34 || i2 == 60 || i2 == 62 || i2 == 92 || i2 == 94 || i2 == 96 || i2 == 123 || i2 == 124 || i2 == 125 || i2 == 127) {
                    sb.append(encodedByte(i2));
                } else {
                    sb.append((char) bytes[i]);
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException unused) {
            this.catalogManager.debug.message(1, "UTF-8 is an unsupported encoding!?");
            return str;
        }
    }

    /* access modifiers changed from: protected */
    public String encodedByte(int i) {
        String upperCase = Integer.toHexString(i).toUpperCase();
        if (upperCase.length() < 2) {
            return "%0" + upperCase;
        }
        return "%" + upperCase;
    }

    /* access modifiers changed from: protected */
    public void addDelegate(CatalogEntry catalogEntry) {
        String entryArg = catalogEntry.getEntryArg(0);
        Enumeration elements = this.localDelegate.elements();
        int i = 0;
        while (elements.hasMoreElements()) {
            String entryArg2 = ((CatalogEntry) elements.nextElement()).getEntryArg(0);
            if (!entryArg2.equals(entryArg)) {
                if (entryArg2.length() > entryArg.length()) {
                    i++;
                }
                if (entryArg2.length() < entryArg.length()) {
                    break;
                }
            } else {
                return;
            }
        }
        if (this.localDelegate.size() == 0) {
            this.localDelegate.addElement(catalogEntry);
        } else {
            this.localDelegate.insertElementAt(catalogEntry, i);
        }
    }
}
