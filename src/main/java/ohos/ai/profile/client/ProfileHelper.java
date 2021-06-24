package ohos.ai.profile.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import ohos.ai.profile.util.LogUtil;
import ohos.ai.profile.util.SensitiveUtil;
import ohos.annotation.SystemApi;
import ohos.app.Context;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.devtools.JLogConstants;

@SystemApi
public final class ProfileHelper {
    private static final Map<Class<?>, Object> DEFAULT_VALUE_MAP = new HashMap() {
        /* class ohos.ai.profile.client.ProfileHelper.AnonymousClass1 */

        {
            put(Boolean.class, Boolean.FALSE);
            put(Short.class, 0);
            put(Integer.class, 0);
            put(Long.class, 0L);
            put(Float.class, Float.valueOf(0.0f));
            put(Double.class, Double.valueOf((double) XPath.MATCH_SCORE_QNAME));
        }
    };
    private static final String TAG = "ProfileHelper";
    private static final int TRY_TIMES = 2;
    private static final int WAIT_CONNECT_TIMEOUT = 10000;
    private static final int WAIT_RESULT_TIMEOUT = 10000;
    private static ProfileHelper instance;
    private int connectCount;
    private ProfileServiceConnection connection;
    private Context context;

    private ProfileHelper(Context context2) {
        this.context = context2;
        this.connection = new ProfileServiceConnection(context2);
    }

    public static synchronized ProfileHelper getInstance(Context context2) {
        ProfileHelper profileHelper;
        synchronized (ProfileHelper.class) {
            if (instance == null && context2 != null) {
                instance = new ProfileHelper(context2);
            }
            profileHelper = instance;
        }
        return profileHelper;
    }

    public <T> Optional<T> syncExecute(Function<ProfileClient, T> function) {
        return syncExecute(function, ForkJoinPool.commonPool());
    }

    private <T> Optional<T> syncExecute(Function<ProfileClient, T> function, Executor executor) {
        return waitResult(CompletableFuture.supplyAsync(new Supplier(function) {
            /* class ohos.ai.profile.client.$$Lambda$ProfileHelper$gEnxmywMxrjVScI_o0jCswtwFw */
            private final /* synthetic */ Function f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return ProfileHelper.this.lambda$syncExecute$0$ProfileHelper(this.f$1);
            }
        }, executor), JLogConstants.JLID_DISTRIBUTE_FILE_READ, null).orElse((T) Optional.empty());
    }

    /* access modifiers changed from: private */
    /* renamed from: executeInner */
    public <T> Optional<T> lambda$syncExecute$0$ProfileHelper(Function<ProfileClient, T> function) {
        boolean z;
        int i = 0;
        while (true) {
            if (i >= 2) {
                z = false;
                break;
            } else if (connectProfileService()) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            try {
                LogUtil.error(TAG, "Fail to connect profile service.", new Object[0]);
            } catch (Throwable th) {
                disconnectProfileService();
                throw th;
            }
        }
        Optional<T> execute = execute(function, z);
        disconnectProfileService();
        return execute;
    }

    public void asyncExecute(Consumer<ProfileClient> consumer) {
        asyncExecute(consumer, ForkJoinPool.commonPool());
    }

    private void asyncExecute(Consumer<ProfileClient> consumer, Executor executor) {
        CompletableFuture.runAsync(new Runnable(consumer) {
            /* class ohos.ai.profile.client.$$Lambda$ProfileHelper$lokZujQkqWrB5yd_Iq_bpHn54PA */
            private final /* synthetic */ Consumer f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                ProfileHelper.this.lambda$asyncExecute$2$ProfileHelper(this.f$1);
            }
        }, executor);
    }

    public /* synthetic */ void lambda$asyncExecute$2$ProfileHelper(Consumer consumer) {
        lambda$syncExecute$0$ProfileHelper(new Function(consumer) {
            /* class ohos.ai.profile.client.$$Lambda$ProfileHelper$5HmSPfOi9bzd0z6w54f89E419Ow */
            private final /* synthetic */ Consumer f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ProfileHelper.lambda$asyncExecute$1(this.f$0, (ProfileClient) obj);
            }
        });
    }

    private <T> Optional<T> waitResult(Future<T> future, int i, Runnable runnable) {
        try {
            return Optional.ofNullable(future.get((long) i, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LogUtil.error(TAG, "Wait result failed for " + SensitiveUtil.getMessage(e), new Object[0]);
            if (runnable != null) {
                runnable.run();
            }
            return Optional.empty();
        }
    }

    public synchronized boolean connectProfileService() {
        LogUtil.info(TAG, "Connecting profile service.", new Object[0]);
        if (this.connection.hasConnected()) {
            this.connectCount++;
            LogUtil.info(TAG, "Reuse existed connection, connectCount = " + this.connectCount, new Object[0]);
            return true;
        }
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        this.connection.connect(completableFuture);
        boolean booleanValue = ((Boolean) waitResult(completableFuture, JLogConstants.JLID_DISTRIBUTE_FILE_READ, new Runnable() {
            /* class ohos.ai.profile.client.$$Lambda$ProfileHelper$GyWhTJqiRyxzkjRGg0oab3QtFt0 */

            public final void run() {
                ProfileHelper.this.lambda$connectProfileService$3$ProfileHelper();
            }
        }).orElse(false)).booleanValue();
        if (booleanValue) {
            this.connectCount++;
            LogUtil.info(TAG, "Create new connection, connectCount = " + this.connectCount, new Object[0]);
        }
        return booleanValue;
    }

    public /* synthetic */ void lambda$connectProfileService$3$ProfileHelper() {
        this.connection.disconnect();
    }

    public synchronized void disconnectProfileService() {
        this.connectCount--;
        LogUtil.info(TAG, "Disconnect, connectCount = " + this.connectCount, new Object[0]);
        if (this.connectCount <= 0) {
            this.connection.disconnect();
            this.connectCount = 0;
        }
    }

    public synchronized <T> Optional<T> execute(Function<ProfileClient, T> function) {
        return execute(function, true);
    }

    private synchronized <T> Optional<T> execute(Function<ProfileClient, T> function, boolean z) {
        Optional<T> optional;
        if (z) {
            return Optional.ofNullable(function.apply(new ProfileServiceClient(this.context.getBundleName(), this.connection)));
        }
        Object newProxyInstance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{ProfileClient.class}, new ProfileClientHandler());
        if (newProxyInstance instanceof ProfileClient) {
            optional = Optional.ofNullable(function.apply((ProfileClient) newProxyInstance));
        } else {
            optional = Optional.empty();
        }
        return optional;
    }

    public boolean hasConnected() {
        return this.connection.hasConnected();
    }

    /* access modifiers changed from: private */
    public class ProfileClientHandler implements InvocationHandler {
        private ProfileClientHandler() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            Class<?> returnType = method.getReturnType();
            if (ProfileHelper.DEFAULT_VALUE_MAP.containsKey(returnType)) {
                return ProfileHelper.DEFAULT_VALUE_MAP.get(returnType);
            }
            return null;
        }
    }
}
