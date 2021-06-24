package ohos.os;

import android.os.FileUtils;

public class FileHelper {
    private FileHelper() {
    }

    public static int translateFileOperationModePfdToPosix(int i) {
        return FileUtils.translateModePfdToPosix(i);
    }
}
