package server.http;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import util.HttpUtil;

import java.util.concurrent.CountDownLatch;

public class PostForm implements Runnable{
    private CountDownLatch latch;
    private HttpUtil httpUtil;
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;

    PostForm(CountDownLatch latch){
        this.latch = latch;
        this.httpUtil = HttpTest.httpUtil;
    }


    @Override
    public void run() {

    }
}
