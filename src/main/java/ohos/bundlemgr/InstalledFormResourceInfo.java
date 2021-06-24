package ohos.bundlemgr;

/* access modifiers changed from: package-private */
public class InstalledFormResourceInfo {
    String abilityName;
    String bundleName;
    int descriptionId;
    String hapSourceDir;
    boolean isJsForm;
    int[] landscapeLayoutIds;
    String moduleName;
    String name;
    String originalBundleName;
    String packageName;
    int[] portraitLayoutIds;

    InstalledFormResourceInfo() {
        this(null);
    }

    InstalledFormResourceInfo(InstalledFormResourceInfo installedFormResourceInfo) {
        this.descriptionId = 0;
        if (installedFormResourceInfo != null) {
            this.packageName = installedFormResourceInfo.packageName;
            this.hapSourceDir = installedFormResourceInfo.hapSourceDir;
            this.bundleName = installedFormResourceInfo.bundleName;
            this.originalBundleName = installedFormResourceInfo.originalBundleName;
            this.moduleName = installedFormResourceInfo.moduleName;
            this.abilityName = installedFormResourceInfo.abilityName;
            this.name = installedFormResourceInfo.name;
            this.isJsForm = installedFormResourceInfo.isJsForm;
            this.descriptionId = installedFormResourceInfo.descriptionId;
            int[] iArr = installedFormResourceInfo.landscapeLayoutIds;
            if (iArr != null) {
                this.landscapeLayoutIds = (int[]) iArr.clone();
            }
            int[] iArr2 = installedFormResourceInfo.portraitLayoutIds;
            if (iArr2 != null) {
                this.portraitLayoutIds = (int[]) iArr2.clone();
            }
        }
    }
}
