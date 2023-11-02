import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DemoTest extends JedisCommandsTestBase{
    @Test
    void addition() {
        jedis.set("1", "hello");
        assertEquals( "hello", jedis.get("1"));
    }
}
