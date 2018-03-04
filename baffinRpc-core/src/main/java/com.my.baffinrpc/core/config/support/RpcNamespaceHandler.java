package com.my.baffinrpc.core.config.support;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser());
        registerBeanDefinitionParser("exportService", new ExportServiceBeanDefinitionParser());
        registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParser());
    }
}
