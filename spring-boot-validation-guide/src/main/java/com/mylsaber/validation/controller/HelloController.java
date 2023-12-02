package com.mylsaber.validation.controller;

import com.mylsaber.validation.domain.ResponseResult;
import com.mylsaber.validation.dto.UserParam;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author mylsaber
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class HelloController {

    @Autowired
    private Validator validator;

    @PostMapping("/v1/add")
    public ResponseResult<String> addV1(@Validated @RequestBody UserParam userParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                log.error("Invalid Parameter : object - {},field - {},errorMessage - {}", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
            });
            return ResponseResult.fail("invalid parameter");
        }
        return ResponseResult.success("success");
    }

    @PostMapping("/v2/add")
    public ResponseResult<UserParam> addV2(@Validated(UserParam.AddGroup.class) @RequestBody UserParam userParam) {
        return ResponseResult.success(userParam);
    }

    @PostMapping("edit")
    public ResponseResult<UserParam> edit(@Validated(UserParam.UpdateGroup.class) @RequestBody UserParam userParam) {
        return ResponseResult.success(userParam);
    }

    @PostMapping("/v3/add")
    public ResponseResult<UserParam> addV3(@RequestBody UserParam userParam) {
        Set<ConstraintViolation<UserParam>> validate = validator.validate(userParam);
        if (!validate.isEmpty()) {
            validate.forEach(constraintViolation -> log.error(constraintViolation.getMessage()));
            return ResponseResult.fail();
        }
        return ResponseResult.success(userParam);
    }
}
