package com.my.baffinrpc.demo.server.impl;


import com.my.baffinrpc.demo.api.ExceptionService;

import java.io.IOException;

public class ExceptionServiceImpl implements ExceptionService {
    @Override
    public String sayHiAndThrowException() {
        System.err.println("will throw exception");
        throw new RuntimeException("to busy to greet");
    }

    @Override
    public String sayHiAndThrowCheckedException() throws IOException {
        System.err.println("will throw io exception");
        throw new IOException("io problem during say hi");
    }
}
