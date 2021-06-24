package ohos.com.sun.org.apache.xerces.internal.impl.xs;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import ohos.com.sun.org.apache.xerces.internal.util.MessageFormatter;
import ohos.com.sun.org.apache.xerces.internal.utils.SecuritySupport;

public class XSMessageFormatter implements MessageFormatter {
    public static final String SCHEMA_DOMAIN = "http://www.w3.org/TR/xml-schema-1";
    private Locale fLocale = null;
    private ResourceBundle fResourceBundle = null;

    @Override // ohos.com.sun.org.apache.xerces.internal.util.MessageFormatter
    public String formatMessage(Locale locale, String str, Object[] objArr) throws MissingResourceException {
        if (this.fResourceBundle == null || locale != this.fLocale) {
            if (locale != null) {
                this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages", locale);
                this.fLocale = locale;
            }
            if (this.fResourceBundle == null) {
                this.fResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages");
            }
        }
        String string = this.fResourceBundle.getString(str);
        if (objArr != null) {
            try {
                string = MessageFormat.format(string, objArr);
            } catch (Exception unused) {
                string = this.fResourceBundle.getString("FormatFailed") + " " + this.fResourceBundle.getString(str);
            }
        }
        if (string != null) {
            return string;
        }
        throw new MissingResourceException(this.fResourceBundle.getString("BadMessageKey"), "com.sun.org.apache.xerces.internal.impl.msg.SchemaMessages", str);
    }
}
