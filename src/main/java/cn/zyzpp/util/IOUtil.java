package cn.zyzpp.util;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Create by yster@foxmail.com 2018-05-04
 **/
public class IOUtil {
    /**
     * 读取文本文件
     *
     * @param path
     * @return 文件的字符串内容 或null
     */
    public static String readFile(String path, Charset charset) {
        try {
            // File对象  
            File filename = new File(path);
            if (!filename.exists()) {
                return null;
            }
            // 文件字节输入流对象  
            FileInputStream fileInputStream = new FileInputStream(filename);
            // 字节转字符输入流对象 InputStreamReader：字节流到字符流的桥梁  
            InputStreamReader reader = new InputStreamReader(fileInputStream, charset);
            // BufferedReader(字符缓冲输入流)，提供通用的缓冲方式文本读取  
            BufferedReader br = new BufferedReader(reader);
            // 多线程StringBuffer 单线程StringBuilder  
            StringBuffer txt = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                txt.append(line);
            }
            br.close();
            return txt.toString();
        } catch (Exception e) {
            // e.printStackTrace();  
            return null;
        }
    }

    /**
     * 读取为字节流文件
     *
     * @param path
     * @return 文件的字符串内容
     */
    public static byte[] readFileToByte(String path) {
        try {
            // File对象  
            File filename = new File(path);
            if (!filename.exists()) {
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
        File filename = new File(path);
        if (!filename.isFile()) {
            return false;
        }
        return true;
    }

    public static boolean isDirectory(String path) {
        File filename = new File(path);
        if (filename.isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * 写入文件到磁盘
     * @param text
     * @param path
     */
    public static void write(String text, String path) {
        try {
            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream,"UTF-8"));
            bufferedWriter.write(text);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
