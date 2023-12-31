package com.baizhi.aspect;

import com.baizhi.config.RedisConstants;
import com.baizhi.dto.AdminResponse;
import com.baizhi.dto.Result;
import com.baizhi.entity.Log;
import com.baizhi.mapper.LogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    HttpSession session;

    @Around("execution(* com.baizhi.service.impl.*.add*(..))")
    public Object arroud(ProceedingJoinPoint joinPoint) throws Throwable {
        String target = joinPoint.getTarget().getClass().getName();
        Log log = new Log();
        Signature signature = joinPoint.getSignature();
        System.out.println("执行的方法名为" + signature.getName());
        Result proceed = (Result) joinPoint.proceed();
        log.setMethodName(signature.toLongString());
//        获取redis存储的用户对象
        AdminResponse adminResponse = (AdminResponse) redisTemplate.opsForValue().get(RedisConstants.LOGIN_PREFIX_VALUE + session.getId());
        log.setAdminName(adminResponse.getUsername());
        if (proceed.getStatus()) {
            log.setOptionStatus(1);
            redisTemplate.delete(target);
        } else {
            log.setOptionStatus(0);
        }
        logMapper.insert(log);
        return proceed;
    }

    @Around(value = "execution(* com.baizhi.service.impl.*.query*(..)) || @annotation(com.baizhi.annotation.AddCache)")
    public Object around2(ProceedingJoinPoint joinPoint) throws Throwable {
        //重新设置存活时间
        redisTemplate.expire(RedisConstants.LOGIN_PREFIX_VALUE + session.getId(), 30, TimeUnit.MINUTES);
//        获取目标对象
        String target = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg);
        }
        System.out.println("参数为" + args.toString());
//        获取目标方法
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        name = sb.append(name).toString();
        HashOperations hash = redisTemplate.opsForHash();

        Boolean hasKey = hash.hasKey(target, name);
        //        根据目标对象和方法名看是否能获取结果
        if (hasKey) {
            Object result = hash.get(target, name);
            System.out.println(target);
            System.out.println(name);
            System.out.println(result);
            return result;
        }
//        没找到放行
        System.out.println("没找到缓存");
        Result proceed = (Result) joinPoint.proceed();
        hash.put(target, name, proceed);
        return proceed;
    }
}
