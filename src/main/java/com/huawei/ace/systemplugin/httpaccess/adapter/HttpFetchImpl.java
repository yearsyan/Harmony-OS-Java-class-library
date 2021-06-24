package com.huawei.ace.systemplugin.httpaccess.adapter;

import android.content.Context;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.systemplugin.httpaccess.HttpProbe;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import com.huawei.ace.systemplugin.httpaccess.data.ResponseData;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import ohos.abilityshell.utils.AbilityContextUtils;

public class HttpFetchImpl {
    private static final String TAG = "HttpFetchImpl";
    private Context context;

    public HttpFetchImpl(ohos.app.Context context2) {
        Object androidContext = AbilityContextUtils.getAndroidContext(context2);
        if (androidContext instanceof Context) {
            this.context = (Context) androidContext;
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public void httpUrlFetch(RequestData requestData, HttpProbe httpProbe) {
        Throwable th;
        BufferedInputStream bufferedInputStream;
        char c;
        HttpURLConnection httpURLConnection;
        ResponseData responseData = new ResponseData();
        try {
            if (requestData.getMethod().isEmpty()) {
                requestData.setMethod("GET");
            }
            String method = requestData.getMethod();
            switch (method.hashCode()) {
                case -531492226:
                    if (method.equals("OPTIONS")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 70454:
                    if (method.equals("GET")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 79599:
                    if (method.equals("PUT")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 2213344:
                    if (method.equals("HEAD")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 2461856:
                    if (method.equals("POST")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 80083237:
                    if (method.equals("TRACE")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 1669334218:
                    if (method.equals("CONNECT")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 2012838315:
                    if (method.equals("DELETE")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    httpURLConnection = buildConnectionWithParam(requestData);
                    break;
                case 6:
                case 7:
                    httpURLConnection = buildConnectionWithStream(requestData);
                    break;
                default:
                    ALog.e(TAG, "no method match!");
                    responseData.setCode(-1);
                    httpProbe.onResponse(responseData);
                    HttpUtils.closeQuietly((InputStream) null);
                    HttpUtils.closeQuietly((OutputStream) null);
            }
            if (httpURLConnection == null) {
                ALog.e(TAG, "httpURLConnection is null!");
                responseData.setCode(-1);
                httpProbe.onResponse(responseData);
                HttpUtils.closeQuietly((InputStream) null);
                HttpUtils.closeQuietly((OutputStream) null);
            }
            httpURLConnection.connect();
            responseData.setHeaders(HttpUtils.analyzeResponseHeaders(httpURLConnection.getHeaderFields()));
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                ALog.e(TAG, "connect error, response code :" + responseCode);
                bufferedInputStream = new BufferedInputStream(httpURLConnection.getErrorStream());
                try {
                    String str = new String(HttpUtils.parseInputStream(bufferedInputStream), "UTF-8");
                    responseData.setCode(responseCode);
                    responseData.setData(str);
                    httpProbe.onResponse(responseData);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) null);
                } catch (MalformedURLException unused) {
                    ALog.e(TAG, "caught MalformedURLException!");
                    responseData.setCode(-1);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) null);
                    httpProbe.onResponse(responseData);
                } catch (IOException unused2) {
                    ALog.e(TAG, "caught IOException!");
                    responseData.setCode(-1);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) null);
                    httpProbe.onResponse(responseData);
                }
            } else {
                BufferedInputStream bufferedInputStream2 = new BufferedInputStream(httpURLConnection.getInputStream());
                try {
                    String str2 = new String(HttpUtils.parseInputStream(bufferedInputStream2), "UTF-8");
                    responseData.setCode(200);
                    responseData.setData(str2);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream2);
                } catch (MalformedURLException unused3) {
                    bufferedInputStream = bufferedInputStream2;
                    ALog.e(TAG, "caught MalformedURLException!");
                    responseData.setCode(-1);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) null);
                    httpProbe.onResponse(responseData);
                } catch (IOException unused4) {
                    bufferedInputStream = bufferedInputStream2;
                    ALog.e(TAG, "caught IOException!");
                    responseData.setCode(-1);
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) null);
                    httpProbe.onResponse(responseData);
                } catch (Throwable th2) {
                    th = th2;
                    bufferedInputStream = bufferedInputStream2;
                    HttpUtils.closeQuietly((InputStream) bufferedInputStream);
                    HttpUtils.closeQuietly((OutputStream) null);
                    throw th;
                }
                HttpUtils.closeQuietly((OutputStream) null);
                httpProbe.onResponse(responseData);
            }
        } catch (MalformedURLException unused5) {
            bufferedInputStream = null;
            ALog.e(TAG, "caught MalformedURLException!");
            responseData.setCode(-1);
            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
            HttpUtils.closeQuietly((OutputStream) null);
            httpProbe.onResponse(responseData);
        } catch (IOException unused6) {
            bufferedInputStream = null;
            ALog.e(TAG, "caught IOException!");
            responseData.setCode(-1);
            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
            HttpUtils.closeQuietly((OutputStream) null);
            httpProbe.onResponse(responseData);
        } catch (Throwable th3) {
            th = th3;
            HttpUtils.closeQuietly((InputStream) bufferedInputStream);
            HttpUtils.closeQuietly((OutputStream) null);
            throw th;
        }
    }

    public HttpURLConnection buildConnectionWithParam(RequestData requestData) {
        HttpURLConnection httpURLConnection;
        ALog.d(TAG, "begin to build connection with param!");
        String url = requestData.getUrl();
        try {
            String data = requestData.getData();
            if (!data.isEmpty()) {
                if (url.contains("?")) {
                    int indexOf = url.indexOf("?") + 1;
                    String substring = url.substring(indexOf);
                    StringBuilder sb = new StringBuilder();
                    sb.append(url.substring(0, indexOf));
                    sb.append(URLEncoder.encode(substring + "&" + data, "UTF-8"));
                    url = sb.toString();
                } else {
                    url = url + "?" + URLEncoder.encode(data, "UTF-8");
                }
            }
            ALog.d(TAG, "final url : " + url);
            URLConnection openConnection = new URL(url).openConnection();
            if (!(openConnection instanceof HttpURLConnection)) {
                return null;
            }
            httpURLConnection = (HttpURLConnection) openConnection;
            try {
                httpURLConnection.setRequestMethod(requestData.getMethod());
                HttpUtils.setConnProperty(httpURLConnection, requestData, this.context);
                return httpURLConnection;
            } catch (MalformedURLException unused) {
            } catch (UnsupportedEncodingException unused2) {
                ALog.e(TAG, "buildConnection caught UnsupportedEncodingException!");
                return httpURLConnection;
            } catch (IOException unused3) {
                ALog.e(TAG, "buildConnection caught IOException!");
                return httpURLConnection;
            }
        } catch (MalformedURLException unused4) {
            httpURLConnection = null;
            ALog.e(TAG, "buildConnection caught MalformedURLException!");
            return httpURLConnection;
        } catch (UnsupportedEncodingException unused5) {
            httpURLConnection = null;
            ALog.e(TAG, "buildConnection caught UnsupportedEncodingException!");
            return httpURLConnection;
        } catch (IOException unused6) {
            httpURLConnection = null;
            ALog.e(TAG, "buildConnection caught IOException!");
            return httpURLConnection;
        }
    }

    public HttpURLConnection buildConnectionWithStream(RequestData requestData) {
        Throwable th;
        HttpURLConnection httpURLConnection;
        BufferedOutputStream bufferedOutputStream;
        ALog.d(TAG, "begin to build connection with stream!");
        String url = requestData.getUrl();
        ALog.d(TAG, "final url : " + url);
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            URLConnection openConnection = new URL(url).openConnection();
            if (openConnection instanceof HttpURLConnection) {
                httpURLConnection = (HttpURLConnection) openConnection;
                try {
                    HttpUtils.setConnProperty(httpURLConnection, requestData, this.context);
                    httpURLConnection.setRequestMethod(requestData.getMethod());
                    bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                } catch (MalformedURLException unused) {
                    ALog.e(TAG, "buildConnection caught MalformedURLException!");
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
                    return httpURLConnection;
                } catch (UnsupportedEncodingException unused2) {
                    ALog.e(TAG, "buildConnection caught UnsupportedEncodingException!");
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
                    return httpURLConnection;
                } catch (IOException unused3) {
                    ALog.e(TAG, "buildConnection caught IOException!");
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
                    return httpURLConnection;
                }
                try {
                    bufferedOutputStream.write(requestData.getData().getBytes("UTF-8"));
                    bufferedOutputStream.flush();
                } catch (MalformedURLException unused4) {
                    bufferedOutputStream2 = bufferedOutputStream;
                } catch (UnsupportedEncodingException unused5) {
                    bufferedOutputStream2 = bufferedOutputStream;
                    ALog.e(TAG, "buildConnection caught UnsupportedEncodingException!");
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
                    return httpURLConnection;
                } catch (IOException unused6) {
                    bufferedOutputStream2 = bufferedOutputStream;
                    ALog.e(TAG, "buildConnection caught IOException!");
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
                    return httpURLConnection;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedOutputStream2 = bufferedOutputStream;
                    HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
                    throw th;
                }
            } else {
                bufferedOutputStream = null;
                httpURLConnection = null;
            }
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream);
        } catch (MalformedURLException unused7) {
            httpURLConnection = null;
            ALog.e(TAG, "buildConnection caught MalformedURLException!");
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
            return httpURLConnection;
        } catch (UnsupportedEncodingException unused8) {
            httpURLConnection = null;
            ALog.e(TAG, "buildConnection caught UnsupportedEncodingException!");
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
            return httpURLConnection;
        } catch (IOException unused9) {
            httpURLConnection = null;
            ALog.e(TAG, "buildConnection caught IOException!");
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
            return httpURLConnection;
        } catch (Throwable th3) {
            th = th3;
            HttpUtils.closeQuietly((OutputStream) bufferedOutputStream2);
            throw th;
        }
        return httpURLConnection;
    }
}
