package com.my.baffinrpc.core.spi;

import java.util.ServiceLoader;

public class ExtensionLoader {

    public static <T> T getExtension(Class<T> interfaceClz,String extensionName)
    {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(interfaceClz);
        for (T extension : serviceLoader) {
            if (extension.getClass().getName().equals(extensionName))
                return extension;
        }
        return null;
    }
}
