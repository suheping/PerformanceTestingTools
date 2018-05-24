package server.jdbcTest;

import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;

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
        long startTime, endTime, responseTime;
//        取到当前是第几个线程
        int threadId = Integer.parseInt(Thread.currentThread().getName());
        try {
//            System.out.println(Thread.currentThread().getName() + " started ------------");
//            从连接池取连接
            Connection conn = DBUtil.getConnection();
//            System.out.println(Thread.currentThread().getName() + " connected to db-------");
//            准备sql
            PreparedStatement ps = conn.prepareStatement(QueryTest.SQL);
            for(int i =1; i<=QueryTest.iterator; i++){  //开始迭代
                switch (QueryTest.paramNum){   //判断参数个数
                    case 1:
                        String string = params[(threadId-1)*block + i];
                        ps.setString(1,string);
                        break;
                    case 2:
                        String string2 = params[(threadId-1)*block + i];
                        ps.setString(1, string2.split(" ")[0]);
                        ps.setString(2, string2.split(" ")[1]);
                        break;
                    case 3:
                        String string3 = params[(threadId-1)*block + i];
                        ps.setString(1, string3.split(" ")[0]);
                        ps.setString(2, string3.split(" ")[1]);
                        ps.setString(3, string3.split(" ")[2]);
                        break;
                    case 4:
                        String string4 = params[(threadId-1)*block + i];
                        ps.setString(1, string4.split(" ")[0]);
                        ps.setString(2, string4.split(" ")[1]);
                        ps.setString(3, string4.split(" ")[2]);
                        ps.setString(4, string4.split(" ")[3]);
                        break;
                    case 0:
                        break;
                }
//                记录开始时间
                startTime = System.currentTimeMillis();
//                发起jdbc请求
                ps.execute();
//              记录结束时间，计算响应时间
                endTime = System.currentTimeMillis();
                responseTime = endTime-startTime;
//                打印日志
                String str = "线程" + Thread.currentThread().getName() + " 第" + i + "次请求完成-- 查询耗时：" + responseTime + "ms ";
                System.out.println(str);
//                把响应时间存入列表responseTimeList中
                QueryTest.setValue(responseTime);
//                使用mq打印每次耗时到JTextArea
//                MqProducer.sendMessage(QueryTest.infoQueue,str);

                latch.countDown();
            }
//            线程结束，将conn归还给连接池
            conn.close();
//            System.out.println(Thread.currentThread().getName()+ "连接释放-----------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
