package ohos.com.sun.org.apache.xalan.internal.xsltc.compiler;

import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

final class Comment extends Instruction {
    Comment() {
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        parseChildren(parser);
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        typeCheckContents(symbolTable);
        return Type.String;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void translate(ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator r11, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator r12) {
        /*
        // Method dump skipped, instructions count: 187
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Comment.translate(ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator):void");
    }
}
