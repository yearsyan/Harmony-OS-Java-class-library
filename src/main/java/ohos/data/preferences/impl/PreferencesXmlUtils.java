package ohos.data.preferences.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import ohos.data.preferences.PreferencesFileReadException;
import ohos.data.preferences.PreferencesFileWriteException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class PreferencesXmlUtils {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218109520, "PreferencesXmlUtils");

    private static native HashMap<String, Object> nativeReadSettingXml(String str) throws PreferencesFileReadException;

    private static native boolean nativeWriteSettingXml(HashMap<String, Object> hashMap, String str) throws PreferencesFileWriteException;

    static {
        try {
            System.loadLibrary("preferences_jni.z");
            HiLog.debug(LABEL, "preferences_jni.z loaded success.", new Object[0]);
        } catch (SecurityException | UnsatisfiedLinkError e) {
            HiLog.error(LABEL, "preferences_jni.z loaded fail:%{public}s", e.getMessage());
        }
    }

    private PreferencesXmlUtils() {
    }

    public static final HashMap<String, Object> readSettingXml(String str) throws PreferencesFileReadException {
        return nativeReadSettingXml(str);
    }

    public static final boolean writeSettingXml(HashMap<String, Object> hashMap, String str) throws PreferencesFileWriteException {
        return nativeWriteSettingXml(hashMap, str);
    }

    public static void limitFilePermission(File file) {
        if (file != null) {
            try {
                Set<PosixFilePermission> posixFilePermissions = Files.getPosixFilePermissions(file.toPath(), new LinkOption[0]);
                if (posixFilePermissions.removeAll(PosixFilePermissions.fromString("--x--xrwx"))) {
                    Files.setPosixFilePermissions(file.toPath(), posixFilePermissions);
                }
            } catch (IOException unused) {
                HiLog.error(LABEL, "An I/O error occurs when limiting permissions of file:%{public}s", file.getName());
            }
        }
    }

    public static void printFileAttributes(File file) {
        if (file != null) {
            try {
                Map<String, Object> readAttributes = Files.readAttributes(file.toPath(), "posix:*", new LinkOption[0]);
                StringBuilder sb = new StringBuilder();
                readAttributes.forEach(new BiConsumer(sb) {
                    /* class ohos.data.preferences.impl.$$Lambda$PreferencesXmlUtils$cotcTOSMcQlny3dfEbx3_TvjEag */
                    private final /* synthetic */ StringBuilder f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        this.f$0.append((StringBuilder) (((String) obj) + "=" + obj2 + ", "));
                    }
                });
                sb.append(".");
                HiLog.error(LABEL, "%{public}s attributes: %{public}s", file.getName(), sb.toString());
            } catch (IOException unused) {
                HiLog.error(LABEL, "An I/O error occurs when check file attributes of %{public}s", file.getName());
            }
        }
    }
}
