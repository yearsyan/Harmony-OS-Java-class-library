package ohos.com.sun.org.apache.xpath.internal.functions;

import ohos.com.sun.org.apache.xalan.internal.res.XSLMessages;

public class FuncSubstring extends Function3Args {
    static final long serialVersionUID = -5996676095024715502L;

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x005c  */
    @Override // ohos.com.sun.org.apache.xpath.internal.functions.Function, ohos.com.sun.org.apache.xpath.internal.Expression
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.com.sun.org.apache.xpath.internal.objects.XObject execute(ohos.com.sun.org.apache.xpath.internal.XPathContext r9) throws ohos.javax.xml.transform.TransformerException {
        /*
        // Method dump skipped, instructions count: 103
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xpath.internal.functions.FuncSubstring.execute(ohos.com.sun.org.apache.xpath.internal.XPathContext):ohos.com.sun.org.apache.xpath.internal.objects.XObject");
    }

    @Override // ohos.com.sun.org.apache.xpath.internal.functions.Function, ohos.com.sun.org.apache.xpath.internal.functions.FunctionOneArg, ohos.com.sun.org.apache.xpath.internal.functions.Function3Args, ohos.com.sun.org.apache.xpath.internal.functions.Function2Args
    public void checkNumberArgs(int i) throws WrongNumberArgsException {
        if (i < 2) {
            reportWrongNumberArgs();
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.com.sun.org.apache.xpath.internal.functions.Function, ohos.com.sun.org.apache.xpath.internal.functions.FunctionOneArg, ohos.com.sun.org.apache.xpath.internal.functions.Function3Args, ohos.com.sun.org.apache.xpath.internal.functions.Function2Args
    public void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_TWO_OR_THREE", null));
    }
}
