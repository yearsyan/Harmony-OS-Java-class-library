package ohos.com.sun.org.apache.xml.internal.serializer;

import ohos.com.sun.org.apache.xml.internal.serializer.utils.StringToIntTable;

public final class ElemDesc {
    static final int ASPECIAL = 65536;
    public static final int ATTREMPTY = 4;
    public static final int ATTRURL = 2;
    static final int BLOCK = 8;
    static final int BLOCKFORM = 16;
    static final int BLOCKFORMFIELDSET = 32;
    private static final int CDATA = 64;
    static final int EMPTY = 2;
    private static final int FLOW = 4;
    static final int FONTSTYLE = 4096;
    static final int FORMCTRL = 16384;
    static final int HEAD = 262144;
    static final int HEADELEM = 4194304;
    static final int HEADMISC = 131072;
    private static final int HTMLELEM = 8388608;
    private static final int INLINE = 512;
    private static final int INLINEA = 1024;
    static final int INLINELABEL = 2048;
    static final int LIST = 524288;
    private static final int PCDATA = 128;
    static final int PHRASE = 8192;
    static final int PREFORMATTED = 1048576;
    static final int RAW = 256;
    static final int SPECIAL = 32768;
    static final int WHITESPACESENSITIVE = 2097152;
    private StringToIntTable m_attrs = null;
    private int m_flags;

    ElemDesc(int i) {
        this.m_flags = i;
    }

    private boolean is(int i) {
        return (this.m_flags & i) != 0;
    }

    /* access modifiers changed from: package-private */
    public int getFlags() {
        return this.m_flags;
    }

    /* access modifiers changed from: package-private */
    public void setAttr(String str, int i) {
        if (this.m_attrs == null) {
            this.m_attrs = new StringToIntTable();
        }
        this.m_attrs.put(str, i);
    }

    public boolean isAttrFlagSet(String str, int i) {
        StringToIntTable stringToIntTable = this.m_attrs;
        if (stringToIntTable == null || (stringToIntTable.getIgnoreCase(str) & i) == 0) {
            return false;
        }
        return true;
    }
}
