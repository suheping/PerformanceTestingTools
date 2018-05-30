package gui;

import server.http.HttpTest;
import util.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class HttpGui implements ActionListener {

    JTabbedPane jTabbedPane_http;
    private JComboBox<String> jComboBox_type;
    //    定义按钮
    private JButton jButton_start,jButton_clearOut, jButton_clearErr;
    //    定义文本框
    private JTextField jTextField_threadCount, jTextField_iterator, jTextField_url,jTextField_connectionTimeout;
    private JTextField jTextField_socketTimeout, jTextField_requestTimeout, jTextField_maxTotal, jTextField_maxPerRoute;

    //    定义文本域
    private JTextArea jTextArea_out, jTextArea_err;

    HttpGui(){
//        定义主面板
        jTabbedPane_http = new JTabbedPane(JTabbedPane.TOP);
//        定义第一个tab页 jSplitPane_get
//        tab1上的切分为四部分：参数区、按钮区、结果输出区、报错输出区
//        先上下拆分，top、bottom
//        再分别左右拆分，top：param、button    bottom：out、error
//        out和err区域需要添加滚动条，所以定义JScrollPane
        JSplitPane jSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jTabbedPane_http.addTab("HTTP",jSplitPane1);
        JSplitPane jSplitPane_top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane jSplitPane_bottom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane1.setTopComponent(jSplitPane_top);
        jSplitPane1.setBottomComponent(jSplitPane_bottom);
//        param、button区
        JPanel jPanel_param = new JPanel();
        JPanel jPanel_button = new JPanel();
        JScrollPane jScrollPane_out = new JScrollPane();
        JScrollPane jScrollPane_err = new JScrollPane();
        jSplitPane_top.setLeftComponent(jPanel_param);
        jSplitPane_top.setRightComponent(jPanel_button);
        jSplitPane_top.setResizeWeight(0.7);
//        out、err区
        jSplitPane_bottom.setLeftComponent(jScrollPane_out);
        jSplitPane_bottom.setRightComponent(jScrollPane_err);
        jSplitPane_bottom.setResizeWeight(0.5);
        jSplitPane_bottom.setOneTouchExpandable(true);

//        定义按钮区
        jButton_start = new JButton("执行");
        jButton_clearOut = new JButton("清空结果");
        jButton_clearErr = new JButton("清空报错");

//        定义参数区
        JLabel jLabel_threadcount = new JLabel("并发数：");
        jTextField_threadCount = new JTextField(5);
        JLabel jLabel_iterator = new JLabel("迭代次数：");
        jTextField_iterator = new JTextField(5);
        JLabel jLabel_type = new JLabel("请求类型：");
        jComboBox_type = new JComboBox<>();
        jComboBox_type.insertItemAt("GET",0);
        jComboBox_type.insertItemAt("POST",1);
        jComboBox_type.setSelectedIndex(0);
        JLabel jLabel_url = new JLabel("请求URL：");
        jTextField_url = new JTextField(50);
        JLabel jLabel_connectionTimeout = new JLabel("连接超时时间：");
        jTextField_connectionTimeout = new JTextField(5);
        JLabel jLabel_requestTimeout = new JLabel("请求超时时间");
        jTextField_requestTimeout = new JTextField(5);
        JLabel  jLabel_socketTimeout = new JLabel("socket超时时间：");
        jTextField_socketTimeout = new JTextField(5);
        JLabel jLabel_maxTotal = new JLabel("最大连接数");
        jTextField_maxTotal = new JTextField(5);
        JLabel jLabel_maxPerRoute = new JLabel("每个路由最大连接数");
        jTextField_maxPerRoute = new JTextField(5);

//        定义jTextArea_out、jTextArea_out
        jTextArea_out = new JTextArea();
        jTextArea_err = new JTextArea();
        jScrollPane_out.setViewportView(jTextArea_out);
        jScrollPane_err.setViewportView(jTextArea_err);
        jTextArea_out.setText("结果输出区域：\n");
        jTextArea_err.setText("报错输出区域：\n");

//        布局
//        按钮区布局   - 默认流式布局
        jPanel_button.add(jButton_start);
        jPanel_button.add(jButton_clearOut);
        jPanel_button.add(jButton_clearErr);

//        参数区布局
//        使用网格流式布局
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
        jPanel_param.add(jLabel_threadcount,gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        jPanel_param.add(jTextField_threadCount,gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        jPanel_param.add(jLabel_iterator,gbc);
        gbc.gridx = 4;
        gbc.gridy = 1;
        jPanel_param.add(jTextField_iterator,gbc);
        gbc.gridx = 5;
        gbc.gridy = 1;
        jPanel_param.add(jLabel_type,gbc);
        gbc.gridx = 6;
        gbc.gridy = 1;
        jPanel_param.add(jComboBox_type,gbc);
//        第二行
        gbc.gridx = 1;
        gbc.gridy = 2;
        jPanel_param.add(jLabel_url,gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        jPanel_param.add(jTextField_url,gbc);
//        第三行
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth =1;
        jPanel_param.add(jLabel_connectionTimeout,gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        jPanel_param.add(jTextField_connectionTimeout,gbc);
        gbc.gridx = 3;
        gbc.gridy = 3;
        jPanel_param.add(jLabel_requestTimeout,gbc);
        gbc.gridx = 4;
        gbc.gridy = 3;
        jPanel_param.add(jTextField_requestTimeout,gbc);
        gbc.gridx = 5;
        gbc.gridy = 3;
        jPanel_param.add(jLabel_socketTimeout,gbc);
        gbc.gridx = 6;
        gbc.gridy = 3;
        jPanel_param.add(jTextField_socketTimeout,gbc);
//        第四行
        gbc.gridx = 1;
        gbc.gridy = 4;
        jPanel_param.add(jLabel_maxTotal,gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;
        jPanel_param.add(jTextField_maxTotal,gbc);
        gbc.gridx = 3;
        gbc.gridy = 4;
        jPanel_param.add(jLabel_maxPerRoute,gbc);
        gbc.gridx = 4;
        gbc.gridy = 4;
        jPanel_param.add(jTextField_maxPerRoute,gbc);



        jButton_start.addActionListener(this);
        jButton_clearOut.addActionListener(this);
        jButton_clearErr.addActionListener(this);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(jButton_clearOut == e.getSource()){
            String content = "结果输出区域\n";
            LogUtil.clearLog(jTextArea_out,content);
        }else if(jButton_clearErr == e.getSource()){
            String content = "报错输出区域\n";
            LogUtil.clearLog(jTextArea_err,content);
        }else if(jButton_start == e.getSource()){
            LogUtil.redirectErrorLog(jTextArea_err);
//            读取文本框的参数
            String threadCount = jTextField_threadCount.getText();
            String iterator = jTextField_iterator.getText();
            String type = (String) jComboBox_type.getSelectedItem();
            String url = jTextField_url.getText();
            String connectionTimeout = jTextField_connectionTimeout.getText();
            String requestTimeout = jTextField_requestTimeout.getText();
            String socketTimeout = jTextField_socketTimeout.getText();
            String maxTotal = jTextField_maxTotal.getText();
            String maxPerRoute = jTextField_maxPerRoute.getText();
            assert type != null;
            if(threadCount.equals("")|| iterator.equals("")|| type.equals("")|| url.equals("")|| connectionTimeout.equals("")||
                    requestTimeout.equals("")|| socketTimeout.equals("")|| maxTotal.equals("")|| maxPerRoute.equals("")){
                JOptionPane.showMessageDialog(Gui.jFrame,"请完整填写并发数等参数！","提示",JOptionPane.WARNING_MESSAGE);
            }else {
                HttpTest.threadCount = Integer.parseInt(threadCount);
                HttpTest.iterator = Integer.parseInt(iterator);
                HttpTest.type = type;
                HttpTest.url = url;
                HttpTest.connectionTimeout = Integer.parseInt(connectionTimeout);
                HttpTest.requestTimeout = Integer.parseInt(requestTimeout);
                HttpTest.socketTimeout = Integer.parseInt(socketTimeout);
                HttpTest.maxTotal = Integer.parseInt(maxTotal);
                HttpTest.maxPerRoute = Integer.parseInt(maxPerRoute);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpTest.pTest_http();
                    }
                }).start();
            }
        }
    }



}
