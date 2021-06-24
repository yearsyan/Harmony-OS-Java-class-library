package ohos.bundle;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class ApplicationInfo implements Sequenceable {
    private static final int MAX_LIMIT_SIZE = 1024;
    public static final Sequenceable.Producer<ApplicationInfo> PRODUCER = $$Lambda$ApplicationInfo$CuJaZGiCuIOjPg7WAZJrncFY0.INSTANCE;
    private String cpuAbi;
    public boolean debug;
    private String description;
    private int descriptionId;
    public boolean enabled;
    private String entryModuleName;
    private int flags;
    private String icon;
    private int iconId;
    private boolean isCompressNativeLibs;
    private String label;
    private int labelId;
    private List<ModuleInfo> moduleInfos;
    public String name;
    private List<String> permissions;
    private String process;
    private int supportedModes;
    public boolean systemApp;

    static /* synthetic */ ApplicationInfo lambda$static$0(Parcel parcel) {
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.unmarshalling(parcel);
        return applicationInfo;
    }

    public ApplicationInfo() {
        this(null);
    }

    public ApplicationInfo(ApplicationInfo applicationInfo) {
        this.name = "";
        this.systemApp = false;
        this.enabled = true;
        this.debug = false;
        this.icon = "";
        this.label = "";
        this.description = "";
        this.cpuAbi = "";
        this.process = "";
        this.supportedModes = 0;
        this.labelId = 0;
        this.iconId = 0;
        this.descriptionId = 0;
        this.isCompressNativeLibs = true;
        this.permissions = new ArrayList();
        this.moduleInfos = new ArrayList();
        this.flags = 0;
        this.entryModuleName = "";
        if (applicationInfo != null) {
            this.name = applicationInfo.name;
            this.icon = applicationInfo.icon;
            this.label = applicationInfo.label;
            this.description = applicationInfo.description;
            List<String> list = applicationInfo.permissions;
            if (list != null) {
                this.permissions.addAll(list);
            }
            this.process = applicationInfo.process;
            this.systemApp = applicationInfo.systemApp;
            List<ModuleInfo> list2 = applicationInfo.moduleInfos;
            if (list2 != null) {
                this.moduleInfos.addAll(list2);
            }
            this.supportedModes = applicationInfo.supportedModes;
            this.labelId = applicationInfo.labelId;
            this.iconId = applicationInfo.iconId;
            this.descriptionId = applicationInfo.descriptionId;
            this.cpuAbi = applicationInfo.cpuAbi;
            this.isCompressNativeLibs = applicationInfo.isCompressNativeLibs;
            this.enabled = applicationInfo.enabled;
            this.debug = applicationInfo.debug;
            this.flags = applicationInfo.flags;
            this.entryModuleName = applicationInfo.entryModuleName;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getLabel() {
        return this.label;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProcess() {
        return this.process;
    }

    public int getSupportedModes() {
        return this.supportedModes;
    }

    public List<String> getModuleSourceDirs() {
        ArrayList arrayList = new ArrayList();
        for (ModuleInfo moduleInfo : this.moduleInfos) {
            arrayList.add(moduleInfo.getModuleSourceDir());
        }
        return arrayList;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public List<ModuleInfo> getModuleInfos() {
        return this.moduleInfos;
    }

    public boolean getSystemApp() {
        return this.systemApp;
    }

    public int getIconId() {
        return this.iconId;
    }

    public int getDescriptionId() {
        return this.descriptionId;
    }

    public int getLabelId() {
        return this.labelId;
    }

    public String getCpuAbi() {
        return this.cpuAbi;
    }

    public boolean isCompressNativeLibs() {
        return this.isCompressNativeLibs;
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x00ac  */
    @Override // ohos.utils.Sequenceable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean marshalling(ohos.utils.Parcel r5) {
        /*
        // Method dump skipped, instructions count: 229
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bundle.ApplicationInfo.marshalling(ohos.utils.Parcel):boolean");
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        this.name = parcel.readString();
        this.icon = parcel.readString();
        this.label = parcel.readString();
        this.description = parcel.readString();
        this.cpuAbi = parcel.readString();
        this.process = parcel.readString();
        this.systemApp = parcel.readBoolean();
        this.supportedModes = parcel.readInt();
        this.iconId = parcel.readInt();
        this.descriptionId = parcel.readInt();
        this.labelId = parcel.readInt();
        this.isCompressNativeLibs = parcel.readBoolean();
        int readInt = parcel.readInt();
        if (readInt > 1024) {
            return false;
        }
        for (int i = 0; i < readInt; i++) {
            this.permissions.add(parcel.readString());
        }
        int readInt2 = parcel.readInt();
        if (readInt2 > 1024) {
            return false;
        }
        for (int i2 = 0; i2 < readInt2; i2++) {
            this.moduleInfos.add(new ModuleInfo(parcel.readString(), parcel.readString()));
        }
        this.enabled = parcel.readBoolean();
        this.debug = parcel.readBoolean();
        this.entryModuleName = parcel.readString();
        return true;
    }

    public ApplicationInfo parseApplication(HapModuleInfo hapModuleInfo, String str) {
        if (hapModuleInfo != null) {
            this.name = str + hapModuleInfo.getName();
            this.description = hapModuleInfo.getDescription();
            this.icon = hapModuleInfo.getIconPath();
            this.label = hapModuleInfo.getLabel();
            this.supportedModes = hapModuleInfo.getSupportedModes();
        }
        return this;
    }

    public void dump(String str, PrintWriter printWriter) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("prefix is null, dump failed");
        } else if (printWriter != null) {
            StringBuilder sb = new StringBuilder();
            dumpCommonAttributes(str, sb);
            dumpPermissions(str, sb);
            dumpModules(str, sb);
            printWriter.print(sb.toString());
        } else {
            throw new IllegalArgumentException("writer is null, dump failed");
        }
    }

    private void dumpCommonAttributes(String str, StringBuilder sb) {
        sb.append(str);
        sb.append("process=");
        sb.append(this.process);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("name=");
        sb.append(this.name);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("label=");
        sb.append(this.label);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("description=");
        sb.append(this.description);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("systemApp=");
        sb.append(this.systemApp);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("enabled=");
        sb.append(this.enabled);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("flags=");
        sb.append(this.flags);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("supportedModes=");
        sb.append(this.supportedModes);
        sb.append(System.lineSeparator());
        sb.append(str);
        sb.append("debug=");
        sb.append(this.debug);
        sb.append(System.lineSeparator());
    }

    private void dumpPermissions(String str, StringBuilder sb) {
        List<String> list = this.permissions;
        if (list != null && !list.isEmpty()) {
            sb.append(str);
            sb.append("permissions=");
            sb.append(this.permissions);
            sb.append(System.lineSeparator());
        }
    }

    private void dumpModules(String str, StringBuilder sb) {
        List<ModuleInfo> list = this.moduleInfos;
        if (!(list == null || list.isEmpty())) {
            sb.append(str);
            sb.append("permissions=");
            sb.append("[");
            for (ModuleInfo moduleInfo : this.moduleInfos) {
                sb.append(moduleInfo.getModuleName());
                sb.append(", ");
            }
            sb.append("]");
            sb.append(System.lineSeparator());
        }
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int i) {
        this.flags = i;
    }

    public String getEntryDir() {
        List<ModuleInfo> list;
        String str = this.entryModuleName;
        if (str != null && !str.trim().isEmpty() && (list = this.moduleInfos) != null && !list.isEmpty()) {
            for (ModuleInfo moduleInfo : this.moduleInfos) {
                if (this.entryModuleName.equals(moduleInfo.getModuleName())) {
                    return moduleInfo.getModuleSourceDir();
                }
            }
        }
        return "";
    }
}
