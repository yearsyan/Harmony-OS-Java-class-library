package ohos.media.photokit.adapter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import ohos.app.Context;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class PhotoDataAdapter {
    private static final String ALG_SHA256 = "SHA256";
    private static final int BYTE_OFFER = 15;
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int HEX_DIGITS_RIGHT_MOVE_FOUR = 4;
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(PhotoDataAdapter.class);
    private static final char SPLIT_CHAR = ':';

    public static String getSignatureDigest(Context context, String str) {
        PackageManager packageManager = ((android.content.Context) context.getHostContext()).getPackageManager();
        if (packageManager == null) {
            LOGGER.error("packageManager is null", new Object[0]);
            return "";
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 134217728);
            if (packageInfo != null) {
                if (packageInfo.signingInfo != null) {
                    Signature[] apkContentsSigners = packageInfo.signingInfo.getApkContentsSigners();
                    if (apkContentsSigners != null && apkContentsSigners.length > 0) {
                        return getSha256(apkContentsSigners[0].toByteArray());
                    }
                    LOGGER.error("getSignatureDigest signatures is invalid!", new Object[0]);
                    return "";
                }
            }
            LOGGER.error("getSignatureDigest packageInfo or packageInfo.signingInfo is null", new Object[0]);
            return "";
        } catch (PackageManager.NameNotFoundException unused) {
            LOGGER.error("getSignatureDigest NameNotFoundException", new Object[0]);
            return "";
        }
    }

    private static String getSha256(byte[] bArr) {
        if (!(bArr == null || bArr.length == 0)) {
            try {
                MessageDigest instance = MessageDigest.getInstance(ALG_SHA256);
                instance.update(bArr);
                return byteArrayToHexString(instance.digest());
            } catch (NoSuchAlgorithmException unused) {
                LOGGER.error("NoSuchAlgorithmException", new Object[0]);
            }
        }
        return "";
    }

    private static String byteArrayToHexString(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        char[] cArr = new char[((bArr.length * 3) - 1)];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            byte b = bArr[i2];
            if (i2 > 0) {
                cArr[i] = SPLIT_CHAR;
                i++;
            }
            int i3 = i + 1;
            char[] cArr2 = HEX_DIGITS;
            cArr[i] = cArr2[(b >>> 4) & 15];
            i = i3 + 1;
            cArr[i3] = cArr2[b & 15];
        }
        return new String(cArr);
    }
}
