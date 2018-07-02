package server.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import sun.rmi.runtime.Log;
import util.HttpUtil;
import util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class Http_get implements Runnable {
    private CountDownLatch latch;
    private HttpUtil httpUtil;
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;
    private org.apache.http.client.methods.HttpGet httpGet;


    Http_get(CountDownLatch latch){
        this.latch = latch;
        this.httpUtil = HttpTest.httpUtil;
    }


    @Override
    public void run() {
        httpClient = httpUtil.getClient();
        //        请求配置信息
        RequestConfig config = RequestConfig.custom().setConnectTimeout(HttpTest.connectionTimeout) // 创建连接的最长时间
                .setConnectionRequestTimeout(HttpTest.requestTimeout) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(HttpTest.socketTimeout) // 数据传输的最长时间10s
                .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
                .build();
        if("POST".equals(HttpTest.type)){
            httpPost = new HttpPost(HttpTest.url);
            httpPost.setConfig(config);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for(String key:HttpTest.map_body.keySet()){
                params.add(new BasicNameValuePair(key, HttpTest.map_body.get(key)));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                for(int i=1; i<= HttpTest.iterator; i++) {
                    long startTime, endTime, responseTime;
                    startTime = System.currentTimeMillis();
//                HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    int code = httpResponse.getStatusLine().getStatusCode();
                LogUtil.httpOutLog(code+"-----------------------");

                    endTime = System.currentTimeMillis();
                    HttpEntity httpEntity = httpResponse.getEntity();
                    LogUtil.httpOutLog(EntityUtils.toString(httpEntity));
//                    InputStream in = httpEntity.getContent();
//                LogUtil.httpOutLog(in.read()+"--------------");
//                    in.close();
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
        }else if("GET".equals(HttpTest.type)){
            httpGet = new org.apache.http.client.methods.HttpGet(HttpTest.url);
            httpGet.setConfig(config);
            try {
                for(int i=1; i<= HttpTest.iterator; i++) {
                    long startTime, endTime, responseTime;
                    startTime = System.currentTimeMillis();
                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    int code = httpResponse.getStatusLine().getStatusCode();
//                LogUtil.httpOutLog(code+"-----------------------");

                    endTime = System.currentTimeMillis();
                    HttpEntity httpEntity = httpResponse.getEntity();
                    InputStream in = httpEntity.getContent();
//                LogUtil.httpOutLog(in.read()+"--------------");
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
}
