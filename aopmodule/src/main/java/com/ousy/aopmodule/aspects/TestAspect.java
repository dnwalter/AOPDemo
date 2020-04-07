package com.ousy.aopmodule.aspects;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by ousiyuan on 2018/12/29 0029.
 * description:
 */
@Aspect
public class TestAspect {

//    @Before("execution(* android.app.Activity.on**(..))")
    @Before("execution(* android.app.Activity.onCreate(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint)
    {
        String key = joinPoint.toShortString();
        Log.e("ousyxx", "onActivityMethodBefore: " + key);
    }

    @After("execution(* android.app.Activity.onResume(..))")
    public void onActivityMethodAfter(JoinPoint joinPoint)
    {
        String key = joinPoint.toShortString();
        Log.e("ousyxx", "onActivityMethodAfter: " + key);
    }

//    @Around("execution(* android.app.Activity.onStart(..))")
    public void onActivityMethodAround(JoinPoint joinPoint)
    {
        String key = joinPoint.toShortString();
        Log.e("ousyxx", "onActivityMethodAround: " + key);
    }

//    @Before("execution(* @com.ousy.aopmodule.interfaces.DebugLog * *(..))")
//    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
//        String key = joinPoint.toShortString();
//        Log.d("ousyxx", "onActivityMethodBefore: " + key);
//    }
}
