package util;

import gui.JdbcGui;

import java.util.List;

public class CountUtil {

    /**
     * 定义统计响应时间、tps方法
     */
    public static void count(String env, List<Long> responseTimeList, int threadCount, int iterator){
        long sum = 0;
        int size = responseTimeList.size();
        if("jdbc".equals(env)){
            if(size ==0){
                LogUtil.jdbcOutLog("所有请求都失败了！！！！");
                LogUtil.jdbcOutLog("共提交" + threadCount*iterator + "个请求");
            }else{
                for (int i = 0; i <= size-1; i++) {
                    sum = sum + responseTimeList.get(i);
                }
                //    声明平均响应时间avg和tps
                long avg = sum / responseTimeList.size();
                LogUtil.jdbcOutLog("共提交" + threadCount*iterator + "个请求");
                LogUtil.jdbcOutLog("有" + responseTimeList.size() +"个请求成功，有" + (threadCount*iterator-responseTimeList.size()) + "个请求失败！！！");
                if(avg == 0){
                    LogUtil.jdbcOutLog("响应太快了，统计不出来，请加大并发数！！！！");
                }else {
                    long tps = threadCount * 1000 / avg;
                    LogUtil.jdbcOutLog("平均响应时间为" + avg + "ms");
                    LogUtil.jdbcOutLog("TPS为" + tps);
                }
            }
        }else if ("http".equals(env)){
            if(size ==0){
                LogUtil.httpOutLog("所有请求都失败了！！！！");
                LogUtil.httpOutLog("共提交" + threadCount*iterator + "个请求");
            }else{
                for (int i = 0; i <= size-1; i++) {
                    sum = sum + responseTimeList.get(i);
                }
                //    声明平均响应时间avg和tps
                long avg = sum / responseTimeList.size();
                LogUtil.httpOutLog("共提交" + threadCount*iterator + "个请求");
                LogUtil.httpOutLog("有" + responseTimeList.size() +"个请求成功，有" + (threadCount*iterator-responseTimeList.size()) + "个请求失败！！！");
                if(avg == 0){
                    LogUtil.httpOutLog("响应太快了，统计不出来，请加大并发数！！！！");
                }else {
                    long tps = threadCount * 1000 / avg;
                    LogUtil.httpOutLog("平均响应时间为" + avg + "ms");
                    LogUtil.httpOutLog("TPS为" + tps);
                }
            }
        }
//        最后清空responseTimeList
        responseTimeList.clear();
    }
}
