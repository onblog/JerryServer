package cn.zyzpp;

import cn.zyzpp.entity.EntityInter;
import com.alibaba.fastjson.JSON;
import net.sf.json.util.JSONTokener;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test() {
        String pathname = "D:\\IDEA\\workSpace\\JerryServer\\picture\\1532688969.png";//JDK优秀
//        String pathname = "D:\\IDEA\\workSpace\\JerryServer\\webapps\\ROOT\\page.json";
        String type = new MimetypesFileTypeMap().getContentType(new File(pathname));
        System.out.println("第二种javax.activation: " + type);

        try {
            String s = Files.probeContentType(new File(pathname).toPath());
            System.out.println("第三种java.nio: " + s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(pathname);
        System.out.println("第四种java.net: " + contentType);
    }

    @Test
    public void test2() throws IOException{
//        Connection.Response response = Jsoup.connect("https://www.kuaidi100.com/query?type=shentong&postid=3374107234608&id=1")
//                .method(Connection.Method.GET)
//                .execute();
        List<Object> list = new ArrayList<>();
        list.add(new EntityInter("123",1));
        list.add(new EntityInter("123",1));
        Map<String,List> map = new HashMap<>();
        map.put("list",list);

        String string = JSON.toJSONString(map);
        Object json = new JSONTokener(string).nextValue();
        if(json instanceof net.sf.json.JSONObject){
            Map<String,Object> object = (Map<String, Object>) json;
            System.out.println(object);
        }else if (json instanceof net.sf.json.JSONArray){
            List<Object> objects = (List<Object>) json;
            System.out.println(objects);
        }
    }

}
