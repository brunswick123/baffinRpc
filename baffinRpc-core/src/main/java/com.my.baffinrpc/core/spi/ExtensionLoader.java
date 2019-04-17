package com.my.baffinrpc.core.spi;

import com.my.baffinrpc.core.annotation.Extension;
import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.common.exception.RPCConfigException;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.util.ReflectUtil;
import com.my.baffinrpc.core.util.StringUtil;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    private static HashMap<String,HashMap<String,Class<?>>> extensionImplMap = new HashMap<>();
    private static ConcurrentHashMap<String,Object> cachedInstanceMap = new ConcurrentHashMap<>();
    private static Set<Class<?>> allExtensions = new HashSet<>();
    private static final String CORE_PATH = "com.my.baffinrpc.core";

    static
    {
        //获取所有扩展点
        Reflections reflections = new Reflections(CORE_PATH);
        allExtensions = reflections.getTypesAnnotatedWith(Extension.class);
        //获取core下的所有扩展点实现
        loadExtensionImplFromCore();
    }

    private static void loadExtensionImplFromCore()
    {
        loadExtensionImplFromPath(CORE_PATH);
    }

    public static void loadExtensionImplFromPath(String basePackage)
    {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> allExtensionImpl = reflections.getTypesAnnotatedWith(ExtensionImpl.class);
        for (Class<?> clz : allExtensionImpl)
        {
            if (ReflectUtil.isClassAbstract(clz))
                throw new RPCConfigException(clz.getName() + " is an abstract class, cannot be an extension implementation");
            ExtensionImpl extensionImpl = clz.getAnnotation(ExtensionImpl.class);
            String name = extensionImpl.name();
            if (StringUtil.isEmptyOrNull(name))
                throw new RPCFrameworkException("Name field for @extensionImpl of " + clz.getName() + " cannot be null or empty");
            Class<?> extension = extensionImpl.extension();
            if (!allExtensions.contains(extension))
                throw new RPCFrameworkException(extension.getName() + " is not a possible extension, and it cannot be the extension for " + clz.getName());

            HashMap<String,Class<?>> extensionImplMapForOneExtension = extensionImplMap.get(extension.getName());
            if (extensionImplMapForOneExtension == null)
                extensionImplMapForOneExtension = new HashMap<>();
            extensionImplMapForOneExtension.put(name,clz);
            extensionImplMap.put(extension.getName(),extensionImplMapForOneExtension);
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T getExtension(Class<?> clz, String extensionImplName)
    {
        String cachedMapKey = clz.getName()+ "." + extensionImplName + ".class";
        //先从缓存中获取
        Object instance = cachedInstanceMap.get(cachedMapKey);
        if (instance != null) {
            return (T) instance;
        }
        //缓存中不存在,获取Class，再用newInstance()创建
        //根据extension找到所有的extensionImpl,再由extensionImplName找到class
        HashMap<String,Class<?>> extensionImplMapForOneExtension = extensionImplMap.get(clz.getName());
        if (extensionImplMapForOneExtension!= null)
        {
            Class<?> extensionImplClz = extensionImplMapForOneExtension.get(extensionImplName);
            if (extensionImplClz != null)
                try {
                    Object o = extensionImplClz.newInstance();
                    //加入缓存
                    cachedInstanceMap.put(cachedMapKey,o);
                    return (T)o;
                } catch (Exception e) {
                   throw new RPCFrameworkException(e);
                }
        }
        throw new RPCFrameworkException("Extension implementation for extension " + clz.getName() + " with name " + extensionImplName + " is not found");
    }



}
