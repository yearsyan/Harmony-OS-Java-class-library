package ohos.com.sun.org.apache.bcel.internal.generic;

public class ILOAD extends LoadInstruction {
    ILOAD() {
        super(21, 26);
    }

    public ILOAD(int i) {
        super(21, 26, i);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction, ohos.com.sun.org.apache.bcel.internal.generic.LoadInstruction
    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitILOAD(this);
    }
}
