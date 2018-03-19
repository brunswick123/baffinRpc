package com.my.baffinrpc.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectUtil {
    public static Method findMethod(Class<?> clz, String methodName)
    {
        Method[] methods = clz.getMethods();
        for (Method method : methods)
        {
            if (method.getName().equals(methodName))
                return method;
        }
        return null;
    }

    public static int findArgIndexByTypeInMethod(Class<?> classType,Method method)
    {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (parameterTypes[i].getName().equals(classType.getName()))
                return i;
        }
        return -1;
    }

    public static boolean isClassAbstract(Class<?> clz)
    {
        return Modifier.isAbstract(clz.getModifiers());
    }
}
