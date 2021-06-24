package ohos.com.sun.org.apache.bcel.internal.generic;

public class LLOAD extends LoadInstruction {
    LLOAD() {
        super(22, 30);
    }

    public LLOAD(int i) {
        super(22, 30, i);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction, ohos.com.sun.org.apache.bcel.internal.generic.LoadInstruction
    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitLLOAD(this);
    }
}
