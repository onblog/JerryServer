package com.github.onblog.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装HttpURLConnection
 * Create by Martin 2018/9/10/010 19:17
 */
public class Connection {
    private HttpURLConnection conn;
    private Charset charset = Charset.forName("UTF-8");
    private int readTimeout = 10000;
    private int connectTimeout = 10000;
    private String method = "GET";
    private boolean doInput = true;
    private Map<String, String> headers = null;
    private String data = null;

    public Connection(HttpURLConnection conn) {
        this.conn = conn;
    }

    /**
     * 设置请求URL
     */
    public static Connection connect(String url) throws IOException {
        Connection util = new Connection((HttpURLConnection) new URL(url).openConnection());
        return util;
    }

    /**
     * 设置读去超时时间/ms
     *
     * @param timeout
     * @return
     */
    public Connection setReadTimeout(int timeout) {
        this.readTimeout = timeout;
        return this;
    }

    /**
     * 设置链接超时时间/ms
     *
     * @param timeout
     * @return
     */
    public Connection setConnectTimeout(int timeout) {
        this.connectTimeout = timeout;
        return this;
    }

    /**
     * 设置请求方式
     *
     * @param method
     * @return
     * @throws ProtocolException
     */
    public Connection setRequestMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * 添加Headers
     *
     * @param map
     * @return
     */
    public Connection setHeaders(Map<String, String> map) {
        String cookie = "Cookie";
        if(map.containsKey(cookie)){
            headers = new HashMap<>();
            headers.put(cookie,map.get(cookie));
        }
        return this;
    }

    /**
     * 是否接受输入流
     * 默认true
     *
     * @param is
     * @return
     */
    public Connection setDoInput(boolean is) {
        this.doInput = is;
        return this;
    }

    /**
     * 设置请求响应的编码
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * 写入数据,接受Map<String,String>或String类型<br>
     * 例如POST时的参数<br>
     * demo=1&name=2
     *
     * @return
     */
    public Connection setData(Object object) {
        if (object == null) {
            return this;
        } else if (object instanceof String) {
            this.data = (String) object;
        } else if (object instanceof Map) {
            Map map = (Map) object;
            StringBuilder builder = new StringBuilder();
            for (Object key : map.keySet()) {
                builder.append(key + "=" + map.get(key) + "&");
            }
            this.data = builder.toString().substring(0, builder.length() > 0 ? builder.length() - 1 : builder.length());
        }
        return this;
    }

    /**
     * 发起请求
     */
    public HttpURLConnection execute() throws IOException {
        //添加请求头
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        //设置读去超时时间为10秒
        conn.setReadTimeout(readTimeout);
        //设置链接超时为10秒
        conn.setConnectTimeout(connectTimeout);
        //设置请求方式,GET,POST
        conn.setRequestMethod(method.toUpperCase());
        //接受输入流
        conn.setDoInput(doInput);
        //写入参数
        if (data != null && !method.equalsIgnoreCase("GET")) {
            //启动输出流，当需要传递参数时需要开启
            conn.setDoOutput(true);
            //添加请求参数，注意：如果是GET请求，参数要写在URL中
            OutputStream output = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, charset));
            //写入参数 用&分割。
            writer.write(data);
            writer.flush();
            writer.close();
        }
        //发起请求
        conn.connect();
        return conn;
    }


    /**
     * 辅助可扩展工具类
     */
    public static class ConnectionUtil {
        /**
         * 获取响应字符串
         *
         * @param conn
         * @return
         */
        public static String getBody(HttpURLConnection conn, Charset charset) {
            try {
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
                String line = bufferedReader.readLine();
                StringBuilder builder = new StringBuilder();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
