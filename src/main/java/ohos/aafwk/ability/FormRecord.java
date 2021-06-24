package ohos.aafwk.ability;

import java.util.HashSet;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.ComponentProvider;

/* access modifiers changed from: package-private */
public class FormRecord {
    String abilityName;
    String bundleName;
    IntentParams customParams;
    int eSystemPreviewLayoutId;
    String formName;
    HashSet<Integer> formUserUids = new HashSet<>(1);
    ComponentProvider formView;
    boolean formVisibleNotify;
    int formVisibleNotifyState;
    String[] hapSourceDirs;
    InstantProvider instantProvider;
    boolean isEnableUpdate;
    boolean isInited = false;
    boolean isJsForm = true;
    String moduleName;
    boolean needFreeInstall = false;
    boolean needRefresh = false;
    int orientation;
    String originalBundleName;
    String packageName;
    int previewLayoutId;
    String relatedBundleName;
    int specification;
    boolean tempFormFlag;
    int updateAtHour;
    int updateAtMin;
    long updateDuration;
    int userId;
    boolean versionUpgrade = false;

    FormRecord() {
    }
}
