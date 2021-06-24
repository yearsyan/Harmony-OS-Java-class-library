package ohos.data;

public class DatabaseFileConfig {
    private DatabaseFileSecurityLevel databaseFileSecurityLevel;
    private DatabaseFileType databaseFileType;
    private boolean isEncrypted;
    private String name;

    private DatabaseFileConfig(Builder builder) {
        this.name = builder.name;
        this.isEncrypted = builder.isEncrypted;
        this.databaseFileType = builder.databaseFileType;
        this.databaseFileSecurityLevel = builder.databaseFileSecurityLevel;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    public DatabaseFileType getDatabaseFileType() {
        return this.databaseFileType;
    }

    public DatabaseFileSecurityLevel getDatabaseFileSecurityLevel() {
        return this.databaseFileSecurityLevel;
    }

    public static final class Builder {
        private DatabaseFileSecurityLevel databaseFileSecurityLevel = DatabaseFileSecurityLevel.NO_LEVEL;
        private DatabaseFileType databaseFileType = DatabaseFileType.NORMAL;
        private boolean isEncrypted = false;
        private String name = null;

        public Builder setName(String str) {
            this.name = str;
            return this;
        }

        public Builder setEncrypted(boolean z) {
            this.isEncrypted = z;
            return this;
        }

        public Builder setDatabaseFileType(DatabaseFileType databaseFileType2) {
            this.databaseFileType = databaseFileType2;
            return this;
        }

        public Builder setDatabaseFileSecurityLevel(DatabaseFileSecurityLevel databaseFileSecurityLevel2) {
            this.databaseFileSecurityLevel = databaseFileSecurityLevel2;
            return this;
        }

        public DatabaseFileConfig build() {
            return new DatabaseFileConfig(this);
        }
    }
}
