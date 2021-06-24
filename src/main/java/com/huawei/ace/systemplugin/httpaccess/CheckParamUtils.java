package com.huawei.ace.systemplugin.httpaccess;

import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CheckParamUtils {
    private static final Set<String> FETCH_METHOD = new HashSet(Arrays.asList("OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE"));
    private static final int MAX_PATH_LENGTH = 4096;
    private static final Set<String> RESPONSE_TYPE = new HashSet(Arrays.asList("text", "json"));
    private static final Set<String> UPLOAD_METHOD = new HashSet(Arrays.asList("POST", "PUT"));

    private static <T> boolean checkNull(T t) {
        return t == null;
    }

    public static boolean checkDownloadRequest(RequestData requestData) {
        return isValidUrl(requestData.getUrl()) && (checkEmptyString(requestData.getData()) && checkEmptyString(requestData.getMethod()) && checkEmptyString(requestData.getResponseType()) && checkNull(requestData.getFiles()) && checkEmptyString(requestData.getToken()));
    }

    public static boolean checkFetchRequest(RequestData requestData) {
        return isValidUrl(requestData.getUrl()) && (checkEmptyString(requestData.getFileName()) && checkEmptyString(requestData.getDescription()) && checkNull(requestData.getFiles()) && checkEmptyString(requestData.getToken())) && (FETCH_METHOD.contains(requestData.getMethod()) || "".equals(requestData.getMethod())) && (RESPONSE_TYPE.contains(requestData.getResponseType()) || "".equals(requestData.getResponseType()));
    }

    public static boolean checkUploadRequest(RequestData requestData) {
        return isValidUrl(requestData.getUrl()) && (checkEmptyString(requestData.getFileName()) && checkEmptyString(requestData.getDescription()) && checkEmptyString(requestData.getResponseType()) && checkEmptyString(requestData.getToken())) && isValidUploadFile(requestData.getFiles()) && (UPLOAD_METHOD.contains(requestData.getMethod()) || "".equals(requestData.getMethod()));
    }

    public static boolean checkOnDownloadCompleteRequest(RequestData requestData) {
        boolean z;
        try {
            if (Long.parseLong(requestData.getToken()) >= 0) {
                z = true;
                return !(!checkEmptyString(requestData.getFileName()) && checkEmptyString(requestData.getDescription()) && checkEmptyString(requestData.getResponseType()) && checkNull(requestData.getFiles()) && checkEmptyString(requestData.getMethod()) && checkEmptyString(requestData.getResponseType()) && checkEmptyString(requestData.getUrl()) && checkEmptyString(requestData.getData())) && z;
            }
        } catch (NumberFormatException unused) {
        }
        z = false;
        if (!(!checkEmptyString(requestData.getFileName()) && checkEmptyString(requestData.getDescription()) && checkEmptyString(requestData.getResponseType()) && checkNull(requestData.getFiles()) && checkEmptyString(requestData.getMethod()) && checkEmptyString(requestData.getResponseType()) && checkEmptyString(requestData.getUrl()) && checkEmptyString(requestData.getData()))) {
        }
    }

    private static boolean isValidUrl(String str) {
        if (str == null) {
            return false;
        }
        return str.startsWith("http") || str.startsWith("https");
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isValidUploadFile(java.util.List<com.huawei.ace.systemplugin.httpaccess.data.FormFileData> r3) {
        /*
            r0 = 0
            if (r3 == 0) goto L_0x002f
            int r1 = r3.size()
            if (r1 != 0) goto L_0x000a
            goto L_0x002f
        L_0x000a:
            java.util.Iterator r3 = r3.iterator()
        L_0x000e:
            boolean r1 = r3.hasNext()
            if (r1 == 0) goto L_0x002d
            java.lang.Object r1 = r3.next()
            com.huawei.ace.systemplugin.httpaccess.data.FormFileData r1 = (com.huawei.ace.systemplugin.httpaccess.data.FormFileData) r1
            java.lang.String r1 = r1.getUri()
            int r2 = r1.length()
            if (r2 <= 0) goto L_0x002c
            int r1 = r1.length()
            r2 = 4096(0x1000, float:5.74E-42)
            if (r1 <= r2) goto L_0x000e
        L_0x002c:
            return r0
        L_0x002d:
            r3 = 1
            return r3
        L_0x002f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ace.systemplugin.httpaccess.CheckParamUtils.isValidUploadFile(java.util.List):boolean");
    }

    private static boolean checkEmptyString(String str) {
        return "".equals(str);
    }
}
