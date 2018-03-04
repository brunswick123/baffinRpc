package com.my.baffinrpc.demo.server.impl;

import com.my.baffinrpc.demo.api.HelloService;
import com.my.baffinrpc.demo.api.model.Person;

import java.util.Date;

public class HelloServiceImpl implements HelloService {
    public String hello(String name, int number) {
        System.err.println("greeting from " + name);
        return "hello " + name + ", with number: " + number;
    }

    public String hiPerson(Person person) {
        return person.toString();
    }
}
