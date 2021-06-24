package ohos.security.permission.adapter;

import android.os.Process;
import com.huawei.android.os.UserHandleEx;

public class UserHandleAdapter {
    public static final int PER_USER_RANGE = 100000;

    private UserHandleAdapter() {
    }

    public static boolean isInMultiUserMode() {
        return !isOwner();
    }

    public static boolean isOwner() {
        return UserHandleEx.getUserId(Process.myUid()) == 0;
    }

    public static int myUserId() {
        return getUserId(Process.myUid());
    }

    public static int getUserId(int i) {
        return UserHandleEx.getUserId(i);
    }
}
