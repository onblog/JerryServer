package cn.zyzpp.template;

import cn.zyzpp.config.HttpServerConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Create by yster@foxmail.com 2018/9/7/007 18:54
 */
public class FreeMark {

    public static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

    static {
        try {
            //设置FreeMarker的模版文件夹位置
            cfg.setDirectoryForTemplateLoading(new File(HttpServerConfig.WEB_ROOT));
            cfg.setDefaultEncoding(HttpServerConfig.fm_charset.toString());
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] resolve(Map<String, Object> map, String path) {
        try {
            //创建模版对象
            Template t = cfg.getTemplate(path);
            //在模版上执行插值操作，并输出到制定的输出流中
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream,HttpServerConfig.fm_charset);
            t.process(map, outputStreamWriter);
            //关闭流
            outputStreamWriter.close();
            //返回字节数组
            return byteArrayOutputStream.toByteArray();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
