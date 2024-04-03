package com.hanhan.javautil.basedao.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author pl
 */
public class InsertBatchWithKeyMethod extends AbstractMethod {

    private static final long serialVersionUID = 1L;

    public InsertBatchWithKeyMethod() {
        super("insertBatchWithKey");
    }

    /**
     * insert into user(id, name, age) values (1, "a", 17), (2, "b", 18);
     * <script>
     * insert into user(id, name, age) values
     * <foreach collection="list" item="item" index="index" open="(" separator="),(" close=")">
     * #{item.id}, #{item.name}, #{item.age}
     * </foreach>
     * </script>
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>insert into %s %s values %s</script>";
        String fieldSql = prepareFieldSql(tableInfo);
        String valueSql = prepareValuesSql(tableInfo);
        String sqlResult = String.format(sql, tableInfo.getTableName(), fieldSql, valueSql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        //第三个参数必须和RootMapper的自定义方法名一致（数据库自动生成的主键值）
        return this.addInsertMappedStatement(mapperClass, modelClass, "insertBatchWithKey", sqlSource, new Jdbc3KeyGenerator(), "id", "id");
    }


    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(",");
        });
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }

    private String prepareValuesSql(TableInfo tableInfo) {
        StringBuilder valueSql = new StringBuilder();
        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        tableInfo.getFieldList().forEach(x -> {
                    valueSql.append("#{item.").append(x.getProperty());
                    String dbType = getDbType(x.getPropertyType().getTypeName());
                    valueSql.append(",").append("jdbcType=").append(null == x.getJdbcType()?(null == dbType?"VARCHAR":dbType):x.getJdbcType()).append("},");
                }
        );
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }


    private String getDbType(String type) {
        String javaType = null;
        if (type.contains("Long")) {
            javaType = "BIGINT";
        } else if (type.contains("Double") || type.contains("float") || type.contains("BigDecimal")) {
            javaType = "NUMERIC";
        } else if (type.contains("Boolean")) {
            javaType = "BOOLEAN";
        } else if (type.contains("Integer") || type.contains("int")) {
            javaType = "INTEGER";
        } else if (type.contains("Date")) {
            javaType = "TIMESTAMP";
        }
        return javaType;
    }
}
