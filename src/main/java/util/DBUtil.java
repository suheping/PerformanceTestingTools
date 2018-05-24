package util;

import org.apache.commons.dbcp2.BasicDataSource;
import server.jdbcTest.QueryTest;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {
//声明连接池
    private static BasicDataSource dbcp;
//为不同线程管理连接
    private static ThreadLocal<Connection> tl;

    public DBUtil(){
        try {
//            读取前台jdbc连接池配置
            dbcp = new BasicDataSource();
            dbcp.setDriverClassName(QueryTest.jdbcDriver);
            dbcp.setUrl(QueryTest.url);
            dbcp.setUsername(QueryTest.user);
            dbcp.setPassword(QueryTest.password);
            dbcp.setInitialSize(QueryTest.initsize);
            dbcp.setMaxTotal(QueryTest.maxtotal);
            dbcp.setMaxWaitMillis(QueryTest.maxwait);
            dbcp.setMaxIdle(QueryTest.maxidle);
            dbcp.setMinIdle(QueryTest.minidle);
            System.out.println(QueryTest.url);

//            初始化线程本地
            tl = new ThreadLocal<>();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  从连接池中取连接
     * @return 返回一个数据库连接
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = dbcp.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tl.set(conn);
        return conn;
    }

    /**
     * 释放连接池中的连接
     */
    public static void closeConnection(){
        try {
            Connection conn = tl.get();
            if (conn != null) {
                conn.close();
                tl.remove();
            }
        }catch (SQLException e) {
                e.printStackTrace();
        }
    }

    public static void releaseDbcp(){
        dbcp = null;
    }
}
