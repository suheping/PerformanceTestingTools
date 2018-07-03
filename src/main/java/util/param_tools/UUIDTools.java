package util.param_tools;

import java.util.UUID;

/**
 * 实现替换字符串中的${uuid}为 UUID（去掉-）
 */
public class UUIDTools {

    public String replaceUUID(String str){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        if (str.contains("${uuid}")){ //如果包含${uuid}，返回替换后的字符串
            return str.replace("${uuid}", uuid);
        }else { //如果不包含，返回原字符串
            return str;
        }
    }
}
