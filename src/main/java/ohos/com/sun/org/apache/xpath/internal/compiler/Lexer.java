package ohos.com.sun.org.apache.xpath.internal.compiler;

import java.util.Vector;
import ohos.com.sun.org.apache.xml.internal.utils.ObjectVector;
import ohos.com.sun.org.apache.xml.internal.utils.PrefixResolver;
import ohos.javax.xml.transform.TransformerException;

/* access modifiers changed from: package-private */
public class Lexer {
    static final int TARGETEXTRA = 10000;
    private Compiler m_compiler;
    PrefixResolver m_namespaceContext;
    private int[] m_patternMap = new int[100];
    private int m_patternMapSize;
    XPathParser m_processor;

    Lexer(Compiler compiler, PrefixResolver prefixResolver, XPathParser xPathParser) {
        this.m_compiler = compiler;
        this.m_namespaceContext = prefixResolver;
        this.m_processor = xPathParser;
    }

    /* access modifiers changed from: package-private */
    public void tokenize(String str) throws TransformerException {
        tokenize(str, null);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x012e  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x014c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void tokenize(java.lang.String r18, java.util.Vector r19) throws ohos.javax.xml.transform.TransformerException {
        /*
        // Method dump skipped, instructions count: 510
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xpath.internal.compiler.Lexer.tokenize(java.lang.String, java.util.Vector):void");
    }

    private boolean mapPatternElemPos(int i, boolean z, boolean z2) {
        if (i != 0) {
            return z;
        }
        int i2 = this.m_patternMapSize;
        int[] iArr = this.m_patternMap;
        if (i2 >= iArr.length) {
            int length = iArr.length;
            this.m_patternMap = new int[(i2 + 100)];
            System.arraycopy(iArr, 0, this.m_patternMap, 0, length);
        }
        if (!z) {
            int[] iArr2 = this.m_patternMap;
            int i3 = this.m_patternMapSize - 1;
            iArr2[i3] = iArr2[i3] - 10000;
        }
        this.m_patternMap[this.m_patternMapSize] = (this.m_compiler.getTokenQueueSize() - (z2 ? 1 : 0)) + 10000;
        this.m_patternMapSize++;
        return false;
    }

    private int getTokenQueuePosFromMap(int i) {
        int i2 = this.m_patternMap[i];
        return i2 >= 10000 ? i2 - 10000 : i2;
    }

    private final void resetTokenMark(int i) {
        int tokenQueueSize = this.m_compiler.getTokenQueueSize();
        XPathParser xPathParser = this.m_processor;
        if (i <= 0) {
            i = 0;
        } else if (i <= tokenQueueSize) {
            i--;
        }
        xPathParser.m_queueMark = i;
        if (this.m_processor.m_queueMark < tokenQueueSize) {
            XPathParser xPathParser2 = this.m_processor;
            ObjectVector tokenQueue = this.m_compiler.getTokenQueue();
            XPathParser xPathParser3 = this.m_processor;
            int i2 = xPathParser3.m_queueMark;
            xPathParser3.m_queueMark = i2 + 1;
            xPathParser2.m_token = (String) tokenQueue.elementAt(i2);
            XPathParser xPathParser4 = this.m_processor;
            xPathParser4.m_tokenChar = xPathParser4.m_token.charAt(0);
            return;
        }
        XPathParser xPathParser5 = this.m_processor;
        xPathParser5.m_token = null;
        xPathParser5.m_tokenChar = 0;
    }

    /* access modifiers changed from: package-private */
    public final int getKeywordToken(String str) {
        try {
            Integer keyWord = Keywords.getKeyWord(str);
            if (keyWord != null) {
                return keyWord.intValue();
            }
            return 0;
        } catch (ClassCastException | NullPointerException unused) {
            return 0;
        }
    }

    private void recordTokenString(Vector vector) {
        int tokenQueuePosFromMap = getTokenQueuePosFromMap(this.m_patternMapSize - 1);
        int i = tokenQueuePosFromMap + 1;
        resetTokenMark(i);
        if (this.m_processor.lookahead('(', 1)) {
            int keywordToken = getKeywordToken(this.m_processor.m_token);
            if (keywordToken == 35) {
                vector.addElement("/");
            } else if (keywordToken != 36) {
                switch (keywordToken) {
                    case OpCodes.NODETYPE_COMMENT /*{ENCODED_INT: 1030}*/:
                        vector.addElement(PsuedoNames.PSEUDONAME_COMMENT);
                        return;
                    case OpCodes.NODETYPE_TEXT /*{ENCODED_INT: 1031}*/:
                        vector.addElement(PsuedoNames.PSEUDONAME_TEXT);
                        return;
                    case 1032:
                        vector.addElement("*");
                        return;
                    case OpCodes.NODETYPE_NODE /*{ENCODED_INT: 1033}*/:
                        vector.addElement("*");
                        return;
                    default:
                        vector.addElement("*");
                        return;
                }
            } else {
                vector.addElement("*");
            }
        } else {
            if (this.m_processor.tokenIs('@')) {
                resetTokenMark(i + 1);
                tokenQueuePosFromMap = i;
            }
            if (this.m_processor.lookahead(':', 1)) {
                tokenQueuePosFromMap += 2;
            }
            vector.addElement(this.m_compiler.getTokenQueue().elementAt(tokenQueuePosFromMap));
        }
    }

    private final void addToTokenQueue(String str) {
        this.m_compiler.getTokenQueue().addElement(str);
    }

    private int mapNSTokens(String str, int i, int i2, int i3) throws TransformerException {
        String str2;
        String substring = (i < 0 || i2 < 0) ? "" : str.substring(i, i2);
        if (this.m_namespaceContext == null || substring.equals("*") || substring.equals("xmlns")) {
            str2 = substring;
        } else {
            try {
                str2 = substring.length() > 0 ? this.m_namespaceContext.getNamespaceForPrefix(substring) : this.m_namespaceContext.getNamespaceForPrefix(substring);
            } catch (ClassCastException unused) {
                str2 = this.m_namespaceContext.getNamespaceForPrefix(substring);
            }
        }
        if (str2 == null || str2.length() <= 0) {
            this.m_processor.errorForDOM3("ER_PREFIX_MUST_RESOLVE", new String[]{substring});
            return -1;
        }
        addToTokenQueue(str2);
        addToTokenQueue(":");
        String substring2 = str.substring(i2 + 1, i3);
        if (substring2.length() <= 0) {
            return -1;
        }
        addToTokenQueue(substring2);
        return -1;
    }
}
