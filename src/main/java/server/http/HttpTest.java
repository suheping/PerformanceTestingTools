package server.http;


import org.apache.http.impl.client.CloseableHttpClient;
import util.CountUtil;
import util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class HttpTest{
    public static int threadCount, iterator;
    public static String url;
    public static String type;
    public static int maxTotal, maxPerRoute;
    public static int connectionTimeout, requestTimeout, socketTimeout;
    //    响应时间数组
    private static List<Long> responseTimeList = new ArrayList<>();

    public static HttpUtil httpUtil;

//    static String urlGet = "http://www.sys.choicesoft.com.cn:66/choicedms/www/index.php/log-record-.html?browserLanguage=zh-CN&resolution=1366+X+728";
//    static String urlPost = "http://gateway.dev.choicesaas.cn/olp-takeout/openapi/elemeOrderOperate/orderOperate";

    /**
     * 定义setValue方法，向responseTimeList中传值
     * @param value 每个线程的单次迭代的响应时间
     */
    synchronized static void setValue(long value){
        responseTimeList.add(value);
    }



    public static void pTest_http() {
        //        从连接池中取连接
        httpUtil = new HttpUtil(maxTotal,maxPerRoute);

//        mapHeaders.put("Content-Type","application/json;charset=utf-8");
//        Http_bak httpBak = new Http_bak(httpWait,socketWait);
        final CountDownLatch latch = new CountDownLatch(threadCount*iterator);
        Http http = new Http(latch);

        for(int i =1; i<= threadCount; i++){
            Thread t = new Thread(http);
            t.setName(""+ i);
            t.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        所有线程运行完成，开始统计数据
        CountUtil.count(responseTimeList,threadCount,iterator);
    }
}
