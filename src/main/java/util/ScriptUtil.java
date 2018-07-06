package util;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScriptUtil {

    public void save(List<String> list) {

        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(null);
        if(option==JFileChooser.APPROVE_OPTION){	//假如用户选择了保存
            File file = chooser.getSelectedFile();
            try {
                FileOutputStream fos = new FileOutputStream(file);
                //写文件操作……
                for (String str :list){
                    fos.write(str.getBytes());
                    fos.write("\r\n".getBytes());
                }
                fos.close();

            } catch (IOException e) {
//                System.err.println("IO异常");
                e.printStackTrace();
            }
        }
    }

    public List<String> open() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        BufferedReader br =new BufferedReader(new FileReader(f));
        List<String> content = new ArrayList<>();
        while (true){
            String str = br.readLine();
            if (str==null){
                break;
            }
//            System.out.println(str);
            content.add(str);
        }
        br.close();
        return content;
    }
}
