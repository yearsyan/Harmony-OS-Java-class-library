package com.huawei.ace.systemplugin.httpaccess.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.security.NetworkSecurityPolicy;
import android.security.net.config.ApplicationConfig;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import java.io.ByteArrayOutputStream;
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

public class HttpUtils {
    private static final String TAG = "HttpUtils";

    public static void closeQuietly(InputStream inputStream) {
        if (inputStream instanceof Closeable) {
            closeQuietly((Closeable) inputStream);
        }
    }

    public static void closeQuietly(OutputStream outputStream) {
        if (outputStream instanceof Closeable) {
            closeQuietly((Closeable) outputStream);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
                ALog.e(TAG, "closeQuietly IOException!");
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
            ALog.e(TAG, "caught package NameNotFoundException!");
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
            ALog.e(TAG, "param of the method can not be null!");
            return;
        }
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        if (requestData.getMethod().equals("POST") || requestData.getMethod().equals("PUT")) {
            ALog.d(TAG, "set doOutPut property!");
            httpURLConnection.setDoOutput(true);
        }
        addHeaders(httpURLConnection, requestData.getHeader());
        if (httpURLConnection.getRequestProperty("Connection") == null) {
            httpURLConnection.addRequestProperty("Connection", "Keep-Alive");
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            try {
                ALog.d(TAG, "https connection, set sslContext!");
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(getSSLContextForPackage(context, context.getApplicationInfo().packageName).getSocketFactory());
            } catch (GeneralSecurityException unused) {
                ALog.e(TAG, "caught GeneralSecurityException!");
            }
        }
    }

    public static byte[] parseInputStream(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[8192];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            } catch (IOException unused) {
                ALog.e(TAG, "parseInputStream caught IOException!");
            } catch (Throwable th) {
                closeQuietly((OutputStream) byteArrayOutputStream);
                throw th;
            }
        }
        closeQuietly((OutputStream) byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
