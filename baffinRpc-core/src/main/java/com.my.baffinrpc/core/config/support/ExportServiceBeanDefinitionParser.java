package com.my.baffinrpc.core.config.support;

import com.my.baffinrpc.core.common.constant.DefaultConfig;
import com.my.baffinrpc.core.common.exception.RPCConfigException;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.CallbackInfo;
import com.my.baffinrpc.core.config.MethodConfig;
import com.my.baffinrpc.core.config.ProtocolConfig;
import com.my.baffinrpc.core.config.RegistryConfig;
import com.my.baffinrpc.core.config.ServiceConfig;
import com.my.baffinrpc.core.filter.ExceptionFilter;
import com.my.baffinrpc.core.filter.ResultSerializableCheckFilter;
import com.my.baffinrpc.core.protocol.FilterWrapProtocol;
import com.my.baffinrpc.core.protocol.Protocol;
import com.my.baffinrpc.core.protocol.ProtocolImpl;
import com.my.baffinrpc.core.util.ReflectUtil;
import com.my.baffinrpc.core.util.StringUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExportServiceBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String exportInterfaceName = element.getAttribute("interface");
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ServiceConfig.class);
        builder.setLazyInit(false);
        try {
            Class interfaceClass = Class.forName(exportInterfaceName);
            if (!interfaceClass.isInterface())
                throw new IllegalStateException(interfaceClass.getName() + " is not a interface");
            builder.addPropertyValue("interfaceClz",interfaceClass);
            String serviceImplBeanId = element.getAttribute("ref");
            if (parserContext.getRegistry().containsBeanDefinition(serviceImplBeanId))
            {
                Object serviceImplBeanReference = new RuntimeBeanReference(serviceImplBeanId);
                builder.addPropertyValue("serviceRef",serviceImplBeanReference);
            }
            //set protocolConfig
            builder.addPropertyValue("protocolConfig",parseProtocol(element,interfaceClass));
            //set methodConfig
            builder.addPropertyValue("methodConfigs",parseMethods(element, interfaceClass));
            //set registryConfig
            builder.addPropertyReference("registryConfig",StringUtil.convertFirstLetterToLowerCase(RegistryConfig.class.getSimpleName()));
            parserContext.getRegistry().registerBeanDefinition(exportInterfaceName,builder.getBeanDefinition());
            return builder.getBeanDefinition();
        }catch (Exception e)
        {
            throw new RPCFrameworkException(e);
        }
    }

    private ProtocolConfig parseProtocol(Element element, Class<?> interfaceClass)
    {
        NodeList nodeList = element.getElementsByTagName("rpc:protocol");
        if (nodeList != null && nodeList.getLength() > 0)
        {
            if (nodeList.getLength() > 1)
                throw new RPCConfigException("more than one protocol config found for " + interfaceClass.getName() + ", only one config is allowed for one service");
            else
            {
                Protocol protocol = null;
                Element protocolElement = (Element)nodeList.item(0);
                String transportName = protocolElement.getAttribute("transport");
                if (StringUtil.isEmptyOrNull(transportName))
                    transportName = DefaultConfig.TRANSPORT;
                String portString = protocolElement.getAttribute("port");
                if (!StringUtil.isEmptyOrNull(portString))
                {
                    try {
                        int port = Integer.parseInt(portString);
                        protocol = new ProtocolImpl(port);
                    }catch (Exception e)
                    {
                        throw new RPCFrameworkException(e);
                    }
                }else
                    protocol = new ProtocolImpl();

                //todo use protocol interface
                FilterWrapProtocol filterWrapProtocol = new FilterWrapProtocol(protocol);
                filterWrapProtocol.addFilter(new ExceptionFilter());
                filterWrapProtocol.addFilter(new ResultSerializableCheckFilter());
                return new ProtocolConfig(filterWrapProtocol);
            }
        }
        else
        {
            FilterWrapProtocol filterWrapProtocol = new FilterWrapProtocol(new ProtocolImpl());
            filterWrapProtocol.addFilter(new ExceptionFilter());
            filterWrapProtocol.addFilter(new ResultSerializableCheckFilter());
            return new ProtocolConfig(new FilterWrapProtocol(filterWrapProtocol));
        }
    }

    private List<MethodConfig> parseMethods(Element element, Class<?> interfaceClass)
    {
        List<MethodConfig> methodConfigList = new ArrayList<>();
        NodeList nodeList = element.getElementsByTagName("rpc:exportMethod");
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            MethodConfig methodConfig = MethodConfig.defaultMethodConfig();
            Element exportMethod = (Element)nodeList.item(i);
            String methodName = exportMethod.getAttribute("name");
            Method method = ReflectUtil.findMethod(interfaceClass,methodName);
            if (method == null)
                throw new RPCConfigException(methodName + " method is not found for interface " + interfaceClass.getName());
            methodConfig.setMethodName(methodName);
            String asyncString = exportMethod.getAttribute("async");
            if (!StringUtil.isEmptyOrNull(asyncString))
            {
                boolean async = Boolean.parseBoolean(asyncString);
                methodConfig.setAsync(async);
            }
            NodeList argsList = element.getElementsByTagName("rpc:args");
            if (argsList.getLength() > 1)
                throw new RPCConfigException("multiple args config not supported");
            for (int j = 0; j < argsList.getLength(); j++)
            {
                Element args = (Element)argsList.item(j);
                boolean callback = Boolean.parseBoolean(args.getAttribute("callback"));
                methodConfig.setCallback(callback);
                if (callback)
                {
                    try {
                        Class<?> callbackInterface = Class.forName(args.getAttribute("interface"));
                        if (!callbackInterface.isInterface())
                        {
                            throw new RPCConfigException(callbackInterface.getName() + " is not a interface, can not be a callback arg");
                        }
                        int callbackParameterIndex = ReflectUtil.findArgIndexByTypeInMethod(callbackInterface,method);
                        if (callbackParameterIndex == -1)
                            throw new RPCConfigException(callbackInterface.getName() + " is not a arg type in " + interfaceClass.getName() + "." + methodName);
                        CallbackInfo callbackInfo = new CallbackInfo(callbackInterface,callbackParameterIndex);
                        methodConfig.setCallback(true);
                        methodConfig.setCallbackInfo(callbackInfo);
                    } catch (ClassNotFoundException e) {
                        throw new RPCConfigException(e);
                    }
                }
            }
            methodConfigList.add(methodConfig);
        }
        return methodConfigList;
    }


}
