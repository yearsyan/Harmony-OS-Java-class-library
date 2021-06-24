package ohos.agp.components.webengine.adapter;

import android.webkit.WebBackForwardList;
import java.util.function.Supplier;
import ohos.agp.components.webengine.BrowsingItem;
import ohos.agp.components.webengine.BrowsingList;
import ohos.utils.PlainArray;

public class BrowsingListBridge implements BrowsingList {
    WebBackForwardList mBackForwardList;
    private final PlainArray<BrowsingItemBridge> mItems = new PlainArray<>();

    private BrowsingListBridge(WebBackForwardList webBackForwardList) {
        this.mBackForwardList = webBackForwardList;
    }

    static BrowsingListBridge create(WebBackForwardList webBackForwardList) {
        if (webBackForwardList == null) {
            return null;
        }
        return new BrowsingListBridge(webBackForwardList);
    }

    @Override // ohos.agp.components.webengine.BrowsingList
    public int getCurrentIndex() {
        return this.mBackForwardList.getCurrentIndex();
    }

    @Override // ohos.agp.components.webengine.BrowsingList
    public int getSize() {
        return this.mBackForwardList.getSize();
    }

    @Override // ohos.agp.components.webengine.BrowsingList
    public BrowsingItem getCurrent() {
        int currentIndex = this.mBackForwardList.getCurrentIndex();
        if (this.mItems.contains(currentIndex)) {
            return this.mItems.get(currentIndex).orElseGet(new Supplier() {
                /* class ohos.agp.components.webengine.adapter.$$Lambda$BrowsingListBridge$D2Ng_D5iNMgNC5uHuySIKhdWYfw */

                @Override // java.util.function.Supplier
                public final Object get() {
                    return BrowsingListBridge.this.lambda$getCurrent$0$BrowsingListBridge();
                }
            });
        }
        BrowsingItemBridge create = BrowsingItemBridge.create(this.mBackForwardList.getCurrentItem());
        if (create == null) {
            return null;
        }
        this.mItems.put(currentIndex, create);
        return create;
    }

    public /* synthetic */ BrowsingItemBridge lambda$getCurrent$0$BrowsingListBridge() {
        return BrowsingItemBridge.create(this.mBackForwardList.getCurrentItem());
    }

    @Override // ohos.agp.components.webengine.BrowsingList
    public BrowsingItem getItemAt(int i) {
        if (this.mItems.contains(i)) {
            return this.mItems.get(i).orElseGet(new Supplier(i) {
                /* class ohos.agp.components.webengine.adapter.$$Lambda$BrowsingListBridge$IsQN8qfbHKORt9_zaDZ34MVAcA */
                private final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.util.function.Supplier
                public final Object get() {
                    return BrowsingListBridge.this.lambda$getItemAt$1$BrowsingListBridge(this.f$1);
                }
            });
        }
        BrowsingItemBridge create = BrowsingItemBridge.create(this.mBackForwardList.getItemAtIndex(i));
        if (create == null) {
            return null;
        }
        this.mItems.put(i, create);
        return create;
    }

    public /* synthetic */ BrowsingItemBridge lambda$getItemAt$1$BrowsingListBridge(int i) {
        return BrowsingItemBridge.create(this.mBackForwardList.getItemAtIndex(i));
    }
}
