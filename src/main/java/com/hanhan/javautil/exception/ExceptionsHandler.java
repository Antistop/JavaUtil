package com.hanhan.javautil.exception;

import com.hanhan.javautil.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler(value = BizException.class)
    public Result<Void> Exception(BizException e) {
        log.error("业务异常: ", e);
        return Result.error(e);
    }

    @ExceptionHandler(value = Exception.class)
    public Result<Void> Exception(Exception e) {
        log.error("接口未知异常", e);
        String message = e.getMessage();
        if (Objects.isNull(message)) {
            message = Arrays.stream(e.getStackTrace()).limit(10).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
        }
        return Result.error(BizError.SYSTEM_ERROR, message);
    }
}
