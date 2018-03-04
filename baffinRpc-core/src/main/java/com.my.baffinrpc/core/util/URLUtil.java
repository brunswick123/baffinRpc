package com.my.baffinrpc.core.util;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.CallbackInfo;
import com.my.baffinrpc.core.common.model.URL;

import java.util.Map;

public class URLUtil {

    public static final String IS_CALLBACK = "isCallback";
    public static final String CALLBACK_ARG_INDEX = "callbackArgIndex";
    public static final String CALLBACK_ARG_INTERFACE = "callbackArgInterface";
    public static final String IS_ASYNC = "isAsync";
    public static final String PROTOCOL = "protocol";
    public static final String TRANSPORT = "transport";
    public static final String MESSAGE = "message";
    public static final String SERIALIZATION = "serialization";


    public static boolean isMethodAsync(URL url, String methodName)
    {
        Map<String,String> parameters = url.getParameters();
        if (parameters != null) {
            String booleanString = parameters.get(methodName + "." + IS_ASYNC);
            return booleanString != null;
        }
        return true;
    }

    public static CallbackInfo getCallbackInfo(URL url, String methodName)
    {
        Map<String,String> parameters = url.getParameters();
        if (parameters != null) {
            String booleanString = parameters.get(methodName + "." + IS_CALLBACK);
            if (booleanString == null)
                return null;
            int callbackIndex = Integer.parseInt(parameters.get(methodName + "." + CALLBACK_ARG_INDEX));
            String callbackInterfaceName = parameters.get(methodName + "." + CALLBACK_ARG_INTERFACE);
            try {
                Class<?> callbackInterface = Class.forName(callbackInterfaceName);
                return new CallbackInfo(callbackInterface, callbackIndex);
            } catch (ClassNotFoundException e) {
                throw new RPCFrameworkException("callback interface with name " + callbackInterfaceName + " not found");
            }
        }
        return null;
    }
}
