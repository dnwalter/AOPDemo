package com.ousy.aopmodule.aspects;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import com.ousy.aopmodule.annotations.LimitClick;
import com.ousy.aopmodule.annotations.SingleClick;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ousiyuan on 2018/12/29 0029.
 * description:
 */
@Aspect
public class SingleClickAspect {
    private static long mLastClickTime;
    private static int mLastClickId;

    private static final String POINTCUT_METHOD =
            "execution(* on*Click(..))";
    private static final String POINTCUT_SINGLE_CLICK =
            "execution(* on*Click(..))";
    private static final String POINTCUT_BUTTER_KNIFE =
            "execution(@butterknife.OnClick * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodPointcut() {

    }

    @Pointcut(POINTCUT_SINGLE_CLICK)
    public void methodSingleClickPointcut() {

    }

    @Pointcut(POINTCUT_BUTTER_KNIFE)
    public void butterKnifePointcut() {

    }

    @Around("methodPointcut() || butterKnifePointcut()")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Class pointClass = joinPoint.getTarget().getClass();

        Field[] fields = pointClass.getDeclaredFields();
        List<Field> viewFields = new ArrayList<>();
        // 带有注解的view的id
        List<Integer> viewIds = new ArrayList<>();
        // 获取带有LimitClick注解的view
        for (Field i : fields){
            if (i.isAnnotationPresent(LimitClick.class)){
                viewFields.add(i);
            }
        }

        if (viewFields.size() <= 0){
            return;
        }

        viewIds = getViewIds(joinPoint.getTarget(), viewFields);
        if (viewIds.size() <= 0){
            return;
        }

        //计算点击间隔，没有注解默认500，有注解按注解参数来，注解参数为空默认500；
        int interval = 500;

        Object[] args = joinPoint.getArgs();
        // 被点击的view
        View view = findViewInMethodArgs(args);
        if (args.length >= 1 && view != null) {
            int id = view.getId();
            int index = isHas(id, viewIds);
//            int index = isHasAnnoTation(id, viewFields);
            if (index < 0){
                joinPoint.proceed();
                return;
            }

            //本次点击控件与上次不同情况
            if (mLastClickId != id) {
                joinPoint.proceed();
                mLastClickId = id;
                mLastClickTime = System.currentTimeMillis();
                return;
            }

            LimitClick limitClick = viewFields.get(index).getAnnotation(LimitClick.class);
            interval = limitClick.value();
        }

        if (canClick(interval)) {
            joinPoint.proceed();
        }else{
            Log.e("ousyxx","不能快速点");
        }
    }

    private List<Integer> getViewIds(Object target, List<Field> viewFields)
    {
        List<Integer> viewIds = new ArrayList<>();
        for (Field i : viewFields){
            i.setAccessible(true);
            try
            {
                View view = (View) i.get(target);
                viewIds.add(view.getId());
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        return viewIds;
    }

    // 判断这个被点击的view有没有注解; i>=0为有注解
    private int isHas(int id, List<Integer> viewIds)
    {
        int i = 0;
        for (;i< viewIds.size();i++){
            if (id == viewIds.get(i)){
                break;
            }
        }
        if (i >= viewIds.size()){
            i = -1;
        }

        return i;
    }

    // 判断这个被点击的view有没有注解; i>=0为有注解
    private int isHasAnnoTation(int id, List<Field> viewFields)
    {
        int i = 0;
        for (;i< viewFields.size();i++){
            LimitClick limitClick = viewFields.get(i).getAnnotation(LimitClick.class);
            if (id == limitClick.viewId()){
                break;
            }
        }
        if (i >= viewFields.size()){
            i = -1;
        }

        return i;
    }

    //    @Around("methodPointcut() || butterKnifePointcut()")
//    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        //检查方法是否有注解
//        boolean hasAnnotation = method != null && method.isAnnotationPresent(SingleClick.class);
//        //点击的不同对象不计算点击间隔
//        Object[] args = joinPoint.getArgs();
//        View view = findViewInMethodArgs(args);
//        if (args.length >= 1 && view != null) {
//            int id = view.getId();
//            //本次点击控件与上次不同情况
//            if (mLastClickId != id) {
//                joinPoint.proceed();
//                mLastClickId = id;
//                mLastClickTime = System.currentTimeMillis();
//                return;
//            }
//            //注解排除某个控件不防止双击
//            if (hasAnnotation) {
//                SingleClick annotation = method.getAnnotation(SingleClick.class);
//                //按id排除点击
//                int[] except = annotation.except();
//                for (int i : except) {
//                    if (i == id) {
//                        joinPoint.proceed();
//                        return;
//                    }
//                }
//                //按id名排除点击
//                String[] idName = annotation.exceptIdName();
//                Resources resources = view.getResources();
//                for (String name : idName) {
//                    int resId = resources.getIdentifier(name, "id", view.getContext().getPackageName());
//                    if (resId == id) {
//                        joinPoint.proceed();
//                        return;
//                    }
//                }
//            }
//        }
//        //计算点击间隔，没有注解默认500，有注解按注解参数来，注解参数为空默认500；
//        int interval = 500;
//        if (hasAnnotation) {
//            SingleClick annotation = method.getAnnotation(SingleClick.class);
//            interval = annotation.value();
//        }
//        if (canClick(interval)) {
//            joinPoint.proceed();
//        }else{
//            Log.e("ousyxx","不能快速点");
//        }
//    }

    public View findViewInMethodArgs(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof View) {
                View view = (View) args[i];
                if (view.getId() != View.NO_ID) {
                    return view;
                }
            }
        }
        return null;
    }

    public boolean canClick(int interval) {
        long l = System.currentTimeMillis() - mLastClickTime;
        if (l > interval) {
            mLastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}

