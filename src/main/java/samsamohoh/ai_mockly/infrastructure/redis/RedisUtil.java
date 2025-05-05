package samsamohoh.ai_mockly.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(key);
    }

    public boolean existData(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public void setData(String key, String value) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        operations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
