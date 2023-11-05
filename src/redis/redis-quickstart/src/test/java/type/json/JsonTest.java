package type.json;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson2.JSON;

import base.JedisCommandsTestBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonTest extends JedisCommandsTestBase {
    @Test
    void test() {
        Student maya = new Student("Maya", "Jayavant");
        client.jsonSet("student:1", JSON.toJSONString(maya));
        log.info(client.jsonGet("student:1").toString());
    }   
}
