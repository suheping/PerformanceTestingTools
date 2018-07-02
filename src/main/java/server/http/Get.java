package server.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import util.HttpUtil;
import util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Get implements Runnable{
    private static final String CHARSET = "UTF-8";
    private CountDownLatch latch;
    private HttpUtil httpUtil;
    private CloseableHttpClient httpClient;
    private HttpGet httpGet;

    Get(CountDownLatch latch){
        this.latch = latch;
        this.httpUtil = HttpTest.httpUtil;
    }

    @Override
    public void run() {
        try{
            httpClient = httpUtil.getClient();
//           组装请求的url
            if(HttpTest.map_body !=null && HttpTest.map_body.size() !=0){
                List<NameValuePair> pairs = new ArrayList<>(HttpTest.map_body.size());
                for(String key : HttpTest.map_body.keySet()){
                    pairs.add(new BasicNameValuePair(key, HttpTest.map_body.get(key)));
                }
                HttpTest.url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);
            }
            httpGet = new HttpGet(HttpTest.url);
//           请求配置信息
            RequestConfig config = RequestConfig.custom().setConnectTimeout(HttpTest.connectionTimeout) // 创建连接的最长时间
                    .setConnectionRequestTimeout(HttpTest.requestTimeout) // 从连接池中获取到连接的最长时间
                    .setSocketTimeout(HttpTest.socketTimeout) // 数据传输的最长时间10s
                    .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
                    .build();
            httpGet.setConfig(config);
//           配置请求头
            if(HttpTest.map_header !=null && HttpTest.map_header.size() !=0){
                for(String key : HttpTest.map_header.keySet()){
                    httpGet.setHeader(key, HttpTest.map_header.get(key));
                }
            }
//            开始执行压测
            for(int i=1; i<= HttpTest.iterator; i++){
                long startTime, endTime, responseTime;
                startTime = System.currentTimeMillis();
                HttpResponse httpResponse = httpClient.execute(httpGet);
//                int code = httpResponse.getStatusLine().getStatusCode();
                endTime = System.currentTimeMillis();
                HttpEntity httpEntity = httpResponse.getEntity();
                System.out.println(EntityUtils.toString(httpEntity));
                responseTime = endTime - startTime;
                String string = "线程-" + Thread.currentThread().getName() + " 第" + i + "次迭代完成，响应时间为：" + responseTime + "ms";
                LogUtil.httpOutLog(string);
                HttpTest.setValue(responseTime);
                latch.countDown();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
