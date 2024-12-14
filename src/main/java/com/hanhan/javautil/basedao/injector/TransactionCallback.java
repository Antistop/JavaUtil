package com.hanhan.javautil.basedao.injector;

import com.hanhan.javautil.exception.BizError;
import com.hanhan.javautil.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 事务回调注册工具类
 */
@Slf4j
public class TransactionCallback {
    private static final ThreadLocal<List<Runnable>> callback = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<List<Runnable>> commit = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<List<Runnable>> rollback = ThreadLocal.withInitial(ArrayList::new);

    static void doCommitCallback() {
        execute(commit);
        execute(callback);
    }
    /**
     * 注册事务执行成功后回调
     * */
    public static void commitCallback(Runnable runnable) {
        if(!isTransactionActive()){
            throw new BizException(BizError.SYSTEM_CONFIG_ERROR,"事务回调必须在事务注解内注册");
        }
        commit.get().add(runnable);
    }
    /**
     * 注册事务执行失败后回调
     * */
    public static void rollbackCallback(Runnable runnable){
        if(!isTransactionActive()){
            throw new BizException(BizError.SYSTEM_CONFIG_ERROR,"事务回调必须在事务注解内注册");
        }
        rollback.get().add(runnable);
    }

    public static boolean isTransactionActive(){
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

    /**
     * 注册事务执行后回调，不管成功还是失败
     * */
    public static void callback(Runnable runnable){
        if(!TransactionSynchronizationManager.isActualTransactionActive()){
            throw new BizException(BizError.SYSTEM_CONFIG_ERROR,"事务回调必须在事务注解内注册");
        }
        callback.get().add(runnable);
    }

    private static void execute(ThreadLocal<List<Runnable>> t) {
        List<Runnable> jobs = t.get();
        if (jobs.isEmpty()) {
            return;
        }
        try {
            for (Runnable job : jobs) {
                try {
                    job.run();
                }catch (Throwable e){
                    log.error("事务回调执行失败",e);
                }
            }
        } finally {
            jobs.clear();
        }

    }

    static void doRollback() {
        execute(rollback);
        execute(callback);
    }

}
