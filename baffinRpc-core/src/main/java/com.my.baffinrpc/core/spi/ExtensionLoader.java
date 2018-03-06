package com.my.baffinrpc.core.spi;

import com.my.baffinrpc.core.annotation.Extension;
import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    private static HashMap<String,HashMap<String,Class<?>>> extensionImplMap = new HashMap<>();
    private static ConcurrentHashMap<String,Object> cachedInstanceMap = new ConcurrentHashMap<>();
    private static Set<Class<?>> allExtensions = new HashSet<>();
    public static final String CORE_PATH = "com.my.baffrpc.core";


    public static void loadAllExtensions()
    {
        Reflections reflections = new Reflections(CORE_PATH);
        allExtensions = reflections.getTypesAnnotatedWith(Extension.class);

    }

    public static void loadAllExtensionImpl(String basePackage)
    {
        Reflections reflections = new Reflections(CORE_PATH);
        Set<Class<?>> allExtensionImpl = reflections.getTypesAnnotatedWith(ExtensionImpl.class);
        for (Class<?> clz : allExtensionImpl)
        {
            ExtensionImpl extensionImpl = clz.getAnnotation(ExtensionImpl.class);
            String name = extensionImpl.name();
            Class<?> extension = extensionImpl.extension();
            if (!allExtensions.contains(extension))
                throw new RPCFrameworkException(extension.getName() + " is not a possible extension, cannot be the extension for " + clz.getName());
            HashMap<String,Class<?>> extensionImplMapForOneExtension = extensionImplMap.get(extension.getName());
            if (extensionImplMapForOneExtension == null)
            {
                extensionImplMapForOneExtension = new HashMap<>();
            }
            extensionImplMapForOneExtension.put(name,clz);
            extensionImplMap.put(extension.getName(),extensionImplMapForOneExtension);
        }

    }

    public static <T> T getExtension(Class<?> clz, String extensionImplName)
    {
        String cachedMapKey = clz.getName()+ "." + extensionImplName + ".class";
        Object instance = cachedInstanceMap.get(cachedMapKey);
        if (instance != null)
            return (T)instance;
        //缓存中不存在,获取类 用newInstance()创建
        HashMap<String,Class<?>> extensionImplMapForOneExtension = extensionImplMap.get(clz.getName());
        if (extensionImplMapForOneExtension!= null)
        {
            Class<?> extensionImplClz = extensionImplMapForOneExtension.get(extensionImplName);
            if (extensionImplClz != null)
                try {
                    Object o = extensionImplClz.newInstance();
                    cachedInstanceMap.put(cachedMapKey,o);
                    return (T)o;
                } catch (Exception e) {
                   throw new RPCFrameworkException(e);
                }
        }
        throw new RPCFrameworkException("Extension implementation for" + clz.getName() + " with name " + extensionImplName + "is not found");
    }



}
