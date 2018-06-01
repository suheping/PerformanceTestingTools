package server.jdbcTest;

import org.springframework.jdbc.core.JdbcTemplate;
import util.JdbcPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import gui.JdbcGui;
import util.LogUtil;

public class Query implements Runnable{
    private CountDownLatch latch;
//    加载参数
    private String[] params = QueryTest.params;
//    块大小
    private int block = QueryTest.iterator;

    Query(CountDownLatch latch){
        this.latch = latch;
    }



    public void run() {
//        LogUtil.redirectOutLog(JdbcGui.jTextArea_out);
//        LogUtil.redirectErrorLog(JdbcGui.jTextArea_err);
        long startTime, endTime, responseTime;
//        取到当前是第几个线程
        int threadId = Integer.parseInt(Thread.currentThread().getName());
        try {
//            System.out.println(Thread.currentThread().getName() + " started ------------");
//            从连接池取连接
//            JdbcTemplate jdbcTemplate = JdbcPool.getCon();
            List<Map<String, Object>> res = null;
//            System.out.println(Thread.currentThread().getName() + " connected to db-------");
//            LogUtil.redirectOutLog(JdbcGui.jTextArea_out);

            for(int i =1; i<=QueryTest.iterator; i++){  //开始迭代
                //                记录开始时间
                startTime = System.currentTimeMillis();
                if( QueryTest.paramNum == 0){
                    res = QueryTest.jdbcTemplate.queryForList(QueryTest.SQL);
                }else {
                    String string = params[(threadId-1)*block + i];
                    //                发起jdbc请求
                    switch (QueryTest.paramNum){   //判断参数个数
                        case 1:
                            res = QueryTest.jdbcTemplate.queryForList(QueryTest.SQL,string);
                            break;
                        case 2:
                            res = QueryTest.jdbcTemplate.queryForList(QueryTest.SQL, string.split(" ")[0], string.split(" ")[1]);
                            break;
                        case 3:
                            res = QueryTest.jdbcTemplate.queryForList(QueryTest.SQL, string.split(" ")[0], string.split(" ")[1], string.split(" ")[2]);
                            break;
                        case 4:
                            res = QueryTest.jdbcTemplate.queryForList(QueryTest.SQL, string.split(" ")[0], string.split(" ")[1], string.split(" ")[2], string.split(" ")[3]);
                            break;
                        default:
                            break;
                    }
                }
                //              记录结束时间，计算响应时间
                endTime = System.currentTimeMillis();
                responseTime = endTime-startTime;
//                打印日志
                String str = "线程" + Thread.currentThread().getName() + " 第" + i + "次请求完成-- 查询耗时：" + responseTime + "ms";
//                System.out.println(str);
                LogUtil.jdbcOutLog(str);

//                把响应时间存入列表responseTimeList中
                QueryTest.setValue(responseTime);
//                使用mq打印每次耗时到JTextArea
//                MqProducer.sendMessage(QueryTest.infoQueue,str);
                latch.countDown();
            }
//            System.out.println(Thread.currentThread().getName()+ "连接释放-----------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
