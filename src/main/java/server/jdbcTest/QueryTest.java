package server.jdbcTest;

import util.CountUtil;
import util.DBUtil;
import util.LoadParamFromFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class QueryTest {
//    定义数据库连接池配置参数
    public static String jdbcDriver = null;
    public static String url = null;
    public static String user = null;
    public static String password = null;
    public static int initsize;
    public static int maxtotal;
    public static long maxwait;
    public static int maxidle;
    public static int minidle;

    //    并发数
//    public static int threadCount = 3;
    public static int threadCount;
//    迭代次数
//    public static int iterator = 2;
    public static int iterator;
//    参数个数
    public static int paramNum;
//    SQL
    public static String SQL;
//    参数文件路径
    public static String paramFile;
//    参数数组
    static String[] params;

//    响应时间数组
    private static List<Long> responseTimeList = new ArrayList<>();


    /**
     * 定义setValue方法，向responseTimeList中传值
     * @param value 每个线程的单次迭代的响应时间
     */
    synchronized static void setValue(long value){
        responseTimeList.add(value);
    }

    /**
     * 开始执行压测
     */
    public static void pTest_jdbc (){
        //    加载参数
        if ( 0 < paramNum){  //如果有参数，读取参数；没有就不读取
            params = LoadParamFromFile.fileToList(paramFile);
        }else {
            params = null;
        }
//        实例化DBUtil --- 调用DBUtil的构造方法，创建连接池
//        new DBUtil();
        final CountDownLatch latch = new CountDownLatch(threadCount*iterator);
//        实例化要进行压测的业务
        Query query = new Query(latch);
//        开始压测
        for(int i=1; i<= threadCount; i++){
            Thread t = new Thread(query);
//            将当前是第几个线程赋值给当前线程的名字
            t.setName(String.valueOf(i));
            t.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        所有线程运行结束后，进行数据统计
        CountUtil.count(responseTimeList,threadCount,iterator);
//        所有线程运行结束，释放连接池
//        DBUtil.closeConnection();
//        本地测试完成，连接池置为null
//        DBUtil.releaseDbcp();
    }
}
