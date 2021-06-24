package ohos.miscservices.httpaccess;

import android.content.Context;
import android.content.pm.PackageManager;
import android.security.NetworkSecurityPolicy;
import android.security.net.config.ApplicationConfig;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.httpaccess.data.RequestData;

public class HttpUtils {
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "HttpUtils");

    public static void closeQuietly(InputStream inputStream) {
        closeQuietly((Closeable) inputStream);
    }

    public static void closeQuietly(OutputStream outputStream) {
        closeQuietly((Closeable) outputStream);
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
                HiLog.error(TAG, "closeQuietly IOException!", new Object[0]);
            }
        }
    }

    public static SSLContext getSSLContextForPackage(Context context, String str) throws GeneralSecurityException {
        try {
            ApplicationConfig applicationConfigForPackage = NetworkSecurityPolicy.getApplicationConfigForPackage(context, str);
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, new TrustManager[]{applicationConfigForPackage.getTrustManager()}, null);
            return instance;
        } catch (PackageManager.NameNotFoundException unused) {
            HiLog.error(TAG, "caught package NameNotFoundException!", new Object[0]);
            return SSLContext.getDefault();
        }
    }

    public static void addHeaders(URLConnection uRLConnection, Map<String, String> map) {
        if (!(map == null || map.isEmpty())) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!(key == null || key.length() == 0 || value == null || value.length() == 0)) {
                    uRLConnection.setRequestProperty(key, value);
                }
            }
        }
    }

    public static void setConnProperty(HttpURLConnection httpURLConnection, RequestData requestData, Context context) {
        if (httpURLConnection == null || requestData == null || context == null) {
            HiLog.error(TAG, "param of the method can not be null!", new Object[0]);
            return;
        }
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        if (requestData.getMethod().equals("POST") || requestData.getMethod().equals("PUT")) {
            HiLog.debug(TAG, "set doOutPut property!", new Object[0]);
            httpURLConnection.setDoOutput(true);
        }
        addHeaders(httpURLConnection, requestData.getHeader());
        if (httpURLConnection.getRequestProperty("Connection") == null) {
            httpURLConnection.addRequestProperty("Connection", "Keep-Alive");
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            try {
                HiLog.debug(TAG, "https connection, set sslContext!", new Object[0]);
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(getSSLContextForPackage(context, context.getApplicationInfo().packageName).getSocketFactory());
            } catch (GeneralSecurityException unused) {
                HiLog.error(TAG, "caught GeneralSecurityException!", new Object[0]);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.miscservices.httpaccess.HttpUtils.TAG, "parseInputStream caught IOException!", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        closeQuietly((java.io.OutputStream) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002d, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x001b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] parseInputStream(java.io.InputStream r5) {
        /*
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            r1 = 8192(0x2000, float:1.14794E-41)
            byte[] r1 = new byte[r1]
        L_0x0009:
            r2 = 0
            int r3 = r5.read(r1)     // Catch:{ IOException -> 0x001b }
            r4 = -1
            if (r3 != r4) goto L_0x0015
        L_0x0011:
            closeQuietly(r0)
            goto L_0x0025
        L_0x0015:
            r0.write(r1, r2, r3)
            goto L_0x0009
        L_0x0019:
            r5 = move-exception
            goto L_0x002a
        L_0x001b:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.httpaccess.HttpUtils.TAG     // Catch:{ all -> 0x0019 }
            java.lang.String r1 = "parseInputStream caught IOException!"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0019 }
            ohos.hiviewdfx.HiLog.error(r5, r1, r2)     // Catch:{ all -> 0x0019 }
            goto L_0x0011
        L_0x0025:
            byte[] r5 = r0.toByteArray()
            return r5
        L_0x002a:
            closeQuietly(r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.httpaccess.HttpUtils.parseInputStream(java.io.InputStream):byte[]");
    }

    public static Map<String, List<String>> analyzeResponseHeaders(Map<String, List<String>> map) {
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key == null || !key.contains("Android")) {
                hashMap.put(key, entry.getValue());
            }
        }
        return hashMap;
    }
}
