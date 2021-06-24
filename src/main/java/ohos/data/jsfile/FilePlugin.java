package ohos.data.jsfile;

import com.huawei.ace.plugin.Function;
import com.huawei.ace.plugin.ModuleGroup;
import com.huawei.ace.plugin.Result;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import ohos.ace.ability.AceAbility;
import ohos.ai.asr.util.AsrConstants;
import ohos.app.Context;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.fastjson.JSONObject;

public class FilePlugin implements ModuleGroup.ModuleGroupHandler {
    private static final String APP_CACHE_DIRECTORY = "internal://cache/";
    private static final String APP_FILE_DIRECTORY = "internal://app/";
    private static final String APP_SHARE_DIRECTORY = "internal://share/";
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 218109441, "JsFileKit");
    private static final String MODULE_GROUP_NAME = "AceModuleGroup/File";
    private static final String TAG = "FilePlugin";
    private static Map<Integer, FilePlugin> instanceMap = new ConcurrentHashMap();
    private Context applicationContext;
    private ModuleGroup moduleGroup;

    public static String getJsCode() {
        return "var file ={ \nfileModuleGroup : null,\nonInit : function onInit(){\n    if (file.fileModuleGroup == null) {\n        file.fileModuleGroup = ModuleGroup.getGroup(\"AceModuleGroup/File\");\n    }\n},\nmkdir : async function mkdir(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"mkdir\",param),param);\n},\nrmdir : async function rmdir(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"rmdir\",param),param);\n},\nget : async function get(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"get\",param),param);\n},\nlist : async function list(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"list\",param),param);\n},\ncopy : async function copy(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"copy\",param),param);\n},\nmove : async function move(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"move\",param),param);\n},\ndeleteFile : async function deleteFile(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"delete\",param),param);\n},\naccess : async function access(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"access\",param),param);\n},\nwriteText : async function writeText(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"writeText\",param),param);\n},\nwriteArrayBuffer : async function writeArrayBuffer(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"writeArrayBuffer\",param),param);\n},\nreadText : async function readText(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"readText\",param),param);\n},\nreadArrayBuffer : async function readArrayBuffer(param) {\n    file.onInit();\n    return await catching(file.fileModuleGroup.callNative(\"readArrayBuffer\",param),param);\n}\n};\nglobal.systemplugin.file = {\n    get: file.get,\n    mkdir: file.mkdir,\n    rmdir: file.rmdir,\n    list: file.list,\n    copy: file.copy,\n    move: file.move,\n    delete: file.deleteFile,\n    access: file.access,\n    writeText: file.writeText,\n    writeArrayBuffer: file.writeArrayBuffer,\n    readText: file.readText,\n    readArrayBuffer: file.readArrayBuffer,\n};\n";
    }

    private FilePlugin(Context context) {
        this.applicationContext = context;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0058, code lost:
        if (r1.equals("move") != false) goto L_0x00b1;
     */
    @Override // com.huawei.ace.plugin.ModuleGroup.ModuleGroupHandler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onFunctionCall(com.huawei.ace.plugin.Function r5, com.huawei.ace.plugin.Result r6) {
        /*
        // Method dump skipped, instructions count: 310
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.jsfile.FilePlugin.onFunctionCall(com.huawei.ace.plugin.Function, com.huawei.ace.plugin.Result):void");
    }

    private void move(Function function, Result result) {
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey("srcUri") || !parseObject.containsKey("dstUri")) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString("srcUri"), result);
        String path2 = getPath(parseObject.getString("dstUri"), result);
        if (path != null && path2 != null) {
            try {
                if (Files.move(new File(path).toPath(), new File(path2).toPath(), new CopyOption[0]) != null) {
                    result.success(parseObject.getString("dstUri"));
                } else {
                    result.error(300, "move file fail, IO Exception");
                }
            } catch (IOException unused) {
                result.error(300, "move file fail, IO Exception");
            }
        }
    }

    private void copy(Function function, Result result) {
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey("srcUri") || !parseObject.containsKey("dstUri")) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString("srcUri"), result);
        String path2 = getPath(parseObject.getString("dstUri"), result);
        if (path != null && path2 != null) {
            try {
                if (Files.copy(new File(path).toPath(), new File(path2).toPath(), new CopyOption[0]) != null) {
                    result.success(parseObject.getString("dstUri"));
                } else {
                    result.error(300, "copy file fail, IO Exception");
                }
            } catch (IOException unused) {
                result.error(300, "copy file fail, IO Exception");
            }
        }
    }

    private void list(Function function, Result result) {
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey(Constants.ELEMNAME_URL_STRING)) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString(Constants.ELEMNAME_URL_STRING), result);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                ArrayList arrayList = new ArrayList();
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file2 : listFiles) {
                        arrayList.add(getFileInfo(parseObject.getString(Constants.ELEMNAME_URL_STRING) + File.separator + file2.getName(), file2, false));
                    }
                }
                HashMap hashMap = new HashMap();
                hashMap.put("fileList", arrayList);
                result.success(hashMap);
                return;
            }
            result.error(300, "list file failed");
        }
    }

    private void get(Function function, Result result) {
        HashMap<String, Object> hashMap;
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey(Constants.ELEMNAME_URL_STRING)) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString(Constants.ELEMNAME_URL_STRING), result);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                if (parseObject.containsKey("recursive")) {
                    hashMap = getFileInfo(parseObject.getString(Constants.ELEMNAME_URL_STRING), file, parseObject.getBoolean("recursive").booleanValue());
                } else {
                    hashMap = getFileInfo(parseObject.getString(Constants.ELEMNAME_URL_STRING), file, false);
                }
                result.success(hashMap);
                return;
            }
            result.error(300, "get file failed");
        }
    }

    private HashMap<String, Object> getFileInfo(String str, File file, boolean z) {
        File[] listFiles;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.ELEMNAME_URL_STRING, str);
        hashMap.put("lastModifiedTime", Long.valueOf(file.lastModified()));
        hashMap.put("length", Long.valueOf(file.length()));
        if (file.isDirectory()) {
            hashMap.put("type", "dir");
            if (z && (listFiles = file.listFiles()) != null) {
                for (File file2 : listFiles) {
                    hashMap.put("subFiles", getFileInfo(str + file2.getName(), file2, true));
                }
            }
        } else {
            hashMap.put("type", AsrConstants.ASR_SRC_FILE);
        }
        return hashMap;
    }

    private void delete(Function function, Result result) {
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey(Constants.ELEMNAME_URL_STRING)) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString(Constants.ELEMNAME_URL_STRING), result);
        if (path != null) {
            if (new File(path).delete()) {
                result.success(null);
            } else {
                result.error(300, "delete file failed");
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0069, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006a, code lost:
        $closeResource(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006d, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeText(com.huawei.ace.plugin.Function r5, com.huawei.ace.plugin.Result r6) {
        /*
        // Method dump skipped, instructions count: 127
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.jsfile.FilePlugin.writeText(com.huawei.ace.plugin.Function, com.huawei.ace.plugin.Result):void");
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0093, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0094, code lost:
        $closeResource(r6, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0097, code lost:
        throw r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeArrayBuffer(com.huawei.ace.plugin.Function r7, com.huawei.ace.plugin.Result r8) {
        /*
        // Method dump skipped, instructions count: 187
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.jsfile.FilePlugin.writeArrayBuffer(com.huawei.ace.plugin.Function, com.huawei.ace.plugin.Result):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0085, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0086, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0089, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x008c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x008d, code lost:
        $closeResource(r3, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0090, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readText(com.huawei.ace.plugin.Function r4, com.huawei.ace.plugin.Result r5) {
        /*
        // Method dump skipped, instructions count: 162
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.jsfile.FilePlugin.readText(com.huawei.ace.plugin.Function, com.huawei.ace.plugin.Result):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00ad, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ae, code lost:
        $closeResource(r10, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b1, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readArrayBuffer(com.huawei.ace.plugin.Function r10, com.huawei.ace.plugin.Result r11) {
        /*
        // Method dump skipped, instructions count: 213
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.data.jsfile.FilePlugin.readArrayBuffer(com.huawei.ace.plugin.Function, com.huawei.ace.plugin.Result):void");
    }

    private void access(Function function, Result result) {
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey(Constants.ELEMNAME_URL_STRING)) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString(Constants.ELEMNAME_URL_STRING), result);
        if (path != null) {
            if (new File(path).exists()) {
                result.success(null);
            } else {
                result.error(301, "file not exist");
            }
        }
    }

    private void mkdir(Function function, Result result) {
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey(Constants.ELEMNAME_URL_STRING)) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString(Constants.ELEMNAME_URL_STRING), result);
        if (path != null) {
            File file = new File(path);
            if (!parseObject.containsKey("recursive") || !parseObject.getBoolean("recursive").booleanValue()) {
                if (file.mkdir()) {
                    result.success(null);
                } else {
                    result.error(300, "make directory failed");
                }
            } else if (file.mkdirs()) {
                result.success(null);
            } else {
                result.error(300, "make directory failed");
            }
        }
    }

    private void rmdir(Function function, Result result) {
        JSONObject parseObject = JSONObject.parseObject((String) function.arguments.get(0));
        if (parseObject == null || !parseObject.containsKey(Constants.ELEMNAME_URL_STRING)) {
            result.error(202, "json arguments illegal");
            return;
        }
        String path = getPath(parseObject.getString(Constants.ELEMNAME_URL_STRING), result);
        if (path != null) {
            File file = new File(path);
            if (!parseObject.containsKey("recursive") || !parseObject.getBoolean("recursive").booleanValue()) {
                if (file.delete()) {
                    result.success(null);
                } else {
                    result.error(300, "remove directory failed");
                }
            } else if (deleteFolder(file)) {
                result.success(null);
            } else {
                result.error(300, "remove directory failed");
            }
        }
    }

    private boolean deleteFolder(File file) {
        File[] listFiles;
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                deleteFolder(file2);
            }
        }
        return file.delete();
    }

    private String getPath(String str, Result result) {
        try {
            String realPath = getRealPath(str);
            if (realPath != null) {
                return realPath;
            }
            result.error(202, "illegal uri:" + str);
            HiLog.warn(LABEL_LOG, "illegal uri", new Object[0]);
            return null;
        } catch (IOException | NullPointerException unused) {
            result.error(202, "illegal path");
            return null;
        }
    }

    private String getRealPath(String str) throws IOException, NullPointerException {
        if (str == null) {
            return null;
        }
        if (str.indexOf(APP_CACHE_DIRECTORY) == 0) {
            String path = Paths.get(str.substring(16), new String[0]).normalize().toString();
            return this.applicationContext.getCacheDir().getCanonicalPath() + path;
        } else if (str.indexOf(APP_FILE_DIRECTORY) == 0) {
            String path2 = Paths.get(str.substring(14), new String[0]).normalize().toString();
            return this.applicationContext.getFilesDir().getCanonicalPath() + path2;
        } else if (str.indexOf(APP_SHARE_DIRECTORY) == 0) {
            String path3 = Paths.get(str.substring(16), new String[0]).normalize().toString();
            return this.applicationContext.getExternalCacheDir().getCanonicalPath() + path3;
        } else {
            HiLog.warn(LABEL_LOG, "getRealPath: illegal uri", new Object[0]);
            return null;
        }
    }

    public static void register(Context context) {
        if (context == null) {
            HiLog.error(LABEL_LOG, "Context may not be null when register FilePlugin", new Object[0]);
        } else if (!(context instanceof AceAbility)) {
            HiLog.error(LABEL_LOG, "Failed to get abilityId when register FilePlugin", new Object[0]);
        } else {
            int abilityId = ((AceAbility) context).getAbilityId();
            FilePlugin filePlugin = new FilePlugin(context);
            instanceMap.put(Integer.valueOf(abilityId), filePlugin);
            ModuleGroup.registerModuleGroup(MODULE_GROUP_NAME, filePlugin, Integer.valueOf(abilityId));
            filePlugin.onRegister(context);
        }
    }

    private void onRegister(Context context) {
        this.applicationContext = context;
    }

    public static void deregister(Context context) {
        if (!(context instanceof AceAbility)) {
            HiLog.error(LABEL_LOG, "Failed to get abilityId when deregister FilePlugin", new Object[0]);
            return;
        }
        int abilityId = ((AceAbility) context).getAbilityId();
        instanceMap.remove(Integer.valueOf(abilityId));
        ModuleGroup.registerModuleGroup(MODULE_GROUP_NAME, null, Integer.valueOf(abilityId));
    }

    public static Set<String> getPluginGroup() {
        HashSet hashSet = new HashSet();
        hashSet.add(MODULE_GROUP_NAME);
        return hashSet;
    }
}
