package com.hanhan.javautil.basedao.injector;

import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class CacheDeleteManager {

    public void removeCache(String prefix, String key) {
        if (TransactionCallback.isTransactionActive()) {
            TransactionCallback.callback(() -> {
                //删除缓存逻辑
            });
        } else {
            //删除缓存逻辑
        }
    }

    public void removeCache(String prefix, Set<String> keys) {
        if (TransactionCallback.isTransactionActive()) {
            TransactionCallback.callback(() -> {
                //删除缓存逻辑
            });
        } else {
            //删除缓存逻辑
        }
    }
}
