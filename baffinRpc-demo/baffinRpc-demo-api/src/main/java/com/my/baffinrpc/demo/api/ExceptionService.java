package com.my.baffinrpc.demo.api;

import java.io.IOException;

public interface ExceptionService {
    String sayHiAndThrowException();
    String sayHiAndThrowCheckedException() throws IOException;
}
