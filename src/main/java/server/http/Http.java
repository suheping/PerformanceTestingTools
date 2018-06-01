package server.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import util.HttpUtil;
import util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class Http implements Runnable {
    private CountDownLatch latch;
    private HttpUtil httpUtil;
    private CloseableHttpClient httpClient;


    Http(CountDownLatch latch){
        this.latch = latch;
        this.httpUtil = HttpTest.httpUtil;
    }

    @Override
    public void run() {
        httpClient = httpUtil.getClient();
        HttpGet httpGet = new HttpGet(HttpTest.url);
//        请求配置信息
        RequestConfig config = RequestConfig.custom().setConnectTimeout(HttpTest.connectionTimeout) // 创建连接的最长时间
                .setConnectionRequestTimeout(HttpTest.requestTimeout) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(HttpTest.socketTimeout) // 数据传输的最长时间10s
                .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
                .build();
        httpGet.setConfig(config);
        try {
            for(int i=1; i<= HttpTest.iterator; i++) {
                long startTime, endTime, responseTime;
                startTime = System.currentTimeMillis();
                HttpResponse httpResponse = httpClient.execute(httpGet);
//                int code = response.getStatusLine().getStatusCode();
                endTime = System.currentTimeMillis();
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream in = httpEntity.getContent();
                in.close();
//                httpClient.close();
                responseTime = endTime - startTime;
//                System.out.println("线程-" + Thread.currentThread().getName() + " 第" + i + "次迭代完成，响应时间为：" + responseTime + "ms");
                String string = "线程-" + Thread.currentThread().getName() + " 第" + i + "次迭代完成，响应时间为：" + responseTime + "ms";
                LogUtil.httpOutLog(string);
//                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
//                System.out.println(content);
                HttpTest.setValue(responseTime);
                latch.countDown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
