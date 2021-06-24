package ohos.agp.components.webengine;

public interface BrowsingList {
    BrowsingItem getCurrent();

    int getCurrentIndex();

    BrowsingItem getItemAt(int i);

    int getSize();
}
