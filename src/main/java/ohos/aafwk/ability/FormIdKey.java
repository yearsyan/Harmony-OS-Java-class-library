package ohos.aafwk.ability;

import java.util.Objects;

/* access modifiers changed from: package-private */
public class FormIdKey {
    String abilityName;
    String bundleName;
    String formName;
    String moduleName;
    int orientation;
    int specificationId;

    FormIdKey() {
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof FormIdKey)) {
            return false;
        }
        FormIdKey formIdKey = (FormIdKey) obj;
        return this.specificationId == formIdKey.specificationId && this.orientation == formIdKey.orientation && Objects.equals(this.bundleName, formIdKey.bundleName) && Objects.equals(this.moduleName, formIdKey.moduleName) && Objects.equals(this.abilityName, formIdKey.abilityName) && Objects.equals(this.formName, formIdKey.formName);
    }

    public int hashCode() {
        return Objects.hash(this.bundleName, this.moduleName, this.abilityName, this.formName, Integer.valueOf(this.specificationId), Integer.valueOf(this.orientation));
    }
}
