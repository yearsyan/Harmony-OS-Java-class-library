package ohos.com.sun.org.apache.xalan.internal.xsltc.compiler;

import ohos.com.sun.org.apache.bcel.internal.Constants;
import ohos.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import ohos.com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import ohos.com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import ohos.com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import ohos.com.sun.org.apache.bcel.internal.generic.InstructionList;
import ohos.com.sun.org.apache.bcel.internal.generic.NEW;
import ohos.com.sun.org.apache.bcel.internal.generic.PUSH;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import ohos.com.sun.org.apache.xml.internal.utils.XML11Char;

/* access modifiers changed from: package-private */
public final class DecimalFormatting extends TopLevelElement {
    private static final String DFS_CLASS = "java.text.DecimalFormatSymbols";
    private static final String DFS_SIG = "Ljava/text/DecimalFormatSymbols;";
    private QName _name = null;

    DecimalFormatting() {
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.Void;
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String attribute = getAttribute("name");
        if (attribute.length() > 0 && !XML11Char.isXML11ValidQName(attribute)) {
            parser.reportError(3, new ErrorMsg("INVALID_QNAME_ERR", (Object) attribute, (SyntaxTreeNode) this));
        }
        this._name = parser.getQNameIgnoreDefaultNs(attribute);
        if (this._name == null) {
            this._name = parser.getQNameIgnoreDefaultNs("");
        }
        SymbolTable symbolTable = parser.getSymbolTable();
        if (symbolTable.getDecimalFormatting(this._name) != null) {
            reportWarning(this, parser, ErrorMsg.SYMBOLS_REDEF_ERR, this._name.toString());
        } else {
            symbolTable.addDecimalFormatting(this._name, this);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x01ac A[SYNTHETIC] */
    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void translate(ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator r17, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator r18) {
        /*
        // Method dump skipped, instructions count: 451
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.DecimalFormatting.translate(ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator, ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator):void");
    }

    public static void translateDefaultDFS(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        ConstantPoolGen constantPool = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int addMethodref = constantPool.addMethodref(DFS_CLASS, Constants.CONSTRUCTOR_NAME, "(Ljava/util/Locale;)V");
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(new PUSH(constantPool, ""));
        instructionList.append(new NEW(constantPool.addClass(DFS_CLASS)));
        instructionList.append(DUP);
        instructionList.append(new GETSTATIC(constantPool.addFieldref(Constants.LOCALE_CLASS, "US", Constants.LOCALE_SIG)));
        instructionList.append(new INVOKESPECIAL(addMethodref));
        int addMethodref2 = constantPool.addMethodref(DFS_CLASS, "setNaN", "(Ljava/lang/String;)V");
        instructionList.append(DUP);
        instructionList.append(new PUSH(constantPool, "NaN"));
        instructionList.append(new INVOKEVIRTUAL(addMethodref2));
        int addMethodref3 = constantPool.addMethodref(DFS_CLASS, "setInfinity", "(Ljava/lang/String;)V");
        instructionList.append(DUP);
        instructionList.append(new PUSH(constantPool, ohos.com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_INFINITY));
        instructionList.append(new INVOKEVIRTUAL(addMethodref3));
        instructionList.append(new INVOKEVIRTUAL(constantPool.addMethodref(Constants.TRANSLET_CLASS, "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V")));
    }
}
