import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import redis.clients.jedis.Jedis;

public abstract class JedisCommandsTestBase {

  protected Jedis jedis;

  public JedisCommandsTestBase() {
    super();
  }

  @BeforeEach
  public void setUp() throws Exception {
    jedis = new Jedis(
        "redis://default:qSOzP3a4Sh1v89oaHGQ1nVMLOXpu1Le2@redis-15066.c44.us-east-1-2.ec2.cloud.redislabs.com:15066");
  }

  @AfterEach
  public void tearDown() throws Exception {
    jedis.close();
  }
}
