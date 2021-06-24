package ohos.agp.components.webengine;

import ohos.media.image.PixelMap;

public interface BrowsingItem {
    String getCurrentUrl();

    PixelMap getFavicon();

    String getFirstRequestUrl();

    String getTitle();
}
