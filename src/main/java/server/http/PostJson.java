package server.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import util.AssertTools;
import util.HttpUtil;
import util.LogUtil;
import util.param_tools.UUIDTools;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class PostJson implements Runnable{
    private CountDownLatch latch;
    private HttpUtil httpUtil;
    private HttpPost httpPost;
    private CloseableHttpClient httpClient;

    PostJson(CountDownLatch latch){
        this.latch = latch;
        this.httpUtil = HttpTest.httpUtil;
    }

//    请求配置信息
    private RequestConfig config = RequestConfig.custom().setConnectTimeout(HttpTest.connectionTimeout) // 创建连接的最长时间
            .setConnectionRequestTimeout(HttpTest.requestTimeout) // 从连接池中获取到连接的最长时间
            .setSocketTimeout(HttpTest.socketTimeout) // 数据传输的最长时间10s
            .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
            .build();


    @Override
    public void run() {
        CloseableHttpClient httpClient = httpUtil.getClient();
        HttpPost httpPost;
        httpPost = new HttpPost(HttpTest.url);
//                添加配置信息
        httpPost.setConfig(config);
//                配置请求头
        if(HttpTest.map_header !=null && HttpTest.map_header.size() !=0){
            for(String key : HttpTest.map_header.keySet()){
                httpPost.setHeader(key, HttpTest.map_header.get(key));
            }
        }
        try {
            //            开始执行压测
            for(int i=1; i<= HttpTest.iterator; i++){
//                替换json中的${uuid}
                UUIDTools uuidTools =new UUIDTools();
                String body_json_new = uuidTools.replaceUUID(HttpTest.body_json);
//                设置请求正文
                httpPost.setEntity(new StringEntity(body_json_new));
//                定义开始时间、结束时间、响应时间
                long startTime, endTime, responseTime;
                startTime = System.currentTimeMillis();
                HttpResponse httpResponse = httpClient.execute(httpPost); //发送请求
                endTime = System.currentTimeMillis();
                AssertTools assertTools = new AssertTools();
                Boolean successFlag = assertTools.responseAssert(httpResponse); // 响应断言
//                如果flag为true，记录数据
                if (successFlag){
                    responseTime = endTime - startTime;
                    String string = "线程-" + Thread.currentThread().getName() + " 第" + i + "次迭代完成，响应时间为：" + responseTime + "ms";
                    HttpTest.setValue(responseTime);
                    LogUtil.httpOutLog(string);
                }else { //flag为false，输出
                    String string = "线程-" + Thread.currentThread().getName() + " 第" + i + "次迭代失败。。。";
                    LogUtil.httpOutLog(string);
                }
            }
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
