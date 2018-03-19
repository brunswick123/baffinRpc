package com.my.baffinrpc.core.config.support;

import com.my.baffinrpc.core.common.constant.DefaultConfig;
import com.my.baffinrpc.core.common.exception.RPCConfigException;
import com.my.baffinrpc.core.config.RegistryConfig;
import com.my.baffinrpc.core.registry.RegistryService;
import com.my.baffinrpc.core.registry.zookeeper.ZkRegistryService;
import com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.ZookeeperClient;
import com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.ZookeeperClientFactory;
import com.my.baffinrpc.core.spi.ExtensionLoader;
import com.my.baffinrpc.core.util.StringUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class RegistryBeanDefinitionParser implements BeanDefinitionParser {

    private static final String TYPE_ZOOKEEPER = "zookeeper";
    private static final String TYPE_DIRECT = "direct";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(RegistryConfig.class);
        beanDefinition.setLazyInit(false);
        String type = element.getAttribute("type");
        if (type.equals(TYPE_ZOOKEEPER))
        {
            String address = element.getAttribute("address");
            if (!StringUtil.isEmptyOrNull(address))
            {
                String zookeeperClient = element.getAttribute("zookeeperClient");
                if (StringUtil.isEmptyOrNull(zookeeperClient))
                    zookeeperClient = DefaultConfig.ZOOKEEPER_CLIENT;
                ZookeeperClientFactory zookeeperClientFactory = ExtensionLoader.getExtension(ZookeeperClientFactory.class,zookeeperClient);
                RegistryService registryService = new ZkRegistryService(address,zookeeperClientFactory);
                parserContext.getRegistry().registerBeanDefinition(StringUtil.convertFirstLetterToLowerCase(RegistryConfig.class.getSimpleName()),beanDefinition);
                beanDefinition.getPropertyValues().addPropertyValue("registryService",registryService);
            }
            else
                throw new RPCConfigException("Invalid address for zookeeper " + address);

        }
        else if (type.equals(TYPE_DIRECT))
            throw new RPCConfigException("Direct registry not supported");
        return beanDefinition;
    }




}
