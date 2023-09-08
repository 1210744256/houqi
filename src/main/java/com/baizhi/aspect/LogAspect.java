package com.baizhi.aspect;

import cn.hutool.core.util.ObjectUtil;
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
        }else {
            log.setOptionStatus(0);
        }
        logMapper.insert(log);
        return proceed;
    }
    @Around(value = "execution(* com.baizhi.service.impl.*.*(..))")
    public Object around2(ProceedingJoinPoint joinPoint) throws Throwable {
//        获取目标对象
        Object target = joinPoint.getTarget();
        target=target.toString().split("@")[0];
//        获取目标方法
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        HashOperations hash = redisTemplate.opsForHash();
//        根据目标对象和方法名看是否能获取结果
        Object result = hash.get(target, name);
        System.out.println(target);
        System.out.println(name);
        System.out.println(result);
        if(!ObjectUtil.isEmpty(result)){
            System.out.println("找到缓存");
            return result;
        }
//        没找到放行
        System.out.println("没找到缓存");
        Result proceed = (Result) joinPoint.proceed();
        hash.put(target,name,proceed);
        return proceed;
    }
}
