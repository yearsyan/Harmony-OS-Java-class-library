package ohos.aafwk.ability;

import java.util.HashSet;

/* access modifiers changed from: package-private */
public class FormDBRecord {
    String abilityName;
    String bundleName;
    long formId;
    HashSet<Integer> formUserUids;
    String moduleName;
    String originalBundleName;
    int userId;

    FormDBRecord(long j, int i, String str, String str2, String str3, String str4, HashSet<Integer> hashSet) {
        this.formId = j;
        this.userId = i;
        this.bundleName = str;
        this.moduleName = str2;
        this.originalBundleName = str3;
        this.abilityName = str4;
        HashSet<Integer> hashSet2 = this.formUserUids;
        if (hashSet2 == null) {
            this.formUserUids = new HashSet<>();
        } else {
            hashSet2.clear();
        }
        if (hashSet != null) {
            this.formUserUids.addAll(hashSet);
        }
    }
}
