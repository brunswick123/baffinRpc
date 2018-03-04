package com.my.baffinrpc.core.common.model;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.serialization.SerializeType;
import com.my.baffinrpc.core.config.MethodConfig;
import com.my.baffinrpc.core.util.URLUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//rpc://127.0.0.1:9999/com.my.baffinrpc.test.HelloService?hi.isAsync=true&hello.isCallback=true&hello.callbackArgIndex=1
public class URL implements Serializable{
    private String interfaceName;
    private String host;
    private int port;
    private transient Map<String,String> parameters = new HashMap<>();
    private transient String urlStringCache = null;


    public URL(String interfaceName, String host, int port, Map<String,String> parameters) {
        this.interfaceName = interfaceName;
        this.host = host;
        this.port = port;
        this.parameters = parameters;
    }

    public URL(String interfaceName, String host, int port) {
        this.interfaceName = interfaceName;
        this.host = host;
        this.port = port;
    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        URL url = (URL) object;
        if (port != url.port) return false;
        if (!interfaceName.equals(url.interfaceName)) return false;
        return host.equals(url.host);
    }

    @Override
    public int hashCode() {
        int result = interfaceName.hashCode();
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
        return result;
    }


    public String buildString()
    {
        if (urlStringCache == null) {
            StringBuilder stringBuilder = new StringBuilder("rpc://");
            stringBuilder.append(host).append(":").append(port).append("/").append(interfaceName).append("?");
            int count = 0;
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                stringBuilder.append(key).append("=").append(value);
                count++;
                if (count != parameters.keySet().size()) {
                    stringBuilder.append("&");
                }
            }
            urlStringCache = stringBuilder.toString();
        }
        return urlStringCache;
        //rpc://127.0.0.1:9999/com.my.baffinrpc.test.HelloService?hi.isAsync=true&hello.isCallback=true&hello.callbackArgIndex=1
    }

    public static URL buildURL(String interfaceName, String host, int port, String transportType, SerializeType serializeType, List<MethodConfig> methodConfigs)
    {
        Map<String,String> parameters = new HashMap<>();
        parameters.put(URLUtil.SERIALIZATION,serializeType.name().toLowerCase());
        parameters.put(URLUtil.TRANSPORT,transportType);
        for (MethodConfig methodConfig : methodConfigs)
        {
            String methodName = methodConfig.getMethodName();
            if (methodConfig.isAsync())
                parameters.put(methodName + "." + URLUtil.IS_ASYNC, String.valueOf(methodConfig.isAsync()));
            if (methodConfig.isCallback())
            {
                parameters.put(methodName + "." + URLUtil.IS_CALLBACK, String.valueOf(methodConfig.isCallback()));
                parameters.put(methodName + "." + URLUtil.CALLBACK_ARG_INDEX,String.valueOf(methodConfig.getCallbackInfo().getMethodArgsIndex()));
                parameters.put(methodName + "." + URLUtil.CALLBACK_ARG_INTERFACE, String.valueOf(methodConfig.getCallbackInfo().getCallbackInterface().getName()));

            }
        }
        URL url = new URL(interfaceName,host,port);
        url.setParameters(parameters);
        return url;

    }

    public static URL buildURLFromString(String urlString)
    {
        if (urlString == null || urlString.trim().length() == 0)
            throw new IllegalArgumentException(urlString  +  " is not a valid url string");
        String host = "";
        int port = 0;
        try {
            String[] protocolAndBody = urlString.split("//");
            String protocol  = protocolAndBody[0];
            String[] addressAndParameter = protocolAndBody[1].split("\\?");
            String[] ipAndServiceName = addressAndParameter[0].split("/");
            String serviceName = ipAndServiceName[1];
            String[] hostAndPort = ipAndServiceName[0].split(":");
            host = hostAndPort[0];
            port = Integer.parseInt(hostAndPort[1]);
            Map<String,String> parameters = parseParameters(addressAndParameter[1]);
            return new URL(serviceName,host,port,parameters);

        }catch (Exception e)
        {
            throw new RPCFrameworkException("parse url[" + urlString + "] failed due to " + e);
        }
    }

    private static Map<String,String> parseParameters(String parametersString)
    {
        String[] keyAndValuePairs = parametersString.split("&");
        Map<String,String> parameters = new HashMap<>();
        for (String keyAndValue : keyAndValuePairs)
        {
            String[] parts = keyAndValue.split("=");
            parameters.put(parts[0],parts[1]);
        }
        return parameters;
    }

    @Override
    public String toString()
    {
        if (urlStringCache == null)
            return buildString();
        else
            return urlStringCache;
    }



}
