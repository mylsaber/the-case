package com.mylsaber.redis.lettuce.controller;

import com.mylsaber.redis.lettuce.domain.ResponseResult;
import com.mylsaber.redis.lettuce.dto.User;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author jiangdi
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    // 注意：这里@Autowired是报错的，因为@Autowired按照类名注入的
    @Resource
    private RedisTemplate<String, User> redisTemplate;

    @PostMapping("add")
    public ResponseResult<User> add(@RequestBody User user) {
        redisTemplate.opsForValue().set(String.valueOf(user.getId()), user);
        return ResponseResult.success(redisTemplate.opsForValue().get(String.valueOf(user.getId())));
    }

    @GetMapping("find/{userId}")
    public ResponseResult<User> find(@PathVariable("userId") String userId) {
        return ResponseResult.success(redisTemplate.opsForValue().get(userId));
    }

    @GetMapping("sync")
    public ResponseResult<String> sync() {
        try (RedisClient client = RedisClient.create("redis://127.0.0.1")) {
            StatefulRedisConnection<String, String> connection = client.connect();
            RedisStringCommands<String, String> sync = connection.sync();
            String value = sync.get("key");
            return ResponseResult.success(value);
        } catch (Exception e) {
            return ResponseResult.fail();
        }
    }

    @GetMapping("async")
    public ResponseResult<String> async() {
        try (RedisClient client = RedisClient.create("redis://127.0.0.1")) {
            StatefulRedisConnection<String, String> connection = client.connect();
            RedisStringAsyncCommands<String, String> async = connection.async();
            RedisFuture<String> set = async.set("key", "hellow");
            RedisFuture<String> get = async.get("key");

            log.info("set: {}, get: {}", set.get(), get.get());
            return ResponseResult.success();

        } catch (Exception e) {
            log.error("async error", e);
            return ResponseResult.fail();
        }
    }

    @GetMapping("reactive")
    public ResponseResult<String> reactive() {
        try (RedisClient client = RedisClient.create("redis://127.0.0.1")) {
            StatefulRedisConnection<String, String> connection = client.connect();
            RedisStringReactiveCommands<String, String> reactive = connection.reactive();
            Mono<String> set = reactive.set("key", "value");
            Mono<String> get = reactive.get("key");

            set.subscribe();
            String block = get.block();
            log.info("get: {}", block);
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("async error", e);
            return ResponseResult.fail();
        }
    }

}
