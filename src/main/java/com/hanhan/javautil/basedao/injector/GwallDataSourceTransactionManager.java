package com.hanhan.javautil.basedao.injector;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import javax.sql.DataSource;

/**
 * @description 自定义事务管理器，扩展事务提交和回滚后处理逻辑
 */
public class GwallDataSourceTransactionManager extends DataSourceTransactionManager {

    public GwallDataSourceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        try {
            super.doCommit(status);
        } finally {
            TransactionCallback.doCommitCallback();
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        try {
            super.doRollback(status);
        } finally {
            TransactionCallback.doRollback();
        }
    }
}
