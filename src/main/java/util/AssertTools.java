package util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import server.http.HttpTest;

import java.io.IOException;

public class AssertTools {
    private static boolean successFlag =false;

    public boolean responseAssert(HttpResponse httpResponse) throws IOException {
        int code = httpResponse.getStatusLine().getStatusCode();
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
        return successFlag;
    }
}
