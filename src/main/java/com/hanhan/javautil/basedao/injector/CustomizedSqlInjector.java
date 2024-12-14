package com.hanhan.javautil.basedao.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import java.util.List;

/**
 * @author pl
 * 需要加入全局配置对象GlobalConfig
 */
public class CustomizedSqlInjector extends DefaultSqlInjector  {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        DefaultSqlInjector sqlInjector = new DefaultSqlInjector();
        List<AbstractMethod> methodList = sqlInjector.getMethodList(mapperClass,tableInfo);
        methodList.add(new InsertBatchMethod());
        methodList.add(new InsertBatchWithKeyMethod());
        return methodList;
    }
}
