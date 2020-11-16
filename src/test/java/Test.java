import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class Test {


    public static void main(String[] args) {
        RedisTemplate<Object, Object> objectObjectRedisTemplate = new RedisTemplate<>();
        objectObjectRedisTemplate.opsForList().leftPush("sds","sds2222d");

    }
}
