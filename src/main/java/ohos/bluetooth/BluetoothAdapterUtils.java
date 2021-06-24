package ohos.bluetooth;

public class BluetoothAdapterUtils {
    private static final String REPLACE_STRING = "android";
    private static final String SEARCH_STRING = "ohos";

    public static String stringReplace(String str) {
        return str.replace(SEARCH_STRING, REPLACE_STRING);
    }
}
