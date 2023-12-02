package com.mylsaber.exception.handle.config;

import com.mylsaber.exception.handle.domain.ResponseResult;
import com.mylsaber.exception.handle.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mylsaber
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @InitBinder
    public void handleInitBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    }

    @ModelAttribute("currentUser")
    public String modelAttribute() {
        return "user";
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResponseResult<String> handleParameterVerificationException(@NonNull Exception e) {
        log.error(e.getLocalizedMessage(), e);
        if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            return ResponseResult.<String>builder().code(400).message(bindingResult.getAllErrors().get(0).getDefaultMessage()).build();
        } else {
            return ResponseResult.<String>builder().code(400).message(e.getLocalizedMessage()).build();
        }
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseResult<String> processBusinessException(BusinessException businessException) {
        log.error(businessException.getLocalizedMessage(), businessException);
        return ResponseResult.<String>builder().code(400).message(businessException.getLocalizedMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<String> processException(Exception exception) {
        log.error(exception.getLocalizedMessage(), exception);
        return ResponseResult.<String>builder().code(500).message(exception.getLocalizedMessage()).build();
    }
}
