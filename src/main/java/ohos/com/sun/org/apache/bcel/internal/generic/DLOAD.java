package ohos.com.sun.org.apache.bcel.internal.generic;

public class DLOAD extends LoadInstruction {
    DLOAD() {
        super(24, 38);
    }

    public DLOAD(int i) {
        super(24, 38, i);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction, ohos.com.sun.org.apache.bcel.internal.generic.LoadInstruction
    public void accept(Visitor visitor) {
        super.accept(visitor);
        visitor.visitDLOAD(this);
    }
}
