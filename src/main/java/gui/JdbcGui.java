package gui;


import server.jdbcTest.QueryTest;
import util.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class JdbcGui implements ActionListener {

//    定义jdbc工具最底层面板
    JTabbedPane jTabbedPane_jdbc;
//    定义按钮
    private JButton jButton_start,jButton_clearOut, jButton_clearErr, jButton_apply;
//    定义文本框
    private JTextField jTextField_threadCount, jTextField_iterator, jTextField_paramNum, jTextField_sql, jTextField_paramFile;
    private JTextField jTextField_driver, jTextField_url, jTextField_user, jTextField_passwd, jTextField_initsize;
    private JTextField jTextField_maxtotal, jTextField_maxwait, jTextField_maxidle, jTextField_minidle;
//    定义文本域
    private JTextArea jTextArea_out, jTextArea_err;


    JdbcGui(){
//        定义控件
//        jdbc工具最底层面板
        jTabbedPane_jdbc = new JTabbedPane(JTabbedPane.TOP);
//        tab页1：jdbc工具
        JSplitPane jSplitPane_jdbc = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
//        tab页2：jdbc连接池配置
        JPanel jPanel_jdbcConf;
//        tab1上的切分为四部分：参数区、按钮区、结果输出区、报错输出区
//        先上下拆分，top、bottom
//        再分别左右拆分，top：param、button    bottom：out、error
//        out和err区域需要添加滚动条，所以定义JScrollPane
        JSplitPane jSplitPane_top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane jSplitPane_bottom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel jPanel_param = new JPanel();
        JPanel jPanel_button = new JPanel();
        JScrollPane jScrollPane_out = new JScrollPane();
        JScrollPane jScrollPane_err = new JScrollPane();

//        jSplitPane_jdbc配置
        jSplitPane_jdbc.setTopComponent(jSplitPane_top);
        jSplitPane_jdbc.setBottomComponent(jSplitPane_bottom);
//        jSplitPane_top配置
        jSplitPane_top.setLeftComponent(jPanel_param);
        jSplitPane_top.setRightComponent(jPanel_button);
        jSplitPane_top.setResizeWeight(0.7);
//        jSplitPane_bottom配置
        jSplitPane_bottom.setLeftComponent(jScrollPane_out);
        jSplitPane_bottom.setRightComponent(jScrollPane_err);
        jSplitPane_bottom.setOneTouchExpandable(true);
        jSplitPane_bottom.setResizeWeight(0.5);

//        定义按钮
        jButton_start = new JButton("执行");
        jButton_clearOut = new JButton("清空结果");
        jButton_clearErr = new JButton("清空报错");

//        定义参数区
        JLabel threadCountLabel = new JLabel("并发数：");
        jTextField_threadCount = new JTextField(5);
        JLabel iteratorLabel = new JLabel("迭代次数：");
        jTextField_iterator = new JTextField(5);
        JLabel paramNumLabel = new JLabel("sql参数个数：");
        jTextField_paramNum = new JTextField(5);
        jTextField_paramNum.setText("0");
        JLabel SQLLabel = new JLabel("SQL：");
        jTextField_sql = new JTextField(50);
        JLabel paramFileLabel = new JLabel("参数文件路径：");
        jTextField_paramFile = new JTextField(50);

//        定义out区和err区
        jTextArea_out = new JTextArea(10,15);
        jTextArea_err = new JTextArea(10,15);

//        tab2上包含jdbc连接池的几个必要配置，如下：
        jPanel_jdbcConf = new JPanel();
        JLabel jLabelDriver = new JLabel("jdbc.driver");
        JLabel jLabelUrl = new JLabel("jdbc.url");
        JLabel jLabelUser = new JLabel("user");
        JLabel jLabelPasswd = new JLabel("password");
        JLabel jLabelInitsize = new JLabel("initsize");
        JLabel jLabelMaxtotal = new JLabel("maxtotal");
        JLabel jLabelMaxwait = new JLabel("maxwait");
        JLabel jLabelMaxidle = new JLabel("maxidle");
        JLabel jLabelMinidle = new JLabel("minidle");

        jTextField_driver = new JTextField(50);
        jTextField_driver.setText("com.mysql.cj.jdbc.Driver");
        jTextField_url = new JTextField(50);
        jTextField_user = new JTextField(20);
        jTextField_passwd = new JPasswordField(20);
        jTextField_initsize = new JTextField(10);
        jTextField_initsize.setText("5");
        jTextField_maxtotal = new JTextField(10);
        jTextField_maxtotal.setText("200");
        jTextField_maxwait = new JTextField(10);
        jTextField_maxwait.setText("200");
        jTextField_maxidle = new JTextField(10);
        jTextField_maxidle.setText("1");
        jTextField_minidle = new JTextField(10);
        jTextField_minidle.setText("1");

        jButton_apply = new JButton("应用参数");


//        布局
//        第一个tab页
//        按钮区布局   - 默认流式布局
        jPanel_button.add(jButton_start);
        jPanel_button.add(jButton_clearOut);
        jPanel_button.add(jButton_clearErr);

//        输出区
        jScrollPane_out.setViewportView(jTextArea_out);
        jScrollPane_err.setViewportView(jTextArea_err);
        jTextArea_out.setText("温馨提示：\n" +
                "1、参数文件需要准备足够的行数，大于或等于 并发数x迭代次数；\n" +
                "2、参数个数要与sql中的?个数对应；\n" +
                "3、参数文件中如有多个参数，一个参数一列，两列间用一个空格\" \"分隔；\n" +
                "4、目前最多支持4个参数。\n" +
                "===================================================================\n" +
                "结果输出区域\n");
        jTextArea_err.setText("报错信息输出区域\n");

//        参数区布局
        //        使用网格流动布局
        GridBagLayout gridBagLayout = new GridBagLayout();
        jPanel_param.setLayout(gridBagLayout);
        GridBagConstraints gbc = new GridBagConstraints();
//        多个组件均分区域
        gbc.weightx = 1;
        gbc.weighty = 1;
//        填充
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        居左
        gbc.anchor = GridBagConstraints.WEST;
//        组件位置
//          第一行：
        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel_param.add(threadCountLabel,gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        jPanel_param.add(jTextField_threadCount,gbc);
        gbc.gridx = 4;
        gbc.gridy = 1;
        jPanel_param.add(iteratorLabel,gbc);
        gbc.gridx = 5;
        gbc.gridy = 1;
        jPanel_param.add(jTextField_iterator,gbc);
        gbc.gridx = 7;
        gbc.gridy = 1;
        jPanel_param.add(paramNumLabel,gbc);
        gbc.gridx = 8;
        gbc.gridy = 1;
        jPanel_param.add(jTextField_paramNum,gbc);
//        第二行：
        gbc.gridx = 1;
        gbc.gridy = 2;
        jPanel_param.add(SQLLabel,gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 7;
        jPanel_param.add(jTextField_sql,gbc);
//        第三行
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        jPanel_param.add(paramFileLabel,gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 7;
        jPanel_param.add(jTextField_paramFile,gbc);

//        第二个tab页
        jPanel_jdbcConf.setLayout(gridBagLayout);
        //        多个组件均分区域
        gbc.weightx = 1;
        gbc.weighty = 1;
//        填充
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        居左
        gbc.anchor = GridBagConstraints.WEST;
//        组件位置
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel_jdbcConf.add(jLabelDriver,gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        jPanel_jdbcConf.add(jTextField_driver,gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        jPanel_jdbcConf.add(jLabelUrl,gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        jPanel_jdbcConf.add(jTextField_url,gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        jPanel_jdbcConf.add(jLabelUser,gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        jPanel_jdbcConf.add(jTextField_user,gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        jPanel_jdbcConf.add(jLabelPasswd,gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;
        jPanel_jdbcConf.add(jTextField_passwd,gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        jPanel_jdbcConf.add(jLabelInitsize,gbc);
        gbc.gridx = 2;
        gbc.gridy = 5;
        jPanel_jdbcConf.add(jTextField_initsize,gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        jPanel_jdbcConf.add(jLabelMaxtotal,gbc);
        gbc.gridx = 2;
        gbc.gridy = 6;
        jPanel_jdbcConf.add(jTextField_maxtotal,gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        jPanel_jdbcConf.add(jLabelMaxwait,gbc);
        gbc.gridx = 2;
        gbc.gridy = 7;
        jPanel_jdbcConf.add(jTextField_maxwait,gbc);
        gbc.gridx = 1;
        gbc.gridy = 8;
        jPanel_jdbcConf.add(jLabelMaxidle,gbc);
        gbc.gridx = 2;
        gbc.gridy = 8;
        jPanel_jdbcConf.add(jTextField_maxidle,gbc);
        gbc.gridx = 1;
        gbc.gridy = 9;
        jPanel_jdbcConf.add(jLabelMinidle,gbc);
        gbc.gridx = 2;
        gbc.gridy = 9;
        jPanel_jdbcConf.add(jTextField_minidle,gbc);
        gbc.gridx = 1;
        gbc.gridy = 10;
        jPanel_jdbcConf.add(jButton_apply,gbc);


        jTabbedPane_jdbc.addTab("JDBC测试",jSplitPane_jdbc);
        jTabbedPane_jdbc.addTab("JDBC连接池配置",jPanel_jdbcConf);

        jButton_start.addActionListener(this);
        jButton_clearOut.addActionListener(this);
        jButton_clearErr.addActionListener(this);
        jButton_apply.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(jButton_apply ==e.getSource()){
            loadJdbcConfig();
        }else if(jButton_clearOut == e.getSource()){
            String content = "温馨提示：\n" +
                    "1、参数文件需要准备足够的行数，大于或等于 并发数x迭代次数；\n" +
                    "2、参数个数要与sql中的?个数对应；\n" +
                    "3、参数文件中如有多个参数，一个参数一列，两列间用一个空格\" \"分隔；\n" +
                    "4、目前最多支持4个参数。\n" +
                    "===================================================================\n" +
                    "结果输出区域\n";
            LogUtil.clearLog(jTextArea_out,content);
        }else if(jButton_clearErr == e.getSource()){
            String content = "报错输出区域\n";
            LogUtil.clearLog(jTextArea_err,content);
        }else if(jButton_start == e.getSource()) {
//            redirectLog();
            LogUtil.redirectLog(jTextArea_out,jTextArea_err);
            if (QueryTest.jdbcDriver == null) { //判断是否已经配置数据库连接池
//                提示请配置数据库连接池
                JOptionPane.showMessageDialog(Gui.jFrame, "请配置数据库连接池", "提示", JOptionPane.WARNING_MESSAGE);
//                自动切换到数据库连接池配置页面
                jTabbedPane_jdbc.setSelectedIndex(1);
            } else {
                //        读取信息，存入变量
                //    声明获取的值
                String threadCountText = jTextField_threadCount.getText();
                String iteratorText = jTextField_iterator.getText();
                String paramNum = jTextField_paramNum.getText();
                String SQLText = jTextField_sql.getText();
                String paramFile = jTextField_paramFile.getText();
                if (threadCountText.equals("") || iteratorText.equals("") || SQLText.equals("")) {
                    JOptionPane.showMessageDialog(Gui.jFrame, "请输入并发数等相关参数", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    QueryTest.threadCount = Integer.parseInt(threadCountText);
                    QueryTest.iterator = Integer.parseInt(iteratorText);
                    QueryTest.paramNum = Integer.parseInt(paramNum);
                    QueryTest.SQL = SQLText;
                    QueryTest.paramFile = paramFile;
//                  执行数据库压力测试程序，进行测试
//                  如果接下来的业务处理耗时较长，那么jframe就会出现假死现象，所以要单独开辟线程来出来，不占用主线程
                    new Thread(new Runnable() {
                        public void run() {
                            QueryTest.pTest_jdbc();
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * 加载jdbc连接池配置
     */
    private void loadJdbcConfig(){

//        取到所有文本框中的内容，存到局部变量中
        String jdbcDriver = jTextField_driver.getText();
        String url = jTextField_url.getText();
        String user = jTextField_user.getText();
        String password = jTextField_passwd.getText();
        String initsize = jTextField_initsize.getText();
        String  maxtotal = jTextField_maxtotal.getText();
        String maxwait = jTextField_maxwait.getText();
        String maxidle = jTextField_maxidle.getText();
        String minidle = jTextField_minidle.getText();
//        判断是否有的文本框为空，如果有空的，提示填写；如果都填了，加载配置
        if(jdbcDriver.equals("") || url.equals("") || user.equals("") || password.equals("") || initsize.equals("")
                || maxtotal.equals("")  || maxwait.equals("") || maxidle.equals("") ||minidle.equals("") ){
            JOptionPane.showMessageDialog(Gui.jFrame,"请完整填写所有配置信息！","提示",JOptionPane.WARNING_MESSAGE);
        }else {
            System.out.println("开始加载数据库配置-----");
            QueryTest.jdbcDriver = jdbcDriver;
            QueryTest.url = url;
            QueryTest.user = user;
            QueryTest.password = password;
            QueryTest.initsize = Integer.parseInt(initsize);
            QueryTest.maxtotal = Integer.parseInt(maxtotal);
            QueryTest.maxwait = Long.parseLong(maxwait);
            QueryTest.maxidle = Integer.parseInt(maxidle);
            QueryTest.minidle = Integer.parseInt(minidle);
            JOptionPane.showMessageDialog(Gui.jFrame,"配置加载完成！");
            jTabbedPane_jdbc.setSelectedIndex(0);
            System.out.println(QueryTest.url);
        }
    }

}
