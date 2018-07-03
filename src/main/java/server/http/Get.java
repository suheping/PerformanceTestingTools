package server.http;

import org.apache.http.Header;
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
import util.param_tools.UUIDTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Get implements Runnable{
    private static final String CHARSET = "UTF-8";
    private CountDownLatch latch;
    private HttpUtil httpUtil;

    Get(CountDownLatch latch){
        this.latch = latch;
        this.httpUtil = HttpTest.httpUtil;
    }

    //           请求配置信息
    private RequestConfig config = RequestConfig.custom().setConnectTimeout(HttpTest.connectionTimeout) // 创建连接的最长时间
            .setConnectionRequestTimeout(HttpTest.requestTimeout) // 从连接池中获取到连接的最长时间
            .setSocketTimeout(HttpTest.socketTimeout) // 数据传输的最长时间10s
            .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
            .build();



    @Override
    public void run() {
        CloseableHttpClient httpClient = httpUtil.getClient();
        try{
//            开始执行压测
            for(int i=1; i<= HttpTest.iterator; i++){
                HttpGet httpGet;
//                组装请求的url，支持${uuid}参数化
                if(HttpTest.map_body !=null && HttpTest.map_body.size() !=0){ //如果有需要组装的参数，进行组装
                    List<NameValuePair> pairs = new ArrayList<>(HttpTest.map_body.size());
                    for(String key : HttpTest.map_body.keySet()){
                        String value = HttpTest.map_body.get(key);
                        UUIDTools uuidTools = new UUIDTools();
                        String value_new = uuidTools.replaceUUID(value);
                        pairs.add(new BasicNameValuePair(key, value_new));
                    }
//                    避免多线程、循环 多次赋值，声明局部变量url_new，只对当前迭代生效
                    String url_new = HttpTest.url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), CHARSET);
                    httpGet = new HttpGet(url_new);
//                    LogUtil.httpOutLog("url=========="+url_new);
                }else {
                    httpGet = new HttpGet(HttpTest.url);
                }

//                添加配置信息
                httpGet.setConfig(config);
//                配置请求头
                if(HttpTest.map_header !=null && HttpTest.map_header.size() !=0){
                    for(String key : HttpTest.map_header.keySet()){
                        httpGet.setHeader(key, HttpTest.map_header.get(key));
                    }
                }

                long startTime, endTime, responseTime;
                startTime = System.currentTimeMillis();
                HttpResponse httpResponse = httpClient.execute(httpGet); //发送请求
                endTime = System.currentTimeMillis();
                int code = httpResponse.getStatusLine().getStatusCode();
                boolean successFlag = false; //声明成功与否变量
                if (200 == code){ //如果状态码为200，就去做响应断言
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String response = EntityUtils.toString(httpEntity);
                    System.out.println(response);
//                    如果响应断言为空，那么flag为true
                    if (HttpTest.list_assert.size() == 0){
                        successFlag = true;
                    }else { // 进行断言判断
                        for (String str:HttpTest.list_assert){ //遍历所有断言字符串
                            if ("and".equals(HttpTest.assert_type)) {
//                            如果断言类型为and，
//                            判断响应中是否包含str，如果包含flag为true，
//                              如果不包含，flag为false，跳出循环
                                if (response.contains(str)) {
                                    successFlag = true;
                                } else {
                                    successFlag = false;
                                    break;
                                }
                            }else if ("or".equals(HttpTest.assert_type)){
//                            如果断言类型为or
//                            判断响应中是否包含str，如果包含flag为true，跳出循环
                                if(response.contains(str)){
                                    successFlag = true;
                                    break;
                                }else {
                                    successFlag = false;
                                }
                            }
                        }
                    }
                } //响应断言结束
                HttpTest.list_assert.clear();
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
                latch.countDown();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
