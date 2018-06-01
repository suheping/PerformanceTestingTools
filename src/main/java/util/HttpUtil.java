package util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpUtil {
    private  int maxTotal, maxPerRoute;

    public HttpUtil(int maxTotal, int maxPerRoute){
        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
    }

    public  CloseableHttpClient getClient(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        return httpClient;
    }
}
