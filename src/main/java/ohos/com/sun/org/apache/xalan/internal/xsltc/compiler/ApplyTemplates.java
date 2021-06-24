package ohos.com.sun.org.apache.xalan.internal.xsltc.compiler;

import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import ohos.com.sun.org.apache.xml.internal.utils.XML11Char;

/* access modifiers changed from: package-private */
public final class ApplyTemplates extends Instruction {
    private String _functionName;
    private QName _modeName;
    private Expression _select;
    private Type _type = null;

    ApplyTemplates() {
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int i) {
        indent(i);
        Util.println("ApplyTemplates");
        int i2 = i + 4;
        indent(i2);
        Util.println("select " + this._select.toString());
        if (this._modeName != null) {
            indent(i2);
            Util.println("mode " + this._modeName);
        }
    }

    public boolean hasWithParams() {
        return hasContents();
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String attribute = getAttribute(Constants.ATTRNAME_SELECT);
        String attribute2 = getAttribute(Constants.ATTRNAME_MODE);
        if (attribute.length() > 0) {
            this._select = parser.parseExpression(this, Constants.ATTRNAME_SELECT, null);
        }
        if (attribute2.length() > 0) {
            if (!XML11Char.isXML11ValidQName(attribute2)) {
                parser.reportError(3, new ErrorMsg("INVALID_QNAME_ERR", (Object) attribute2, (SyntaxTreeNode) this));
            }
            this._modeName = parser.getQNameIgnoreDefaultNs(attribute2);
        }
        this._functionName = parser.getTopLevelStylesheet().getMode(this._modeName).functionName();
        parseChildren(parser);
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        Expression expression = this._select;
        if (expression != null) {
            this._type = expression.typeCheck(symbolTable);
            Type type = this._type;
            if ((type instanceof NodeType) || (type instanceof ReferenceType)) {
                this._select = new CastExpr(this._select, Type.NodeSet);
                this._type = Type.NodeSet;
            }
            Type type2 = this._type;
            if ((type2 instanceof NodeSetType) || (type2 instanceof ResultTreeType)) {
                typeCheckContents(symbolTable);
                return Type.Void;
            }
            throw new TypeCheckError(this);
        }
        typeCheckContents(symbolTable);
        return Type.Void;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0103  */
    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void translate(ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator r10, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator r11) {
        /*
        // Method dump skipped, instructions count: 310
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.ApplyTemplates.translate(ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator):void");
    }
}
