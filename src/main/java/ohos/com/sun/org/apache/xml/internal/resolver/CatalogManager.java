package ohos.com.sun.org.apache.xml.internal.resolver;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import ohos.com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import ohos.com.sun.org.apache.xml.internal.resolver.helpers.BootstrapResolver;
import ohos.com.sun.org.apache.xml.internal.resolver.helpers.Debug;

public class CatalogManager {
    private static String pAllowPI = "xml.catalog.allowPI";
    private static String pClassname = "xml.catalog.className";
    private static String pFiles = "xml.catalog.files";
    private static String pIgnoreMissing = "xml.catalog.ignoreMissing";
    private static String pPrefer = "xml.catalog.prefer";
    private static String pStatic = "xml.catalog.staticCatalog";
    private static String pVerbosity = "xml.catalog.verbosity";
    private static Catalog staticCatalog = null;
    private static CatalogManager staticManager = new CatalogManager();
    private BootstrapResolver bResolver = new BootstrapResolver();
    private String catalogClassName;
    private String catalogFiles;
    public Debug debug;
    private String defaultCatalogFiles;
    private boolean defaultOasisXMLCatalogPI;
    private boolean defaultPreferPublic;
    private boolean defaultRelativeCatalogs;
    private boolean defaultUseStaticCatalog;
    private int defaultVerbosity;
    private boolean fromPropertiesFile;
    private boolean ignoreMissingProperties;
    private Boolean oasisXMLCatalogPI;
    private boolean overrideDefaultParser;
    private Boolean preferPublic;
    private String propertyFile;
    private URL propertyFileURI;
    private Boolean relativeCatalogs;
    private ResourceBundle resources;
    private Boolean useStaticCatalog;
    private Integer verbosity;

    public CatalogManager() {
        this.ignoreMissingProperties = (SecuritySupport.getSystemProperty(pIgnoreMissing) == null && SecuritySupport.getSystemProperty(pFiles) == null) ? false : true;
        this.propertyFile = "CatalogManager.properties";
        this.propertyFileURI = null;
        this.defaultCatalogFiles = "./xcatalog";
        this.catalogFiles = null;
        this.fromPropertiesFile = false;
        this.defaultVerbosity = 1;
        this.verbosity = null;
        this.defaultPreferPublic = true;
        this.preferPublic = null;
        this.defaultUseStaticCatalog = true;
        this.useStaticCatalog = null;
        this.defaultOasisXMLCatalogPI = true;
        this.oasisXMLCatalogPI = null;
        this.defaultRelativeCatalogs = true;
        this.relativeCatalogs = null;
        this.catalogClassName = null;
        this.debug = null;
        init();
    }

    public CatalogManager(String str) {
        this.ignoreMissingProperties = (SecuritySupport.getSystemProperty(pIgnoreMissing) == null && SecuritySupport.getSystemProperty(pFiles) == null) ? false : true;
        this.propertyFile = "CatalogManager.properties";
        this.propertyFileURI = null;
        this.defaultCatalogFiles = "./xcatalog";
        this.catalogFiles = null;
        this.fromPropertiesFile = false;
        this.defaultVerbosity = 1;
        this.verbosity = null;
        this.defaultPreferPublic = true;
        this.preferPublic = null;
        this.defaultUseStaticCatalog = true;
        this.useStaticCatalog = null;
        this.defaultOasisXMLCatalogPI = true;
        this.oasisXMLCatalogPI = null;
        this.defaultRelativeCatalogs = true;
        this.relativeCatalogs = null;
        this.catalogClassName = null;
        this.debug = null;
        this.propertyFile = str;
        init();
    }

    private void init() {
        this.debug = new Debug();
        if (System.getSecurityManager() == null) {
            this.overrideDefaultParser = true;
        }
    }

    public void setBootstrapResolver(BootstrapResolver bootstrapResolver) {
        this.bResolver = bootstrapResolver;
    }

    public BootstrapResolver getBootstrapResolver() {
        return this.bResolver;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0062 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void readProperties() {
        /*
        // Method dump skipped, instructions count: 192
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.resolver.CatalogManager.readProperties():void");
    }

    public static CatalogManager getStaticManager() {
        return staticManager;
    }

    public boolean getIgnoreMissingProperties() {
        return this.ignoreMissingProperties;
    }

    public void setIgnoreMissingProperties(boolean z) {
        this.ignoreMissingProperties = z;
    }

    public void ignoreMissingProperties(boolean z) {
        setIgnoreMissingProperties(z);
    }

    private int queryVerbosity() {
        String num = Integer.toString(this.defaultVerbosity);
        String systemProperty = SecuritySupport.getSystemProperty(pVerbosity);
        if (systemProperty == null) {
            if (this.resources == null) {
                readProperties();
            }
            ResourceBundle resourceBundle = this.resources;
            if (resourceBundle != null) {
                try {
                    num = resourceBundle.getString("verbosity");
                } catch (MissingResourceException unused) {
                }
            }
        } else {
            num = systemProperty;
        }
        int i = this.defaultVerbosity;
        try {
            i = Integer.parseInt(num.trim());
        } catch (Exception unused2) {
            PrintStream printStream = System.err;
            printStream.println("Cannot parse verbosity: \"" + num + "\"");
        }
        if (this.verbosity == null) {
            this.debug.setDebug(i);
            this.verbosity = new Integer(i);
        }
        return i;
    }

    public int getVerbosity() {
        if (this.verbosity == null) {
            this.verbosity = new Integer(queryVerbosity());
        }
        return this.verbosity.intValue();
    }

    public void setVerbosity(int i) {
        this.verbosity = new Integer(i);
        this.debug.setDebug(i);
    }

    public int verbosity() {
        return getVerbosity();
    }

    private boolean queryRelativeCatalogs() {
        if (this.resources == null) {
            readProperties();
        }
        ResourceBundle resourceBundle = this.resources;
        if (resourceBundle == null) {
            return this.defaultRelativeCatalogs;
        }
        try {
            String string = resourceBundle.getString("relative-catalogs");
            return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("yes") || string.equalsIgnoreCase("1");
        } catch (MissingResourceException unused) {
            return this.defaultRelativeCatalogs;
        }
    }

    public boolean getRelativeCatalogs() {
        if (this.relativeCatalogs == null) {
            this.relativeCatalogs = new Boolean(queryRelativeCatalogs());
        }
        return this.relativeCatalogs.booleanValue();
    }

    public void setRelativeCatalogs(boolean z) {
        this.relativeCatalogs = new Boolean(z);
    }

    public boolean relativeCatalogs() {
        return getRelativeCatalogs();
    }

    private String queryCatalogFiles() {
        String systemProperty = SecuritySupport.getSystemProperty(pFiles);
        this.fromPropertiesFile = false;
        if (systemProperty == null) {
            if (this.resources == null) {
                readProperties();
            }
            ResourceBundle resourceBundle = this.resources;
            if (resourceBundle != null) {
                try {
                    systemProperty = resourceBundle.getString("catalogs");
                    this.fromPropertiesFile = true;
                } catch (MissingResourceException unused) {
                    PrintStream printStream = System.err;
                    printStream.println(this.propertyFile + ": catalogs not found.");
                    systemProperty = null;
                }
            }
        }
        return systemProperty == null ? this.defaultCatalogFiles : systemProperty;
    }

    public Vector getCatalogFiles() {
        if (this.catalogFiles == null) {
            this.catalogFiles = queryCatalogFiles();
        }
        StringTokenizer stringTokenizer = new StringTokenizer(this.catalogFiles, ";");
        Vector vector = new Vector();
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            if (this.fromPropertiesFile && !relativeCatalogs()) {
                try {
                    nextToken = new URL(this.propertyFileURI, nextToken).toString();
                } catch (MalformedURLException unused) {
                }
            }
            vector.add(nextToken);
        }
        return vector;
    }

    public void setCatalogFiles(String str) {
        this.catalogFiles = str;
        this.fromPropertiesFile = false;
    }

    public Vector catalogFiles() {
        return getCatalogFiles();
    }

    private boolean queryPreferPublic() {
        String systemProperty = SecuritySupport.getSystemProperty(pPrefer);
        if (systemProperty == null) {
            if (this.resources == null) {
                readProperties();
            }
            ResourceBundle resourceBundle = this.resources;
            if (resourceBundle == null) {
                return this.defaultPreferPublic;
            }
            try {
                systemProperty = resourceBundle.getString("prefer");
            } catch (MissingResourceException unused) {
                return this.defaultPreferPublic;
            }
        }
        if (systemProperty == null) {
            return this.defaultPreferPublic;
        }
        return systemProperty.equalsIgnoreCase("public");
    }

    public boolean getPreferPublic() {
        if (this.preferPublic == null) {
            this.preferPublic = new Boolean(queryPreferPublic());
        }
        return this.preferPublic.booleanValue();
    }

    public void setPreferPublic(boolean z) {
        this.preferPublic = new Boolean(z);
    }

    public boolean preferPublic() {
        return getPreferPublic();
    }

    private boolean queryUseStaticCatalog() {
        String systemProperty = SecuritySupport.getSystemProperty(pStatic);
        if (systemProperty == null) {
            if (this.resources == null) {
                readProperties();
            }
            ResourceBundle resourceBundle = this.resources;
            if (resourceBundle == null) {
                return this.defaultUseStaticCatalog;
            }
            try {
                systemProperty = resourceBundle.getString("static-catalog");
            } catch (MissingResourceException unused) {
                return this.defaultUseStaticCatalog;
            }
        }
        if (systemProperty == null) {
            return this.defaultUseStaticCatalog;
        }
        return systemProperty.equalsIgnoreCase("true") || systemProperty.equalsIgnoreCase("yes") || systemProperty.equalsIgnoreCase("1");
    }

    public boolean getUseStaticCatalog() {
        if (this.useStaticCatalog == null) {
            this.useStaticCatalog = new Boolean(queryUseStaticCatalog());
        }
        return this.useStaticCatalog.booleanValue();
    }

    public void setUseStaticCatalog(boolean z) {
        this.useStaticCatalog = new Boolean(z);
    }

    public boolean staticCatalog() {
        return getUseStaticCatalog();
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x008d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xml.internal.resolver.Catalog getPrivateCatalog() {
        /*
        // Method dump skipped, instructions count: 144
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.resolver.CatalogManager.getPrivateCatalog():ohos.com.sun.org.apache.xml.internal.resolver.Catalog");
    }

    public Catalog getCatalog() {
        Catalog catalog = staticCatalog;
        if (this.useStaticCatalog == null) {
            this.useStaticCatalog = new Boolean(getUseStaticCatalog());
        }
        if (catalog == null || !this.useStaticCatalog.booleanValue()) {
            catalog = getPrivateCatalog();
            if (this.useStaticCatalog.booleanValue()) {
                staticCatalog = catalog;
            }
        }
        return catalog;
    }

    public boolean queryAllowOasisXMLCatalogPI() {
        String systemProperty = SecuritySupport.getSystemProperty(pAllowPI);
        if (systemProperty == null) {
            if (this.resources == null) {
                readProperties();
            }
            ResourceBundle resourceBundle = this.resources;
            if (resourceBundle == null) {
                return this.defaultOasisXMLCatalogPI;
            }
            try {
                systemProperty = resourceBundle.getString("allow-oasis-xml-catalog-pi");
            } catch (MissingResourceException unused) {
                return this.defaultOasisXMLCatalogPI;
            }
        }
        if (systemProperty == null) {
            return this.defaultOasisXMLCatalogPI;
        }
        return systemProperty.equalsIgnoreCase("true") || systemProperty.equalsIgnoreCase("yes") || systemProperty.equalsIgnoreCase("1");
    }

    public boolean getAllowOasisXMLCatalogPI() {
        if (this.oasisXMLCatalogPI == null) {
            this.oasisXMLCatalogPI = new Boolean(queryAllowOasisXMLCatalogPI());
        }
        return this.oasisXMLCatalogPI.booleanValue();
    }

    public boolean overrideDefaultParser() {
        return this.overrideDefaultParser;
    }

    public void setAllowOasisXMLCatalogPI(boolean z) {
        this.oasisXMLCatalogPI = new Boolean(z);
    }

    public boolean allowOasisXMLCatalogPI() {
        return getAllowOasisXMLCatalogPI();
    }

    public String queryCatalogClassName() {
        String systemProperty = SecuritySupport.getSystemProperty(pClassname);
        if (systemProperty == null) {
            if (this.resources == null) {
                readProperties();
            }
            ResourceBundle resourceBundle = this.resources;
            systemProperty = null;
            if (resourceBundle == null) {
                return null;
            }
            try {
                return resourceBundle.getString("catalog-class-name");
            } catch (MissingResourceException unused) {
            }
        }
        return systemProperty;
    }

    public String getCatalogClassName() {
        if (this.catalogClassName == null) {
            this.catalogClassName = queryCatalogClassName();
        }
        return this.catalogClassName;
    }

    public void setCatalogClassName(String str) {
        this.catalogClassName = str;
    }

    public String catalogClassName() {
        return getCatalogClassName();
    }
}
