package ohos.aafwk.ability;

import java.util.HashMap;

public class FormItemInfo {
    private String abilityModuleName;
    private String abilityName;
    private String bundleName;
    private boolean eSystemFlag;
    int eSystemPreviewLayoutId;
    private long formId;
    private String formName;
    private boolean formVisibleNotify;
    private String[] hapSourceDirs;
    private boolean isJsForm;
    private String jsComponentName;
    private String layoutIdConfig;
    private HashMap<String, String> moduleInfoMap;
    private String moduleName;
    private int orientation;
    private String originalBundleName;
    private String packageName;
    int previewLayoutId;
    private String relatedBundleName;
    private String scheduledUpdateTime;
    private int specificationId;
    private boolean temporaryFlag;
    private boolean updageFlag;
    private int updateDuration;

    public long getFormId() {
        return this.formId;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public String getOriginalBundleName() {
        return this.originalBundleName;
    }

    public String getRelatedBundleName() {
        return this.relatedBundleName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public String getAbilityName() {
        return this.abilityName;
    }

    public String getFormName() {
        return this.formName;
    }

    public String getJsComponentName() {
        return this.jsComponentName;
    }

    public String getAbilityModuleName() {
        return this.abilityModuleName;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public String getLayoutIdConfig() {
        return this.layoutIdConfig;
    }

    public int getSpecificationId() {
        return this.specificationId;
    }

    public boolean isESystem() {
        return this.eSystemFlag;
    }

    public boolean isEnableUpdateFlag() {
        return this.updageFlag;
    }

    public int getUpdateDuration() {
        return this.updateDuration;
    }

    public String getScheduledUpdateTime() {
        return this.scheduledUpdateTime;
    }

    public String[] getHapSourceDirs() {
        String[] strArr = this.hapSourceDirs;
        return strArr != null ? (String[]) strArr.clone() : new String[0];
    }

    public boolean isTemporaryForm() {
        return this.temporaryFlag;
    }

    public String getHapSourceByModuleName(String str) {
        return this.moduleInfoMap.containsKey(str) ? this.moduleInfoMap.get(str) : "";
    }

    /* access modifiers changed from: package-private */
    public boolean isValidItem() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6 = this.bundleName;
        if (str6 == null || str6.isEmpty() || (str = this.moduleName) == null || str.isEmpty() || (str2 = this.originalBundleName) == null || str2.isEmpty() || (str3 = this.abilityName) == null || str3.isEmpty() || (str4 = this.formName) == null || str4.isEmpty()) {
            return false;
        }
        if (this.isJsForm || this.previewLayoutId > 0 || ((str5 = this.layoutIdConfig) != null && !str5.isEmpty())) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isMatch(FormRecord formRecord) {
        if (formRecord != null && isEqual(formRecord.bundleName, this.bundleName) && isEqual(formRecord.moduleName, this.moduleName) && isEqual(formRecord.abilityName, this.abilityName) && isEqual(formRecord.formName, this.formName) && isEqual(formRecord.originalBundleName, this.originalBundleName) && formRecord.specification == this.specificationId) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isSameFormConfig(FormRecord formRecord) {
        if (formRecord != null && isEqual(formRecord.bundleName, this.bundleName) && isEqual(formRecord.moduleName, this.moduleName) && isEqual(formRecord.abilityName, this.abilityName) && isEqual(formRecord.formName, this.formName)) {
            return true;
        }
        return false;
    }

    private boolean isEqual(String str, String str2) {
        return str != null && str.equals(str2);
    }

    public boolean isJsForm() {
        return this.isJsForm;
    }

    public boolean isFormVisibleNotify() {
        return this.formVisibleNotify;
    }
}
