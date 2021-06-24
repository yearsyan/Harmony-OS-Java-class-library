package com.huawei.ace.systemplugin.httpaccess.adapter;

import android.webkit.MimeTypeMap;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.systemplugin.httpaccess.HttpProbe;
import com.huawei.ace.systemplugin.httpaccess.data.FormFileData;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import com.huawei.ace.systemplugin.httpaccess.data.ResponseData;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import ohos.abilityshell.utils.AbilityContextUtils;
import ohos.app.Context;
import ohos.utils.fastjson.JSONArray;
import ohos.utils.fastjson.JSONObject;
import ohos.utils.zson.ZSONArray;

public class HttpUploadImpl {
    private static final String APP_CACHE_DIRECTORY = "internal://cache/";
    private static final String CHARSET = "Charset";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    private static final String LINE_END = "\r\n";
    private static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";
    private static final String PREFIX = "--";
    private static final String TAG = "HttpUploadImpl";
    private static final String UTF_8 = "UTF-8";
    private Context appContext;
    private android.content.Context context;

    public HttpUploadImpl(Context context2) {
        this.appContext = context2;
        Object androidContext = AbilityContextUtils.getAndroidContext(context2);
        if (androidContext instanceof android.content.Context) {
            this.context = (android.content.Context) androidContext;
        }
    }

    public void httpUrlUpload(RequestData requestData, HttpProbe httpProbe) {
        Throwable th;
        BufferedOutputStream bufferedOutputStream;
        ALog.d(TAG, "start upload!");
        ResponseData responseData = new ResponseData();
        BufferedInputStream bufferedInputStream = null;
        try {
            URLConnection openConnection = new URL(requestData.getUrl()).openConnection();
            if (openConnection instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
                HttpUtils.setConnProperty(httpURLConnection, requestData, this.context);
                String method = requestData.getMethod();
                if ("".equals(method)) {
                    method = "POST";
                }
                httpURLConnection.setRequestMethod(method);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty(CHARSET, "UTF-8");
                String uuid = UUID.randomUUID().toString();
                httpURLConnection.setRequestProperty(CONTENT_TYPE, "multipart/form-data;boundary=" + uuid);
                HttpUtils.addHeaders(httpURLConnection, requestData.getHeader());
                httpURLConnection.setChunkedStreamingMode(0);
                bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                try {
                    addFormDatas(bufferedOutputStream, requestData, uuid);
                    addFormFileDatas(bufferedOutputStream, requestData, uuid);
                    bufferedOutputStream.write(("\r\n--" + uuid + PREFIX + "\r\n").getBytes("UTF-8"));
                    bufferedOutputStream.flush();
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode != 200) {
                        ALog.e(TAG, "connect error, response code : " + responseCode);
                        BufferedInputStream bufferedInputStream2 = new BufferedInputStream(httpURLConnection.getErrorStream());
                        try {
                            String str = new String(HttpUtils.parseInputStream(bufferedInputStream2), "UTF-8");
                            responseData.setCode(responseCode);
                            responseData.setData(str);
                            httpProbe.onResponse(responseData);
                            HttpUtils.closeQuietly((InputStream) bufferedInputStream2);
                            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
                        } catch (MalformedURLException unused) {
                            bufferedInputStream = bufferedInputStream2;
                            ALog.e(TAG, "caught MalformedURLException!");
                            responseData.setCode(-1);
                            httpProbe.onResponse(responseData);
                            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
                        } catch (IOException unused2) {
                            bufferedInputStream = bufferedInputStream2;
                            ALog.e(TAG, "caught IOException!");
                            responseData.setCode(-1);
                            httpProbe.onResponse(responseData);
                            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
                        } catch (Throwable th2) {
                            th = th2;
                            bufferedInputStream = bufferedInputStream2;
                            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
                            throw th;
                        }
                    } else {
                        BufferedInputStream bufferedInputStream3 = new BufferedInputStream(httpURLConnection.getInputStream());
                        responseData.setData(new String(HttpUtils.parseInputStream(bufferedInputStream3), "UTF-8"));
                        responseData.setHeaders(HttpUtils.analyzeResponseHeaders(httpURLConnection.getHeaderFields()));
                        responseData.setCode(responseCode);
                        httpProbe.onResponse(responseData);
                        HttpUtils.closeQuietly((InputStream) bufferedInputStream3);
                        HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
                    }
                } catch (MalformedURLException unused3) {
                    ALog.e(TAG, "caught MalformedURLException!");
                    responseData.setCode(-1);
                    httpProbe.onResponse(responseData);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
                } catch (IOException unused4) {
                    ALog.e(TAG, "caught IOException!");
                    responseData.setCode(-1);
                    httpProbe.onResponse(responseData);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
                }
            } else {
                ALog.e(TAG, "can not transfer to right connection!");
                responseData.setCode(-1);
                httpProbe.onResponse(responseData);
                HttpUtils.closeQuietly((InputStream) null);
                HttpUtils.closeQuietly((OutputStream) null);
            }
        } catch (MalformedURLException unused5) {
            bufferedOutputStream = null;
            ALog.e(TAG, "caught MalformedURLException!");
            responseData.setCode(-1);
            httpProbe.onResponse(responseData);
            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
        } catch (IOException unused6) {
            bufferedOutputStream = null;
            ALog.e(TAG, "caught IOException!");
            responseData.setCode(-1);
            httpProbe.onResponse(responseData);
            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
        } catch (Throwable th3) {
            th = th3;
            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
            throw th;
        }
    }

    private void addFormDatas(OutputStream outputStream, RequestData requestData, String str) {
        JSONArray parseArray = ZSONArray.parseArray(requestData.getData());
        if (!(parseArray == null || parseArray.size() == 0)) {
            for (int i = 0; i < parseArray.size(); i++) {
                JSONObject jSONObject = parseArray.getJSONObject(i);
                String string = jSONObject.getString("name");
                String string2 = jSONObject.getString("value");
                if (string == null || string2 == null || "".equals(string) || "".equals(string2)) {
                    ALog.w(TAG, "formdatas' name or value is null!");
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(PREFIX + str + "\r\n" + "Content-Disposition: form-data; name=\"" + string + "\"" + "\r\n");
                    stringBuffer.append("\r\n");
                    StringBuilder sb = new StringBuilder();
                    sb.append(string2);
                    sb.append("\r\n");
                    stringBuffer.append(sb.toString());
                    try {
                        outputStream.write(stringBuffer.toString().getBytes("UTF-8"));
                    } catch (IOException unused) {
                        ALog.e(TAG, "write formdatas caught IOException!");
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0127, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x012c, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x012d, code lost:
        r0.addSuppressed(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0130, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addFormFileDatas(java.io.OutputStream r11, com.huawei.ace.systemplugin.httpaccess.data.RequestData r12, java.lang.String r13) {
        /*
        // Method dump skipped, instructions count: 321
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ace.systemplugin.httpaccess.adapter.HttpUploadImpl.addFormFileDatas(java.io.OutputStream, com.huawei.ace.systemplugin.httpaccess.data.RequestData, java.lang.String):void");
    }

    private String getMimeType(FormFileData formFileData) {
        String type = formFileData.getType();
        if (!"".equals(type)) {
            return getMimeWithSuffix(type);
        }
        String suffix = getSuffix(formFileData.getFileName());
        if (!"".equals(suffix)) {
            return getMimeWithSuffix(suffix);
        }
        String suffix2 = getSuffix(formFileData.getUri());
        return !"".equals(suffix2) ? getMimeWithSuffix(suffix2) : DEFAULT_MIME_TYPE;
    }

    private String getMimeWithSuffix(String str) {
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(str);
        if (mimeTypeFromExtension == null || mimeTypeFromExtension.isEmpty()) {
            mimeTypeFromExtension = DEFAULT_MIME_TYPE;
        }
        ALog.d(TAG, "final mimeType : " + mimeTypeFromExtension);
        return mimeTypeFromExtension;
    }

    private String getSuffix(String str) {
        int lastIndexOf = str.lastIndexOf(".");
        return lastIndexOf > 0 ? str.substring(lastIndexOf + 1) : "";
    }

    private String getFileNameByUri(String str) {
        int lastIndexOf = str.lastIndexOf("/");
        return lastIndexOf > 0 ? str.substring(lastIndexOf + 1) : "";
    }
}
