package gui;

import server.http.HttpTest;
import util.LogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class HttpGui implements ActionListener {

//    http测试工具最底层面板
    JTabbedPane jTabbedPane_http, jTabbedPane_body;
//    定义请求类型
    private JComboBox<String> jComboBox_type;
    //    定义按钮
    private JButton jButton_start,jButton_clearOut, jButton_clearErr;
    private JButton jButton_add_body_form, jButton_del_body_form, jButton_add_header_form, jButton_del_header_form;
    //    定义文本框
    private JTextField jTextField_threadCount, jTextField_iterator, jTextField_url,jTextField_connectionTimeout;
    private JTextField jTextField_socketTimeout, jTextField_requestTimeout, jTextField_maxTotal, jTextField_maxPerRoute;
    private DefaultTableModel tableModel_header_form ,tableModel_body_form, tableModel_assert;

    //    定义文本域
    public static JTextArea jTextArea_out, jTextArea_err, jTextArea_json, jTextArea_text;
    public static JButton jButton_assert_add ,jButton_assert_del;
    public static JRadioButton jRadioButton_and, jRadioButton_or;


    HttpGui(){
//        定义主面板
        jTabbedPane_http = new JTabbedPane(JTabbedPane.TOP);
//        定义第一个tab页 jSplitPane_get
//        tab1上的切分为四部分：参数区、按钮区（按钮区、断言区）、结果输出区、报错输出区
//        先上下拆分，top、bottom
//        再分别左右拆分，top：param、button    bottom：out、error
//        out和err区域需要添加滚动条，所以定义JScrollPane
        JSplitPane jSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jTabbedPane_http.addTab("HTTP",jSplitPane1);
        JSplitPane jSplitPane_top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane jSplitPane_bottom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane1.setTopComponent(jSplitPane_top);
        jSplitPane1.setBottomComponent(jSplitPane_bottom);

//        param区使用split面板，
//      上部为普通配置区-普通面板，
//      下部为http请求参数区，包含两部分 请求头和body --split面板
//      请求头： 表单以及按钮--split面板（jscroll和jpanel）
//        body： 分为三个tab页：一个form表单，一个json，一个text
//        form表单：分为上下两部分：表单以及按钮 -- split面板（jscroll和jpanel）
//        json：jscroll，带jtextarea
        JSplitPane jSplitPane_param = new JSplitPane(JSplitPane.VERTICAL_SPLIT); //param区
        JPanel jPanel_normal_param = new JPanel(); //上部普通参数区
        JSplitPane jSplitPane_http = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); //下部http请求参数区
        JSplitPane jSplitPane_header = new JSplitPane(JSplitPane.VERTICAL_SPLIT); //请求头区
        JScrollPane jScrollPane_header_param = new JScrollPane(); //请求头参数区
        JPanel jPanel_header_button = new JPanel();  //请求头按钮区
        jTabbedPane_body = new JTabbedPane(JTabbedPane.TOP); //请求body区
        JSplitPane jSplitPane_body_form = new JSplitPane(JSplitPane.VERTICAL_SPLIT); //body_form区
        JScrollPane jScrollPane_body_form_param = new JScrollPane(); // body_form 参数区
        JPanel jPanel_body_form_button = new JPanel(); //body_form 按钮区
        JScrollPane jScrollPane_json = new JScrollPane(); //body_json区
        JScrollPane jScrollPane_text = new JScrollPane(); // body_text区
        jTextArea_json = new JTextArea(); //body_json文本框
        jScrollPane_json.setViewportView(jTextArea_json); //将json文本框放到body_json区上
        jTextArea_text = new JTextArea(); //body_text文本框
        jScrollPane_text.setViewportView(jTextArea_text); //将body_text文本框放到body_text区上
        jSplitPane_body_form.setTopComponent(jScrollPane_body_form_param); //将body_form参数区放到body_form区上部
        jSplitPane_body_form.setBottomComponent(jPanel_body_form_button); //将body_form按钮区放到body_form区下部
        jSplitPane_body_form.setResizeWeight(0.99);
        jTabbedPane_body.addTab("form表单",jSplitPane_body_form);//body参数区添加标签页 body_form
        jTabbedPane_body.addTab("json",jScrollPane_json);//body参数区添加标签页 body_json
        jTabbedPane_body.addTab("text",jScrollPane_text); //body参数区添加标签页 body_text
        jSplitPane_header.setTopComponent(jScrollPane_header_param); //将请求头参数区放到请求头区上部
        jSplitPane_header.setBottomComponent(jPanel_header_button); //将请求头按钮区放到请求头下部
        jSplitPane_header.setResizeWeight(0.99);
        jSplitPane_param.setTopComponent(jPanel_normal_param); //将普通参数区放到param区上部
        jSplitPane_param.setBottomComponent(jSplitPane_http); //将http请求区放到param区下部
        jSplitPane_param.setResizeWeight(0.01);
        jSplitPane_http.setLeftComponent(jSplitPane_header); //将请求头区放到http请求区左侧
        jSplitPane_http.setRightComponent(jTabbedPane_body); //将请求body区放到http请求区右侧
        jSplitPane_http.setResizeWeight(0.5);

//        按钮区+断言区 -- splitpanel
        JSplitPane jSplitPane_button_assert = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel jPanel_button = new JPanel(); // 按钮区使用普通面板
        JPanel jPanel_assert = new JPanel(); // 断言区底层使用普通面板
        jSplitPane_button_assert.setTopComponent(jPanel_button);
        jSplitPane_button_assert.setBottomComponent(jPanel_assert);
        jSplitPane_button_assert.setResizeWeight(0.01);
//        两个日志输出区域使用jscroll面板
        JScrollPane jScrollPane_out = new JScrollPane();
        JScrollPane jScrollPane_err = new JScrollPane();
//        将参数区和按钮区+断言区加到上部面板中
        jSplitPane_top.setLeftComponent(jSplitPane_param);
        jSplitPane_top.setRightComponent(jSplitPane_button_assert);
        jSplitPane_top.setResizeWeight(0.9);

//        out、err区
        jSplitPane_bottom.setLeftComponent(jScrollPane_out);
        jSplitPane_bottom.setRightComponent(jScrollPane_err);
        jSplitPane_bottom.setResizeWeight(0.5);
        jSplitPane_bottom.setOneTouchExpandable(true);

//        定义按钮区
        jButton_start = new JButton("执行");
        jButton_clearOut = new JButton("清空结果");
        jButton_clearErr = new JButton("清空报错");

//        断言区
        jPanel_assert.setLayout(new BorderLayout());
        //        单选按钮区
        JPanel jPanel_north = new JPanel();
        jRadioButton_and = new JRadioButton("and");
        jRadioButton_or = new JRadioButton("or",true);
        jPanel_north.add(jRadioButton_and);
        jPanel_north.add(jRadioButton_or);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButton_and);
        buttonGroup.add(jRadioButton_or);
        jPanel_assert.add(jPanel_north,BorderLayout.NORTH);

//        断言列表区
        JTable jTable = new JTable(0,1);
        tableModel_assert = (DefaultTableModel) jTable.getModel();
//        设置数据
        Vector data = new Vector();
//        设置表头
        Vector names = new Vector();
        names.add("断言内容");
//        set
        tableModel_assert.setDataVector(data,names);
//        设置不自动调整宽度
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        设置每列的宽度
        jTable.getColumnModel().getColumn(0).setPreferredWidth(200);
//        jPanel.add(jTable);
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jPanel_assert.add(jScrollPane,BorderLayout.CENTER);

//        按钮区
        JPanel jPanel_south = new JPanel();
        jButton_assert_add = new JButton("添加");
        jButton_assert_del = new JButton("删除");
        jPanel_south.add(jButton_assert_add);
        jPanel_south.add(jButton_assert_del);
        jPanel_assert.add(jPanel_south,BorderLayout.SOUTH);

//        定义参数区
//        上部普通参数
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
        jTextField_url = new JTextField(500);
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
//        下部请求参数
//        实例化jtable_header_form
        JTable jTable_header_form = new JTable(0,1);
        tableModel_header_form = (DefaultTableModel) jTable_header_form.getModel();
//        设置数据
        Vector data_header = new Vector();
//        设置表头
        Vector names_header = new Vector();
        names_header.add("字段名");
        names_header.add("字段值");
//        set
        tableModel_header_form.setDataVector(data_header,names_header);
//        设置每列的宽度
        jTable_header_form.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable_header_form.getColumnModel().getColumn(1).setPreferredWidth(300);
//        设置不自动调整宽度
        jTable_header_form.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane_header_param.setViewportView(jTable_header_form);
//        header参数按钮区
        jButton_add_header_form = new JButton("添加一行");
        jButton_del_header_form = new JButton("删除最后一行");
        JLabel jLabel_header = new JLabel("请求头");
        jPanel_header_button.add(jLabel_header);
        jPanel_header_button.add(jButton_add_header_form);
        jPanel_header_button.add(jButton_del_header_form);

//        实例化jtable_body_form
        //    定义参数表
        JTable jTable_body_form = new JTable(0, 1);
        tableModel_body_form = (DefaultTableModel) jTable_body_form.getModel();
//        设置数据
        Vector data_body = new Vector();
//        设置表头
        Vector names_body = new Vector();
        names_body.add("字段名");
        names_body.add("字段值");
//        set
        tableModel_body_form.setDataVector(data_body,names_body);
//        设置每列的宽度
        jTable_body_form.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable_body_form.getColumnModel().getColumn(1).setPreferredWidth(300);
//        设置不自动调整宽度
        jTable_body_form.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane_body_form_param.setViewportView(jTable_body_form);
//        http_form参数区按钮
        jButton_add_body_form = new JButton("添加一行");
        jButton_del_body_form = new JButton("删除最后一行");
        JLabel jLabel_body = new JLabel("请求体");
        jPanel_body_form_button.add(jLabel_body);
        jPanel_body_form_button.add(jButton_add_body_form);
        jPanel_body_form_button.add(jButton_del_body_form);


//        定义jTextArea_out、jTextArea_out
        jTextArea_out = new JTextArea();
        jTextArea_err = new JTextArea();
        jScrollPane_out.setViewportView(jTextArea_out);
        jScrollPane_err.setViewportView(jTextArea_err);
        jTextArea_out.setText("结果输出区域：\n");
        jTextArea_err.setText("报错输出区域：\n");

//        布局
//        按钮区布局   - 网格流式布局
        GridBagLayout gridBagLayout = new GridBagLayout();
        jPanel_button.setLayout(gridBagLayout);
        GridBagConstraints gbc = new GridBagConstraints();

//        填充
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        居左
        gbc.anchor = GridBagConstraints.WEST;
//        组件位置
//          第一行：
        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel_button.add(jButton_start,gbc);
//        第二行
        gbc.gridx = 1;
        gbc.gridy = 2;
        jPanel_button.add(jButton_clearOut,gbc);
//        第三行
        gbc.gridx = 1;
        gbc.gridy = 3;
        jPanel_button.add(jButton_clearErr,gbc);

//        参数区布局
//        使用网格流式布局
        jPanel_normal_param.setLayout(gridBagLayout);
//        多个组件均分区域
        gbc.weightx =1;
        gbc.weighty =1;
//        组件位置
//          第一行：
        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel_normal_param.add(jLabel_threadcount,gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        jPanel_normal_param.add(jTextField_threadCount,gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        jPanel_normal_param.add(jLabel_iterator,gbc);
        gbc.gridx = 4;
        gbc.gridy = 1;
        jPanel_normal_param.add(jTextField_iterator,gbc);
        gbc.gridx = 5;
        gbc.gridy = 1;
        jPanel_normal_param.add(jLabel_type,gbc);
        gbc.gridx = 6;
        gbc.gridy = 1;
        jPanel_normal_param.add(jComboBox_type,gbc);
//        第二行
        gbc.gridx = 1;
        gbc.gridy = 2;
        jPanel_normal_param.add(jLabel_url,gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        jPanel_normal_param.add(jTextField_url,gbc);
//        第三行
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth =1;
        jPanel_normal_param.add(jLabel_connectionTimeout,gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        jPanel_normal_param.add(jTextField_connectionTimeout,gbc);
        gbc.gridx = 3;
        gbc.gridy = 3;
        jPanel_normal_param.add(jLabel_requestTimeout,gbc);
        gbc.gridx = 4;
        gbc.gridy = 3;
        jPanel_normal_param.add(jTextField_requestTimeout,gbc);
        gbc.gridx = 5;
        gbc.gridy = 3;
        jPanel_normal_param.add(jLabel_socketTimeout,gbc);
        gbc.gridx = 6;
        gbc.gridy = 3;
        jPanel_normal_param.add(jTextField_socketTimeout,gbc);
//        第四行
        gbc.gridx = 1;
        gbc.gridy = 4;
        jPanel_normal_param.add(jLabel_maxTotal,gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;
        jPanel_normal_param.add(jTextField_maxTotal,gbc);
        gbc.gridx = 3;
        gbc.gridy = 4;
        jPanel_normal_param.add(jLabel_maxPerRoute,gbc);
        gbc.gridx = 4;
        gbc.gridy = 4;
        jPanel_normal_param.add(jTextField_maxPerRoute,gbc);


        jButton_start.addActionListener(this);
        jButton_clearOut.addActionListener(this);
        jButton_clearErr.addActionListener(this);
        jButton_add_body_form.addActionListener(this);
        jButton_del_body_form.addActionListener(this);
        jButton_add_header_form.addActionListener(this);
        jButton_del_header_form.addActionListener(this);
        jRadioButton_and.addActionListener(this);
        jRadioButton_or.addActionListener(this);
        jButton_assert_add.addActionListener(this);
        jButton_assert_del.addActionListener(this);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(jButton_clearOut == e.getSource()){
            String content = "结果输出区域\n";
            LogUtil.clearLog(jTextArea_out,content);
        }else if(jButton_clearErr == e.getSource()){
            String content = "报错输出区域\n";
            LogUtil.clearLog(jTextArea_err,content);
        }else if(jButton_add_body_form == e.getSource()){
//            body_form添加一行参数
            tableModel_body_form.addRow(new Object[]{"",""});
        }else if (jButton_del_body_form == e.getSource()){
//            body_form删除最后一行参数
            if(tableModel_body_form.getRowCount()>=1){
                tableModel_body_form.removeRow(tableModel_body_form.getRowCount()-1);
            }
        }else if(jButton_add_header_form == e.getSource()){
//            header_form添加一行
            tableModel_header_form.addRow(new Object[]{"",""});
        }else if(jButton_del_header_form == e.getSource()){
//            header_form删除最后一行
            if(tableModel_header_form.getRowCount()>=1){
                tableModel_header_form.removeRow(tableModel_header_form.getRowCount()-1);
            }
        }else if(jButton_assert_add == e.getSource()){
//            assert添加一行
            tableModel_assert.addRow(new Object[]{"",""});
        }else if(jButton_assert_del == e.getSource()){
//            assert删除最后一行
            if (tableModel_assert.getRowCount() >=1){
                tableModel_assert.removeRow(tableModel_assert.getRowCount()-1);
            }
        }else if (jRadioButton_and == e.getSource()){
//            选中了and，修改assert_type的值
            HttpTest.assert_type = "and";
        }else if (jRadioButton_or == e.getSource()){
//            选中了or，修改assert_type的值
            HttpTest.assert_type = "or";
        } else if(jButton_start == e.getSource()){
            LogUtil.redirectErrorLog(jTextArea_err);
//            读取基础配置参数
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
//                读取http_header请求参数
                int rowCount_header = tableModel_header_form.getRowCount();
                for(int i=0; i<rowCount_header; i++){
                    Object key = tableModel_header_form.getValueAt(i,0);
                    Object value = tableModel_header_form.getValueAt(i,1);
                    if(key !=""){
                        HttpTest.map_header.put(key.toString(),value.toString());
                    }
                }
//                读取断言列表的参数
                int rowCount_assert = tableModel_assert.getRowCount();
                for (int i=0; i<rowCount_assert; i++){
                    Object value = tableModel_assert.getValueAt(i,0);
                    if(value !=""){
                        HttpTest.list_assert.add(value.toString());
                    }
                }
//                    判断请求body为哪种类型
                if (jTabbedPane_body.getSelectedIndex() == 0){
//                    当前选中body_form
                    HttpTest.param_type = "form";
//                    读取http_body_form请求参数
                    int rowCount_body = tableModel_body_form.getRowCount();
                    for(int i=0; i<rowCount_body; i++){
                        Object key = tableModel_body_form.getValueAt(i,0);
                        System.out.println(key.toString());
                        Object value = tableModel_body_form.getValueAt(i,1);
                        System.out.println(value.toString());
                        if(key !=""){
                            HttpTest.map_body.put(key.toString(),value.toString());
                        }
                    }
                }else if (jTabbedPane_body.getSelectedIndex() == 1){
//                    当前选中body_json
                    HttpTest.param_type="json";
                    HttpTest.body_json = jTextArea_json.getText();
                }else if (jTabbedPane_body.getSelectedIndex() == 2){
//                    当前选中body_text
                    HttpTest.param_type="text";
                    HttpTest.body_text = jTextArea_text.getText();
                }


//                新起线程转向后台服务
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
