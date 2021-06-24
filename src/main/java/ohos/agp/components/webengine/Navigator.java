package ohos.agp.components.webengine;

public interface Navigator {
    boolean canGo(int i);

    boolean canGoBack();

    boolean canGoForward();

    void clear();

    BrowsingList copyBrowsingList();

    void go(int i);

    void goBack();

    void goForward();
}
