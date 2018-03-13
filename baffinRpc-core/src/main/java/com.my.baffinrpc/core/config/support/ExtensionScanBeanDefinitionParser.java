package com.my.baffinrpc.core.config.support;

import com.my.baffinrpc.core.spi.ExtensionLoader;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ExtensionScanBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String basePackage = element.getAttribute("basePackage");
        if (basePackage != null && !"".equals(basePackage.trim()))
        {
            ExtensionLoader.loadExtensionImplFromPath(basePackage);
        }
        return null;
    }
}
