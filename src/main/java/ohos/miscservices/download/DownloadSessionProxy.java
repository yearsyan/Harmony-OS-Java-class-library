package ohos.miscservices.download;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ohos.agp.styles.attributes.TextAttrsConstants;
import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.event.notification.NotificationRequest;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.download.DownloadSession;
import ohos.miscservices.download.DownloadSessionManager;
import ohos.net.UriConverter;

public class DownloadSessionProxy {
    static final int SESSION_REMOVED = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "DownloadSessionProxy");
    private final Uri allUri = Uri.parse("content://downloads/all_downloads");
    private final Uri baseUri = Uri.parse("content://downloads/my_downloads");
    private Context context;
    private DownloadManager downloadManager;
    private Map<Long, ContentObserver> downloadSessionObserverMap = new HashMap();
    private ContentResolver resolver;
    private final Object sessionLock = new Object();
    private ohos.app.Context zosContext;

    public interface OnDownloadChangedProbe {
        void onCompleted(long j);

        void onFailed(long j, int i);

        void onPaused(long j);

        void onProgress(long j, long j2, long j3);

        void onRemoved(long j);
    }

    public boolean addAllSessionsListener(OnDownloadChangedProbe onDownloadChangedProbe) {
        return true;
    }

    public String getMimeTypeForDownloadedFile(long j) {
        return "";
    }

    public boolean removeAllSessionsListener() {
        return true;
    }

    public DownloadSessionProxy(ohos.app.Context context2) {
        this.zosContext = context2;
        this.context = DownloadUtils.getAPlatformContext(context2);
        Context context3 = this.context;
        if (context3 != null) {
            this.resolver = context3.getContentResolver();
            Object systemService = this.context.getSystemService("download");
            if (systemService instanceof DownloadManager) {
                this.downloadManager = (DownloadManager) systemService;
            }
        }
    }

    /* access modifiers changed from: private */
    public class DownloadChangedObserver extends ContentObserver {
        int prevStatus;
        OnDownloadChangedProbe probe;
        long sessionId;

        public DownloadChangedObserver(long j, int i, OnDownloadChangedProbe onDownloadChangedProbe) {
            super(DownloadUtils.getAsyncHandler());
            this.probe = onDownloadChangedProbe;
            this.sessionId = j;
            this.prevStatus = i;
        }

        /* JADX WARNING: Removed duplicated region for block: B:52:0x011a A[DONT_GENERATE] */
        /* JADX WARNING: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onChange(boolean r10, android.net.Uri r11) {
            /*
            // Method dump skipped, instructions count: 306
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.download.DownloadSessionProxy.DownloadChangedObserver.onChange(boolean, android.net.Uri):void");
        }
    }

    public boolean addSessionListener(long j, OnDownloadChangedProbe onDownloadChangedProbe) {
        if (this.downloadManager == null || this.resolver == null) {
            return false;
        }
        synchronized (this.sessionLock) {
            Cursor cursor = null;
            try {
                cursor = this.downloadManager.query(new DownloadManager.Query().setFilterById(j));
                if (cursor == null || !cursor.moveToFirst()) {
                    HiLog.error(TAG, "addSessionListener error, download does not exsist!", new Object[0]);
                    return false;
                }
                int i = cursor.getInt(cursor.getColumnIndex(NotificationRequest.CLASSIFICATION_STATUS));
                if (i == 4) {
                    onDownloadChangedProbe.onPaused(j);
                } else if (i == 8) {
                    onDownloadChangedProbe.onCompleted(j);
                } else if (i == 16) {
                    onDownloadChangedProbe.onFailed(j, cursor.getColumnIndex("reason"));
                }
                cursor.close();
                DownloadChangedObserver downloadChangedObserver = new DownloadChangedObserver(j, i, onDownloadChangedProbe);
                this.resolver.registerContentObserver(ContentUris.withAppendedId(this.baseUri, j), true, downloadChangedObserver);
                this.downloadSessionObserverMap.put(Long.valueOf(j), downloadChangedObserver);
                HiLog.debug(TAG, "addSessionListener success!", new Object[0]);
                return true;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    public void removeSessionListener(long j) {
        if (this.resolver != null) {
            synchronized (this.sessionLock) {
                ContentObserver contentObserver = this.downloadSessionObserverMap.get(Long.valueOf(j));
                if (contentObserver == null) {
                    HiLog.error(TAG, "register listener is null!", new Object[0]);
                    return;
                }
                this.resolver.unregisterContentObserver(contentObserver);
                this.downloadSessionObserverMap.remove(Long.valueOf(j));
                HiLog.debug(TAG, "removeSessionListener success!", new Object[0]);
            }
        }
    }

    public long start(DownloadConfig downloadConfig) {
        if (this.downloadManager == null) {
            return -1;
        }
        return this.downloadManager.enqueue(buildRequest(downloadConfig));
    }

    private DownloadManager.Request buildRequest(DownloadConfig downloadConfig) {
        ohos.utils.net.Uri uri;
        DownloadManager.Request request = new DownloadManager.Request(UriConverter.convertToAndroidUri(downloadConfig.getDownloadUri()));
        int i = 1;
        if (downloadConfig.getDownloadStoragePath() != null) {
            uri = downloadConfig.getDownloadStoragePath();
        } else {
            String uri2 = downloadConfig.getDownloadUri().toString();
            uri = DownloadUtils.createDownloadPathInPrivateDir(this.zosContext, "Download", uri2.length() > 0 ? uri2.substring(uri2.lastIndexOf("/") + 1) : "");
        }
        request.setDestinationUri(UriConverter.convertToAndroidUri(uri));
        request.setTitle(downloadConfig.getTitle());
        request.setDescription(downloadConfig.getDescription());
        request.setAllowedNetworkTypes(downloadConfig.getNetworkRestriction());
        request.setAllowedOverRoaming(downloadConfig.isRoamingAllowed());
        request.setAllowedOverMetered(downloadConfig.isMeteredAllowed());
        request.setRequiresCharging(downloadConfig.isRequiresCharging());
        if (!downloadConfig.isShowNotify()) {
            i = 2;
        }
        request.setNotificationVisibility(i);
        request.setRequiresDeviceIdle(downloadConfig.isDownloadInIdle());
        for (Map.Entry<String, String> entry : downloadConfig.getHttpHeaders().entrySet()) {
            request.addRequestHeader(entry.getKey(), entry.getValue());
        }
        return request;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:21|22|(1:25)) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.info(ohos.miscservices.download.DownloadSessionProxy.TAG, "dup FileDescriptor errno exception", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004c, code lost:
        ohos.rpc.MessageParcel.closeFileDescriptor(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0051, code lost:
        r2 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x003f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.rpc.MessageParcel openDownloadedFile(long r6) throws java.io.FileNotFoundException {
        /*
        // Method dump skipped, instructions count: 101
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.download.DownloadSessionProxy.openDownloadedFile(long):ohos.rpc.MessageParcel");
    }

    public int remove(long... jArr) {
        DownloadManager downloadManager2 = this.downloadManager;
        if (downloadManager2 == null) {
            return 0;
        }
        return downloadManager2.remove(jArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00ee, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00ef, code lost:
        if (r0 != null) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00f5, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00f6, code lost:
        r7.addSuppressed(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00f9, code lost:
        throw r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean rename(long r8, java.lang.String r10) {
        /*
        // Method dump skipped, instructions count: 251
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.download.DownloadSessionProxy.rename(long, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0036  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.miscservices.download.DownloadSession.DownloadInfo query(long r5) {
        /*
            r4 = this;
            android.app.DownloadManager r0 = r4.downloadManager
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            android.app.DownloadManager$Query r0 = new android.app.DownloadManager$Query
            r0.<init>()
            r2 = 1
            long[] r2 = new long[r2]
            r3 = 0
            r2[r3] = r5
            r0.setFilterById(r2)
            android.app.DownloadManager r5 = r4.downloadManager     // Catch:{ all -> 0x0032 }
            android.database.Cursor r5 = r5.query(r0)     // Catch:{ all -> 0x0032 }
            if (r5 == 0) goto L_0x002c
            boolean r6 = r5.moveToFirst()     // Catch:{ all -> 0x002a }
            if (r6 == 0) goto L_0x002c
            ohos.miscservices.download.DownloadSession$DownloadInfo r4 = r4.fromCursorToDownload(r5)     // Catch:{ all -> 0x002a }
            r5.close()
            return r4
        L_0x002a:
            r4 = move-exception
            goto L_0x0034
        L_0x002c:
            if (r5 == 0) goto L_0x0031
            r5.close()
        L_0x0031:
            return r1
        L_0x0032:
            r4 = move-exception
            r5 = r1
        L_0x0034:
            if (r5 == 0) goto L_0x0039
            r5.close()
        L_0x0039:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.download.DownloadSessionProxy.query(long):ohos.miscservices.download.DownloadSession$DownloadInfo");
    }

    public String queryMimeType(long j) {
        DownloadManager downloadManager2 = this.downloadManager;
        if (downloadManager2 == null) {
            return null;
        }
        return downloadManager2.getMimeTypeForDownloadedFile(j);
    }

    public List<DownloadSession.DownloadInfo> batchQueryByStatus(int i) {
        ArrayList arrayList = new ArrayList();
        if (this.downloadManager == null) {
            return arrayList;
        }
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(i);
        Cursor cursor = null;
        try {
            cursor = this.downloadManager.query(query);
            if (cursor == null) {
                HiLog.info(TAG, "cursor is null,just return", new Object[0]);
                return arrayList;
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DownloadSession.DownloadInfo fromCursorToDownload = fromCursorToDownload(cursor);
                if (fromCursorToDownload != null) {
                    arrayList.add(fromCursorToDownload);
                }
                cursor.moveToNext();
            }
            cursor.close();
            return arrayList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<DownloadSession.DownloadInfo> batchQuery(DownloadSessionManager.QueryArgs queryArgs) {
        ArrayList arrayList = new ArrayList();
        if (this.downloadManager == null) {
            return arrayList;
        }
        long[] queryIds = queryArgs.getQueryIds();
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(queryArgs.getQueryStatus());
        query.setFilterById(queryIds);
        Cursor cursor = null;
        try {
            cursor = this.downloadManager.query(query);
            if (cursor == null) {
                HiLog.info(TAG, "cursor is null,just return", new Object[0]);
                return arrayList;
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DownloadSession.DownloadInfo fromCursorToDownload = fromCursorToDownload(cursor);
                if (fromCursorToDownload != null) {
                    arrayList.add(fromCursorToDownload);
                }
                cursor.moveToNext();
            }
            cursor.close();
            return arrayList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<DownloadSession.DownloadInfo> batchQueryById(long... jArr) {
        ArrayList arrayList = new ArrayList();
        for (long j : jArr) {
            DownloadSession.DownloadInfo query = query(j);
            if (query != null) {
                arrayList.add(query);
            }
        }
        return arrayList;
    }

    private DownloadSession.DownloadInfo fromCursorToDownload(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        DownloadSession.DownloadInfo downloadInfo = new DownloadSession.DownloadInfo();
        downloadInfo.setDownloadId(getLong(cursor, "_id").longValue());
        downloadInfo.setTitle(getString(cursor, "title"));
        downloadInfo.setTotalBytes(getLong(cursor, "total_size").longValue());
        downloadInfo.setDownloadBytes(getLong(cursor, "bytes_so_far").longValue());
        String string = getString(cursor, TextAttrsConstants.HINT);
        if (string != null) {
            String[] split = string.split("/");
            downloadInfo.setFileName(split.length > 0 ? split[split.length - 1] : "");
            downloadInfo.setPath(UriConverter.convertToZidaneUri(Uri.parse(string)));
        }
        String string2 = getString(cursor, Constants.ELEMNAME_URL_STRING);
        if (string2 != null) {
            downloadInfo.setTargetUri(UriConverter.convertToZidaneUri(Uri.parse(string2)));
        }
        int intValue = getInt(cursor, NotificationRequest.CLASSIFICATION_STATUS).intValue();
        long j = cursor.getLong(cursor.getColumnIndexOrThrow("reason"));
        if ((intValue & 4) != 0) {
            downloadInfo.setPausedReason(DownloadUtils.getReasonCode(j));
        }
        if ((intValue & 16) != 0) {
            downloadInfo.setFailedReason(DownloadUtils.getReasonCode(j));
        }
        downloadInfo.setStatus(intValue);
        downloadInfo.setDescription(getString(cursor, "description"));
        return downloadInfo;
    }

    public static Long getMaxBytesOverDevice(ohos.app.Context context2) {
        Context aPlatformContext = DownloadUtils.getAPlatformContext(context2);
        if (aPlatformContext == null) {
            return null;
        }
        try {
            return Long.valueOf(Settings.Global.getLong(aPlatformContext.getContentResolver(), "download_manager_max_bytes_over_mobile"));
        } catch (Settings.SettingNotFoundException unused) {
            return null;
        }
    }

    public static Long getRecommendedMaxBytesOverDevice(ohos.app.Context context2) {
        Context aPlatformContext = DownloadUtils.getAPlatformContext(context2);
        if (aPlatformContext == null) {
            return null;
        }
        try {
            return Long.valueOf(Settings.Global.getLong(aPlatformContext.getContentResolver(), "download_manager_recommended_max_bytes_over_mobile"));
        } catch (Settings.SettingNotFoundException unused) {
            return null;
        }
    }

    public int pause(long j) {
        if (this.resolver == null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", (Integer) 1);
        HiLog.debug(TAG, "pause : %{public}ld", Long.valueOf(j));
        return this.resolver.update(ContentUris.withAppendedId(this.baseUri, j), contentValues, null, null);
    }

    public int pauseByManager(long j) {
        if (this.resolver == null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", (Integer) 1);
        HiLog.debug(TAG, "pause by manager, id : %{public}ld", Long.valueOf(j));
        return this.resolver.update(ContentUris.withAppendedId(this.allUri, j), contentValues, null, null);
    }

    public int resume(long j) {
        if (this.resolver == null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", (Integer) 0);
        HiLog.debug(TAG, "resume : %{public}ld", Long.valueOf(j));
        return this.resolver.update(ContentUris.withAppendedId(this.baseUri, j), contentValues, null, null);
    }

    public int resumeByManager(long j) {
        if (this.resolver == null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", (Integer) 0);
        HiLog.debug(TAG, "resume by manager, id : %{public}ld", Long.valueOf(j));
        return this.resolver.update(ContentUris.withAppendedId(this.allUri, j), contentValues, null, null);
    }

    private Long getLong(Cursor cursor, String str) {
        return Long.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(str)));
    }

    private String getString(Cursor cursor, String str) {
        String string = cursor.getString(cursor.getColumnIndexOrThrow(str));
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return string;
    }

    private Integer getInt(Cursor cursor, String str) {
        return Integer.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(str)));
    }
}
