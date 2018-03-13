package com.my.baffinrpc.core.common.model;

import com.my.baffinrpc.core.common.constant.DefaultConfig;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.config.MethodConfig;
import com.my.baffinrpc.core.util.NetworkUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//rpc://127.0.0.1:9999/com.my.baffinrpc.test.HelloService?hi.isAsync=true&hello.hasCallback=true&hello.callbackArgIndex=1
public class URL implements Serializable{
    private final String interfaceName;
    private final String host;
    private final int port;
    private final Map<String,String> parameters;
    private transient String urlStringCache = null;

    public static final transient String IS_CALLBACK_METHOD = "isCallbackMethod";
    public static final transient String CALLBACK_ARG_INDEX = "callbackArgIndex";
    public static final transient String CALLBACK_ARG_INTERFACE = "callbackArgInterface";
    public static final transient String CALLBACK_PORT = "callbackPort";
    public static final transient String IS_ASYNC = "isAsync";
    public static final transient String PROTOCOL = "protocol";
    public static final transient String TRANSPORT = "transport";
    public static final transient String MESSAGE = "message";
    public static final transient String SERIALIZATION = "serialization";
    private static final transient String PROXY = "proxy";



    private URL(String interfaceName, String host, int port, Map<String,String> parameters) {
        this.interfaceName = interfaceName;
        this.host = host;
        this.port = port;
        this.parameters = parameters;
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
        //rpc://127.0.0.1:9999/com.my.baffinrpc.test.HelloService?hi.isAsync=true&hello.hasCallback=true&hello.callbackArgIndex=1
    }

    public static URL buildURL(String interfaceName, String host, int port, String transport, String serializeTypeName, String message, List<MethodConfig> methodConfigs)
    {
        Map<String,String> parameters = new HashMap<>();
        parameters.put(SERIALIZATION,serializeTypeName.toLowerCase());
        parameters.put(TRANSPORT,transport);
        parameters.put(MESSAGE,message);
        if (methodConfigs != null && methodConfigs.size() > 0) {
            for (MethodConfig methodConfig : methodConfigs) {
                String methodName = methodConfig.getMethodName();
                if (methodConfig.isAsync())
                    parameters.put(methodName + "." + IS_ASYNC, String.valueOf(methodConfig.isAsync()));
                if (methodConfig.isCallback()) {
                    parameters.put(methodName + "." + IS_CALLBACK_METHOD, String.valueOf(true));
                    parameters.put(methodName + "." + CALLBACK_ARG_INDEX, String.valueOf(methodConfig.getCallbackInfo().getMethodArgsIndex()));
                    parameters.put(methodName + "." + CALLBACK_ARG_INTERFACE, String.valueOf(methodConfig.getCallbackInfo().getCallbackInterface().getName()));
                    parameters.put(methodName + "." + CALLBACK_PORT, String.valueOf(methodConfig.getCallbackInfo().getCallbackURL().getPort()));
                }
            }
        }
        return new URL(interfaceName,host,port,parameters);

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


    public boolean isMethodAsync(String methodName)
    {
        if (parameters != null) {
            String booleanString = parameters.get(methodName + "." + IS_ASYNC);
            return booleanString != null;
        }
        return true;
    }


    /***
     * 从url中获取方法的callbackInfo
     * @param methodName 方法名
     * @return callbackInfo
     */
    public CallbackInfo getCallbackInfo(String methodName)
    {
        if (parameters != null) {
            String booleanString = parameters.get(methodName + "." + IS_CALLBACK_METHOD);
            if (booleanString == null)
                return null;
            int callbackIndex = Integer.parseInt(parameters.get(methodName + "." + CALLBACK_ARG_INDEX));
            String callbackInterfaceName = parameters.get(methodName + "." + CALLBACK_ARG_INTERFACE);
            int callbackPort = Integer.parseInt(parameters.get(methodName + "." + CALLBACK_PORT));
            String transport = parameters.get(TRANSPORT);
            String serialization = parameters.get(SERIALIZATION);
            String message = parameters.get(MESSAGE);
            try {
                Class<?> callbackInterface = Class.forName(callbackInterfaceName);
                URL callbackURL = URL.buildURL(callbackInterfaceName, NetworkUtil.getLocalHostAddress(),callbackPort,transport,serialization,message,null);
                return new CallbackInfo(callbackInterface, callbackIndex,callbackURL);
            } catch (ClassNotFoundException e) {
                throw new RPCFrameworkException("callback interface with name " + callbackInterfaceName + " not found");
            }
        }
        return null;
    }


    public String getTransport()
    {
        return parameters.get(TRANSPORT);
    }

    public String getMessage()
    {
        return parameters.get(MESSAGE);
    }

    public String getProxy()
    {
        return parameters.get(PROXY);
    }



}
