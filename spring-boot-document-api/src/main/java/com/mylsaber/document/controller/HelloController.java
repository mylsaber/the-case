package com.mylsaber.document.controller;

import com.mylsaber.document.domain.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mylsaber
 */
@RestController
@RequestMapping("/hello")
@Tag(name = "这是Hello Controller")
public class HelloController {

    @GetMapping("/hello")
    @Operation(summary = "Say Hello", description = "这个方法用来说hello")
    @Parameters(
            @Parameter(name = "name", description = "姓名", required = true)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResponseResult.class))),
    })
    public ResponseResult<String> sayHello(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        return ResponseResult.success("hello " + name);
    }
}
