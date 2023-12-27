package base;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public abstract class JedisCommandsTestBase {

  protected Jedis jedis;
  protected JedisPooled client;

  public JedisCommandsTestBase() {
    super();
  }
  @BeforeAll
  public void setUp() throws Exception {
    jedis = new Jedis("redis://default:qSOzP3a4Sh1v89oaHGQ1nVMLOXpu1Le2@redis-15066.c44.us-east-1-2.ec2.cloud.redislabs.com:15066");

    client = new JedisPooled("redis://default:qSOzP3a4Sh1v89oaHGQ1nVMLOXpu1Le2@redis-15066.c44.us-east-1-2.ec2.cloud.redislabs.com:15066");

    log.info("jedis setup");
    jedis.flushAll();

  }
  @AfterAll
  public void tearDown() throws Exception {
    jedis.close();
    log.info("jedis close");
  }
}
