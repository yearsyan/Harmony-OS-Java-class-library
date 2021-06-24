package ohos.agp.components.webengine.adapter;

import android.webkit.WebChromeClient;
import ohos.agp.components.webengine.PickFilesParams;

public class PickFilesParamsBridge implements PickFilesParams {
    private final WebChromeClient.FileChooserParams mFileChooserParams;

    PickFilesParamsBridge(WebChromeClient.FileChooserParams fileChooserParams) {
        this.mFileChooserParams = fileChooserParams;
    }

    static PickFilesParamsBridge create(WebChromeClient.FileChooserParams fileChooserParams) {
        if (fileChooserParams == null) {
            return null;
        }
        return new PickFilesParamsBridge(fileChooserParams);
    }

    @Override // ohos.agp.components.webengine.PickFilesParams
    public String[] getMimeTypes() {
        return this.mFileChooserParams.getAcceptTypes();
    }

    @Override // ohos.agp.components.webengine.PickFilesParams
    public String getDefaultName() {
        return this.mFileChooserParams.getFilenameHint();
    }

    @Override // ohos.agp.components.webengine.PickFilesParams
    public int getType() {
        int mode = this.mFileChooserParams.getMode();
        if (mode == 0) {
            return 2;
        }
        if (mode != 1) {
            return mode != 3 ? 0 : 1;
        }
        return 3;
    }

    @Override // ohos.agp.components.webengine.PickFilesParams
    public CharSequence getTitle() {
        return this.mFileChooserParams.getTitle();
    }

    @Override // ohos.agp.components.webengine.PickFilesParams
    public boolean isLiveMedia() {
        return this.mFileChooserParams.isCaptureEnabled();
    }
}
