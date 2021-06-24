package ohos.agp.components.webengine.adapter;

import android.webkit.WebSettings;
import ohos.agp.components.webengine.FontConfig;
import ohos.agp.components.webengine.WebConfig;

public class WebConfigBridge implements WebConfig {
    private final FontConfigInner mFontConfig = new FontConfigInner(null);
    private WebSettings mWebSettings;

    /* access modifiers changed from: package-private */
    public void setWebSettings(WebSettings webSettings) {
        this.mWebSettings = webSettings;
        this.mFontConfig.setWebSettings(this.mWebSettings);
        setDefault();
    }

    private void setDefault() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setAllowFileAccess(false);
            this.mWebSettings.setAllowFileAccessFromFileURLs(false);
            this.mWebSettings.setAllowUniversalAccessFromFileURLs(false);
            this.mWebSettings.setDisplayZoomControls(false);
            this.mWebSettings.setSafeBrowsingEnabled(false);
        }
    }

    /* access modifiers changed from: package-private */
    public FontConfig getFontConfig() {
        return this.mFontConfig;
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setJavaScriptPermit(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setJavaScriptEnabled(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setDataAbilityPermit(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setAllowContentAccess(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setWebStoragePermit(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setDomStorageEnabled(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setLocationPermit(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setGeolocationEnabled(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setLoadsImagesPermit(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setLoadsImagesAutomatically(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setMediaAutoReplay(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setMediaPlaybackRequiresUserGesture(!z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setSecurityMode(int i) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setMixedContentMode(i);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isDataAbilityPermitted() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return true;
        }
        return webSettings.getAllowContentAccess();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isJavaScriptPermitted() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return false;
        }
        return webSettings.getJavaScriptEnabled();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isWebStoragePermitted() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return false;
        }
        return webSettings.getDomStorageEnabled();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isLoadsImagesPermitted() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return true;
        }
        return webSettings.getLoadsImagesAutomatically();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isMediaAutoReplay() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return true;
        }
        return !webSettings.getMediaPlaybackRequiresUserGesture();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public int getSecurityMode() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return 1;
        }
        return webSettings.getMixedContentMode();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setAutoFitOnLoad(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setLoadWithOverviewMode(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isAutoFitOnLoad() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return false;
        }
        return webSettings.getLoadWithOverviewMode();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setViewPortFitScreen(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setUseWideViewPort(!z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isViewPortFitScreen() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return false;
        }
        return !webSettings.getUseWideViewPort();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setWebCachePriority(int i) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            if (i == 0) {
                webSettings.setCacheMode(-1);
            } else if (i == 1) {
                webSettings.setCacheMode(1);
            } else if (i == 2) {
                webSettings.setCacheMode(2);
            } else if (i == 3) {
                webSettings.setCacheMode(3);
            }
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public int getWebCachePriority() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return 0;
        }
        int cacheMode = webSettings.getCacheMode();
        int i = 1;
        if (cacheMode != 1) {
            i = 2;
            if (cacheMode != 2) {
                i = 3;
                if (cacheMode != 3) {
                    return 0;
                }
            }
        }
        return i;
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setSupportWebSql(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setDatabaseEnabled(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isSupportWebSql() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return false;
        }
        return webSettings.getDatabaseEnabled();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setFocusFirstNode(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setNeedInitialFocus(z);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setDefaultEncoding(String str) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setDefaultTextEncodingName(str);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public String getDefaultEncoding() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return "";
        }
        return webSettings.getDefaultTextEncodingName();
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setTextAutoSizing(boolean z) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setLayoutAlgorithm(z ? WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING : WebSettings.LayoutAlgorithm.NORMAL);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public boolean isTextAutoSizing() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null && webSettings.getLayoutAlgorithm() == WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING) {
            return true;
        }
        return false;
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public void setUserAgent(String str) {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings != null) {
            webSettings.setUserAgentString(str);
        }
    }

    @Override // ohos.agp.components.webengine.WebConfig
    public String getUserAgent() {
        WebSettings webSettings = this.mWebSettings;
        if (webSettings == null) {
            return "";
        }
        return webSettings.getUserAgentString();
    }

    /* access modifiers changed from: private */
    public static class FontConfigInner implements FontConfig {
        private WebSettings mWebSettings;

        private FontConfigInner() {
        }

        /* synthetic */ FontConfigInner(AnonymousClass1 r1) {
            this();
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setWebSettings(WebSettings webSettings) {
            this.mWebSettings = webSettings;
        }

        @Override // ohos.agp.components.webengine.FontConfig
        public void setFontFamily(FontConfig.FontGenericFamily fontGenericFamily, String str) {
            if (this.mWebSettings != null) {
                switch (fontGenericFamily) {
                    case GENERIC_FAMILY_STANDARD:
                        this.mWebSettings.setStandardFontFamily(str);
                        return;
                    case GENERIC_FAMILY_FIXED:
                        this.mWebSettings.setFixedFontFamily(str);
                        return;
                    case GENERIC_FAMILY_SERIF:
                        this.mWebSettings.setSerifFontFamily(str);
                        return;
                    case GENERIC_FAMILY_SANS_SERIF:
                        this.mWebSettings.setSansSerifFontFamily(str);
                        return;
                    case GENERIC_FAMILY_CURSIVE:
                        this.mWebSettings.setCursiveFontFamily(str);
                        return;
                    case GENERIC_FAMILY_FANTASY:
                        this.mWebSettings.setFantasyFontFamily(str);
                        return;
                    default:
                        return;
                }
            }
        }

        @Override // ohos.agp.components.webengine.FontConfig
        public String getFontFamily(FontConfig.FontGenericFamily fontGenericFamily) {
            if (this.mWebSettings == null) {
                return "";
            }
            switch (fontGenericFamily) {
                case GENERIC_FAMILY_STANDARD:
                    return this.mWebSettings.getStandardFontFamily();
                case GENERIC_FAMILY_FIXED:
                    return this.mWebSettings.getFixedFontFamily();
                case GENERIC_FAMILY_SERIF:
                    return this.mWebSettings.getSerifFontFamily();
                case GENERIC_FAMILY_SANS_SERIF:
                    return this.mWebSettings.getSansSerifFontFamily();
                case GENERIC_FAMILY_CURSIVE:
                    return this.mWebSettings.getCursiveFontFamily();
                case GENERIC_FAMILY_FANTASY:
                    return this.mWebSettings.getFantasyFontFamily();
                default:
                    return "";
            }
        }

        @Override // ohos.agp.components.webengine.FontConfig
        public void setFontSize(FontConfig.FontSizeType fontSizeType, int i) {
            if (this.mWebSettings != null) {
                int i2 = AnonymousClass1.$SwitchMap$ohos$agp$components$webengine$FontConfig$FontSizeType[fontSizeType.ordinal()];
                if (i2 == 1) {
                    this.mWebSettings.setDefaultFontSize(i);
                } else if (i2 == 2) {
                    this.mWebSettings.setDefaultFixedFontSize(i);
                } else if (i2 == 3) {
                    this.mWebSettings.setMinimumFontSize(i);
                } else if (i2 == 4) {
                    this.mWebSettings.setMinimumLogicalFontSize(i);
                }
            }
        }

        @Override // ohos.agp.components.webengine.FontConfig
        public int getFontSize(FontConfig.FontSizeType fontSizeType) {
            if (this.mWebSettings == null) {
                return 0;
            }
            int i = AnonymousClass1.$SwitchMap$ohos$agp$components$webengine$FontConfig$FontSizeType[fontSizeType.ordinal()];
            if (i == 1) {
                return this.mWebSettings.getDefaultFontSize();
            }
            if (i == 2) {
                return this.mWebSettings.getDefaultFixedFontSize();
            }
            if (i == 3) {
                return this.mWebSettings.getMinimumFontSize();
            }
            if (i != 4) {
                return 0;
            }
            return this.mWebSettings.getMinimumLogicalFontSize();
        }
    }

    /* renamed from: ohos.agp.components.webengine.adapter.WebConfigBridge$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$components$webengine$FontConfig$FontSizeType = new int[FontConfig.FontSizeType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|(3:27|28|30)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|(3:27|28|30)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(23:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|(3:27|28|30)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0066 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0071 */
        static {
            /*
            // Method dump skipped, instructions count: 125
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.webengine.adapter.WebConfigBridge.AnonymousClass1.<clinit>():void");
        }
    }
}
