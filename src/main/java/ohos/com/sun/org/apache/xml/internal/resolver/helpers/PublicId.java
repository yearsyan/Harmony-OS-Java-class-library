package ohos.com.sun.org.apache.xml.internal.resolver.helpers;

import ohos.com.sun.org.apache.xml.internal.serializer.CharInfo;

public abstract class PublicId {
    protected PublicId() {
    }

    public static String normalize(String str) {
        String trim = str.replace('\t', ' ').replace(CharInfo.S_CARRIAGERETURN, ' ').replace('\n', ' ').trim();
        while (true) {
            int indexOf = trim.indexOf("  ");
            if (indexOf < 0) {
                return trim;
            }
            trim = trim.substring(0, indexOf) + trim.substring(indexOf + 1);
        }
    }

    public static String encodeURN(String str) {
        String stringReplace = stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(normalize(str), "%", "%25"), ";", "%3B"), "'", "%27"), "?", "%3F"), "#", "%23"), "+", "%2B"), " ", "+"), "::", ";"), ":", "%3A"), "//", ":"), "/", "%2F");
        return "urn:publicid:" + stringReplace;
    }

    public static String decodeURN(String str) {
        return str.startsWith("urn:publicid:") ? stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(str.substring(13), "%2F", "/"), ":", "//"), "%3A", ":"), ";", "::"), "+", " "), "%2B", "+"), "%23", "#"), "%3F", "?"), "%27", "'"), "%3B", ";"), "%25", "%") : str;
    }

    private static String stringReplace(String str, String str2, String str3) {
        int indexOf = str.indexOf(str2);
        String str4 = "";
        while (indexOf >= 0) {
            str4 = (str4 + str.substring(0, indexOf)) + str3;
            str = str.substring(indexOf + 1);
            indexOf = str.indexOf(str2);
        }
        return str4 + str;
    }
}
