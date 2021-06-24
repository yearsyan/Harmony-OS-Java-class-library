package ohos.global.resource;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import ohos.global.resource.utils.ZipFileUtils;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.fastjson.JSONArray;
import ohos.utils.fastjson.JSONObject;

public class OnlineTheme implements Closeable {
    private static final String APP = "app";
    private static final String BUNDLE_NAME = "bundleName";
    private static final String CONFIG_FILE = "config.json";
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "OnlineTheme");
    private static final int MAX_FILE_LENGTH = 10485760;
    private static final String SYSTEM_RESOURCE_PACKAGE = "ohos.global.systemres";
    private static final String THEME_PATH = "/data/skin/";
    private ZipFile appThemeZip;

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0070, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0075, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0076, code lost:
        r0.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0079, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x007a, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.global.resource.OnlineTheme.LABEL, "exception occurs while getting config", new java.lang.Object[0]);
     */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[ExcHandler: IOException | IllegalStateException | SecurityException (unused java.lang.Throwable), SYNTHETIC, Splitter:B:13:0x0032] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Optional<java.lang.String> getPackageName(ohos.global.resource.ResourcePath r3) {
        /*
        // Method dump skipped, instructions count: 137
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.OnlineTheme.getPackageName(ohos.global.resource.ResourcePath):java.util.Optional");
    }

    public static Optional<OnlineTheme> createOnlineTheme(ResourcePath resourcePath) {
        if (resourcePath == null) {
            return Optional.empty();
        }
        Optional<String> packageName = getPackageName(resourcePath);
        if (!packageName.isPresent()) {
            return Optional.empty();
        }
        File file = new File(THEME_PATH, packageName.get());
        if (!file.exists()) {
            return Optional.empty();
        }
        return Optional.ofNullable(new OnlineTheme(file));
    }

    private OnlineTheme(File file) {
        try {
            this.appThemeZip = new ZipFile(file, 1);
        } catch (IOException unused) {
            HiLog.error(LABEL, "open online theme failed ", new Object[0]);
        }
    }

    private HashMap<String, String> parseColor(String str) {
        JSONArray jSONArray;
        HashMap<String, String> hashMap = new HashMap<>();
        JSONObject parseObject = JSONObject.parseObject(str);
        if (parseObject == null || !parseObject.containsKey("color") || (jSONArray = parseObject.getJSONArray("color")) == null) {
            return hashMap;
        }
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            Object obj = jSONObject.get("name");
            Object obj2 = jSONObject.get("value");
            if (!(obj == null || obj2 == null)) {
                hashMap.put(obj.toString(), obj2.toString());
            }
        }
        return hashMap;
    }

    public Optional<InputStream> openFileStream(String str, boolean z) {
        if (str == null || str.length() == 0 || this.appThemeZip == null) {
            return Optional.empty();
        }
        if (z) {
            str = SYSTEM_RESOURCE_PACKAGE + File.separator + str;
        }
        try {
            ZipEntry entry = this.appThemeZip.getEntry(str);
            if (entry != null) {
                return Optional.ofNullable(this.appThemeZip.getInputStream(entry));
            }
        } catch (IOException | IllegalStateException unused) {
            HiLog.error(LABEL, "openRawFile failed", new Object[0]);
        }
        return Optional.empty();
    }

    public HashMap<String, String> getThemeColors(boolean z) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (this.appThemeZip == null) {
            return hashMap;
        }
        String str = "colors.json";
        if (z) {
            try {
                str = SYSTEM_RESOURCE_PACKAGE + File.separator + str;
            } catch (IllegalStateException unused) {
                HiLog.error(LABEL, "getModeColors failed", new Object[0]);
                hashMap.clear();
                return hashMap;
            }
        }
        Optional<String> contentFromZip = ZipFileUtils.getContentFromZip(this.appThemeZip, str);
        if (contentFromZip.isPresent()) {
            return parseColor(contentFromZip.get());
        }
        return hashMap;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        ZipFile zipFile = this.appThemeZip;
        if (zipFile != null) {
            zipFile.close();
            this.appThemeZip = null;
        }
    }
}
