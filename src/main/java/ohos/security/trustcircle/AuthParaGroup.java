package ohos.security.trustcircle;

public final class AuthParaGroup {
    public static final int MAX_TYPE = 5;
    public static final int REC_ACK = 2;
    public static final int REC_AUTH_SYNC_ACK = 1;
    public static final int REC_PK = 4;
    public static final int REQ_PK = 3;

    private AuthParaGroup() {
    }

    public static class InitAuthInfo {
        private byte[] mAesTmpKey = new byte[0];
        private int mAuthType;
        private int mAuthVersion;
        private int mPolicy;
        private long mUserId;

        public InitAuthInfo(int i, int i2, int i3, long j, byte[] bArr) {
            this.mAuthType = i;
            this.mAuthVersion = i2;
            this.mPolicy = i3;
            this.mUserId = j;
            if (bArr != null) {
                this.mAesTmpKey = (byte[]) bArr.clone();
            }
        }

        public int getAuthType() {
            return this.mAuthType;
        }

        public int getAuthVersion() {
            return this.mAuthVersion;
        }

        public int getPolicy() {
            return this.mPolicy;
        }

        public long getUserId() {
            return this.mUserId;
        }

        public byte[] getAesTmpKey() {
            return (byte[]) this.mAesTmpKey.clone();
        }
    }

    public static class OnAuthSyncInfo {
        private short mAuthKeyAlgoType;
        private byte[] mAuthKeyInfo;
        private byte[] mAuthKeyInfoSign;
        private long mNonce;
        private int mPkVersion;
        private short mTaVersion;
        private byte[] mTcisId;

        private OnAuthSyncInfo() {
            this.mTcisId = new byte[0];
            this.mAuthKeyInfo = new byte[0];
            this.mAuthKeyInfoSign = new byte[0];
        }

        public byte[] getTcisId() {
            return (byte[]) this.mTcisId.clone();
        }

        public int getPkVersion() {
            return this.mPkVersion;
        }

        public short getTaVersion() {
            return this.mTaVersion;
        }

        public long getNonce() {
            return this.mNonce;
        }

        public short getAuthKeyAlgoType() {
            return this.mAuthKeyAlgoType;
        }

        public byte[] getAuthKeyInfo() {
            return (byte[]) this.mAuthKeyInfo.clone();
        }

        public byte[] getAuthKeyInfoSign() {
            return (byte[]) this.mAuthKeyInfoSign.clone();
        }

        public static class Builder {
            private final OnAuthSyncInfo mOnAuthSyncInfo = new OnAuthSyncInfo();

            public Builder setTcisId(byte[] bArr) {
                if (bArr != null) {
                    this.mOnAuthSyncInfo.mTcisId = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setPkVersion(int i) {
                this.mOnAuthSyncInfo.mPkVersion = i;
                return this;
            }

            public Builder setTaVersion(short s) {
                this.mOnAuthSyncInfo.mTaVersion = s;
                return this;
            }

            public Builder setNonce(long j) {
                this.mOnAuthSyncInfo.mNonce = j;
                return this;
            }

            public Builder setAuthKeyAlgoType(short s) {
                this.mOnAuthSyncInfo.mAuthKeyAlgoType = s;
                return this;
            }

            public Builder setAuthKeyInfo(byte[] bArr) {
                if (bArr != null) {
                    this.mOnAuthSyncInfo.mAuthKeyInfo = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setAuthKeyInfoSign(byte[] bArr) {
                if (bArr != null) {
                    this.mOnAuthSyncInfo.mAuthKeyInfoSign = (byte[]) bArr.clone();
                }
                return this;
            }

            public OnAuthSyncInfo create() {
                return this.mOnAuthSyncInfo;
            }
        }
    }

    public static class RecAuthAckInfo {
        private short mAuthKeyAlgoTypeSecondary;
        private byte[] mAuthKeyInfoSecondary;
        private byte[] mAuthKeyInfoSignSecondary;
        private byte[] mMacSecondary;
        private long mNonceSecondary;
        private int mPkVersionSecondary;
        private byte[] mTcisIdSecondary;

        private RecAuthAckInfo() {
            this.mTcisIdSecondary = new byte[0];
            this.mMacSecondary = new byte[0];
            this.mAuthKeyInfoSecondary = new byte[0];
            this.mAuthKeyInfoSignSecondary = new byte[0];
        }

        public byte[] getTcisIdSecondary() {
            return (byte[]) this.mTcisIdSecondary.clone();
        }

        public int getPkVersionSecondary() {
            return this.mPkVersionSecondary;
        }

        public long getNonceSecondary() {
            return this.mNonceSecondary;
        }

        public byte[] getMacSecondary() {
            return (byte[]) this.mMacSecondary.clone();
        }

        public short getAuthKeyAlgoTypeSecondary() {
            return this.mAuthKeyAlgoTypeSecondary;
        }

        public byte[] getAuthKeyInfoSecondary() {
            return (byte[]) this.mAuthKeyInfoSecondary.clone();
        }

        public byte[] getAuthKeyInfoSignSecondary() {
            return (byte[]) this.mAuthKeyInfoSignSecondary.clone();
        }

        public static class Builder {
            private final RecAuthAckInfo mRecAuthAckInfo = new RecAuthAckInfo();

            public Builder setTcisId(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthAckInfo.mTcisIdSecondary = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setPkVersion(int i) {
                this.mRecAuthAckInfo.mPkVersionSecondary = i;
                return this;
            }

            public Builder setNonce(long j) {
                this.mRecAuthAckInfo.mNonceSecondary = j;
                return this;
            }

            public Builder setMac(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthAckInfo.mMacSecondary = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setAuthKeyAlgoType(short s) {
                this.mRecAuthAckInfo.mAuthKeyAlgoTypeSecondary = s;
                return this;
            }

            public Builder setAuthKeyInfo(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthAckInfo.mAuthKeyInfoSecondary = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setAuthKeyInfoSign(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthAckInfo.mAuthKeyInfoSignSecondary = (byte[]) bArr.clone();
                }
                return this;
            }

            public RecAuthAckInfo create() {
                return this.mRecAuthAckInfo;
            }
        }
    }

    public static class ReqPkInfo {
        private long mUserId;

        public ReqPkInfo(long j) {
            this.mUserId = j;
        }

        public long getUserId() {
            return this.mUserId;
        }
    }

    public static class RespPkInfo {
        private short mAuthKeyAlgoType;
        private byte[] mAuthKeyData = new byte[0];
        private byte[] mAuthKeyDataSign = new byte[0];

        public RespPkInfo(short s, byte[] bArr, byte[] bArr2) {
            this.mAuthKeyAlgoType = s;
            if (bArr != null) {
                this.mAuthKeyData = (byte[]) bArr.clone();
            }
            if (bArr2 != null) {
                this.mAuthKeyDataSign = (byte[]) bArr2.clone();
            }
        }

        public short getAuthKeyAlgoType() {
            return this.mAuthKeyAlgoType;
        }

        public byte[] getAuthKeyData() {
            return (byte[]) this.mAuthKeyData.clone();
        }

        public byte[] getAuthKeyDataSign() {
            return (byte[]) this.mAuthKeyDataSign.clone();
        }
    }

    public static class OnAuthAckInfo {
        private byte[] mMac = new byte[0];
        private int mResult;
        private byte[] mSessionKey = new byte[0];
        private byte[] mSessionKeyIv = new byte[0];

        public OnAuthAckInfo(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            this.mResult = i;
            if (bArr != null) {
                this.mSessionKeyIv = (byte[]) bArr.clone();
            }
            if (bArr2 != null) {
                this.mSessionKey = (byte[]) bArr2.clone();
            }
            if (bArr3 != null) {
                this.mMac = (byte[]) bArr3.clone();
            }
        }

        public int getResult() {
            return this.mResult;
        }

        public byte[] getSessionKeyIv() {
            return (byte[]) this.mSessionKeyIv.clone();
        }

        public byte[] getSessionKey() {
            return (byte[]) this.mSessionKey.clone();
        }

        public byte[] getMac() {
            return (byte[]) this.mMac.clone();
        }
    }

    public static class RecAuthInfo {
        private byte[] mAesTmpKey;
        private short mAuthKeyAlgoType;
        private byte[] mAuthKeyInfo;
        private byte[] mAuthKeyInfoSign;
        private int mAuthType;
        private int mAuthVersion;
        private long mNonce;
        private int mPkVersion;
        private int mPolicy;
        private short mTaVersion;
        private byte[] mTcisId;
        private long mUserId;

        private RecAuthInfo() {
            this.mAesTmpKey = new byte[0];
            this.mTcisId = new byte[0];
            this.mAuthKeyInfo = new byte[0];
            this.mAuthKeyInfoSign = new byte[0];
        }

        public int getAuthType() {
            return this.mAuthType;
        }

        public int getAuthVersion() {
            return this.mAuthVersion;
        }

        public short getTaVersion() {
            return this.mTaVersion;
        }

        public long getUserId() {
            return this.mUserId;
        }

        public int getPolicy() {
            return this.mPolicy;
        }

        public byte[] getAesTmpKey() {
            return (byte[]) this.mAesTmpKey.clone();
        }

        public byte[] getTcisId() {
            return (byte[]) this.mTcisId.clone();
        }

        public int getPkVersion() {
            return this.mPkVersion;
        }

        public long getNonce() {
            return this.mNonce;
        }

        public byte[] getAuthKeyInfo() {
            return (byte[]) this.mAuthKeyInfo.clone();
        }

        public short getAuthKeyAlgoType() {
            return this.mAuthKeyAlgoType;
        }

        public byte[] getAuthKeyInfoSign() {
            return (byte[]) this.mAuthKeyInfoSign.clone();
        }

        public static class Builder {
            private final RecAuthInfo mRecAuthInfo = new RecAuthInfo();

            public Builder setAuthType(int i) {
                this.mRecAuthInfo.mAuthType = i;
                return this;
            }

            public Builder setAuthVersion(int i) {
                this.mRecAuthInfo.mAuthVersion = i;
                return this;
            }

            public Builder setTaVersion(short s) {
                this.mRecAuthInfo.mTaVersion = s;
                return this;
            }

            public Builder setPolicy(int i) {
                this.mRecAuthInfo.mPolicy = i;
                return this;
            }

            public Builder setUserId(long j) {
                this.mRecAuthInfo.mUserId = j;
                return this;
            }

            public Builder setAesTmpKey(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthInfo.mAesTmpKey = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setTcisId(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthInfo.mTcisId = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setPkVersion(int i) {
                this.mRecAuthInfo.mPkVersion = i;
                return this;
            }

            public Builder setNonce(long j) {
                this.mRecAuthInfo.mNonce = j;
                return this;
            }

            public Builder setAuthKeyAlgoType(short s) {
                this.mRecAuthInfo.mAuthKeyAlgoType = s;
                return this;
            }

            public Builder setAuthKeyInfo(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthInfo.mAuthKeyInfo = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setAuthKeyInfoSign(byte[] bArr) {
                if (bArr != null) {
                    this.mRecAuthInfo.mAuthKeyInfoSign = (byte[]) bArr.clone();
                }
                return this;
            }

            public RecAuthInfo create() {
                return this.mRecAuthInfo;
            }
        }
    }

    public static class OnAuthSyncAckInfo {
        private short mAuthKeyAlgoType;
        private byte[] mAuthKeyInfo;
        private byte[] mAuthKeyInfoSign;
        private byte[] mMac;
        private long mNonceSecondary;
        private int mPkVersionSecondary;
        private byte[] mTcisIdSecondary;

        private OnAuthSyncAckInfo() {
            this.mTcisIdSecondary = new byte[0];
            this.mMac = new byte[0];
            this.mAuthKeyInfo = new byte[0];
            this.mAuthKeyInfoSign = new byte[0];
        }

        public int getPkVersionSecondary() {
            return this.mPkVersionSecondary;
        }

        public long getNonceSecondary() {
            return this.mNonceSecondary;
        }

        public byte[] getTcisIdSecondary() {
            return (byte[]) this.mTcisIdSecondary.clone();
        }

        public byte[] getMac() {
            return (byte[]) this.mMac.clone();
        }

        public byte[] getAuthKeyInfo() {
            return (byte[]) this.mAuthKeyInfo.clone();
        }

        public byte[] getAuthKeyInfoSign() {
            return (byte[]) this.mAuthKeyInfoSign.clone();
        }

        public short getAuthKeyAlgoType() {
            return this.mAuthKeyAlgoType;
        }

        public static class Builder {
            private final OnAuthSyncAckInfo mOnAuthSyncAckInfo = new OnAuthSyncAckInfo();

            public Builder setTcisIdSecondary(byte[] bArr) {
                if (bArr != null) {
                    this.mOnAuthSyncAckInfo.mTcisIdSecondary = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setPkVersionSecondary(int i) {
                this.mOnAuthSyncAckInfo.mPkVersionSecondary = i;
                return this;
            }

            public Builder setNonceSecondary(long j) {
                this.mOnAuthSyncAckInfo.mNonceSecondary = j;
                return this;
            }

            public Builder setMac(byte[] bArr) {
                if (bArr != null) {
                    this.mOnAuthSyncAckInfo.mMac = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setAuthKeyAlgoType(short s) {
                this.mOnAuthSyncAckInfo.mAuthKeyAlgoType = s;
                return this;
            }

            public Builder setAuthKeyInfo(byte[] bArr) {
                if (bArr != null) {
                    this.mOnAuthSyncAckInfo.mAuthKeyInfo = (byte[]) bArr.clone();
                }
                return this;
            }

            public Builder setAuthKeyInfoSign(byte[] bArr) {
                if (bArr != null) {
                    this.mOnAuthSyncAckInfo.mAuthKeyInfoSign = (byte[]) bArr.clone();
                }
                return this;
            }

            public OnAuthSyncAckInfo create() {
                return this.mOnAuthSyncAckInfo;
            }
        }
    }

    public static class RecAckInfo {
        private byte[] mMac = new byte[0];

        public RecAckInfo(byte[] bArr) {
            if (bArr != null) {
                this.mMac = (byte[]) bArr.clone();
            }
        }

        public byte[] getMac() {
            return (byte[]) this.mMac.clone();
        }
    }
}
