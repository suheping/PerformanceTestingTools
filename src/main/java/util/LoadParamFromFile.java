package util;

import java.io.*;
import java.util.ArrayList;

public class LoadParamFromFile {

    /**
     * 读取参数文件，存到String[]数组中
     * @param file 参数文件的绝对路径
     * @return 参数数组 String[]
     */
    public static String[] fileToList(String file){
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for(String line = br.readLine(); line !=null; line=br.readLine()){
                arrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = arrayList.size();
        return arrayList.toArray(new String[size]);
    }
}
