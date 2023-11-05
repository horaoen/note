package type;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import base.JedisCommandsTestBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringTest extends JedisCommandsTestBase{
    @Test
    void getAndSetTest() {
        jedis.set("1", "hello");
        assertEquals( "hello", jedis.get("1"));
    }
    
    @Test
    void setnxTest() {
        jedis.set("2", "hello");
        jedis.setnx("2", "world");
        log.info(jedis.get("2"));
        assertEquals("hello", jedis.get("2"));
    }
    
    @Test
    void msetTest() {
        jedis.mset("key:1", "1", "key:2", "2");
        assertEquals(jedis.get("key:1"), "1");
        assertEquals(jedis.get("key:2"), "2");
        List<String> values = jedis.mget("key:1", "key:2");
        assertEquals(Arrays.asList("1", "2"), values);
    }
    
    @Test
    void stirngCounterTest() {
        jedis.set("counter1", "0");
        jedis.incr("counter1");
        assertEquals("1", jedis.get("counter1"));
        jedis.incrBy("counter1", 3);
        assertEquals(String.valueOf(4), jedis.get("counter1"));
    }
}
