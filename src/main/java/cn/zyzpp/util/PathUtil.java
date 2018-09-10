package cn.zyzpp.util;

import java.io.File;

/**
 * 适用于Linux和Windows系统,根目录为jar包上一层目录
 * <br>
 * Create by yster@foxmail.com 2018/9/3/003 19:06
 */
public class PathUtil {
    //获取项目的上一层目录
    public final static String projectPath;

    static {
        //打包为jar时的路径
//        projectPath =  System.getProperty("user.dir") + File.separator;
        //直接运行main方法的路径
        projectPath = System.getProperty("user.dir").substring(0,System.getProperty("user.dir").lastIndexOf(File.separator))+File.separator+"JerryServer"+File.separator;
    }

    /**
     * 项目上一层目录
     */
    public static String getRootPath() {
        return RootPath("");
    }

    /**
     * 自定义追加路径
     */
    public static String getRootPath(String u_path) {
        return RootPath(u_path);
    }

    /**
     * 私有处理方法
     */
    private static String RootPath(String u_path) {
        String rootPath = "";
        //windows下
        if ("\\".equals(File.separator)) {
            rootPath = projectPath + u_path;
            rootPath = rootPath.replaceAll("/", "\\\\");
            if (rootPath.substring(0, 1).equals("\\")) {
                rootPath = rootPath.substring(1);
            }
        }
        //linux下
        if ("/".equals(File.separator)) {
            rootPath = projectPath + u_path;
            rootPath = rootPath.replaceAll("\\\\", "/");
        }
        return rootPath;
    }

}
