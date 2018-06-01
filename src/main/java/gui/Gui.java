package gui;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

//@SpringBootApplication
public class Gui {
//    定义最底层JFrame
    static JFrame jFrame = new JFrame("性能测试工具");

    //    类构造方法
    private Gui(){

        //    定义主窗体
        Container c = jFrame.getContentPane();
//        定义整个工具最底层
        //    定义最底层面板
        JTabbedPane jTabbedPane_main = new JTabbedPane(JTabbedPane.LEFT);
        //    定义jdbc测试工具最底层
        JdbcGui jdbcGui = new JdbcGui();
        JTabbedPane jTabbedPane_jdbc = jdbcGui.jTabbedPane_jdbc;
//        定义http测试工具最底层
        HttpGui httpGui = new HttpGui();
        JTabbedPane jTabbedPane_http = httpGui.jTabbedPane_http;
//        定义tcp测试工具最底层
        JTabbedPane jTabbedPane_tcp = new JTabbedPane(JTabbedPane.TOP);

        c.add(jTabbedPane_main);

//        将jdbc工具添加到整个工具上
        jTabbedPane_main.addTab("JDBC测试工具", jTabbedPane_jdbc);
        jTabbedPane_main.setSelectedIndex(0);
//        将http工具添加到整个工具上
        jTabbedPane_main.addTab("HTTP测试工具", jTabbedPane_http);
//        将tcp工具添加到整个工具上
        jTabbedPane_main.addTab("TCP测试工具", jTabbedPane_tcp);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(1000,700);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Gui();

    }
}
