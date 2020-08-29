package priv.example.web.aspect;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    // 切点，controller层
    @Pointcut("execution(* priv.example.web.controller..*(..))")
    public void controllers() {
    }

    @Around("controllers()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 开始时间
        long start = Instant.now().toEpochMilli();
        // 执行切点
        Object result = proceedingJoinPoint.proceed();
        if (log.isDebugEnabled()) {
            aroundAfterLog(start, proceedingJoinPoint, result);
        }

        return result;
    }

    @AfterThrowing(pointcut = "controllers()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        // 获取签名对象
        Signature signature = joinPoint.getSignature();
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        // 记录日志
        RequestErrorInfo logErrInfo = new RequestErrorInfo();
        logErrInfo.setIp(request.getRemoteAddr());
        logErrInfo.setUrl(request.getRequestURL().toString());
        logErrInfo.setHttpMethod(request.getMethod());
        logErrInfo.setClassMethod(String.format("%s.%s", signature.getDeclaringTypeName(), signature.getName()));
        logErrInfo.setRequestParams(getRequestParams(joinPoint));
        logErrInfo.setException(e);

        log.debug("本次请求错误日志信息：{}", JSON.toJSONString(logErrInfo));
    }

    private void aroundAfterLog(long start, ProceedingJoinPoint proceedingJoinPoint, Object result) {
        // 获取签名对象
        Signature signature = proceedingJoinPoint.getSignature();
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        // 记录日志
        RequestInfo logInfo = new RequestInfo();
        logInfo.setIp(request.getRemoteAddr());
        logInfo.setUrl(request.getRequestURL().toString());
        logInfo.setHttpMethod(request.getMethod());
        logInfo.setClassMethod(String.format("%s.%s", signature.getDeclaringTypeName(), signature.getName()));
        logInfo.setRequestParams(getRequestParams(proceedingJoinPoint));
        logInfo.setResult(result);
        logInfo.setTimeCost(Instant.now().toEpochMilli() - start);

        log.debug("本次请求日志信息：{}", JSON.toJSONString(logInfo));
    }

    private Map<String, Object> getRequestParams(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名
        String[] paramNames = methodSignature.getParameterNames();
        // 获取参数值
        Object[] paramValues = joinPoint.getArgs();

        return paramsHandler(Arrays.asList(paramNames), Arrays.asList(paramValues));
    }

    private Map<String, Object> getRequestParams(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 获取参数名
        String[] paramNames = methodSignature.getParameterNames();
        // 获取参数值
        Object[] paramValues = proceedingJoinPoint.getArgs();

        return paramsHandler(Arrays.asList(paramNames), Arrays.asList(paramValues));
    }

    private Map<String, Object> paramsHandler(List<String> names, List<Object> values) {
        return names.stream().collect(
                Collectors.toMap(name -> name, name -> {
                    Object value = values.get(names.indexOf(name));
                    if (value instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) value;
                        // 获取文件名
                        String filename = file.getOriginalFilename();
                        if (filename == null) {
                            filename = "空文件名";
                        }
                        return filename;
                    }
                    return value;
                })
        );
    }

}

@Getter
@Setter
class RequestInfo {

    private String ip;

    private String url;

    private String httpMethod;

    private String classMethod;

    private Object requestParams;

    private Object result;

    private Long timeCost;

}

@Getter
@Setter
class RequestErrorInfo {

    private String ip;

    private String url;

    private String httpMethod;

    private String classMethod;

    private Object requestParams;

    private Exception exception;

}
