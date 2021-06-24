package ohos.agp.components.webengine.adapter;

import android.graphics.Bitmap;
import android.webkit.WebHistoryItem;
import ohos.agp.components.webengine.BrowsingItem;
import ohos.media.image.PixelMap;
import ohos.media.image.inner.ImageDoubleFwConverter;

public class BrowsingItemBridge implements BrowsingItem {
    private Bitmap mFavicon;
    private final WebHistoryItem mWebHistoryItem;

    private BrowsingItemBridge(WebHistoryItem webHistoryItem) {
        this.mWebHistoryItem = webHistoryItem;
    }

    static BrowsingItemBridge create(WebHistoryItem webHistoryItem) {
        if (webHistoryItem == null) {
            return null;
        }
        return new BrowsingItemBridge(webHistoryItem);
    }

    @Override // ohos.agp.components.webengine.BrowsingItem
    public PixelMap getFavicon() {
        Bitmap favicon = this.mWebHistoryItem.getFavicon();
        if (favicon == null) {
            return null;
        }
        this.mFavicon = favicon.copy(favicon.getConfig(), true);
        Bitmap bitmap = this.mFavicon;
        if (bitmap == null) {
            return null;
        }
        return ImageDoubleFwConverter.createShellPixelMap(bitmap);
    }

    @Override // ohos.agp.components.webengine.BrowsingItem
    public String getTitle() {
        return this.mWebHistoryItem.getTitle();
    }

    @Override // ohos.agp.components.webengine.BrowsingItem
    public String getFirstRequestUrl() {
        return this.mWebHistoryItem.getOriginalUrl();
    }

    @Override // ohos.agp.components.webengine.BrowsingItem
    public String getCurrentUrl() {
        return this.mWebHistoryItem.getUrl();
    }
}
