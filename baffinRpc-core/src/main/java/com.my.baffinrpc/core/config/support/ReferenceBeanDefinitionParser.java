package com.my.baffinrpc.core.config.support;

import com.my.baffinrpc.core.common.exception.RPCConfigException;
import com.my.baffinrpc.core.config.ClusterConfig;
import com.my.baffinrpc.core.config.ProtocolConfig;
import com.my.baffinrpc.core.config.ReferenceConfig;
import com.my.baffinrpc.core.config.RegistryConfig;
import com.my.baffinrpc.core.util.StringUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReferenceBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ReferenceConfig.class);
        builder.setLazyInit(true);
        String interfaceClzName = element.getAttribute("interface");
        Class<?> interfaceClz = null;
        try {
            interfaceClz = Class.forName(interfaceClzName);
            if (!interfaceClz.isInterface())
                throw new RPCConfigException(interfaceClzName + " is not a interface");
        } catch (ClassNotFoundException e) {
            throw new RPCConfigException(e);
        }
        String beanName = StringUtil.convertFirstLetterToLowerCase(interfaceClz.getSimpleName());
        parserContext.getRegistry().registerBeanDefinition(beanName,builder.getBeanDefinition());
        builder.addPropertyValue("interfaceClz",interfaceClz);
        builder.addPropertyValue("clusterConfig",parseClusterConfig(element,interfaceClzName));
        builder.addPropertyReference("registryConfig",StringUtil.convertFirstLetterToLowerCase(RegistryConfig.class.getSimpleName()));
        return builder.getBeanDefinition();
    }

    private ClusterConfig parseClusterConfig(Element element, String interfaceName)
    {
        ClusterConfig.Builder builder= new ClusterConfig.Builder();
        NodeList nodeList = element.getElementsByTagName("rpc:cluster");
        if (nodeList != null && nodeList.getLength() > 0)
        {
            if (nodeList.getLength() > 1)
                throw new RPCConfigException("More than one cluster config found for " + interfaceName + ", only one config is allowed for one reference");
            else
            {

                Element protocolElement = (Element)nodeList.item(0);
                String highAvailable = protocolElement.getAttribute("highAvailable");
                if (!StringUtil.isEmptyOrNull(highAvailable))
                    builder.highAvailableStrategy(highAvailable);
                String loadBalance = protocolElement.getAttribute("loadBalance");
                if (!StringUtil.isEmptyOrNull(loadBalance))
                    builder.loadBalanceStrategy(loadBalance);
            }
        }
        return builder.build();
    }


   /* @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ReferenceConfig.class);
        beanDefinition.setLazyInit(false);
        String interfaceClzName = element.getAttribute("class");

        Class<?> interfaceClz = null;
        try {
            interfaceClz = Class.forName(interfaceClzName);
            if (!interfaceClz.isInterface())
                throw new RPCFrameworkException(interfaceClzName + " is not a interface");
        } catch (ClassNotFoundException e) {
            throw new RPCFrameworkException(e);
        }
        String beanName = StringUtil.convertFirstLetterToLowerCase(interfaceClz.getSimpleName());
        parserContext.getRegistry().registerBeanDefinition(beanName,beanDefinition);
        beanDefinition.getPropertyValues().addPropertyValue("interfaceClz",interfaceClz);
        return beanDefinition;
    }*/


}
