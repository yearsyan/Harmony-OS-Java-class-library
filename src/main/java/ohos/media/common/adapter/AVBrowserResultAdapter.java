package ohos.media.common.adapter;

import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.service.media.MediaBrowserService;
import java.util.ArrayList;
import java.util.List;
import ohos.media.common.AVDescription;
import ohos.media.common.sessioncore.AVBrowserResult;
import ohos.media.common.sessioncore.AVElement;
import ohos.media.common.sessioncore.delegate.IAVBrowserResultDelegate;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class AVBrowserResultAdapter<T> implements IAVBrowserResultDelegate {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVBrowserResultAdapter.class);
    private final MediaBrowserService.Result<T> hostResult;
    private AVBrowserResult result;

    public AVBrowserResultAdapter(MediaBrowserService.Result<T> result2) {
        this.hostResult = result2;
    }

    private static AVBrowserResult convertResult(MediaBrowserService.Result<?> result2, AVBrowserResultAdapter<?> aVBrowserResultAdapter) {
        return new AVBrowserResult(reflectResultDebug(MediaBrowserService.Result.class, "mDebug", result2), aVBrowserResultAdapter);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0032  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object reflectResultDebug(java.lang.Class<?> r4, java.lang.String r5, android.service.media.MediaBrowserService.Result<?> r6) {
        /*
            r0 = 1
            r1 = 0
            r2 = 0
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ IllegalAccessException | NoSuchFieldException -> 0x0019, all -> 0x0016 }
            r4.setAccessible(r0)     // Catch:{ IllegalAccessException | NoSuchFieldException -> 0x0014 }
            java.lang.Object r5 = r4.get(r6)     // Catch:{ IllegalAccessException | NoSuchFieldException -> 0x0014 }
            r4.setAccessible(r2)
            return r5
        L_0x0012:
            r5 = move-exception
            goto L_0x0030
        L_0x0014:
            r5 = move-exception
            goto L_0x001b
        L_0x0016:
            r5 = move-exception
            r4 = r1
            goto L_0x0030
        L_0x0019:
            r5 = move-exception
            r4 = r1
        L_0x001b:
            ohos.media.utils.log.Logger r6 = ohos.media.common.adapter.AVBrowserResultAdapter.LOGGER     // Catch:{ all -> 0x0012 }
            java.lang.String r3 = "reflectResultDebug failed, e: %{public}s"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0012 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0012 }
            r0[r2] = r5     // Catch:{ all -> 0x0012 }
            r6.error(r3, r0)     // Catch:{ all -> 0x0012 }
            if (r4 == 0) goto L_0x002f
            r4.setAccessible(r2)
        L_0x002f:
            return r1
        L_0x0030:
            if (r4 == 0) goto L_0x0035
            r4.setAccessible(r2)
        L_0x0035:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.common.adapter.AVBrowserResultAdapter.reflectResultDebug(java.lang.Class, java.lang.String, android.service.media.MediaBrowserService$Result):java.lang.Object");
    }

    public static List<MediaBrowser.MediaItem> convertItems(List<AVElement> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (AVElement aVElement : list) {
            arrayList.add(convertItem(aVElement));
        }
        return arrayList;
    }

    public static MediaBrowser.MediaItem convertItem(AVElement aVElement) {
        return new MediaBrowser.MediaItem(convertDescription(aVElement.getAVDescription()), aVElement.getFlags());
    }

    private static MediaDescription convertDescription(AVDescription aVDescription) {
        return AVDescriptionAdapter.getMediaDescription(aVDescription);
    }

    public AVBrowserResult getResult() {
        if (this.result == null) {
            this.result = convertResult(this.hostResult, this);
        }
        return this.result;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v2, resolved type: android.service.media.MediaBrowserService$Result<T> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // ohos.media.common.sessioncore.delegate.IAVBrowserResultDelegate
    public void sendAVElement(AVElement aVElement) {
        try {
            this.hostResult.sendResult(convertItem(aVElement));
        } catch (IllegalStateException unused) {
            throw new IllegalStateException("sendAVElement called twice");
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v2, resolved type: android.service.media.MediaBrowserService$Result<T> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // ohos.media.common.sessioncore.delegate.IAVBrowserResultDelegate
    public void sendAVElementList(List<AVElement> list) {
        try {
            this.hostResult.sendResult(convertItems(list));
        } catch (IllegalStateException unused) {
            throw new IllegalStateException("sendAVElementList called twice");
        }
    }

    @Override // ohos.media.common.sessioncore.delegate.IAVBrowserResultDelegate
    public void detachForRetrieveAsync() {
        try {
            this.hostResult.detach();
        } catch (IllegalStateException unused) {
            throw new IllegalStateException("detachForRetrieveAsync called twice or called after sendAVElement or sendAVElementList has been called");
        }
    }
}
