package ohos.miscservices.httpaccess;

import android.webkit.MimeTypeMap;
import java.io.IOException;
import java.io.OutputStream;
import ohos.abilityshell.utils.AbilityContextUtils;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.httpaccess.data.FormFileData;
import ohos.miscservices.httpaccess.data.RequestData;
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
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "HttpUploadImpl");
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

    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0125 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void httpUrlUpload(ohos.miscservices.httpaccess.data.RequestData r11, ohos.miscservices.httpaccess.HttpProbe r12) {
        /*
        // Method dump skipped, instructions count: 340
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.httpaccess.HttpUploadImpl.httpUrlUpload(ohos.miscservices.httpaccess.data.RequestData, ohos.miscservices.httpaccess.HttpProbe):void");
    }

    private void addFormDatas(OutputStream outputStream, RequestData requestData, String str) {
        JSONArray parseArray = ZSONArray.parseArray(requestData.getData());
        if (!(parseArray == null || parseArray.size() == 0)) {
            for (int i = 0; i < parseArray.size(); i++) {
                JSONObject jSONObject = parseArray.getJSONObject(i);
                String string = jSONObject.getString("name");
                String string2 = jSONObject.getString("value");
                if (string == null || string2 == null || "".equals(string) || "".equals(string2)) {
                    HiLog.warn(TAG, "formdatas' name or value is null!", new Object[0]);
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
                        HiLog.error(TAG, "write formdatas caught IOException!", new Object[0]);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0123, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0128, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0129, code lost:
        r0.addSuppressed(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x012c, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addFormFileDatas(java.io.OutputStream r11, ohos.miscservices.httpaccess.data.RequestData r12, java.lang.String r13) {
        /*
        // Method dump skipped, instructions count: 324
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.httpaccess.HttpUploadImpl.addFormFileDatas(java.io.OutputStream, ohos.miscservices.httpaccess.data.RequestData, java.lang.String):void");
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
        HiLog.debug(TAG, "final mimeType : %{public}s!", mimeTypeFromExtension);
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
