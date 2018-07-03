package server.http;


import util.CountUtil;
import util.HttpUtil;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class HttpTest{
    public static int threadCount, iterator;
    public static String url;
    public static String type;
    public static int maxTotal, maxPerRoute;
    public static int connectionTimeout, requestTimeout, socketTimeout;
    //    响应时间数组
    private static List<Long> responseTimeList = new ArrayList<>();
    static HttpUtil httpUtil;
//    存储body_form请求参数的map
    public static Map<String,String> map_body = new HashMap<>();
//    存储header_form
    public static Map<String,String> map_header = new HashMap<>();
//    存储assert参数的list
    public static List<String> list_assert = new ArrayList<>();
//    存储body_json的String
    public static String body_json;
//    存储body_text的String
    public static String body_text;
//    请求参数类型
    public static String param_type;
//    断言参数类型
    public static String assert_type = "or";


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

//        判断请求类型，选择要执行的请求
        if("GET".equals(type)){  //get
//            Http_get http_get = new Http_get(latch);
            Get get = new Get(latch);
            for(int i =1; i<= threadCount; i++){
                Thread t = new Thread(get);
                t.setName(""+ i);
                t.start();
            }
        }else if ("POST".equals(type)){  //post
            System.out.println("执行post");
            //判断form、json还是text类型参数
            if("form".equals(param_type)){
                System.out.println("执行form类型post");
            }else if ("json".equals(param_type)){
                System.out.println("执行json类型post");
            }else if ("text".equals(param_type)){
                System.out.println("执行text类型post");
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        所有线程运行完成，开始统计数据
        CountUtil.count("http",responseTimeList,threadCount,iterator);
        list_assert.clear();
        map_body.clear();
        map_header.clear();

    }
}
