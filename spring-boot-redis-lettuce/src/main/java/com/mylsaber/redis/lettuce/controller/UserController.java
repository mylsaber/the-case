package com.mylsaber.redis.lettuce.controller;

import com.mylsaber.redis.lettuce.domain.ResponseResult;
import com.mylsaber.redis.lettuce.dto.User;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangdi
 */
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

}
