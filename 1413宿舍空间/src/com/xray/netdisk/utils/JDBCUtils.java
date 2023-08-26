package com.xray.netdisk.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.xray.netdisk.pojo.FileItem;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 数据库工具类
 */
public class JDBCUtils {

    //创建连接池对象
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    //创建DBUtils的SQL执行对象
    private static QueryRunner queryRunner = new QueryRunner(dataSource);

    /**
     * 查询数据
     * @param clazz 实体类类型
     * @param sql 语句
     * @param args 参数
     * @param <T> 类型
     * @return
     */
    public static <T> List<T> query(Class clazz,String sql,Object... args) throws SQLException {
        BeanListHandler<T> handler = new BeanListHandler<T>(clazz);
        List<T> list = queryRunner.query(sql, handler, args);
        return list;
    }

    /**
     * 执行增删改操作
     * @param sql
     * @param args
     * @return
     * @throws SQLException
     */
    public static int update(String sql,Object... args) throws SQLException {
        int rows = queryRunner.update(sql, args);
        return rows;
    }
}
