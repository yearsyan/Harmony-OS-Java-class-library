package ohos.data.distributed.common;

import java.util.Base64;

public class Options {
    private byte[] binarySchema;
    private boolean isAutoSync = true;
    private boolean isBackup;
    private boolean isCreateIfMissing;
    private boolean isEncrypt;
    private KvStoreType kvStoreType = KvStoreType.DEVICE_COLLABORATION;
    private Schema schema = null;
    private SecurityLevel securityLevel = SecurityLevel.NO_LEVEL;

    public boolean isCreateIfMissing() {
        return this.isCreateIfMissing;
    }

    public Options setCreateIfMissing(boolean z) {
        this.isCreateIfMissing = z;
        return this;
    }

    public boolean isEncrypt() {
        return this.isEncrypt;
    }

    public Options setEncrypt(boolean z) {
        this.isEncrypt = z;
        return this;
    }

    public boolean isBackup() {
        return this.isBackup;
    }

    public Options setBackup(boolean z) {
        this.isBackup = z;
        return this;
    }

    public boolean isAutoSync() {
        return this.isAutoSync;
    }

    public Options setAutoSync(boolean z) {
        this.isAutoSync = z;
        return this;
    }

    public KvStoreType getKvStoreType() {
        return this.kvStoreType;
    }

    public Options setKvStoreType(KvStoreType kvStoreType2) {
        this.kvStoreType = kvStoreType2;
        return this;
    }

    public Schema getSchema() {
        return this.schema;
    }

    public Options setSchema(Schema schema2) {
        this.schema = schema2;
        return this;
    }

    public byte[] getBinarySchema() {
        return this.binarySchema;
    }

    public Options setBinarySchema(byte[] bArr) {
        this.binarySchema = Base64.getEncoder().encode(bArr);
        return this;
    }

    public SecurityLevel getSecurityLevel() {
        return this.securityLevel;
    }

    public Options setSecurityLevel(SecurityLevel securityLevel2) {
        this.securityLevel = securityLevel2;
        return this;
    }
}
