package ohos.com.sun.org.apache.xalan.internal.xslt;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ResourceBundle;
import ohos.com.sun.org.apache.xalan.internal.res.XSLMessages;

public class Process {
    protected static void printArgOptions(ResourceBundle resourceBundle) {
        System.out.println(resourceBundle.getString("xslProc_option"));
        PrintStream printStream = System.out;
        printStream.println("\n\t\t\t" + resourceBundle.getString("xslProc_common_options") + "\n");
        System.out.println(resourceBundle.getString("optionXSLTC"));
        System.out.println(resourceBundle.getString("optionIN"));
        System.out.println(resourceBundle.getString("optionXSL"));
        System.out.println(resourceBundle.getString("optionOUT"));
        System.out.println(resourceBundle.getString("optionV"));
        System.out.println(resourceBundle.getString("optionEDUMP"));
        System.out.println(resourceBundle.getString("optionXML"));
        System.out.println(resourceBundle.getString("optionTEXT"));
        System.out.println(resourceBundle.getString("optionHTML"));
        System.out.println(resourceBundle.getString("optionPARAM"));
        System.out.println(resourceBundle.getString("optionMEDIA"));
        System.out.println(resourceBundle.getString("optionFLAVOR"));
        System.out.println(resourceBundle.getString("optionDIAG"));
        System.out.println(resourceBundle.getString("optionURIRESOLVER"));
        System.out.println(resourceBundle.getString("optionENTITYRESOLVER"));
        waitForReturnKey(resourceBundle);
        System.out.println(resourceBundle.getString("optionCONTENTHANDLER"));
        System.out.println(resourceBundle.getString("optionSECUREPROCESSING"));
        PrintStream printStream2 = System.out;
        printStream2.println("\n\t\t\t" + resourceBundle.getString("xslProc_xsltc_options") + "\n");
        System.out.println(resourceBundle.getString("optionXO"));
        waitForReturnKey(resourceBundle);
        System.out.println(resourceBundle.getString("optionXD"));
        System.out.println(resourceBundle.getString("optionXJ"));
        System.out.println(resourceBundle.getString("optionXP"));
        System.out.println(resourceBundle.getString("optionXN"));
        System.out.println(resourceBundle.getString("optionXX"));
        System.out.println(resourceBundle.getString("optionXT"));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:350|351|352|353|354|(2:356|357)|358|359|(1:365)|366|(1:368)(1:369)) */
    /* JADX WARNING: Code restructure failed: missing block: B:326:0x06aa, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:362:0x0715, code lost:
        r7 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:322:0x069b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:358:0x0706 */
    /* JADX WARNING: Removed duplicated region for block: B:327:? A[ExcHandler: AbstractMethodError | NoSuchMethodError (unused java.lang.Throwable), SYNTHETIC, Splitter:B:320:0x0698] */
    /* JADX WARNING: Removed duplicated region for block: B:365:0x0718  */
    /* JADX WARNING: Removed duplicated region for block: B:368:0x0721  */
    /* JADX WARNING: Removed duplicated region for block: B:369:0x0734  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x07f4 A[LOOP:4: B:415:0x07f0->B:417:0x07f4, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:425:0x080b  */
    /* JADX WARNING: Removed duplicated region for block: B:426:0x080f  */
    /* JADX WARNING: Removed duplicated region for block: B:428:0x0846  */
    /* JADX WARNING: Removed duplicated region for block: B:431:0x0852  */
    /* JADX WARNING: Removed duplicated region for block: B:444:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void _main(java.lang.String[] r28) {
        /*
        // Method dump skipped, instructions count: 2134
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.xslt.Process._main(java.lang.String[]):void");
    }

    static void doExit(String str) {
        throw new RuntimeException(str);
    }

    private static void waitForReturnKey(ResourceBundle resourceBundle) {
        System.out.println(resourceBundle.getString("xslProc_return_to_continue"));
        do {
            try {
            } catch (IOException unused) {
                return;
            }
        } while (System.in.read() != 10);
    }

    private static void printInvalidXSLTCOption(String str) {
        System.err.println(XSLMessages.createMessage("xslProc_invalid_xsltc_option", new Object[]{str}));
    }

    private static void printInvalidXalanOption(String str) {
        System.err.println(XSLMessages.createMessage("xslProc_invalid_xalan_option", new Object[]{str}));
    }
}
