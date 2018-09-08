package cn.zyzpp;

import cn.zyzpp.entity.EntityInter;
import com.alibaba.fastjson.JSON;
import net.sf.json.util.JSONTokener;
import org.junit.Test;

import java.io.IOException;
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

    }

    @Test
    public void test2() throws IOException{
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
