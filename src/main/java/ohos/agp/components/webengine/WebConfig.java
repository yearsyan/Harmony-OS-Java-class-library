package ohos.agp.components.webengine;

public interface WebConfig {
    public static final int PRIORITY_CACHE_EXPIRED_FIRST = 1;
    public static final int PRIORITY_CACHE_FIRST = 0;
    public static final int PRIORITY_CACHE_ONLY = 3;
    public static final int PRIORITY_NETWORK_ONLY = 2;
    public static final int SECURITY_ALLOW = 0;
    public static final int SECURITY_NOT_ALLOW = 1;
    public static final int SECURITY_SELF_ADAPTIVE = 2;

    String getDefaultEncoding();

    int getSecurityMode();

    String getUserAgent();

    int getWebCachePriority();

    boolean isAutoFitOnLoad();

    boolean isDataAbilityPermitted();

    boolean isJavaScriptPermitted();

    boolean isLoadsImagesPermitted();

    boolean isMediaAutoReplay();

    boolean isSupportWebSql();

    boolean isTextAutoSizing();

    boolean isViewPortFitScreen();

    boolean isWebStoragePermitted();

    void setAutoFitOnLoad(boolean z);

    void setDataAbilityPermit(boolean z);

    void setDefaultEncoding(String str);

    void setFocusFirstNode(boolean z);

    void setJavaScriptPermit(boolean z);

    void setLoadsImagesPermit(boolean z);

    void setLocationPermit(boolean z);

    void setMediaAutoReplay(boolean z);

    void setSecurityMode(int i);

    void setSupportWebSql(boolean z);

    void setTextAutoSizing(boolean z);

    void setUserAgent(String str);

    void setViewPortFitScreen(boolean z);

    void setWebCachePriority(int i);

    void setWebStoragePermit(boolean z);
}
