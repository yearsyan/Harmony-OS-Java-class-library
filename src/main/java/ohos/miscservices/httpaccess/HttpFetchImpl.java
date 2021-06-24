package ohos.miscservices.httpaccess;

import android.content.Context;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import ohos.abilityshell.utils.AbilityContextUtils;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.httpaccess.data.RequestData;

public class HttpFetchImpl {
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "HttpFetchImpl");
    private Context context;

    public HttpFetchImpl(ohos.app.Context context2) {
        Object androidContext = AbilityContextUtils.getAndroidContext(context2);
        if (androidContext instanceof Context) {
            this.context = (Context) androidContext;
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Missing exception handler attribute for start block: B:63:0x0116 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void httpUrlFetch(ohos.miscservices.httpaccess.data.RequestData r10, ohos.miscservices.httpaccess.HttpProbe r11) {
        /*
        // Method dump skipped, instructions count: 376
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.httpaccess.HttpFetchImpl.httpUrlFetch(ohos.miscservices.httpaccess.data.RequestData, ohos.miscservices.httpaccess.HttpProbe):void");
    }

    public HttpURLConnection buildConnectionWithParam(RequestData requestData) {
        HttpURLConnection httpURLConnection;
        HiLog.debug(TAG, "begin to build connection with param!", new Object[0]);
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
            HiLog.debug(TAG, "final url : %{public}s!", url);
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
                HiLog.error(TAG, "buildConnection caught UnsupportedEncodingException!", new Object[0]);
                return httpURLConnection;
            } catch (IOException unused3) {
                HiLog.error(TAG, "buildConnection caught IOException!", new Object[0]);
                return httpURLConnection;
            }
        } catch (MalformedURLException unused4) {
            httpURLConnection = null;
            HiLog.error(TAG, "buildConnection caught MalformedURLException!", new Object[0]);
            return httpURLConnection;
        } catch (UnsupportedEncodingException unused5) {
            httpURLConnection = null;
            HiLog.error(TAG, "buildConnection caught UnsupportedEncodingException!", new Object[0]);
            return httpURLConnection;
        } catch (IOException unused6) {
            httpURLConnection = null;
            HiLog.error(TAG, "buildConnection caught IOException!", new Object[0]);
            return httpURLConnection;
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0062 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.net.HttpURLConnection buildConnectionWithStream(ohos.miscservices.httpaccess.data.RequestData r6) {
        /*
        // Method dump skipped, instructions count: 137
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.httpaccess.HttpFetchImpl.buildConnectionWithStream(ohos.miscservices.httpaccess.data.RequestData):java.net.HttpURLConnection");
    }
}
