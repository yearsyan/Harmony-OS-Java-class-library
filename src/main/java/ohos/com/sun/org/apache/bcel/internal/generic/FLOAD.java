package ohos.com.sun.org.apache.bcel.internal.generic;

public class FLOAD extends LoadInstruction {
    FLOAD() {
        super(23, 34);
    }

    public FLOAD(int i) {
        super(23, 34, i);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction, ohos.com.sun.org.apache.bcel.internal.generic.LoadInstruction
    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitFLOAD(this);
    }
}
