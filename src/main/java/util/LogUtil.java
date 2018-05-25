package util;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class LogUtil extends OutputStream {
    private final JTextArea jTextArea;

    /**
     * 重定向标准输出到JtextArea
     * @param jTextArea 重定向到的JtextArea对象
     */
    private LogUtil(JTextArea jTextArea){
        if(jTextArea == null){
            throw new IllegalArgumentException("jTextArea is null!");
        }
        this.jTextArea = jTextArea;
    }

    /**
     * 重写write方法
     * @param b 要写入的内容
     */
    public void write(int b){
        write (new byte [] {(byte)b}, 0, 1);
    }

    /**
     * 重写write方法
     * @param b b
     * @param offset offset
     * @param length length
     */
    public void write(byte[] b, int offset, int length) {
        final String text = new String (b,offset,length);
        SwingUtilities.invokeLater(new Runnable ()
        {
            public void run()
            {
                jTextArea.append (text);
            }
        });
    }

    /**
     * 重定向日志输出到jTextArea_out、jTextArea_err
     * 并把光标定位到最后一行
     */
    public static void redirectLog(JTextArea jTextArea_out,JTextArea jTextArea_err){
        //        日志输出重定向
        LogUtil infoLogUtil = new LogUtil(jTextArea_out);
        LogUtil errLogUtil = new LogUtil(jTextArea_err);
        System.setOut(new PrintStream(infoLogUtil));
        System.setErr(new PrintStream(errLogUtil));
        jTextArea_out.setCaretPosition(jTextArea_out.getDocument().getLength());
        jTextArea_err.setCaretPosition(jTextArea_err.getDocument().getLength());
    }

    /**
     * 清理日志输出区域jTextArea
     * @param jTextArea 目标控件--输出区域
     * @param string 输入内容
     */
    public static void clearLog(JTextArea jTextArea,String string){
        jTextArea.setText(string);
    }
}
