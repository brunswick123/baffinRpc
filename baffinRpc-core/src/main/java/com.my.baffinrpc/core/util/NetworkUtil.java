package com.my.baffinrpc.core.util;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtil {
    public static String getLocalHostAddress()
    {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RPCFrameworkException(e);
        }
    }
}
