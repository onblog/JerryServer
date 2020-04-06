package cn.zyzpp.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by yster@foxmail.com 2018-05-05
**/
public class StringUtil {
	/**
	 * 获取指定字符串出现的次数
	 * 
	 * @param srcText 源字符串
	 * @param findText 要查找的字符串
	 * @return
	 */
	public static int appearNumber(String srcText, String findText) {
	    int count = 0;
	    Pattern p = Pattern.compile(findText);
	    Matcher m = p.matcher(srcText);
	    while (m.find()) {
	        count++;
	    }
	    return count;
	}

    /**
     * 根据系统分隔符格式化路径
     * @return
     */
	public static String divideStr(String path){
	    String str = null;
        if ("\\".equals(File.separator)) {
            str  = path.replaceAll("/","\\\\");
        }
        if ("/".equals(File.separator)) {
            str = path.replaceAll("\\\\","/");
        }
        return str;
    }
	/**
	 * 分隔符转为/，也就是http形式
	 * @return
	 */
	public static String httpStr(String path){
		String str = null;
		if (path!=null) {
			str  = path.replaceAll("\\\\","/");
		}
		return str;
	}

}
