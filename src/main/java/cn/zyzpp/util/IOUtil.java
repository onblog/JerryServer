package cn.zyzpp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import cn.zyzpp.config.HttpServerConfig;

/**
 * Create by yster@foxmail.com 2018-05-04
**/
public class IOUtil {
	/** 
     *  读取文本文件 
     * @param path 
     * @return 文件的字符串内容 
     */  
    public static String readFile(String path) {  
    	path = checkout(path);
        try {  
            String pathname = path;  
            // File对象  
            File filename = new File(pathname);  
            if(!filename.exists()) {
            	return null;
            }
            // 文件字节输入流对象  
            FileInputStream fileInputStream = new FileInputStream(filename);  
            // 字节转字符输入流对象 InputStreamReader：字节流到字符流的桥梁  
            InputStreamReader reader = new InputStreamReader(fileInputStream, "utf-8");  
            // BufferedReader(字符缓冲输入流)，提供通用的缓冲方式文本读取  
            BufferedReader br = new BufferedReader(reader);  
            // 多线程StringBuffer 单线程StringBuilder  
            StringBuffer txt = new StringBuffer();  
            String line = "";  
            while ((line = br.readLine()) != null) {  
                txt.append(line);  
            }  
            // 方法一：流的关闭：先打开的后关闭，后打开的先关闭  
            // 方法二：可以只调用外层流的close方法关闭其装饰的内层流  
            br.close(); 
            return txt.toString();  
        } catch (Exception e) {  
            // e.printStackTrace();  
            return null;  
        }  
    }  
    
	/** 
     *  读取为字节流文件
     * @param path 
     * @return 文件的字符串内容 
     */  
	public static byte[] readFileToByte(String path) { 
    	path = checkout(path);
        try {   
            // File对象  
            File filename = new File(path);  
            if(!filename.exists()) {
            	return null;
            }
            // 文件字节输入流对象  
            FileInputStream fileInputStream = new FileInputStream(filename);  
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            fileInputStream.close();
            return b;  
        } catch (Exception e) {  
            // e.printStackTrace();  
            return null;  
        }  
    }  
    
    public static boolean isFile(String path) {
    	path = checkout(path);
        File filename = new File(path);  
        if(!filename.isFile()) {
        	return false;
        }
        return true;
    }

    public static boolean isDirectory(String path) {
    	path = checkout(path);
        File filename = new File(path);  
        if(!filename.isDirectory()) {
        	return false;
        }
        return true;
    }
    
    /**
     * 校验Uri
     * @param path
     * @return
     */
    private static String checkout(String path) {
    	if(!HttpServerConfig.NO_FOUND.equals(path)) {
        	path = HttpServerConfig.WEB_ROOT + path; 
    	}
    	return path;
    }
    
}
